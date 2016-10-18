package im.vinci.website.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import im.vinci.website.domain.admin.*;
import im.vinci.website.exception.ErrorCode;
import im.vinci.website.exception.VinciException;
import im.vinci.website.persistence.*;
import im.vinci.website.utils.AuthUtils;
import im.vinci.website.utils.BizTemplate;
import im.vinci.website.utils.JsonUtils;
import im.vinci.website.utils.UserContext;
import im.vinci.website.utils.itsdangerouser.Signer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * admin相关的service
 * Created by tim@vinci on 16/1/8.
 */
@Service
@Transactional
public class AdminAuthorityService {
    @Autowired
    private AdminUsersInfoMapper adminUsersInfoMapper;

    @Autowired
    private AdminUserGroupMapper adminUserGroupMapper;

    @Autowired
    private AdminActionLogMapper adminActionLogMapper;

    @Autowired
    private AdminGroupMapper adminGroupMapper;

    @Autowired
    private AdminAcclMapper adminAcclMapper;

    /**
     * 插入一个admin用户
     */
    public boolean insertAdminUser(final AdminUserInfo user) {
        return new BizTemplate<Boolean>("insertAdminUser") {

            @Override
            protected void checkParams() throws VinciException {
                if (user == null) {
                    throw new VinciException(ErrorCode.ARGUMENT_ERROR, "insert admin user null", "插入的用户为空");
                }
                if (StringUtils.isEmpty(user.getFullName())) {
                    throw new VinciException(ErrorCode.ARGUMENT_ERROR, "insert admin user fullName null", "插入的用户FullName为空");
                }
                if (user.getEmail() == null || !user.getEmail().endsWith("@vinci.im")) {
                    throw new VinciException(ErrorCode.ARGUMENT_ERROR, "insert admin user email null or not vinci", "插入的用户email不合法");
                }
                if (user.getPassword() == null || user.getPassword().length() < 8) {
                    throw new VinciException(ErrorCode.ARGUMENT_ERROR, "updatePassword's password is short", "密码位数小于8位");
                }
            }

            @Override
            protected Boolean process() throws Exception {
                String salt = AuthUtils.generateSalt();
                Signer signer = new Signer(AuthUtils.SECRET_KEY, salt);
                String token = signer.getSignature(user.getPassword());
                user.setSalt(salt);
                user.setPassword(token);
                return adminUsersInfoMapper.insertAdminUser(user) > 0;
            }

            @Override
            protected void onSuccess(Boolean result) {
                if (result) {
                    AdminActionLogBean logBean = new AdminActionLogBean();
                    logBean.setUserId(UserContext.getAdminUser().getUserid());
                    logBean.setActionType("AddAdminUser");
                    logBean.setBody("增加一个AdminUser:" + user.getFullName() + " , " + user.getEmail());
                    adminActionLogMapper.addAdminActionLog(logBean);
                }
            }
        }.execute();
    }

    public AdminUser checkAndGetUserByUserPasswd(final String email, final String password) {
        return new BizTemplate<AdminUser>("checkAndGetUserByUserPasswd") {
            @Override
            protected void checkParams() throws VinciException {
                if (StringUtils.isEmpty(email)) {
                    throw new VinciException(ErrorCode.ARGUMENT_ERROR, "checkAndGetUserByUserPasswd's email is empty", "请输入正确的邮箱");
                }
                if (StringUtils.isEmpty(password)) {
                    throw new VinciException(ErrorCode.ARGUMENT_ERROR, "checkAndGetUserByUserPasswd's password is empty", "请输入密码");
                }
            }

            @Override
            protected AdminUser process() throws Exception {
                AdminUserInfo userInfo = adminUsersInfoMapper.getUserByEmail(email);
                if (userInfo == null || !email.equals(userInfo.getEmail())) {
                    throw new VinciException(ErrorCode.LOGIN_FAILED, "email(" + email + ") is not exist", "用户或密码错误");
                }
                Signer signer = new Signer(AuthUtils.SECRET_KEY, userInfo.getSalt());
                if (!signer.verifySignature(password, userInfo.getPassword())) {
                    throw new VinciException(ErrorCode.LOGIN_FAILED, "email(" + email + ") password error", "用户或密码错误");
                }
                if (userInfo.isFreeze()) {
                    throw new VinciException(ErrorCode.LOGIN_FAILED, "email(" + email + ") is freeze", "用户或密码错误");
                }
                AdminUser adminUser = new AdminUser(userInfo.getId());
                adminUser.setEmail(userInfo.getEmail());
                adminUser.setFreeze(false);
                adminUser.setFullName(userInfo.getFullName());
                adminUser.setAcclList(Collections.emptyList());

                List<AdminUserGroupBean> groupList = adminUserGroupMapper.getUserGroupsByUserId(adminUser.getUserid());
                if (CollectionUtils.isEmpty(groupList)) {
                    return adminUser;
                }
                List<AdminACCLBean> accls = adminAcclMapper.getAcclsByGroupId(Lists.transform(groupList, input -> {
                    if (input != null) {
                        return input.getGroupId();
                    } else {
                        return -1L;
                    }
                }));
                if (!CollectionUtils.isEmpty(accls)) {
                    adminUser.setAcclList(accls);
                }
                return adminUser;
            }
        }.execute();
    }


    /**
     * 修改用户密码
     */
    public boolean updatePassword(final String email, final String password) {
        return new BizTemplate<Boolean>("updateUserInfo") {

            @Override
            protected void checkParams() throws VinciException {
                if (StringUtils.isEmpty(email)) {
                    throw new VinciException(ErrorCode.ARGUMENT_ERROR, "updatePassword's email is empty", "请输入正确的邮箱");
                }
                if (password == null || password.length() < 8) {
                    throw new VinciException(ErrorCode.ARGUMENT_ERROR, "updatePassword's password is short", "密码位数小于8位");
                }
            }

            @Override
            protected Boolean process() throws Exception {
                AdminUserInfo userInfo = adminUsersInfoMapper.getUserByEmail(email);
                if (userInfo == null || !email.equals(userInfo.getEmail())) {
                    throw new VinciException(ErrorCode.ADMIN_USER_NOT_EXIST, "email(" + email + ") is not exist", "admin用户不存在");
                }
                String salt = AuthUtils.generateSalt();
                Signer signer = new Signer(AuthUtils.SECRET_KEY, salt);
                String token = signer.getSignature(password);
                if (adminUsersInfoMapper.updateUserPassword(email, salt, token) <= 0) {
                    throw new VinciException(ErrorCode.ARGUMENT_ERROR, "email(" + email + ") is not exist", "用户不存在");
                }
                return true;
            }
        }.execute();
    }

    /**
     * 获取所有的admin user信息,但是没有accl之类的东西
     */
    public List<AdminUser> listAdminUsers() {
        return new BizTemplate<List<AdminUser>>("listAdminUsers") {

            @Override
            protected void checkParams() throws VinciException {
            }

            @Override
            protected List<AdminUser> process() throws Exception {
                List<AdminUserInfo> list = adminUsersInfoMapper.listAdminUsers();
                if (CollectionUtils.isEmpty(list)) {
                    return Collections.emptyList();
                }
                List<AdminUser> result = Lists.newArrayListWithCapacity(list.size());
                for (AdminUserInfo adminUser : list) {
                    if (adminUser == null) {
                        continue;
                    }
                    AdminUser user = new AdminUser(adminUser.getId());
                    user.setEmail(adminUser.getEmail());
                    user.setFullName(adminUser.getFullName());
                    user.setAcclList(null);
                    result.add(user);
                }
                return result;
            }
        }.execute();
    }

    /**
     * 获取所有权限分组
     */
    public List<AdminGroupBean> listAdminGroups() {
        return new BizTemplate<List<AdminGroupBean>>("listAdminGroups") {

            @Override
            protected void checkParams() throws VinciException {

            }

            @Override
            protected List<AdminGroupBean> process() throws Exception {
                return adminGroupMapper.listAdminGroups();
            }
        }.execute();
    }

    /**
     * 插入一个分组
     */
    public boolean addAdminGroup(AdminGroupBean bean) {
        return new BizTemplate<Boolean>("addAdminGroup") {

            @Override
            protected void checkParams() throws VinciException {
                if (bean == null || StringUtils.isEmpty(bean.getGroupName()) || StringUtils.isEmpty(bean.getDesc())) {
                    throw new VinciException(ErrorCode.ARGUMENT_ERROR, "groupName or desc is empty", "输入的参数为空");
                }
            }

            @Override
            protected Boolean process() throws Exception {
                if (adminGroupMapper.insertGroup(bean) <= 0) {
                    throw new VinciException(ErrorCode.ADMIN_GROUP_NAME_EXIST, "group name(" + bean.getGroupName() + ") has exist", "权限组名已存在");
                }
                return true;
            }

            @Override
            protected void onSuccess(Boolean result) {
                AdminActionLogBean logBean = new AdminActionLogBean();
                logBean.setUserId(UserContext.getAdminUser().getUserid());
                logBean.setActionType("AddAdminGroup");
                logBean.setBody("增加AdminGroup:" + bean.getGroupName() + " , " + bean.getDesc());
                adminActionLogMapper.addAdminActionLog(logBean);
            }
        }.execute();
    }

    /**
     * 删除一个group
     */
    public boolean deleteAdminGroup(final long groupId) {
        return new BizTemplate<Boolean>("deleteAdminGroup") {
            private AdminGroupBean bean = null;

            @Override
            protected void checkParams() throws VinciException {
                if (groupId <= 0) {
                    throw new VinciException(ErrorCode.ARGUMENT_ERROR, "admin group id is :" + groupId, "权限组不存在");
                }

            }

            @Override
            protected Boolean process() throws Exception {
                bean = adminGroupMapper.getGroupsById(groupId);
                if (bean == null) {
                    throw new VinciException(ErrorCode.ADMIN_GROUP_NOT_EXIST, "group(" + groupId + ") is't exist", "输入参数错误,权限组不存在");
                }
                if (bean.getUserCount() > 0) {
                    throw new VinciException(ErrorCode.ADMIN_GROUP_HAS_USERS, "group(" + groupId + ") has user", bean.getGroupName() + "还有用户,不能删除");
                }
                return adminGroupMapper.deleteGroup(groupId) > 0;
            }

            @Override
            protected void onSuccess(Boolean result) {
                if (result) {
                    AdminActionLogBean logBean = new AdminActionLogBean();
                    logBean.setUserId(UserContext.getAdminUser().getUserid());
                    logBean.setActionType("RemoveAdminGroup");
                    logBean.setBody("删除AdminGroup:" + bean.getGroupName() + " , " + bean.getDesc());
                    adminActionLogMapper.addAdminActionLog(logBean);
                }
            }
        }.execute();
    }


    /**
     * 获取一个权限组的所有权限
     */
    public List<AdminACCLBean> listAcclsByGroup(final long groupId) {
        return new BizTemplate<List<AdminACCLBean>>("listAcclsByGroup") {

            @Override
            protected void checkParams() throws VinciException {
            }

            @Override
            protected List<AdminACCLBean> process() throws Exception {
                List<AdminACCLBean> list = adminAcclMapper.getAcclsByGroupId(ImmutableList.of(groupId));
                if (list == null) {
                    return Collections.emptyList();
                }
                return list;
            }
        }.execute();
    }

    /**
     * 加入一个accl
     */
    public boolean addAccl(final AdminACCLBean bean) {
        return new BizTemplate<Boolean>("addAdminAccl") {

            @Override
            protected void checkParams() throws VinciException {
                if (bean == null) {
                    throw new VinciException(ErrorCode.ARGUMENT_ERROR, "add admin accl , bean is empty", "输入参数错误");
                }
                if (StringUtils.isEmpty(bean.getResource())) {
                    throw new VinciException(ErrorCode.ADMIN_ACCL_RESOURCE_IS_EMPTY, "add admin accl , resource is empty", "输入参数错误,资源为空");
                }
                if (!"allow".equalsIgnoreCase(bean.getAccessType()) && !"deny".equalsIgnoreCase(bean.getAccessType())) {
                    throw new VinciException(ErrorCode.ADMIN_ACCL_ACCESS_TYPE_ILLEGAL, "add admin accl , allow is illegal", "输入参数错误,accessType必须allow/deny");
                }
            }

            @Override
            protected Boolean process() throws Exception {
                if (adminGroupMapper.getGroupsById(bean.getGroupId()) == null) {
                    throw new VinciException(ErrorCode.ADMIN_GROUP_NOT_EXIST, "add admin accl , group(" + bean.getGroupId() + ") is't exist", "输入参数错误,权限组不存在");
                }
                return adminAcclMapper.addAccl(bean) > 0;
            }

            @Override
            protected void onSuccess(Boolean result) {
                if (result) {
                    AdminActionLogBean logBean = new AdminActionLogBean();
                    logBean.setUserId(UserContext.getAdminUser().getUserid());
                    logBean.setActionType("AddAdminAccl");
                    logBean.setBody("增加Admin权限:" + JsonUtils.encode(bean));
                    adminActionLogMapper.addAdminActionLog(logBean);
                }
            }
        }.execute();
    }

    /**
     * 删除一些权限从权限组中
     */
    public int removeAccls(final long groupId, final List<Long> ids) {
        return new BizTemplate<Integer>("removeAdminAccl") {
            private List<AdminACCLBean> needRemoveList = Collections.emptyList();

            @Override
            protected void checkParams() throws VinciException {
            }

            @Override
            protected Integer process() throws Exception {
                if (CollectionUtils.isEmpty(ids)) {
                    return 0;
                }
                if (adminGroupMapper.getGroupsById(groupId) == null) {
                    throw new VinciException(ErrorCode.ADMIN_GROUP_NOT_EXIST, "add admin accl , group(" + groupId + ") is't exist", "输入参数错误,权限组不存在");
                }
                List<AdminACCLBean> accls = adminAcclMapper.getAcclsByGroupId(ImmutableList.of(groupId));
                needRemoveList = Lists.newArrayListWithCapacity(ids.size());
                Set<Long> sets = Sets.newHashSet(ids);
                for (AdminACCLBean bean : accls) {
                    if (bean != null) {
                        if (sets.remove(bean.getId())) {
                            needRemoveList.add(bean);
                        }
                    }
                }
                return adminAcclMapper.deleteAccls(groupId, Lists.transform(needRemoveList, AdminACCLBean::getId));
            }

            @Override
            protected void onSuccess(Integer result) {
                if (result > 0) {
                    AdminActionLogBean logBean = new AdminActionLogBean();
                    logBean.setUserId(UserContext.getAdminUser().getUserid());
                    logBean.setActionType("RemoveAdminAccl");
                    logBean.setBody("删除Admin权限:" + groupId + "," + JsonUtils.encode(needRemoveList));
                    adminActionLogMapper.addAdminActionLog(logBean);
                }
            }
        }.execute();
    }

    /**
     * 加入用户到一个权限组中
     */
    public int addUsers2Group(final long groupId, final Set<Long> userIds) {
        return new BizTemplate<Integer>("addAdminUser2Group") {

            @Override
            protected void checkParams() throws VinciException {

            }

            @Override
            protected Integer process() throws Exception {
                if (CollectionUtils.isEmpty(userIds)) {
                    return 0;
                }
                if (adminGroupMapper.getGroupsById(groupId) == null) {
                    throw new VinciException(ErrorCode.ADMIN_GROUP_NOT_EXIST, "add admin accl , group(" + groupId + ") is't exist", "输入参数错误,权限组不存在");
                }
                int delta = adminUserGroupMapper.addUser2Group(groupId, userIds);
                adminGroupMapper.updateAdminGroupUserCount(groupId, delta);
                return delta;
            }

            @Override
            protected void onSuccess(Integer result) {
                if (result > 0) {
                    AdminActionLogBean logBean = new AdminActionLogBean();
                    logBean.setUserId(UserContext.getAdminUser().getUserid());
                    logBean.setActionType("RemoveaddAdminUser2GroupAdminAccl");
                    logBean.setBody("增加用户到权限组:" + groupId + "," + userIds);
                    adminActionLogMapper.addAdminActionLog(logBean);
                }
            }
        }.execute();
    }

    /**
     * 删除用户到一个权限组中
     */
    public int removeUsers2Group(final long groupId, final long userId) {
        return new BizTemplate<Integer>("removeAdminUser2Group") {

            @Override
            protected void checkParams() throws VinciException {

            }

            @Override
            protected Integer process() throws Exception {
                if (adminGroupMapper.getGroupsById(groupId) == null) {
                    throw new VinciException(ErrorCode.ADMIN_GROUP_NOT_EXIST, "add admin accl , group(" + groupId + ") is't exist", "输入参数错误,权限组不存在");
                }
                if (adminUserGroupMapper.removeUser4Group(groupId, userId) > 0) {
                    adminGroupMapper.updateAdminGroupUserCount(groupId, -1);
                    return 1;
                }
                return 0;
            }

            @Override
            protected void onSuccess(Integer result) {
                if (result > 0) {
                    AdminActionLogBean logBean = new AdminActionLogBean();
                    logBean.setUserId(UserContext.getAdminUser().getUserid());
                    logBean.setActionType("removeAdminUser2Group");
                    logBean.setBody("删除用户到权限组:" + groupId + "," + userId);
                    adminActionLogMapper.addAdminActionLog(logBean);
                }
            }
        }.execute();
    }
}
