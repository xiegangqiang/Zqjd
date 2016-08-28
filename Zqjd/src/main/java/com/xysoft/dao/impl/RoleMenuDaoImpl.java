package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.RoleMenuDao;
import com.xysoft.entity.RoleMenu;
import com.xysoft.support.BaseDaoImpl;

@Component
public class RoleMenuDaoImpl extends BaseDaoImpl<RoleMenu> implements RoleMenuDao {

	public List<RoleMenu> getRoleMenus(String role) {
		return this.find("from RoleMenu where role = ?", role);
	}

	public void saveRoleMenu(RoleMenu roleMenu) {
		this.saveOrUpdate(roleMenu);
	}

	public void deleteRoleMenu(RoleMenu roleMenu) {
		this.delete(roleMenu);
	}

}
