package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.xysoft.dao.ProductDao;
import com.xysoft.entity.Product;
import com.xysoft.support.BaseDaoImpl;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

@Component
public class ProductDaoImpl extends BaseDaoImpl<Product> implements ProductDao {
	
	public Pager<Product> getProducts(PageParam page, String name) {
		return this.getForPager("from Product where name like ? order by level, createDate", page, "%" + name + "%"); 
	}

	public Product getProduct(String id) {
		return this.get(Product.class, id);
	}

	@CacheEvict(value = "XY_PRODUCT_INFO", allEntries = true)
	public void saveProduct(Product product) {
		this.saveOrUpdate(product);
	}

	@CacheEvict(value = "XY_PRODUCT_INFO", allEntries = true)
	public void deleteProduct(Product product) {
		this.delete(product);
	}

	public List<Product> getProductsByProductClass(String productClass) {
		return this.find("from Product where productClass is ?", productClass);
	}



}
