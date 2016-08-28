package com.xysoft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class TextMes extends BaseEntity {
	
	private static final long serialVersionUID = -4445831844648360041L;

	private String name; //文本名称
	
	private String markcode; //关键字
	
	private String content; //文本内容
	
	private Boolean visible; //是否显示
	
	private Integer viewcount; //浏览次数
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	
	public Integer getViewcount() {
		return viewcount;
	}

	public void setViewcount(Integer viewcount) {
		this.viewcount = viewcount;
	}
}
