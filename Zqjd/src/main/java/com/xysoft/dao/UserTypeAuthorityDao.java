package com.xysoft.dao;

import java.util.List;
import com.xysoft.entity.UserTypeAuthority;
import com.xysoft.support.BaseDao;

public interface UserTypeAuthorityDao extends BaseDao<UserTypeAuthority>{

	List<UserTypeAuthority> getUserTypeAuthority(Integer userType);

}
