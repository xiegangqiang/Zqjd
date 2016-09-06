package com.xysoft.tag.param;

import java.util.Map;

public class ProductParam {

	private String id;
	private String productClass;
	private String name;
	private Integer page;
	private Integer count;
	
	public ProductParam(Map<String, Object> map){
		
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
		
		if(map.containsKey("productClass")){
			try {
				productClass = map.get("productClass").toString();
			} catch (Exception e) {
				productClass = "";
			}
		}else productClass = "";
	
		if(map.containsKey("name")){
			try {
				name = map.get("name").toString();
			} catch (Exception e) {
				name = "";
			}
		}else name = "";
		
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

	public String getProductClass() {
		return productClass;
	}

	public void setProductClass(String productClass) {
		this.productClass = productClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
