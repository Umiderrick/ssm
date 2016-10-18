package im.vinci.monitor;

import im.vinci.monitor.muti.AbstractMutiMonitor;
import im.vinci.monitor.muti.MutiLongMonitor;
import im.vinci.monitor.util.SystemTimer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;

/**
 * 从机票的监控组件移植过来的，只是接口兼容
 * 
 * @author sunli created on 12-11-23 下午2:59
 * @version $Id: QMonitor.java 9790 2012-12-21 10:18:53Z build $
 */

public class QMonitor {

	private static final ConcurrentMap<String, Boolean> qMonitorKeyMap = new ConcurrentHashMap<String, Boolean>();
	private static final String QMONITOR_KEY_PREFIX = "QMonitorItem";
	private static final String INNER_COUNT_SUFFIX = "_Counts";
	private static final String INNER_TIME_SUFFIX = "_Times";
	// QMonitor对外暴露的key后缀
	private static final String VALUE_SUFFIX = "_Value";
	private static final String TIME_SUFFIX = "_Time";
	private static final String COUNT_SUFFIX = "_Count";
	private static final AbstractMutiMonitor mutiLongMonitor = MutiLongMonitor
			.buildWithPrefix(QMONITOR_KEY_PREFIX, "QMonitor监控", true);
	private static final int COMPUTE_PERIOD_TIME_SECONDS = 2;
	private static long lastRunTime = 0; // 上一次运行计算定时任务的时间
	private static final AtomicLongCounter JVM_THREAD_COUNT = VinMonitor
			.createAtomicLongCounter("JVM_Thread_Count", "线程数");
	private static ConcurrentMap<String, MonitorItem> systemItems = new ConcurrentHashMap<String, MonitorItem>();
	private static final ScheduledExecutorService QMonitorScheduler = Executors
			.newScheduledThreadPool(1, new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r, "QMonitor-monitor-task");
					t.setDaemon(true);
					return t;
				}

			});
	private static ThreadLocal<Long> lastSMonitorStartTime = new ThreadLocal<Long>() {
		@Override
		protected Long initialValue() {
			return SystemTimer.currentTimeMillis();
		}
	};
	static {
		QMonitorScheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					long now = SystemTimer.currentTimeMillis();
					// 间隔大于50秒
					if (now - lastRunTime < TimeUnit.MILLISECONDS.convert(50,
							TimeUnit.SECONDS)) {
						return;
					}
					long seconds = DateUtils.getFragmentInSeconds(
							new Date(now), Calendar.MINUTE);
					// 保证在没分钟的前10秒开始执行,抓取服务可以在后30秒进行，这样可以避免抓取和task的时间交叉
					if (seconds > 10) {
						return;
					}
					lastRunTime = now;
					runTask();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, 0, COMPUTE_PERIOD_TIME_SECONDS, TimeUnit.SECONDS);

	}

	private static String makeName(String name) {
		return name.replaceAll(" ", "_");
	}

	private static final String TOMCAT_PREFIX = "TOMCAT_";
	private static final String JVM_PREFIX = "JVM_";

	private static void addComputedCounter(String name, String desc,
			long count, long time, boolean needTime) {
		MonitorItem item = QMonitor.systemItems.get(name);
		boolean justCreated = false;
		if (item == null) {
			item = new MonitorItem(null);
			item.add(count, time);
			QMonitor.systemItems.put(name, item);
			justCreated = true;
		}
		AtomicLongCounter counter = fetchLongMonitorByName(name + COUNT_SUFFIX,
				desc);
		counter.set(count - item.count);
		if(needTime){
			AtomicLongCounter timeCounter = fetchLongMonitorByName(name
					+ TIME_SUFFIX, desc);
			if (count - item.count > 0L) {
				timeCounter.set(Long.valueOf((time - item.time)
						/ (count - item.count)));
			} else {
				timeCounter.set(0L);
			}
		}
		if(!justCreated){
			item = new MonitorItem(null);
			item.add(count, time);
			QMonitor.systemItems.put(name, item);
		}		
	}

	private static void runTask() {
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
		JVM_THREAD_COUNT.set(threadBean.getThreadCount());
		String name;
		MonitorItem item;
		List<GarbageCollectorMXBean> gcBeans = ManagementFactory
				.getGarbageCollectorMXBeans();
		for (GarbageCollectorMXBean bean : gcBeans) {
			name = makeName((new StringBuilder(JVM_PREFIX)).append(
					bean.getName()).toString());
			final long count = bean.getCollectionCount();
			final long time = bean.getCollectionTime();
			item = QMonitor.systemItems.get(name);
			if (item == null) {
				item = new MonitorItem(null);
				item.add(count, time);
				QMonitor.systemItems.put(name, item);
			}
			// 更新监控
			addComputedCounter(name, "JVM", count, time, true);
		}
		List<TomcatInformations> list = TomcatInformations
				.buildTomcatInformationsList();
		for (TomcatInformations tomcatInfo : list) {
			// 获得tomcat每个线程池的状态
			String tomcatThreadPoolName = StringUtils.trimToNull(tomcatInfo
					.getName());
			if (StringUtils.isEmpty(tomcatThreadPoolName)) {
				// 线程池名称不存在的忽略, which is almost impossible
				continue;
			}
			final int maxThreads = tomcatInfo.getMaxThreads(); // 线程池最大线程数
			final int currentThreadCount = tomcatInfo.getCurrentThreadCount(); // 线程池当前线程数
			final int currentThreadsBusy = tomcatInfo.getCurrentThreadsBusy(); // 线程池当前活跃线程数
			final long bytesRecv = tomcatInfo.getBytesReceived(); // 线程池收到的字节数
			final long bytesSent = tomcatInfo.getBytesSent(); // 线程池发送的字节数
			final int requestCount = tomcatInfo.getRequestCount(); // 处理的总的请求次数
			final int errorCount = tomcatInfo.getErrorCount(); // 处理的总的失败次数
			final long processingTime = tomcatInfo.getProcessingTime(); // 处理所有请求的总的时间
			// 记录当前配置线程池最大线程数
			AtomicLongCounter maxThreadCounter = fetchLongMonitorByName(
					makeName(TOMCAT_PREFIX + tomcatThreadPoolName
							+ "_maxThread" + VALUE_SUFFIX), "TOMCAT");
			maxThreadCounter.set(maxThreads);
			// 记录当前线程池线程数
			AtomicLongCounter currentThreadCounter = fetchLongMonitorByName(
					makeName(TOMCAT_PREFIX + tomcatThreadPoolName
							+ "_currentThread" + VALUE_SUFFIX), "TOMCAT");
			currentThreadCounter.set(currentThreadCount);
			// 记录当前线程池线程繁忙数
			AtomicLongCounter currentThreadBusyCounter = fetchLongMonitorByName(
					makeName(TOMCAT_PREFIX + tomcatThreadPoolName
							+ "_currentThreadsBusy" + VALUE_SUFFIX), "TOMCAT");
			currentThreadBusyCounter.set(currentThreadsBusy);
			// 记录指定时间段内收到的字节数
			addComputedCounter(makeName(TOMCAT_PREFIX + tomcatThreadPoolName
					+ "_bytesReceived"), "TOMCAT", bytesRecv, 0, false);
			// 记录指定时间段内发送出的字节数
			addComputedCounter(makeName(TOMCAT_PREFIX + tomcatThreadPoolName
					+ "_bytesSent"), "TOMCAT", bytesSent, 0, false);
			// 记录指定时间段内处理的请求个数以及平均响应时间
			addComputedCounter(makeName(TOMCAT_PREFIX + tomcatThreadPoolName
					+ "_request"), "TOMCAT", requestCount, processingTime, true);
			// 记录指定时间段内的失败请求个数
			addComputedCounter(makeName(TOMCAT_PREFIX + tomcatThreadPoolName
					+ "_error"), "TOMCAT", errorCount, 0, false);
		}
	}

	/**
	 * 根据名称获取或者创建一个AtomicLongCounter
	 * 
	 * @param name
	 * @param description
	 * @return
	 */
	private static AtomicLongCounter fetchLongMonitorByName(String name,
			String description) {
		AtomicLongCounter counter = (AtomicLongCounter) VinMonitor
				.getMonitor(name);
		if (counter == null) {
			if (qMonitorKeyMap.putIfAbsent(name, Boolean.TRUE) == null) {
				counter = VinMonitor.createAtomicLongCounter(name,
						description);
			} else {
				counter = (AtomicLongCounter) VinMonitor.getMonitor(name);
			}

		}
		return counter;
	}

	private static class MonitorItem {

		private long count, time;

		public synchronized void add(long count, long time) {
			this.count = this.count + count;
			this.time = this.time + time;
		}

		// public synchronized MonitorItem dumpAndClearItem() {
		// MonitorItem item = new MonitorItem();
		// item.count = count;
		// item.time = time;
		// count = 0L;
		// time = 0L;
		// return item;
		// }

		private MonitorItem() {
		}

		MonitorItem(MonitorItem monitoritem) {
			this();
		}
	}

	private QMonitor() {
	}

	public static void recordTimeStart() {
		lastSMonitorStartTime.set(SystemTimer.currentTimeMillis());
	}

	public static void recordTimeEnd(String name) {
		long now = SystemTimer.currentTimeMillis();
		recordMany(name, 1, now - lastSMonitorStartTime.get());
	}

	/**
	 * 计算平均时间
	 * 
	 * @param name
	 * @param time
	 */
	public static void recordOne(String name, long time) {
		recordMany(makeName(name), 1L, time);
	}

	/**
	 * 计数器，
	 * 
	 * @param name
	 *            要自增的key
	 */
	public static void recordOne(String name) {
		incrementByCount(makeName(name), 1L);
	}

	/**
	 * 计数器，
	 * 
	 * @param name
	 *            要自减的key
	 */
	public static void decrRecord(String name) {
		incrementByCount(makeName(name), -1L);
	}

	public static void incrRecord(String name, long count) {
		incrementByCount(makeName(name), count);
	}
	
	public static void incrRecord(String name, long count, long time){
		recordMany(makeName(name), count, time);
	}
	/**
     * 注册一个比例计算(只针对Count类型的监控值)，如果指定的比例监控name已经存在，do nothing，最终生成的监控项是以_Value结尾
     * 
     * @param name
     *            比例监控name
     * @param desc
     *            比例监控描述
     * @param denominatorKey
     *            作为分子的监控name
     * @param numeratorKey
     *            作为父母的监控name
     */
	public static void generateRate(String name, String desc,
			String denominatorKey, String numeratorKey) {
		registerRateCalculator(makeName(name), desc, makeName(denominatorKey),
				makeName(numeratorKey),true);
	}
	/**
	 * 注册成功率，当分母为0,成功率为1
	 * key会自动加后缀_SuccessRate
	 * @param name
	 * @param desc
	 * @param denominatorKey
	 * @param numeratorKey
	 */
	public static void generateSuccessRate(String name, String desc,
            String denominatorKey, String numeratorKey) {
        registerRateCalculator(makeName(name)+"_SuccessRate", desc, makeName(denominatorKey),
                makeName(numeratorKey),true);
    }
	/**
	 * 注册失败率，当分母为0时，失败率为0
	 * key会自动加后缀__FaildRate
	 * @param name
	 * @param desc
	 * @param denominatorKey
	 * @param numeratorKey
	 */
	public static void generateFaildRate(String name, String desc,
            String denominatorKey, String numeratorKey) {
        registerRateCalculator(makeName(name)+"_FaildRate", desc, makeName(denominatorKey),
                makeName(numeratorKey),false);
    }
	private static String makeKey(String prefix, String key, String suffix) {
		return prefix + "." + key + suffix;

	}

	/**
	 * 计数器，对name指定的监控值每次增加count大小
	 * 
	 * @param name
	 * @param count
	 */
	private static void incrementByCount(final String name, long count) {
		mutiLongMonitor.increment(name + INNER_COUNT_SUFFIX, (int) count);
		if (qMonitorKeyMap.putIfAbsent(name, Boolean.TRUE) == null) {
			// 一份中内的次数
			VinMonitor.addComputerMonitor(name + COUNT_SUFFIX, "计数器",
					new ComputerMonitor() {
						@Override
						public Number getValue() {
							return this.getLast1Minutes(makeKey(
									QMONITOR_KEY_PREFIX, name,
									INNER_COUNT_SUFFIX));
						}
					});
		}
	}

	/**
	 * 注册一个比例计算(只针对Count类型的监控值)，如果指定的比例监控name已经存在，do nothing，最终生成的监控项是以_Value结尾
	 * <p>
	 * 注意：如果分母的值为0,则比例值为100%
	 * 所以更适合用来做成功率的监控。失败率也可以转换成成功率。
	 * </p>
	 * 
	 * @param name
	 *            比例监控name
	 * @param desc
	 *            比例监控描述
	 * @param denominatorKey
	 *            作为分子的监控name
	 * @param numeratorKey
	 *            作为父母的监控name
	 */
	private static void registerRateCalculator(final String name,
			final String desc, final String denominatorKey,
			final String numeratorKey,boolean isSucessRate) {
		VinMonitor.addComputerMonitor(
				name + VALUE_SUFFIX,
				desc,
				new RateComputerIncrementMonitor(makeKey(QMONITOR_KEY_PREFIX,
						denominatorKey, INNER_COUNT_SUFFIX), makeKey(
						QMONITOR_KEY_PREFIX, numeratorKey, INNER_COUNT_SUFFIX),
						1, isSucessRate));
	}

	private static void recordMany(final String name, long count, long time) {
		mutiLongMonitor.increment(name + INNER_COUNT_SUFFIX, (int) count);
		mutiLongMonitor.increment(name + INNER_TIME_SUFFIX, (int) time);
		if (qMonitorKeyMap.putIfAbsent(name, Boolean.TRUE) == null) {
			// 一份中内的平均时间
			VinMonitor.addComputerMonitor(name + TIME_SUFFIX, "平均耗时",
					new ComputerMonitor() {
						@Override
						public Number getValue() {
							long count = this.getLast1Minutes(
									makeKey(QMONITOR_KEY_PREFIX, name,
											INNER_COUNT_SUFFIX)).longValue();
							long time = this.getLast1Minutes(
									makeKey(QMONITOR_KEY_PREFIX, name,
											INNER_TIME_SUFFIX)).longValue();
							if (count > 0L) {
								return time / count;
							} else {
								return 0;
							}
						}
					});
			// 一份中内的次数
			VinMonitor.addComputerMonitor(name + COUNT_SUFFIX, "计数器",
					new ComputerMonitor() {
						@Override
						public Number getValue() {
							return this.getLast1Minutes(makeKey(
									QMONITOR_KEY_PREFIX, name,
									INNER_COUNT_SUFFIX));
						}
					});

		}
	}

	/**
	 * 绝对值监控
	 * 
	 * @param name
	 *            监控的key
	 * @param size
	 *            要set的值
	 */
	public static void recordSize(String name, long size) {
		AtomicLongCounter counter = fetchLongMonitorByName(name + VALUE_SUFFIX,
				"绝对值监控");
		counter.set((int) size);
	}

	/**
	 * 绝对值监控
	 * 
	 * @param name
	 *            监控的key
	 * @param count
	 *            对绝对值增加cout
	 */
	public static void recordValue(String name, long count) {
		AtomicLongCounter counter = fetchLongMonitorByName(name + VALUE_SUFFIX,
				"绝对值监控");
		counter.increment((int) count);
	}

	public static Map<String, Number> getValues() {
		Map<String, Number> monitorData = VinMonitor.getMonitorData("*");
		Iterator<Entry<String, Number>> iterator = monitorData.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, Number> entry = iterator.next();
			String key = StringUtils.trimToNull(entry.getKey());
			if (key == null || key.isEmpty()) {
				iterator.remove();
				continue;
			}
			if (!key.endsWith(VALUE_SUFFIX) && !key.endsWith(COUNT_SUFFIX)
					&& !key.endsWith(TIME_SUFFIX)) {
				iterator.remove();
				continue;
			}
		}
		return Collections.unmodifiableMap(monitorData);
	}
}
