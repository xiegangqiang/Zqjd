package com.xysoft.tag.param;

import java.util.Map;

public class DynamicBeanParam {
	
	private String sql;
	private String table;
	private String where;
	private Integer page;
	private Integer count;
	
	public DynamicBeanParam(Map<String, Object> map) {
		
		if(map.containsKey("sql")){
			try {
				sql = map.get("sql").toString();
			} catch (Exception e) {
				sql = "";
			}
		}else sql = "";
		
		if(map.containsKey("table")){
			try {
				table = map.get("table").toString();
			} catch (Exception e) {
				table = "";
			}
		}else table = "";
		
		if(map.containsKey("where")){
			try {
				where = map.get("where").toString();
			} catch (Exception e) {
				where = "";
			}
		}else where = "";
		
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
		
	}
	
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
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
