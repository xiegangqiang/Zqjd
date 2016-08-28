package com.xysoft.websocket;

import java.util.Date;
import java.util.Iterator;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.xysoft.util.DateUtil;
import com.xysoft.util.JsonUtil;
@Component
@RequestMapping(value = "/websocket.do")
public class WebsocketEndPoint extends TextWebSocketHandler{

	@Override
	protected void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
		
		TextMessage msg = null;
		System.out.println(session.getId()+"号用户发来信息："+message.getPayload());
		
		JSONObject jsonObject = JSONObject.fromObject(message.getPayload());
		Message resqMsg = (Message) JSONObject.toBean(jsonObject, Message.class);
		
		if( resqMsg.getType().equals( Message.MESSAGE_ONLINE ) ) {
			
			System.out.println(resqMsg.getFromUser() + "上线");
			SocketQueue.put(resqMsg.getFromUser(), session);
			Message respMsg = new Message(Message.MESSAGE_ONLINE, resqMsg.getFromUser(), "ALL", SocketQueue.length().toString() + " 人", DateUtil.toStrYyyyMmDdHhMm(new Date()));
			msg = new TextMessage(JsonUtil.toStringFromObject(respMsg), true);
	        Iterator<String> it = SocketQueue.map.keySet().iterator();
	        while (it.hasNext()) {  
	            SocketQueue.map.get(it.next()).sendMessage(msg);
	        } 
			
		}else if( resqMsg.getType().equals( Message.MESSAGE_OFFLINE ) ) {
			
			System.out.println(resqMsg.getFromUser() + "下线");
			SocketQueue.remove( resqMsg.getFromUser() );
			Message respMsg = new Message(Message.MESSAGE_OFFLINE, resqMsg.getFromUser(), "ALL", SocketQueue.length().toString() + " 人", DateUtil.toStrYyyyMmDdHhMm(new Date()));
			msg = new TextMessage(JsonUtil.toStringFromObject(respMsg), true);
	        Iterator<String> it = SocketQueue.map.keySet().iterator();
	        while (it.hasNext()) {  
	            SocketQueue.map.get(it.next()).sendMessage(msg);
	        } 
			
		}else if( resqMsg.getType().equals( Message.FORCED_OFFLINE ) ) {
			
			System.out.println(resqMsg.getFromUser() + "强制" + resqMsg.getToUser() + "下线");
			Message respMsg = new Message(Message.FORCED_OFFLINE, resqMsg.getFromUser(), resqMsg.getToUser(), "", DateUtil.toStrYyyyMmDdHhMm(new Date()));
			msg = new TextMessage(JsonUtil.toStringFromObject(respMsg), true);
			WebSocketSession user = SocketQueue.get(resqMsg.getToUser());
			if(user != null) {
				user.sendMessage(msg);
				SocketQueue.remove( resqMsg.getToUser() );
			}
			
		}else if( resqMsg.getType().equals( Message.GET_USERS ) ) {
			
			System.out.println(resqMsg.getFromUser() + "来获取在线人员列表");
			Message respMsg = new Message(Message.GET_USERS, resqMsg.getFromUser(), resqMsg.getToUser(), JsonUtil.toString(SocketQueue.getForList()), DateUtil.toStrYyyyMmDdHhMm(new Date()));
			msg = new TextMessage(JsonUtil.toStringFromObject(respMsg), true);
			WebSocketSession user = SocketQueue.get(resqMsg.getToUser());
			if(user != null) {
				user.sendMessage(msg);
			}
			
		}
		//session.sendMessage(msg);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus status) throws Exception {
		System.out.println(session.getId() + "号连接关闭！");
		String key = SocketQueue.getKey(session.getId());
		SocketQueue.remove(key);
		session.close();
	}
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		System.out.println(session.getId()+"号用户进入");
		System.out.println("连接打开！");
		//Map<String, Object>map = session.getAttributes();
		//SecurityContextImpl scl = (SecurityContextImpl) map.get("SPRING_SECURITY_CONTEXT");
		//AdminHm admin = (AdminHm) scl.getAuthentication().getPrincipal();
		//WebSocketSession user = SocketQueue.get(admin.getId());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
