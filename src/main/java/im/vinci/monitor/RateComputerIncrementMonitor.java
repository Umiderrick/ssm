package im.vinci.monitor;

import java.math.BigDecimal;

/**
 * 自增计数器的比率监控
 * 
 * @author jiaqiang.yan Date: 12-5-4 Time: 下午2:08
 */
public class RateComputerIncrementMonitor extends ComputerMonitor {
    private String denominatorKey;
    private String numeratorKey;
    private int minutes;
    private boolean successRate = true;

    /**
     * 比如要计算某个请求每5分钟的成功率，可以
     * <p>
     * <code>
     *         new RateComputerIncrementMonitor("操作次数统计的key"，“成功次数统计的key”，5)
     *     </code>
     * 
     * </p>
     * 
     * 传入的key所在监控需要是自增类型的，并且需要加入到Period里面，即调用了addPeriodMonitor(Monitor counter)
     * 
     * @param denominatorKey 分母key
     * @param numeratorKey 分子key
     * @param minutes 计算的时间间隔
     */
    public RateComputerIncrementMonitor(String denominatorKey, String numeratorKey, int minutes) {
        this.denominatorKey = denominatorKey;
        this.numeratorKey = numeratorKey;
        this.minutes = minutes;
    }

    public RateComputerIncrementMonitor(String denominatorKey, String numeratorKey, int minutes, boolean successRate) {
        this.denominatorKey = denominatorKey;
        this.numeratorKey = numeratorKey;
        this.minutes = minutes;
        this.successRate = successRate;
    }

    /**
     * 
     * @param denominatorCounter 分母Counter
     * @param numeratorCounter 分子counter
     * @param minutes 计算的时间间隔
     */
    public RateComputerIncrementMonitor(Monitor denominatorCounter, Monitor numeratorCounter, int minutes) {
        this.denominatorKey = denominatorCounter.getInstanceName();
        this.numeratorKey = numeratorCounter.getInstanceName();
        this.minutes = minutes;
    }

    public RateComputerIncrementMonitor(Monitor denominatorCounter, Monitor numeratorCounter, int minutes,
            boolean successRate) {
        this.denominatorKey = denominatorCounter.getInstanceName();
        this.numeratorKey = numeratorCounter.getInstanceName();
        this.minutes = minutes;
        this.successRate = successRate;
    }

    @Override
    public Number getValue() {
        Number denominator = getPeriod(denominatorKey, 1, minutes);
        Number numerator = getPeriod(numeratorKey, 1, minutes);

        BigDecimal value;

        if (denominator == null || denominator.intValue() == 0) {
            if (this.successRate) {
                value = BigDecimal.ONE;
            } else {
                value = BigDecimal.ZERO;
            }
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
