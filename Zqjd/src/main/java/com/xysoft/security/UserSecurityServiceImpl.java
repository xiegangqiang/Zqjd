package com.xysoft.security;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import com.xysoft.common.InitializationConst;
import com.xysoft.dao.AdminDao;
import com.xysoft.dao.AdminRoleDao;
import com.xysoft.dao.AuthorityDao;
import com.xysoft.dao.MenuDao;
import com.xysoft.dao.RoleMenuDao;
import com.xysoft.dao.UserAuthorityDao;
import com.xysoft.dao.UserDao;
import com.xysoft.entity.Admin;
import com.xysoft.entity.AdminRole;
import com.xysoft.entity.Authority;
import com.xysoft.entity.Menu;
import com.xysoft.entity.RoleMenu;
import com.xysoft.entity.User;
import com.xysoft.entity.UserAuthority;
import com.xysoft.util.NullUtils;

public class UserSecurityServiceImpl {
	@Resource
	private AdminDao adminDao;
	@Resource
	private MenuDao menuDao;
	@Resource
	private RoleMenuDao roleMenuDao;
	@Resource
	private AdminRoleDao adminRoleDao;
	@Resource
	private UserDao userDao;
	@Resource
	private AuthorityDao authorityDao;
	@Resource
	private UserAuthorityDao userAuthorityDao;
//	@Resource
//	private AuthorityDao authorityDao;
	
	public Admin getLoginAdminInfo(String username) {
		Admin admin = this.adminDao.getAdmin(username);
		Date nowDate = new Date();
		if (admin == null || (admin.getLoginFailureCount() >= InitializationConst.FailureLockCount) 
				||(admin.getLockedDate() != null && admin.getLockedDate().getTime() > nowDate.getTime())) {
			return null;
		}
		admin.setAuthorities(this.obtionAdminGrantedAuthorities(admin));
		return admin;
	}

	public User getLoginUserInfo(String username) {
		User user = this.userDao.getUser(username);
		Date nowDate = new Date();
		if(user == null || (user.getLoginFailureCount() >= InitializationConst.FailureLockCount)
				|| (user.getLockedDate() != null && user.getLockedDate().getTime() > nowDate.getTime())) {
			return null;
		}
		user.setAuthorities(this.obtionUserGrantedAuthorities(user));
		return user;
	}
	
	/**
	 * 取得管理员的权限(Hibernate方式).
	 */
	private Set<GrantedAuthority> obtionAdminGrantedAuthorities(Admin admin) {
		Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>(); 
		
		switch (admin.getUserType()) {
			case 99 : {
				List<Menu> menus = this.menuDao.getMenusAction();
				for (Menu menu : menus) {
					GrantedAuthority gay = new GrantedAuthorityImpl(menu.getAction());
	    			authSet.add(gay);
				}
				break;
			}
			case 9 : {
				List<Menu> menus = this.menuDao.getMenusAction();
				for (Menu menu : menus) {
					GrantedAuthority gay = new GrantedAuthorityImpl(menu.getAction());
	    			authSet.add(gay);
				}
				break;
			}
			default: {
				List<Menu> menus = this.menuDao.getMenusAction();
				Map<String, Menu> authorityMaps = new HashMap<String, Menu>();
				for (Menu menu : menus) {
					authorityMaps.put(menu.getId(), menu);
		        }
				List<AdminRole> adminRoles = this.adminRoleDao.getAdminRoles(admin.getId());
				for (AdminRole adminRole : adminRoles) {
					List<RoleMenu> roleMenus = this.roleMenuDao.getRoleMenus(adminRole.getRole());
					for (RoleMenu roleMenu : roleMenus) {
						if (authorityMaps.containsKey(roleMenu.getMenu())) {
							GrantedAuthority gay = new GrantedAuthorityImpl(authorityMaps.get(roleMenu.getMenu()).getAction());
			    			authSet.add(gay);
						}
					}
				}
				break;
			}
		}
		for (String access : InitializationConst.Init_Roles) {
			GrantedAuthority gayInit = new GrantedAuthorityImpl(access);
			authSet.add(gayInit);
		}
        return authSet;
	}
	
	/**
	 * 取得用户的权限.  
	*/ 
	private Set<GrantedAuthority> obtionUserGrantedAuthorities(User user) {  
        Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>(); 
        
        List<Authority> authoritys = this.authorityDao.getAuthoritys();
        switch (user.getUserType()) {
			case 99: {
				for (Authority authority : authoritys) {
					GrantedAuthority gay = new GrantedAuthorityImpl(authority.getAction());
					authSet.add(gay);
				}
				break;
			}
			default: {
				List<UserAuthority> userAuthoritys = this.userAuthorityDao.getUserAuthority(user.getId());
				for (Authority authority : authoritys) {
					for (UserAuthority userAuthority : userAuthoritys) {
						if(NullUtils.isNotEmpty(authority.getAction()) && authority.getId().equals(userAuthority.getAuthority())) {
							GrantedAuthority gay = new GrantedAuthorityImpl(authority.getAction());
							authSet.add(gay);
						}
					}
				}
				break;
			}
		}
        for (String access : InitializationConst.Init_User_Roles) {
			GrantedAuthority gayInit = new GrantedAuthorityImpl(access);
			authSet.add(gayInit);
		}
        return authSet;
    }

}
