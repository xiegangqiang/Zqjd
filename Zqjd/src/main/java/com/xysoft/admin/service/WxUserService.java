package com.xysoft.admin.service;

import com.xysoft.entity.WxUser;
import com.xysoft.entity.WxUserGroup;
import com.xysoft.support.PageParam;

public interface WxUserService {

	String getWxUsers(PageParam page, String nickname, String groupid, String[] subscribe_time,
			String sex);

	String synchronizeWx();

	String updateWxUser(WxUser param);

	String getWxUserGroups();

	String saveWxUserGroup(WxUserGroup param);

	String deleteWxUserGroup(String id);

	String batchMoveGroup(String[] openid_list, String to_groupid);
}
