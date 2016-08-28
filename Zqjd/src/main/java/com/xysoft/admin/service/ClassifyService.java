package com.xysoft.admin.service;

import com.xysoft.entity.Classify;
import com.xysoft.support.PageParam;

public interface ClassifyService {
	
	/**
	 * 获取分类列表.
	 */
	public String getClassifys(PageParam page, String name);

	/**
	 * 保存分类信息.
	 */
	public String saveClassify(Classify param);
	
	/**
	 * 删除分类信息.
	 */
	public String deleteClassify(String id);
	
}
