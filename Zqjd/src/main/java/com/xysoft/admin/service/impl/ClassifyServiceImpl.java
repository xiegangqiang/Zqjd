package com.xysoft.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.admin.service.ClassifyService;
import com.xysoft.dao.ClassifyDao;
import com.xysoft.entity.Classify;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.util.JsonUtil;

@SuppressWarnings("unchecked")
@Component
public class ClassifyServiceImpl implements ClassifyService {
	@Resource
	private ClassifyDao classifyDao;
	
	@Transactional(readOnly = true)
	public String getClassifys(PageParam page, String name) {
		Pager<Classify> pager = this.classifyDao.getClassifys(page, name);
		return JsonUtil.toStringFromObject(pager);
	}

	@Transactional
	public String saveClassify(Classify param) {
		Classify classify = null;
		if (!"".equals(param.getId())) {
			classify = this.classifyDao.getClassify(param.getId());
		} else {
			classify = new Classify();
		}
		BeanUtils.copyProperties(param, classify, new String[]{"id","createDate"});
		this.classifyDao.saveClassify(classify);
		return JsonUtil.toRes("保存成功");
	}

	@Transactional
	public String deleteClassify(String id) {
		Classify classify = this.classifyDao.getClassify(id);
		this.classifyDao.deleteClassify(classify);
		return JsonUtil.toRes("删除成功");
	}
	
}
