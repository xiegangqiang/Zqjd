package com.xysoft.tag.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xysoft.support.DataPager;
import com.xysoft.support.DynamicBean;
import com.xysoft.support.JdbcDao;
import com.xysoft.support.Pager;
import com.xysoft.tag.service.ProductTagService;
import com.xysoft.util.NullUtils;
import com.xysoft.util.SqlUtil;

@Component
public class ProductTagServiceImpl implements ProductTagService {
	
	@Resource
	private JdbcDao<DynamicBean> jdbcDao;

	@Transactional(readOnly = true)
	public DataPager<Object> getProducts(String productClass, Integer page, Integer count, String name) {
		Pager<DynamicBean>  productsPager = null;
		String sql = "SELECT pd.*,pdc.`name` AS productClassName FROM product pd JOIN productclass pdc ON pdc.id=pd.productClass WHERE pd.`name` LIKE "+"'%"+name+"%'";
		if(NullUtils.isNotEmpty(productClass)) {
			 sql += " WHERE pd.productClass = "+productClass;
		}
		sql += "ORDER BY level desc";
		productsPager = this.jdbcDao.queryForPager(sql, page, count);
		DataPager<Object> pager = new DataPager<Object>(SqlUtil.DynamicToBean(productsPager.getDatas()), productsPager.getTotal());
		return pager;
	}

}
