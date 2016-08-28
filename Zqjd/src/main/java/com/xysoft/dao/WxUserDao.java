package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.WxUser;
import com.xysoft.support.BaseDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;


public interface WxUserDao extends BaseDao<WxUser>{

	Pager<WxUser> getWxUsers(PageParam page, String nickname, String groupid, String[] subscribe_time, String sex);

	List<WxUser> getWxUsers();

	void saveWxUser(WxUser wxUser);

	WxUser getWxUser(String id);

	WxUser getWxUserByOpenId(String openid);

}
