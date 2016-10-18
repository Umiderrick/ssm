package im.vinci.website.domain.order;

/**
 * Created by tim@vinci on 16/1/11.
 */
public enum PaymentStatus {
    NotPay("未支付"),
    Paid("已支付"),
    Refund("已退款");

    private String desc;
    PaymentStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
