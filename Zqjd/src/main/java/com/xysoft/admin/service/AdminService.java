package com.xysoft.admin.service;

import com.xysoft.entity.Admin;
import com.xysoft.support.PageParam;

public interface AdminService {
	
	/**
	 * 获取管理员列表.
	 */
	public String getAdmins(PageParam page, String name, String departmentName, Boolean online);

	/**
	 * 保存管理员信息.
	 */
	public String saveAdmin(Admin param, String[] roles);
	
	/**
	 * 批量生成管理员
	 */
	public String saveAdminBatch(String username, String department, String password,
			Integer number, String[] roles);
	
	/**
	 * 删除管理员信息.
	 */
	public String deleteAdmin(String id);
	
	/**
	 * 获取管理员信息.
	 */
	public String getAdminInfo(String id);

	/**
	 * 更新管理员信息
	 */
	public String updateAdmin(Admin param);
	
}
