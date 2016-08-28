package com.xysoft.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Department extends BaseEntity{

	private static final long serialVersionUID = 787046797398647655L;

	private String name;//名称
	private String address;//地址
	private String parentId;//下级
	private Boolean visible;//是否可见
	private Integer level;//排序等级
	private String phone;//电话
	private String fax;//传真
	private String shortnum;//简码
	private String remark;//备注
	private Boolean expanded; //辅助字段，是否展开
	private List<Admin> admins = new ArrayList<Admin>();;//辅助字段，总人数
	private List<Department> children = new ArrayList<Department>();//辅助字段，数据库不生成字段
	private Boolean checked = null;//辅助字段
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getPhone() {
		return phone;
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
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getShortnum() {
		return shortnum;
	}
	public void setShortnum(String shortnum) {
		this.shortnum = shortnum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Transient
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	@Transient
	public List<Admin> getAdmins() {
		return admins;
	}
	public void setAdmins(List<Admin> admins) {
		this.admins = admins;
	}
	@Transient
	public List<Department> getChildren() {
		return children;
	}
	public void setChildren(List<Department> children) {
		this.children = children;
	}
	@Transient
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	
	
}
