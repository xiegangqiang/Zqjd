package com.xysoft.tag.service;

import com.xysoft.support.DataPager;

public interface DynamicBeanTagService {

	DataPager<Object> getDynamicBeans(String sql, String table, String where, Integer page, Integer count);

}
