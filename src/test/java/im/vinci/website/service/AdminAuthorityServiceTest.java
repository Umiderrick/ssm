package im.vinci.website.service;

import com.google.common.collect.Sets;
import com.google.common.primitives.Longs;
import im.vinci.VinciApplication;
import im.vinci.website.config.UserProfile;
import im.vinci.website.domain.admin.AdminACCLBean;
import im.vinci.website.domain.admin.AdminGroupBean;
import im.vinci.website.domain.admin.AdminUser;
import im.vinci.website.domain.admin.AdminUserInfo;
import im.vinci.website.exception.ErrorCode;
import im.vinci.website.exception.VinciException;
import im.vinci.website.utils.UserContext;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by tim@vinci on 16/1/12.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {VinciApplication.class})
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles(UserProfile.UNIT_TEST)
//@Rollback(true)
//@Transactional
public class AdminAuthorityServiceTest {

    private static final ResourceLoader resourceLoader = new DefaultResourceLoader();
    private static final ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminAuthorityService adminAuthorityService;

    @BeforeClass
    public static void beforeClass() {
        databasePopulator.addScript(resourceLoader.getResource("/database_test.sql"));
    }

    @Before
    public void beforeTest() {
        databasePopulator.execute(dataSource);
        AdminUser user = new AdminUser(10);
        user.setEmail("tim@vinci.im");
        user.setFullName("tim");
        UserContext.setAdminUser(user);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testInserAdminUser() {
        AdminUserInfo user = new AdminUserInfo();
        user.setFullName("wangzhe");
        user.setPassword("123456");
        user.setEmail("tim@vinci.im");
        try {
            assertThat(adminAuthorityService.insertAdminUser(user), is(true));
        }catch (VinciException e) {
            assertThat(e.getErrorCode(), is(ErrorCode.ARGUMENT_ERROR));
            assertThat(e.getErrorMsgToUser(),is("密码位数小于8位"));
        }
        user.setPassword("12345678");
        assertThat(adminAuthorityService.insertAdminUser(user), is(true));

        assertThat(adminAuthorityService.checkAndGetUserByUserPasswd("tim@vinci.im","12345678"), IsNull.notNullValue(AdminUser.class));

        try {
            adminAuthorityService.checkAndGetUserByUserPasswd("tim@vinci.im", "1234567");
        }catch (VinciException e) {
            assertThat(e.getMessage(),containsString("password error"));
        }
    }

    @Test
    public void testAddGroupAndAccl() {
        AdminGroupBean groupBean = new AdminGroupBean();
        groupBean.setGroupName("gropuname");
        groupBean.setDesc("groupname desc");
        assertThat(adminAuthorityService.addAdminGroup(groupBean),is(true));
        try {
            adminAuthorityService.addAdminGroup(groupBean);
        }catch (VinciException e) {
            assertThat(e.getMessage(),containsString("has exist"));
        }

        AdminACCLBean accl = new AdminACCLBean();
        accl.setGroupId(1000);
        accl.setAccessType("allow");
        accl.setResource("/abcd");
        try {
            adminAuthorityService.addAccl(accl);
        }catch (VinciException e) {
            assertThat(e.getMessage(),containsString("is't exist"));
        }

        accl.setGroupId(groupBean.getId());
        assertThat(adminAuthorityService.addAccl(accl),is(true));

        List<AdminGroupBean> groups = adminAuthorityService.listAdminGroups();

        assertThat(groups,hasSize(1));
        assertThat(groups.get(0).getGroupName(),is("gropuname"));

        List<AdminACCLBean> accls = adminAuthorityService.listAcclsByGroup(groupBean.getId());
        assertThat(accls,hasSize(1));
        assertThat(accls.get(0).getResource(),is("/abcd"));
    }

    @Test
    public void testAddUserAndDelGroup() {
        testInserAdminUser();
        testAddGroupAndAccl();
        List<AdminUser> users = adminAuthorityService.listAdminUsers();
        assertThat(users,hasSize(1));
        AdminUser user = users.get(0);

        AdminGroupBean group = adminAuthorityService.listAdminGroups().get(0);

        assertThat(adminAuthorityService.addUsers2Group(group.getId(), Sets.newHashSet(user.getUserid()))
                ,is(1));
        assertThat(adminAuthorityService.listAdminGroups().get(0).getUserCount(),is(1));
        assertThat(adminAuthorityService.checkAndGetUserByUserPasswd("tim@vinci.im","12345678").getAcclList(),hasSize(1));

        try {
            adminAuthorityService.deleteAdminGroup(group.getId());
        }catch (VinciException e) {
            assertThat(e.getMessage(),containsString("has user"));
        }

        assertThat(adminAuthorityService.removeUsers2Group(group.getId(),user.getUserid()),is(1));
        assertThat(adminAuthorityService.deleteAdminGroup(group.getId()),is(true));
    }
}
