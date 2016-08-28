package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.LetterAdminDao;
import com.xysoft.entity.LetterAdmin;
import com.xysoft.support.BaseDaoImpl;

@Component
public class LetterAdminDaoImpl extends BaseDaoImpl<LetterAdmin> implements LetterAdminDao{

	public void deleteLetterAdminr(LetterAdmin letterAdmin) {
		this.delete(letterAdmin);
	}

	public List<LetterAdmin> getLetterAdmins() {
		return this.find("from LetterAdmin");
	}

	public void saveLetterAdmin(LetterAdmin letterAdmin) {
		this.saveOrUpdate(letterAdmin);
	}

	public List<LetterAdmin> getMessages(String admin, Boolean state) {
		String hql = "from LetterAdmin where admin = ? ";
		if(!state) {
			hql += "and state is " + state;
		}
		hql += " order by createDate asc";
		return this.find(hql, admin);
	}

}
