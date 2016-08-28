package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.DiymenDao;
import com.xysoft.entity.Diymen;
import com.xysoft.support.BaseDaoImpl;

@Component
public class DiymenDaoImpl extends BaseDaoImpl<Diymen> implements DiymenDao {

	public List<Diymen> getDiymenes() {
		return this.find("from Diymen order by parentId asc, level desc, id asc");
	}
	
	public Diymen getDiymen(String id) {
		return this.get(Diymen.class, id);
	}
	
	public void saveDiymen(Diymen Diymen) {
		this.saveOrUpdate(Diymen);
	}

	public void deleteDiymen(Diymen Diymen) {
		this.delete(Diymen);
	}

	public List<Diymen> getDiymenNull() {
		return this.find("from Diymen where parentId is null and order by level desc, createDate asc");
	}

	public List<Diymen> getDiymenChildren(String parentId) {
		return this.find("from Diymen where parentId is ?", parentId);
	}
	
}
