package im.vinci.website.domain.admin;

import java.io.Serializable;

/**
 * mapper : admin_user_group
 * Created by tim@vinci on 16/1/8.
 */
public class AdminUserGroupBean implements Serializable{
    private long id;
    private long userId;
    private long groupId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}
