package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.ModuleDao;
import com.xysoft.entity.Module;
import com.xysoft.support.BaseDaoImpl;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

@Component
public class ModuleDaoImpl extends BaseDaoImpl<Module> implements ModuleDao{

	public Pager<Module> getModules(PageParam page, String name) {
		return this.getForPager("from Module where name like ? order by level asc", page, "%" + name + "%");
	}
	
	public Module getModule(String id) {
		return this.get(Module.class, id);
	}
	
	public void saveModule(Module module) {
		this.saveOrUpdate(module);
	}

	public void deleteModule(Module module) {
		this.delete(module);
	}
	
	public List<Module> getModules() {
		return this.find("from Module order by level asc");
	}
}
