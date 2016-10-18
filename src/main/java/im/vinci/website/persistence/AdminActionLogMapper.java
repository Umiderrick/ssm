package im.vinci.website.persistence;

import im.vinci.website.domain.admin.AdminActionLogBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * admin action log
 * Created by tim@vinci on 16/1/11.
 */
@Repository
public interface AdminActionLogMapper {
    @Insert("insert into admin_action_log (user_id,action_type,body,dt_create) values (#{userId},#{actionType},#{body},now())")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int addAdminActionLog(AdminActionLogBean bean);

    @Select({"<script>",
            "SELECT * FROM admin_action_log",
            "WHERE dt_create between #{fromDate} and #{toDate}",
            "<if test='userId>0'>",
            "and user_id=#{userId}",
            "</if>",
            "</script>"})
    List<AdminActionLogBean> listAdminActionLogs(@Param("userId") long userId , @Param("fromDate") Date from,@Param("toDate") Date to);

}
