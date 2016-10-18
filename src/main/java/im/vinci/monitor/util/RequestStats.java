package im.vinci.monitor.util;

import com.google.common.collect.Maps;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 按path的请求分布状态
 * 
 * @author sunli
 */
public class RequestStats {
    /**
     * 存储按path为key的日访问情况
     */
    protected static final ConcurrentMap<String, ConcurrentMap<String, AtomicInteger>> counterMap = Maps.newConcurrentMap();

    private static final ConcurrentMap<String, PathStatsCounter> pathStatsCounterMap = Maps.newConcurrentMap();

    private static final Lock lock = new ReentrantLock();
    private static final Lock lockMap = new ReentrantLock();
    private static final Lock pathStatsCounterMapLock = new ReentrantLock();
    private static final int pathLengthMax = 50;

    public RequestStats() {

    }

    /**
     * 获取当天的统计map
     * 
     * @return
     */
    private static ConcurrentMap<String, AtomicInteger> getTodayMap() {
        String time = SystemTimer.getTimeyyyyMMdd();
        ConcurrentMap<String, AtomicInteger> todayMap = counterMap.get(time);
        if (todayMap == null) {
            lockMap.lock();
            try {
                todayMap = counterMap.get(time);
                if (todayMap == null) {
                    todayMap = Maps.newConcurrentMap();
                    counterMap.putIfAbsent(time, todayMap);
                }
            } finally {
                lockMap.unlock();
            }
        }
        return todayMap;
    }

    private static PathStatsCounter getPathStatsCounter(String path) {
        PathStatsCounter pathStatsCounter = pathStatsCounterMap.get(path);
        if (pathStatsCounter == null) {
            pathStatsCounterMapLock.lock();
            try {
                pathStatsCounter = pathStatsCounterMap.get(path);
                if (pathStatsCounter == null) {
                    pathStatsCounter = new PathStatsCounter();
                    pathStatsCounterMap.putIfAbsent(path, pathStatsCounter);
                }
            } finally {
                pathStatsCounterMapLock.unlock();
            }
        }
        return pathStatsCounter;
    }

    private static void incrementPathStatsCounter(String path, long spendtime) {
        PathStatsCounter pathStatsCounter = getPathStatsCounter(path);
        pathStatsCounter.markSlowrRquests(spendtime);
    }

    public static void incrementPath(String path, long spendtime) {
        // 记录每个路径的执行时间的分区
        incrementPathStatsCounter(path, spendtime);
        // 记录每个路径每天的访问次数
        ConcurrentMap<String, AtomicInteger> todayMap = getTodayMap();
        String filterPath = pathFilter(path);
        AtomicInteger counter = todayMap.get(filterPath);
        if (counter == null) {
            lock.lock();
            try {
                counter = todayMap.get(filterPath);
                if (counter == null) {
                    counter = new AtomicInteger(0);
                    todayMap.put(filterPath, counter);
                }
            } finally {
                lock.unlock();
            }
        }
        counter.incrementAndGet();
    }

    private static String pathFilter(String path) {
        if (path.length() > pathLengthMax) {
            return path.substring(0, pathLengthMax);
        }
        return path;
    }

    public static String getPathCounter(String time) {
        ConcurrentMap<String, AtomicInteger> dayMap = counterMap.get(time);
        StringBuilder sb = new StringBuilder();

        if (dayMap != null) {
            for (Entry<String, AtomicInteger> entry : dayMap.entrySet()) {
                sb.append(entry.getKey());
                sb.append(" ");
                sb.append(entry.getValue());
                sb.append("\r\n");
            }
        }
        return sb.toString();
    }

    public static String getPathStatsCounter() {
        StringBuilder sb = new StringBuilder();
        for (Entry<String, PathStatsCounter> entry : pathStatsCounterMap.entrySet()) {
            sb.append(entry.getKey());
            sb.append(" ");
            sb.append(entry.getValue().toString());
            sb.append("\r\n");
        }

        return sb.toString();
    }

    public static class PathStatsCounter {
        /**
         * 请求时间小于5ms
         */
        private final AtomicLong SLOWREQEUSTLESS5 = new AtomicLong();
        /**
         * 请求时间小于10ms
         */
        private final AtomicLong SLOWREQEUSTLESS10 = new AtomicLong();
        /**
         * 请求时间小于20毫秒
         */
        private final AtomicLong SLOWREQEUSTLESS20 = new AtomicLong();
        /**
         * 请求时间小于50毫秒
         */
        private final AtomicLong SLOWREQEUSTLESS50 = new AtomicLong();
        /**
         * 请求时间小于100毫秒
         */
        private final AtomicLong SLOWREQEUSTLESS100 = new AtomicLong();
        /**
         * 请求时间小于1秒
         */
        private final AtomicLong SLOWREQEUSTLESS1000 = new AtomicLong();
        /**
         * 请求时间大于1秒
         */
        private final AtomicLong SLOWREQEUSTMORE1000 = new AtomicLong();

        public PathStatsCounter() {

        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\r\n\tSLOWREQEUSTLESS5:").append(SLOWREQEUSTLESS5.get());
            sb.append("\r\n\tSLOWREQEUSTLESS10:").append(SLOWREQEUSTLESS10.get());
            sb.append("\r\n\tSLOWREQEUSTLESS20:").append(SLOWREQEUSTLESS20.get());
            sb.append("\r\n\tSLOWREQEUSTLESS50:").append(SLOWREQEUSTLESS50.get());
            sb.append("\r\n\tSLOWREQEUSTLESS100:").append(SLOWREQEUSTLESS100.get());
            sb.append("\r\n\tSLOWREQEUSTLESS1000:").append(SLOWREQEUSTLESS1000.get());
            sb.append("\r\n\tSLOWREQEUSTMORE1000:").append(SLOWREQEUSTMORE1000.get());
            return sb.toString();
        }

        /**
         * 记录执行时间
         * 
         * @param spendtime 请求的执行时间
         */
        public void markSlowrRquests(long spendtime) {
            if (spendtime < 5) {
                SLOWREQEUSTLESS5.incrementAndGet();
                return;
            }
            if (spendtime < 10) {
                SLOWREQEUSTLESS10.incrementAndGet();
                return;
            }
            if (spendtime < 20) {
                SLOWREQEUSTLESS20.incrementAndGet();
                return;
            }
            if (spendtime < 50) {
                SLOWREQEUSTLESS50.incrementAndGet();
                return;
            }
            if (spendtime < 100) {
                SLOWREQEUSTLESS100.incrementAndGet();
                return;
            }
            if (spendtime < 1000) {
                SLOWREQEUSTLESS1000.incrementAndGet();
                return;
            }
            SLOWREQEUSTMORE1000.incrementAndGet();
        }
    }
}
