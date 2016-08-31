package com.xysoft.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.admin.service.OrderService;
import com.xysoft.dao.OrderDao;
import com.xysoft.support.DynamicBean;
import com.xysoft.support.JdbcDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.util.JsonUtil;

@Component
public class OrderServiceImpl implements OrderService {
	
	@Resource
	private OrderDao orderDao;
	@Resource
	private JdbcDao<DynamicBean> jdbcDao;

	@Transactional(readOnly = true)
	public String getOrders(PageParam page, String phone) {
		String sql = "SELECT us.id AS `user`,us.`name`,us.phone,us.address,od.id,od.createDate,od.modifyDate,od.ordernumber,fs.id AS flowsteprec FROM orders od JOIN `user` us ON od.`user`=us.id JOIN flowsteprec fs ON fs.orders=od.id WHERE us.phone LIKE ?";
		Pager<DynamicBean> objects = this.jdbcDao.queryForPager(sql, page, "%"+phone+"%");
		Pager<Object> pager = new Pager<Object>();
		for (DynamicBean dynamicBean : objects.getDatas()) {
			pager.getDatas().add(dynamicBean.getObject());
		}
		pager.setTotal(objects.getTotal());
		pager.setPageCount(objects.getPageCount());
		pager.setPageIndex(objects.getPageIndex());
		return JsonUtil.toStringFromObject(pager);
	}

}
