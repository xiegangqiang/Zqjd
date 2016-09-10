package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import com.xysoft.dao.EvaluatDao;
import com.xysoft.entity.Evaluat;
import com.xysoft.support.BaseDaoImpl;

@Component
public class EvaluatDaoImpl extends BaseDaoImpl<Evaluat> implements EvaluatDao{

	public void saveEvaluat(Evaluat evaluat) {
		this.saveOrUpdate(evaluat);
	}

	public void deleteEvaluat(Evaluat evaluat) {
		this.delete(evaluat);
	}

	public List<Evaluat> getEvaluatByUser(String user) {
		return this.find("from Evaluat where user = ?", user);
	}

	public Evaluat getEvaluat(String id) {
		return this.get(Evaluat.class, id);
	}

}
