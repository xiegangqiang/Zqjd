package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.WxUserDao;
import com.xysoft.entity.WxUser;
import com.xysoft.support.BaseDaoImpl;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;


@Component
public class WxUserDaoImpl extends BaseDaoImpl<WxUser> implements WxUserDao{

	public Pager<WxUser> getWxUsers(PageParam page, String nickname, String groupid, String[] subscribe_time, String sex) {
		String hql = "from WxUser where wx = ? and nickname like ? and groupid like ? ";
		if(subscribe_time.length != 0 && !subscribe_time[0].equals("") && !subscribe_time[1].equals("")) {
			hql += " and DATE_FORMAT(subscribe_time, '%Y-%m-%d') >= ? and DATE_FORMAT(subscribe_time, '%Y-%m-%d') <= ?";
			if(!"".equals(sex)) {
				hql += " and sex=?";
				return this.getForPager(hql, page, "%" + nickname + "%", "%" + groupid + "%", subscribe_time[0], subscribe_time[1], sex);
			}else return this.getForPager(hql, page, "%" + nickname + "%", "%" + groupid + "%", subscribe_time[0], subscribe_time[1]);
		}
		if(!"".equals(sex)) {
			hql += " and sex=? order by createDate desc";
			return this.getForPager(hql, page, "%" + nickname + "%", "%" + groupid + "%", sex);
		}else {
			hql += " order by createDate desc";
			return this.getForPager(hql, page, "%" + nickname + "%", "%" + groupid + "%");
		}
	}

	public List<WxUser> getWxUsers() {
		return this.find("from WxUser");
	}

	public void saveWxUser(WxUser wxUser) {
		this.saveOrUpdate(wxUser);
	}
	
	public WxUser getWxUser(String id) {
		return this.get(WxUser.class, id);
	}
	
	public WxUser getWxUserByOpenId(String fromUserName) {
		return this.get("from WxUser where openid=?", fromUserName);
	}
}
