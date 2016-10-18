package im.vinci.monitor.servlet;

import im.vinci.monitor.JVMMonitor;
import im.vinci.monitor.util.RequestUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sunli
 */
public class JVMMonitorServlet extends HttpServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 4867077131549251372L;

    public void init() throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {

        String ip = RequestUtil.getIP(request);
        resp.setContentType("text/plain;charset=UTF-8");
        if (!ip.startsWith("127.0.0.") && !ip.startsWith("192.168.")) {
            resp.getWriter().write("你没有权限查看");
            resp.getWriter().flush();
        } else {
            String items = request.getParameter("items");
            if (items != null) {
                String[] itemList = StringUtils.split(items, ",");
                for (int i = 0, len = itemList.length; i < len; i++) {
                    String data = JVMMonitor.getMonitorStats(itemList[i]);
                    resp.getWriter().append(data);
                    resp.getWriter().append("\r\n");
                }
                resp.flushBuffer();
            } else {
                resp.getWriter().append("need items");
                resp.flushBuffer();
            }
        }
    }

}
