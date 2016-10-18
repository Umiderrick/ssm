package im.vinci.website.domain.order;

import com.google.common.collect.Lists;
import im.vinci.website.domain.user.UserInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户订单类
 * Created by tim@vinci on 16/1/11.
 */
public class UserOrder implements Serializable{
    private long id;
    private int version;
    private long orderId;
    private long userId;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private String paymentMethod;
    private BigDecimal totalPayment;
    private BigDecimal actTotalPayment;
    private String discountCode;
    private UserOrderAddress address;
    private Date dtCreate;
    private Date dtUpdate;
    private boolean isInvoiced;

    private UserInfo userInfo = null;
    private List<UserOrderSpec> userOrderSpecList = Lists.newArrayList();
    private Invoice invoice = null;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimal getActTotalPayment() {
        return actTotalPayment;
    }

    public void setActTotalPayment(BigDecimal actTotalPayment) {
        this.actTotalPayment = actTotalPayment;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public UserOrderAddress getAddress() {
        return address;
    }

    public void setAddress(UserOrderAddress address) {
        this.address = address;
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

    public boolean isInvoiced() {
        return isInvoiced;
    }

    public void setInvoiced(boolean invoiced) {
        isInvoiced = invoiced;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<UserOrderSpec> getUserOrderSpecList() {
        return userOrderSpecList;
    }

    public void setUserOrderSpecList(List<UserOrderSpec> userOrderSpecList) {
        this.userOrderSpecList = userOrderSpecList;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
