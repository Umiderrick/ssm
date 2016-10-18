package im.vinci.website.domain.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by mlc on 2016/1/14.
 */
public class OrderRequest {
    private List<String> order_status;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date from_create_date;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date to_create_date;
    private List<String> product_id;
    private String special_need;
    private Date delivery_date;
    private List<String> status;
    private List<String> ship_from;
    private Date ship_date;
    private List<String> invoice_category;
    private List<String> express_status;
    private List<String> payment_method;

    public List<String> getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(List<String> payment_method) {
        this.payment_method = payment_method;
    }

    public List<String> getExpress_status() {
        return express_status;
    }

    public void setExpress_status(List<String> express_status) {
        this.express_status = express_status;
    }

    public List<String> getInvoice_category() {
        return invoice_category;
    }

    public void setInvoice_category(List<String> invoice_category) {
        this.invoice_category = invoice_category;
    }

    public Date getShip_date() {
        return ship_date;
    }

    public void setShip_date(Date ship_date) {
        this.ship_date = ship_date;
    }

    public List<String> getShip_from() {
        return ship_from;
    }

    public void setShip_from(List<String> ship_from) {
        this.ship_from = ship_from;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public Date getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(Date delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getSpecial_need() {
        return special_need;
    }

    public void setSpecial_need(String special_need) {
        this.special_need = special_need;
    }

    public List<String> getProduct_id() {
        return product_id;
    }

    public void setProduct_id(List<String> product_id) {
        this.product_id = product_id;
    }

    public Date getTo_create_date() {
        return to_create_date;
    }

    public void setTo_create_date(Date to_create_date) {
        this.to_create_date = to_create_date;
    }

    public Date getFrom_create_date() {
        return from_create_date;
    }

    public void setFrom_create_date(Date from_create_date) {
        this.from_create_date = from_create_date;
    }

    public List<String> getOrder_status() {
        return order_status;
    }

    public void setOrder_status(List<String> order_status) {
        this.order_status = order_status;
    }
}
