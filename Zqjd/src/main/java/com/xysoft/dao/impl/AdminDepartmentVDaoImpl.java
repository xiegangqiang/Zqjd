package com.xysoft.dao.impl;


import java.util.List;

import org.springframework.stereotype.Component;
import com.xysoft.dao.AdminDepartmentVDao;
import com.xysoft.support.BaseDaoImpl;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.view.AdminDepartmentV;
@Component
public class AdminDepartmentVDaoImpl extends BaseDaoImpl<AdminDepartmentV> implements AdminDepartmentVDao{

	public Pager<AdminDepartmentV> getAdminDepartmentVs(PageParam page, String departmentName, String name) {
		return this.getForPager("from AdminDepartmentV where userType = 0 and (name like ? or username like ? ) and departmentName like ? order by createDate desc", 
				page, "%" + name + "%", "%" + name + "%", "%" + departmentName + "%");
	}

	public List<AdminDepartmentV> getAdminDepartmentVsByIds(String sqlIn) {
		return this.find("from AdminDepartmentV where id in"+sqlIn);
	}

	public Pager<AdminDepartmentV> getAdminDepartmentVsByDepartments(
			PageParam page, String name, String sqlIn) {
		return this.getForPager("from AdminDepartmentV where userType = 0 and (name like ? or username like ? ) and department In"+sqlIn+"order by createDate desc", 
				page, "%" + name + "%", "%" + name + "%");
	}

	public AdminDepartmentV getAdminDepartmentVByUserName(String username) {
		return this.get("from AdminDepartmentV where username = ?", username);
	}

	public List<AdminDepartmentV> getAdminDepartmentVsByDepartments(String sqlIn) {
		return this.find("from AdminDepartmentV where department In"+sqlIn);
	}

	public List<AdminDepartmentV> getAdminDepartmentVs() {
		return this.find("from AdminDepartmentV");
	}

}
