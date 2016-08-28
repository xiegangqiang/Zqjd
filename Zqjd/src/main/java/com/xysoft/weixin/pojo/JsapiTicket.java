package com.xysoft.weixin.pojo;

import java.util.Date;

/**
 * 微信jssdk凭证
 */
public class JsapiTicket {

	// 获取到的凭证  
	private String ticket;
	 // 凭证有效时间，单位：秒  
	private String expiresIn;
    //辅助字段，获取时间根据系统当前时间
    private Date gettime;
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}
	public Date getGettime() {
		return gettime;
	}
	public void setGettime(Date gettime) {
		this.gettime = gettime;
	}
	
	
}
