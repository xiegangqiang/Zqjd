package com.xysoft.admin.service;

import com.xysoft.entity.Attention;

public interface AttentionService {
	
	/**
	 * 获取关注列表.
	 */
	public String getAttention();

	/**
	 * 保存关注信息.
	 */
	public String saveAttention(Attention param);
	
}
