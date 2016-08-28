package com.xysoft.admin.service;

import com.xysoft.entity.TextMes;
import com.xysoft.support.PageParam;

public interface TextMesService {
	
	/**
	 * 获取文本列表.
	 */
	public String getTextMess(PageParam page, String name);

	/**
	 * 保存文本信息.
	 */
	public String saveTextMes(TextMes param);
	
	/**
	 * 删除文本信息.
	 */
	public String deleteTextMes(String id);
	
}
