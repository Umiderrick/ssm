package im.vinci.website.domain.admin;

import im.vinci.website.domain.admin.AdminACCLBean;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;

import java.util.List;

/**
 * admin 用户的类
 * Created by tim@vinci on 16/1/8.
 */
public class AdminUser {
    private long userid;
    private String email;
    private String fullName;
    private boolean isFreeze;
    private List<AdminACCLBean> acclList;

    private final static AntPathMatcher matcher = new AntPathMatcher();

    public AdminUser(long userid) {
        this.userid = userid;
    }

    public boolean isAllow(String urlPath) {
        if (isFreeze) {
            return false;
        }
        if (CollectionUtils.isEmpty(acclList)) {
            return false;
        }
        boolean isAllow = false;
        for (AdminACCLBean accl : acclList) {
            if (accl != null && matcher.match(accl.getResource(),urlPath)) {
                if ("allow".equalsIgnoreCase(accl.getAccessType())) {
                    isAllow = true;
                } else {
                    return false;
                }
            }
        }
        return isAllow;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<AdminACCLBean> getAcclList() {
        return acclList;
    }

    public void setAcclList(List<AdminACCLBean> acclList) {
        this.acclList = acclList;
    }

    public static PathMatcher getMatcher() {
        return matcher;
    }

    public boolean isFreeze() {
        return isFreeze;
    }

    public void setFreeze(boolean freeze) {
        isFreeze = freeze;
    }
}
