package com.xysoft.weixin.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import com.xysoft.support.XyApplicationContextUtil;
import com.xysoft.weixin.util.MessageUtil;

/** 
 * 核心服务类 
 */ 
public class CoreService {
//	@Resource
//	public WxDao wxDao;
//	@Resource
//	private static ClassifyDao classifyDao;
	
//	private static void daoDataToReq(String toUserName, String fromUserName) {
//		
//	}
	
	private static WeiXinService weiXinService;
	
	private static WeiXinService getWeiXinService() {
		if (weiXinService == null) {
			weiXinService = (WeiXinService)XyApplicationContextUtil.getBean("weiXinServiceImpl");
		}
		return weiXinService;
	}
	
	/** 
     * 处理微信发来的请求 
     *  
     * @param request 
     * @return 
     */  
    public static String processRequest(HttpServletRequest request) {  
        String respMessage = null;  
        try {  
            // 默认返回的文本消息内容  
//            String respContent = "请求处理异常，请稍候尝试！";  
            // xml请求解析  
            Map<String, String> requestMap = MessageUtil.parseXml(request);  
            // 发送方帐号（open_id）  
            String fromUserName = requestMap.get("FromUserName");  
            // 公众帐号  
            String toUserName = requestMap.get("ToUserName");  
            // 消息类型  
            String msgType = requestMap.get("MsgType"); 
            
            respMessage = getWeiXinService().returnRespMessage(fromUserName, toUserName, msgType, requestMap);
//            WxDao wxDao = (WxDao)XyApplicationContextUtil.getBean("wxDaoImpl");
//            Wx wx = wxDao.getWxByMarkcode(toUserName);
//            if (wx == null) {
//            	TextMessage textMessage = new TextMessage();  
//            	textMessage.setToUserName(fromUserName);  
//            	textMessage.setFromUserName(toUserName);  
//            	textMessage.setCreateTime(new Date().getTime());  
//            	textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);  
//            	textMessage.setFuncFlag(0);  
//            	textMessage.setContent("微信号相关信息设置不正确，请稍后访问！");  
//            	respMessage = MessageUtil.textMessageToXml(textMessage);  
//            }
//            
//            // 文本消息
//            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {  
//            	System.out.println("您发送的是文本消息！");
//            } 
//            // 图片消息 
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {  
//            	System.out.println("您发送的是图片消息！");  
//	        }  
//            // 地理位置消息  
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {  
//            	System.out.println("您发送的是地理位置消息！");  
//            }  
//            // 链接消息  
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {  
//            	System.out.println("您发送的是链接消息！");  
//            }  
//	        // 音频消息  
//	        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {  
//	        	System.out.println("您发送的是音频消息！");  
//	        }  
//	        // 事件推送  
//	        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) { 
//	        	// 事件KEY值，与创建自定义菜单时指定的KEY值对应  
//	        	String eventKey = requestMap.get("EventKey"); 
//	        	System.out.println("自定义菜单点击事件 ");
//	        }
            
            // 回复文本消息  
//            TextMessage textMessage = new TextMessage();  
//            textMessage.setToUserName(fromUserName);  
//            textMessage.setFromUserName(toUserName);  
//            textMessage.setCreateTime(new Date().getTime());  
//            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);  
//            textMessage.setFuncFlag(0);  
            // 文本消息  
//            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {  
//            	String content = requestMap.get("Content");  
//            	// 创建图文消息  
//            	NewsMessage newsMessage = new NewsMessage();  
//            	newsMessage.setToUserName(fromUserName);  
//            	newsMessage.setFromUserName(toUserName);  
//            	newsMessage.setCreateTime(new Date().getTime());  
//            	newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
//            	newsMessage.setFuncFlag(0); 
//
//            	List<Article> articleList = new ArrayList<Article>();
//            	Article article = new Article();  
//            	article.setTitle("微信公众帐号开发教程Java版");  
//            	article.setDescription("柳峰，80后，微信公众帐号开发经验4个月。为帮助初学者入门，特推出此系列教程，也希望借此机会认识更多同行！柳峰，80后，微信公众帐号开发经验4个月。为帮助初学者入门，特推出此系列教程，也希望借此机会认识更多同行！");  
//            	article.setPicUrl("http://www.zqjssj.com/resource/common/image/2014/01/17/9b3cab35395f466d8a86006399bae4a2.jpg");  
//            	article.setUrl("http://blog.csdn.net/lyq8479");  
//            	articleList.add(article);  
//            	// 设置图文消息个数  
//            	newsMessage.setArticleCount(articleList.size());  
//            	//设置图文消息包含的图文集合  
//            	newsMessage.setArticles(articleList);  
//            	// 将图文消息对象转换成xml字符串  
//            	respMessage = MessageUtil.newsMessageToXml(newsMessage);
//            	
//                //respContent = "您发送的是文本消息！";  
//            }  
//            // 图片消息  
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {  
//                respContent = "您发送的是图片消息！";  
//            }  
//            // 地理位置消息  
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {  
//                respContent = "您发送的是地理位置消息！";  
//            }  
//            // 链接消息  
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {  
//                respContent = "您发送的是链接消息！";  
//            }  
//            // 音频消息  
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {  
//                respContent = "您发送的是音频消息！";  
//            }  
//            // 事件推送  
//            else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {  
//                // 事件类型  
//                String eventType = requestMap.get("Event");  
//                // 订阅  
//                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {  
//                    respContent = "谢谢您的关注！";  
//                }  
//                // 取消订阅  
//                else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {  
//                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息  
//                }  
//                // 自定义菜单点击事件  
//                else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {  
//                    // 事件KEY值，与创建自定义菜单时指定的KEY值对应  
//                    String eventKey = requestMap.get("EventKey");  
//  
//                    if (eventKey.equals("11")) {  
//                        respContent = "天气预报菜单项被点击11！";  
//                    } else if (eventKey.equals("12")) {  
//                        respContent = "公交查询菜单项被点击！";  
//                    } else if (eventKey.equals("13")) {  
//                        respContent = "周边搜索菜单项被点击！";  
//                    } else if (eventKey.equals("14")) {  
//                        respContent = "历史上的今天菜单项被点击！";  
//                    } else if (eventKey.equals("21")) {  
//                        respContent = "歌曲点播菜单项被点击！";  
//                    } else if (eventKey.equals("22")) {  
//                        respContent = "经典游戏菜单项被点击！";  
//                    } else if (eventKey.equals("23")) {  
//                        respContent = "美女电台菜单项被点击！";  
//                    } else if (eventKey.equals("24")) {  
//                        respContent = "人脸识别菜单项被点击！";  
//                    } else if (eventKey.equals("25")) {  
//                        respContent = "聊天唠嗑菜单项被点击！";  
//                    } else if (eventKey.equals("31")) {  
//                        respContent = "Q友圈菜单项被点击！";  
//                    } else if (eventKey.equals("32")) {  
//                        respContent = "电影排行榜菜单项被点击！";  
//                    } else if (eventKey.equals("33")) {  
//                        respContent = "幽默笑话菜单项被点击11！";  
//                    }  
//                }  
//            }  
  
//            textMessage.setContent(respContent);  
//            respMessage = MessageUtil.textMessageToXml(textMessage);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return respMessage;  
    }  
}
