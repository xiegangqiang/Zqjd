package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xysoft.admin.service.WxHomeService;
import com.xysoft.entity.Home;
import com.xysoft.support.BaseController;

@Controller
@RequestMapping(value = "/admin/home.do")
public class WxHomeController extends BaseController {
	@Resource
	private WxHomeService homeService;
	
	@RequestMapping(params = "info")
	public String list(HttpServletResponse response, HttpServletRequest request) 
			throws IOException {
		String res = this.homeService.getHome();
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletResponse response, HttpServletRequest request, 
			Home param) throws IOException {
		String res = this.homeService.saveHome(param);
		response.getWriter().print(res);
		return null;
	}
	
}
