package com.xysoft.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pager<T> {
	private int total;
	private int pageCount;
	private int pageIndex;
	private List<T> datas;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getDatas() {
		return datas;
	}
	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	/**
	 * 返回值.
	 */
	public Map<String, Object> putMapObject() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", this.getTotal());
		map.put("pageCount", this.getPageCount());
		map.put("pageIndex", this.getPageIndex());
		return map;
	}
}
