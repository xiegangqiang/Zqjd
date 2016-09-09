package com.xysoft.tag.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.support.DataPager;
import com.xysoft.support.DynamicBean;
import com.xysoft.support.JdbcDao;
import com.xysoft.support.Pager;
import com.xysoft.tag.service.DynamicBeanTagService;
import com.xysoft.util.NullUtils;
import com.xysoft.util.SqlUtil;

@Component
public class DynamicBeanTagServiceImpl implements DynamicBeanTagService{
	
	@Resource
	private JdbcDao<DynamicBean> jdbcDao;

	@Transactional(readOnly = true)
	public DataPager<Object> getDynamicBeans(String sql, String table, String where, Integer page, Integer count) {
		if(NullUtils.isEmpty(sql)) {
			sql = "SELECT * FROM "+table;
			if(NullUtils.isNotEmpty(where)) sql += " WHERE "+where;
			sql += " ORDER BY createDate DESC";
		}
		Pager<DynamicBean>  beans = this.jdbcDao.queryForPager(sql, page, count);
		DataPager<Object> pager = new DataPager<Object>(SqlUtil.DynamicToBean(beans.getDatas()), beans.getTotal());
		return pager;
	}

}
