package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.RoleDao;
import com.xysoft.entity.Role;
import com.xysoft.support.BaseDaoImpl;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

@Component
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao {

	public List<Role> getAllActiveRoles() {
		return this.find("from Role where visible = true order by createDate asc");
	}

	public Pager<Role> getRoles(PageParam page, String name) {
		return this.getForPager("from Role where name like ? order by createDate asc", page, "%" + name + "%");
	}

	public Role getRole(String id) {
		return this.get(Role.class, id);
	}

	public void saveRole(Role role) {
		this.saveOrUpdate(role);
	}

	public void deleteRole(Role role) {
		this.delete(role);
	}

}
