package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.Menu;
import com.xysoft.support.BaseDao;

public interface MenuDao extends BaseDao<Menu> {
	
	/**
	 * 根据不同模块获取菜单列表.
	 */
	public List<Menu> getMenus(String module);
	
	/**
	 * 获取权限菜单列表.
	 */
	public List<Menu> getMenusAction();
	
}
