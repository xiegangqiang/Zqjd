package com.xysoft.front.controller;

import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.xysoft.front.service.FrontService;
import com.xysoft.util.RequestUtil;

@Controller
public class FrontController {
	@Resource
	private FrontService frontService;
	
	@ExceptionHandler({Exception.class})   
    public ModelAndView exception(Exception e, HttpServletResponse response, 
    		HttpServletRequest request) throws Exception {   
		e.printStackTrace();
		Map<String, Object> model = this.frontService.error();
		return new ModelAndView(model.get("model").toString(), RequestUtil.initFrontMap(model)); 
    }
	
	@RequestMapping(value = "/404.jhtml")
	public ModelAndView error404(HttpServletResponse response, HttpServletRequest request) throws Exception {
		Map<String, Object> model = this.frontService.error404();
		return new ModelAndView(model.get("model").toString(), RequestUtil.initFrontMap(model));
	}
	
	@RequestMapping(value = "/500.jhtml")
	public ModelAndView error500(HttpServletResponse response, HttpServletRequest request) throws Exception {
		Map<String, Object> model = this.frontService.error500();
		return new ModelAndView(model.get("model").toString(), RequestUtil.initFrontMap(model));
	}
	
	@RequestMapping(value = "/login.jhtml")
	public ModelAndView login(HttpServletResponse response, HttpServletRequest request) throws Exception {
		Map<String, Object> model = this.frontService.login();
		return new ModelAndView(model.get("model").toString(), RequestUtil.initFrontMap(model));
	}
	
	@RequestMapping(value = "/index.jhtml")
	public ModelAndView index(HttpServletResponse response, HttpServletRequest request) throws Exception {
		Map<String, Object> model = this.frontService.index();
		return new ModelAndView(model.get("model").toString(), RequestUtil.initFrontMap(model));
	}
	
	

	
}
