package im.vinci.website.persistence.handler;

import im.vinci.website.domain.order.UserOrderAddress;
import im.vinci.website.utils.JsonUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

/**
 * db handler
 * Created by tim@vinci on 16/1/11.
 */
public class UserOrderAddressHandler implements TypeHandler<UserOrderAddress> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, UserOrderAddress node, JdbcType jdbcType)
            throws SQLException {
        if (node == null) {
            preparedStatement.setNull(i, Types.VARCHAR);
        } else {
            preparedStatement.setString(i, JsonUtils.encode(node));
        }
    }

    @Override
    public UserOrderAddress getResult(ResultSet resultSet, String s) throws SQLException {
        String str = resultSet.getString(s);
        if (str == null) {
            return null;
        }
        return JsonUtils.decode(str, UserOrderAddress.class);
    }

    @Override
    public UserOrderAddress getResult(ResultSet resultSet, int i) throws SQLException {
        String str = resultSet.getString(i);
        if (str == null) {
            return null;
        }
        return JsonUtils.decode(str, UserOrderAddress.class);
    }

    @Override
    public UserOrderAddress getResult(CallableStatement callableStatement, int i) throws SQLException {
        String str = callableStatement.getString(i);
        if (str == null) {
            return null;
        }
        return JsonUtils.decode(str, UserOrderAddress.class);
    }
}
