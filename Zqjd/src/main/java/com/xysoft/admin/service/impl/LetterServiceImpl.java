package com.xysoft.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xysoft.admin.service.LetterService;
import com.xysoft.dao.AdminDao;
import com.xysoft.dao.LetterAdminDao;
import com.xysoft.dao.LetterDao;
import com.xysoft.entity.Admin;
import com.xysoft.entity.Letter;
import com.xysoft.entity.LetterAdmin;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;

@Component
@SuppressWarnings("unchecked")
public class LetterServiceImpl implements LetterService{
	
	@Resource
	private LetterDao letterDao;
	@Resource
	private AdminDao adminDao;
	@Resource
	private LetterAdminDao letterAdminDao;

	@Transactional(readOnly = true)
	public String getLetters(PageParam page) {
		Pager<Letter> pager = this.letterDao.getLetters(page);
		return JsonUtil.toStringFromObject(pager);
	}

	@Transactional
	public String saveLetter(Letter param) {
		Letter letter = null;
		if(NullUtils.isEmpty(param.getId())){
			letter = new Letter();
			letter.setIsSend(0);
			letter.setVisit(0);
			letter.setOmit(0);
		}else {
			letter = this.letterDao.getLetter(param.getId());
		}
		letter.setRanged(param.getRanged());
		letter.setContent(param.getContent());
		this.letterDao.saveLetter(letter);
		return JsonUtil.toRes("保存成功");
	}

	@Transactional
	public String deleteLetter(String ids) {
		String[] letterIds = ids.split(",");
		List<Letter> letters = this.letterDao.getLetters();
		List<LetterAdmin> letterAdmins = this.letterAdminDao.getLetterAdmins();
		Map<String, Letter> maps = new HashMap<String, Letter>(); 
		for (Letter letter : letters) {
			maps.put(letter.getId(), letter);
		}
		for (String id : letterIds) {
			if(maps.containsKey(id)) {
				Letter letter = maps.get(id);
				for (LetterAdmin letterAdmin : letterAdmins) {
					if(letterAdmin.getLetter().equals(letter.getId())) {
						this.letterAdminDao.deleteLetterAdminr(letterAdmin);
					}
				}
				this.letterDao.deleteLetter(letter);
			}
		}
		return JsonUtil.toRes("删除成功");
	}

	@Transactional
	public String sendLetter(String id) {
		Letter letter = this.letterDao.getLetter(id);
		List<Admin> admins = this.adminDao.getAdmins();
		for (Admin admin : admins) {
			LetterAdmin letterAdmin = new LetterAdmin();
			letterAdmin.setAdmin(admin.getId());
			letterAdmin.setLetter(letter.getId());
			letterAdmin.setState(false);
			this.letterAdminDao.saveLetterAdmin(letterAdmin);
		}
		letter.setIsSend(1);
		this.letterDao.saveLetter(letter);
		return JsonUtil.toRes("发送成功");
	}

	@Transactional
	public String getMessages(String admin, Boolean state) {
		List<Letter> letters = this.letterDao.getLetters();
		Map<String, Letter> map = new HashMap<String, Letter>();
		for (Letter letter : letters) {
			if(!map.containsKey(letter)){
				map.put(letter.getId(), letter);
			}
		}
		List<LetterAdmin> letterAdmins = this.letterAdminDao.getMessages(admin, state);
		for (LetterAdmin letterAdmin : letterAdmins) {
			if(map.get(letterAdmin.getLetter()) != null) {
				letterAdmin.setMesasge(map.get(letterAdmin.getLetter()));
			}
		}
		return JsonUtil.toString(letterAdmins);
	}

	@Transactional
	public String readMessage(Integer type, String admin, String letter) {
		LetterAdmin letterAdmin = null;
		List<LetterAdmin> letterAdmins = this.letterAdminDao.getMessages(admin, false);
		for (LetterAdmin la : letterAdmins) {
			if(la.getLetter().equals(letter)) {
				letterAdmin = la;
			}
		}
		switch (type) {
		case 0://已阅读
			letterAdmin.setState(true);
			this.letterAdminDao.saveLetterAdmin(letterAdmin);
			break;
		case 1://不再显示、删除
			this.letterAdminDao.deleteLetterAdminr(letterAdmin);
			break;
		}
		return JsonUtil.toRes("修改成功");
	}
}










