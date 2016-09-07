package com.xysoft.tag.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xysoft.support.DataPager;
import com.xysoft.support.DynamicBean;
import com.xysoft.support.JdbcDao;
import com.xysoft.support.Pager;
import com.xysoft.tag.service.ProductClassTagService;
import com.xysoft.util.SqlUtil;

@Component
public class ProductClassTagServiceImpl implements ProductClassTagService {
	
	@Resource
	private JdbcDao<DynamicBean> jdbcDao;

	@Transactional(readOnly = true)
	public DataPager<Object> getProductClasses(Integer page, Integer count) {
		String sql = "SELECT * FROM productclass WHERE parentId <> '' ";
		Pager<DynamicBean>  beans = this.jdbcDao.queryForPager(sql, page, count);
		DataPager<Object> pager = new DataPager<Object>(SqlUtil.DynamicToBean(beans.getDatas()), beans.getTotal());
		return pager;
	}

}
