package com.xysoft.weixin.template;

public class GovTemplate {

	private String touser;
	private String template_id;
	private String url;
	private GovData data;
	
	public GovTemplate() {
		
	}
	
	public GovTemplate(String touser, String template_id, String url,
			GovData data) {
		super();
		this.touser = touser;
		this.template_id = template_id;
		this.url = url;
		this.data = data;
	}

	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public GovData getData() {
		return data;
	}
	public void setData(GovData data) {
		this.data = data;
	}
	
	
}
