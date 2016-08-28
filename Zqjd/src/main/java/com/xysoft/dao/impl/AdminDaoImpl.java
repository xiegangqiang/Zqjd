package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.AdminDao;
import com.xysoft.entity.Admin;
import com.xysoft.support.BaseDaoImpl;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

@Component
public class AdminDaoImpl extends BaseDaoImpl<Admin> implements AdminDao {

	public Pager<Admin> getAdmins(PageParam page, String name) {
		return this.getForPager("from Admin where userType = 0 and (name like ? or username like ? ) order by createDate desc", 
				page, "%" + name + "%", "%" + name + "%");
	}
	
	public Admin getAdmin(String username) {
		return this.get("from Admin where username = ?", username);
	}
	
	public Admin getAdminById(String id) {
		return this.get(Admin.class, id);
	}

	public void saveAdmin(Admin admin) {
		this.save(admin);
	}

	public void deleteAdmin(Admin admin) {
		this.delete(admin);
	}
	
	public List<Admin> getAdminValid(String username) {
		return this.find("from Admin where username = ?", username);
	}

	public List<Admin> getAdmins() {
		return this.find("from Admin");
	}

	public List<Admin> getAdminByIds(String sqlIn) {
		return this.find("from Admin where id in"+sqlIn+" and isAccountEnabled is true");
	}
}
