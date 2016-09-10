package com.xysoft.admin.service;

import com.xysoft.entity.Evaluat;
import com.xysoft.support.PageParam;

public interface EvaluatService {

	String getEvaluats(PageParam page, String name);

	String saveEvaluat(Evaluat param);
	
	String deleteEvaluat(String id);


}
