package com.xysoft.dao.impl;

import org.springframework.stereotype.Component;

import com.xysoft.dao.AttentionDao;
import com.xysoft.entity.Attention;
import com.xysoft.support.BaseDaoImpl;

@Component
public class AttentionDaoImpl extends BaseDaoImpl<Attention> implements AttentionDao {

	public Attention getAttention() {
		return this.get("from Attention");
	}

	public void saveAttention(Attention attention) {
		this.save(attention);
	}

	public void deleteAttention(Attention attention) {
		this.delete(attention);
	}
	
	public Attention getAttentionByMarkcode(String eventKey) {
		return this.get("from Attention where markcode=?", eventKey);
	}
}
