package im.vinci.website.domain.admin;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Date;

/**
 * mapper: admin_authority_group
 * Created by tim@vinci on 16/1/8.
 */
public class AdminGroupBean implements Serializable{
    private long id;
    private String groupName;
    private String desc;
    private int userCount;
    private Date dtCreate;
    private Date dtUpdate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public Date getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(Date dtCreate) {
        this.dtCreate = dtCreate;
    }

    public Date getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(Date dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminGroupBean groupBean = (AdminGroupBean) o;
        return id == groupBean.id &&
                Objects.equal(groupName, groupBean.groupName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, groupName);
    }
}
