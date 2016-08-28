package com.xysoft.dao;

import java.util.List;

import com.xysoft.entity.Letter;
import com.xysoft.support.BaseDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

public interface LetterDao extends BaseDao<Letter>{

	Pager<Letter> getLetters(PageParam page);

	Letter getLetter(String id);

	void saveLetter(Letter letter);

	List<Letter> getLetters();

	void deleteLetter(Letter letter);

}
