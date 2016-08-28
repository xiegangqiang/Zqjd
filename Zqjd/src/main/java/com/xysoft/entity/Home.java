package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class Home extends BaseEntity {
	
	private static final long serialVersionUID = -9125191187592977342L;

	private String name; //首页名称
	
	private String descript; //首页简介
	
	private String img; //首页图片
	
	private String siteimg; //网站图片
	
	private String markcode; //关键字
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getSiteimg() {
		return siteimg;
	}

	public void setSiteimg(String siteimg) {
		this.siteimg = siteimg;
	}

	public String getMarkcode() {
		return markcode;
	}

	public void setMarkcode(String markcode) {
		this.markcode = markcode;
	}
}
