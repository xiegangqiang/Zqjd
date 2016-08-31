package com.xysoft.dao.impl;

import org.springframework.stereotype.Component;
import com.xysoft.dao.GiftCodeDao;
import com.xysoft.entity.GiftCode;
import com.xysoft.support.BaseDaoImpl;

@Component
public class GiftCodeDaoImpl extends BaseDaoImpl<GiftCode> implements GiftCodeDao {

	public GiftCode getGiftCodeByPhone(String phone) {
		return this.get("from GiftCode where phone=?", phone);
	}

	public GiftCode getGiftCodeByCode(String code) {
		return this.get("from GiftCode where code=?", code);
	}

	public void saveGiftCode(GiftCode giftCode) {
		this.saveOrUpdate(giftCode);
	}

}
