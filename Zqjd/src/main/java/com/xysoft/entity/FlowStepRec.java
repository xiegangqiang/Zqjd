package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class FlowStepRec extends BaseEntity{

	private static final long serialVersionUID = 2764937881458388040L;

	private String orders;//订单
	private String flowstep;//流程
	private int state;//状态
	private String flowStepRecAdmin;//负责人
	private String admin;//操作人
	
	public String getOrders() {
		return orders;
	}
	public void setOrders(String orders) {
		this.orders = orders;
	}
	public String getFlowstep() {
		return flowstep;
	}
	public void setFlowstep(String flowstep) {
		this.flowstep = flowstep;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getFlowStepRecAdmin() {
		return flowStepRecAdmin;
	}
	public void setFlowStepRecAdmin(String flowStepRecAdmin) {
		this.flowStepRecAdmin = flowStepRecAdmin;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
	
}
