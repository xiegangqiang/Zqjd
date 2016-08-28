package com.xysoft.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Diymen extends BaseEntity {

	private static final long serialVersionUID = 1417977494945472808L;
	
	/**映射字段**/
	private String name; //名称
	private Boolean visible; //是否显示
	private Integer level; //等级排序
	private String parentId; //父节点
	private String url; //链接地址
	private String markcode; //关键字
	
	/**辅助字段**/
	private List<Diymen> children = new ArrayList<Diymen>();
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Column(columnDefinition = "text")
	public String getUrl() {
		return url;
	}
	
	@Column(columnDefinition = "text")
	public void setUrl(String url) {
		this.url = url;
	}

	public String getMarkcode() {
		return markcode;
	}

	public void setMarkcode(String markcode) {
		this.markcode = markcode;
	}
	
	@Transient
	public List<Diymen> getChildren() {
		return children;
	}

	public void setChildren(List<Diymen> children) {
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
