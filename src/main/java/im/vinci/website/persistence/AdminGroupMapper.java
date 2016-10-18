package im.vinci.website.persistence;

import im.vinci.website.domain.admin.AdminGroupBean;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * admin group mapper
 * Created by tim@vinci on 16/1/8.
 */
@Repository
public interface AdminGroupMapper {
    @Select("select * from admin_authority_group where id=#{id}")
    AdminGroupBean getGroupsById(@Param("id")long id);

    @Select("select * from admin_authority_group")
    List<AdminGroupBean> listAdminGroups();

    @Insert("insert ignore into admin_authority_group (group_name,`desc`,dt_create) values (#{groupName},#{desc},now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int insertGroup(AdminGroupBean bean);

    @Update("update admin_authority_group set user_count=user_count+#{delta} where id=#{id}")
    int updateAdminGroupUserCount(@Param("id") long id, @Param("delta")int delta);

    @Delete("delete from admin_authority_group where id=#{id} and user_count<=0")
    int deleteGroup(@Param("id") long id);
}
