package com.xysoft.admin.service;

import com.xysoft.entity.Department;

public interface DepartmentService {

	String getDepartments(String name);

	String getDepartmentNull(String name);

	String saveDepartment(Department param);

	String deleteDepartment(String id);

}
