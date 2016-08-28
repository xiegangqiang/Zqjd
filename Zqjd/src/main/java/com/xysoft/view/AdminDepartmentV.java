package com.xysoft.view;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.xysoft.entity.BaseEntity;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@Table(name = "admin_department_v")
public class AdminDepartmentV extends BaseEntity{

	private static final long serialVersionUID = -5015375920858578883L;

	private Integer userType; // 是否系统自带
	private String username;//用户名
	private String password;//密码
	private String name;// 姓名
	private String email; // E-mail 
	private Boolean isAccountEnabled; // 账号是否启用
	private String loginIp; // 最后登录IP
	private Date loginDate; // 最后登录日期
	private String remark;//备注
	private String department; // 部门Id
	private String departmentParent;//上级部门Id
	private String departmentName;//部门名称
	private Boolean online;//是否在线
	
	
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Boolean getIsAccountEnabled() {
		return isAccountEnabled;
	}
	public void setIsAccountEnabled(Boolean isAccountEnabled) {
		this.isAccountEnabled = isAccountEnabled;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDepartmentParent() {
		return departmentParent;
	}
	public void setDepartmentParent(String departmentParent) {
		this.departmentParent = departmentParent;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	@Transient
	public Boolean getOnline() {
		return online;
	}
	public void setOnline(Boolean online) {
		this.online = online;
	}
	
	
}
