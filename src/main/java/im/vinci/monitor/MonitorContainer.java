
package im.vinci.monitor;

import im.vinci.monitor.util.LastData;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 所有监控数据的存储容器
 * 
 * @author sunli
 */
public class MonitorContainer {
    /**
     * 存储所有的监控实例
     */
    private static final ConcurrentSkipListMap<String, Monitor> monitorMap = new ConcurrentSkipListMap<String, Monitor>();
    /**
     * 存储需要计算区间值的监控实例
     */
    private static final ConcurrentSkipListMap<String, Monitor> periodMonitorMap = new ConcurrentSkipListMap<String, Monitor>();
    /**
     * 存储计算型监控实例
     */
    private static final ConcurrentSkipListMap<String, Monitor> computerMonitors = new ConcurrentSkipListMap<String, Monitor>();
    /**
     * 存储区间值的历史数据
     */
    private static final ConcurrentHashMap<String, LastData<Number>> lastValueMap = new ConcurrentHashMap<String, LastData<Number>>();
    /**
     * 存储最近一个月的区间历史数据
     */
    private static final ConcurrentHashMap<String, LastData<Number>> lastmonthValueMap = new ConcurrentHashMap<String, LastData<Number>>();

    public static ConcurrentHashMap<String, LastData<Number>> getLastmonthvaluemap() {
        return lastmonthValueMap;
    }

    public static ConcurrentSkipListMap<String, Monitor> getComputermonitors() {
        return computerMonitors;
    }

    public static ConcurrentSkipListMap<String, Monitor> getMonitormap() {
        return monitorMap;
    }

    public static ConcurrentSkipListMap<String, Monitor> getPeriodmonitormap() {
        return periodMonitorMap;
    }

    public static ConcurrentHashMap<String, LastData<Number>> getLastvaluemap() {
        return lastValueMap;
    }
}
