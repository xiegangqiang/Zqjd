package com.xysoft.client.contorller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xysoft.client.service.HomeService;
import com.xysoft.support.BaseController;
import com.xysoft.util.RequestUtil;

@Controller
@RequestMapping(value = "/client/home.do")
public class HomeController extends BaseController {
	
	@Resource
	private HomeService homeService;
	
	@RequestMapping(params = "authority")
	public String authority(HttpServletResponse response, HttpServletRequest request) throws IOException {
		String res = this.homeService.getAuthority();
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "userTypeAuthority")
	public String userTypeAuthority(HttpServletResponse response, HttpServletRequest request) throws IOException {
		String res = this.homeService.getUserTypeAuthority();
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "saveAuthority")
	public String saveAuthority(HttpServletResponse response, HttpServletRequest request, String[] authoritys) throws IOException {
		String res = this.homeService.saveUserAuthority(authoritys);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "welcome")
	public ModelAndView hint(HttpServletResponse response, HttpServletRequest request) 
			throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("client/layout/welcome", RequestUtil.initFrontMap(model));
	}
	
	@RequestMapping(params = "index")
	public ModelAndView index(HttpServletResponse response, HttpServletRequest request) 
			throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("client/index", RequestUtil.initFrontMap(model));
	}
	
	@RequestMapping(params = "top")
	public ModelAndView top(HttpServletResponse response, HttpServletRequest request) 
			throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("client/layout/top", RequestUtil.initFrontMap(model));
	}
	
	@RequestMapping(params = "left")
	public ModelAndView left(HttpServletResponse response, HttpServletRequest request) 
			throws IOException {
		Map<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("client/layout/left", RequestUtil.initFrontMap(model));
	}


	
}
