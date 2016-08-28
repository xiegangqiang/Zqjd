package com.xysoft.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	/**
	 * 时间格式转换.
	 */
	public static String toStr(Date date, String pattern) {
		if (date == null) return "";
		SimpleDateFormat formt = new SimpleDateFormat(pattern);
		return formt.format(date);
	}
	
	public static String toStrMmDd(Date date){
		if (date == null) return "";
		SimpleDateFormat formt = new SimpleDateFormat("MM-dd");
		return formt.format(date);
	}
	
	public static String toStrYyyyMmDd(Date date) {
		if (date == null) return "";
		SimpleDateFormat formt = new SimpleDateFormat("yyyy-MM-dd");
		return formt.format(date);
	}
	
	public static String toStrYyyyMmDdHhMm(Date date) {
		if(date == null) return null;
		SimpleDateFormat formt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return formt.format(date);
	}
	
	public static String toStrYyyyMmDdHhMmSs(Date date) {
		if(date == null) return null;
		SimpleDateFormat formt = new SimpleDateFormat("yyyyMMddHHmmss");
		return formt.format(date);
	}
	
	public static String toStrYMD(Date date) {
		if(date == null) return null;
		SimpleDateFormat formt = new SimpleDateFormat("yyyy" + File.separator + "MM" + File.separator + "dd");
		return formt.format(date);
	}
	
	public static Date toDate(String str, String pattern) {
		if ("".equals(str)) return null;
		SimpleDateFormat formt = new SimpleDateFormat(pattern);
		try {
			return formt.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
