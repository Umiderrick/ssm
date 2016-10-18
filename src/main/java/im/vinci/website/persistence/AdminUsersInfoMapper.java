package im.vinci.website.persistence;

import im.vinci.website.domain.admin.AdminUserInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * admin users的dao
 * Created by tim@vinci on 16/1/8.
 */
@Repository
public interface AdminUsersInfoMapper {
    @Select("select * from admin_user where id=#{id}")
    AdminUserInfo getUserById(@Param("id")long id);

    @Select("select * from admin_user where email=#{email}")
    AdminUserInfo getUserByEmail(@Param("email")String email);

    @Select("select * from admin_user")
    List<AdminUserInfo> listAdminUsers();

    @Insert("insert into admin_user (email,full_name,password,salt,dt_create) values (#{email},#{fullName},#{password},#{salt},now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    long insertAdminUser(AdminUserInfo user);

    @Update("update admin_user set salt=#{salt},password=#{password} where email=#{email}")
    int updateUserPassword(@Param("email")String email, @Param("salt")String salt, @Param("password")String password);

    @Update("update admin_user set is_freeze = 1 where id=#{id}")
    int freezeUser(@Param("id") long id);
}
