package com.xysoft.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xysoft.admin.service.ProductClassService;
import com.xysoft.dao.ProductClassDao;
import com.xysoft.dao.ProductDao;
import com.xysoft.entity.Product;
import com.xysoft.entity.ProductClass;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;

@Component
@SuppressWarnings("unchecked")
public class ProductClassServiceImpl implements ProductClassService {

	@Resource
	private ProductClassDao productClassDao;
	@Resource
	private ProductDao productDao;
	
	@Transactional(readOnly = true)
	public String getProductClasses(String name) {
		List<ProductClass> productClasses = this.productClassDao.getProductClasses(name);
		Map<String, List<ProductClass>> maps = new HashMap<String, List<ProductClass>>();
		for(ProductClass productClass : productClasses){
			if(NullUtils.isEmpty(productClass.getParentId())) continue;
			List<ProductClass> hms = null;
			if(maps.containsKey(productClass.getParentId())){
				hms = maps.get(productClass.getParentId());
			}else{
				hms = new ArrayList<ProductClass>();
			}
			hms.add(productClass);
			maps.put(productClass.getParentId(), hms);
		}
		List<ProductClass> hms =  new ArrayList<ProductClass>();
		for(ProductClass productClass : productClasses){
			if(NullUtils.isEmpty(productClass.getParentId())){
				hms.add(rebuildClass(hms, maps, productClass));
			}
		}
		return JsonUtil.toString(hms, "check");
	}
	
	/**
	 * 递归查找子分类节点.
	 */
	private ProductClass rebuildClass(List<ProductClass> hms, Map<String, List<ProductClass>> maps, ProductClass productClass) {
		if(!maps.containsKey(productClass.getId())) return productClass;
		List<ProductClass> list = maps.get(productClass.getId());
		for(ProductClass ls : list){
			productClass.setExpanded(true);
			productClass.getChildren().add(rebuildClass(hms, maps, ls));
		}
		return productClass;
	}
	
	private List<ProductClass> subClass(Map<String, ProductClass> maps, List<ProductClass> hms, List<ProductClass> children){
		for(ProductClass productClass : children){
			productClass.setName("　" + productClass.getName());
			hms.add(productClass);
			List<ProductClass> childrens = new ArrayList<ProductClass>(); 
			for (Entry<String, ProductClass> entry : maps.entrySet()) {
				if( productClass.getId().equals( entry.getKey() ) ){
					childrens.add(entry.getValue());
				}
			}
			if(childrens.size() > 0){
				subClass(maps, hms, childrens);
			}
		}
		return hms;
	}

	@Transactional(readOnly = true)
	public String getProductClassNull(String name) {
		List<ProductClass> productClasses = this.productClassDao.getProductClasses(name.replaceAll("　", ""));
		List<ProductClass> hms = new ArrayList<ProductClass>(); 
		ProductClass pdc = new ProductClass();
		pdc.setName("没有上一级");
		hms.add(pdc);
		
		Map<String, ProductClass> maps = new IdentityHashMap<String, ProductClass>();
		for(ProductClass productClass : productClasses){
			if(NullUtils.isNotEmpty( productClass.getParentId() )){
				maps.put(productClass.getParentId(), productClass);
			}
		}
		for (ProductClass space : productClasses) {
			if(NullUtils.isEmpty(space.getParentId())){
				hms.add(space);
			}
			List<ProductClass> children = new ArrayList<ProductClass>(); 
			for (Entry<String, ProductClass> entry : maps.entrySet()) {
				if( space.getId().equals( entry.getKey() ) ){
					children.add(entry.getValue());
				}
			}
			if(children.size() > 0){
				subClass(maps, hms, children);
			}
		}
		return JsonUtil.toString(hms);
	}

	@Transactional
	public String saveProductClass(ProductClass param) {
		ProductClass productClass = null;
		if(NullUtils.isEmpty(param.getId())) {
			productClass = new ProductClass();
		}else {
			productClass = this.productClassDao.getProductClass(param.getId());
		}
		BeanUtils.copyProperties(param, productClass, new String[] {"id", "createDate"});
		this.productClassDao.saveProductClass(productClass);
		return JsonUtil.toRes("保存成功");
	}

	@Transactional
	public String deleteProductClass(String id) {
		ProductClass productClass = this.productClassDao.getProductClass(id);
		this.productClassDao.delectProductClass(productClass);
		List<Product> products = this.productDao.getProductsByProductClass(productClass.getId());
		for (Product product : products) {
			this.productDao.deleteProduct(product);
		}
		return JsonUtil.toRes("删除成功");
	}

}
