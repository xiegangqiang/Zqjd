package com.xysoft.entity;

import javax.persistence.Entity;


@Entity
public class RoleMenu extends BaseEntity {

	private static final long serialVersionUID = -3338907955312139971L;
	
	private String role;
	
	private String menu;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

}
