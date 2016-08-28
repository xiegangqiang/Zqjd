package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class Attention extends BaseEntity {
	
	private static final long serialVersionUID = 6469799377442988749L;

	private String content; //回复内容
	
	private Boolean ismarkcode; //是否使用关键字
	
	private String markcode; //关键字
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getIsmarkcode() {
		return ismarkcode;
	}

	public void setIsmarkcode(Boolean ismarkcode) {
		this.ismarkcode = ismarkcode;
	}

	public String getMarkcode() {
		return markcode;
	}

	public void setMarkcode(String markcode) {
		this.markcode = markcode;
	}

}
