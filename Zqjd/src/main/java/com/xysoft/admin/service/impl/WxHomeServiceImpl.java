package com.xysoft.admin.service.impl;

import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.admin.service.WxHomeService;
import com.xysoft.dao.WxHomeDao;
import com.xysoft.entity.Home;
import com.xysoft.util.JsonUtil;

@SuppressWarnings("unchecked")
@Component
public class WxHomeServiceImpl implements WxHomeService {
	@Resource
	private WxHomeDao homeDao;
	
	@Transactional(readOnly = true)
	public String getHome() {
		Home home = this.homeDao.getHome();
		return JsonUtil.toStringFromObject(home);
	}
	
	@Transactional
	public String saveHome(Home param) {
		Home home = this.homeDao.getHome();
		if (home == null) {
			home = new Home();
		}
		BeanUtils.copyProperties(param, home, new String[]{"id"});
		this.homeDao.saveHome(home);
		return JsonUtil.toRes("保存成功");
	}
	
}
