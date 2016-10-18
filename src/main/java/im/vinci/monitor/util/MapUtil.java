package im.vinci.monitor.util;


import im.vinci.monitor.Monitor;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author sunli
 */
public class MapUtil {
    /**
     * 通过前缀，后缀查询SortedMap中的数据
     * <p>
     * query包含三种方式的值
     * <ul>
     * <li>* 全部包含</li>
     * <li>key* 前缀查询</li>
     * <li>*后缀查询 前缀查询</li>
     * <ul>
     * </p>
     * 
     * @param map
     * @param query
     * @return
     */
    public static SortedMap<String, Number> find(SortedMap<String, Monitor> map, String query) {
        SortedMap<String, Number> result = new TreeMap<String, Number>();
        if ("*".equals(query) || query == null) {
            for (Entry<String, Monitor> entry : map.entrySet()) {
                result.put(entry.getKey(), entry.getValue().getValue());
            }
        } else if (query.endsWith("*")) {
            String keyPrefix = query.substring(0, query.length() - 1);
            SortedMap<String, Monitor> queryMonitorMap = map.tailMap(keyPrefix);
            for (Entry<String, Monitor> entry : queryMonitorMap.entrySet()) {
                if (entry.getKey().startsWith(keyPrefix) == false) {
                    break;
                } else {
                    result.put(entry.getKey(), entry.getValue().getValue());
                }
            }
        } else if (query.startsWith("*")) {
            String keySuffix = query.substring(1, query.length());
            for (Entry<String, Monitor> entry : map.entrySet()) {
                if (entry.getKey().endsWith(keySuffix)) {
                    result.put(entry.getKey(), entry.getValue().getValue());
                }
            }
        } else {
            if (map.containsKey(query)) {
                result.put(query, map.get(query).getValue());
            }
        }
        return result;
    }

}
