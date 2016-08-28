package com.xysoft.admin.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public interface UploadService {
	/**
	 * 上传kindeditor文件.
	 */
	public String uploadFile(HttpServletRequest request);
	
//	/**
//	 * 文件空间管理器.
//	 */
//	public String getManagerFiles(HttpServletRequest request, String dir, String path);
	
	/**
	 * 上传图片.
	 */
	public String saveFile(CommonsMultipartFile upload);
}
