package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import com.xysoft.dao.UserDao;
import com.xysoft.entity.User;
import com.xysoft.support.BaseDaoImpl;

@Component
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao{

	public User getUser(String username) {
		return this.get("from User where username = ?", username);
	}

	public void saveUser(User user) {
		this.saveOrUpdate(user);
	}

	public List<User> getUsersByField(String field, String value) {
		return this.find("from User where "+field+" like ?", "%"+value+"%");
	}
	
	public User getUserByField(String field, String value) {
		return this.get("from User where "+field+" = ?", value);
	}

	public User getUserById(String id) {
		return this.get(User.class, id);
	}

}
