package com.xysoft.tag.param;

import java.util.Map;

public class MarkProgressTagParam {

	private String subjectiveId;
	
	public MarkProgressTagParam(Map<String, Object> map) {
		if (map.containsKey("subjectiveId")) {
			try {
				subjectiveId = map.get("subjectiveId").toString();
			} catch (Exception e) {
				subjectiveId = "";
			}
		} else subjectiveId = "";
	}

	public String getSubjectiveId() {
		return subjectiveId;
	}

	public void setSubjectiveId(String subjectiveId) {
		this.subjectiveId = subjectiveId;
	}

	
	
}
