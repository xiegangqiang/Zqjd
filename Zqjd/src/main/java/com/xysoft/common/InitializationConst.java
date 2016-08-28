package com.xysoft.common;

import java.util.ArrayList;
import java.util.List;

public class InitializationConst {
	
	/**
	 * 开发管理员代码.
	 */
	public static final Integer DevelopRole = 99;
	
	/**
	 * 超级管理员代码.
	 */
	public static final Integer SuperRole = 9;
	
	/**
	 * 管理员默认权限.
	 */
	public static final List<String> Init_Roles = new ArrayList<String>();
	
	/**
	 * 用户默认权限.
	 */
	public static final List<String> Init_User_Roles = new ArrayList<String>();
	
	/**
	 * 登录失败N次，账号自动锁定.
	 */
	public static Integer FailureLockCount = 0;
	
	static {
		Init_Roles.add("ROLE_ADMIN_LOGIN");
		Init_User_Roles.add("ROLE_USER_LOGIN");
		FailureLockCount = 3;
	}
}
