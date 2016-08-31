package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xysoft.admin.service.WxUserService;
import com.xysoft.entity.WxUser;
import com.xysoft.entity.WxUserGroup;
import com.xysoft.support.BaseController;
import com.xysoft.support.PageParam;

@Controller
@RequestMapping(value = "/admin/wxUser.do")
public class WxUserController extends BaseController{
	
	@Resource
	private WxUserService wxUserService;
	
	@RequestMapping(params = "list")
	public String list(HttpServletResponse response, HttpServletRequest request, 
			PageParam page, @RequestParam(defaultValue = "") String nickname, @RequestParam(defaultValue = "")String groupid,
			@RequestParam(defaultValue = "")String[] subscribe_time, @RequestParam(defaultValue = "")String sex) 
			throws IOException {
		String res = this.wxUserService.getWxUsers(page, nickname, groupid, subscribe_time, sex);
		response.getWriter().print(res);
		return null;
	}

	@RequestMapping(params = "synchronizeWx")
	public String synchronizeWx(HttpServletResponse response, HttpServletRequest request) 
			throws IOException {
		String res = this.wxUserService.synchronizeWx();
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "update")
	public String update(HttpServletResponse response, HttpServletRequest request, WxUser param) 
			throws IOException {
		String res = this.wxUserService.updateWxUser(param);
		response.getWriter().print(res);
		return null;
	}
	
	@RequestMapping(params = "group")
	public String group(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String res = this.wxUserService.getWxUserGroups();
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "saveGroup")
	public String saveGroup(HttpServletRequest request, HttpServletResponse response, WxUserGroup param)
			throws IOException {
		String res = this.wxUserService.saveWxUserGroup(param);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "deleteGroup")
	public String deleteGroup(HttpServletRequest request, HttpServletResponse response, String id)
			throws IOException {
		String res = this.wxUserService.deleteWxUserGroup(id);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "batchMoveGroup")
	public String batchMoveGroup(HttpServletRequest request, HttpServletResponse response, String[] openid_list, String to_groupid)
			throws IOException {
		String res = this.wxUserService.batchMoveGroup(openid_list, to_groupid);
		response.getWriter().write(res);
		return null;
	}
	
	
	
	
}
