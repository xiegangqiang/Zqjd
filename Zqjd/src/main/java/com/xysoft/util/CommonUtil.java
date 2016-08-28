package com.xysoft.util;

import java.util.UUID;

public class CommonUtil {
	
	/**
	 * 随机获取UUID字符串(无中划线).
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) + uuid.substring(19, 23) + uuid.substring(24);
	}
	
	/**
	 * 转换分号字符串为数据库逗号字符串.(1;2;3;4 -> '1','2','3','4')
	 */
	public static String transToStrId(String Ids) {
		if (Ids == null) return "";
		String[] cls = Ids.split(";");
		String res = "";
		for (String cl : cls) {
			if (NullUtils.isEmpty(res)) {
				res = "'" + cl + "'";
			} else {
				res += ",'" + cl + "'";
			}
		}
		return res;
	}
	
}
