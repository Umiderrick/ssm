package im.vinci.website.persistence;

import im.vinci.website.domain.admin.AdminACCLBean;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by tim@vinci on 16/1/8.
 */
@Repository
    public interface AdminAcclMapper {

        @Select({"<script>",
                "SELECT *",
                "FROM admin_accl",
                "WHERE group_id IN",
                "<foreach item='item' index='index' collection='list'",
                "open='(' separator=',' close=')'>",
                "#{item}",
                "</foreach>",
                "</script>"})
        List<AdminACCLBean> getAcclsByGroupId(@Param("list")List<Long> groupIds);

        @Select("select * from admin_accl where id=#{id}")
        AdminACCLBean getAcclById(@Param("id")long id);

        @Insert("insert into admin_accl (resource,group_id,access_type,dt_create) values (#{resource},#{groupId},#{accessType},now())")
        @Options(useGeneratedKeys = true, keyColumn = "id")
        int addAccl(AdminACCLBean bean);

        @Delete({"<script>",
           "delete FROM admin_accl",
        "WHERE group_id=#{groupId} and id IN",
        "<foreach item='item' index='index' collection='list'",
        "open='(' separator=',' close=')'>",
        "#{item}",
        "</foreach>",
        "</script>"})
        int deleteAccls(@Param("groupId")long groupId, @Param("list") List<Long> ids);

        }
