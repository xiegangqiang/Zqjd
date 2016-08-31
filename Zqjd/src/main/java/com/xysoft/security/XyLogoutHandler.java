package com.xysoft.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class XyLogoutHandler extends AbstractAuthenticationTargetUrlRequestHandler implements LogoutSuccessHandler {
	
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
//		if (request.getSession().getAttribute("sys_logintype") == null) {
//			response.sendRedirect("/index.jhtml");
//		} else {
//			int type = Integer.valueOf(request.getSession().getAttribute("sys_logintype").toString());
//			request.getSession().removeAttribute("sys_logintype");
//			String[] roleInfo = InitializationConst.getRoleInfo(type);
//			response.sendRedirect(roleInfo[1]);
//		}
	}

}
