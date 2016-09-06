package com.xysoft.dao;

import com.xysoft.entity.Info;
import com.xysoft.support.BaseDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

public interface InfoDao extends BaseDao<Info>{
	
	Pager<Info> getInfos(PageParam page, String name);

	Info getInfo(String id);

	void saveInfo(Info info);

	void deleteInfo(Info info);
}
