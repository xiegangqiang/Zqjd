package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xysoft.admin.service.MenuService;
import com.xysoft.admin.service.RoleService;
import com.xysoft.entity.Role;
import com.xysoft.support.BaseController;
import com.xysoft.support.PageParam;

@Controller
@RequestMapping(value = "/admin/role.do")
public class RoleController extends BaseController{

	@Resource
	private RoleService roleService;
	@Resource
	private MenuService menuService;
	
	@RequestMapping(params = "list")
	public String list(HttpServletRequest request, HttpServletResponse response, PageParam page,
			@RequestParam(defaultValue = "") String name)
			throws IOException {
		String res = this.roleService.getRoles(page, name);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletRequest request, HttpServletResponse response, Role param)
			throws IOException {
		String res = this.roleService.saveRole(param);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String delete(HttpServletRequest request, HttpServletResponse response, String id)
			throws IOException {
		String res = this.roleService.deleteRole(id);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "menu")  
	public String menu(HttpServletRequest request, HttpServletResponse response, String roleId)
			throws IOException {
		String res = this.menuService.getAllActiveMenus(roleId);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "config")  
	public String config(HttpServletRequest request, HttpServletResponse response, String id, String [] menus)
			throws IOException {
		String res = this.roleService.configMenus(id, menus);
		response.getWriter().write(res);
		return null;
	}
}
