
package im.vinci.monitor;

import org.apache.commons.lang3.StringUtils;

import javax.management.*;
import java.lang.management.*;
import java.util.*;

/**
 * JVM监控类
 * 
 * @author sunli
 */
public class JVMMonitor {
    private static final OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
    private static final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    private static final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private static final long maxMemory = Runtime.getRuntime().maxMemory();
    private static final ClassLoadingMXBean classLoadingBean = ManagementFactory.getClassLoadingMXBean();
    private static final List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory
            .getGarbageCollectorMXBeans();
    private static final List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
    private static final Set<String> edenSpace = new HashSet<String>();
    private static final Set<String> survivorSpace = new HashSet<String>();
    private static final Set<String> oldSpace = new HashSet<String>();
    private static final Set<String> permSpace = new HashSet<String>();
    private static final Set<String> codeCacheSpace = new HashSet<String>();
    private static final List<String> youngGenCollectorNames = new ArrayList<String>();
    private static final List<String> fullGenCollectorNames = new ArrayList<String>();
    private static final String emptyFileDescriptor = "MaxFileDescriptorCount:0\r\nOpenFileDescriptorCount:0";
    private static final String CRLF = "\r\n";
    static {
        // 各种GC下的eden名字
        edenSpace.add("Eden Space");// -XX:+UseSerialGC
        edenSpace.add("PS Eden Space");// –XX:+UseParallelGC
        edenSpace.add("Par Eden Space");// -XX:+UseConcMarkSweepGC
        edenSpace.add("Par Eden Space");// -XX:+UseParNewGC
        edenSpace.add("PS Eden Space");// -XX:+UseParallelOldGC
        // 各种gc下survivorSpace的名字
        survivorSpace.add("Survivor Space");// -XX:+UseSerialGC
        survivorSpace.add("PS Survivor Space");// –XX:+UseParallelGC
        survivorSpace.add("Par Survivor Space");// -XX:+UseConcMarkSweepGC
        survivorSpace.add("Par survivor Space");// -XX:+UseParNewGC
        survivorSpace.add("PS Survivor Space");// -XX:+UseParallelOldGC
        // 各种gc下oldspace的名字
        oldSpace.add("Tenured Gen");// -XX:+UseSerialGC
        oldSpace.add("PS Old Gen");// –XX:+UseParallelGC
        oldSpace.add("CMS Old Gen");// -XX:+UseConcMarkSweepGC
        oldSpace.add("Tenured Gen  Gen");// Tenured Gen Gen
        oldSpace.add("PS Old Gen");// -XX:+UseParallelOldGC

        // 各种gc下持久代的名字
        permSpace.add("Perm Gen");// -XX:+UseSerialGC
        permSpace.add("PS Perm Gen");// –XX:+UseParallelGC
        permSpace.add("CMS Perm Gen");// -XX:+UseConcMarkSweepGC
        permSpace.add("Perm Gen");// -XX:+UseParNewGC
        permSpace.add("PS Perm Gen");// -XX:+UseParallelOldGC
        // codeCache的名字
        codeCacheSpace.add("Code Cache");
        // Oracle (Sun) HotSpot
        // -XX:+UseSerialGC
        youngGenCollectorNames.add("Copy");
        // -XX:+UseParNewGC
        youngGenCollectorNames.add("ParNew");
        // -XX:+UseParallelGC
        youngGenCollectorNames.add("PS Scavenge");
        // Oracle (Sun) HotSpot
        // -XX:+UseSerialGC
        fullGenCollectorNames.add("MarkSweepCompact");
        // -XX:+UseParallelGC and (-XX:+UseParallelOldGC or -XX:+UseParallelOldGCCompacting)
        fullGenCollectorNames.add("PS MarkSweep");
        // -XX:+UseConcMarkSweepGC
        fullGenCollectorNames.add("ConcurrentMarkSweep");

    }

    public JVMMonitor() {

    }

    /**
     * @return MBeanServer
     */
    static MBeanServer getPlatformMBeanServer() {
        return ManagementFactory.getPlatformMBeanServer();
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
     * 返回文件描述符数
     * 
     * @return
     */
    public static String getFileDescriptor() {
        try {
            String[] attributeNames = new String[] { "MaxFileDescriptorCount", "OpenFileDescriptorCount" };
            ObjectName name = new ObjectName("java.lang:type=OperatingSystem");
            AttributeList attributes = getPlatformMBeanServer().getAttributes(name, attributeNames);
            StringBuilder sb = new StringBuilder();
            for (int i = 0, len = attributes.size(); i < len; i++) {
                if (sb.length() > 0) {
                    sb.append(CRLF);
                }
                sb.append(attributes.get(i).toString().replace(" = ", ":"));
            }
            return sb.toString();
        } catch (MalformedObjectNameException e) {
            return emptyFileDescriptor;
        } catch (InstanceNotFoundException e) {
            return emptyFileDescriptor;
        } catch (ReflectionException e) {
            return emptyFileDescriptor;
        }
    }

    /**
     * 获取所有的线程数
     * 
     * @return
     */
    public static int getAllThreadsCount() {
        return threadBean.getThreadCount();
    }

    /**
     * 获取峰值线程数
     * 
     * @return
     */
    public static int getPeakThreadCount() {
        return threadBean.getPeakThreadCount();
    }

    /**
     * Returns the current number of live daemon threads.
     * 
     * @return the current number of live daemon threads.
     */
    public static int getDaemonThreadCount() {
        return threadBean.getDaemonThreadCount();
    }

    /**
     * 获取启动以来创建的线程数
     * 
     * @return
     */
    public static long getTotalStartedThreadCount() {
        return threadBean.getTotalStartedThreadCount();
    }

    /**
     * 获取死锁数
     * 
     * @return 死锁数
     */
    public static int getDeadLockCount() {
        ThreadMXBean th = ManagementFactory.getThreadMXBean();
        long[] deadLockIds = th.findMonitorDeadlockedThreads();
        if (deadLockIds == null) {
            return 0;
        } else {
            return deadLockIds.length;
        }

    }

    /**
     * 获取虚拟机的heap内存使用情况
     * 
     * @return
     */
    public static MemoryUsage getJvmHeapMemory() {
        return memoryMXBean.getHeapMemoryUsage();

    }

    /**
     * 获取虚拟机的noheap内存使用情况
     * 
     * @return
     */
    public static MemoryUsage getJvmNoHeapMemory() {
        return memoryMXBean.getNonHeapMemoryUsage();

    }

    /**
     * 获取当前JVM占用的总内存
     * 
     * @return
     */
    public static long getTotolMemory() {
        long totalMemory = Runtime.getRuntime().totalMemory();

        return totalMemory;
    }

    /**
     * 获取当前JVM给应用分配的内存
     * 
     * @return
     */
    public static long getUsedMemory() {
        long totalMemory = Runtime.getRuntime().totalMemory();
        return totalMemory - Runtime.getRuntime().freeMemory();
    }

    /**
     * 获取JVM能使用到的最大内存
     * 
     * @return
     */
    public static long getMaxUsedMemory() {
        return maxMemory;
    }

    /**
     * 获取启动以来加载的总的class数
     * 
     * @return
     */
    public static long getTotalLoadedClassCount() {
        return classLoadingBean.getTotalLoadedClassCount();
    }

    /**
     * 获取当前JVM加载的class数
     * 
     * @return
     */
    public static int getLoadedClassCount() {
        return classLoadingBean.getLoadedClassCount();
    }

    /**
     * 获取JVM被启动以来unload的class数
     * 
     * @return
     */
    public static long getUnloadedClassCount() {

        return classLoadingBean.getUnloadedClassCount();
    }

    /**
     * 获取GC的时间
     * 
     * @return
     */
    public static String getGcTime() {
        StringBuilder sb = new StringBuilder();
        for (GarbageCollectorMXBean gcbean : garbageCollectorMXBeans) {
            if (sb.length() > 0) {
                sb.append(CRLF);
            }
            if (youngGenCollectorNames.contains(gcbean.getName())) {
                sb.append("youngGCCount:");
                sb.append(gcbean.getCollectionCount());
                sb.append(CRLF);
                sb.append("youngGCTime:");
                sb.append(gcbean.getCollectionTime());
            } else if (fullGenCollectorNames.contains(gcbean.getName())) {
                sb.append("fullGCCount:");
                sb.append(gcbean.getCollectionCount());
                sb.append(CRLF);
                sb.append("fullGCTime:");
                sb.append(gcbean.getCollectionTime());
            }

        }

        return sb.toString();
    }

    public static Map<String, MemoryUsage> getMemoryPoolCollectionUsage() {
        Map<String, MemoryUsage> gcMemory = new HashMap<String, MemoryUsage>();
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            String name = memoryPoolMXBean.getName();
            if (edenSpace.contains(name)) {
                gcMemory.put("eden", memoryPoolMXBean.getCollectionUsage());
            } else if (survivorSpace.contains(name)) {
                gcMemory.put("survivor", memoryPoolMXBean.getCollectionUsage());
            } else if (oldSpace.contains(name)) {
                gcMemory.put("old", memoryPoolMXBean.getCollectionUsage());
            } else if (permSpace.contains(name)) {
                gcMemory.put("perm", memoryPoolMXBean.getCollectionUsage());
            } else if (codeCacheSpace.contains(name)) {
                gcMemory.put("codeCache", memoryPoolMXBean.getCollectionUsage());
            }

        }
        return gcMemory;
    }

    public static Map<String, MemoryUsage> getMemoryPoolUsage() {
        Map<String, MemoryUsage> gcMemory = new HashMap<String, MemoryUsage>();
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            String name = memoryPoolMXBean.getName();
            if (edenSpace.contains(name)) {
                gcMemory.put("eden", memoryPoolMXBean.getUsage());
            } else if (survivorSpace.contains(name)) {
                gcMemory.put("survivor", memoryPoolMXBean.getUsage());
            } else if (oldSpace.contains(name)) {
                gcMemory.put("old", memoryPoolMXBean.getUsage());
            } else if (permSpace.contains(name)) {
                gcMemory.put("perm", memoryPoolMXBean.getUsage());
            } else if (codeCacheSpace.contains(name)) {
                gcMemory.put("codeCache", memoryPoolMXBean.getUsage());
            }

        }
        return gcMemory;
    }

    public static Map<String, MemoryUsage> getMemoryPoolPeakUsage() {
        Map<String, MemoryUsage> gcMemory = new HashMap<String, MemoryUsage>();
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            String name = memoryPoolMXBean.getName();
            if (edenSpace.contains(name)) {
                gcMemory.put("eden", memoryPoolMXBean.getPeakUsage());
            } else if (survivorSpace.contains(name)) {
                gcMemory.put("survivor", memoryPoolMXBean.getPeakUsage());
            } else if (oldSpace.contains(name)) {
                gcMemory.put("old", memoryPoolMXBean.getPeakUsage());
            } else if (permSpace.contains(name)) {
                gcMemory.put("perm", memoryPoolMXBean.getPeakUsage());
            } else if (codeCacheSpace.contains(name)) {
                gcMemory.put("codeCache", memoryPoolMXBean.getPeakUsage());
            }

        }
        return gcMemory;
    }

    /**
     * 返回指定的监控项目
     * 
     * @param item 监控项
     * @return
     */
    public static String getMonitorStats(String item) {
        if ("load".equals(item)) {
            return "load:" + JVMMonitor.getSystemLoad();
        } else if ("allThreadsCount".equals(item)) {
            return "allThreadsCount:" + JVMMonitor.getAllThreadsCount();
        } else if ("peakThreadCount".equals(item)) {
            return "peakThreadCount:" + JVMMonitor.getPeakThreadCount();
        } else if ("daemonThreadCount".equals(item)) {
            return "daemonThreadCount:" + JVMMonitor.getDaemonThreadCount();
        } else if ("totalStartedThreadCount".equals(item)) {
            return "totalStartedThreadCount:" + JVMMonitor.getTotalStartedThreadCount();
        } else if ("deadLockCount".equals(item)) {
            return "deadLockCount:" + JVMMonitor.getDeadLockCount();
        } else if ("heapMemory".equals(item)) {
            MemoryUsage memoryUsage = JVMMonitor.getJvmHeapMemory();
            return "used:" + memoryUsage.getUsed() + CRLF + "committed:" + memoryUsage.getCommitted() + CRLF + "max:"
                    + memoryUsage.getMax();
        } else if ("noHeapMemory".equals(item)) {
            MemoryUsage memoryUsage = JVMMonitor.getJvmNoHeapMemory();
            return "used:" + memoryUsage.getUsed() + CRLF + "committed:" + memoryUsage.getCommitted() + CRLF + "max:"
                    + memoryUsage.getMax();
        } else if ("memory".equals(item)) {
            return "totolMemory:" + JVMMonitor.getTotolMemory() + CRLF + "used:" + JVMMonitor.getUsedMemory() + CRLF
                    + "maxUsedMemory:" + JVMMonitor.getMaxUsedMemory();
        } else if ("classCount".equals(item)) {
            return "totalLoadedClassCount:" + JVMMonitor.getTotalLoadedClassCount() + CRLF + "loadedClassCount:"
                    + JVMMonitor.getLoadedClassCount() + CRLF + "unloadedClassCount:"
                    + JVMMonitor.getUnloadedClassCount();
        } else if ("GCTime".equals(item)) {
            return JVMMonitor.getGcTime();
        } else if ("memoryPoolCollectionUsage".equals(item)) {
            Map<String, MemoryUsage> gcMap = JVMMonitor.getMemoryPoolCollectionUsage();
            StringBuilder sb = new StringBuilder();
            for (String key : gcMap.keySet()) {
                if (sb.length() > 0) {
                    sb.append(CRLF);
                }
                MemoryUsage memoryUsage = gcMap.get(key);
                if (memoryUsage == null) {
                    memoryUsage = new MemoryUsage(0, 0, 0, 0);
                }
                if (memoryUsage != null) {
                    sb.append(key);
                    sb.append("Init:");
                    sb.append(memoryUsage.getInit());
                    sb.append(CRLF);
                    sb.append(key);
                    sb.append("Used:");
                    sb.append(memoryUsage.getUsed());
                    sb.append(CRLF);
                    sb.append(key);
                    sb.append("Committed:");
                    sb.append(memoryUsage.getCommitted());
                    sb.append(CRLF);
                    sb.append(key);
                    sb.append("Max:");
                    sb.append(memoryUsage.getMax());
                }

            }
            return sb.toString();
        } else if ("memoryPoolUsage".equals(item)) {
            Map<String, MemoryUsage> gcMap = JVMMonitor.getMemoryPoolUsage();
            StringBuilder sb = new StringBuilder();
            for (String key : gcMap.keySet()) {
                if (sb.length() > 0) {
                    sb.append(CRLF);
                }
                MemoryUsage memoryUsage = gcMap.get(key);
                if (memoryUsage == null) {
                    memoryUsage = new MemoryUsage(0, 0, 0, 0);
                }
                if (memoryUsage != null) {
                    sb.append(key);
                    sb.append("Init:");
                    sb.append(memoryUsage.getInit());
                    sb.append(CRLF);
                    sb.append(key);
                    sb.append("Used:");
                    sb.append(memoryUsage.getUsed());
                    sb.append(CRLF);
                    sb.append(key);
                    sb.append("Committed:");
                    sb.append(memoryUsage.getCommitted());
                    sb.append(CRLF);
                    sb.append(key);
                    sb.append("Max:");
                    sb.append(memoryUsage.getMax());
                }
            }
            return sb.toString();
        } else if ("memoryPoolPeakUsage".equals(item)) {
            Map<String, MemoryUsage> gcMap = JVMMonitor.getMemoryPoolPeakUsage();
            StringBuilder sb = new StringBuilder();
            for (String key : gcMap.keySet()) {
                if (sb.length() > 0) {
                    sb.append(CRLF);
                }
                MemoryUsage memoryUsage = gcMap.get(key);
                if (memoryUsage == null) {
                    memoryUsage = new MemoryUsage(0, 0, 0, 0);
                }
                if (memoryUsage != null) {
                    sb.append(key);
                    sb.append("Init:");
                    sb.append(memoryUsage.getInit());
                    sb.append(CRLF);
                    sb.append(key);
                    sb.append("Used:");
                    sb.append(memoryUsage.getUsed());
                    sb.append(CRLF);
                    sb.append(key);
                    sb.append("Committed:");
                    sb.append(memoryUsage.getCommitted());
                    sb.append(CRLF);
                    sb.append(key);
                    sb.append("Max:");
                    sb.append(memoryUsage.getMax());
                }

            }
            return sb.toString();
        } else if ("tomcat".equals(item)) {
            List<TomcatInformations> list = TomcatInformations.buildTomcatInformationsList();
            StringBuilder sb = new StringBuilder();
            for (int i = 0, len = list.size(); i < len; i++) {
                sb.append("name:" + list.get(i).getName());
                sb.append("\r\nbytesReceived:" + list.get(i).getBytesReceived());
                sb.append("\r\nbytesSent:" + list.get(i).getBytesSent());
                sb.append("\r\ncurrentThreadCount:" + list.get(i).getCurrentThreadCount());
                sb.append("\r\ncurrentThreadsBusy:" + list.get(i).getCurrentThreadsBusy());
                sb.append("\r\nerrorCount:" + list.get(i).getErrorCount());
                sb.append("\r\nmaxThreads:" + list.get(i).getMaxThreads());
                sb.append("\r\nmaxTime:" + list.get(i).getMaxTime());
                sb.append("\r\nprocessingTime:" + list.get(i).getProcessingTime());
                sb.append("\r\nrequestCount:" + list.get(i).getRequestCount());
            }
            return sb.toString();
        } else if (item.startsWith("tomcat_")) {
            String[] names = StringUtils.split(item, '_');
            if (names.length != 2) {
                return "";
            }
            String tomcatName = names[1];
            List<TomcatInformations> list = TomcatInformations.buildTomcatInformationsList();
            StringBuilder sb = new StringBuilder();
            for (int i = 0, len = list.size(); i < len; i++) {
                if (tomcatName.equals(list.get(i).getName())) {
                    sb.append("bytesReceived:" + list.get(i).getBytesReceived());
                    sb.append("\r\nbytesSent:" + list.get(i).getBytesSent());
                    sb.append("\r\ncurrentThreadCount:" + list.get(i).getCurrentThreadCount());
                    sb.append("\r\ncurrentThreadsBusy:" + list.get(i).getCurrentThreadsBusy());
                    sb.append("\r\nerrorCount:" + list.get(i).getErrorCount());
                    sb.append("\r\nmaxThreads:" + list.get(i).getMaxThreads());
                    sb.append("\r\nmaxTime:" + list.get(i).getMaxTime());
                    sb.append("\r\nprocessingTime:" + list.get(i).getProcessingTime());
                    sb.append("\r\nrequestCount:" + list.get(i).getRequestCount());
                }
            }
            return sb.toString();
        } else if ("fileDescriptor".equals(item)) {
            return getFileDescriptor();
        }
        return "command not found";
    }
}
