package im.vinci.monitor.filter;


import im.vinci.monitor.util.PerformanceMonitor;
import im.vinci.monitor.util.RequestStats;
import im.vinci.monitor.util.SystemTimer;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author sunli
 */
@Component
public class PerformanceFilter implements Filter {
    private static final String monitorPrefix = "GlobalPerformance";
    private PerformanceMonitor monitor = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        long start = SystemTimer.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            long send = SystemTimer.currentTimeMillis() - start;
            monitor.markSlowrRquests(send);
            RequestStats.incrementPath(this.getMMVCpath((HttpServletRequest) request), send);
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {

    }

    private String getMMVCpath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.substring(request.getContextPath().length());
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        monitor = new PerformanceMonitor(monitorPrefix);
    }

}
