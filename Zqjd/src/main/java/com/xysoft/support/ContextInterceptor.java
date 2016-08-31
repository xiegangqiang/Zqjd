package com.xysoft.support;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ContextInterceptor extends HandlerInterceptorAdapter {
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws ServletException {
		response.setContentType("text/html;charset=UTF-8");
		if(request.getSession().getAttribute("SPRING_SECURITY_CONTEXT") == null){
			try {
				String ajax = request.getHeader("x-Requested-with");  
		        if(ajax != null && ajax.equals("XMLHttpRequest")){  
		        	response.setHeader("sessionstatus", "timeout");  
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}  
			return false;
		}else
		return true;
	}
	
}
