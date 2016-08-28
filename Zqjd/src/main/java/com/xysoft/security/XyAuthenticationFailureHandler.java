package com.xysoft.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.transaction.annotation.Transactional;

import com.xysoft.util.JsonUtil;

public class XyAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Transactional
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		String userAgent = request.getHeader("user-agent").toLowerCase();
		Boolean Client = userAgent.indexOf("linux") >= 0 || userAgent.indexOf("iphone") >= 0 || userAgent.indexOf("ipad") >=0;
		if ("/admin/j_spring_security_check".equals(request.getRequestURI())) {
			if (exception instanceof BadCredentialsException) {
				if(Client) actionJsonError(request, response, "您的用户名或密码错误!");
				else actionAdminError(request, response, "您的用户名或密码错误!");
			} else if (exception instanceof DisabledException) {
				if(Client) actionJsonError(request, response, "您的账号已被禁用,无法登录!");
				else actionAdminError(request, response, "您的账号已被禁用,无法登录!");
			} else if (exception instanceof LockedException) {
				if(Client) actionJsonError(request, response, "您的账号已被锁定,无法登录!");
				else actionAdminError(request, response, "您的账号已被锁定,无法登录!");
			} else if (exception instanceof AccountExpiredException) {
				if(Client) actionJsonError(request, response, "您的账号已过期,无法登录!");
				else actionAdminError(request, response, "您的账号已过期,无法登录!");
			} else {
				//if(Client) actionJsonError(request, response, "出现未知错误,无法登录!");
				//else actionAdminError(request, response, "出现未知错误,无法登录!");
				if(Client) actionJsonError(request, response, exception.getMessage());
				else actionAdminError(request, response, exception.getMessage());
			}
		} else{
			if (exception instanceof BadCredentialsException) {
				actionJsonError(request, response, "您的用户名或密码错误!");
			} else if (exception instanceof DisabledException) {
				actionJsonError(request, response, "您的账号已被禁用,无法登录!");
			} else if (exception instanceof LockedException) {
				actionJsonError(request, response, "您的账号已被锁定,无法登录!");
			} else if (exception instanceof AccountExpiredException) {
				actionJsonError(request, response, "您的账号已过期,无法登录!");
			} else {
				actionJsonError(request, response, "您的用户名或密码错误!");
			}
		}
		
	}
	
	/**
	 * 管理员登录失败跳转页面.
	 */
	private static void actionAdminError(HttpServletRequest request, 
			HttpServletResponse response, String message) {
		try {
			request.setAttribute("ErrorMessage", message);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/login.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 登录失败统一返回json串.
	 */
	@SuppressWarnings({"unchecked"})
	private static void actionJsonError(HttpServletRequest request, 
			HttpServletResponse response, String message) {
		try {
			response.setHeader("Content-Type", "application/json;charset=UTF-8");  
			PrintWriter out =  response.getWriter();
			out.write(JsonUtil.toResOfFail(message));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 前台用户登录失败跳转页面.
	 */
/*	private static void actionUserError(HttpServletRequest request, 
			HttpServletResponse response, String message) {
		try {
			request.setAttribute("ErrorMessage", message);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jhtml");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

}
