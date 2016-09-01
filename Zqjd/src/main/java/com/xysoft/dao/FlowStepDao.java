package com.xysoft.dao;

import com.xysoft.entity.FlowStep;
import com.xysoft.support.BaseDao;

public interface FlowStepDao extends BaseDao<FlowStep>{

	FlowStep getLastStep();

	FlowStep getFlowStep(String id);

}
