package im.vinci.monitor;

/**
 * @author sunli
 */
public abstract class MonitorData implements Monitor {
    private String description;

    /**
     * 获取监控对象的描述值
     * 
     * @return
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * 设置监控对象的描述信息
     * 
     * @param description
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取监控对象的值
     */
    @Override
    public abstract Number getValue();

    /**
     * 获取监控计数器的值
     * 
     * @param key
     * @return
     */
    public Number get(String key) {
        return MonitorContainer.getMonitormap().get(key).getValue();
    }

    /**
     * 获取按分钟计算的获取最近一段时间的监控变化值。
     * 
     * @param key
     * @param minutesOffset
     * @param period
     * @return
     */
    public Number getPeriod(String key, int minutesOffset, int period) {
        return VinMonitor.getPeriod(key, minutesOffset, period);
    }

    /**
     * 获取最近一分钟的递增值
     * 
     * @param key
     * @return
     */
    public Number getLast1Minutes(String key) {
        return getPeriod(key, 1, 1);
    }

    /**
     * 获取最近五分钟的递增值
     * 
     * @param key
     * @return
     */
    public Number getLast5Minutes(String key) {
        return getPeriod(key, 1, 5);
    }

    /**
     * 获取按天计算的获取最近一段时间的监控变化值。
     * 
     * @param key
     * @param dayOffset
     * @param period
     * @return
     */
    public Number getPeriodDay(String key, int dayOffset, int period) {
        return VinMonitor.getPeriodDay(key, dayOffset, period);
    }

}
