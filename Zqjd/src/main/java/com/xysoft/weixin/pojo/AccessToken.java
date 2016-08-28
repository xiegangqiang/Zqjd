package com.xysoft.weixin.pojo;

import java.util.Date;

/** 
 * 微信通用接口凭证 
 */  
public class AccessToken {
	// 获取到的凭证  
    private String token;  
    // 凭证有效时间，单位：秒  
    private int expiresIn;  
    //用户刷新token
    private String refresh;
    //用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
    private String openid;
    //用户授权的作用域，使用逗号（,）分隔
    private String scope;
    //只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
    private String unionid;
    //辅助字段，获取时间根据系统当前时间
    private Date gettime;
  
    public String getToken() {  
        return token;  
    }  
  
    public void setToken(String token) {  
        this.token = token;  
    }  
  
    public int getExpiresIn() {  
        return expiresIn;  
    }  
  
    public void setExpiresIn(int expiresIn) {  
        this.expiresIn = expiresIn;  
    }

	public String getRefresh() {
		return refresh;
	}

	public void setRefresh(String refresh) {
		this.refresh = refresh;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public Date getGettime() {
		return gettime;
	}

	public void setGettime(Date gettime) {
		this.gettime = gettime;
	}  
    
    
}
