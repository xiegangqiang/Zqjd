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

import com.xysoft.entity.Admin;

public class XyAdminDetailServiceImpl implements UserDetailsService {
	
	@Resource
	private UserSecurityServiceImpl userSecurityServiceImpl;

	@Transactional
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		if ("/j_spring_security_check".equals(request.getRequestURI())) return null;
		Admin admin = this.userSecurityServiceImpl.getLoginAdminInfo(username);
        if(admin == null) {  
            throw new UsernameNotFoundException(username);  
        }  
		return admin;
	}
}
