package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xysoft.admin.service.ModuleService;
import com.xysoft.entity.Module;
import com.xysoft.support.BaseController;
import com.xysoft.support.PageParam;

@Controller
@RequestMapping(value = "/admin/module.do")
public class ModuleController extends BaseController{
	
	@Resource
	private ModuleService moduleService;

	@RequestMapping(params = "list")
	public String list(HttpServletRequest request, HttpServletResponse response, PageParam page, 
			@RequestParam(defaultValue = "") String name)
			throws IOException {
		String res = this.moduleService.getModules(page, name);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletRequest request, HttpServletResponse response, Module param, String[] roles)
			throws IOException {
		String res = this.moduleService.saveModule(param, roles);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String delete(HttpServletRequest request, HttpServletResponse response, String id)
			throws IOException {
		String res = this.moduleService.deleteModule(id);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "role")
	public String role(HttpServletRequest request, HttpServletResponse response, String moduleId)
			throws IOException {
		String res = this.moduleService.getAllActiveRoles(moduleId);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "module")
	public String module(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String res = this.moduleService.getAllActiveModule();
		response.getWriter().write(res);
		return null;
	}
	
	
	
	
	
	
	
	
	
}
