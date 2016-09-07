package com.xysoft.admin.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xysoft.admin.service.InfoService;
import com.xysoft.dao.InfoDao;
import com.xysoft.entity.Info;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;

@SuppressWarnings("unchecked")
@Component
public class InfoServiceImpl implements InfoService {
	
	@Resource
	private InfoDao infoDao;

	@Transactional(readOnly = true)
	public String getInfos(PageParam page, String name) {
		Pager<Info> pager = this.infoDao.getInfos(page, name);
		return JsonUtil.toStringFromObject(pager);
	}

	@Transactional
	public String saveInfo(Info param) {
		Info info = null;
		if(NullUtils.isEmpty(param.getId())){
			info = new Info();
		}else{
			info = this.infoDao.getInfo(param.getId());
		}
		BeanUtils.copyProperties(param, info, new String[]{"id","createDate", "markcode"});
		this.infoDao.saveInfo(info);
		return JsonUtil.toRes("保存成功");
	}

	@Transactional
	public String deleteInfo(String id) {
		Info info = this.infoDao.getInfo(id);
		this.infoDao.deleteInfo(info);
		return JsonUtil.toRes("删除成功");
	}
}
