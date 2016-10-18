package im.vinci.monitor.servlet;

import im.vinci.monitor.VinMonitor;
import im.vinci.monitor.util.RequestUtil;
import im.vinci.monitor.util.StringUtil;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sunli
 */
public class MeerkatMonitorServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -4945176045719610172L;

    @Override
    public void init() throws ServletException {

    }

    /**
     * 获取监控数据
     * <p>
     * 获取所有应用监控对象数据和最近1分钟递增值 ?
     * 
     * <pre>
     * ?query=*&time=1&offset=1
     * </pre>
     * 
     * 获取所有应用监控对象数据和最近30分钟任意分钟时间区间段的递增值?
     * 
     * <pre>
     * ?query=*&time=间隔的分钟数&offset=往前数的第多少分钟开始计算（1表示最近的一个记录点）
     * </pre>
     * 
     * 取所有应用监控对象数据和最近30分钟任意分钟时间区间段的递增值和最近30天任意天数区间段值?
     * 
     * <pre>
     * ?query=*&time=间隔的分钟数&offset=往前数的第多少分钟开始计算（1表示最近的一个记录点）&day=间隔的天数&dayoffset=往前数的第多少天开始计算（1表示最近的一个记录点）
     * </pre>
     * 
     * </p>
     * <p>
     * 
     * <pre>
     * ?query=*&time=1&offset=1
     * </pre>
     * 
     * </p>
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        String ip = RequestUtil.getIP(request);
        resp.setContentType("text/plain;charset=UTF-8");
        if (!ip.startsWith("127.0.0.") && !ip.startsWith("192.168.")) {
            resp.getWriter().write("你没有权限查看");
            resp.getWriter().flush();
        } else {
            String query = request.getParameter("query");
            int time = NumberUtils.toInt(request.getParameter("time"), 1);
            int offset = NumberUtils.toInt(request.getParameter("offset"), 1);
            int day = NumberUtils.toInt(request.getParameter("day"), 1);
            int dayoffset = NumberUtils.toInt(request.getParameter("dayoffset"), 1);
            boolean description = request.getParameter("description") != null;
            if (query == null || "".equals(query.trim())) {
                query = "*";
            }
            if (time < 1 || time > 30) {
                time = 1;
            }
            String data = getAllMonitorData(query, offset, time, dayoffset, day, description);
            resp.getWriter().write(data);
            resp.getWriter().flush();
        }
    }

    /**
     * @param query
     * @param minutesOffset
     * @param periodTimeMinutes
     * @param dayoffset
     * @param day
     * @return
     */
    public String getAllMonitorData(String query, int minutesOffset, int periodTimeMinutes, int dayoffset, int day,
            boolean description) {
        if (description) {
            return StringUtil.monitorMapToStringWithDescription(VinMonitor.getMonitorData(query, minutesOffset,
                    periodTimeMinutes, day, dayoffset));
        } else {
            return StringUtil.monitorMapToString(VinMonitor.getMonitorData(query, minutesOffset, periodTimeMinutes,
                    day, dayoffset));
        }
    }

}
