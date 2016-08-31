package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.Module;
import com.xysoft.support.BaseDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

public interface ModuleDao extends BaseDao<Module>{

	Pager<Module> getModules(PageParam page, String name);

	Module getModule(String id);

	void saveModule(Module module);

	void deleteModule(Module module);

	List<Module> getModules();

}
