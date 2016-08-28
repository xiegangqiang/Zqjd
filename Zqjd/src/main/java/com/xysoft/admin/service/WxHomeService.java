package com.xysoft.admin.service;

import com.xysoft.entity.Home;

public interface WxHomeService {
	
	/**
	 * 获取首页列表.
	 */
	public String getHome();

	/**
	 * 保存首页信息.
	 */
	public String saveHome(Home param);
	
}
