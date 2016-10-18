package im.vinci.website.domain.wrapper.admin;

import com.google.common.collect.Lists;
import im.vinci.VinciApplication;
import im.vinci.website.config.UserProfile;
import im.vinci.website.domain.admin.AdminACCLBean;
import im.vinci.website.domain.admin.AdminUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;
/**
 * admin user test
 * Created by tim@vinci on 16/1/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {VinciApplication.class})
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles(UserProfile.INTG)
public class AdminUserTest {

    @Test
    public void testMatcher() {
        AdminUser adminUser = new AdminUser(1L);
        AdminACCLBean a1 = new AdminACCLBean();
        a1.setResource("/admin/**/*.json");
        a1.setAccessType("allow");
        adminUser.setAcclList(Lists.newArrayList(a1));
        assertTrue(adminUser.isAllow("/admin/a.json"));
        assertTrue(adminUser.isAllow("/admin/a/b.json"));
        assertFalse(adminUser.isAllow("/admin/a"));
        assertFalse(adminUser.isAllow("/add/a.json"));

        AdminACCLBean a2 = new AdminACCLBean();
        a2.setResource("/admin/**/a.json");
        a2.setAccessType("deny");
        adminUser.setAcclList(Lists.newArrayList(a1,a2));
        assertFalse(adminUser.isAllow("/admin/a.json"));
        assertTrue(adminUser.isAllow("/admin/a/b.json"));

        AdminACCLBean a3 = new AdminACCLBean();
        a3.setResource("/admin/a/b.json");
        a3.setAccessType("deny");
        adminUser.setAcclList(Lists.newArrayList(a1,a2,a3));
        assertFalse(adminUser.isAllow("/admin/a.json"));
        assertFalse(adminUser.isAllow("/admin/a/b.json"));

        a1.setResource("/**/*");
        adminUser.setAcclList(Lists.newArrayList(a1));
        assertTrue(adminUser.isAllow("/admin/a.json"));
        assertTrue(adminUser.isAllow("/admin/a/b.json"));
        assertTrue(adminUser.isAllow("/admin/a"));
        assertTrue(adminUser.isAllow("/add/a.json"));

    }
}
