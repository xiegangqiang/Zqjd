package com.xysoft.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class LoginUrlEntryPoint implements AuthenticationEntryPoint {

	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		String targetUrl = null;  
        String url = request.getRequestURI();  
    
        if(url.indexOf("admin") != -1){  
            //未登录而访问后台受控资源时，跳转到后台登录页面  
            targetUrl = "/admin/login.jsp";  
        }else if(url.indexOf("mobile") != -1) {
        	//未登录而访问后台受控资源时，跳转到移动端登录页面  
        	targetUrl = "/mobile/login.html";  
        }else{  
            //未登录而访问前台受控资源时，跳转到前台登录页面  
            targetUrl = "/login.jhtml";  
        }  
        targetUrl = request.getContextPath() + targetUrl;  
        response.sendRedirect(targetUrl);
	}

}
