package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.WxUserGroup;
import com.xysoft.support.BaseDao;

public interface WxUserGroupDao extends BaseDao<WxUserGroup>{

	List<WxUserGroup> getWxUserGroups();
	
	void saveWxUserGroup(WxUserGroup wxUserGroup);
	
	void deleteWxUserGroup(WxUserGroup wxUserGroup);

	WxUserGroup getWxUserGroup(String id);

}
