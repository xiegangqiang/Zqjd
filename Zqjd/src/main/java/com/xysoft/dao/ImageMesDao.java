package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.ImageMes;
import com.xysoft.support.BaseDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

public interface ImageMesDao extends BaseDao<ImageMes> {
	
	/**
	 * 获取图文列表.
	 */
	public Pager<ImageMes> getImageMess(PageParam page, String name);
	
	/**
	 * 获取图文信息.
	 */
	public ImageMes getImageMes(String id);
	
	/**
	 * 保存图文信息.
	 */
	public void saveImageMes(ImageMes imageMes);
	
	/**
	 * 删除图文信息.
	 */
	public void deleteImageMes(ImageMes imageMes);
	
	/**
	 * 获取图文列表(分类).
	 */
	public Pager<ImageMes> getImageMess(Integer count, String classify);
	
	/**
	 * 获取图文列表(关键字).
	 */
	public Pager<ImageMes> getImageMessByMarkcode(Integer count, String markcode);
	
	/**
	 * 获取图文信息(缓存).
	 */
	public ImageMes getImageMesCache(String id);
	
	/**
	 * 获取所有的图文列表.
	 */
	public List<ImageMes> getImageMess();
	
	/**
	 * 获取图文列表.
	 */
	public Pager<ImageMes> getImageMess(String classify, Integer page, Integer count);
}
