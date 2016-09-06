package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xysoft.admin.service.ProductService;
import com.xysoft.entity.Product;
import com.xysoft.support.BaseController;
import com.xysoft.support.PageParam;

@Controller
@RequestMapping(value = "/admin/product.do")
public class ProductController extends BaseController {
	
	@Resource
	private ProductService productService;

	@RequestMapping(params = "list")
	public String list(HttpServletRequest request, HttpServletResponse response, 
			PageParam page,@RequestParam(defaultValue = "") String name)
			throws IOException {
		String res = this.productService.getProducts(page, name);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String exception(HttpServletRequest request, HttpServletResponse response, Product param)
			throws IOException {
		String res = this.productService.saveProduct(param);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String exception(HttpServletRequest request, HttpServletResponse response, String id)
			throws IOException {
		String res = this.productService.deleteProduct(id);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "productClasses")
	public String productClasses(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String res = this.productService.getProductClasses();
		response.getWriter().write(res);
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
