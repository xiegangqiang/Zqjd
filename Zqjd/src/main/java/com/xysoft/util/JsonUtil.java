package com.xysoft.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JsonUtil {
	/**
	 * JSON字符串Array
	 */
	public static String toString(final Object obj, final String ...fields) {
		JsonConfig config = new JsonConfig();
		config.setExcludes(fields);
		JSONArray json = JSONArray.fromObject(obj, config);
		return json.toString();
	}
	
	/**
	 * JSON字符串Array(排序某些字段)
	 */
	public static String toStringExclusions(final Object obj, final String ...fields) {
		JsonConfig config = new JsonConfig();
		config.setExcludes(fields);
		JSONArray json = JSONArray.fromObject(obj, config);
		return json.toString();
	}
	
	/**
	 * JSON字符串Object
	 */
	public static String toStringFromObject(final Object obj) {
		JSONObject json = JSONObject.fromObject(obj);
		return json.toString();
	}
	
	/**
	 * 返回结果字符串.
	 */
	public static String toRes(String title, String msg, Map<String, Object> ...res) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("success", true);
		resMap.put("title", title);
		resMap.put("msg", msg);
		if (res != null && res.length > 0) {
			resMap.putAll(res[0]);
		}
		return JsonUtil.toStringFromObject(resMap);
	}
	
	/**
	 * 返回结果字符串.
	 */
	public static String toRes(String title, Map<String, Object> ...res) {
		return toRes(title, "", res);
	}
	
	/**
	 * 返回结果字符串(失败).
	 */
	public static String toResOfFail(String title, Map<String, Object> ...res) {
		return toResOfFail(title, "", res);
	}
	
	/**
	 * 返回结果字符串(失败).
	 */
	public static String toResOfFail(String title, String msg, Map<String, Object> ...res) {
		Map<String, Object> resMap = new HashMap<String, Object>();
		resMap.put("success", false);
		resMap.put("title", title);
		resMap.put("msg", msg);
		if (res != null && res.length > 0) {
			resMap.putAll(res[0]);
		}
		return JsonUtil.toStringFromObject(resMap);
	}
}
