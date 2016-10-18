package im.vinci.website.utils;

import im.vinci.monitor.util.SystemTimer;
import im.vinci.website.utils.itsdangerouser.Signer;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;

/**
 * 验证登录用的工具类
 * Created by tim@vinci on 16/1/7.
 */
public class AuthUtils {
    public static final String SECRET_KEY = "a good key baldfjflasjdlkfjaoisdujvjlkaj";
    private static final Logger logger = LoggerFactory.getLogger(AuthUtils.class);


    /**
     * 加密value,把时间也写进去
     */
    public static String sign(String value) {
        Signer signer = new Signer(SECRET_KEY);
        return signer.sign(value+"#"+ SystemTimer.getTime());
    }

    /**
     * 验证token是否是ok的
     * @param token 通过sign做的token
     * @param maxAge 超时时间,-1为不超时
     */
    public static String unsign(String token, long maxAge) {
        try {
            Signer signer = new Signer(SECRET_KEY);
            int idx = token.indexOf('.');
            if (idx <= 0) {
                return null;
            }
            String value = token.substring(0,idx);
            if (!signer.verifySignature(value,token.substring(idx+1))) {
                return null;
            }
            idx = value.indexOf('#');
            String v = value.substring(0,idx);
            long ts = Long.parseLong(value.substring(idx+1));
            if (maxAge > 0 && SystemTimer.getTime() - ts > maxAge) {
                return null;
            }
            return v;
        } catch (Exception e) {
            logger.warn("检查登录失败，请检查：", e);
        }
        return null;
    }

    public static String findCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName())) {
                return StringUtils.trimToNull(cookie.getValue());
            }
        }
        return null;
    }


    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        return Base64.encodeBase64String(bytes);
    }

//    public static void main(String[] args) {
//        System.err.println(sign("18601998564"));
//        System.err.println(unsign("18601998564#1452154287341.77-9du-_ve-_ve-_vcWY77-977-9dA1mAXDvv71r77-9ND8t",11));
//    }
}
