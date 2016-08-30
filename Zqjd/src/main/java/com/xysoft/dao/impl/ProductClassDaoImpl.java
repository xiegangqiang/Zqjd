package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.ProductClassDao;
import com.xysoft.entity.ProductClass;
import com.xysoft.support.BaseDaoImpl;
@Component
public class ProductClassDaoImpl extends BaseDaoImpl<ProductClass> implements ProductClassDao{

	public List<ProductClass> getProductClasses(String name) {
		return this.find("from ProductClass where name like ? order by level asc", "%" + name + "%");
	}

	public ProductClass getProductClass(String id) {
		return this.get(ProductClass.class, id);
	}

	public void saveProductClass(ProductClass productClass) {
		this.saveOrUpdate(productClass);
	}

	public void delectProductClass(ProductClass productClass) {
		this.delete(productClass);
	}

}
