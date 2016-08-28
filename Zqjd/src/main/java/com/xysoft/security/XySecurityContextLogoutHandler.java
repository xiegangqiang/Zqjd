package com.xysoft.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.Assert;

public class XySecurityContextLogoutHandler implements LogoutHandler {
	private boolean invalidateHttpSession = true;

	public void logout(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) {
		Assert.notNull(request, "HttpServletRequest required");
        String userAgent = request.getHeader("user-agent").toLowerCase();
        Boolean Client = userAgent.indexOf("linux") >= 0 || userAgent.indexOf("iphone") >= 0 || userAgent.indexOf("ipad") >=0;
        if (invalidateHttpSession) {
            HttpSession session = request.getSession(false);
            if (session != null) {
            	 if ("/admin/logout".equals(request.getRequestURI())) {
                 	session.invalidate();
                 } else {
                 	session.removeAttribute("user");
                 }
            }
        }
        SecurityContextHolder.clearContext();
        try {
        	if ("/admin/logout".equals(request.getRequestURI())) {
        		if(Client) response.sendRedirect("/mobile/login.html");
        		else response.sendRedirect("/admin/login.jsp");
            } else {
            	response.sendRedirect("/login.jhtml");
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}

	public boolean isInvalidateHttpSession() {
        return invalidateHttpSession;
    }
	
	public void setInvalidateHttpSession(boolean invalidateHttpSession) {
        this.invalidateHttpSession = invalidateHttpSession;
    }
}
