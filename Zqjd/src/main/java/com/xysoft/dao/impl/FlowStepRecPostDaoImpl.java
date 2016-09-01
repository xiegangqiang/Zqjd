package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.FlowStepRecPostDao;
import com.xysoft.entity.FlowStepRecPost;
import com.xysoft.support.BaseDaoImpl;

@Component
public class FlowStepRecPostDaoImpl extends BaseDaoImpl<FlowStepRecPost> implements FlowStepRecPostDao{

	public List<FlowStepRecPost> getFlowStepRecPostByFlowStepRec(String flowStepRec) {
		return this.find("from FlowStepRecPost where flowStepRec = ?", flowStepRec);
	}

	public void saveFlowStepRecPost(FlowStepRecPost flowStepRecPost) {
		this.saveOrUpdate(flowStepRecPost);
	}

	public void deleteFlowStepRecPost(FlowStepRecPost flowStepRecPost) {
		this.delete(flowStepRecPost);
	}

}
