package com.xysoft.admin.service;

public interface MenuService {
	
	/**
	 * 根据不同模块获取菜单列表.
	 */
	public String getMenus(String module);

	public String getAllActiveMenus(String roleId);
	
}
