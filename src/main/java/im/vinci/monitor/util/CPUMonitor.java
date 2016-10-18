package im.vinci.monitor.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class CPUMonitor {
    private static final OperatingSystemMXBean bean = ManagementFactory.getOperatingSystemMXBean();
  
    private int availableProcessors = bean.getAvailableProcessors();

    private long lastSystemTime = 0;

    private long lastProcessCpuTime = 0;

    public synchronized double getCpuUsage() {
        if (lastSystemTime == 0) {
            baselineCounters();
            return 0;
        }

        long systemTime = System.nanoTime();
        long processCpuTime = 0;

        if (bean instanceof com.sun.management.OperatingSystemMXBean) {
            processCpuTime = ((com.sun.management.OperatingSystemMXBean) bean).getProcessCpuTime();
        }

        double cpuUsage = (double) (processCpuTime - lastProcessCpuTime) / (systemTime - lastSystemTime);

        lastSystemTime = systemTime;
        lastProcessCpuTime = processCpuTime;

        return cpuUsage / availableProcessors;
    }

    private void baselineCounters() {
        lastSystemTime = System.nanoTime();
        if (bean instanceof com.sun.management.OperatingSystemMXBean) {
            lastProcessCpuTime = ((com.sun.management.OperatingSystemMXBean) bean).getProcessCpuTime();
        }
    }
}