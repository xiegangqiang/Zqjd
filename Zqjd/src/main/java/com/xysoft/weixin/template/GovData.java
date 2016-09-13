package com.xysoft.weixin.template;

public class GovData {

	private First first;
	private Keyword keyword1;
	private Keyword keyword2;
	private Keyword keyword3;
	private Keyword keyword4;
	private Keyword keyword5;
	private Remark remark;
	
	public GovData() {
		
	}
	
	public GovData(First first, Keyword keyword1, Keyword keyword2,
			Keyword keyword3, Keyword keyword4, Keyword keyword5, Remark remark) {
		super();
		this.first = first;
		this.keyword1 = keyword1;
		this.keyword2 = keyword2;
		this.keyword3 = keyword3;
		this.keyword4 = keyword4;
		this.keyword5 = keyword5;
		this.remark = remark;
	}

	public First getFirst() {
		return first;
	}
	public void setFirst(First first) {
		this.first = first;
	}
	public Keyword getKeyword1() {
		return keyword1;
	}
	public void setKeyword1(Keyword keyword1) {
		this.keyword1 = keyword1;
	}
	public Keyword getKeyword2() {
		return keyword2;
	}
	public void setKeyword2(Keyword keyword2) {
		this.keyword2 = keyword2;
	}
	public Keyword getKeyword3() {
		return keyword3;
	}
	public void setKeyword3(Keyword keyword3) {
		this.keyword3 = keyword3;
	}
	public Keyword getKeyword4() {
		return keyword4;
	}
	public void setKeyword4(Keyword keyword4) {
		this.keyword4 = keyword4;
	}
	public Keyword getKeyword5() {
		return keyword5;
	}
	public void setKeyword5(Keyword keyword5) {
		this.keyword5 = keyword5;
	}
	public Remark getRemark() {
		return remark;
	}
	public void setRemark(Remark remark) {
		this.remark = remark;
	}
	
	
	
}
