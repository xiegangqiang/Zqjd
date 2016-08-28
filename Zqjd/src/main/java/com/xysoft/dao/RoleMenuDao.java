package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.RoleMenu;
import com.xysoft.support.BaseDao;

public interface RoleMenuDao extends BaseDao<RoleMenu> {
	
	/**
	 * 获取权限列表(角色).
	 */
	public List<RoleMenu> getRoleMenus(String role);

	public void saveRoleMenu(RoleMenu roleMenu);

	public void deleteRoleMenu(RoleMenu roleMenu);
}
