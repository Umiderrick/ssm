package im.vinci.monitor;

import java.math.BigDecimal;


/**
 * 绝对值的比率监控。比如servlet busy的线程数和数据库连接数的比例
 * 
 * @author sunli Date: 12-9-6 Time: 下午1:40
 */
public class RateComputerAbsoluteMonitor extends ComputerMonitor {
    private String denominatorKey;
    private String numeratorKey;

    /**
     * 传入的key需要都是是绝对值类型的监控，否则结果可能是不正确的。
     * 
     * @param denominatorKey 分母key
     * @param numeratorKey 分子key
     */
    public RateComputerAbsoluteMonitor(String denominatorKey, String numeratorKey) {
        this.denominatorKey = denominatorKey;
        this.numeratorKey = numeratorKey;
    }
    /**
     *
     * @param denominatorCounter 分母Counter
     * @param numeratorCounter 分子counter
     */
    public RateComputerAbsoluteMonitor(Monitor denominatorCounter, Monitor numeratorCounter) {
        this.denominatorKey = denominatorCounter.getInstanceName();
        this.numeratorKey = numeratorCounter.getInstanceName();
    }

    @Override
    public Number getValue() {
        Number denominator = get(this.denominatorKey);
        Number numerator = get(this.numeratorKey);

        BigDecimal value;
        // 分母为0,返回100%
        if (denominator == null || denominator.intValue() == 0) {
            value = BigDecimal.ONE;
        } else if (numerator == null || numerator.intValue() == 0) {// 分子为0,返回0
            value = BigDecimal.ZERO;
        } else {// 都不为0,进行计算
            value = BigDecimal.valueOf((numerator.doubleValue() / denominator.doubleValue()));
        }
        value = value.multiply(BigDecimal.valueOf(100));
        value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        return value;
    }
}
