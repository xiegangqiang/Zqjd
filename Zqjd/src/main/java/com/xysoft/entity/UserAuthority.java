package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class UserAuthority extends BaseEntity{

	private static final long serialVersionUID = 430254902309575593L;
	
	private String user;
	private String authority;
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	
}
