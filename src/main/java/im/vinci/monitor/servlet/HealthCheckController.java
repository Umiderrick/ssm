package im.vinci.monitor.servlet;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tim@vinci on 15/11/27.
 * 健康检查
 *
 */
@RestController
public class HealthCheckController {

    @RequestMapping("/healthcheck.html")
    public void healthCheck(HttpServletRequest request , HttpServletResponse response) throws IOException {
        response.getOutputStream().println("check OK!");
    }
}
