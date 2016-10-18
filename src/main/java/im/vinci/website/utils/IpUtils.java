package im.vinci.website.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * 网络相关的一些工具方法
 * Created by tim@vinci on 16/1/6.
 */
public class IpUtils {
    private static final String[] HEADERS_TO_TRY = {
            "x-real-ip",
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR" };

    /**
     * 通过HttpServletRequest返回IP地址
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String rip = request.getRemoteAddr();
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                rip = ip;
                break;
            }
        }
        int pos = rip.indexOf(',');
        if (pos >= 0) {
            rip = rip.substring(0, pos);
        }
        return rip;
    }
}
