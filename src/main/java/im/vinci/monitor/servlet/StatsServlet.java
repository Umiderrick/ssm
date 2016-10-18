package im.vinci.monitor.servlet;

import im.vinci.monitor.ThreadCpuUsageItem;
import im.vinci.monitor.TomcatInformations;
import im.vinci.monitor.VinMonitor;
import im.vinci.monitor.util.*;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author sunli
 */
public class StatsServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 7374298367288763494L;
    private static final CPUMonitor per = new CPUMonitor();
    private static final String action = "action";

    @Override
    public void init() throws ServletException {
        this.getServletConfig().getInitParameter("controllerPackage");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ip = RequestUtil.getIP(req);
        resp.setContentType("text/plain;charset=UTF-8");
        if (!ip.startsWith("127.0.0.") && !ip.startsWith("192.168.")) {
            resp.getWriter().write("你没有权限查看");
            resp.getWriter().flush();
        } else {
            String data = null;
            if ("system".equals(req.getParameter(action))) {
                data = systemInfo();
            } else if ("counter".equals(req.getParameter(action))) {
                if (req.getParameter("date") != null) {
                    data = RequestStats.getPathCounter(req.getParameter(action));
                } else {
                    data = RequestStats.getPathCounter(SystemTimer.getTimeyyyyMMdd());
                }

            } else if ("pathcounter".equals(req.getParameter(action))) {
                data = RequestStats.getPathStatsCounter();
            } else if ("tomcat".equals(req.getParameter(action))) {
                List<TomcatInformations> list = TomcatInformations.buildTomcatInformationsList();
                StringBuilder sb = new StringBuilder();
                for (int i = 0, len = list.size(); i < len; i++) {
                    sb.append(list.get(i).toString());
                }
                data = sb.toString();
            } else if ("cpuusage".equals(req.getParameter(action))) {
                List<ThreadCpuUsageItem> usageItems = SystemStats.dumpThreadUsages();
                Collections.sort(usageItems, new Comparator<ThreadCpuUsageItem>() {
                    public int compare(ThreadCpuUsageItem b1, ThreadCpuUsageItem b2) {
                        double[] usage1 = b1.getUsages();
                        double[] usage2 = b2.getUsages();
                        if (usage1[2] > usage2[2]) {
                            return -1;
                        } else if (usage1[2] < usage2[2]) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                StringBuilder sb = new StringBuilder();
                for (ThreadCpuUsageItem item : usageItems) {
                    double[] usages = item.getUsages();
                    if ("1".equals(req.getParameter("stacktrace"))) {
                        String stackTrace = SystemStats.getThreadStackTrace(item.getThreadId());
                        sb.append(SystemStats.formatCpuUsageOutput(item.getThreadId(), item.getThreadName(), usages));
                        if (!StringUtils.isEmpty(stackTrace)) {
                            sb.append("\n").append(stackTrace);
                        }
                        sb.append("\n");
                    } else {
                        sb.append(SystemStats.formatCpuUsageOutput(item.getThreadId(), item.getThreadName(), usages))
                                .append("\n");
                    }
                }
                data = sb.toString();
            } else if ("help".equals(req.getParameter(action))) {
                resp.setContentType("text/html;charset=UTF-8");
                data = createHelpHtml();
            } else {
                data = info(req);
            }
            resp.getWriter().write(data);
            resp.getWriter().flush();
        }
    }

    private String createHelpHtml() {
        StringBuilder html = new StringBuilder();
        html.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        html.append("<head>");
        html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
        html.append(" <title>test</title>");
        html.append("</head>");
        html.append("<body>");
        html.append("<a href=\"?action=system\">全部线程堆栈信息</a><br>\n");
        html.append("<a href=\"?action=tomcat\">tomcat信息</a><br>\n");
        html.append("<a href=\"?action=pathcounter\">每个path的访问时间分布</a><br>\n");
        html.append("<a href=\"?action=counter\">当天每个path的请求的次数</a><br>\n");
        html.append("<a href=\"?action=cpuusage\">类似top的方式查看线程的cpu利用率</a>linux下使用: watch curl -s '$url'\n<br>\n");
        html.append("<a href=\"?action=cpuusage&stacktrace=1\">查看线程（带堆栈信息）的cpu利用率</a><br>\n");
        html.append("</body></html>");
        return html.toString();
    }

    private String info(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("Your IP:" + RequestUtil.getIP(req));
        sb.append("\r\ntime:" + SystemTimer.currentTimeMillis());
        sb.append("\r\n");
        sb.append(StringUtil.monitorMapToString(VinMonitor.getAllCounterData("GlobalPerformance.*"))
                .replace("=", ":"));
        sb.append("\r\nSystemLoad:" + SystemStats.getSystemLoad());
        sb.append("\r\nAvailableProcessors:" + SystemStats.getAvailableProcessors());
        sb.append("\r\nAllthreadsCount:" + SystemStats.getAllThreadsCount());
        sb.append("\r\nCPU:" + per.getCpuUsage() * 100);
        return sb.toString();
    }

    private String systemInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(SystemStats.getDeadLock());
        sb.append(SystemStats.getJvmMemory());
        sb.append(SystemStats.getAllThreadStackTrace());
        return sb.toString();
    }
}
