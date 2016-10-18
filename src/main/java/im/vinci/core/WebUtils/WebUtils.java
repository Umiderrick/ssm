package im.vinci.core.WebUtils;

import java.io.BufferedReader;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.log4j.Logger;
import im.vinci.core.metatype.Dto;
import im.vinci.core.metatype.Dtos;
import im.vinci.core.metatype.impl.BaseDto;
import im.vinci.core.util.G4Utils;

/**
 * 和Web层相关的实用工具类
 *
 * @author 熊春
 * @since 2008-09-22
 */
public class WebUtils {
	private static final Logger logger = Logger.getLogger(WebUtils.class);

	/**
	 * 获取一个SessionContainer容器,如果为null则创建之
	 *
	 * @param form
	 * @param obj
	 */
	public static SessionContainer getSessionContainer(
			HttpServletRequest request) {
		SessionContainer sessionContainer = (SessionContainer) request
				.getSession().getAttribute("SessionContainer");
		if (sessionContainer == null) {
			sessionContainer = new SessionContainer();
			HttpSession session = request.getSession(true);
			session.setAttribute("SessionContainer", sessionContainer);
		}
		return sessionContainer;
	}

	/**
	 * 获取一个SessionContainer容器,如果为null则创建之
	 *
	 * @param form
	 * @param obj
	 */
	public static SessionContainer getSessionContainer(HttpSession session) {
		SessionContainer sessionContainer = (SessionContainer) session
				.getAttribute("SessionContainer");
		if (sessionContainer == null) {
			sessionContainer = new SessionContainer();
			session.setAttribute("SessionContainer", sessionContainer);
		}
		return sessionContainer;
	}

	/**
	 * 获取一个Session属性对象
	 *
	 * @param request
	 * @param sessionName
	 * @return
	 */
	public static Object getSessionAttribute(HttpServletRequest request,
											 String sessionKey) {
		Object objSessionAttribute = null;
		HttpSession session = request.getSession(false);
		if (session != null) {
			objSessionAttribute = session.getAttribute(sessionKey);
		}
		return objSessionAttribute;
	}

	/**
	 * 设置一个Session属性对象
	 *
	 * @param request
	 * @param sessionName
	 * @return
	 */
	public static void setSessionAttribute(HttpServletRequest request,
										   String sessionKey, Object objSessionAttribute) {
		HttpSession session = request.getSession();
		if (session != null)
			session.setAttribute(sessionKey, objSessionAttribute);
	}

	/**
	 * 移除Session对象属性值
	 *
	 * @param request
	 * @param sessionName
	 * @return
	 */
	public static void removeSessionAttribute(HttpServletRequest request,
											  String sessionKey) {
		HttpSession session = request.getSession();
		if (session != null)
			session.removeAttribute(sessionKey);
	}

	/**
	 * 将请求参数封装为Dto
	 *
	 * @param request
	 * @return
	 */
	public static Dto getParamAsDto(HttpServletRequest request) {
		Dto dto = new BaseDto();
		Map map = request.getParameterMap();
		Iterator keyIterator = map.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			String value = ((String[]) (map.get(key)))[0];
			dto.put(key, value);
		}
		return dto;
	}

	/**
	 * 获取代码对照值
	 *
	 * @param field   代码类别
	 * @param code    代码值
	 * @param request
	 * @return
	 */
	public static String getCodeDesc(String pField, String pCode,
									 HttpServletRequest request) {
		List codeList = (List) request.getSession().getServletContext()
				.getAttribute("EACODELIST");
		String codedesc = null;
		for (int i = 0; i < codeList.size(); i++) {
			Dto codeDto = (BaseDto) codeList.get(i);
			if (pField.equalsIgnoreCase(codeDto.getAsString("field"))
					&& pCode.equalsIgnoreCase(codeDto.getAsString("code")))
				codedesc = codeDto.getAsString("codedesc");
		}
		return codedesc;
	}

	/**
	 * 根据代码类别获取代码表列表
	 *
	 * @param codeType
	 * @param request
	 * @return
	 */
	public static List getCodeListByField(String pField,
										  HttpServletRequest request) {
		List codeList = (List) request.getSession().getServletContext()
				.getAttribute("EACODELIST");
		List lst = new ArrayList();
		for (int i = 0; i < codeList.size(); i++) {
			Dto codeDto = (BaseDto) codeList.get(i);
			if (codeDto.getAsString("field").equalsIgnoreCase(pField)) {
				lst.add(codeDto);
			}
		}
		return lst;
	}

	/**
	 * 获取全局参数值
	 *
	 * @param pParamKey 参数键名
	 * @return
	 */
	public static String getParamValue(String pParamKey,
									   HttpServletRequest request) {
		String paramValue = "";
		ServletContext context = request.getSession().getServletContext();
		if (G4Utils.isEmpty(context)) {
			return "";
		}
		List paramList = (List) context.getAttribute("EAPARAMLIST");
		for (int i = 0; i < paramList.size(); i++) {
			Dto paramDto = (BaseDto) paramList.get(i);
			if (pParamKey.equals(paramDto.getAsString("paramkey"))) {
				paramValue = paramDto.getAsString("paramvalue");
			}
		}
		return paramValue;
	}

	/**
	 * 获取全局参数
	 *
	 * @return
	 */
	public static List getParamList(HttpServletRequest request) {
		ServletContext context = request.getSession().getServletContext();
		if (G4Utils.isEmpty(context)) {
			return new ArrayList();
		}
		return (List) context.getAttribute("EAPARAMLIST");
	}

	/**
	 * 获取指定Cookie的值
	 *
	 * @param cookies      cookie集合
	 * @param cookieName   cookie名字
	 * @param defaultValue 缺省值
	 * @return
	 */
	public static String getCookieValue(Cookie[] cookies, String cookieName,
										String defaultValue) {
		if (cookies == null) {
			return defaultValue;
		}
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookieName.equals(cookie.getName()))
				return (cookie.getValue());
		}
		return defaultValue;
	}

	/**
	 * 在一个session中存储名值对
	 *
	 * @param sessionKey
	 * @param sessionValue
	 * @param request
	 */
	public static void saveSession(String sessionKey, String sessionValue, HttpServletRequest request) {
		if (sessionValue != null && sessionValue.trim().length() > 0)//去掉末尾的逗号
		{
			sessionValue = sessionValue.substring(0, sessionValue.lastIndexOf(","));
			logger.info("传入的sessionValue为:");
			logger.info(sessionValue);
		} else
			return;

		String[] selArray = sessionValue.split(",");//将选择的ID串拆分
		HttpSession session = request.getSession();
		if (session != null) {
			//session.setAttribute(sessionKey, sessionValue);
			Object obj = session.getAttribute(sessionKey);
			List list = null;
			if (obj == null) {
				logger.info("属性为空，增加rowID:");
				logger.info(sessionValue);
				list = new ArrayList();
				for (int i = 0; i < selArray.length; i++) {
					list.add(selArray[i]);
				}
				//list.add(sessionValue);
				session.setAttribute(sessionKey, list);
			} else {
				//logger.info("属性不为空,增加rowId:");
				list = (List) obj;

				for (int j = 0; j < selArray.length; j++) {
					//list.add(selArray[i]);
					boolean bExists = false;
					for (int i = 0; i < list.size(); i++) {
						String sTmp = list.get(i).toString();
						if (sTmp.equals(selArray[j])) {
							bExists = true;
							break;
						}
					}
					if (!bExists) {
						//logger.info("session中不存在，增加到List...");
						list.add(selArray[j]);
						//session.setAttribute(sessionKey, list);
					}
				}
				session.setAttribute(sessionKey, list);

			}
			if (list != null) {
				logger.info("从session中获取的最终list:");
				for (int i = 0; i < list.size(); i++) {
					logger.info(list.get(i).toString());
				}
				logger.info("-------------------------");
			}
		}
	}

	public static Object getSessionValue(String sessionKey, HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(sessionKey);
		return obj;
	}


	public void clearSession(String sessionKey, HttpServletRequest request) {
		//清除某一个session的key值
		HttpSession session = request.getSession();
		session.setAttribute(sessionKey, null);
	}

	/**
	 * 用于判断是否session超时，如果超时，访问功能时需要返回到登录页面。此方法在dwr.xml中声明供js调用
	 *
	 * @param request
	 * @return
	 */
	//public static String isNullSession(String url,HttpServletRequest request)
	public static boolean isNullSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
        return session == null;

    }

	/**
	 * @param sessionKey
	 * @param sessionValue
	 * @param request
	 * @return
	 */
	public static void setAttribute(String sessionKey, Object sessionValue, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session != null) {
			session.setAttribute(sessionKey, sessionValue);
		}

	}

	public static Object getAttribute(String sessionKey, HttpServletRequest request) {
		Object obj = null;
		HttpSession session = request.getSession();
		if (session != null) {
			obj = session.getAttribute(sessionKey);
		}

		return obj;
	}

	/**
	 * 会话被invalidate会自动调用OnlineUserBindingListener删除在线用户名单
	 *
	 * @param request
	 * @return
	 */
	public static String invalidateSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		//session.setAttribute("invalidateSession", "true");
		try {
			logger.info("开始销毁会话......");
			session.invalidate();
			return "会话已注销!";
		} catch (Exception ex) {
			return "会话注销失败，可能session已不存在!";
			//ex.toString();
		}
	}


	public static List<String> getOnlineUser(HttpServletRequest request) {
		List onlineUserList = null;
		HttpSession session = request.getSession();
		ServletContext application = session.getServletContext();
		// 从在线列表中删除用户名
		onlineUserList = (List) application.getAttribute("onlineUserList");//存储的是格式:用户名[登录帐号]

		return onlineUserList;
	}

	public static boolean isOnline(String loginId, HttpServletRequest request) {
		List list = SessionUtils.getOnlineUser(request);
		if (list == null || list.size() == 0) {
			return false;
		}
		//检查是否含当前用户
		for (int i = 0; i < list.size(); i++) {
			logger.info("---------------");
			logger.info(list.get(i));
		}
		return false;
		//return onlineUserList;
	}

	/**
	 * @param request
	 * @return
	 */
	public static Dto getCurrentUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session == null) {
			return null;
		}

		Object obj2 = null;

		obj2 = session.getAttribute("userinfo");
		if (obj2 == null) {
			//logger.info("获取SPRING_SECURITY_CONTEXT为空!!!");
			return null;  //session超时
		}
		Dto dto = Dtos.newDto();
		G4Utils.copyPropFromBean2Dto(obj2, dto);

		return dto;
	}

	//前端发送的数据为json
	public static Dto getJsonAsDto(HttpServletRequest request) throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
		Dto dto = new BaseDto();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
			Map map = JSON.parseObject(sb.toString());
			dto = transferMapToDto(map);
		} catch (Exception e) {
			throw e;
		} finally {
			reader.close();
		}
		return dto;
	}


	public static List<Dto> getJsonArrayAsDtos(HttpServletRequest request) throws Exception{
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
		List<Dto> dtos = new ArrayList<Dto>();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
			JSONArray array = JSONArray.parseArray(sb.toString());
			for (int i=0;i<array.size();i++){
				Map map = JSON.parseObject(array.get(i).toString());
				dtos.add(transferMapToDto(map));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			reader.close();
		}
		return dtos;
	}


	private static Dto transferMapToDto(Map map) {
		Dto dto = new BaseDto();
		Set<String> set = map.keySet();
		for (String key : set) {
			dto.put(key, map.get(key));
		}
		return dto;
	}

}
