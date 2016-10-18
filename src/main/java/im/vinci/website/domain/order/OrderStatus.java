package im.vinci.website.domain.order;

/**
 * Created by tim@vinci on 16/1/11.
 */
public enum OrderStatus {
    Start("订单生成"), //下订单
    Confirm("订单确认(已支付)"), //支付完成转到这个状态
    Making("制作中"),
    Sent("已发货"),
    Received("已收货"),
    Cancel("订单取消"),
    ApplyReturn("申请退货"),
    AgreeReturn("同意退货"),
    ReturnSuccess("退货成功");

    private String desc;

    OrderStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
