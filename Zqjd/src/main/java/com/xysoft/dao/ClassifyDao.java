package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.Classify;
import com.xysoft.support.BaseDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

public interface ClassifyDao extends BaseDao<Classify> {
	
	/**
	 * 获取分类列表.
	 */
	public Pager<Classify> getClassifys(PageParam page, String name);
	
	/**
	 * 获取分类信息.
	 */
	public Classify getClassify(String id);
	
	/**
	 * 保存分类信息.
	 */
	public void saveClassify(Classify classify);
	
	/**
	 * 删除分类信息.
	 */
	public void deleteClassify(Classify classify);
	
	/**
	 * 根据关键字获取分类信息.
	 */
	public List<Classify> getClassifyByMarkcode(String markcode);
	
	/**
	 * 获取分类信息(缓存).
	 */
	public Classify getClassifyCache(String id);
	
	/**
	 * 获取所有分类.
	 */
	public List<Classify> getClassifys();
	
	/**
	 * 获取所有分类信息.
	 */
	public List<Classify> getClassifys(Boolean visible);
}
