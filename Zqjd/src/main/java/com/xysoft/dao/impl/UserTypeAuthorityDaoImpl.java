package com.xysoft.dao.impl;

import java.util.List;
import org.springframework.stereotype.Component;
import com.xysoft.dao.UserTypeAuthorityDao;
import com.xysoft.entity.UserTypeAuthority;
import com.xysoft.support.BaseDaoImpl;

@Component
public class UserTypeAuthorityDaoImpl extends BaseDaoImpl<UserTypeAuthority> implements UserTypeAuthorityDao{

	public List<UserTypeAuthority> getUserTypeAuthority(Integer userType) {
		return this.find("from UserTypeAuthority where userType = ?", userType);
	}

}
