package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class Config extends BaseEntity{

	private static final long serialVersionUID = 3296679005681109512L;
	
	private String name; //名称
	private String markcode; //唯一标识
	private String markvalue; //值
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
	public String getMarkvalue() {
		return markvalue;
	}
	public void setMarkvalue(String markvalue) {
		this.markvalue = markvalue;
	}

	
	
}
