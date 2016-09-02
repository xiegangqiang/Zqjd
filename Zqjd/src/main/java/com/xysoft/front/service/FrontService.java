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
	
	/**
	 * 个人中心入口
	 */
	public Map<String, Object> entry();

	/**
	 * 微信个人中心索引
	 */
	public Map<String, Object> wxcenter(String code);

	/**
	 * 订单列表索引
	 */
	public Map<String, Object> orderlist(String phone);

	/**
	 * 订单评价索引
	 */
	public Map<String, Object> ordermark(String order);

	/**
	 * 我的评价索引
	 */
	public Map<String, Object> mymark(String openId);

	


	

}
