package com.xysoft.dao.impl;

import java.util.List;

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

	public void deleteOrders(Orders order) {
		this.delete(order);
	}

	public List<Orders> getOrderByUser(String user) {
		return this.find("from Orders where user = ?", user);
	}

}
