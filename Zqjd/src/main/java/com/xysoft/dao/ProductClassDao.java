package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.ProductClass;
import com.xysoft.support.BaseDao;

public interface ProductClassDao extends BaseDao<ProductClass>{

	List<ProductClass> getProductClasses(String name);

	ProductClass getProductClass(String id);

	void saveProductClass(ProductClass productClass);

	void delectProductClass(ProductClass productClass);

}
