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
import com.xysoft.admin.service.DepartmentService;
import com.xysoft.dao.AdminDao;
import com.xysoft.dao.DepartmentDao;
import com.xysoft.entity.Admin;
import com.xysoft.entity.Department;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;

@Component
@SuppressWarnings("unchecked")
public class DepartmentServiceImpl implements DepartmentService{
	
	@Resource
	private AdminDao adminDao;
	@Resource
	private DepartmentDao departmentDao;

	@Transactional(readOnly = true)
	public String getDepartments(String name) {
		List<Admin> admins = this.adminDao.getAdmins();
		List<Department> departments = this.departmentDao.getDepartments(name);
		Map<String, List<Department>> maps = new HashMap<String, List<Department>>();
		for(Department department : departments){
			List<Admin> administrator = new ArrayList<Admin>();
			for (Admin admin : admins) {
				if(admin.getDepartment().equals(department.getId())) {
					administrator.add(admin);
				}
			}
			department.setAdmins(administrator);
			if(NullUtils.isEmpty(department.getParentId())) continue;
			List<Department> hms = null;
			if(maps.containsKey(department.getParentId())){
				hms = maps.get(department.getParentId());
			}else{
				hms = new ArrayList<Department>();
			}
			hms.add(department);
			maps.put(department.getParentId(), hms);
		}
		List<Department> hms =  new ArrayList<Department>();
		for(Department space : departments){
			if(NullUtils.isEmpty(space.getParentId())){
				hms.add(rebuildClass(hms, maps, space));
			}
		}
		return JsonUtil.toString(hms, "check");
	}
	
	/**
	 * 递归查找子分类节点.
	 */
	private Department rebuildClass(List<Department> hms, Map<String, List<Department>> maps, Department department) {
		if(!maps.containsKey(department.getId())) return department;
		List<Department> list = maps.get(department.getId());
		for(Department ls : list){
			department.setExpanded(true);
			department.getChildren().add(rebuildClass(hms, maps, ls));
		}
		return department;
	}
	
	
	private List<Department> subClass(Map<String, Department> maps, List<Department> hms, List<Department> children){
		for(Department department : children){
			department.setName("　" + department.getName());
			hms.add(department);
			List<Department> childrens = new ArrayList<Department>(); 
			for (Entry<String, Department> entry : maps.entrySet()) {
				if( department.getId().equals( entry.getKey() ) ){
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
	public String getDepartmentNull(String name) {
		List<Department> departments = this.departmentDao.getDepartments(name.replaceAll("　", ""));
		List<Department> hms = new ArrayList<Department>(); 
		Department dtm = new Department();
		dtm.setName("没有上一级");
		hms.add(dtm);
		
		Map<String, Department> maps = new IdentityHashMap<String, Department>();
		for(Department department : departments){
			if(NullUtils.isNotEmpty( department.getParentId() )){
				maps.put(department.getParentId(), department);
			}
		}
		for (Department space : departments) {
			if(NullUtils.isEmpty(space.getParentId())){
				hms.add(space);
			}
			List<Department> children = new ArrayList<Department>(); 
			for (Entry<String, Department> entry : maps.entrySet()) {
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
	public String saveDepartment(Department param) {
		Department department = null;
		if(NullUtils.isEmpty(param.getId())) {
			department = new Department();
		}else {
			department = this.departmentDao.getDepartment(param.getId());
		}
		BeanUtils.copyProperties(param, department, new String[] {"id", "createDate"});
		this.departmentDao.saveDepartment(department);
		return JsonUtil.toRes("保存成功");
	}
	
	@Transactional
	public String deleteDepartment(String id) {
		Department department = this.departmentDao.getDepartment(id);
		List<Admin> admins = this.adminDao.getAdmins();
		List<Department> departments = this.departmentDao.getDepartmentChildren(id);
		for (Admin admin : admins) {
			if(admin.getDepartment().equals(id)) {
				return JsonUtil.toRes("删除失败，请先删除部门人员");
			}
		}
		if(departments.size() > 0) {
			return JsonUtil.toRes("删除失败，请先删除下级部门");
		}
		this.departmentDao.deleteDepartment(department);
		return JsonUtil.toRes("删除成功");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
