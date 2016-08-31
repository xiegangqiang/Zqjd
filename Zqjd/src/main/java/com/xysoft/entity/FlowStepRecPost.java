package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class FlowStepRecPost extends BaseEntity{

	private static final long serialVersionUID = -1687683998360271898L;
	
	private String flowStepRec;//所属流程明细
	private String posts;//所属岗位
	
	public String getFlowStepRec() {
		return flowStepRec;
	}
	public void setFlowStepRec(String flowStepRec) {
		this.flowStepRec = flowStepRec;
	}
	public String getPosts() {
		return posts;
	}
	public void setPosts(String posts) {
		this.posts = posts;
	}
	
	
	
}
