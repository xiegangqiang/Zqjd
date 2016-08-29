package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class UserAdmin extends BaseEntity{

	private static final long serialVersionUID = -142977824956777691L;
	
	private String user;//所属客户
	private String admin;//所属销售员
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}

	
}
