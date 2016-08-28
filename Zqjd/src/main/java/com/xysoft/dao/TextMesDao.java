package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.TextMes;
import com.xysoft.support.BaseDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

public interface TextMesDao extends BaseDao<TextMes> {
	
	/**
	 * 获取文本列表.
	 */
	public Pager<TextMes> getTextMess(PageParam page, String name);
	
	/**
	 * 获取文本信息.
	 */
	public TextMes getTextMes(String id);
	
	/**
	 * 保存文本信息.
	 */
	public void saveTextMes(TextMes imageMes);
	
	/**
	 * 删除文本信息.
	 */
	public void deleteTextMes(TextMes imageMes);
	
	/**
	 * 根据关键字获取相应的文本信息.
	 */
	public List<TextMes> getTextMess(String markcode);
	
	/**
	 * 获取所有的文本信息.
	 */
	public List<TextMes> getTextMessAll();
	
}
