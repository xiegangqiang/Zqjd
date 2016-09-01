package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.User;
import com.xysoft.support.BaseDao;

public interface UserDao extends BaseDao<User>{

	User getUser(String username);

	void saveUser(User user);

	List<User> getUserByField(String field, String value);

	User getUserById(String id);

}
