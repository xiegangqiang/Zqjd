package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xysoft.admin.service.TextMesService;
import com.xysoft.entity.TextMes;
import com.xysoft.support.BaseController;
import com.xysoft.support.PageParam;

@Controller
@RequestMapping(value = "/admin/textMes.do")
public class TextMesController extends BaseController {
	@Resource
	private TextMesService textMesService;
	
	@RequestMapping(params = "list")
	public String list(HttpServletResponse response, HttpServletRequest request, 
			PageParam page, @RequestParam(defaultValue = "") String name) 
			throws IOException {
		String res = this.textMesService.getTextMess(page, name);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletResponse response, HttpServletRequest request, 
			TextMes param) throws IOException {
		String res = this.textMesService.saveTextMes(param);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String delete(HttpServletResponse response, HttpServletRequest request, 
			String id) throws IOException {
		String res = this.textMesService.deleteTextMes(id);
		response.getWriter().print(res);
		return null;
	}
}
