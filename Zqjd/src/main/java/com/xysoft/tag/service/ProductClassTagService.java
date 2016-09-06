package com.xysoft.tag.service;

import com.xysoft.support.DataPager;

public interface ProductClassTagService {

	DataPager<Object> getProductClasses(Integer page, Integer count);

}
