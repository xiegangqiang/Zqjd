package com.xysoft.dao.impl;

import org.springframework.stereotype.Component;
import com.xysoft.dao.FlowStepRecDao;
import com.xysoft.entity.FlowStepRec;
import com.xysoft.support.BaseDaoImpl;

@Component
public class FlowStepRecDaoImpl extends BaseDaoImpl<FlowStepRec> implements FlowStepRecDao{

	public FlowStepRec getCurrentStep(String id, String orderId) {
		return this.get("from FlowStepRec where flowstep = ? and orders = ?", id, orderId);
	}

	public void saveFlowStepRec(FlowStepRec flowStepRec) {
		this.saveOrUpdate(flowStepRec);
	}

}
