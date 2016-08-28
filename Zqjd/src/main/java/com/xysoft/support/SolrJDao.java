package com.xysoft.support;

import java.util.List;

/**
 * 使用solrJ 向solr 提交请求，增删改查，
 * solrJ 底层页是发送http 协议...
 * @author xgq
 *
 */
public interface SolrJDao {

	public void save(String url, Object param);
	
	public void delete(String url, String param);
	
	public <T> List<T> find(String url, String field, String value, Class<T> clazz);
	
	public <T> Pager<T> getForPager(String url, String field, String value, Integer pageIndex, Integer rows, Class<T> clazz);
	
	public <T> Pager<T> getForPager(String url, String field, Integer pageIndex, Integer rows, Class<T> clazz, Object... scope);
	
	public <T> Pager<T> getForPager(String url, String field, String value, String sortField, Boolean sort, Integer pageIndex, Integer rows, Class<T> clazz, String scopeField, Object... scope);
}
