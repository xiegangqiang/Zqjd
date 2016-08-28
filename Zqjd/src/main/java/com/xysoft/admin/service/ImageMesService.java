package com.xysoft.admin.service;

import com.xysoft.entity.ImageMes;
import com.xysoft.support.PageParam;

public interface ImageMesService {
	
	/**
	 * 获取图文列表.
	 */
	public String getImageMess(PageParam page, String name);

	/**
	 * 保存图文信息.
	 */
	public String saveImageMes(ImageMes param);
	
	/**
	 * 删除图文信息.
	 */
	public String deleteImageMes(String id);
	
	/**
	 * 获取分类列表.
	 */
	public String getClassifys();
	
}
