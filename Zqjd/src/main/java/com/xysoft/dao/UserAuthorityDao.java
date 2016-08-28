package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.UserAuthority;
import com.xysoft.support.BaseDao;

public interface UserAuthorityDao extends BaseDao<UserAuthority>{

	List<UserAuthority> getUserAuthority(String user);

	void saveUserAuthority(UserAuthority userAuthority);

	void deleteUserAuthority(UserAuthority userAuthority);

}
