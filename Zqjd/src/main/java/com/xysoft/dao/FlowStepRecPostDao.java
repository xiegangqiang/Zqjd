package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.FlowStepRecPost;
import com.xysoft.support.BaseDao;

public interface FlowStepRecPostDao extends BaseDao<FlowStepRecPost>{

	List<FlowStepRecPost> getFlowStepRecPostByFlowStepRec(String flowStepRec);

	void saveFlowStepRecPost(FlowStepRecPost flowStepRecPost);

	void deleteFlowStepRecPost(FlowStepRecPost flowStepRecPost);

}
