package im.vinci.website.domain.order;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发票类
 * Created by tim@vinci on 16/1/11.
 */
public class Invoice implements Serializable{
    private long id;
    private long orderId;
    private long userId;
    /**
     * 0:无 1:个人 2:企业
     */
    private String category;
    //抬头
    private String title;
    //开票金额,同订单实际价格，冗余数据
    private BigDecimal price;

    //以下是增值税专用发票,暂时不启用
    // 专票： 税号
    private String taxNum;
    //专票：公司注册地址
    private String address;
    //专票：注册电话
    private String phoneNum;
    //专票：开户行
    private String bank;
    //专票：账号
    private String bankAccountNumber;

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTaxNum() {
        return taxNum;
    }

    public void setTaxNum(String taxNum) {
        this.taxNum = taxNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }
}
