package com.xysoft.dao;

import com.xysoft.entity.GiftCode;
import com.xysoft.support.BaseDao;

public interface GiftCodeDao extends BaseDao<GiftCode> {
	
	GiftCode getGiftCodeByPhone (String phone);
	
	GiftCode getGiftCodeByCode (String code);

	void saveGiftCode(GiftCode giftCode);

}
