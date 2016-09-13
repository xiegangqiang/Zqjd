package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xysoft.admin.service.OrderService;
import com.xysoft.entity.Orders;
import com.xysoft.entity.User;
import com.xysoft.support.BaseController;
import com.xysoft.support.PageParam;
@Controller
@RequestMapping(value = "/admin/orders.do")
public class OrderController extends BaseController{
	
	@Resource
	private OrderService orderService;

	@RequestMapping(params = "list")
	public String list(HttpServletResponse response, HttpServletRequest request, 
			PageParam page, @RequestParam(defaultValue = "") String phone) 
			throws IOException {
		String res = this.orderService.getOrders(page, phone);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "users")
	public String user(HttpServletResponse response, HttpServletRequest request, @RequestParam(defaultValue = "") String phone) 
			throws IOException {
		String res = this.orderService.getUsersByPhone(phone);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletResponse response, HttpServletRequest request, String userId, User user, Orders order, String[] roles, String nextstep) 
			throws IOException {
		String res = this.orderService.saveOrders(userId, user, order, roles, nextstep);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "posts")
	public String posts(HttpServletResponse response, HttpServletRequest request, String flowsteprec) 
			throws IOException {
		String res = this.orderService.getOrderPosts(flowsteprec);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String delete(HttpServletResponse response, HttpServletRequest request, String id) 
			throws IOException {
		String res = this.orderService.deleteOrders(id);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "notice")
	public String notice(HttpServletResponse response, HttpServletRequest request, String order) 
			throws IOException {
		String res = this.orderService.sendNotice(order);
		response.getWriter().print(res);
		return null;
	}
}
