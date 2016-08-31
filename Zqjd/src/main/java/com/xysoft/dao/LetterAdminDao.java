package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.LetterAdmin;
import com.xysoft.support.BaseDao;

public interface LetterAdminDao extends BaseDao<LetterAdmin>{

	void deleteLetterAdminr(LetterAdmin letterAdmin);

	List<LetterAdmin> getLetterAdmins();

	void saveLetterAdmin(LetterAdmin letterAdmin);

	List<LetterAdmin> getMessages(String admin, Boolean state);

}
