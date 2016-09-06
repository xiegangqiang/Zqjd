package com.xysoft.front.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.common.ElementConst;
import com.xysoft.dao.EvaluatDao;
import com.xysoft.dao.GiftCodeDao;
import com.xysoft.dao.OrderDao;
import com.xysoft.dao.UserDao;
import com.xysoft.entity.Evaluat;
import com.xysoft.entity.GiftCode;
import com.xysoft.entity.Orders;
import com.xysoft.entity.User;
import com.xysoft.entity.WxUser;
import com.xysoft.front.service.FrontService;
import com.xysoft.support.DynamicBean;
import com.xysoft.support.JdbcDao;
import com.xysoft.util.CommonUtil;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;
import com.xysoft.util.SqlUtil;
import com.xysoft.weixin.pojo.AccessToken;
import com.xysoft.weixin.util.WeixinUtil;

@Component
@SuppressWarnings("unchecked")
public class FrontServiceImpl implements FrontService {
	
	@Resource
	private GiftCodeDao giftCodeDao;
	@Resource
	private UserDao userDao;
	@Resource
	private OrderDao orderDao;
	@Resource
	private EvaluatDao evaluatDao;
	@Resource
	private JdbcDao<DynamicBean> jdbcDao;
	
	@Transactional(readOnly = true)
	public Map<String, Object> error() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("model", "front/error");
		return model;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> error404() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("model", "front/404");
		return model;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> error500() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("model", "front/500");
		return model;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> login() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("model", "client/login");
		return model;
	}

	@Transactional(readOnly = true)
	public Map<String, Object> index() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("model", "front/index");
		return model;
	}

	@Transactional(readOnly = true)
	public Map<String, Object> scangift() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("model", "front/scangift/scangift");
		return model;
	}

	@Transactional
	public String getGiftCode(String phone) {
		phone.trim();
		GiftCode  giftCode = this.giftCodeDao.getGiftCodeByPhone(phone);
		if(giftCode == null) {//未有码
			String code = CommonUtil.getUUID().substring(0, 8).toUpperCase();
			giftCode = new GiftCode();
			giftCode.setCode(code);
			giftCode.setPhone(phone);
			giftCode.setIsGrant(false);
			this.giftCodeDao.saveGiftCode(giftCode);
			
			User users = this.userDao.getUserByField("phone", phone);
			if(users == null) {
				User user = new User();
				user.setPhone(phone);
				user.setUsername(phone);
				Md5PasswordEncoder encoder = new Md5PasswordEncoder();
				user.setPassword(encoder.encodePassword(phone, phone));
				user.setUserType(0);
				user.setIsAccountEnabled(true);
				user.setIsAccountExpired(false);
				user.setIsAccountLocked(false);
				user.setIsCredentialsExpired(false);
				user.setLoginFailureCount(0);
				this.userDao.saveUser(user);
			}
			
			return JsonUtil.toStringFromObject(giftCode);
		}
		return JsonUtil.toStringFromObject(giftCode);
	}
	
	@Transactional
	public String updateUser(User param) {
		User user = this.userDao.getUserByField("phone", param.getPhone().replace(" ", ""));
		if(user == null) {
			user = new User();
			user.setUsername(param.getPhone().replace(" ", ""));
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			user.setPassword(encoder.encodePassword(param.getPhone().replace(" ", ""), param.getPhone().replace(" ", "")));
			user.setUserType(0);
			user.setIsAccountEnabled(true);
			user.setIsAccountExpired(false);
			user.setIsAccountLocked(false);
			user.setIsCredentialsExpired(false);
			user.setLoginFailureCount(0);
		}
		user.setPhone(param.getPhone().replace(" ", ""));
		user.setWxUser(param.getWxUser());
		user.setAddress(param.getAddress());
		this.userDao.saveUser(user);
		return JsonUtil.toRes("更新成功");
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> entry() {
		Map<String, Object> model = new HashMap<String, Object>();
		String redirectUri = WeixinUtil.getOauth2Code(ElementConst.Wx_AppId, ElementConst.Service_Address + "/wxcenter.jhtml", 0);
		model.put("sys_url", redirectUri);
		model.put("model", "front/go");
		return model;
	}

	@Transactional(readOnly = true)
	public Map<String, Object> wxcenter(String code) {
		Map<String, Object> model = new HashMap<String, Object>();
		if(code != null && !"authdeny".equals(code)) {
			// 获取网页授权access_token
			AccessToken accessToken = WeixinUtil.getOauth2AccessToken(ElementConst.Wx_AppId, ElementConst.Wx_AppSecret, code);
			// 获取用户信息
			WxUser wxUser = WeixinUtil.getOauth2WxUser(accessToken.getToken(), accessToken.getOpenid());
			
			String sql = "SELECT * From wxuser wu LEFT JOIN `user` u ON u.wxUser=wu.id WHERE wu.openid=?";
			DynamicBean bean = this.jdbcDao.get(sql, wxUser.getOpenid());
			model.put("user", bean.getObject());
		}
		model.put("model", "front/wxcenter/wxcenter");
		return model;
	}

	@Transactional
	public Map<String, Object> orderlist(String wxUser, String phone) {
		Map<String, Object> model = new HashMap<String, Object>();
		String newphone = phone.replace(" ", "");
		User user = this.userDao.getUserByField("phone", newphone);
		if(user != null) {
			user.setPhone(newphone);
			user.setWxUser(wxUser);
		}else {
			user = new User();
			user.setPhone(newphone);
			user.setUsername(newphone);
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			user.setPassword(encoder.encodePassword(newphone, newphone));
			user.setUserType(0);
			user.setIsAccountEnabled(true);
			user.setIsAccountExpired(false);
			user.setIsAccountLocked(false);
			user.setIsCredentialsExpired(false);
			user.setLoginFailureCount(0);
		}
		this.userDao.saveUser(user);
		
		List<DynamicBean> beans = this.jdbcDao.find("SELECT ord.*,COUNT(el.orders) AS evaluat FROM orders ord LEFT JOIN evaluat el ON el.orders=ord.id WHERE ord.`user`=? GROUP BY ord.id", user.getId());
		model.put("phone", phone);
		model.put("orders", SqlUtil.DynamicToBean(beans));
		model.put("model", "front/orderlist/orderlist");
		return model;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> ordermark(String order, String phone) {
		Map<String, Object> model = new HashMap<String, Object>();
		String sql = "SELECT fsrp.id,r.`name` FROM flowsteprec fsr JOIN flowsteprecpost fsrp ON fsr.id=fsrp.flowStepRec JOIN role r ON r.id=fsrp.posts WHERE fsr.orders = ?";
		List<DynamicBean> beans = this.jdbcDao.find(sql, order);
		model.put("phone", phone);
		model.put("order", order);
		model.put("flowStepRecPost", SqlUtil.DynamicToBean(beans));
		model.put("model", "front/ordermark/ordermark");
		return model;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> mymark(String wxUser, String phone) {
		Map<String, Object> model = new HashMap<String, Object>();
		String newphone = phone.replace(" ", "");
		User user = this.userDao.getUserByField("phone", newphone);
		if(user != null) {
			user.setPhone(newphone);
			user.setWxUser(wxUser);
		}else {
			user = new User();
			user.setPhone(newphone);
			user.setUsername(newphone);
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			user.setPassword(encoder.encodePassword(newphone, newphone));
			user.setUserType(0);
			user.setIsAccountEnabled(true);
			user.setIsAccountExpired(false);
			user.setIsAccountLocked(false);
			user.setIsCredentialsExpired(false);
			user.setLoginFailureCount(0);
		}
		this.userDao.saveUser(user);
		
		String evaluatSql = "SELECT el.*,r.`name` FROM evaluat el LEFT JOIN flowsteprecpost fsr ON fsr.id=el.flowStepRecPost LEFT JOIN role r ON r.id=fsr.posts WHERE el.`user` = ?";
		List<DynamicBean> evaluats = this.jdbcDao.find(evaluatSql, user.getId());
		
		String orderSql = "SELECT ord.*,COUNT(el.orders) AS evaluat FROM orders ord LEFT JOIN evaluat el ON el.orders=ord.id WHERE ord.`user`=? GROUP BY ord.id";
		List<DynamicBean> orders = this.jdbcDao.find(orderSql, user.getId());
		
		model.put("orders", SqlUtil.DynamicToBean(orders));
		model.put("evaluats", SqlUtil.DynamicToBean(evaluats));
		model.put("model", "front/mymark/mymark");
		return model;
	}

	@Transactional
	public String submitEvaluat(String order, String[] post, String[] rats, String[] imgs, String describe) {
		if(NullUtils.isNotEmpty(order) || post != null || rats !=null || imgs != null) {
			Orders orders = this.orderDao.getOrder(order);
			//处理分值
			for (int i=0;i<post.length;i++) {
				Evaluat evaluat = new Evaluat();
				evaluat.setOrders(orders.getId());
				evaluat.setFlowStepRecPost(post[i]);
				evaluat.setUser(orders.getUser());
				evaluat.setType(0);
				evaluat.setValue(rats[i]);
				this.evaluatDao.saveEvaluat(evaluat);
			}
			//处理图片
			for (String img : imgs) {
				Evaluat evaluat = new Evaluat();
				evaluat.setOrders(orders.getId());
				evaluat.setUser(orders.getUser());
				evaluat.setType(1);
				evaluat.setValue(img);
				this.evaluatDao.saveEvaluat(evaluat);
			}
			//处理描述
			if(NullUtils.isNotEmpty(describe)) {
				Evaluat evaluat = new Evaluat();
				evaluat.setOrders(orders.getId());
				evaluat.setUser(orders.getUser());
				evaluat.setType(2);
				evaluat.setValue(describe);
				this.evaluatDao.saveEvaluat(evaluat);
			}
		}
		return JsonUtil.toRes("提交成功");
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> wxhome(String anchor) {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("anchor", anchor);
		model.put("model", "front/wxhome/index");
		return model;
	}

}



