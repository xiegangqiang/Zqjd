package com.xysoft.httpmodel;

import java.util.LinkedList;
import java.util.List;


public class NormScoreDetailHm {

	private String assessors;
	private String assessment;
	private String normId;
	private String name;
	private String department;
	private Float total;
	private List<String> title = new LinkedList<String>();
	private List<Float> score = new LinkedList<Float>();
	
	
	public String getAssessors() {
		return assessors;
	}
	public void setAssessors(String assessors) {
		this.assessors = assessors;
	}
	public String getAssessment() {
		return assessment;
	}
	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}
	public String getNormId() {
		return normId;
	}
	public void setNormId(String normId) {
		this.normId = normId;
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
	public Float getTotal() {
		return total;
	}
	public void setTotal(Float total) {
		this.total = total;
	}
	public List<String> getTitle() {
		return title;
	}
	public void setTitle(List<String> title) {
		this.title = title;
	}
	public List<Float> getScore() {
		return score;
	}
	public void setScore(List<Float> score) {
		this.score = score;
	}
	
	
}	
