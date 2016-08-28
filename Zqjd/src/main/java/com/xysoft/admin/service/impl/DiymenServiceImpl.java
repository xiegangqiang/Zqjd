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
import com.xysoft.admin.service.DiymenService;
import com.xysoft.dao.DiymenDao;
import com.xysoft.entity.Diymen;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;
import com.xysoft.weixin.menu.MenuManager;

@SuppressWarnings("unchecked")
@Component
public class DiymenServiceImpl implements DiymenService {
	@Resource
	private DiymenDao diymenDao;
	@Resource
	private MenuManager menuManager;
	
	@Transactional(readOnly = true)
	public String getDiymenes() {
		List<Diymen> diymenes = this.diymenDao.getDiymenes();
		Map<String, List<Diymen>> maps = new HashMap<String, List<Diymen>>();
		for (Diymen diymen : diymenes) {
			if (NullUtils.isEmpty(diymen.getParentId())) continue;
			List<Diymen> hms = null;
			if (maps.containsKey(diymen.getParentId())) {
				hms = maps.get(diymen.getParentId());
			} else {
				hms = new ArrayList<Diymen>();
			}
			hms.add(diymen);
			maps.put(diymen.getParentId(), hms);
		}
		List<Diymen> hms = new ArrayList<Diymen>(); 
		for (Diymen diymen : diymenes) {
			if (NullUtils.isEmpty(diymen.getParentId())) {
				hms.add(rebuildClass(hms, maps, diymen));
			}
		}
		return JsonUtil.toString(hms, "checked");
	}
	
	@Transactional(readOnly = true)
	public String getDiymenesNull() {
		List<Diymen> diymenes = this.diymenDao.getDiymenes();
		IdentityHashMap<String, Diymen> maps = new IdentityHashMap<String, Diymen>();
		List<Diymen> parents = new ArrayList<Diymen>();
		Diymen diymen = new Diymen();
		diymen.setId("");
		diymen.setName("没有上一级菜单");
		parents.add(0, diymen);
		for (Diymen men : diymenes) {
			if(NullUtils.isEmpty(men.getParentId())) {
				parents.add(men);
			}else{
				maps.put(men.getParentId(), men);
			}
		}
		//List<Diymen> parents = this.diymenDao.getDiymenNull(RequestUtil.getWeiXinId());
		for (Diymen men : parents) {
			for (Entry<String, Diymen> entry : maps.entrySet()) {
				if( men.getId().equals( entry.getKey() ) ){
					men.getChildren().add(entry.getValue());
				}
			}
		}
		return JsonUtil.toString(parents);
	}

	/**
	 * 递归查找子分类节点.
	 */
	private Diymen rebuildClass(List<Diymen> hms, Map<String, List<Diymen>> maps, Diymen diymen) {
		if (!maps.containsKey(diymen.getId())) return diymen;
		List<Diymen> lst = maps.get(diymen.getId()); 
		for (Diymen ls : lst) {
			diymen.setExpanded(true);
			diymen.getChildren().add(rebuildClass(hms, maps, ls));
		}
		return diymen;
	}
	
	@Transactional
	public String saveDiymen(Diymen param) {
		Diymen diymen = null;
		if (NullUtils.isNotEmpty(param.getId())) {
			diymen = this.diymenDao.getDiymen(param.getId());
		} else {
			diymen = new Diymen();
		}
		BeanUtils.copyProperties(param, diymen, new String[]{"id", "createDate"});
		diymen.setParentId("".equals(diymen.getParentId())? null : diymen.getParentId());
		this.diymenDao.saveDiymen(diymen);
		
		if(NullUtils.isNotEmpty(diymen.getParentId())) {
			Diymen parent = this.diymenDao.getDiymen(diymen.getParentId());
			if(parent != null) {
				parent.setMarkcode("暂无");
				parent.setUrl("暂无");
				this.diymenDao.saveDiymen(parent);
			}
		}
		return JsonUtil.toRes("保存成功");
	}

	@Transactional
	public String deleteDiymen(String id) {
		Diymen diymen = this.diymenDao.getDiymen(id);
		List<Diymen> diymens = this.diymenDao.getDiymenChildren(id);
		if (diymens.size() > 0) {
			return JsonUtil.toResOfFail("删除失败，请先删除其子菜单");
		}
		this.diymenDao.deleteDiymen(diymen);
		return JsonUtil.toRes("删除成功");
	}

	public String createMenu() {
		Boolean flag = this.menuManager.createMenu();
		if (flag) {
			return JsonUtil.toRes("生成成功");
		} else {
			return JsonUtil.toResOfFail("生成失败，授权参数设置不正确");
		}
	}

}
