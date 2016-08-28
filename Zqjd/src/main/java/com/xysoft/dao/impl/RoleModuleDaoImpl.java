package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.RoleModuleDao;
import com.xysoft.entity.RoleModule;
import com.xysoft.support.BaseDaoImpl;

@Component
public class RoleModuleDaoImpl extends BaseDaoImpl<RoleModule> implements RoleModuleDao{

	public List<RoleModule> getRoleModuleByModule(String module) {
		return this.find("from RoleModule where module = ?", module);
	}

	public void saveRoleModule(RoleModule roleModule) {
		this.saveOrUpdate(roleModule);
	}

	public void deleteRoleModule(RoleModule roleModule) {
		this.delete(roleModule);
	}

	public List<RoleModule> getRoleModule() {
		return this.find("from RoleModule");
	}
	
	public List<RoleModule> getRoleModuleByRole(String role) {
		return this.find("from RoleModule where role = ?", role);
	}
}
