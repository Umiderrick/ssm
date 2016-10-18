package im.vinci.website.persistence;

import im.vinci.website.domain.order.UserOrderChangelog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单更改记录
 CREATE TABLE `user_order_changelog` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT,
 `order_id` bigint(20) NOT NULL DEFAULT '0',
 `operator_type` varchar(16) COLLATE utf8mb4_bin NOT NULL COMMENT '操作人类型user/admin...',
 `operator` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '操作人,例如:用户 管理员xxx',
 `action_type` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '操作类型,例如modify_address,comment',
 `body` text COLLATE utf8mb4_bin NOT NULL COMMENT '实际操作内容',
 `dt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (`id`),
 UNIQUE KEY `unique_order_id` (`order_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin
 * Created by tim@vinci on 16/1/12.
 */
@Repository
public interface UserOrderChangelogMapper {

    @Insert({"insert into user_order_changelog (order_id,operator_type,operator,action_type,body,dt_create) values ",
            "(#{orderId},#{operatorType},#{operator},#{actionType},#{body},now())"
    })
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insertOrderChangeLog(UserOrderChangelog log);

    @Select("select * from user_order_changelog where order_id=#{orderId} and version=#{version}")
    UserOrderChangelog getUserOrderChangelogByOrderIdVersion(@Param("orderId") long orderId, @Param("version") int version);

    @Select("select * from user_order_changelog where order_id=#{orderId}")
    List<UserOrderChangelog> getUserOrderChangelogByOrderId(@Param("orderId")long orderId);
}
