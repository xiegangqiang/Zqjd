package com.xysoft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Letter extends BaseEntity{

	private static final long serialVersionUID = 1221789477975530036L;
	
	private Integer ranged;//发送范围
	private String content;//内容
	private Integer isSend;//状态
	private Integer visit;//被阅读人数
	private Integer omit;//被删除人数
	
	public Integer getRanged() {
		return ranged;
	}
	public void setRanged(Integer ranged) {
		this.ranged = ranged;
	}
	@Column(columnDefinition = "text")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getIsSend() {
		return isSend;
	}
	public void setIsSend(Integer isSend) {
		this.isSend = isSend;
	}
	public Integer getVisit() {
		return visit;
	}
	public void setVisit(Integer visit) {
		this.visit = visit;
	}
	public Integer getOmit() {
		return omit;
	}
	public void setOmit(Integer omit) {
		this.omit = omit;
	}
	
}
