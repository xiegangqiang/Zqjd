package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.Product;
import com.xysoft.support.BaseDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

public interface ProductDao extends BaseDao<Product> {
	
	Pager<Product> getProducts(PageParam page, String name);

	Product getProduct(String id);

	void saveProduct(Product product);

	void deleteProduct(Product product);

	List<Product> getProductsByProductClass(String productClass);
}
