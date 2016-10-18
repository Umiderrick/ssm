package im.vinci.website.domain.dto.respoonse;

import java.util.Date;
import java.util.List;

/**
 * Created by mlc on 2016/1/14.
 */
public class OrderResponse {
    private String order_status;
    private Date dt_create;
    private String product_id;
    private String product_color;
    private String order_id;
    private String product_count;
    private String phonenum;
    private String special_need;
    private Date   delivery_date;
    private String status;
    private String address;
    private String ship_from;
    private Date   ship_date;
    private String invoice_catagory;
    private String invoice_title;
    private String express;
    private String payment_method;
    private String payment_status;
    private String act_total_payment;
    private String payment_id;
    private List<OrderLog> log_lis;
    private String remark;

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public Date getDt_create() {
        return dt_create;
    }

    public void setDt_create(Date dt_create) {
        this.dt_create = dt_create;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_color() {
        return product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_count() {
        return product_count;
    }

    public void setProduct_count(String product_count) {
        this.product_count = product_count;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getSpecial_need() {
        return special_need;
    }

    public void setSpecial_need(String special_need) {
        this.special_need = special_need;
    }

    public Date getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(Date delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShip_from() {
        return ship_from;
    }

    public void setShip_from(String ship_from) {
        this.ship_from = ship_from;
    }

    public Date getShip_date() {
        return ship_date;
    }

    public void setShip_date(Date ship_date) {
        this.ship_date = ship_date;
    }

    public String getInvoice_catagory() {
        return invoice_catagory;
    }

    public void setInvoice_catagory(String invoice_catagory) {
        this.invoice_catagory = invoice_catagory;
    }

    public String getInvoice_title() {
        return invoice_title;
    }

    public void setInvoice_title(String invoice_title) {
        this.invoice_title = invoice_title;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getAct_total_payment() {
        return act_total_payment;
    }

    public void setAct_total_payment(String act_total_payment) {
        this.act_total_payment = act_total_payment;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public List<OrderLog> getLog_lis() {
        return log_lis;
    }

    public void setLog_lis(List<OrderLog> log_lis) {
        this.log_lis = log_lis;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
