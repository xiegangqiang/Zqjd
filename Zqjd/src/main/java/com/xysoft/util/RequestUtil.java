package com.xysoft.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.xysoft.common.ElementConst;

/**
 * request工具类.
 */
public class RequestUtil {
	
	/**
	 * 获取当前登录用户.
	 */
	public static String getUsername() {
		if ("anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) return "";
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userDetails.getUsername();
	}
	
	/**
	 * 获取当前登录用户已有的权限.
	 */
	public static List<String> getActionsByLoginUser() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<String> resLst = new ArrayList<String>();
		for (GrantedAuthority granted : userDetails.getAuthorities()) {
			resLst.add(granted.getAuthority());
		}
		return resLst;
	}
	
	
	public static Map<String, Object> initFrontMap(Map<String, Object> map) {
		map.put("sys_http", ElementConst.Service_Address);
		return map;
	}
	
	
	/**
	 * 记录登陆人第几场考试ID
	 */
	public static void setExamId(String id) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		request.getSession().setAttribute("EXAM_ID", id);
	}
	
	/**
	 * 获取登陆人第几场考试ID
	 */
	public static String getExamId() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getSession().getAttribute("EXAM_ID").toString();
	}
	
}
