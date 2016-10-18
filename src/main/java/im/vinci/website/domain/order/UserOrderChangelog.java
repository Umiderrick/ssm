package im.vinci.website.domain.order;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * Created by tim@vinci on 16/1/11.
 */
public class UserOrderChangelog implements Serializable{
    private long id;
    private long orderId;
    //user or admin
    private String operatorType;
    //用户 or 管理员xxx
    private String operator;
    //操作类型
    private String actionType;
    private String body;
    private Date dtCreate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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
