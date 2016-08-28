package com.xysoft.front.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.front.service.FrontService;

@Component
public class FrontServiceImpl implements FrontService {
	
	@Transactional(readOnly = true)
	public Map<String, Object> error() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("model", "front/error");
		return model;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> error404() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("model", "front/404");
		return model;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> error500() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("model", "front/500");
		return model;
	}
	
	@Transactional(readOnly = true)
	public Map<String, Object> login() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("model", "client/login");
		return model;
	}

	@Transactional(readOnly = true)
	public Map<String, Object> index() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("model", "front/index");
		return model;
	}

	
	

}



