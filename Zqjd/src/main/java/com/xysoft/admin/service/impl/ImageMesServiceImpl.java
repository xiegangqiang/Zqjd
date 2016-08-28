package com.xysoft.admin.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.admin.service.ImageMesService;
import com.xysoft.dao.ClassifyDao;
import com.xysoft.dao.ImageMesClassifyVDao;
import com.xysoft.dao.ImageMesDao;
import com.xysoft.entity.Classify;
import com.xysoft.entity.ImageMes;
import com.xysoft.view.ImageMesClassifyV;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.util.JsonUtil;

@SuppressWarnings("unchecked")
@Component
public class ImageMesServiceImpl implements ImageMesService {
	@Resource
	private ImageMesDao imageMesDao;
	@Resource
	private ClassifyDao classifyDao;
	@Resource
	private ImageMesClassifyVDao imageMesClassifyVDao;
	
	@Transactional(readOnly = true)
	public String getImageMess(PageParam page, String name) {
		Pager<ImageMesClassifyV> pager = this.imageMesClassifyVDao.getImageMesClassifyVs(page, name);
		return JsonUtil.toStringFromObject(pager);
	}

	@Transactional
	public String saveImageMes(ImageMes param) {
		ImageMes imageMes = null;
		if (!"".equals(param.getId())) {
			imageMes = this.imageMesDao.getImageMes(param.getId());
		} else {
			imageMes = new ImageMes();
		}
		BeanUtils.copyProperties(param, imageMes, new String[]{"id", "viewcount"});
		this.imageMesDao.saveImageMes(imageMes);
		return JsonUtil.toRes("保存成功");
	}

	@Transactional
	public String deleteImageMes(String id) {
		ImageMes imageMes = this.imageMesDao.getImageMes(id);
		this.imageMesDao.deleteImageMes(imageMes);
		return JsonUtil.toRes("删除成功");
	}

	@Transactional(readOnly = true)
	public String getClassifys() {
		List<Classify> classfiys = this.classifyDao.getClassifys();
		return JsonUtil.toString(classfiys);
	}
	
}
