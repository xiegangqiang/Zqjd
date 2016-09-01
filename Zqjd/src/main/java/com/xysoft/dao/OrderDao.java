package com.xysoft.dao;

import com.xysoft.entity.Orders;
import com.xysoft.support.BaseDao;

public interface OrderDao extends BaseDao<Orders>{

	Orders getOrder(String id);

	void saveOrder(Orders order);

}
