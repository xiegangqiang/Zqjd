package com.xysoft.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

public class SocketQueue {
	
	public static Object lock = new Object();

	public static Map<String, WebSocketSession> map = new HashMap<String, WebSocketSession>();
	
	public static void put(String key, WebSocketSession session) {
		synchronized(lock) {
			map.put(key, session);
		}
	}
	
	public static void remove(String key) {
		synchronized(lock) {
			map.remove(key);
		}
	}
	
	public static WebSocketSession get(String key) {
		synchronized(lock) {
			return map.get(key);
		}
	}
	
	public static List<String> getForList() {
		List<String> list = new ArrayList<String>();
		synchronized(lock) {
	        Iterator<String> it = map.keySet().iterator();
	        while (it.hasNext()) {  
	        	list.add(it.next());
	        }
		}
		return list;
	}
	
	public static String  getKey(String sessionId) {
		String key = null;
		synchronized(lock) {
	        Iterator<String> it = map.keySet().iterator();
	        while (it.hasNext()) {  
	           String k = it.next();
	           if( map.get(k).getId().equals(sessionId)) 
	        	   key = k;
	        } 
		}
		return key;
	}
	
	public static boolean isEmpty() {
		return map.isEmpty();
	}
	
	public static Integer length() {
		return map.size();
	}
}
