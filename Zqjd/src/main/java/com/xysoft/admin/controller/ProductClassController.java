package com.xysoft.admin.controller;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.xysoft.admin.service.ProductClassService;
import com.xysoft.entity.ProductClass;
import com.xysoft.support.BaseController;

@Controller
@RequestMapping(value = "/admin/productClass.do")
public class ProductClassController extends BaseController {
	
	@Resource
	private ProductClassService productClassService;

	@RequestMapping(params = "tree")
	public String tree(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(defaultValue = "") String name)throws IOException {
		String res = this.productClassService.getProductClasses(name);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "parent")
	public String parent(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(defaultValue = "") String name)
			throws IOException {
		String res = this.productClassService.getProductClassNull(name);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletRequest request, HttpServletResponse response, ProductClass param)
			throws IOException {
		String res = this.productClassService.saveProductClass(param);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String delete(HttpServletRequest request, HttpServletResponse response, String id)
			throws IOException {
		String res = this.productClassService.deleteProductClass(id);
		response.getWriter().write(res);
		return null;
	}
	
}
