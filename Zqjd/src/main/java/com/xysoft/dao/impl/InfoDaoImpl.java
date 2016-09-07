package com.xysoft.dao.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.xysoft.dao.InfoDao;
import com.xysoft.entity.Info;
import com.xysoft.support.BaseDaoImpl;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

@Component
public class InfoDaoImpl extends BaseDaoImpl<Info> implements InfoDao{
	
	public Pager<Info> getInfos(PageParam page, String name) {
		return this.getForPager("from Info where name like ? order by level, id desc", page, "%" + name + "%");
	}

	public Info getInfo(String id) {
		return this.get(Info.class, id);
	}

	@CacheEvict(value = "XY_INFO", allEntries = true)
	public void saveInfo(Info info) {
		this.saveOrUpdate(info);
	}

	@CacheEvict(value = "XY_INFO", allEntries = true)
	public void deleteInfo(Info info) {
		this.delete(info);
	}
}
