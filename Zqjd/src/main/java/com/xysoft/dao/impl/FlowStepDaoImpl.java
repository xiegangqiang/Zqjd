package com.xysoft.dao.impl;

import org.springframework.stereotype.Component;
import com.xysoft.dao.FlowStepDao;
import com.xysoft.entity.FlowStep;
import com.xysoft.support.BaseDaoImpl;

@Component
public class FlowStepDaoImpl extends BaseDaoImpl<FlowStep> implements FlowStepDao{

	public FlowStep getLastStep() {
		return this.get("from FlowStep where nextstep is null");
	}

	public FlowStep getFlowStep(String id) {
		return this.get(FlowStep.class, id);
	}

}
