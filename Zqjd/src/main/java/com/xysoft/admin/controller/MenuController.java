package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xysoft.admin.service.MenuService;
import com.xysoft.support.BaseController;

@Controller
@RequestMapping(value = "/admin/menu.do")
public class MenuController extends BaseController {
	@Resource
	private MenuService menuService;
	
	@RequestMapping(params = "tree")
	public String tree(HttpServletResponse response, HttpServletRequest request, String module) 
			throws IOException {
		String res = this.menuService.getMenus(module);
		response.getWriter().print(res);
		return null;
	}
	
}
