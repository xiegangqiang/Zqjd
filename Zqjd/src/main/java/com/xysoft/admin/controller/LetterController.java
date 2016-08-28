package com.xysoft.admin.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xysoft.admin.service.LetterService;
import com.xysoft.entity.Letter;
import com.xysoft.support.BaseController;
import com.xysoft.support.PageParam;

@Controller
@RequestMapping(value = "/admin/letter.do")
public class LetterController extends BaseController{
	
	@Resource
	private LetterService letterService;

	@RequestMapping(params = "list")
	public String list(HttpServletRequest request, HttpServletResponse response, PageParam page)
			throws IOException {
		String res = this.letterService.getLetters(page);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "save")
	public String save(HttpServletRequest request, HttpServletResponse response, Letter param)
			throws IOException {
		String res = this.letterService.saveLetter(param);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "delete")
	public String delete(HttpServletRequest request, HttpServletResponse response, String ids)
			throws IOException {
		String res = this.letterService.deleteLetter(ids);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "send")
	public String send(HttpServletRequest request, HttpServletResponse response, String id)
			throws IOException {
		String res = this.letterService.sendLetter(id);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "info")
	public String info(HttpServletRequest request, HttpServletResponse response, String admin, Boolean state)
			throws IOException {
		String res = this.letterService.getMessages(admin, state);
		response.getWriter().write(res);
		return null;
	}
	
	@RequestMapping(params = "read")
	public String read(HttpServletRequest request, HttpServletResponse response, Integer type, String admin, String letter)
			throws IOException {
		String res = this.letterService.readMessage(type, admin, letter);
		response.getWriter().write(res);
		return null;
	}
}
