package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.Role;
import com.xysoft.support.BaseDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

public interface RoleDao extends BaseDao<Role> {
	
	/**
	 * 获取所有有效的角色.
	 */
	public List<Role> getAllActiveRoles();

	public Pager<Role> getRoles(PageParam page, String name);

	public Role getRole(String id);

	public void saveRole(Role role);

	public void deleteRole(Role role);
}
