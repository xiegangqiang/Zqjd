package com.xysoft.admin.service;

import com.xysoft.entity.Info;
import com.xysoft.support.PageParam;

public interface InfoService {

	String getInfos(PageParam page, String name);

	String saveInfo(Info param);

	String deleteInfo(String id);
}
