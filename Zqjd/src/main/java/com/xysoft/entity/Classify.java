package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class Classify extends BaseEntity {
	
	private static final long serialVersionUID = -837420133598457403L;
	
	private String name; //分类名称
	
	private String descript; //分类描述
	
	private Integer level; //等级排序
	
	private String img; //分类图片
	
	private String smallimg; //分类图标
	
	private Boolean visible; //是否显示
	
	private String url; //链接地址
	
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
	
	public String getSmallimg() {
		return smallimg;
	}

	public void setSmallimg(String smallimg) {
		this.smallimg = smallimg;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMarkcode() {
		return markcode;
	}

	public void setMarkcode(String markcode) {
		this.markcode = markcode;
	}
	
}
