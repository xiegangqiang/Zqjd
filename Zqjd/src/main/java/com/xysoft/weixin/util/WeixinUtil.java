package com.xysoft.weixin.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xysoft.entity.WxUserGroup;
import com.xysoft.weixin.pojo.AccessToken;
import com.xysoft.weixin.pojo.JsapiTicket;
import com.xysoft.weixin.pojo.Menu;
import com.xysoft.weixin.template.GovTemplate;
import com.xysoft.entity.WxUser;

/** 
 * 公众平台通用接口工具类 
 */ 
@SuppressWarnings({"static-access", "unchecked"})
public class WeixinUtil {
	private static Logger log = LoggerFactory.getLogger(WeixinUtil.class);  
	  
    /** 
     * 发起https请求并获取结果 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
     */  
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {  
        JSONObject jsonObject = null;  
        StringBuffer buffer = new StringBuffer();  
        try {  
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化  
            TrustManager[] tm = { new MyX509TrustManager() };  
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
            sslContext.init(null, tm, new java.security.SecureRandom());  
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();  
  
            URL url = new URL(requestUrl);  
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();  
            httpUrlConn.setSSLSocketFactory(ssf);  
  
            httpUrlConn.setDoOutput(true);  
            httpUrlConn.setDoInput(true);  
            httpUrlConn.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            httpUrlConn.setRequestMethod(requestMethod);  
  
            if ("GET".equalsIgnoreCase(requestMethod))  
                httpUrlConn.connect();  
  
            // 当有数据需要提交时  
            if (null != outputStr) {  
                OutputStream outputStream = httpUrlConn.getOutputStream();  
                // 注意编码格式，防止中文乱码  
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
  
            // 将返回的输入流转换成字符串  
            InputStream inputStream = httpUrlConn.getInputStream();  
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);  
            }  
            bufferedReader.close();  
            inputStreamReader.close();  
            // 释放资源  
            inputStream.close();  
            inputStream = null;  
            httpUrlConn.disconnect();  
            jsonObject = JSONObject.fromObject(buffer.toString());  
        } catch (ConnectException ce) {  
            log.error("Weixin server connection timed out.");  
        } catch (Exception e) {  
            log.error("https request error:{}", e);  
        }  
        return jsonObject;  
    } 
    
    
 // 获取access_token的接口地址（GET） 限200（次/天）  
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";  
      
    /** 
     * 获取access_token 
     *  
     * @param appid 凭证 
     * @param appsecret 密钥 
     * @return 
     */  
    public static AccessToken getAccessToken(String appid, String appsecret) {  
        AccessToken accessToken = null;  
      
        String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret);  
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
        // 如果请求成功  
        if (null != jsonObject) {  
            try {  
                accessToken = new AccessToken();  
                accessToken.setToken(jsonObject.getString("access_token"));  
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));  
                accessToken.setGettime(new Date());
            } catch (JSONException e) {  
                accessToken = null;  
                // 获取token失败  
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }  
        return accessToken;  
    }
    
    //引导关注者同意授权后，获取code，返回回调地址，用于获取网页授权access_token
    public final static String oauth2Code_url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    
    public static String getOauth2Code(String appid, String redirectUri, Integer scope) {
    	String scope_type = "snsapi_userinfo";
    	if(scope == 0)  scope_type = "snsapi_base";
    	if(scope == 1)  scope_type = "snsapi_userinfo";
    	String requestUrl = oauth2Code_url.replace("APPID", appid).replace("REDIRECT_URI", redirectUri).replace("SCOPE", scope_type);
    	return requestUrl;
    }
    
    //获取网页授权access_token，与上面的方法获取的token不一样
    public final static String oauth2access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";
    
    public static AccessToken getOauth2AccessToken(String appid, String appsecret, String code) {  
        AccessToken accessToken = null;  
      
        String requestUrl = oauth2access_token_url.replace("APPID", appid).replace("APPSECRET", appsecret).replace("CODE", code);  
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
        // 如果请求成功  
        if (null != jsonObject) {  
            try {  
                accessToken = new AccessToken();  
                accessToken.setToken(jsonObject.getString("access_token"));  
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));  
                accessToken.setRefresh(jsonObject.getString("refresh_token"));
                accessToken.setOpenid(jsonObject.getString("openid"));
                accessToken.setScope(jsonObject.getString("scope"));
                accessToken.setGettime(new Date());
            } catch (JSONException e) {  
                accessToken = null;  
                // 获取token失败  
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }  
        return accessToken;  
    }
    
    //采用http GET方式请求获得jsapi_ticket，jssdk使用
    public final static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    
    public static JsapiTicket getJsapiTicket(String accesstoken){
    	JsapiTicket jsapiTicket = null;
    	 String requestUrl = jsapi_ticket_url.replace("ACCESS_TOKEN", accesstoken);
    	 JSONObject jsonObject = httpRequest(requestUrl, "GET", null);  
    	 if(null != jsonObject) {
    		 try {
    			 jsapiTicket = new JsapiTicket();
    			 jsapiTicket.setTicket(jsonObject.getString("ticket"));
    			 jsapiTicket.setExpiresIn(jsonObject.getString("expires_in"));
    			 jsapiTicket.setGettime(new Date());
			} catch (JSONException e) {
				jsapiTicket = null;
				// 获取token失败  
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
			}
    	 }
		return jsapiTicket;
    }
    
 // 菜单创建（POST） 限100（次/天）  
    public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";  
      
    /** 
     * 创建菜单 
     *  
     * @param menu 菜单实例 
     * @param accessToken 有效的access_token 
     * @return 0表示成功，其他值表示失败 
     */  
    public static int createMenu(Menu menu, String accessToken) {  
        int result = 0;  
        // 拼装创建菜单的url  
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);  
        // 将菜单对象转换成json字符串  
        String jsonMenu = JSONObject.fromObject(menu).toString();  
        // 调用接口创建菜单  
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);
        if (null != jsonObject) {  
            if (0 != jsonObject.getInt("errcode")) {  
                result = jsonObject.getInt("errcode");  
                log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }  
      
        return result;  
    }  
    
    
    // 获取openid（GET）
    public static String getUserOpenids_url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";  
      
    public static List<String> getUserOpenids(String accessToken, String next_openid) {  
        // 拼装url  
        String url = getUserOpenids_url.replace("ACCESS_TOKEN", accessToken);  

        List<String> openids = new ArrayList<String>();
        // 调用
        JSONObject jsonObject = httpRequest(url, "GET", next_openid);
        JSONObject openid = (JSONObject) jsonObject.get("data");
        if(openid==null) return null;
        String openidS = openid.getString("openid");
        String  open = openidS.replace("[", "").replace("]", "");
        if("".equals(open)) return openids;
       String [] o = open.split(",");
       for (int i = 0; i < o.length; i++) {
    	   openids.add(o[i].substring(1, o[i].length()-1));
       }
        return openids;  
    }  
    
    
    //获取微信用户基本信息（GET）
    public static String getWxUser_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    
	public static WxUser getWxUser(String accessToken, String openid){
    	// 拼装url  
        String url = getWxUser_url.replace("ACCESS_TOKEN", accessToken).replaceAll("OPENID", openid);  
        // 调用
        JSONObject jsonObject = httpRequest(url, "GET", null);
        WxUser wxuser = (WxUser) jsonObject.toBean(jsonObject, WxUser.class);
    	return wxuser;
    }
    
    //获取微信用户基本信息（GET）网页授权形式
    public static String getOauth2WxUser_url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    
	public static WxUser getOauth2WxUser(String accessToken, String openid){
    	// 拼装url  
        String url = getOauth2WxUser_url.replace("ACCESS_TOKEN", accessToken).replaceAll("OPENID", openid);  
        // 调用
        JSONObject jsonObject = httpRequest(url, "GET", null);
        WxUser wxuser = (WxUser) jsonObject.toBean(jsonObject, WxUser.class);
    	return wxuser;
    }
	
	//修改微信用户备注名称
	public static String WxUserRemark_update_url = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=ACCESS_TOKEN";
	
	public static int updateWxUserRemark(String openid, String remark, String accessToken) {
		int result = 0;
    	//拼装url
    	String url = WxUserRemark_update_url.replace("ACCESS_TOKEN", accessToken);
    	//拼接提交需要字符串
    	String jsonGroup = "{\"openid\":\""+ openid +"\", \"remark\":\""+ remark +"\"}";
    	// 调用
    	JSONObject jsonObject = httpRequest(url, "POST", jsonGroup);
        if (null != jsonObject) {  
            if (0 != jsonObject.getInt("errcode")) {  
                result = jsonObject.getInt("errcode");  
                log.error("修改备注名称失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }
     	return result;
	}
    
    //获取所有微信用户分组信息
    public static String getWxUserGroups_url = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token=ACCESS_TOKEN";
    
	public static List<WxUserGroup> getWxUserGroups(String accessToken) {
    	// 拼装url  
        String url = getWxUserGroups_url.replace("ACCESS_TOKEN", accessToken);
       // 调用
        JSONObject jsonObject = httpRequest(url, "GET", null);
        JSONArray jsonArray =  jsonObject.getJSONArray("groups");
        List<WxUserGroup> list = (List<WxUserGroup>) JSONArray.toCollection(jsonArray, WxUserGroup.class);
        for (WxUserGroup group : list) {
        	group.setGroupId(group.getId());
        	group.setId(null);
		}
		return list;
    }
	
	//创建微信用户分组
	public static String WxUserGroup_create_url = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token=ACCESS_TOKEN";
    
    public static WxUserGroup createWxUserGroup(String name, String accessToken) {
    	WxUserGroup group = null;
    	// 拼装url
    	String url = WxUserGroup_create_url.replace("ACCESS_TOKEN", accessToken);
    	//拼接提交分组需要字符串
    	String jsonGroup = "{\"group\":{\"name\":\"" + name + "\"}}";
    	// 调用
    	JSONObject jsonObject = httpRequest(url, "POST", jsonGroup);
        if (null != jsonObject) {  
            if (jsonObject.get("group") != null) {  
            	group = (WxUserGroup) jsonObject.toBean(jsonObject.getJSONObject("group"), WxUserGroup.class);
            }else {
            	log.error("创建分组失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }
        return group;
    }
    
    //修改微信用户分组名称
    public static String WxUserGroup_update_url = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token=ACCESS_TOKEN";
    
    public static int updateWxUserGroup(String groupId, String name, String accessToken) {
    	int result = 0;
    	// 拼装url
    	String url = WxUserGroup_update_url.replace("ACCESS_TOKEN", accessToken);
    	//拼接提交分组名称需要字符串
    	String jsonGroup = "{\"group\":{\"id\":"+ groupId +", \"name\":\""+ name +"\"}}";
    	// 调用
    	JSONObject jsonObject = httpRequest(url, "POST", jsonGroup);
        if (null != jsonObject) {  
            if (0 != jsonObject.getInt("errcode")) {  
                result = jsonObject.getInt("errcode");  
                log.error("修改分组名称失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }
     	return result;
    }
    
    //删除微信用户分组
    public static String WxUserGroup_delete_url = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token=ACCESS_TOKEN";
    
    public static int deleteWxUserGroup(String groupId, String accessToken) {
    	int result = 0;
    	//拼装url
    	String url = WxUserGroup_delete_url.replace("ACCESS_TOKEN", accessToken);
    	//拼接提交分组ID需要字符串
    	String jsonGroup = "{\"group\":{\"id\":"+ groupId +"}}";
    	// 调用
    	JSONObject jsonObject = httpRequest(url, "POST", jsonGroup);
        if (null != jsonObject) {  
            if (0 != jsonObject.getInt("errcode")) {  
                result = jsonObject.getInt("errcode");  
                log.error("删除分组失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }
     	return result;
    }
    
    //批量移动微信用户到分组
    public static String WxUserGroup_batchmove_url = "https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token=ACCESS_TOKEN";
    
    public static int batchmoveWxUserGroup(String openid_list, String to_groupid, String accessToken) {
    	int result = 0;
    	//拼装url
    	String url = WxUserGroup_batchmove_url.replace("ACCESS_TOKEN", accessToken);
    	//拼接需要字符串
    	String jsonGroup = "{\"openid_list\":"+openid_list+",\"to_groupid\":"+to_groupid+"}";
    	// 调用
    	JSONObject jsonObject = httpRequest(url, "POST", jsonGroup);
        if (null != jsonObject) {  
            if (0 != jsonObject.getInt("errcode")) {  
                result = jsonObject.getInt("errcode");  
                log.error("移动用户到分组失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }
     	return result;
    }
    
    //向微信用户发送模板消息接口
    public static String sen_template_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
    
    public static int senTemplate(GovTemplate template, String accessToken) {
    	int result = 0;
    	String url = sen_template_url.replace("ACCESS_TOKEN", accessToken);
    	//String jsontem = "{\"touser\": \"o_pk3tw19Y1kwS81ErLS9ib6xPzo\",\"template_id\": \"lKQEG2FxXWbfqIOj7Fzn8VFYUs4ArLnSc_UOhVL3Gt0\",\"url\": \"http://www.baidu.com\",\"data\": {\"first\": {\"value\": \"测试业务办理情况通知\",\"color\": \"#173177\"},\"keyword1\": {\"value\": \"字段1\",\"color\": \"#1731FF\"},\"keyword2\": {\"value\": \"201511038888\",\"color\": \"#173177\"},\"keyword3\": {\"value\": \"字段2\",\"color\": \"#173177\"},\"remark\": {\"value\": \"欢迎再次使用！\",\"color\": \"#173177\"}}}";
    	String jsontemplate = JSONObject.fromObject(template).toString();
    	JSONObject jsonObject = httpRequest(url, "POST", jsontemplate);
        if (null != jsonObject) {  
            if (0 != jsonObject.getInt("errcode")) {  
                result = jsonObject.getInt("errcode");  
                log.error("发送模板消息失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));  
            }  
        }
        return result;
    }
    
    
}






