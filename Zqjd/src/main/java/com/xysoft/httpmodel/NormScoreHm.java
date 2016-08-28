package com.xysoft.httpmodel;

public class NormScoreHm {

	private String id;
	private String name;
	private String departmentName;
	private Double self=0.0;//自评
	private Double total1=0.0;//部门评分1
	private Double total2=0.0;//党组2
	private Double total3=0.0;//加分3
	private Double total4=0.0;//扣分4
	private Double total5=0.0;//一票否决5
	private Double total6=0.0;//民主6
	private Double sum=0.0;//
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Double getSelf() {
		return self;
	}
	public void setSelf(Double self) {
		this.self = self;
	}
	public Double getTotal1() {
		return total1;
	}
	public void setTotal1(Double total1) {
		this.total1 = total1;
	}
	public Double getTotal2() {
		return total2;
	}
	public void setTotal2(Double total2) {
		this.total2 = total2;
	}
	public Double getTotal3() {
		return total3;
	}
	public void setTotal3(Double total3) {
		this.total3 = total3;
	}
	public Double getTotal4() {
		return total4;
	}
	public void setTotal4(Double total4) {
		this.total4 = total4;
	}
	public Double getTotal5() {
		return total5;
	}
	public void setTotal5(Double total5) {
		this.total5 = total5;
	}
	public Double getTotal6() {
		return total6;
	}
	public void setTotal6(Double total6) {
		this.total6 = total6;
	}
	public Double getSum() {
		return sum;
	}
	public void setSum(Double sum) {
		this.sum = sum;
	}
	
	
	
}
