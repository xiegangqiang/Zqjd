package com.xysoft.dao;

import com.xysoft.view.ImageMesClassifyV;
import com.xysoft.support.BaseDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

public interface ImageMesClassifyVDao extends BaseDao<ImageMesClassifyV> {
	
	/**
	 * 获取图文列表.
	 */
	public Pager<ImageMesClassifyV> getImageMesClassifyVs(PageParam page, String name);
	
}
