package com.xysoft.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.xysoft.dao.LetterDao;
import com.xysoft.entity.Letter;
import com.xysoft.support.BaseDaoImpl;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;

@Component
public class LetterDaoImpl extends BaseDaoImpl<Letter>  implements LetterDao {

	public Pager<Letter> getLetters(PageParam page) {
		return this.getForPager("from Letter order by createDate desc", page);
	}

	public Letter getLetter(String id) {
		return this.get(Letter.class, id);
	}

	public void saveLetter(Letter letter) {
		this.saveOrUpdate(letter);
	}

	public List<Letter> getLetters() {
		return this.find("from Letter order by createDate desc");
	}

	public void deleteLetter(Letter letter) {
		this.delete(letter);
	}

}
