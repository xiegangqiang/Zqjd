package com.xysoft.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.admin.service.AdminService;
import com.xysoft.dao.AdminDao;
import com.xysoft.dao.AdminDepartmentVDao;
import com.xysoft.dao.AdminRoleDao;
import com.xysoft.entity.Admin;
import com.xysoft.entity.AdminRole;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;
import com.xysoft.util.RequestUtil;
import com.xysoft.view.AdminDepartmentV;
import com.xysoft.websocket.SocketQueue;


@SuppressWarnings("unchecked")
@Component
public class AdminServiceImpl implements AdminService {
	@Resource
	private AdminDao adminDao;
	@Resource
	private AdminRoleDao adminRoleDao;
	@Resource
	private AdminDepartmentVDao adminDepartmentVDao;
	
	@Transactional(readOnly = true)
	public String getAdmins(PageParam page, String name, String departmentName, Boolean online) {
		//Pager<Admin> pager = this.adminDao.getAdmins(page, name);
		Pager<AdminDepartmentV> pager = this.adminDepartmentVDao.getAdminDepartmentVs(page, departmentName.replaceAll("　", ""), name);
		Map<String, AdminDepartmentV> onlineMap = new HashMap<String, AdminDepartmentV>();
		Map<String, AdminDepartmentV> offlineMap = new HashMap<String, AdminDepartmentV>();
		for (AdminDepartmentV adv : pager.getDatas()) {
			if(SocketQueue.map.containsKey(adv.getId())) {
				adv.setOnline(true); 
				onlineMap.put(adv.getId(), adv);
			}else {
				adv.setOnline(false);
				offlineMap.put(adv.getId(), adv);
			}
		}
		if(online == null) return JsonUtil.toStringFromObject(pager);
		if(online) {
			Pager<AdminDepartmentV> onlinePager = new Pager<AdminDepartmentV>();
			onlinePager.setDatas(new ArrayList<AdminDepartmentV>(onlineMap.values()));
			onlinePager.setPageCount(pager.getPageCount());
			onlinePager.setPageIndex(pager.getPageIndex());
			onlinePager.setTotal(pager.getTotal()-offlineMap.size());
			return JsonUtil.toStringFromObject(onlinePager);
		}else {
			Pager<AdminDepartmentV> offlinePager = new Pager<AdminDepartmentV>();
			offlinePager.setDatas(new ArrayList<AdminDepartmentV>(offlineMap.values()));
			offlinePager.setPageCount(pager.getPageCount());
			offlinePager.setPageIndex(pager.getPageIndex());
			offlinePager.setTotal(pager.getTotal()-onlineMap.size());
			return JsonUtil.toStringFromObject(offlinePager);
		}
	}

	@Transactional
	public String saveAdmin(Admin param, String[] roles) {
		Admin admin = null;
		if (!"".equals(param.getId())) {
			admin = this.adminDao.getAdminById(param.getId());
		} else {
			admin = new Admin();
			admin.setUserType(0);
			admin.setIsAccountExpired(false);
			admin.setIsAccountLocked(false);
			admin.setIsCredentialsExpired(false);
			admin.setLoginFailureCount(0);
		}
		List<Admin> admins = this.adminDao.getAdminValid(param.getUsername());
		if (!param.getUsername().trim().equals(admin.getUsername()) && admins.size() > 0) {
			return JsonUtil.toResOfFail("保存失败，用户名已存在");
		}
		BeanUtils.copyProperties(param, admin, new String[]{"id", "loginDate", "loginIp", "createDate", 
				"password", "userType", "isAccountExpired", "isAccountLocked", "isCredentialsExpired", "loginFailureCount"});
		if (!"".equals(param.getPassword())) {
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			admin.setPassword(encoder.encodePassword(param.getPassword(), param.getUsername()));
		}
		this.adminDao.saveAdmin(admin);
		
		List<AdminRole> adminRoles = this.adminRoleDao.getAdminRoles(admin.getId());
		Map<String, AdminRole> maps = new HashMap<String, AdminRole>();
		for (AdminRole adminRole : adminRoles) {
			maps.put(adminRole.getRole(), adminRole);
		}
		if (roles != null) {
			for (String role : roles) {
				if (maps.containsKey(role)) {
					maps.remove(role);
				} else {
					AdminRole ar = new AdminRole();
					ar.setAdmin(admin.getId());
					ar.setRole(role);
					this.adminRoleDao.saveAdminRole(ar);
				}
			}
		}
		for (AdminRole adminRole : maps.values()) {
			this.adminRoleDao.deleteAdminRole(adminRole);
		}
		return JsonUtil.toRes("保存成功");
	}
	
	@Transactional
	public String saveAdminBatch(String username, String password, String department, Integer number, String[] roles) {
		List<Admin> admins = this.adminDao.getAdmins();
		Map<String, Object> map = new HashMap<String, Object>();
		for (Admin admin : admins) {
			map.put(admin.getUsername(), admin);
		}
		int count = 0;
		for (int i = 0; i < number; i++) {
			String account = username + i+"";
			if(map.containsKey(account)) continue;
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			String pass = encoder.encodePassword(password, account);
			
			Admin admin = new Admin();
			admin.setUserType(0);
			admin.setIsAccountEnabled(true);
			admin.setIsAccountExpired(false);
			admin.setIsAccountLocked(false);
			admin.setIsCredentialsExpired(false);
			admin.setLoginFailureCount(0);
			admin.setUsername(account);
			admin.setName(account);
			admin.setPassword(pass);
			admin.setDepartment(department);
			
			this.adminDao.saveAdmin(admin);
			
			if (roles != null) {
				for (String role : roles) {
					AdminRole ar = new AdminRole();
					ar.setAdmin(admin.getId());
					ar.setRole(role);
					this.adminRoleDao.saveAdminRole(ar);
				}
			}
			
			count++;
		}
		return JsonUtil.toRes("成功生成"+count+"个帐号");
	}

	@Transactional
	public String deleteAdmin(String id) {
		Admin admin = this.adminDao.getAdminById(id);
		List<AdminRole> adminRoles = this.adminRoleDao.getAdminRoles(admin.getId());
		
		//删除用户角色
		for (AdminRole adminRole : adminRoles) {
			this.adminRoleDao.deleteAdminRole(adminRole);
		}
		return JsonUtil.toRes("删除成功");
	}

	@Transactional(readOnly = true)
	public String getAdminInfo(String id) {
		Admin admin = this.adminDao.getAdmin(RequestUtil.getUsername());
		return JsonUtil.toStringFromObject(admin);
	}
	
	@Transactional
	public String updateAdmin(Admin param) {
		Admin admin = this.adminDao.getAdminById(param.getId());
		if(NullUtils.isNotEmpty(param.getName())) admin.setName(param.getName());
		if(NullUtils.isNotEmpty(param.getEmail())) admin.setEmail(param.getEmail());
		if( NullUtils.isNotEmpty(param.getUsername()) && NullUtils.isNotEmpty(param.getPassword() )) {
			List<Admin> admins = this.adminDao.getAdminValid(param.getUsername());
			if (!param.getUsername().trim().equals(admin.getUsername()) && admins.size() > 0) {
				return JsonUtil.toResOfFail("用户名已存在");
			}
			admin.setUsername(param.getUsername());
		    Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		    admin.setPassword(encoder.encodePassword(param.getPassword(), param.getUsername()));
		}
		this.adminDao.saveAdmin(admin);
		return JsonUtil.toRes("更新成功");
	}
	
}
