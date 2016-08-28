package com.xysoft.entity;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class LetterAdmin extends BaseEntity{

	private static final long serialVersionUID = -5026545580363727287L;
	
	private String letter;//所属站内信
	private String admin;//发送的会员
	private Boolean state;//会员阅读状态
	private Letter mesasge;
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}
	@Transient
	public Letter getMesasge() {
		return mesasge;
	}
	public void setMesasge(Letter mesasge) {
		this.mesasge = mesasge;
	}
	
	
	

}
