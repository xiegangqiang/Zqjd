package com.xysoft.tag.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.support.DynamicBean;
import com.xysoft.support.JdbcDao;
import com.xysoft.tag.service.InfoTagService;

@Component
public class InfoTagServiceImpl implements InfoTagService {
	
	@Resource
	private JdbcDao<DynamicBean> jdbcDao;

	@Transactional(readOnly = true)
	public Object getInfo(String markcode) {
		String sql = "SELECT * FROM info WHERE markcode = ?";
		DynamicBean bean = this.jdbcDao.get(sql, markcode);
		return bean.getObject();
	}

}
