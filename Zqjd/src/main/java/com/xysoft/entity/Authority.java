package com.xysoft.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Authority extends BaseEntity{

	private static final long serialVersionUID = 6176003838024489040L;
	
	private String name;  // 权限名称
	private String action; // 权限角色
	private Boolean visible; // 是否显示
	private Integer level; // 等级排序
	private Integer leaf; // 叶子节点
	private String url; // 链接地址
	private Integer local; // 所属定位
	private String img;//图标
	private String parentId;
	/**
	 * 辅助字段.
	 */
	private Boolean expanded;
	private Boolean checked;
	private List<Authority> children = new ArrayList<Authority>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
	public Integer getLeaf() {
		return leaf;
	}
	public void setLeaf(Integer leaf) {
		this.leaf = leaf;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getLocal() {
		return local;
	}
	public void setLocal(Integer local) {
		this.local = local;
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
	@Transient
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	@Transient
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	@Transient
	public List<Authority> getChildren() {
		return children;
	}
	public void setChildren(List<Authority> children) {
		this.children = children;
	}
	
	
}
