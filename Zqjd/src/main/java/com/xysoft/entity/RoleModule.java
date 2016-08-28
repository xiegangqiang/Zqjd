package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class RoleModule extends BaseEntity{

	private static final long serialVersionUID = 8500507901773803482L;
	
	private String role;
	
	private String module;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
	

}
