package im.vinci.website.domain.admin;

import java.io.Serializable;
import java.util.Date;

/**
 * 后台操作记录
     `id` bigint(20) NOT NULL AUTO_INCREMENT,
     `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '操作人',
     `action_type` varchar(64) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '操作类型，例如modify_address',
     `body` text COLLATE utf8mb4_bin NOT NULL COMMENT '具体内容，json格式，{address:"origin",address_to:"target"',
     `dt_create` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * Created by tim@vinci on 16/1/11.
 */
public class AdminActionLogBean implements Serializable{
    private long id;
    private long userId;
    private String actionType;
    private String body;
    private Date dtCreate;

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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(Date dtCreate) {
        this.dtCreate = dtCreate;
    }
}
