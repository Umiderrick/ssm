package im.vinci.monitor.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 缓存系统时间，避免每次调用System.currentTimeMillis()消耗系统资源。适用于高并发、对时间精度要求不高的场景
 * 
 * @author li.sun
 * @date 2011-06-19
 * @version $Id: SystemTimer.java 9565 2012-12-05 08:03:43Z build $
 */
public class SystemTimer {
    private final static ScheduledExecutorService executor =  Executors.newScheduledThreadPool(1,
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "thread-system-timer-task");
                    t.setDaemon(true);
                    return t;
                }

            });
    /**
     * 更新间隔
     */
    private static final long interval = Long.parseLong(System.getProperty("notify.systimer.interval", "1000"));

    /**
     * 时间格式
     */
    private static final FastDateFormat formatyyyyMMddHHmmss = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss",
            TimeZone.getDefault(), Locale.getDefault());
    /**
     * 时间格式
     */
    private static final FastDateFormat formatyyyyMMdd = FastDateFormat.getInstance("yyyy-MM-dd",
            TimeZone.getDefault(), Locale.getDefault());
    /**
     * 时间
     */
    private static volatile long time = System.currentTimeMillis();
    private static volatile String timeyyyyMMddHHmmss = formatyyyyMMddHHmmss.format(new Date());
    private static volatile String timeyyyyMMdd = formatyyyyMMdd.format(new Date());

    public SystemTimer() {
    }

    private static class TimerFormatTicker implements Runnable {
        @Override
        public void run() {
            time = System.currentTimeMillis();
            timeyyyyMMddHHmmss = new Timestamp(time).toString().substring(0, 19);
            timeyyyyMMdd = timeyyyyMMddHHmmss.substring(0, 10);// 通过对timeyyyyMMddHHmmss进行substring进行优化，减少运算
        }
    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取
     * 
     * @return
     */
    public static String getTimeyyyyMMddHHmmss() {
        return timeyyyyMMddHHmmss;
    }

    public static String getTimeyyyyMMdd() {
        return timeyyyyMMdd;
    }

    public static long getTime() {
        return time;
    }
    static {
        executor.scheduleAtFixedRate(new TimerFormatTicker(), interval, interval, TimeUnit.MILLISECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                executor.shutdown();
            }
        });
    }
}
