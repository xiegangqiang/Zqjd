package com.xysoft.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.admin.service.WxUserService;
import com.xysoft.common.ElementConst;
import com.xysoft.dao.WxUserDao;
import com.xysoft.dao.WxUserGroupDao;
import com.xysoft.entity.WxUser;
import com.xysoft.entity.WxUserGroup;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;
import com.xysoft.weixin.pojo.AccessToken;
import com.xysoft.weixin.util.WeixinCacheUtil;
import com.xysoft.weixin.util.WeixinUtil;

@Component
@SuppressWarnings("unchecked")
public class WxUserServiceImpl implements WxUserService{
	
	@Resource
	private WxUserDao wxUserDao;
	@Resource
	private WxUserGroupDao wxUserGroupDao;

	@Transactional(readOnly = true)
	public String getWxUsers(PageParam page, String nickname, String groupid, String[] subscribe_time, String sex) {
		Pager<WxUser> pager = this.wxUserDao.getWxUsers(page, nickname, groupid, subscribe_time, sex);
		return JsonUtil.toStringFromObject(pager);
	}
	
	@Transactional
	public String synchronizeWx() {
		int count = 0;
		if(NullUtils.isEmpty(ElementConst.Wx_AppId) && NullUtils.isEmpty(ElementConst.Wx_AppSecret)) {
			return JsonUtil.toRes("授权参数设置不正确");
		}else {
			List<WxUser> wxUsers = this.wxUserDao.getWxUsers();
			Map<String, WxUser> maps = new HashMap<String, WxUser>();
			for (WxUser wxUser : wxUsers) {
				if(!maps.containsKey(wxUser.getOpenid()) && NullUtils.isNotEmpty(wxUser.getOpenid())) {
					maps.put(wxUser.getOpenid(), wxUser);
				}
			}
			AccessToken accessToken = WeixinCacheUtil.getAccessToken(ElementConst.Wx_Token, ElementConst.Wx_AppId, ElementConst.Wx_AppSecret);
			if(accessToken == null){return JsonUtil.toRes("连接微信服务器失败，参数错误");}
			List<String> openids = WeixinUtil.getUserOpenids(accessToken.getToken(), null);
			if(openids == null){return JsonUtil.toRes("公众号还未获得相关接口，请到官方平台申请认证");} 
			for (String openid : openids) { 
				WxUser wxUser = null;
				if(maps.containsKey(openid)) {
					wxUser = maps.get(openid);
				}else {
					wxUser = new WxUser();
					 count++;
				}
				WxUser httpUser = WeixinUtil.getWxUser(accessToken.getToken(), openid);
				if(httpUser == null) continue;
				BeanUtils.copyProperties(httpUser, wxUser, new String[] {"id", "createDate", "nickname", "subscribe_time", "wx"});
				try {
					if(NullUtils.isNotEmpty(httpUser.getNickname())){wxUser.setNickname(new String(httpUser.getNickname().getBytes(), "GBK"));}
				} catch (Exception e) {
					e.printStackTrace();
				}
				String subscribe_time = null;
				if(NullUtils.isNotEmpty(httpUser.getSubscribe_time())){
					subscribe_time = String.format("%tF %<tT", Long.valueOf(httpUser.getSubscribe_time())*1000L);
				}
				wxUser.setSubscribe_time(subscribe_time);
				this.wxUserDao.saveWxUser(wxUser);
			}
		}
		return JsonUtil.toRes("成功同步" + count + "个粉丝");
	}
	
	@Transactional
	public String updateWxUser(WxUser param) {
		WxUser wxUser = this.wxUserDao.getWxUser(param.getId());
		if(wxUser != null){
			if(NullUtils.isEmpty(ElementConst.Wx_AppId) && NullUtils.isEmpty(ElementConst.Wx_AppSecret)){return JsonUtil.toRes("授权参数未设置");};
			AccessToken accessToken = WeixinCacheUtil.getAccessToken(ElementConst.Wx_Token, ElementConst.Wx_AppId, ElementConst.Wx_AppSecret);
			if(NullUtils.isNotEmpty(param.getNickname())) wxUser.setNickname(param.getNickname());
			if(NullUtils.isNotEmpty(param.getRemark())) wxUser.setRemark(param.getRemark());
			int result = WeixinUtil.updateWxUserRemark(wxUser.getOpenid(), param.getRemark(), accessToken.getToken());
			if(result == 0){
				this.wxUserDao.saveOrUpdate(wxUser);
			}
		}
		return JsonUtil.toRes("修改成功");
	}
	
	@Transactional
	public String getWxUserGroups() {
		List<WxUserGroup> wxUserGroups = this.wxUserGroupDao.getWxUserGroups();
		if(wxUserGroups.size() == 0){
			if(NullUtils.isEmpty(ElementConst.Wx_AppId) && NullUtils.isEmpty(ElementConst.Wx_AppSecret)){return JsonUtil.toRes("授权参数未设置");};
			AccessToken accessToken = WeixinCacheUtil.getAccessToken(ElementConst.Wx_Token, ElementConst.Wx_AppId, ElementConst.Wx_AppSecret);
			if(accessToken == null) return JsonUtil.toRes("授权参数设置不正确");
			wxUserGroups = WeixinUtil.getWxUserGroups(accessToken.getToken());
			for (WxUserGroup group : wxUserGroups) {
				this.wxUserGroupDao.saveWxUserGroup(group);
			}
		}
		return JsonUtil.toString(wxUserGroups);
	}
	
	@Transactional
	public String saveWxUserGroup(WxUserGroup param) {
		WxUserGroup wxUserGroup = null;
		if(NullUtils.isEmpty(ElementConst.Wx_AppId) && NullUtils.isEmpty(ElementConst.Wx_AppSecret)){return JsonUtil.toRes("授权参数未设置");};
		AccessToken accessToken = WeixinCacheUtil.getAccessToken(ElementConst.Wx_Token, ElementConst.Wx_AppId, ElementConst.Wx_AppSecret);
		if(NullUtils.isEmpty(param.getId())){
			wxUserGroup = new WxUserGroup();
			WxUserGroup httpGroup = WeixinUtil.createWxUserGroup(param.getName(), accessToken.getToken());
			wxUserGroup.setGroupId(httpGroup.getId());
			wxUserGroup.setName(httpGroup.getName());
		}else{
			wxUserGroup = this.wxUserGroupDao.getWxUserGroup(param.getId());
			int result = WeixinUtil.updateWxUserGroup(wxUserGroup.getGroupId(), param.getName(), accessToken.getToken());
			if(result == 0){
				wxUserGroup.setName(param.getName());
			}
		}
		this.wxUserGroupDao.saveOrUpdate(wxUserGroup);
		return JsonUtil.toRes("保存成功");
	}
	
	@Transactional
	public String deleteWxUserGroup(String id) {
		WxUserGroup wxUserGroup = this.wxUserGroupDao.getWxUserGroup(id);
		if(NullUtils.isEmpty(ElementConst.Wx_AppId) && NullUtils.isEmpty(ElementConst.Wx_AppSecret)){return JsonUtil.toRes("授权参数未设置");};
		AccessToken accessToken = WeixinCacheUtil.getAccessToken(ElementConst.Wx_Token, ElementConst.Wx_AppId, ElementConst.Wx_AppSecret);
		if(wxUserGroup != null) {
			int result = WeixinUtil.deleteWxUserGroup(wxUserGroup.getGroupId(), accessToken.getToken());
			if(result == 0) {
				this.wxUserGroupDao.deleteWxUserGroup(wxUserGroup);
				return JsonUtil.toRes("删除成功");
			}else{
				return JsonUtil.toRes("删除失败");
			}
		}
		return JsonUtil.toRes("删除成功");
	}
	
	@Transactional
	public String batchMoveGroup(String[] openid_list, String to_groupid) {
		if(NullUtils.isEmpty(ElementConst.Wx_AppId) && NullUtils.isEmpty(ElementConst.Wx_AppSecret)){return JsonUtil.toRes("授权参数未设置");};
		AccessToken accessToken = WeixinCacheUtil.getAccessToken(ElementConst.Wx_Token, ElementConst.Wx_AppId, ElementConst.Wx_AppSecret);
		int result = WeixinUtil.batchmoveWxUserGroup(JsonUtil.toString(openid_list), to_groupid, accessToken.getToken());
		if(result == 0){
			List<WxUser> wxUsers = this.wxUserDao.getWxUsers();
			for (WxUser user : wxUsers) {
				for (String openid : openid_list) {
					if(user.getOpenid().equals(openid)) {
						user.setGroupid(to_groupid);
						this.wxUserDao.saveWxUser(user);
					}
				}
			}
		}
		return JsonUtil.toRes("操作成功");
	}
	
	
	

}
