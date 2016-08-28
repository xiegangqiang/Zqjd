package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.ClassifyDao;
import com.xysoft.entity.Classify;
import com.xysoft.support.BaseDaoImpl;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

@Component
public class ClassifyDaoImpl extends BaseDaoImpl<Classify> implements ClassifyDao {

	public Pager<Classify> getClassifys(PageParam page, String name) {
		return this.getForPager("from Classify where name like ? order by level asc, id asc", 
				page, "%" + name + "%");
	}
	
	public Classify getClassify(String id) {
		return this.get(Classify.class, id);
	}

	public void saveClassify(Classify classify) {
		this.save(classify);
	}

	public void deleteClassify(Classify classify) {
		this.delete(classify);
	}

	public List<Classify> getClassifyByMarkcode(String markcode) {
		return this.find("from Classify where markcode = ? order by level desc, id asc", markcode);
	}
	
	public Classify getClassifyCache(String id) {
		return this.get(Classify.class, id);
	}
	
	public List<Classify> getClassifys() {
		return this.find("from Classify order by level desc, id asc");
	}

	public List<Classify> getClassifys(Boolean visible) {
		return this.find("from Classify where visible = ? order by level desc, id asc", visible);
	}
	
}
