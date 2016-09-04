package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class Evaluat extends BaseEntity{

	private static final long serialVersionUID = -249139995854023559L;

	private String orders;//订单
	private String user;//评价人
	private String flowStepRecPost;//评价岗位
	private Integer type;//类型：0是分值， 1是图片，2是描述
    private String value;//值
    
	public String getOrders() {
		return orders;
	}
	public void setOrders(String orders) {
		this.orders = orders;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getFlowStepRecPost() {
		return flowStepRecPost;
	}
	public void setFlowStepRecPost(String flowStepRecPost) {
		this.flowStepRecPost = flowStepRecPost;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
    
}
