package com.xysoft.admin.service;

import com.xysoft.entity.Role;
import com.xysoft.support.PageParam;

public interface RoleService {
	
	/**
	 * 获取角色列表.
	 */
	public String getAllActiveRoles(String adminId);

	public String getRoles(PageParam page, String name);

	public String saveRole(Role param);

	public String deleteRole(String id);

	public String configMenus(String id, String[] menus);
	
}
