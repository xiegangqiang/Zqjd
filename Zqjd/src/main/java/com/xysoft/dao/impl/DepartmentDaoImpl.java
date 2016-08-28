package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.DepartmentDao;
import com.xysoft.entity.Department;
import com.xysoft.support.BaseDaoImpl;

@Component
public class DepartmentDaoImpl extends BaseDaoImpl<Department> implements DepartmentDao{

	public List<Department> getDepartments(String name) {
		return this.find("from Department where name like ? order by level asc", "%" + name + "%");
	}
	
	public Department getDepartment(String id) {
		return this.get(Department.class, id);
	}
	
	public void saveDepartment(Department param) {
		this.saveOrUpdate(param);
	}
	
	public List<Department> getDepartmentChildren(String parentId) {
		return this.find("from Department where parentId = ?", parentId);
	}
	
	public void deleteDepartment(Department department) {
		this.delete(department);
	}
}
