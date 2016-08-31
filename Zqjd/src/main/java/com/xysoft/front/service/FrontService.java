package com.xysoft.front.service;

import java.util.Map;

public interface FrontService {
	
	/**
	 * 错误页面索引.
	 */
	public Map<String, Object> error();
	
	/**
	 * 404错误页面索引
	 */
	public Map<String, Object> error404();
	
	/**
	 * 500错误页面索引
	 */
	public Map<String, Object> error500();

	/**
	 * 登陆页索引
	 */
	public Map<String, Object> login();
	
	/**
	 * 首页索引.
	 */
	public Map<String, Object> index();

	/**
	 * 扫描送礼索引
	 */
	public Map<String, Object> scangift();

	/**
	 * 提交电话号码换礼品码
	 */
	public String getGiftCode(String phone);

	


	

}
