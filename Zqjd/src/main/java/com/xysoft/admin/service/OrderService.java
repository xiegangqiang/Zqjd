package com.xysoft.admin.service;

import com.xysoft.entity.Orders;
import com.xysoft.entity.User;
import com.xysoft.support.PageParam;

public interface OrderService {

	String getOrders(PageParam page, String phone);

	String getUsersByPhone(String phone);

	String saveOrders(String userId, User user, Orders order, String[] roles, String nextstep);

	String getOrderPosts(String flowsteprec);
	
	String deleteOrders(String id);

	String sendNotice(String order);


}
