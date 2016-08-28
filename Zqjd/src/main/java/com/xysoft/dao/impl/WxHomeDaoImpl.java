package com.xysoft.dao.impl;

import org.springframework.stereotype.Component;

import com.xysoft.dao.WxHomeDao;
import com.xysoft.entity.Home;
import com.xysoft.support.BaseDaoImpl;

@Component
public class WxHomeDaoImpl extends BaseDaoImpl<Home> implements WxHomeDao {

	public Home getHome() {
		return this.get("from Home");
	}

	public void saveHome(Home home) {
		this.save(home);
	}

	public void deleteHome(Home home) {
		this.delete(home);
	}
}
