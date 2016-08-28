package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class Module extends BaseEntity{

	private static final long serialVersionUID = -5443207876514255830L;

	private String name;
	
	private Boolean isdefault;
	
	private Boolean visible;
	
	private Integer level;
	
	private String img;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Boolean isdefault) {
		this.isdefault = isdefault;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
	
}
