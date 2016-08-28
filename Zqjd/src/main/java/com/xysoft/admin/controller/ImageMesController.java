package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xysoft.admin.service.ImageMesService;
import com.xysoft.entity.ImageMes;
import com.xysoft.support.BaseController;
import com.xysoft.support.PageParam;

@Controller
@RequestMapping(value = "/admin/imageMes.do")
public class ImageMesController extends BaseController {
	@Resource
	private ImageMesService imageMesService;
	
	@RequestMapping(params = "list")
	public String list(HttpServletResponse response, HttpServletRequest request, 
			PageParam page, @RequestParam(defaultValue = "") String name) 
			throws IOException {
		String res = this.imageMesService.getImageMess(page, name);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletResponse response, HttpServletRequest request, 
			ImageMes param) throws IOException {
		String res = this.imageMesService.saveImageMes(param);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String delete(HttpServletResponse response, HttpServletRequest request, 
			String id) throws IOException {
		String res = this.imageMesService.deleteImageMes(id);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "cls")
	public String classify(HttpServletResponse response, HttpServletRequest request, 
			String id) throws IOException {
		String res = this.imageMesService.getClassifys();
		response.getWriter().print(res);
		return null;
	}
}
