/**
 * Project Name:jhorse_core
 * File Name:SessionUtils.java
 * Package Name:im.vinci.common.util
 * Date:2015年7月12日下午5:03:41
 * Copyright (c) 2015, mlc0202@126.com All Rights Reserved.
 *
*/

package im.vinci.core.WebUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import im.vinci.core.metatype.Dto;
import im.vinci.core.metatype.Dtos;
import im.vinci.core.metatype.impl.BaseDto;
import im.vinci.core.util.G4Utils;

/**
 * ClassName:SessionUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2015年7月12日 下午5:03:41 <br/>
 * @author   mlc
 * @version  
 * @see 	 
 */
public class SessionUtils {
	private static final Logger logger = Logger.getLogger(SessionUtils.class);
	/**
	  * 在一个session中存储名值对
	  * @param sessionKey
	  * @param sessionValue 将rowId存储到数组中
	  * @param request
	  */
	 public static void saveSession(String sessionKey,String sessionValue,HttpServletRequest request)
	 {
	  if(sessionValue!=null&&sessionValue.trim().length()>0)//去掉末尾的逗号
	  {
	   sessionValue = sessionValue.substring(0,sessionValue.lastIndexOf(","));
	   logger.info("传入的sessionValue为:");
	   logger.info(sessionValue);
	  }
	  else
	   return ;
	  
	  String[] selArray = sessionValue.split(",");//将选择的ID串拆分
	  HttpSession session = request.getSession();
	  if(session!=null)
	  {
	   //session.setAttribute(sessionKey, sessionValue);
	      Object obj = session.getAttribute(sessionKey);
	      List list = null;
	      if(obj == null)
	      {
	       logger.info("属性为空，增加rowID:");
	       logger.info(sessionValue);
	       list = new ArrayList();
	       for(int i=0;i<selArray.length;i++)
	       {
	        list.add(selArray[i]);
	       }
	       //list.add(sessionValue);
	       session.setAttribute(sessionKey,list);
	      }
	      else
	      {
	       //logger.info("属性不为空,增加rowId:");
	       list = (List)obj;
	       
	       for(int j=0;j<selArray.length;j++)
	       {
	        //list.add(selArray[i]);
	        boolean bExists = false;
	           for(int i=0;i<list.size();i++)
	           {
	            String sTmp = list.get(i).toString();
	            if(sTmp.equals(selArray[j]))
	            {
	             bExists = true;
	             break;
	            }
	           }
	           if(!bExists)
	        {
	         //logger.info("session中不存在，增加到List...");
	         list.add(selArray[j]);
	         //session.setAttribute(sessionKey, list);
	        }
	       }
	       session.setAttribute(sessionKey, list);
	       
	      }
	      if(list!=null)
	      {
	       logger.info("从session中获取的最终list:");
	       for(int i=0;i<list.size();i++)
	       {
	        logger.info(list.get(i).toString());
	       }
	       logger.info("-------------------------");
	      }
	  }
	 }
	 public static Object getSessionValue(String sessionKey,HttpServletRequest request)
	 {
	  Object obj = request.getSession().getAttribute(sessionKey);
	  return obj;
	 }
	 
	 public static void addSessionList(String sessionKey,Object sessionValue,HttpServletRequest request)
	 {
	  HttpSession session = request.getSession();
	  if(session!=null)
	  {
	   session.setAttribute(sessionKey, sessionValue);
	  }
	 }
	 
	 public void clearSession(String sessionKey ,HttpServletRequest request)
	 {
	  //清除某一个session的key值
	  HttpSession session = request.getSession();
	  session.setAttribute(sessionKey, null);
	 }
	 
	 /**
	  * 用于判断是否session超时，如果超时，访问功能时需要返回到登录页面。此方法在dwr.xml中声明供js调用
	  * @param request
	  * @return
	  */
	 //public static String isNullSession(String url,HttpServletRequest request)
	 public  static boolean isNullSession(String url,HttpServletRequest request)
	 {
	  HttpSession session = request.getSession();
         return session == null;

     }
	 
	 /**
	  * 记录excel上传进度
	  * @param sessionKey
	  * @param sessionValue
	  * @param request
	  * @return
	  */
	 public static String setAttribute(String sessionKey,String sessionValue,HttpServletRequest request)
	 {
	  String sReturn = "";
	  HttpSession session = request.getSession();
	  if(session!=null)
	  {
	   session.setAttribute(sessionKey,sessionValue);
	  }
	  
	  return sReturn;
	 }
	 
	 public static Object getAttribute(String sessionKey,HttpServletRequest request)
	 {
	  Object  obj = null;
	  HttpSession session = request.getSession();
	  if(session!=null)
	  {
	   obj = session.getAttribute(sessionKey );
	  }
	  
	  return obj;
	 }
	 
	 /**
	  * 会话被invalidate会自动调用OnlineUserBindingListener删除在线用户名单
	  * @param request
	  * @return
	  */
	 public static String invalidateSession(HttpServletRequest request)
	 {
	  HttpSession session = request.getSession();
	  //session.setAttribute("invalidateSession", "true");
	  try
	  {
	   logger.info("开始销毁会话......");
	   session.invalidate();
	   return "会话已注销!";
	  }
	  catch(Exception ex)
	  {
	   return "会话注销失败，可能session已不存在!";
	   //ex.toString();
	  }
	 }
	 
	 
	 public static List<String> getOnlineUser(HttpServletRequest request)
	 {
	  List onlineUserList =null;
	  HttpSession session = request.getSession();
	  ServletContext application = session.getServletContext();
	  // 从在线列表中删除用户名
	  onlineUserList = (List) application.getAttribute("onlineUserList");//存储的是格式:用户名[登录帐号]
	   
	  return onlineUserList;
	 }
	 
	 public static boolean isOnline(String loginId,HttpServletRequest request)
	 {
	  List list = SessionUtils.getOnlineUser(request);
	  if(list==null||list.size()==0)
	  {
	   return false;
	  }
	  //检查是否含当前用户
	  for(int i=0;i<list.size();i++)
	  {
	   logger.info("---------------");
	   logger.info(list.get(i));
	  }
	  return false;
	  //return onlineUserList;
	 }
	 
	 /**
	  * 在静态页面中获取当前的登录用户，用 用户名[loginId]表示。
	  * @param request
	  * @return
	  */
	 public static Dto getCurrentUser(HttpServletRequest request)
	 {
	  String user ="";
	  HttpSession session = request.getSession();
	  if(session == null)
	  {
	   return null;
	  }
	  
	  Object obj2 = null;
	  
	   obj2 = session.getAttribute("userinfo");
	   if(obj2==null)
	   {
	    //logger.info("获取SPRING_SECURITY_CONTEXT为空!!!");
	    return null;  //session超时
	   }
	   Dto dto = Dtos.newDto();
	   G4Utils.copyPropFromBean2Dto(obj2, dto);
	   
	   return dto;
	 }
	 
}
