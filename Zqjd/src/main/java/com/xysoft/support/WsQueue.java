package com.xysoft.support;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class WsQueue {
	
	private List<Object> ll = Collections.synchronizedList(new LinkedList<Object>());
			
//	private LinkedList<Object> ll = new LinkedList<Object>();
	
	public synchronized void put(Object o) {
		ll.add(o);
	}
	
	public synchronized Object get() {
		Object o = ll.get(0);
		ll.remove(o);
		return o;
	}
	
	public boolean isEmpty() {
		return ll.isEmpty();
	}
	
	public Integer length() {
		return ll.size();
	}
}
