package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import com.xysoft.admin.service.EvaluatService;
import com.xysoft.entity.Evaluat;
import com.xysoft.support.BaseController;
import com.xysoft.support.PageParam;

@Controller
@RequestMapping(value = "/admin/evaluat.do")
public class EvaluatController extends BaseController {
	
	@Resource
	private EvaluatService evaluatService;

	@RequestMapping(params = "list")
	public String list(HttpServletRequest request, HttpServletResponse response, PageParam page, 
			@RequestParam(defaultValue = "") String name) throws IOException {
		String res = this.evaluatService.getEvaluats(page, name);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletRequest request, HttpServletResponse response, Evaluat param) throws IOException {
		String res = this.evaluatService.saveEvaluat(param);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String delete(HttpServletRequest request, HttpServletResponse response, String id) throws IOException {
		String res = this.evaluatService.deleteEvaluat(id);
		response.getWriter().write(res);
		return null;
	}
	
}
