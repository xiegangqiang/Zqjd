package com.xysoft.dao.impl;

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

}
