package im.vinci.monitor.muti;


import im.vinci.monitor.MonitorCounter;
import im.vinci.monitor.VinMonitor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 结构：
 * <p>
 * 
 * <pre>
 * prefix+
 *       --instanceName1=>MonitorCounter
 *       --instanceName2=>MonitorCounter
 *       --instanceName3=>MonitorCounter
 *       --......
 * </pre>
 * 
 * </p>
 * 
 * @author sunli
 */
public abstract class AbstractMutiMonitor {
    protected final ConcurrentHashMap<String, MonitorCounter> map = new ConcurrentHashMap<String, MonitorCounter>();
    protected static final ConcurrentHashMap<String, AbstractMutiMonitor> instanceMap = new ConcurrentHashMap<String, AbstractMutiMonitor>();
    protected String prefix = null;
    protected boolean addPeriod = false;
    protected String description = null;
    private final Lock lock = new ReentrantLock();

    /**
     * @param prefix 公共前缀
     * @param description 描述信息
     * @param addPeriod 是否存储按时间间隔存储的
     */
    protected AbstractMutiMonitor(String prefix, String description, boolean addPeriod) {
        this.prefix = prefix + '.';
        this.addPeriod = addPeriod;
        this.description = description;
    }

    protected abstract MonitorCounter createMonitorCounter(String instanceName, String description);

    /**
     * 对指定名称的counter增加delta
     * 如果counter不存在，则自动创建。小心使用，防止重名。
     * 
     * @param name
     * @param delta
     */
    public void increment(String name, int delta) {
        MonitorCounter counter = map.get(prefix + name);
        if (counter == null) {
            lock.lock();
            try {
                counter = map.get(prefix + name);
                if (counter == null) {
                    counter = createMonitorCounter(prefix + name, this.description);
                    MonitorCounter lastCounter = map.putIfAbsent(prefix + name, counter);
                    if (lastCounter != null) {
                        counter = map.get(prefix + name);
                    } else {
                        if (addPeriod == true) {
                            VinMonitor.addPeriodMonitor(counter);
                        }
                    }
                }
            } finally {
                lock.unlock();
            }

        }
        counter.increment(delta);
    }

    /**
     * 对指定名称的counter增加1
     * 如果counter不存在，则自动创建。小心使用，防止重名。
     * 
     * @param name
     */
    public void increment(String name) {
        increment(name, 1);
    }
}
