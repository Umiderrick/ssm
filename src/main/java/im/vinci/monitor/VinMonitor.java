package im.vinci.monitor;

import im.vinci.monitor.exception.MonitorExistsException;
import im.vinci.monitor.exception.MonitorNotExistsException;
import im.vinci.monitor.exception.TimeOutOfBoundsException;
import im.vinci.monitor.util.LastData;
import im.vinci.monitor.util.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

/**
 * 监控服务
 * 
 * @author sunli
 */
public class VinMonitor {
    private static final Logger logger = LoggerFactory.getLogger(VinMonitor.class);
    /**
     * 存储所有的监控实例
     */
    private static final ConcurrentSkipListMap<String, Monitor> monitorMap = MonitorContainer.getMonitormap();
    private static final ConcurrentSkipListMap<String, Monitor> periodMonitorMap = MonitorContainer
            .getPeriodmonitormap();
    private static final ConcurrentHashMap<String, LastData<Number>> lastValueMap = MonitorContainer.getLastvaluemap();
    private static final ConcurrentHashMap<String, LastData<Number>> lastMonthValueMap = MonitorContainer
            .getLastmonthvaluemap();
    private static final ConcurrentSkipListMap<String, Monitor> computerMonitors = MonitorContainer
            .getComputermonitors();
    private static final ScheduledExecutorService periodService = Executors.newScheduledThreadPool(1,
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t =  new Thread(r, "thread-monitor-avg-compute-task");
                    t.setDaemon(true);
                    return t;
                }

            });

    private static final ScheduledExecutorService threadMonitorScheduler = Executors.newScheduledThreadPool(1,
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t =  new Thread(r, "thread-monitor-task");
                    t.setDaemon(true);
                    return t;
                }

            });
    // 跟top的默认计算间隔一致
    private static final int monitorPeriodTimeMillisecondes = 3000;
    private static final int periodTimeSecondes = 2;
    static {
        // 三秒钟执行一次线程cpu使用率监控计算
        threadMonitorScheduler.scheduleAtFixedRate(ThreadReporter.getInstance(), 0, monitorPeriodTimeMillisecondes,
                TimeUnit.MILLISECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                periodService.shutdown();
                threadMonitorScheduler.shutdown();
            }
        });
    }

    public VinMonitor() {

    }

    /**
     * 获取一个AtomicIntegerCounter计数器示例，并自动以<code>instanceName</code>名称注册到监控系统中 如果<code>instanceName</code>已经被使用，会抛出
     * <code>MonitorExistsException</code>
     * 
     * @param instanceName 计数器名称
     * @return AtomicIntegerCounter计数器
     */
    public synchronized static AtomicIntegerCounter createAtomicIntegerCounter(String instanceName) {
        return createAtomicIntegerCounter(instanceName, instanceName);
    }

    /**
     * 获取一个AtomicIntegerCounter计数器示例，并自动以<code>instanceName</code>名称注册到监控系统中 如果<code>instanceName</code>已经被使用，会抛出
     * <code>MonitorExistsException</code>
     * 
     * @param instanceName 计数器名称
     * @param description 描述信息
     * @return AtomicIntegerCounter计数器
     */
    public synchronized static AtomicIntegerCounter createAtomicIntegerCounter(String instanceName, String description) {
        // 如果名称为instanceName的监控没有创建过，则新添加
        if (!monitorMap.containsKey(instanceName)) {
            AtomicIntegerCounter counter = new AtomicIntegerCounter();
            if (monitorMap.putIfAbsent(instanceName, counter) == null) {
                counter.setInstanceName(instanceName);
                counter.setDescription(description);
                return counter;
            } else {
                throw new MonitorExistsException(instanceName + " monitor object exists in Concurrent thread");
            }
        } else {
            throw new MonitorExistsException(instanceName + " monitor object exists");
        }
    }

    /**
     * 获取一个AtomicLongCounter计数器示例，并自动以<code>instanceName</code>名称注册到监控系统中 如果<code>instanceName</code>已经被使用，会抛出
     * <code>MonitorExistsException</code>
     * 
     * @param instanceName 计数器名称
     * @return AtomicLongCounter计数器
     */
    public synchronized static AtomicLongCounter createAtomicLongCounter(String instanceName) {
        return createAtomicLongCounter(instanceName, instanceName);
    }

    /**
     * 获取一个AtomicLongCounter计数器示例，并自动以<code>instanceName</code>名称注册到监控系统中 如果<code>instanceName</code>已经被使用，会抛出
     * <code>MonitorExistsException</code>
     * 
     * @param instanceName
     * @param description 描述信息
     * @return
     */
    public synchronized static AtomicLongCounter createAtomicLongCounter(String instanceName, String description) {
        // 如果名称为instanceName的监控没有创建过，则新添加
        if (!monitorMap.containsKey(instanceName)) {
            AtomicLongCounter counter = new AtomicLongCounter();
            if (monitorMap.putIfAbsent(instanceName, counter) == null) {
                counter.setInstanceName(instanceName);
                counter.setDescription(description);
                return counter;
            } else {
                throw new MonitorExistsException(instanceName + " monitor object exists in Concurrent thread");
            }
        } else {
            throw new MonitorExistsException(instanceName + " monitor object exists");
        }
    }

    /**
     * 注册一个monitor实例
     * 
     * <pre>
     *
     * QuMonitor.registerMonitor(&quot;monitorname&quot;, new Monitor() {
     *     public Object getValue() {
     *         return map.size();
     *     }
     * });
     * </pre>
     * 
     * @param instanceName 注册的监控实例名称
     * @param description 监控对象的描述信息
     * @param monitor 注册的监控实例
     */
    public synchronized static void registerMonitor(String instanceName, String description, Monitor monitor) {
        if (!monitorMap.containsKey(instanceName)) {
            if (monitorMap.putIfAbsent(instanceName, monitor) == null) {
                monitor.setDescription(description);
            } else {
                throw new MonitorExistsException(instanceName + " monitor object exists in Concurrent thread");
            }
        } else {
            throw new MonitorExistsException(instanceName + " monitor object exists");
        }
    }

    /**
     * 增加需要计算平均值的monitor
     * 
     * @param counter 需要计算平均值的monitor名称
     */
    public synchronized static void addPeriodMonitor(Monitor counter) {
        Monitor last = periodMonitorMap.putIfAbsent(counter.getInstanceName(), counter);
        if (last == null) {
            PeriodMonitorTask periodMonitorTask = new PeriodMonitorTask(counter);
            periodService.scheduleAtFixedRate(periodMonitorTask, 0, periodTimeSecondes, TimeUnit.SECONDS);
        }
    }

    /**
     * 添加一个复杂计算监控对象
     * <p>
     * 
     * <pre>
     * QuMonitor.addComputerMonitor(&quot;名称前缀.监控名称&quot;, &quot;每分钟用户订单产生数&quot;, new ComputerMonitor() {
     *     &#064;Override
     *     public Number getValue() {
     *         // user.orders的当前值/user.edit的当前值
     *         return this.get(&quot;user.orders&quot;).doubleValue() / this.get(&quot;user.edit&quot;).doubleValue();
     *     }
     * });
     * </pre>
     * 
     * </p>
     * 
     * @param instanceName 监控实例名称
     * @param description 复杂监控对象的描述信息
     * @param counter ComputerMonitor示例
     * @return
     */
    public synchronized static void addComputerMonitor(String instanceName, String description, ComputerMonitor counter) {
        // 保证复杂计算监控对象的instanceName的唯一性
        if (!monitorMap.containsKey(instanceName) && !periodMonitorMap.containsKey(instanceName)
                && !computerMonitors.containsKey(instanceName)) {
            counter.setInstanceName(instanceName);
            counter.setDescription(description);
            computerMonitors.putIfAbsent(instanceName, counter);
        } else {
            throw new MonitorExistsException(instanceName + " monitor object exists");
        }
    }

    /**
     * 获取已经注册到监控系统中的监控实例
     * 
     * @param instanceName
     * @return
     */
    public static Monitor getMonitor(String instanceName) {
        Monitor counter = monitorMap.get(instanceName);
        if (counter != null) {
            return counter;
        }
        return null;
    }

    /**
     * omit the period data
     * 
     * @param query
     * @return
     */
    public static Map<String, Number> getMonitorData(String query) {
        Map<String, Number> dataMap = VinMonitor.getAllCounterData(query);
        dataMap.putAll(MapUtil.find(computerMonitors, query));
        return dataMap;
    }

    /**
     * @param query
     * @param minutesOffset
     * @param periodTimeMinutes
     * @param dayoffset
     * @param day
     * @return
     */
    public static Map<String, Number> getMonitorData(String query, int minutesOffset, int periodTimeMinutes,
            int dayoffset, int day) {
        Map<String, Number> dataMap = VinMonitor.getAllCounterData(query);
        dataMap.putAll(VinMonitor.getPeriodCounterData(query, minutesOffset, periodTimeMinutes));
        dataMap.putAll(VinMonitor.getPeriodDayCounterData(query, day, dayoffset));
        dataMap.putAll(MapUtil.find(computerMonitors, query));
        return dataMap;
    }

    /**
     * 获取所有的监控对象的当前值 不包含递增值
     * 
     * @return
     */
    public static Map<String, Number> getAllCounterData() {
        Map<String, Number> result = new TreeMap<String, Number>();
        for (Iterator<Entry<String, Monitor>> iterator = monitorMap.entrySet().iterator(); iterator.hasNext();) {
            Entry<String, Monitor> entry = iterator.next();
            result.put(entry.getKey(), entry.getValue().getValue());
        }
        return result;
    }

    /**
     * 获取符合query的监控对象的当前值
     * <p>
     * query表示前缀查询 比如abc* 表示监控对象名名称以abc开始的所有监控对象 *表示所有对象
     * </p>
     * 
     * @param query
     * @return
     */
    public static Map<String, Number> getAllCounterData(String query) {
        return MapUtil.find(monitorMap, query);
    }

    /**
     * 获取按分钟递增的监控对象的递增数据
     * <p>
     * offset必>=1,periodTimeMinutes+offset<=30
     * </p>
     * 
     * @param query 查询条件
     * @param offset 从当前数的多少分钟开始
     * @param periodTimeMinutes 间隔多少分钟
     * @return
     */
    public static Map<String, Number> getPeriodCounterData(String query, int offset, int periodTimeMinutes) {
        String keySuffix = ".PeriodMinutes" + offset + "_" + periodTimeMinutes;
        Map<String, Number> result = new HashMap<String, Number>();
        SortedMap<String, Number> map = MapUtil.find(periodMonitorMap, query);
        for (Entry<String, Number> entry : map.entrySet()) {
            result.put(entry.getKey() + keySuffix, getPeriod(entry.getKey(), offset, periodTimeMinutes));

        }
        return result;
    }

    /**
     * 获取按天递增的监控对象的递增数据
     * 
     * @param query 查询条件
     * @param offset 从当前数多少天开始
     * @param periodTimeDay 间隔多少天
     * @return
     */
    public static Map<String, Number> getPeriodDayCounterData(String query, int offset, int periodTimeDay) {
        String keySuffix = ".PeriodDay" + offset + "_" + periodTimeDay;
        Map<String, Number> result = new HashMap<String, Number>();
        SortedMap<String, Number> map = MapUtil.find(periodMonitorMap, query);
        for (Entry<String, Number> entry : map.entrySet()) {
            result.put(entry.getKey() + keySuffix, getPeriodDay(entry.getKey(), offset, periodTimeDay));

        }
        return result;

    }

    /**
     * 获取最近一段时间的监控变化值。
     * <p>
     * 比如一个自增计数器，我们需要知道最近一分钟递增的值，或者最近5分钟递增的值，则可以使用
     * 
     * <pre>
     * PeriodMonitorTask.getPeriod(String instanceName, int offset,int periodTimeMinutes);
     * </pre>
     * 
     * </p>
     * 
     * @param instanceName
     * @param offset 起始分钟数
     * @param periodTimeMinutes
     * @return
     */
    public static Number getPeriod(String instanceName, int offset, int periodTimeMinutes) {
        if (periodTimeMinutes + offset > 31) {
            throw new TimeOutOfBoundsException("periodTimeMinutes + offset must less than 31 Minutes");
        }
        LastData<Number> lastData;
        try {
            Monitor currentMonitor = periodMonitorMap.get(instanceName);
            if (currentMonitor == null) {
            	if(logger.isDebugEnabled()){
            		logger.debug(instanceName + " not found in periodMonitorMap ");
            	}
                return 0;
            }
            lastData = lastValueMap.get(instanceName);
            if (lastData == null) {
            	if(logger.isDebugEnabled()){
            		logger.debug(instanceName + " not found in lastMonthValueMap");
            	}
            	return 0;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return 0;
        }
        // 获取之前periodTimeMinutes的值
        // 当前值
        Number currentValue = lastData.getLastElements(offset);
        // periodTimeMinutes分钟前
        Number lastValue = lastData.getLastElements(periodTimeMinutes + 1);

        if (lastValue == null) {
            lastValue = 0;
        }
        if (currentValue == null) {
            currentValue = 0;
        }
        if (currentValue instanceof Integer) {
            return currentValue.intValue() - lastValue.intValue();
        } else if (currentValue instanceof Long) {
            return currentValue.longValue() - lastValue.longValue();
        } else if (currentValue instanceof Float) {
            return currentValue.floatValue() - lastValue.floatValue();
        } else if (currentValue instanceof Double) {
            return currentValue.doubleValue() - lastValue.doubleValue();
        }
        return 0;
    }

    /**
     * 获取最近一段时间的监控变化值。
     * <p>
     * 比如一个自增计数器，我们需要知道最近一分钟递增的值，或者最近5分钟递增的值，则可以使用
     * 
     * <pre>
     * PeriodMonitorTask.getPeriodDay(String instanceName,  int offset, int periodTimeDay);
     * </pre>
     * 
     * </p>
     * 
     * @param instanceName
     * @param offset 起始天数
     * @param periodTimeDay
     * @return
     */
    public static Number getPeriodDay(String instanceName, int offset, int periodTimeDay) {
        if (periodTimeDay + offset > 31) {
            throw new TimeOutOfBoundsException("periodTimeMinutes + offset must less than 31 Minutes");
        }
        Monitor currentMonitor = periodMonitorMap.get(instanceName);
        if (currentMonitor == null) {
            throw new NullPointerException(instanceName + " not found in periodMonitorMap ");
        }
        LastData<Number> lastMonthData = lastMonthValueMap.get(instanceName);
        if (lastMonthData == null) {
            throw new MonitorNotExistsException(instanceName + " not found in lastMonthValueMap");
        }
        // 获取之前periodTimeMinutes的值
        // 当前值
        Number currentValue = lastMonthData.getLastElements(offset);
        // periodTimeMinutes分钟前
        Number lastValue = lastMonthData.getLastElements(periodTimeDay + 1);

        if (lastValue == null) {
            lastValue = 0;
        }
        if (currentValue == null) {
            currentValue = 0;
        }
        if (currentValue instanceof Integer) {
            return currentValue.intValue() - lastValue.intValue();
        } else if (currentValue instanceof Long) {
            return currentValue.longValue() - lastValue.longValue();
        } else if (currentValue instanceof Float) {
            return currentValue.floatValue() - lastValue.floatValue();
        } else if (currentValue instanceof Double) {
            return currentValue.doubleValue() - lastValue.doubleValue();
        }
        return 0;
    }


}
