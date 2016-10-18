package im.vinci.website.persistence;

import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import im.vinci.VinciApplication;
import im.vinci.website.config.UserProfile;
import im.vinci.website.domain.admin.*;
import im.vinci.website.domain.admin.AdminUserInfo;
import org.hamcrest.core.IsNull;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * Created by tim@vinci on 16/1/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {VinciApplication.class})
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles(UserProfile.UNIT_TEST)
@Rollback(true)
@Transactional

public class AdminMapperTest {

    @Autowired
    private AdminUsersInfoMapper adminUsersInfoMapper;

    @Autowired
    private AdminUserGroupMapper adminUserGroupMapper;

    @Autowired
    private AdminGroupMapper adminGroupMapper;

    @Autowired
    private AdminAcclMapper adminAcclMapper;

    @Autowired
    private AdminActionLogMapper adminActionLogMapper;

    @Test
    public void testAdminUserMapper() {
        AdminUserInfo user = new AdminUserInfo();
        user.setEmail("mm@vinci.im");
        user.setFullName("wangzhe");
        user.setSalt("abcd");
        user.setPassword("yyyyy");
        long num = adminUsersInfoMapper.insertAdminUser(user);
        assertThat(num,greaterThan(0L));
        long id = user.getId();
        assertThat(adminUsersInfoMapper.getUserById(id).getId(),is(id) );
        assertThat(adminUsersInfoMapper.getUserById(id).isFreeze(),is(false) );
        assertThat(adminUsersInfoMapper.getUserByEmail("mm@vinci.im").getId(),is(id) );

        assertThat(adminUsersInfoMapper.freezeUser(id),is(1));
        assertThat(adminUsersInfoMapper.getUserById(id).isFreeze(),is(true) );
    }


    @Test
    public void testAdminUserGroupMapper() {
        assertThat(adminUserGroupMapper.addUser2Group(1, Longs.asList(1,2,3,4)),is(4));
        List<AdminUserGroupBean> list = adminUserGroupMapper.getUserGroupsByUserId(1);
        assertThat(list.size(),is(4));
        assertThat(adminUserGroupMapper.removeUser4Group(1,3),is(1));
        list = adminUserGroupMapper.getUserGroupsByUserId(1);
        assertThat(list.size(),is(3));
        List<Long> l = Lists.transform(list, input -> input.getGroupId());
        assertThat(l,contains(1L,2L,4L));
    }

    @Test
    public void testAdminGroupMapper() {
        AdminGroupBean bean = new AdminGroupBean();
        bean.setGroupName("abcd");
        bean.setDesc("yyyyydesc");
        assertThat(adminGroupMapper.insertGroup(bean),is(1));
        assertThat(adminGroupMapper.getGroupsById(bean.getId()).getGroupName(),is("abcd"));
        assertThat(adminGroupMapper.deleteGroup(bean.getId()),is(1));
        assertThat(adminGroupMapper.getGroupsById(bean.getId()), IsNull.nullValue());
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testAdminAcclMapper() {
        AdminACCLBean b1 = new AdminACCLBean();
        b1.setGroupId(1);
        b1.setResource("/admin/**/*");
        b1.setAccessType("allow");
        assertThat(adminAcclMapper.addAccl(b1),is(1));

        b1.setAccessType("notAllow");
        thrown.expect(DataIntegrityViolationException.class);
        thrown.expectMessage(allOf(containsString("access_type"),containsString("Data truncated for column")));
        adminAcclMapper.addAccl(b1);

        assertThat(adminAcclMapper.getAcclById(b1.getId()).getResource(),is("/admin/**/*"));
        b1.setAccessType("deny");
        adminAcclMapper.addAccl(b1);
        List<AdminACCLBean> list = adminAcclMapper.getAcclsByGroupId(Lists.newArrayList(1L));
        assertThat(Lists.transform(list, AdminACCLBean::getAccessType), contains("allow","deny"));
    }

    @Test
    public void testAdminActionLog() {

        AdminActionLogBean bean = new AdminActionLogBean();
        bean.setUserId(1);
        bean.setBody("abcd");
        bean.setActionType("abc");
        assertThat(adminActionLogMapper.addAdminActionLog(bean),is(1));
        assertThat(adminActionLogMapper.listAdminActionLogs(1, DateTime.parse("2016-1-1T00:00:00").toDate(),DateTime.now().toDate()),hasSize(1));
    }
}
