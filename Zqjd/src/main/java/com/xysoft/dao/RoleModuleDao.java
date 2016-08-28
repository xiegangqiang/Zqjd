package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.RoleModule;
import com.xysoft.support.BaseDao;

public interface RoleModuleDao extends BaseDao<RoleModule>{

	List<RoleModule> getRoleModuleByModule(String module);

	void saveRoleModule(RoleModule roleModule);

	void deleteRoleModule(RoleModule roleModule);

	List<RoleModule> getRoleModule();

	List<RoleModule> getRoleModuleByRole(String role);

}
