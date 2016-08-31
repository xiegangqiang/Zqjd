package com.xysoft.entity;

import javax.persistence.Entity;

@Entity
public class WxUserGroup extends BaseEntity{

	private static final long serialVersionUID = -2993609597565671129L;
	
	private String groupId;
	private String name;
	private Integer count;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	

}
