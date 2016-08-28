package com.xysoft.support;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public interface JdbcDao<T> {
	
	int execute(String sql);
	
	int execute(String sql, Object... param);
	
	int executeMoreResult(final String sql, final int index, final Object... param);
	
	List<T> executeBatch(final List<String> sqls, final RowMapper<T> rowmapper);
	
	String save(final String sql, final Object... param);
	
	List<T> call(String sql, RowMapper<T> rowmapper, Object... param);
	
	List<T> call(final String sql, final int page, final int rows, final RowMapper<T> rowmapper, final Object... param);
	
	Pager<T>  callForPager(String sql, final PageParam page, final RowMapper<T> rowmapper, Object... param);
	
	List<T> callMoreResult(final String sql, final int index, final RowMapper<T> rowmapper, final Object... param);
	
	List<Object> callMoreResult(final String sql, final List<Object> param, final Object... objects);
	
	List<T> callMoreResult(final String sql, final int index, final int page, final int rows, final RowMapper<T> rowmapper, final Object... param);
	
	List<Object> callMoreResult(final String sql, final int page, final int rows, final List<Object> param, final Object... objects);
	
	Pager<T>  callMoreResultForPager(String sql, final int index, final PageParam page, final RowMapper<T> rowmapper, Object... param);
	
	List<T> queryForList(String sql);
	
	List<T> queryForList(String sql, Class<T> elementType, Object... param);
	
	List<T> queryForList(String sql, Object... param);
	
	Map<String, T> queryForMap(String sql, Object... param);
	
	List<T> query(String sql, RowMapper<T> rowmapper);
	
	List<T> query(String sql, Object[] param, RowMapper<T> rowmapper);
	
	List<T> query(String sql, RowMapper<T> rowmapper, Object... param);
	
	List<T> find(String sql, RowMapper<T> rowmapper, Object... param);
	
    List<T> find(String sql, RowMapper<T> rowmapper, List<T> param);
	
	List<T> find(String sql, int page, int rows, RowMapper<T> rowmapper, Object... param);
	
	List<T> find(String sql, int page, int rows, RowMapper<T> rowmapper,List<T> param);
	
	T get(String sql, RowMapper<T> rowmapper, Object... param);
	
	T get(String sql, RowMapper<T> rowmapper, List<T> param);
	
	T queryForObject(String sql, Class<T> elementType, Object... param);
	
	T queryForObject(String sql, Object[] param, RowMapper<T> rowmapper);
	
	T queryForObject(String sql, RowMapper<T> rowmapper, Object... param);
	
	Pager<T> queryForPager(String sql, int page, int rows, RowMapper<T> rowmapper, Object... param);
	
	Pager<T> queryForPager(String sql, PageParam page, RowMapper<T> rowmapper, Object... param);
	
	Pager<T> queryForPager(String sql, PageParam page, RowMapper<T> rowmapper, List<T> param);
	
	/**************************************************动态Bean****************************************************************/
	
	List<DynamicBean> executeBatch(final List<String> sqls);
	
	List<DynamicBean> call(String sql, Object... param);
	
	List<DynamicBean> call(final String sql, final int page, final int rows, final Object... param);
	
	Pager<DynamicBean>  callForPager(String sql, final PageParam page, Object... param);
	
	List<List<DynamicBean>> callMoreResult(final String sql, final Object... param);
	
	List<DynamicBean> callMoreResult(final String sql, final int index, final Object... param);
	
	List<DynamicBean> callMoreResult(final String sql, final int index, final int page, final int rows, final Object... param);
	
	Pager<DynamicBean>  callMoreResultForPager(String sql, final int index, final PageParam page, Object... param);
	
	List<DynamicBean> find(String sql, Object... param);
	
	List<DynamicBean> find(String sql, List<Object> param);
	
	List<DynamicBean> find(String sql, int page, int rows, Object... param);
	
	List<DynamicBean> find(String sql, int page, int rows, List<Object> param);
	
	DynamicBean get(String sql, Object... param);
	
	DynamicBean get(String sql, List<Object> param);
	
	Pager<DynamicBean> queryForPager(String sql, int page, int rows, Object... param);
	
	Pager<DynamicBean> queryForPager(String sql, PageParam page, Object... param);
	
	Pager<DynamicBean> queryForPager(String sql, PageParam page, List<Object> param);
	
	
}

		
		
		
