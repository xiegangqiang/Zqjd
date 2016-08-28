package com.xysoft.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Admin extends BaseEntity implements UserDetails {

	private static final long serialVersionUID = -4090218505220603110L;
	
	private String username; // 用户名
	private String password; // 密码
	private String email; // E-mail
	private String name; // 姓名
	private String department; // 部门
	private String phone;//电话
	private String IDCard;//身份证号码
	private Boolean isAccountEnabled; // 账号是否启用
	private Boolean isAccountLocked; // 账号是否锁定
	private Boolean isAccountExpired; // 账号是否过期
	private Boolean isCredentialsExpired; // 凭证是否过期
	private Integer loginFailureCount; // 连续登录失败的次数
	private Date lockedDate; // 账号锁定日期
	private Date loginDate; // 最后登录日期
	private String loginIp; // 最后登录IP
	private Integer userType; // 是否系统自带
	private String remark;
	
	/**
	 * 辅助字段.
	 */
	private Set<GrantedAuthority> authorities;// 权限信息
	
	@Column(nullable = false, unique = true)
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
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getIDCard() {
		return IDCard;
	}

	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}

	public Boolean getIsAccountEnabled() {
		return isAccountEnabled;
	}

	public void setIsAccountEnabled(Boolean isAccountEnabled) {
		this.isAccountEnabled = isAccountEnabled;
	}
	
	public Boolean getIsAccountLocked() {
		return isAccountLocked;
	}

	public void setIsAccountLocked(Boolean isAccountLocked) {
		this.isAccountLocked = isAccountLocked;
	}
	
	public Boolean getIsAccountExpired() {
		return isAccountExpired;
	}

	public void setIsAccountExpired(Boolean isAccountExpired) {
		this.isAccountExpired = isAccountExpired;
	}
	
	public Boolean getIsCredentialsExpired() {
		return isCredentialsExpired;
	}

	public void setIsCredentialsExpired(Boolean isCredentialsExpired) {
		this.isCredentialsExpired = isCredentialsExpired;
	}
	
	@Column(nullable = false)
	public Integer getLoginFailureCount() {
		return loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}
	
	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}
	
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	@Transient
	public Set<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Transient
	public boolean isEnabled() {
		return this.isAccountEnabled;
	}

	@Transient
	public boolean isAccountNonLocked() {
		return !this.isAccountLocked;
	}

	@Transient
	public boolean isAccountNonExpired() {
		return !this.isAccountExpired;
	}

	@Transient
	public boolean isCredentialsNonExpired() {
		return !this.isCredentialsExpired;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


}
