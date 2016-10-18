package im.vinci.website.domain.dto.respoonse;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mlc on 2016/1/14.
 */
public class OrderLog implements Serializable{
    private String modified_by;
    private Date date;
    private String detail;
    private String departmentId;

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}
