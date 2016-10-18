/**
 * Project Name:jhorse_core
 * File Name:SystemConstants.java
 * Package Name:org.jhorse.common.util
 * Date:2015年6月27日上午9:16:44
 * Copyright (c) 2015, mlc0202@126.com All Rights Reserved.
 *
*/

package im.vinci.core.WebUtils;

import im.vinci.core.util.G4Constants;

/**
 * ClassName:SystemConstants <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2015年6月27日 上午9:16:44 <br/>
 * @author   mlc
 * @version  
 * @see 	 
 */
public interface SystemConstants extends G4Constants{
	/**
	 * 启用状态<br>
	 * 1:启用
	 */
    String ENABLED_Y = "1";
	
	/**
	 * 启用状态<br>
	 * 0:停用
	 */
    String ENABLED_N = "0";
	
	/**
	 * 编辑模式<br>
	 * 1:可编辑
	 */
    String EDITMODE_Y = "1";
	
	/**
	 * 编辑模式<br>
	 * 0:只读
	 */
    String EDITMODE_N = "0";
	
	/**
	 * 锁定态<br>
	 * 1:锁定
	 */
    String LOCK_Y = "1";
	
	/**
	 * 锁定状态<br>
	 * 0:解锁
	 */
    String LOCK_N = "0";
	
	/**
	 * 强制类加载<br>
	 * 1:强制
	 */
    String FORCELOAD_Y = "1";
	
	/**
	 * 强制类加载<br>
	 * 0:不强制
	 */
    String FORCELOAD_N = "0";
	
	/**
	 * 树节点类型<br>
	 * 1:叶子节点
	 */
    String LEAF_Y = "1";
	
	/**
	 * 树节点类型<br>
	 * 0:树枝节点
	 */
    String LEAF_N = "0";
	
	/**
	 * 角色类型<br>
	 * 1:业务角色
	 */
    String ROLETYPE_BUSINESS = "1";
	
	/**
	 * 角色类型<br>
	 * 2:管理角色
	 */
    String ROLETYPE_ADMIN = "2";
	
	/**
	 * 角色类型<br>
	 * 3:系统内置角色
	 */
    String ROLETYPE_EMBED = "3";
	
	/**
	 * 权限级别<br>
	 * 1:访问权限
	 */
    String AUTHORIZELEVEL_ACCESS = "1";
	
	/**
	 * 权限级别<br>
	 * 2:管理权限
	 */
    String AUTHORIZELEVEL_ADMIN = "2";
	
	/**
	 * 用户类型<br>
	 * 1:经办员
	 */
    String USERTYPE_BUSINESS = "1";
	
	/**
	 * 用户类型<br>
	 * 2:管理员
	 */
    String USERTYPE_ADMIN = "2";
	
	/**
	 * 用户类型<br>
	 * 3:系统内置用户
	 */
    String USERTYPE_EMBED = "3";
	
	/**
	 * 根节点ID<br>
	 * 01:菜单树
	 */
    String ROORID_MENU = "01";
	
	/**
	 * 帐户类型<br>
	 * 1:常规帐户
	 */
    String ACCOUNTTYPE_NORMAL = "1";
	
	/**
	 * 帐户类型<br>
	 * 2:SUPER帐户
	 */
    String ACCOUNTTYPE_SUPER = "2";
	
	/**
	 * 帐户类型<br>
	 * 3:DEVELOPER帐户
	 */
    String ACCOUNTTYPE_DEVELOPER = "3";
	
	/**
	 * 操作员事件跟踪监控开关[1:打开;0:关闭]<br>
	 * 1:打开
	 */
    String EVENTMONITOR_ENABLE_Y = "1";
	
	/**
	 * 操作员事件跟踪监控开关[1:打开;0:关闭]<br>
	 * 0:关闭
	 */
    String EVENTMONITOR_ENABLE_N = "0";
	
	/**
	 * 菜单类型<br>
	 * 1:系统菜单
	 */
    String MENUTYPE_SYSTEM = "1";
	
	/**
	 * 菜单类型<br>
	 * 0:业务菜单
	 */
    String MENUTYPE_BUSINESS = "0";
	
	/**
	 * UI元素授权类型<br>
	 * 0:未授权
	 */
    String PARTAUTHTYPE_NOGRANT = "0";
}
