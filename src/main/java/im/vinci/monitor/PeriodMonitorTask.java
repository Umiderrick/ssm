package im.vinci.monitor;

import im.vinci.monitor.util.LastData;
import im.vinci.monitor.util.SystemTimer;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author sunli
 */
public class PeriodMonitorTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PeriodMonitorTask.class);
    private static final ConcurrentHashMap<String, LastData<Number>> lastValueMap = MonitorContainer.getLastvaluemap();
    private static final ConcurrentHashMap<String, LastData<Number>> lastMonthValueMap = MonitorContainer
            .getLastmonthvaluemap();
    private Monitor counter;
    private String monitorKey;
    private String currentDate = "0";
    private long lastRunTime = 0; // 上一次运行计算定时任务的时间

    public PeriodMonitorTask(Monitor counter) {
        this.counter = counter;
        this.monitorKey = counter.getInstanceName();
        lastValueMap.putIfAbsent(this.monitorKey, new LastData<Number>());
        lastMonthValueMap.putIfAbsent(this.monitorKey, new LastData<Number>());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            long now = SystemTimer.currentTimeMillis();
            // 间隔大于50秒才执行
            if ((now - lastRunTime) < TimeUnit.MILLISECONDS.convert(50, TimeUnit.SECONDS)) {
                return;
            }
            long seconds = DateUtils.getFragmentInSeconds(new Date(now), Calendar.MINUTE);
            // 保证在没分钟的前10秒开始执行,抓取服务可以在后30秒进行，这样可以避免抓取和task的时间交叉
            if (seconds > 10) {
                return;
            }
            lastRunTime = now;
            processTask();

        } catch (Exception e) {
            logger.error("PeriodMonitorTask  error fro key:" + this.monitorKey, e);
        }

    }

    public void processTask() {
        // 当前值
        Number valueObject = counter.getValue();
        // 获取counter的历史数据存储对象
        LastData<Number> lastData = lastValueMap.get(this.monitorKey);
        if (lastData == null) {
            lastData = new LastData<Number>();
            LastData<Number> existLastData = lastValueMap.putIfAbsent(this.monitorKey, lastData);
            if (existLastData != null) {
                lastData = existLastData;
            }

        }
        // 存储当前counter的数据到历史数据存储对象
        lastData.add(valueObject);
        // 存储按天的区间数据
        String today = SystemTimer.getTimeyyyyMMdd();
        if (!today.equals(currentDate)) {
            currentDate = today;
            // 获取counter的历史数据存储对象
            LastData<Number> lastMonthData = lastMonthValueMap.get(this.monitorKey);
            if (lastMonthData == null) {
                lastMonthData = new LastData<Number>();
                LastData<Number> existLastMonthData = lastMonthValueMap.putIfAbsent(this.monitorKey, lastMonthData);
                if (existLastMonthData != null) {
                    lastMonthData = existLastMonthData;
                }
            }
            lastMonthData.add(valueObject);

        }
    }
}
