package im.vinci.monitor.muti;

import im.vinci.monitor.MonitorCounter;
import im.vinci.monitor.VinMonitor;
import im.vinci.monitor.exception.MonitorExistsException;

/**
 * Integer型的多计数器。
 * <p>
 * 比如同种类型的计数器，下属有多个计数器，需要动态创建的时候
 * </p>
 * 
 * @author sunli
 */
public class MutiIntegerMonitor extends AbstractMutiMonitor {

    private MutiIntegerMonitor(String prefix, String description, boolean addPeriod) {
        super(prefix, description, addPeriod);
    }

    /**
     * 创建一个MutiLongMonitor，counter不计算每分钟的递增区间值
     * 
     * @param prefix 前缀名称
     * @param description 描述信息
     * @return
     */
    public static MutiIntegerMonitor buildWithPrefix(String prefix, String description) {
        return buildWithPrefix(prefix, description, false);
    }

    /**
     * 创建一个MutiLongMonitor，counter自动计算每分钟的递增区间值
     * 
     * @param prefix 缀名称
     * @param description 描述信息
     * @param addPeriod counter是否计算每分钟的递增区间值
     * @return
     */
    public static MutiIntegerMonitor buildWithPrefix(String prefix, String description, boolean addPeriod) {
        if (!instanceMap.containsKey(prefix)) {
            MutiIntegerMonitor mutiLongMonitor = new MutiIntegerMonitor(prefix, description, addPeriod);
            if (instanceMap.putIfAbsent(prefix, mutiLongMonitor) == null) {
                return mutiLongMonitor;
            }

        }
        throw new MonitorExistsException("MutiLongMonitor with prefix " + prefix + "  exists");
    }

    @Override
    protected MonitorCounter createMonitorCounter(String instanceName, String description) {
        return VinMonitor.createAtomicIntegerCounter(instanceName, description);
    }
}
