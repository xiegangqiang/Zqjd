package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.WxUserGroupDao;
import com.xysoft.entity.WxUserGroup;
import com.xysoft.support.BaseDaoImpl;

@Component
public class WxUserGroupDaoImpl extends BaseDaoImpl<WxUserGroup> implements WxUserGroupDao{

	public List<WxUserGroup> getWxUserGroups() {
		return this.find("from WxUserGroup order by groupId asc");
	}

	public void saveWxUserGroup(WxUserGroup wxUserGroup) {
		this.saveOrUpdate(wxUserGroup);
	}

	public void deleteWxUserGroup(WxUserGroup wxUserGroup) {
		this.delete(wxUserGroup);
	}
	
	public WxUserGroup getWxUserGroup(String id) {
		return this.get(WxUserGroup.class, id);
	}

}
