package com.xysoft.weixin.template;

public class OrderData {
	
	private First first;
	private Keyword orderno;
	private Keyword refundno;
	private Keyword refundproduct;
	private Remark remark;
	
	public OrderData() {
		
	}
	
	public OrderData(First first, Keyword orderno, Keyword refundno,
			Keyword refundproduct, Remark remark) {
		super();
		this.first = first;
		this.orderno = orderno;
		this.refundno = refundno;
		this.refundproduct = refundproduct;
		this.remark = remark;
	}
	
	public First getFirst() {
		return first;
	}
	public void setFirst(First first) {
		this.first = first;
	}
	public Keyword getOrderno() {
		return orderno;
	}
	public void setOrderno(Keyword orderno) {
		this.orderno = orderno;
	}
	public Keyword getRefundno() {
		return refundno;
	}
	public void setRefundno(Keyword refundno) {
		this.refundno = refundno;
	}
	public Keyword getRefundproduct() {
		return refundproduct;
	}
	public void setRefundproduct(Keyword refundproduct) {
		this.refundproduct = refundproduct;
	}
	public Remark getRemark() {
		return remark;
	}
	public void setRemark(Remark remark) {
		this.remark = remark;
	}
	
	

}
