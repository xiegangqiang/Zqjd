package com.xysoft.weixin.service;

import java.util.Map;

public interface WeiXinService {
	
	/**
	 * 处理微信公众号请求返回结果.
	 */
	public String returnRespMessage(String fromUserName, String toUserName, String msgType, Map<String, String> requestMap);
	
}
