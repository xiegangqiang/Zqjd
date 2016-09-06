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
	
	@RequestMapping(value = "/scangift.jhtml")
	public ModelAndView scangift(HttpServletResponse response, HttpServletRequest request) throws Exception {
		Map<String, Object> model = this.frontService.scangift();
		return new ModelAndView(model.get("model").toString(), RequestUtil.initFrontMap(model));
	}
	
	@RequestMapping(value = "/entry.jhtml")
	public ModelAndView entry(HttpServletResponse response, HttpServletRequest request) throws Exception {
		Map<String, Object> model = this.frontService.entry();
		return new ModelAndView(model.get("model").toString(), RequestUtil.initFrontMap(model));
	}

	@RequestMapping(value = "/wxcenter.jhtml")
	public ModelAndView wxcenter(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String code = request.getParameter("code");
		Map<String, Object> model = this.frontService.wxcenter(code);
		return new ModelAndView(model.get("model").toString(), RequestUtil.initFrontMap(model));
	}
	
	@RequestMapping(value = "/orderlist.jhtml")
	public ModelAndView orderlist(HttpServletResponse response, HttpServletRequest request, String wxUser, String phone) throws Exception {
		Map<String, Object> model = this.frontService.orderlist(wxUser,  phone);
		return new ModelAndView(model.get("model").toString(), RequestUtil.initFrontMap(model));
	}
	
	@RequestMapping(value = "/ordermark.jhtml")
	public ModelAndView ordermark(HttpServletResponse response, HttpServletRequest request, String order, String phone) throws Exception {
		Map<String, Object> model = this.frontService.ordermark(order, phone);
		return new ModelAndView(model.get("model").toString(), RequestUtil.initFrontMap(model));
	}
	
	@RequestMapping(value = "/mymark.jhtml")
	public ModelAndView mymark(HttpServletResponse response, HttpServletRequest request, String wxUser, String phone) throws Exception {
		Map<String, Object> model = this.frontService.mymark(wxUser, phone);
		return new ModelAndView(model.get("model").toString(), RequestUtil.initFrontMap(model));
	}
	
	@RequestMapping(value = "/wxhome.jhtml")
	public ModelAndView wxhome(HttpServletResponse response, HttpServletRequest request, String anchor) throws Exception {
		Map<String, Object> model = this.frontService.wxhome(anchor);
		return new ModelAndView(model.get("model").toString(), RequestUtil.initFrontMap(model));
	}
}
