package com.xysoft.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xysoft.admin.service.EvaluatService;
import com.xysoft.dao.EvaluatDao;
import com.xysoft.entity.Evaluat;
import com.xysoft.support.DynamicBean;
import com.xysoft.support.JdbcDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;
import com.xysoft.util.SqlUtil;

@Component
@SuppressWarnings("unchecked")
public class EvaluatServiceImpl implements EvaluatService{
	
	@Resource
	private EvaluatDao evaluatDao;
	@Resource
	private JdbcDao<DynamicBean> jdDao;

	@Transactional(readOnly = true)
	public String getEvaluats(PageParam page, String name) {
		String sql = "SELECT o.ordernumber,u.`name` AS `username`,u.phone,r.`name` AS role,el.* FROM evaluat el LEFT JOIN `user` u ON u.id=el.`user` JOIN orders o ON o.id=el.orders LEFT JOIN flowsteprecpost flp ON flp.id=el.flowStepRecPost LEFT JOIN role r ON r.id=flp.posts ORDER BY type,createDate ASC;";
		Pager<DynamicBean> beans = this.jdDao.queryForPager(sql, page);
		Pager<Object> pager = new Pager<Object>();
		pager.setDatas(SqlUtil.DynamicToBean(beans.getDatas()));
		pager.setPageCount(beans.getPageCount());
		pager.setPageIndex(beans.getPageIndex());
		pager.setTotal(beans.getTotal());
		return JsonUtil.toStringFromObject(pager);
	}
	
	@Transactional
	public String saveEvaluat(Evaluat param) {
		Evaluat evaluat = null;
		if(NullUtils.isEmpty(param.getId())) {
			evaluat = new Evaluat();
		}else {
			evaluat = this.evaluatDao.getEvaluat(param.getId());
		}
		BeanUtils.copyProperties(param, evaluat, new String[]{"id", "createDate"});
		this.evaluatDao.saveEvaluat(evaluat);
		return JsonUtil.toRes("保存成功");
	}

	@Transactional
	public String deleteEvaluat(String id) {
		Evaluat evaluat = this.evaluatDao.getEvaluat(id);
		this.evaluatDao.deleteEvaluat(evaluat);
		return JsonUtil.toRes("删除成功");
	}

}
