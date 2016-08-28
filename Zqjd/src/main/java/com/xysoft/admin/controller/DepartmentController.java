package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xysoft.admin.service.DepartmentService;
import com.xysoft.entity.Department;
import com.xysoft.support.BaseController;
@Controller
@RequestMapping(value = "/admin/department.do")
public class DepartmentController extends BaseController{

	@Resource
	private DepartmentService departmentService;
	
	
	@RequestMapping(params = "tree")
	public String tree(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(defaultValue = "") String name)
			throws IOException {
		String res = this.departmentService.getDepartments(name);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "parent")
	public String parent(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(defaultValue = "") String name)
			throws IOException {
		String res = this.departmentService.getDepartmentNull(name);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletRequest request, HttpServletResponse response, Department param)
			throws IOException {
		String res = this.departmentService.saveDepartment(param);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String delete(HttpServletRequest request, HttpServletResponse response, String id)
			throws IOException {
		String res = this.departmentService.deleteDepartment(id);
		response.getWriter().write(res);
		return null;
	}
	
}
