package com.xysoft.support;

import java.util.List;

public class DataPager<T> {
	private List<T> rows;
	private int total;
	private int allpage;
	
	public DataPager(List<T> rows, int total) {
		this.rows = rows;
		this.total = total;
	}
	
	public DataPager(List<T> rows, int total, int count) {
		this.rows = rows;
		this.total = total;
		if (total % count == 0) {
			allpage = total / count;
		} else {
			allpage = total / count + 1;
		}
	}
	
	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}

	public int getAllpage() {
		return allpage;
	}

	public void setAllpage(int allpage) {
		this.allpage = allpage;
	}
}
