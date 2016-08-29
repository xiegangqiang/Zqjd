package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class FlowStepRecAdmin extends BaseEntity{

	private static final long serialVersionUID = -1687683998360271898L;
	
	private String flowStepRec;//所属流程明细
	private String admin;//所属岗位
	
	public String getFlowStepRec() {
		return flowStepRec;
	}
	public void setFlowStepRec(String flowStepRec) {
		this.flowStepRec = flowStepRec;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	
	
}
