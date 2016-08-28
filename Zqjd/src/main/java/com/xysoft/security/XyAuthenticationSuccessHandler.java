package com.xysoft.security;

import java.io.IOException;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.dao.AdminDao;
import com.xysoft.dao.UserDao;
import com.xysoft.entity.Admin;
import com.xysoft.entity.User;
import com.xysoft.util.IpUtil;
import com.xysoft.util.JsonUtil;

public class XyAuthenticationSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements AuthenticationSuccessHandler {
	@Resource
	private AdminDao adminDao;
	@Resource
	private UserDao userDao;
	
	@Transactional
	@SuppressWarnings("unchecked")
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String userAgent = request.getHeader("user-agent").toLowerCase();
		Boolean Client = userAgent.indexOf("linux") >= 0 || userAgent.indexOf("iphone") >= 0 || userAgent.indexOf("ipad") >=0;
		UserDetails userDetail = (UserDetails)authentication.getPrincipal();
		if ("/admin/j_spring_security_check".equals(request.getRequestURI())) {
			Admin admin = this.adminDao.getAdmin(userDetail.getUsername());
			admin.setLoginDate(new Date());
			admin.setLoginIp(IpUtil.getIpAddr(request));
			admin.setLoginFailureCount(0);
			this.adminDao.saveAdmin(admin);
			clearAuthenticationAttributes(request);
			if(Client){
				response.getWriter().write(JsonUtil.toRes("/mobile/index.html"));
			}else {
				response.sendRedirect("/admin/index.jsp");
			}
		}else {
			User user = this.userDao.getUser(userDetail.getUsername());
			user.setLoginDate(new Date());
			user.setLoginIp(IpUtil.getIpAddr(request));
			user.setLoginFailureCount(0);
			this.userDao.saveUser(user);
			clearAuthenticationAttributes(request);
			request.getSession().setAttribute("user", user);
			//response.sendRedirect("/client/home.do?index");告诉客户端重定向方式
			response.getWriter().write("/client/home.do?index");
		}
	}
	
	protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        session.removeAttribute(WebAttributes.LAST_USERNAME);
    }
	
}
