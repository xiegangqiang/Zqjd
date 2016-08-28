package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.AdminRole;
import com.xysoft.support.BaseDao;

public interface AdminRoleDao extends BaseDao<AdminRole> {
	/**
	 * 获取角色列表(管理员).
	 */
	public List<AdminRole> getAdminRoles(String admin);
	
	/**
	 * 保存角色信息.
	 */
	public void saveAdminRole(AdminRole adminRole);
	
	/**
	 * 删除角色信息.
	 */
	public void deleteAdminRole(AdminRole adminRole);
}
