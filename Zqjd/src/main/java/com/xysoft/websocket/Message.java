package com.xysoft.websocket;

public class Message {
	
	public static final Integer MESSAGE_ONLINE = 0;//上线类型
	public static final Integer MESSAGE_OFFLINE = 1;//下线类型
	public static final Integer FORCED_OFFLINE = 2;//强制下线
	public static final Integer GET_USERS = 3;//获得在线所有人
	
	private Integer type;//消息类型
	private String fromUser;//发送消息人
	private String toUser;//接收消息人
	private String content;//消息内容
    private String date;//发送时间
    
    public Message() {
		
	}
    
	public Message(Integer type, String fromUser, String toUser,
			String content, String date) {
		super();
		this.type = type;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.content = content;
		this.date = date;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
    
    
}
