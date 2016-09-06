package com.xysoft.admin.service.impl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.admin.service.ProductService;
import com.xysoft.dao.ProductClassDao;
import com.xysoft.dao.ProductDao;
import com.xysoft.entity.Product;
import com.xysoft.entity.ProductClass;
import com.xysoft.support.DynamicBean;
import com.xysoft.support.JdbcDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;
import com.xysoft.util.SqlUtil;

@Component
@SuppressWarnings("unchecked")
public class ProductServiceImpl implements ProductService{
	
	@Resource
	private ProductDao productDao;
	@Resource
	private ProductClassDao productClassDao;
	@Resource
	private JdbcDao<DynamicBean> jdbcDao;

	@Transactional(readOnly = true)
	public String getProducts(PageParam page, String name) {
		String sql = "SELECT pd.*,pdc.`name` AS productClassName FROM product pd JOIN productclass pdc ON pdc.id=pd.productClass WHERE pd.`name` LIKE ?";
		Pager<DynamicBean> beans = this.jdbcDao.queryForPager(sql, page, "%"+name+"%");
		Pager<Object> pager = new Pager<Object>();
		pager.setTotal(beans.getTotal());
		pager.setPageCount(beans.getPageCount());
		pager.setPageIndex(beans.getPageIndex());
		pager.setDatas(SqlUtil.DynamicToBean(beans.getDatas()));
		return JsonUtil.toStringFromObject(pager);
	}

	@Transactional
	public String saveProduct(Product param) {
		Product product = null;
		if(NullUtils.isEmpty(param.getId())){
			product = new Product();
		}else{
			product = this.productDao.getProduct(param.getId());
		}
		BeanUtils.copyProperties(param, product, new String[]{"id", "createDate"});
		this.productDao.saveProduct(product);
		return JsonUtil.toRes("保存成功");
	}
	
	@Transactional
	public String deleteProduct(String id) {
		Product product = this.productDao.getProduct(id);
		this.productDao.deleteProduct(product);		
		return JsonUtil.toRes("删除成功");
	}

	@Transactional(readOnly = true)
	public String getProductClasses() {
		List<ProductClass> productClasses = this.productClassDao.getProductClasses("");
		return JsonUtil.toString(productClasses);
	}
}
