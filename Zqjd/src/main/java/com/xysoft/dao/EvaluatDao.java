package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.Evaluat;
import com.xysoft.support.BaseDao;

public interface EvaluatDao extends BaseDao<Evaluat>{
	
	void saveEvaluat(Evaluat evaluat);

	void deleteEvaluat(Evaluat evaluat);

	List<Evaluat> getEvaluatByUser(String user);

	Evaluat getEvaluat(String id);
}
