package com.xysoft.admin.service;

import com.xysoft.entity.Letter;
import com.xysoft.support.PageParam;

public interface LetterService {

	String getLetters(PageParam page);

	String saveLetter(Letter param);

	String deleteLetter(String ids);

	String sendLetter(String id);

	String getMessages(String admin, Boolean state);

	String readMessage(Integer type, String admin, String letter);

}
