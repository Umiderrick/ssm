package im.vinci.website.persistence;

import im.vinci.website.domain.admin.AdminUserGroupBean;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

/**
 * admin group mapper
 * Created by tim@vinci on 16/1/8.
 */
@Repository
public interface AdminUserGroupMapper {
    @Select("select * from admin_user_group where user_id=#{userId}")
    List<AdminUserGroupBean> getUserGroupsByUserId(long userId);

    @Insert({"<script>",
            "insert ignore into admin_user_group (group_id,user_id) values ",
            "<foreach item='item' index='index' collection='userIds' separator=','>",
            "(#{groupId},#{item})",
            "</foreach>",
            "</script>"})
    int addUser2Group(@Param("groupId")long groupId, @Param("userIds")Iterable<Long> userIds );

    @Delete("delete from admin_user_group where group_id=#{groupId} and user_id=#{userId}")
    int removeUser4Group(@Param("userId")long userId, @Param("groupId") long groupId);
}
