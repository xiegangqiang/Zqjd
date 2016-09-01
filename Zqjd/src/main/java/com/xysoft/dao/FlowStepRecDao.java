package com.xysoft.dao;

import com.xysoft.entity.FlowStepRec;
import com.xysoft.support.BaseDao;

public interface FlowStepRecDao extends BaseDao<FlowStepRec>{

	FlowStepRec getCurrentStep(String id, String orderId);

	void saveFlowStepRec(FlowStepRec flowStepRec);

}
