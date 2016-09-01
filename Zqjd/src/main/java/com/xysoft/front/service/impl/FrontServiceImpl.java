package com.xysoft.front.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xysoft.dao.GiftCodeDao;
import com.xysoft.dao.UserDao;
import com.xysoft.entity.GiftCode;
import com.xysoft.entity.User;
import com.xysoft.front.service.FrontService;
import com.xysoft.util.CommonUtil;
import com.xysoft.util.JsonUtil;

@Component
public class FrontServiceImpl implements FrontService {
	
	@Resource
	private GiftCodeDao giftCodeDao;
	@Resource
	private UserDao userDao;
	
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
			
			List<User> users = this.userDao.getUserByField("phone", phone);
			if(users.size() == 0) {
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
	

}



