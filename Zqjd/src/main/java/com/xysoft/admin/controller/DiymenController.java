package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xysoft.admin.service.DiymenService;
import com.xysoft.entity.Diymen;
import com.xysoft.support.BaseController;

@Controller
@RequestMapping(value = "/admin/diymen.do")
public class DiymenController extends BaseController {
	@Resource
	private DiymenService diymenService;
	
	@RequestMapping(params = "tree")
	public String tree(HttpServletResponse response, HttpServletRequest request) 
			throws IOException {
		String res = this.diymenService.getDiymenes();
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "parent")
	public String treecheck(HttpServletResponse response, HttpServletRequest request) 
			throws IOException {
		String res = this.diymenService.getDiymenesNull();
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletResponse response, HttpServletRequest request, 
			Diymen param) throws IOException {
		String res = this.diymenService.saveDiymen(param);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String delete(HttpServletResponse response, HttpServletRequest request, 
			String id) throws IOException {
		String res = this.diymenService.deleteDiymen(id);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "createMenu")
	public String createMenu(HttpServletResponse response, HttpServletRequest request) 
			throws IOException {
		String res = this.diymenService.createMenu();
		response.getWriter().print(res);
		return null;
	}
}
