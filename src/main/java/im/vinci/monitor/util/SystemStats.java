package im.vinci.monitor.util;

import im.vinci.monitor.ThreadCpuUsageBean;
import im.vinci.monitor.ThreadCpuUsageItem;
import im.vinci.monitor.ThreadReporter;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 获取系统信息
 * 
 * @author sunli
 */
public class SystemStats {
    private static final OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
    private static final String CRLF = "\r\n";

    public SystemStats() {

    }

    /**
     * 获取系统负载
     * 
     * @return
     */
    public static double getSystemLoad() {
        if (!(bean instanceof com.sun.management.OperatingSystemMXBean)) {
            return 0L;
        } else {
            return bean.getSystemLoadAverage();
        }
    }

    /**
     * 获取CPU个数
     * 
     * @return
     */
    public static int getAvailableProcessors() {
        if (!(bean instanceof com.sun.management.OperatingSystemMXBean)) {
            return 0;
        } else {
            return bean.getAvailableProcessors();
        }
    }

    /**
     * 获取所有的线程数
     * 
     * @return
     */
    public static int getAllThreadsCount() {
        return ManagementFactory.getThreadMXBean().getThreadCount();
    }

    public static String formatCpuUsageOutput(long tid, String threadName, double[] usages) {
        return String.format("usage=%.2f,user=%.2f,sys=%.2f,id=%d,name=%s", 100 * usages[2], 100 * usages[0],
                100 * usages[1], tid, threadName);
    }

    /**
     * dump所有的线程状态
     * 
     * @return
     */
    public static String getAllThreadStackTrace() {
        StringBuilder sb = new StringBuilder();
        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
        for (Thread thread : map.keySet()) {
            StackTraceElement[] stackTraceElements = map.get(thread);
            sb.append("\r\n************threadName:");
            sb.append(thread.getName());
            sb.append("     threadId:");
            sb.append(thread.getId());
            sb.append("************\r\n");
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                sb.append(stackTraceElement.toString()).append(CRLF);
            }
        }
        return sb.toString();
    }

    /**
     * 获取虚拟机的内存使用情况
     * 
     * @return
     */
    public static String getJvmMemory() {
        MemoryMXBean ml = ManagementFactory.getMemoryMXBean();
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("Heap:\r\n");
        sBuilder.append(ml.getHeapMemoryUsage().toString());
        sBuilder.append("\r\nNonHeap:\r\n");
        sBuilder.append(ml.getNonHeapMemoryUsage().toString());
        return sBuilder.toString();
    }

    /**
     * 获取死锁
     * 
     * @return
     */
    public static String getDeadLock() {
        ThreadMXBean th = ManagementFactory.getThreadMXBean();
        long[] ll = th.findMonitorDeadlockedThreads();
        if (ll == null) {
            return "";
        }
        ThreadInfo[] tia = th.getThreadInfo(ll, Integer.MAX_VALUE);
        StringBuffer sb = new StringBuffer();
        sb.append("findMonitorDeadlockedThreads Info :\r\n");
        for (int i = 0; i < tia.length; i++) {
            ThreadInfo ti = tia[i];
            StackTraceElement[] ste = ti.getStackTrace();
            for (int j = 0; j < ste.length; j++) {
                sb.append(ste[j].toString());
                sb.append(CRLF);
            }
            sb.append(CRLF);
            sb.append(CRLF);
        }
        return sb.toString();
    }

    public static String getThreadStackTrace(long tid) {
        StringBuilder result = new StringBuilder();
        try {
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            ThreadInfo threadInfo = threadMXBean.getThreadInfo(tid, Integer.MAX_VALUE);
            if (threadInfo != null) {
                StackTraceElement[] traces = threadInfo.getStackTrace();
                boolean isFirst = true;
                for (StackTraceElement trace : traces) {
                    if (isFirst) {
                        isFirst = false;
                    } else {
                        result.append("\n");
                    }
                    result.append(trace.toString());
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static List<ThreadCpuUsageItem> dumpThreadUsages() {
        Map<Long, ThreadCpuUsageBean> usageBeanMap = ThreadReporter.getInstance().dumpThreadCpuUsage();
        List<ThreadCpuUsageItem> usageBeanList = new ArrayList<ThreadCpuUsageItem>();
        for (Entry<Long, ThreadCpuUsageBean> entry : usageBeanMap.entrySet()) {
            String threadName = ThreadReporter.getInstance().getThreadName(entry.getKey());
            ThreadCpuUsageBean usageBean = entry.getValue();
            double[] usages = usageBean.getCurrentCpuUsage();
            if (usages[2] > 0D) {
                usageBeanList.add(new ThreadCpuUsageItem(threadName, entry.getKey(), usages));
            }
        }
        return usageBeanList;
    }
}
