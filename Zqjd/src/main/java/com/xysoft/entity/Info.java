package com.xysoft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Info extends BaseEntity{

	private static final long serialVersionUID = 2389369955292791103L;
	
	private String name;
	private Integer level;
	private Boolean visible;
	private String markcode;
	private String content;
	private String discript;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public String getMarkcode() {
		return markcode;
	}
	public void setMarkcode(String markcode) {
		this.markcode = markcode;
	}
	@Column(columnDefinition = "text")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(columnDefinition = "text")
	public String getDiscript() {
		return discript;
	}
	public void setDiscript(String discript) {
		this.discript = discript;
	} 
	
	
}
