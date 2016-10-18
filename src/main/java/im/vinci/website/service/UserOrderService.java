package im.vinci.website.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import im.vinci.website.domain.order.Invoice;
import im.vinci.website.domain.order.UserOrder;
import im.vinci.website.domain.order.UserOrderChangelog;
import im.vinci.website.domain.order.UserOrderSpec;
import im.vinci.website.domain.user.UserInfo;
import im.vinci.website.exception.ErrorCode;
import im.vinci.website.exception.VinciException;
import im.vinci.website.persistence.UserInfoMapper;
import im.vinci.website.persistence.UserOrderChangelogMapper;
import im.vinci.website.persistence.UserOrderMapper;
import im.vinci.website.utils.BizTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户订单类
 * Created by tim@vinci on 16/1/11.
 */
@Service
public class UserOrderService {

    @Autowired
    private UserOrderMapper userOrderMapper;

    @Autowired
    private UserOrderChangelogMapper userOrderChangelogMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;
    /**
     * 通过订单号获取订单详细信息
     */
    public UserOrder getUserOrderByOrderId(final long orderId) {
        return new BizTemplate<UserOrder>("getUserOrderByOrderId") {

            @Override
            protected void checkParams() throws VinciException {

            }

            @Override
            protected UserOrder process() throws Exception {
                UserOrder order = userOrderMapper.getUserOrderByOrderId(orderId);
                if (order == null) {
                    throw new VinciException(ErrorCode.ORDER_NOT_EXIST,"orderId("+orderId+") is not exist","订单不存在");
                }
                if (order.isInvoiced()) {
                    order.setInvoice(userOrderMapper.getUserOrderInvoice(orderId));
                }
                order.setUserInfo(userInfoMapper.getUserById(orderId));
                order.setUserOrderSpecList(userOrderMapper.getUserOrderSpec(orderId));
                return order;
            }
        }.execute();
    }

    /**
     * 通过订单号获取订单详细信息
     */
    public List<UserOrder> getUserOrderByOrderIds(final Set<Long> orderIds) {
        return new BizTemplate<List<UserOrder>>("getUserOrderByOrderIds") {

            @Override
            protected void checkParams() throws VinciException {

            }

            @Override
            protected List<UserOrder> process() throws Exception {
                if (CollectionUtils.isEmpty(orderIds)) {
                    return Collections.emptyList();
                }
                List<UserOrder> orders = userOrderMapper.getUserOrderByOrderIds(orderIds);
                if (CollectionUtils.isEmpty(orders)) {
                    return Collections.emptyList();
                }
                Map<Long,UserOrder> orderMap = Maps.uniqueIndex(orders, UserOrder::getOrderId);
                Map<Long,UserOrder> orderUserIdMap = Maps.uniqueIndex(orders, UserOrder::getUserId);
                List<UserInfo> userInfoList = userInfoMapper.getUserInfoByIds(orderUserIdMap.keySet());
                List<Invoice> invoices = userOrderMapper.getUserOrderInvoices(orderIds);
                List<UserOrderSpec> specs = userOrderMapper.getUserOrderSpecs(orderIds);

                if (!CollectionUtils.isEmpty(invoices)) {
                    invoices.stream().filter(invoice -> invoice != null && orderMap.containsKey(invoice.getOrderId()))
                            .forEach(invoice -> orderMap.get(invoice.getOrderId()).setInvoice(invoice));
                }
                if (!CollectionUtils.isEmpty(specs)) {
                    specs.stream().filter(spec -> spec != null && orderMap.containsKey(spec.getOrderId()))
                            .forEach(spec -> orderMap.get(spec.getOrderId()).getUserOrderSpecList().add(spec));
                }
                if (!CollectionUtils.isEmpty(userInfoList)) {
                    userInfoList.stream().filter(user -> user != null && orderUserIdMap.containsKey(user.getId()))
                            .forEach(user -> orderUserIdMap.get(user.getId()).setUserInfo(user));
                }

                return orders;
            }
        }.execute();
    }


}
