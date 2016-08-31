package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class GiftCode extends BaseEntity{

	private static final long serialVersionUID = 4099486991949647861L;
	
	private String code;//礼品码
	private String phone;//电话号码
	private Boolean isGrant;//是否发放
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Boolean getIsGrant() {
		return isGrant;
	}
	public void setIsGrant(Boolean isGrant) {
		this.isGrant = isGrant;
	}

	
}
