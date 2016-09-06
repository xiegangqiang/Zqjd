package com.xysoft.tag.param;

import java.util.Map;

public class InfoParam {

	private String id;
	private String markcode;
	
	public InfoParam(Map<String, Object> map) {
		if(map.containsKey("id")){
			try {
				id = map.get("id").toString();
			} catch (Exception e) {
				id = "";
			}
		}else id = "";
		
		if(map.containsKey("markcode")){
			try {
				markcode = map.get("markcode").toString();
			} catch (Exception e) {
				markcode = "";
			}
		}else markcode = "";
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMarkcode() {
		return markcode;
	}

	public void setMarkcode(String markcode) {
		this.markcode = markcode;
	}

	
}
