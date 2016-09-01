package com.xysoft.dao.impl;

import org.springframework.stereotype.Component;
import com.xysoft.dao.OrderDao;
import com.xysoft.entity.Orders;
import com.xysoft.support.BaseDaoImpl;

@Component
public class OrderDaoImpl extends BaseDaoImpl<Orders> implements OrderDao{

	public Orders getOrder(String id) {
		return this.get(Orders.class, id);
	}

	public void saveOrder(Orders order) {
		this.saveOrUpdate(order);
	}

}
