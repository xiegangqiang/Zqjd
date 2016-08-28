package com.xysoft.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.admin.service.MenuService;
import com.xysoft.dao.MenuDao;
import com.xysoft.dao.RoleMenuDao;
import com.xysoft.dao.RoleModuleDao;
import com.xysoft.entity.Menu;
import com.xysoft.entity.RoleMenu;
import com.xysoft.entity.RoleModule;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;
import com.xysoft.util.RequestUtil;

@Component
public class MenuServiceImpl implements MenuService {
	@Resource
	private MenuDao menuDao;
	@Resource
	private RoleMenuDao roleMenuDao;
	@Resource
	private RoleModuleDao roleModuleDao;
	
	@Transactional(readOnly = true)
	public String getMenus(String module) {
		List<String> actions = RequestUtil.getActionsByLoginUser();
		List<Menu> menus = this.menuDao.getMenus(module);
		Map<String, List<Menu>> maps = new HashMap<String, List<Menu>>();
		for (Menu menu : menus) {
			if (NullUtils.isEmpty(menu.getParentId()) || actions.indexOf(menu.getAction()) == -1) continue;
			List<Menu> hms = null;
			if (maps.containsKey(menu.getParentId())) {
				hms = maps.get(menu.getParentId());
			} else {
				hms = new ArrayList<Menu>();
			}
			hms.add(menu);
			maps.put(menu.getParentId(), hms);
		}
		List<Menu> hms = new ArrayList<Menu>(); 
		for (Menu menu : menus) {
			if (NullUtils.isEmpty(menu.getParentId())) {
				Menu hm = rebuildMenu(hms, maps, menu);
				if (hm.getChildren().size() == 0) continue;
				hms.add(hm);
			}
		}
		return JsonUtil.toString(hms);
	}
	
	/**
	 * 递归查找子分类节点.
	 */
	private Menu rebuildMenu(List<Menu> hms, Map<String, List<Menu>> maps, Menu menu) {
		if (!maps.containsKey(menu.getId())) return menu;
		List<Menu> lst = maps.get(menu.getId()); 
		for (Menu ls : lst) {
			menu.setExpanded(true);
			menu.getChildren().add(rebuildMenu(hms, maps, ls));
		}
		return menu;
	}

	@Transactional(readOnly = true)
	public String getAllActiveMenus(String roleId) {
/*		//查找所有菜单给前台选择
		List<Menu> menus = this.menuDao.getMenusAction();
		Map<String, Menu> maps = new HashMap<String, Menu>();
		for(Menu menu : menus){
			maps.put(menu.getId(), menu);
		}
		List<RoleMenu> roleMenus = this.roleMenuDao.getRoleMenus(roleId);
		for(RoleMenu roleMenu : roleMenus){
			if(maps.containsKey(roleMenu.getMenu())){
				maps.get(roleMenu.getMenu()).setChecked(true);
			}
		}
		return JsonUtil.toString(maps.values());*/
		String module = null;
		List<RoleMenu> roleMenus = this.roleMenuDao.getRoleMenus(roleId);
		List<RoleModule> roleModules = this.roleModuleDao.getRoleModuleByRole(roleId);
		if(roleModules.size() == 0) module = "";
		for (RoleModule roleModule : roleModules) {
			if(module == null) module = "";
			module += roleModule.getModule() + ",";
		}
		List<Menu> menus = this.menuDao.getMenus(module);
		Map<String, List<Menu>> maps = new HashMap<String, List<Menu>>();
		for (Menu menu : menus) {
			List<Menu> hms = null;
			if (maps.containsKey(menu.getParentId())) {
				hms = maps.get(menu.getParentId());
			} else {
				hms = new ArrayList<Menu>();
			}
			for (RoleMenu roleMenu : roleMenus) {
				if(roleMenu.getMenu().equals(menu.getId())) {
					menu.setChecked(true);
				}
			}
			hms.add(menu);
			maps.put(menu.getParentId(), hms);
		}
		List<Menu> hms = new ArrayList<Menu>(); 
		for (Menu menu : menus) {
			if (NullUtils.isEmpty(menu.getParentId())) {
				Menu hm = rebuildMenu(hms, maps, menu);
				if (hm.getChildren().size() == 0) continue;
				hms.add(hm);
			}
		}
		return JsonUtil.toString(hms);
	}
	

}






