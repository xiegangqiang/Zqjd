package com.xysoft.tag.param;

import java.util.Map;

public class ProductClassParam {

	private String id;
	private Integer page;
	private Integer count;
	
	public ProductClassParam(Map<String, Object> map){
		
		if (map.containsKey("page")) {
			try {
				page = Integer.valueOf(map.get("page").toString());
			} catch (Exception e) {
				page = 1;
			}
		} else page = 1;
		
		if (map.containsKey("count")) {
			try {
				count = Integer.valueOf(map.get("count").toString());
			} catch (Exception e) {
				count = 1;
			}
		} else count = 1;
		
		if(map.containsKey("id")){
			try {
				id = map.get("id").toString();
			} catch (Exception e) {
				id = "";
			}
		}else id = "";
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	
}
