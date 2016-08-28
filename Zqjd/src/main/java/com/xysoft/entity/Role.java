package com.xysoft.entity;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Role extends BaseEntity {
	
	private static final long serialVersionUID = -6844740487564584949L;

	private String name;
	
	private Boolean visible;
	
	/**
	 * 辅助字段.
	 */
	private Boolean checked;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	@Transient
	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
}
