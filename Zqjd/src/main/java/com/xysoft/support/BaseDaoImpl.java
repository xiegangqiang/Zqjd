package com.xysoft.support;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

@Component("baseDao")
@SuppressWarnings({"rawtypes", "unchecked"})
public class BaseDaoImpl<T> implements BaseDao<T> {
	private HibernateTemplate hibernateTemplate;
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		hibernateTemplate = new HibernateTemplate(sessionFactory);
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public void save(T o) {
		this.getCurrentSession().save(o);
	}

	public void update(T o) {
		this.getCurrentSession().update(o);
	}

	public void saveOrUpdate(T o) {
		this.getCurrentSession().saveOrUpdate(o);
	}

	public void merge(T o) {
		this.getCurrentSession().merge(o);
	}

	public void delete(T o) {
		this.getCurrentSession().delete(o);
	}

	public List<T> find(String hql, List param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return q.list();
	}

	public List<T> find(String hql, Object... param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.list();
	}

	public List<T> find(String hql, int page, int rows, List param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}
	

	public List<T> find(String hql, int page, int rows, Object... param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	public T get(Class c, Serializable id) {
		return (T)this.getCurrentSession().get(c, id);
	}

	public T load(Class c, Serializable id) {
		return (T)this.getCurrentSession().load(c, id);
	}

	public T get(String hql, Object... param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return (T)q.uniqueResult();
	}

	public T get(String hql, List param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return (T)q.uniqueResult();
	}
	
	public int exec(String hql, Object... param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.executeUpdate();
	}

	public int count(String hql, Object... param) {
		Query q = this.getCurrentSession().createQuery("select count(*) " + hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return Integer.valueOf(q.uniqueResult().toString());
	}

	public int count(String hql, List param) {
		Query q = this.getCurrentSession().createQuery("select count(*) " + hql);
		if (param != null && param.size() > 0) {
			for (int i = 0; i < param.size(); i++) {
				q.setParameter(i, param.get(i));
			}
		}
		return Integer.valueOf(q.uniqueResult().toString());
	}
	
	public Pager<T> getForPager(String hql, PageParam page, List param) {
		Pager<T> pager = new Pager<T>();
		List<T> list = this.find(hql, page.getPage(), page.getLimit(), param);
		int count = this.count(hql, param);
		pager.setDatas(list);
		pager.setTotal(count);
		return pager;
	}
	
	public Pager<T> getForPager(String hql, PageParam page, Object... param) {
		Pager<T> pager = new Pager<T>();
		List<T> list = this.find(hql, page.getPage(), page.getLimit(), param);
		int count = this.count(hql, param);
		pager.setDatas(list);
		pager.setTotal(count);
		pager.setPageCount(count % page.getLimit() == 0? count / page.getLimit() : (count / page.getLimit()) + 1);
		pager.setPageIndex(page.getPage());
		return pager;
	}

	public Pager<T> getForPager(String hql, int page, int rows, List param) {
		Pager<T> pager = new Pager<T>();
		List<T> list = this.find(hql, page, rows, param);
		int count = this.count(hql, param);
		pager.setDatas(list);
		pager.setTotal(count);
		return pager;
	}

	public Pager<T> getForPager(String hql, int page, int rows, Object... param) {
		Pager<T> pager = new Pager<T>();
		List<T> list = this.find(hql, page, rows, param);
		int count = this.count(hql, param);
		pager.setDatas(list);
		pager.setTotal(count);
		pager.setPageCount(count % rows == 0? count / rows : (count / rows) + 1);
		pager.setPageIndex(page);
		return pager;
	}

	public List getBySql(String sql) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
		return query.list();
	}
	
	public int execBySql(String sql) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
		return query.executeUpdate();
	}

	public List<T> getInitList(String hql) {
		return (List<T>) this.hibernateTemplate.find(hql);
	}
	
	public List<T> findNoPage(String hql, Object... param) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (param != null && param.length > 0) {
			for (int i = 0; i < param.length; i++) {
				q.setParameter(i, param[i]);
			}
		}
		return q.list();
	}

	public int count(String sql) {
		SQLQuery query = this.getCurrentSession().createSQLQuery("select count(*) from ( " + sql+") t");
		return Integer.valueOf(query.uniqueResult().toString());
	}
	
	public Pager getForPagerBySql(Class clss, String sql, int page, int rows) {
		Pager pager = new Pager();
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
		List list = query.setResultTransformer(Transformers.aliasToBean(clss)).setFirstResult((page - 1) * rows).setMaxResults(rows).list();
		int count = this.count(sql);
		pager.setDatas(list);
		pager.setTotal(count);
		pager.setPageCount(count % rows == 0? count / rows : (count / rows) + 1);
		pager.setPageIndex(page);
		return pager;
	}
}
