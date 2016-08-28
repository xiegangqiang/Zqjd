package com.xysoft.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.xysoft.entity.BaseEntity;


@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@Table(name = "imagemes_classify_v")
public class ImageMesClassifyV extends BaseEntity {
	
	private static final long serialVersionUID = -6731524337615585550L;

	private String name; //图文名称
	
	private String smallimg; //图文图标
	
	private String img; //图文图片
	
	private String markcode; //关键字
	
	private String url; //链接地址
	
	private String descript; //图文简介
	
	private String content; //图文内容
	
	private Boolean isImg; //是否显示图片封面
	
	private Boolean visible; //是否显示
	
	private Integer level; //等级排序
	
	private Integer viewcount; //浏览次数
	
	private String classify; //所属分类
	
	private String classifyName; //分类名称
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getMarkcode() {
		return markcode;
	}

	public void setMarkcode(String markcode) {
		this.markcode = markcode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(columnDefinition = "text")
	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	@Column(columnDefinition = "text")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getIsImg() {
		return isImg;
	}

	public void setIsImg(Boolean isImg) {
		this.isImg = isImg;
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

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public Integer getViewcount() {
		return viewcount;
	}

	public void setViewcount(Integer viewcount) {
		this.viewcount = viewcount;
	}

	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

	public String getSmallimg() {
		return smallimg;
	}

	public void setSmallimg(String smallimg) {
		this.smallimg = smallimg;
	}
}
