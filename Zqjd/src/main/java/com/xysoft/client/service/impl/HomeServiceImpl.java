package com.xysoft.client.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.client.service.HomeService;
import com.xysoft.comparator.AuthorityComparator;
import com.xysoft.dao.AuthorityDao;
import com.xysoft.dao.UserAuthorityDao;
import com.xysoft.dao.UserDao;
import com.xysoft.dao.UserTypeAuthorityDao;
import com.xysoft.entity.Authority;
import com.xysoft.entity.User;
import com.xysoft.entity.UserAuthority;
import com.xysoft.entity.UserTypeAuthority;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.RequestUtil;

@Component
@SuppressWarnings("unchecked")
public class HomeServiceImpl implements HomeService {
	
	@Resource
	private AuthorityDao authorityDao;
	@Resource
	private UserTypeAuthorityDao userTypeAuthorityDao;
	@Resource
	private UserDao userDao;
	@Resource
	private UserAuthorityDao userAuthorityDao;

	@Transactional(readOnly = true)
	public String getAuthority() {
		List<String> actions = RequestUtil.getActionsByLoginUser();
		Map<String, Authority> tempMaps = new HashMap<String, Authority>();
		List<Authority> authoritys = this.authorityDao.getAuthoritys();
		for (Authority authority : authoritys) {
			tempMaps.put(authority.getId(), authority);
		}
		Map<String, Authority> menuMaps = new HashMap<String, Authority>();
		for (Authority authority : authoritys) {
			if (actions.indexOf(authority.getAction()) == -1 || authority.getLeaf() != 3) continue;
			menuMaps.put(authority.getId(), authority);
			menuMaps = this.rebuild(menuMaps, authority, tempMaps);
		}
		List<Authority> dtos = new ArrayList<Authority>();
		for (Authority menu : menuMaps.values()) {
			if (menu.getParentId() == null || "".equals(menu.getParentId())) {
				dtos.add(menu);
			}
		}
		Collections.sort(dtos, new AuthorityComparator());
		return JsonUtil.toString(dtos);
	}

	/**
	 * 递归构建菜单树形.
	 */
	private Map<String, Authority> rebuild(Map<String, Authority> authorityMaps, 
			Authority doo, Map<String, Authority> tempMaps) {
		if (doo.getParentId() == null || "".equals(doo.getParentId())) return authorityMaps;
		Authority hm = null;
		if (authorityMaps.containsKey(doo.getParentId())) {
			hm = authorityMaps.get(doo.getParentId());
		} else {
			hm = tempMaps.get(doo.getParentId());
		}
		List<Authority> lst = hm.getChildren();
		for (Authority ls : lst) {
			if (ls.getId() == doo.getId()) {
				lst.remove(ls);
				break;
			}
		}
		lst.add(doo);
		hm.setChildren(lst);
		authorityMaps.put(doo.getParentId(), hm);
		if (doo.getParentId() != null && !"".equals(doo.getParentId())) {
			authorityMaps = this.rebuild(authorityMaps, hm, tempMaps);
		}
		return authorityMaps;
	}

	@Transactional(readOnly = true)
	public String getUserTypeAuthority() {
		User user = this.userDao.getUser(RequestUtil.getUsername());
		List<Authority> authoritys = this.authorityDao.getAuthoritys();
		List<UserTypeAuthority> userTypeAuthoritys = this.userTypeAuthorityDao.getUserTypeAuthority(user.getUserType());
		List<UserAuthority> userAuthoritys = this.userAuthorityDao.getUserAuthority(user.getId());
		Map<String, Authority> tempMaps = new HashMap<String, Authority>();
		for (Authority authority : authoritys) {
			for (UserTypeAuthority uta : userTypeAuthoritys) {
				if(authority.getId().equals(uta.getAuthority()))  tempMaps.put(authority.getId(), authority);
			}
		}
		for (UserAuthority ua : userAuthoritys) {
			if( tempMaps.containsKey(ua.getAuthority()) )  tempMaps.get(ua.getAuthority()).setChecked(true);
		}
		
		Map<String, Authority> menuMaps = new HashMap<String, Authority>();
		for (Authority authority : tempMaps.values()) {
			menuMaps.put(authority.getId(), authority);
			menuMaps = this.rebuild(menuMaps, authority, tempMaps);
		}
		List<Authority> dtos = new ArrayList<Authority>();
		for (Authority menu : menuMaps.values()) {
			if (menu.getParentId() == null || "".equals(menu.getParentId())) {
				dtos.add(menu);
			}
		}
		Collections.sort(dtos, new AuthorityComparator());
		return JsonUtil.toString(dtos);
	}
	
	
	
	private Map<String, UserAuthority> rebuild (Map<String, UserAuthority> parents, Map<String, Authority> map, Authority authority, User user) {
		//父亲权限
		UserAuthority userAuthority = new UserAuthority();
		userAuthority.setUser(user.getId());
		userAuthority.setAuthority(authority.getId());
		parents.put(userAuthority.getAuthority(), userAuthority);
		Authority parent = map.get(authority.getParentId());
		if(parent != null) {
			parents = rebuild(parents, map, parent, user);
		}
		return parents;
	}

	@Transactional
	public String saveUserAuthority(String[] authoritys) {
		User user = this.userDao.getUser(RequestUtil.getUsername());
		List<Authority> list = this.authorityDao.getAuthoritys();
		List<UserAuthority> userAuthoritys = this.userAuthorityDao.getUserAuthority(user.getId());
		Map<String, Authority> map = new HashMap<String, Authority>();
		for (Authority authority : list) {
			map.put(authority.getId(), authority);
		}
		
		for (UserAuthority userAuthority : userAuthoritys) {
			this.userAuthorityDao.deleteUserAuthority(userAuthority);
		}
		
		Map<String, UserAuthority> parents = new HashMap<String, UserAuthority>();
		for (int i = 0; i < authoritys.length; i++) {
			//当前权限
			Authority authority = map.get(authoritys[i]);
			UserAuthority userAuthority = new UserAuthority();
			userAuthority.setUser(user.getId());
			userAuthority.setAuthority(authority.getId());
			this.userAuthorityDao.saveUserAuthority(userAuthority);
			//父亲权限
			Authority parent = map.get(authority.getParentId());
			if(parent != null) {
				parents = rebuild(parents, map, parent, user);
			}
		}
		
		for (UserAuthority ua : parents.values()) {
			this.userAuthorityDao.saveUserAuthority(ua);
		}
		return JsonUtil.toRes("保存成功");
	}
	
	
}
