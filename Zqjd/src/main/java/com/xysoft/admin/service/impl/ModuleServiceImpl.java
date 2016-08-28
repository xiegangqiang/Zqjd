package com.xysoft.admin.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.admin.service.ModuleService;
import com.xysoft.comparator.ModuleComparator;
import com.xysoft.dao.AdminDao;
import com.xysoft.dao.AdminRoleDao;
import com.xysoft.dao.ModuleDao;
import com.xysoft.dao.RoleDao;
import com.xysoft.dao.RoleModuleDao;
import com.xysoft.entity.Admin;
import com.xysoft.entity.AdminRole;
import com.xysoft.entity.Module;
import com.xysoft.entity.Role;
import com.xysoft.entity.RoleModule;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;
import com.xysoft.util.RequestUtil;

@Component
@SuppressWarnings("unchecked")
public class ModuleServiceImpl implements ModuleService{

	@Resource
	private ModuleDao moduleDao;
	@Resource
	private RoleModuleDao roleModuleDao;
	@Resource
	private RoleDao roleDao;
	@Resource
	private AdminDao adminDao;
	@Resource
	private AdminRoleDao adminRoleDao;

	@Transactional(readOnly = true)
	public String getModules(PageParam page, String name) {
		Pager<Module> pager = this.moduleDao.getModules(page, name);
		return JsonUtil.toStringFromObject(pager);
	}
	
	@Transactional
	public String saveModule(Module param, String[] roles) {
		Module module = null;
		if(NullUtils.isEmpty(param.getId())) {
			module = new Module();
		}else {
			module = this.moduleDao.getModule(param.getId());
		}
		BeanUtils.copyProperties(param, module, new String [] {"id", "createDate", "img"});
		this.moduleDao.saveModule(module);
		//处理模块问题
		List<RoleModule> roleModules = this.roleModuleDao.getRoleModuleByModule(param.getId());
		Map<String, RoleModule> maps = new HashMap<String, RoleModule>();
		for (RoleModule roleModule : roleModules) {
			maps.put(roleModule.getRole(), roleModule);
		}
		if(roleModules != null) {
			for (String role : roles) {
				if(maps.containsKey(role)) {
					maps.remove(role);
				}else {
					RoleModule rm = new RoleModule();
					rm.setRole(role);
					rm.setModule(module.getId());
					this.roleModuleDao.saveRoleModule(rm);
				}
			}
		}
		for (RoleModule roleModule : maps.values()) {
			this.roleModuleDao.deleteRoleModule(roleModule);
		}
		return JsonUtil.toRes("保存成功");
	}
	
	@Transactional
	public String deleteModule(String id) {
		Module module = this.moduleDao.getModule(id);
		if(module != null) {
			List<RoleModule> roleModules = this.roleModuleDao.getRoleModuleByModule(module.getId());
			for (RoleModule roleModule : roleModules) {
				this.roleModuleDao.delete(roleModule);
			}
			this.moduleDao.deleteModule(module);
		}
		return JsonUtil.toRes("删除成功");
	}
	
	@Transactional(readOnly = true)
	public String getAllActiveRoles(String moduleId) {
		//查找所有角色给前台选择
		List<Role> Roles = this.roleDao.getAllActiveRoles();
		Map<String, Role> maps = new HashMap<String, Role>();
		for (Role role : Roles) {
			maps.put(role.getId(), role);
		}
		List<RoleModule> roleModules = this.roleModuleDao.getRoleModuleByModule(moduleId);
		for (RoleModule roleModule : roleModules) {
			if(maps.containsKey(roleModule.getRole())) {
				maps.get(roleModule.getRole()).setChecked(true);
			}
		}
		return JsonUtil.toString(maps.values());
	}
	
	@Transactional(readOnly = true)
	public String getAllActiveModule() {
		Admin admin = this.adminDao.getAdmin(RequestUtil.getUsername());
		List<Module> modules = this.moduleDao.getModules();//所有模块
		Map<String, Module> map = new HashMap<String, Module>();
		for (Module module : modules) {
			map.put(module.getId(), module);
		}
		if(admin.getUserType() ==9 || admin.getUserType() == 99) {
			return JsonUtil.toString(modules);
		}else {
			List<AdminRole> adminRoles = this.adminRoleDao.getAdminRoles(admin.getId());//登陆者所有角色
			List<RoleModule> roleModules = this.roleModuleDao.getRoleModule();
			
			List<Module> md = new ArrayList<Module>();
			for (AdminRole adminRole : adminRoles) {
				for (RoleModule roleModule : roleModules) {
					if( adminRole.getRole().equals(roleModule.getRole()) ){
						md.add( map.get(roleModule.getModule()) );
					}
				}
			}
			Collections.sort(md, new ModuleComparator());
			return JsonUtil.toString(md);
		}
	}
	
}










