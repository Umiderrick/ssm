package im.vinci.website.utils;

import im.vinci.website.domain.admin.AdminUser;
import im.vinci.website.domain.user.UserInfo;

public class UserContext {
    private static final ThreadLocal<UserInfo> userInfoLocal = new ThreadLocal<UserInfo>();
    private static final ThreadLocal<AdminUser> adminUserLocal = new ThreadLocal<AdminUser>();

    public static void setUserInfo(UserInfo userInfo) {
        userInfoLocal.set(userInfo);
    }

    public static UserInfo getUserInfo() {
        return userInfoLocal.get();
    }

    public static void setAdminUser(AdminUser adminUser) {
        adminUserLocal.set(adminUser);
    }

    public static AdminUser getAdminUser() {
        return adminUserLocal.get();
    }
}