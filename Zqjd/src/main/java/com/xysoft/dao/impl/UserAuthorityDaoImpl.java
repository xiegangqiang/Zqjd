package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import com.xysoft.dao.UserAuthorityDao;
import com.xysoft.entity.UserAuthority;
import com.xysoft.support.BaseDaoImpl;
@Component
public class UserAuthorityDaoImpl extends BaseDaoImpl<UserAuthority> implements UserAuthorityDao{

	public List<UserAuthority> getUserAuthority(String user) {
		return this.find("from UserAuthority where user = ?", user);
	}

	public void saveUserAuthority(UserAuthority userAuthority) {
		this.saveOrUpdate(userAuthority);
	}

	public void deleteUserAuthority(UserAuthority userAuthority) {
		this.delete(userAuthority);
	}

}
