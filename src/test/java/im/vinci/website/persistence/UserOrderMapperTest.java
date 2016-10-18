package im.vinci.website.persistence;

import im.vinci.VinciApplication;
import im.vinci.website.config.UserProfile;
import im.vinci.website.domain.order.OrderStatus;
import im.vinci.website.domain.order.PaymentStatus;
import im.vinci.website.domain.order.UserOrder;
import im.vinci.website.domain.order.UserOrderAddress;
import org.hamcrest.core.IsNull;
import org.hamcrest.number.BigDecimalCloseTo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


/**
 * Created by tim@vinci on 16/1/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {VinciApplication.class})
@WebAppConfiguration
@IntegrationTest
@ActiveProfiles(UserProfile.UNIT_TEST)
@Rollback(true)
@Transactional
public class UserOrderMapperTest {
    @Autowired
    private UserOrderMapper userOrderMapper;

    @Test
    public void testUserOrderMapper() {
        UserOrderAddress address = new UserOrderAddress();
        address.setCity("bj");
        UserOrder order = new UserOrder();
        order.setUserId(1);
        order.setOrderId(2);
        order.setAddress(address);
        order.setTotalPayment(new BigDecimal("11.23"));
        order.setActTotalPayment(new BigDecimal("11.23"));
        order.setOrderStatus(OrderStatus.AgreeReturn);
        order.setPaymentStatus(PaymentStatus.Paid);
        order.setPaymentMethod("alipay");
        order.setDiscountCode("");
        assertThat(userOrderMapper.insertUserOrder(order),is(1));

        assertThat(userOrderMapper.getUserOrderByOrderId(1), IsNull.nullValue());
        UserOrder o1 = userOrderMapper.getUserOrderByOrderId(2);
        assertThat(o1,IsNull.notNullValue());
        assertThat(o1.getOrderStatus(),is(OrderStatus.AgreeReturn));
        assertThat(o1.getAddress().getCity(),is("bj"));
        assertThat(o1.getTotalPayment(),is(new BigDecimal("11.23")));
    }
}
