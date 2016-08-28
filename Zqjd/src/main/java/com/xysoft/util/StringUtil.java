package com.xysoft.util;

import java.util.List;

public class StringUtil {

	public static String toSQLIn (List<String> params) {
		if(params == null || params.size() == 0) return "(' ')";
		String in = " (";
		for (String param : params) {
			in += "'"+param+"'" + ",";
		}
		in = in.substring(0, in.lastIndexOf(",")) + ")";
		return in;
	}
	
	public static String toSQLIn (String[] params) {
		if(params == null || params.length == 0) return "(' ')";
		String in = " (";
		for (String param : params) {
			in += "'"+param+"'" + ",";
		}
		in = in.substring(0, in.lastIndexOf(",")) + ")";
		return in;
	}
	
}
