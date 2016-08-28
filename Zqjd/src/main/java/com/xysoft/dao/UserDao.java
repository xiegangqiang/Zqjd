package com.xysoft.dao;

import com.xysoft.entity.User;
import com.xysoft.support.BaseDao;

public interface UserDao extends BaseDao<User>{

	User getUser(String username);

	void saveUser(User user);

}
