package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.Diymen;
import com.xysoft.support.BaseDao;

public interface DiymenDao extends BaseDao<Diymen> {
	
	/**
	 * 获取所有菜单.
	 */
	public List<Diymen> getDiymenes();
	
	/**
	 * 获取菜单信息.
	 */
	public Diymen getDiymen(String id);
	
	/**
	 * 保存菜单信息.
	 */
	public void saveDiymen(Diymen diymen);
	
	/**
	 * 删除菜单信息.
	 */
	public void deleteDiymen(Diymen diymen);
	
	/**
	 * 获取父节点菜单信息.
	 */
	public List<Diymen> getDiymenNull();
	
	/**
	 * 获取子节点菜单列表.
	 */
	public List<Diymen> getDiymenChildren(String parentId);
	
}
