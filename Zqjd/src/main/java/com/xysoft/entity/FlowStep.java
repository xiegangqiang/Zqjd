package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class FlowStep extends BaseEntity{

	private static final long serialVersionUID = 9213885471856550282L;
	
	private String name;//流程名称
	private String department;//负责部门
	private String laststep;//上一流程
	private String nextstep;//下一流程
	
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
	public String getLaststep() {
		return laststep;
	}
	public void setLaststep(String laststep) {
		this.laststep = laststep;
	}
	public String getNextstep() {
		return nextstep;
	}
	public void setNextstep(String nextstep) {
		this.nextstep = nextstep;
	}
	
}
