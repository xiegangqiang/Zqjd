package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xysoft.admin.service.OrderService;
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
	
}
