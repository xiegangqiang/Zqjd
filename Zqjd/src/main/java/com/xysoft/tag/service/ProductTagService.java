package com.xysoft.tag.service;

import com.xysoft.support.DataPager;

public interface ProductTagService {

	DataPager<Object> getProducts(String productClass, Integer page, Integer count, String name);

}
