package com.xysoft.admin.service;

import com.xysoft.entity.Product;
import com.xysoft.support.PageParam;

public interface ProductService {
	
	String getProducts(PageParam page, String name);

	String saveProduct(Product param);

	String deleteProduct(String id);

	String getProductClasses();
}
