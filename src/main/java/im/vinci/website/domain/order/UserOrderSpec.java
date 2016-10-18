package im.vinci.website.domain.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户订单中的实际商品
 * Created by tim@vinci on 16/1/11.
 */
public class UserOrderSpec implements Serializable{
    private long id;
    private long orderId;
    private long productId;
    private BigDecimal productPrice;
    private int productCount;
    private ProductSpec productSnapshot;
    private BigDecimal actPayment;
    private String discountCode;
    //之前用作的镭雕,只有一个字符串
    private String remark;
    private Date dtCreate;
    private Date dtUpdate;

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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public ProductSpec getProductSnapshot() {
        return productSnapshot;
    }

    public void setProductSnapshot(ProductSpec productSnapshot) {
        this.productSnapshot = productSnapshot;
    }

    public BigDecimal getActPayment() {
        return actPayment;
    }

    public void setActPayment(BigDecimal actPayment) {
        this.actPayment = actPayment;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
