package im.vinci.website.domain.admin;

import java.util.Date;

/**
 *      `id` bigint(20) NOT NULL AUTO_INCREMENT,
        `resource` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '资源,支持通配符*',
        `group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '组id',
        `access_type` enum('allow','deny') COLLATE utf8mb4_bin DEFAULT 'allow' COMMENT '限制类型',
        `dt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
 * mapper: admin_accl
 * Created by tim@vinci on 16/1/8.
 */
public class AdminACCLBean {
    private long id;
    private String resource;
    private long groupId;
    // allow or deny
    private String accessType;
    private Date dtCreate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public Date getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(Date dtCreate) {
        this.dtCreate = dtCreate;
    }
}
