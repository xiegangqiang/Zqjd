package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import com.xysoft.dao.AuthorityDao;
import com.xysoft.entity.Authority;
import com.xysoft.support.BaseDaoImpl;
@Component
public class AuthorityDaoImpl extends BaseDaoImpl<Authority> implements AuthorityDao{

	public List<Authority> getAuthoritys() {
		return this.find("from Authority where visible = 1 order by leaf asc, level desc");
	}

}
