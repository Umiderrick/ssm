package im.vinci.website.controller;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import im.vinci.website.config.UserProfile;
import im.vinci.website.domain.Result;
import im.vinci.website.domain.admin.AdminUser;
import im.vinci.website.exception.ErrorCode;
import im.vinci.website.exception.VinciException;
import im.vinci.website.utils.IpUtils;
import im.vinci.website.utils.UserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
    * admin
    * Created by tim@vinci on 16/1/6.
 */
    @Controller
    @RequestMapping(value = "/admin", produces = "application/json;charset=UTF-8")
    public class AdminController {

        @RequestMapping(value = {"", "/index"}, method = RequestMethod.GET)
        public String index() {
            return "index";
        }

        @RequestMapping(value = "/login", method = RequestMethod.GET)
        public String login() {
            return "welcome";
        }

        @RequestMapping(value = "/login.json", method = {RequestMethod.GET,RequestMethod.POST})
        @ResponseBody
        public Result apiLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password) {

            return null;
        }


        @Configuration
        static class AdminInterceptors extends WebMvcConfigurerAdapter implements HandlerInterceptor {

        private final static Set<String> URL_ADMIN_NOT_CHECK_LOGIN = ImmutableSet.<String>builder()
                .add("/admin/login").add("/admin/login.json").build();

        @Value("${admin.ipWhiteList}")
        @Profile({UserProfile.INTG, UserProfile.QACI, UserProfile.PROD})
        public void setIpWhiteList(String ipWhiteList) {
            ipWhiteSet = ImmutableSet.copyOf(Splitter.on(',').omitEmptyStrings().splitToList(ipWhiteList));
        }

        private ImmutableSet ipWhiteSet = null;

        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(this).addPathPatterns("/admin/**");
        }

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            if (ipWhiteSet != null && !ipWhiteSet.contains(IpUtils.getClientIpAddress(request))) {
                response.sendRedirect("/error");
                return false;
            }

//            request.getSession(true).setAttribute("admin_user",new AdminUserBean());
            Object obj = request.getSession().getAttribute("admin_user");
            String urlPath = request.getPathInfo();
            if (urlPath == null) urlPath = "";

            obj = new AdminUser(1L);
            // 验证用户是否登录
            if (!URL_ADMIN_NOT_CHECK_LOGIN.contains(urlPath)) {
                if (null == obj || !(obj instanceof AdminUser)) {
                    if (urlPath.endsWith(".json")) {
                        throw new VinciException(ErrorCode.NEED_LOGIN,"need login:"+urlPath,"需要登录");
                    }
                    response.sendRedirect("/admin/login");
                    return false;
                }
            } else {
                return true;
            }
            AdminUser adminUser = (AdminUser)obj;
            if (!adminUser.isAllow(urlPath)) {
                if (urlPath.endsWith(".json")) {
                    throw new VinciException(ErrorCode.UNAUTHORIZED,"permission deny:"+urlPath,"没有权限操作");
                }
                response.setStatus(403);
                response.getWriter().println("Access Deny");
                return false;
            }
            UserContext.setAdminUser(adminUser);
            return true;
        }

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        }


    }
}
