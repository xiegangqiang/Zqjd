package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xysoft.admin.service.ClassifyService;
import com.xysoft.entity.Classify;
import com.xysoft.support.BaseController;
import com.xysoft.support.PageParam;

@Controller
@RequestMapping(value = "/admin/classify.do")
public class ClassifyController extends BaseController {
	@Resource
	private ClassifyService classifyService;
	
	@RequestMapping(params = "list")
	public String list(HttpServletResponse response, HttpServletRequest request, 
			PageParam page, @RequestParam(defaultValue = "") String name) 
			throws IOException {
		String res = this.classifyService.getClassifys(page, name);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletResponse response, HttpServletRequest request, 
			Classify param) throws IOException {
		String res = this.classifyService.saveClassify(param);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String delete(HttpServletResponse response, HttpServletRequest request, 
			String id) throws IOException {
		String res = this.classifyService.deleteClassify(id);
		response.getWriter().print(res);
		return null;
	}
}
