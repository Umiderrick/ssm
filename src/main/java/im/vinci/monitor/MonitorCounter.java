package im.vinci.monitor;

/**
 * @author sunli
 */
public abstract class MonitorCounter implements Monitor {
    protected String instanceName;
    protected String description;

    /**
     * @return
     */
    @Override
    public String getInstanceName() {
        return instanceName;
    }

    /**
     * @param instanceName
     */
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    /**
     * @return
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 实现对计数器的自增操作
     */
    public abstract void increment();

    /**
     * 实现对计数器的自减操作
     */
    public abstract void decrement();

    /**
     * 对计数器增加指定的值
     * 
     * @param delta 增加的数字
     */
    public abstract void increment(int delta);

    /**
     * 对计数器减指定的值
     * 
     * @param delta 减去的数字
     */
    public abstract void decrement(int delta);

    /**
     * 对计数器设置指定的值
     * 
     * @param value 设置指定的值
     */
    public void set(Number value) {
        throw new UnsupportedOperationException("set Unsupported！The class must override set method!");
    }

    /**
     * 获取计数器的值
     * 
     * @see Monitor#getValue()
     */
    @Override
    public abstract Number getValue();

}
