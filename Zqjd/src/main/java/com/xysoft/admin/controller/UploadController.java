package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.xysoft.admin.service.UploadService;

@Controller
@RequestMapping(value = "/admin/upload.do")
public class UploadController {
	@Resource
	private UploadService uploadService;
	
	@RequestMapping(params = "file")
	public String uploadImg(HttpServletResponse response, HttpServletRequest request) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		String res = this.uploadService.uploadFile(request);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletResponse response, HttpServletRequest request, 
			@RequestParam("upload") CommonsMultipartFile upload) throws IOException {
		String res = this.uploadService.saveFile(upload);
		response.getWriter().print(res);
		return null;
	}
	
//	@RequestMapping(params = "manager")
//	public String manager(HttpServletResponse response, HttpServletRequest request, String dir, 
//			@RequestParam(defaultValue = "") String path) throws IOException {
//		String res = this.uploadService.getManagerFiles(request, dir, path);
//		response.getWriter().print(res);
//		return null;
//	}
	
}
