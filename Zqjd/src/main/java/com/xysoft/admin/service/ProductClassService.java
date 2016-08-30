package com.xysoft.admin.service;

import com.xysoft.entity.ProductClass;

public interface ProductClassService {

	String getProductClasses(String name);

	String getProductClassNull(String name);

	String saveProductClass(ProductClass param);

	String deleteProductClass(String id);

}
