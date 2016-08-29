package com.xysoft.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class ProductClass extends BaseEntity{

	private static final long serialVersionUID = -6111318461694363150L;

	private String name;
	private Boolean visible;
	private Integer level;
	private String img;
	private String parentId;
	private String markcode;
	private String descript;
	
	/**辅助字段**/
	private List<ProductClass> children = new ArrayList<ProductClass>();
	private Boolean expanded; //是否展开
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
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getMarkcode() {
		return markcode;
	}
	public void setMarkcode(String markcode) {
		this.markcode = markcode;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	@Transient
	public List<ProductClass> getChildren() {
		return children;
	}
	public void setChildren(List<ProductClass> children) {
		this.children = children;
	}
	@Transient
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	
	
}
