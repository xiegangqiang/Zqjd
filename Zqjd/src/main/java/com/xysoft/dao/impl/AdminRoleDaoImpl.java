package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.AdminRoleDao;
import com.xysoft.entity.AdminRole;
import com.xysoft.support.BaseDaoImpl;

@Component
public class AdminRoleDaoImpl extends BaseDaoImpl<AdminRole> implements AdminRoleDao {

	public List<AdminRole> getAdminRoles(String admin) {
		return this.find("from AdminRole where admin = ?", admin);
	}

	public void saveAdminRole(AdminRole adminRole) {
		this.saveOrUpdate(adminRole);
	}

	public void deleteAdminRole(AdminRole adminRole) {
		this.delete(adminRole);
	}

}
