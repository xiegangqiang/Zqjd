package com.xysoft.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xysoft.admin.service.RoleService;
import com.xysoft.dao.AdminRoleDao;
import com.xysoft.dao.RoleDao;
import com.xysoft.dao.RoleMenuDao;
import com.xysoft.dao.RoleModuleDao;
import com.xysoft.entity.AdminRole;
import com.xysoft.entity.Role;
import com.xysoft.entity.RoleMenu;
import com.xysoft.entity.RoleModule;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;

@Component
@SuppressWarnings("unchecked")
public class RoleServiceImpl implements RoleService {
	@Resource
	private RoleDao roleDao;
	@Resource
	private AdminRoleDao adminRoleDao;
	@Resource
	private RoleMenuDao roleMenuDao;
	@Resource
	private RoleModuleDao roleModuleDao;

	@Transactional(readOnly = true)
	public String getAllActiveRoles(String adminId) {
		List<Role> roles = this.roleDao.getAllActiveRoles();
		Map<String, Role> maps = new HashMap<String, Role>();
		for (Role role : roles) {
			maps.put(role.getId(), role);
		}
		List<AdminRole> adminRoles = adminRoleDao.getAdminRoles(adminId);
		for (AdminRole adminRole : adminRoles) {
			if (maps.containsKey(adminRole.getRole())) {
				maps.get(adminRole.getRole()).setChecked(true);
			}
		}
		return JsonUtil.toString(maps.values());
	}

	@Transactional(readOnly = true)
	public String getRoles(PageParam page, String name) {
		Pager<Role> pager = this.roleDao.getRoles(page, name);
		return JsonUtil.toStringFromObject(pager);
	}

	@Transactional
	public String saveRole(Role param) {
		Role role = null;
		if(NullUtils.isEmpty(param.getId())){
				 role = new Role();
		}else{
				role = this.roleDao.getRole(param.getId());
		}
		BeanUtils.copyProperties(param, role, new String[] {"id", "createDate"});
		this.roleDao.saveRole(role);
		return JsonUtil.toRes("保存成功");
	}

	@Transactional
	public String deleteRole(String id) {
		Role role = this.roleDao.getRole(id);
		if(role != null) {
			//清空角色菜单中间表
			List<RoleMenu> roleMenus = this.roleMenuDao.getRoleMenus(role.getId());
			for (RoleMenu roleMenu : roleMenus) {
				this.roleMenuDao.deleteRoleMenu(roleMenu);
			}
			//清空角色模块中间表
			List<RoleModule> roleModules = this.roleModuleDao.getRoleModuleByRole(role.getId());
			for (RoleModule roleModule : roleModules) {
				this.roleModuleDao.deleteRoleModule(roleModule);
			}
			this.roleDao.deleteRole(role);
		}
		return JsonUtil.toRes("删除成功");
	}
	
	
	@Transactional
	public String configMenus(String id, String[] menus) {
		//处理权限问题
		List<RoleMenu> roleMenus = this.roleMenuDao.getRoleMenus(id);
		Map<String, RoleMenu> maps = new HashMap<String, RoleMenu>();
		for(RoleMenu roleMenu : roleMenus){
			maps.put(roleMenu.getMenu(), roleMenu);
		}
		if(menus != null){
			for(String menu : menus){
				if(maps.containsKey(menu)){
					maps.remove(menu);
				}else{
					RoleMenu rm = new RoleMenu();
					rm.setMenu(menu);
					rm.setRole(id);
					this.roleMenuDao.saveRoleMenu(rm);
				}
			}
		}
		for(RoleMenu roleMenu : maps.values()){
			this.roleMenuDao.deleteRoleMenu(roleMenu);
		}
		return JsonUtil.toRes("设置成功");
	}
	

}













