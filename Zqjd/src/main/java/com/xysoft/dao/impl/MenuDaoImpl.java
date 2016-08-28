package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.MenuDao;
import com.xysoft.entity.Menu;
import com.xysoft.support.BaseDaoImpl;
import com.xysoft.util.StringUtil;

@Component
public class MenuDaoImpl extends BaseDaoImpl<Menu> implements MenuDao {

	public List<Menu> getMenus(String module) {
		if(module == null){
			return this.find("from Menu where visible = true order by leaf asc, level asc");
		}else {
			String[] modules = module.split(",");
			return this.find("from Menu where visible = true and module in "+ StringUtil.toSQLIn(modules) +" order by leaf asc, level asc");
		}
	}

	public List<Menu> getMenusAction() {
		return this.find("from Menu where visible = true and leaf = 1 order by leaf asc");
	}
}
