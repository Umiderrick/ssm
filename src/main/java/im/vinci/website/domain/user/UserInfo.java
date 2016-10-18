package im.vinci.website.domain.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 普通用户
 * Created by tim@vinci on 16/1/12.
 */
public class UserInfo implements Serializable{
    private long id;
    private String phonenum;
    private String phoneValidateCode;
    private Date dtCreate;
    private Date dtUpdate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getPhoneValidateCode() {
        return phoneValidateCode;
    }

    public void setPhoneValidateCode(String phoneValidateCode) {
        this.phoneValidateCode = phoneValidateCode;
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
}
