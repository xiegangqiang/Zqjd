package com.xysoft.security;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xysoft.entity.User;

public class XyUserDetailServiceImpl implements UserDetailsService {
	
	@Resource
	private UserSecurityServiceImpl userSecurityServiceImpl;

	@Transactional
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		if ("/admin/j_spring_security_check".equals(request.getRequestURI())) return null;
		User user = userSecurityServiceImpl.getLoginUserInfo(username);
        if(user == null) {  
            throw new UsernameNotFoundException(username);  
        }  
		return user;
	}
}
