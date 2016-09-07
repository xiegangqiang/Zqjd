package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xysoft.admin.service.InfoService;
import com.xysoft.entity.Info;
import com.xysoft.support.PageParam;

@Controller
@RequestMapping(value = "/admin/info.do")
public class InfoController {
	
	@Resource
	private InfoService infoService;

	@RequestMapping(params = "list")
	public String list(HttpServletRequest request, HttpServletResponse response, 
		PageParam page, 	@RequestParam(defaultValue = "") String name)
		throws IOException {
		String res = this.infoService.getInfos(page,name);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletRequest request, HttpServletResponse response, Info param)
		throws IOException {
		String res = this.infoService.saveInfo(param);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String delete(HttpServletRequest request, HttpServletResponse response, String id)
		throws IOException {
		String res = this.infoService.deleteInfo(id);
		response.getWriter().write(res);
		return null;
	}
	
}
