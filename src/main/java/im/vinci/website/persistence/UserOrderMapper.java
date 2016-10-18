package im.vinci.website.persistence;

import im.vinci.website.domain.order.*;
import im.vinci.website.persistence.handler.UserOrderAddressHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 用户订单表
 * Created by tim@vinci on 16/1/11.
 */
@Repository
public interface UserOrderMapper {

    @Select("select * from user_order where order_id=#{orderId}")
    @Results({
            @Result(column = "address", property = "address", javaType = UserOrderAddress.class, jdbcType = JdbcType.VARCHAR, typeHandler = UserOrderAddressHandler.class)
    })
    UserOrder getUserOrderByOrderId(@Param("orderId")long orderId);

    @Select({"<script>",
            "select * from user_order where order_id in",
            "<foreach item='item' index='index' collection='orderIds'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    @Results({
            @Result(column = "address", property = "address", javaType = UserOrderAddress.class, jdbcType = JdbcType.VARCHAR, typeHandler = UserOrderAddressHandler.class)
    })
    List<UserOrder> getUserOrderByOrderIds(@Param("orderIds")Iterable<Long> orderIds);

    @Select("select order_id from user_order where order_status=#{orderStatus} and dt_create between ${from} and ${to}")
    List<Long> listUserOrdersByOrderStatus(@Param("orderStatus") OrderStatus orderStatus,@Param("from")Date from, @Param("to")Date to);

    @Update("update user_order set order_status=#{status} ,version=version+1 where order_id=#{order_id} and version=#{version}")
    int updateUserOrderStatus(@Param("orderId")long orderId, @Param("status")OrderStatus status, @Param("version")int version);

    @Update("update user_order set payment_status=#{status} ,version=version+1 where order_id=#{order_id} and version=#{version}")
    int updateUserPaymentStatus(@Param("orderId")long orderId, @Param("paymentStatus")PaymentStatus status,@Param("version")int version);

    @Insert({
            "insert into user_order",
            "(version,order_id,user_id,order_status,payment_status,payment_method,total_payment,act_total_payment,discount_code,address,dt_create,is_invoiced)",
            "values",
            "(1,#{orderId},#{userId},#{orderStatus},#{paymentStatus},#{paymentMethod},#{totalPayment},#{actTotalPayment},#{discountCode},",
            "#{address,typeHandler=im.vinci.website.persistence.handler.UserOrderAddressHandler},now(),#{isInvoiced})"
    })
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insertUserOrder(UserOrder order);

    @Select("select * from user_order_spec where order_id=#{orderId}")
    List<UserOrderSpec> getUserOrderSpec(@Param("orderId")long orderId);

    @Select({"<script>",
            "select * from user_order_spec where order_id in",
            "<foreach item='item' index='index' collection='orderIds'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    List<UserOrderSpec> getUserOrderSpecs(@Param("orderIds")Iterable<Long> orderIds);

    @Select("select * from invoice where order_id=#{orderId}")
    Invoice getUserOrderInvoice(@Param("orderId")long orderId);

    @Select({"<script>",
            "select * from invoice where order_id in",
            "<foreach item='item' index='index' collection='orderIds'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    List<Invoice> getUserOrderInvoices(@Param("orderIds")Iterable<Long> orderIds);
}
