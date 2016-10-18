package im.vinci.website.persistence;

import im.vinci.website.domain.admin.AdminUserInfo;
import im.vinci.website.domain.order.UserOrder;
import im.vinci.website.domain.order.UserOrderAddress;
import im.vinci.website.domain.user.UserInfo;
import im.vinci.website.persistence.handler.UserOrderAddressHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 普通用户类
 * Created by tim@vinci on 16/1/12.
 */
@Repository
public interface UserInfoMapper {

    @Select("select * from userinfo where id=#{id}")
    UserInfo getUserById(@Param("id")long id);

    @Select({"<script>",
            "select * from userinfo where id in",
            "<foreach item='item' index='index' collection='ids'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>"})
    List<UserInfo> getUserInfoByIds(@Param("ids")Iterable<Long> ids);

    @Select("select * from userinfo where phonenum=#{phone}")
    UserInfo getUserByPhone(@Param("phone")String phone);

    @Update("update userinfo set phone_validate_code=#{code} where phonenum=#{phoneNum}")
    int updateLastPhoneValidateCode(@Param("phoneNum")String phoneNum, @Param("code")String code);

}
