package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.Orders;
import com.xysoft.support.BaseDao;

public interface OrderDao extends BaseDao<Orders>{

	Orders getOrder(String id);

	void saveOrder(Orders order);

	void deleteOrders(Orders order);

	List<Orders> getOrderByUser(String user);

}
