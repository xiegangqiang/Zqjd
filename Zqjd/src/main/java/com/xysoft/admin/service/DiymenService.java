package com.xysoft.admin.service;

import com.xysoft.entity.Diymen;


public interface DiymenService {
	
	/**
	 * 获取菜单列表.
	 */
	public String getDiymenes();
	
	/**
	 * 获取菜单列表(父节点).
	 */
	public String getDiymenesNull();
	
	/**
	 * 保存菜单信息.
	 */
	public String saveDiymen(Diymen param);
	
	/**
	 * 删除菜单信息.
	 */
	public String deleteDiymen(String id);
	
	/**
	 * 生成微信公众号自定义菜单.
	 */
	public String createMenu();
}
