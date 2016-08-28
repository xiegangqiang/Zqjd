package com.xysoft.weixin.util;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xysoft.weixin.pojo.AccessToken;
import com.xysoft.weixin.pojo.JsapiTicket;

public class WeixinCacheUtil {
	//缓存获得普通形式token
	private static Map<String, AccessToken> accesstokenmap = new ConcurrentHashMap<String, AccessToken>();
	//缓存获得jsapi_ticket
	private static Map<String, JsapiTicket> jsapiticketmap = new ConcurrentHashMap<String, JsapiTicket>(); 
	
	
    public static AccessToken getAccessToken(String wxid, String appid, String appsecret) {  
    	 AccessToken accessToken = accesstokenmap.get(wxid);
    	 if(accessToken == null) {
    		 accessToken = WeixinUtil.getAccessToken(appid, appsecret);
    		 putAccessToken(wxid, accessToken);
    	 }else {
    		 long time = (new Date().getTime() - accessToken.getGettime().getTime())/1000;
    		 if(time>7190) {
    			 accessToken = WeixinUtil.getAccessToken(appid, appsecret);
        		 putAccessToken(wxid, accessToken);
    		 }
    	 }
        return accessToken;  
    }
    
    public static JsapiTicket getJsapiTicket(String wxid, String accesstoken) {
    	JsapiTicket jsapiTicket = jsapiticketmap.get(wxid);
    	if(jsapiTicket == null) {
    		jsapiTicket = WeixinUtil.getJsapiTicket(accesstoken);
    		putJsapiTicket(wxid, jsapiTicket);
    	}else {
    		long time = (new Date().getTime() - jsapiTicket.getGettime().getTime())/1000;
    		if(time>7190) {
    			jsapiTicket = WeixinUtil.getJsapiTicket(accesstoken);
        		putJsapiTicket(wxid, jsapiTicket);
    		}
    	}
    	return jsapiTicket;
    }
    
    public static void putAccessToken(String wxid, AccessToken accessToken) {
    	accesstokenmap.put(wxid, accessToken);
    }
    
    public static void putJsapiTicket(String wxid, JsapiTicket jsapiTicket) {
    	jsapiticketmap.put(wxid, jsapiTicket);
    }
    
}
