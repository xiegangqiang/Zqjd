package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.Department;
import com.xysoft.support.BaseDao;

public interface DepartmentDao extends BaseDao<Department>{

	List<Department> getDepartments(String name);

	Department getDepartment(String id);

	void saveDepartment(Department param);

	List<Department> getDepartmentChildren(String parentId);

	void deleteDepartment(Department department);

}
