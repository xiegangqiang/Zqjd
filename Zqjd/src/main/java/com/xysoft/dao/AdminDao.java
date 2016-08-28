package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.Admin;
import com.xysoft.support.BaseDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

public interface AdminDao extends BaseDao<Admin> {
	
	/**
	 * 获取管理员列表.
	 */
	public Pager<Admin> getAdmins(PageParam page, String name);
	
	/**
	 * 获取管理员信息.
	 */
	public Admin getAdmin(String username);
	
	/**
	 * 获取管理员信息.
	 */
	public Admin getAdminById(String id);
	
	/**
	 * 保存管理员信息.
	 */
	public void saveAdmin(Admin admin);
	
	/**
	 * 删除管理员信息.
	 */
	public void deleteAdmin(Admin admin);
	
	/**
	 * 获取管理员列表.
	 */
	public List<Admin> getAdminValid(String username);

	public List<Admin> getAdmins();

	public List<Admin> getAdminByIds(String sqlIn);
}
