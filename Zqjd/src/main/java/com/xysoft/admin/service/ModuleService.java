package com.xysoft.admin.service;

import com.xysoft.entity.Module;
import com.xysoft.support.PageParam;

public interface ModuleService {

	String getModules(PageParam page, String name);

	String saveModule(Module param, String[] roles);

	String deleteModule(String id);

	String getAllActiveRoles(String moduleId);

	String getAllActiveModule();

}
