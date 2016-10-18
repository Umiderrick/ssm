package im.vinci.website.exception;

/**
 * 所有的error code定义
 * Created by tim@vinci on 16/1/6.
 */
public class ErrorCode {
    // Internal server error
    public static final Integer INTERNAL_SERVER_ERROR = 500;

    //基础错误类型
    //参数错误
    public static final int ARGUMENT_ERROR = 100001;
    //订单不存在
    public static final int ORDER_NOT_EXIST = 100002;
    // Authentication error code

    //需要登录
    public static final int NEED_LOGIN = 101001;
    //未授权(权限)
    public static final int UNAUTHORIZED = 101002;
    //登录失败,用户不存在或密码错误
    public static final int LOGIN_FAILED = 101003;

    //admin some error
    //admin group name已经存在
    public static final int ADMIN_GROUP_NAME_EXIST = 102001;
    //group 中还有user存在,不能删除
    public static final int ADMIN_GROUP_HAS_USERS = 102002;
    public static final int ADMIN_USER_NOT_EXIST = 102003;
    //group不存在
    public static final int ADMIN_GROUP_NOT_EXIST = 1020010;
    public static final int ADMIN_ACCL_RESOURCE_IS_EMPTY = 1020011;
    public static final int ADMIN_ACCL_ACCESS_TYPE_ILLEGAL = 1020011;

    //
}
