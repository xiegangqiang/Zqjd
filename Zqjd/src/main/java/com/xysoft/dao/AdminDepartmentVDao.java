package com.xysoft.dao;

import java.util.List;

import com.xysoft.support.BaseDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.view.AdminDepartmentV;

public interface AdminDepartmentVDao extends BaseDao<AdminDepartmentV>{

	Pager<AdminDepartmentV> getAdminDepartmentVs(
			PageParam page, String departmentName, String name);

	List<AdminDepartmentV> getAdminDepartmentVsByIds(String sqlIn);

	Pager<AdminDepartmentV> getAdminDepartmentVsByDepartments(PageParam page,
			String name, String sqlIn);

	AdminDepartmentV getAdminDepartmentVByUserName(String username);

	List<AdminDepartmentV> getAdminDepartmentVsByDepartments(String sqlIn);

	List<AdminDepartmentV> getAdminDepartmentVs();


}
