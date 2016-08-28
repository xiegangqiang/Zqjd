package com.xysoft.dao;

import com.xysoft.entity.Attention;
import com.xysoft.support.BaseDao;

public interface AttentionDao extends BaseDao<Attention> {
	
	/**
	 * 获取关注信息.
	 */
	public Attention getAttention();
	
	/**
	 * 保存关注信息.
	 */
	public void saveAttention(Attention attention);
	
	/**
	 * 删除关注信息.
	 */
	public void deleteAttention(Attention attention);

	/**
	 * 根据关键字匹配
	 */
	public Attention getAttentionByMarkcode(String eventKey);
	
}
