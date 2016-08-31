package com.xysoft.support;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("rawtypes")
public interface BaseDao<T> {
	
	public void save(T o);

	public void update(T o);

	public void saveOrUpdate(T o);

	public void merge(T o);

	public void delete(T o);

	public List<T> find(String hql, Object... param);

	public List<T> find(String hql, List param);
	
	public List<T> find(String hql, int page, int rows, List param);

	public List<T> find(String hql, int page, int rows, Object... param);

	public T get(Class c, Serializable id);

	public T get(String hql, Object... param);

	public T get(String hql, List param);

	public T load(Class c, Serializable id);

	public int count(String hql, Object... param);

	public int count(String hql, List param);
	
	public int count(String sql);
	
	public int exec(String hql, Object... param);
	
	public Pager<T> getForPager(String hql, int page, int rows, List param);
	
	public Pager<T> getForPager(String hql, PageParam page, List param);
	
	public Pager<T> getForPager(String hql, int page, int rows, Object... param);
	
	public Pager<T> getForPager(String hql, PageParam page, Object... param);
	
	public List getBySql(String sql);
	
	public List<T> getInitList(String hql);
	
	public List<T> findNoPage(String hql, Object... param); 
	
	public Pager getForPagerBySql(Class clss, String sql, int page, int rows);
}
