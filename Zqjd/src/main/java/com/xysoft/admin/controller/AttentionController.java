package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xysoft.admin.service.AttentionService;
import com.xysoft.entity.Attention;
import com.xysoft.support.BaseController;

@Controller
@RequestMapping(value = "/admin/attention.do")
public class AttentionController extends BaseController {
	@Resource
	private AttentionService attentionService;
	
	@RequestMapping(params = "info")
	public String list(HttpServletResponse response, HttpServletRequest request) 
			throws IOException {
		String res = this.attentionService.getAttention();
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletResponse response, HttpServletRequest request, 
			Attention param) throws IOException {
		String res = this.attentionService.saveAttention(param);
		response.getWriter().print(res);
		return null;
	}
	
}
