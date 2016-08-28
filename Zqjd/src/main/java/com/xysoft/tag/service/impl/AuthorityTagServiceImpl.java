package com.xysoft.tag.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.comparator.AuthorityComparator;
import com.xysoft.dao.AuthorityDao;
import com.xysoft.entity.Authority;
import com.xysoft.tag.service.AuthorityTagService;
import com.xysoft.util.RequestUtil;

@Component
public class AuthorityTagServiceImpl implements AuthorityTagService{
	
	@Resource
	private AuthorityDao authorityDao;

	@Transactional(readOnly = true)
	@Cacheable(value = "XY_AUTHORITY_INFO")
	public List<Authority> getAuthorityAction() {
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
		return dtos;
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
