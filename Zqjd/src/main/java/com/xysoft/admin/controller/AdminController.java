package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xysoft.admin.service.AdminService;
import com.xysoft.admin.service.RoleService;
import com.xysoft.entity.Admin;
import com.xysoft.support.BaseController;
import com.xysoft.support.PageParam;

@Controller
@RequestMapping(value = "/admin/admin.do")
public class AdminController extends BaseController { 
	@Resource
	private AdminService adminService;
	@Resource
	private RoleService roleService;
	
	@RequestMapping(params = "list")
	public String list(HttpServletResponse response, HttpServletRequest request, 
			PageParam page, @RequestParam(defaultValue = "") String name, 
			@RequestParam(defaultValue = "") String departmentName, Boolean online) 
			throws IOException {
		String res = this.adminService.getAdmins(page, name, departmentName, online);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletResponse response, HttpServletRequest request, 
			Admin param, String[] roles) throws IOException {
		String res = this.adminService.saveAdmin(param, roles);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "saveBatch")
	public String saveBatch(HttpServletResponse response, HttpServletRequest request, String username, String password, String department, Integer number, String[] roles) throws IOException {
		String res = this.adminService.saveAdminBatch(username, password, department, number, roles);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String delete(HttpServletResponse response, HttpServletRequest request, 
			String id) throws IOException {
		String res = this.adminService.deleteAdmin(id);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "info")
	public String info(HttpServletResponse response, HttpServletRequest request, 
			String id) throws IOException {
		String res = this.adminService.getAdminInfo(id);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "role")
	public String role(HttpServletResponse response, HttpServletRequest request, 
			String adminId) throws IOException {
		String res = this.roleService.getAllActiveRoles(adminId);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "update")
	public String update(HttpServletResponse response, HttpServletRequest request, 
			Admin param) throws IOException {
		String res = this.adminService.updateAdmin(param);
		response.getWriter().print(res);
		return null;
	}
}
