package com.xysoft.front.controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.xysoft.admin.service.UploadService;

@Controller
public class AjaxController {
	
	@Resource
	private UploadService uploadService;
	
	//上传文件
	@RequestMapping(value = "/upload.ajx")
	public String uploadFile(HttpServletResponse response, HttpServletRequest request, 
			@RequestParam("upload") CommonsMultipartFile upload) throws Exception {
		String res = this.uploadService.saveFile(upload);
		response.getWriter().print(res);
		return null;
	}
	

}























