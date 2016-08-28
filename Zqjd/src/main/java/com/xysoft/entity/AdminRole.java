package com.xysoft.entity;

import javax.persistence.Entity;


@Entity
public class AdminRole extends BaseEntity {

	private static final long serialVersionUID = 6751155787061072693L;

	private String role;
	
	private String admin;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

}
