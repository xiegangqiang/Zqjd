package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class UserTypeAuthority extends BaseEntity{

	private static final long serialVersionUID = 3426768688151492765L;
	
	private Integer userType;
	private String authority;
	
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
}
