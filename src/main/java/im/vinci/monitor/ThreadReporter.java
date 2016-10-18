/**
 * 
 */
package im.vinci.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 监控线程的cpu使用时间
 * 
 * @author liuyue
 * 
 */
public class ThreadReporter implements Runnable {
    // thread id-> thread cpu usage bean的映射
    private final Map<Long, ThreadCpuUsageBean> threadUsageBeanMap = new ConcurrentHashMap<Long, ThreadCpuUsageBean>();
    private final Map<Long, String> threadId2NameMap = new ConcurrentHashMap<Long, String>();
    private final AtomicLong lastCalcTime = new AtomicLong(System.nanoTime());
    private static final ThreadReporter instance = new ThreadReporter();

    private ThreadReporter() {
    }

    @Override
    public void run() {
        try {
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            ThreadInfo[] threads = threadMXBean.dumpAllThreads(false, false);
            Set<Long> threadIdSet = new HashSet<Long>();

            if (threads != null && threads.length > 0) {
                long currentTime = System.nanoTime();
                long timeSplit = currentTime - lastCalcTime.getAndSet(currentTime);
                // 遍历每个ThreadInfo，得到每个thread的运行情况
                for (ThreadInfo threadInfo : threads) {
                    long threadId = threadInfo.getThreadId();
                    if (threadInfo.getThreadState() == Thread.State.TERMINATED) {
                        continue;
                    }
                    threadIdSet.add(threadId);
                    long cpuTime = threadMXBean.getThreadCpuTime(threadId);
                    long userTime = threadMXBean.getThreadUserTime(threadId);
                    ThreadCpuUsageBean usageBean = threadUsageBeanMap.get(threadId);
                    threadId2NameMap.put(threadId, threadInfo.getThreadName());
                    if (usageBean == null) {
                        threadUsageBeanMap.put(threadId, usageBean = new ThreadCpuUsageBean());
                    }
                    usageBean.computeCpuUsage(cpuTime, userTime, (cpuTime - userTime), timeSplit);
                }
                Iterator<Entry<Long, ThreadCpuUsageBean>> iter = threadUsageBeanMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Long key = iter.next().getKey();
                    if (!threadIdSet.contains(key)) {
                        iter.remove();
                    }
                }
                // 移除已经不存在的线程占用的threadId2NameMap，解决余朝晖报的内存泄露问题
                Iterator<Entry<Long, String>> iterator = threadId2NameMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Long key = iterator.next().getKey();
                    if (!threadIdSet.contains(key)) {
                        iterator.remove();
                    }
                }

            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Map<Long, ThreadCpuUsageBean> dumpThreadCpuUsage() {
        return Collections.unmodifiableMap(threadUsageBeanMap);
    }

    public String getThreadName(long threadId) {
        return threadId2NameMap.get(threadId);
    }

    public static ThreadReporter getInstance() {
        return instance;
    }
}
