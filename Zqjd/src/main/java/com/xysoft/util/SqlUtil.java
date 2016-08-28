package com.xysoft.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mysql.jdbc.ResultSetMetaData;
import com.xysoft.support.DynamicBean;

public class SqlUtil {
	
	public static boolean isExistColumn(ResultSet rs, String columnName) {
	    try {
	        if (rs.findColumn(columnName) > 0 ) {
	            return true;
	        } 
	    }catch (SQLException e) {
	        return false;
	    }
	    return false;
	}
	
	public static String getMethodName(String fildeName) throws Exception{
		byte[] items = fildeName.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return new String(items);
	}
	
	public static Object ResultSetToBean(ResultSet rs, Class<?> clz) {
		Object obj = null;
		try {
			obj = clz.newInstance();
			Field[] fields = clz.getDeclaredFields();
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < fields.length; i++) {
				map.put(fields[i].getName(), fields[i].getType());
			}
			ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				String field = rsmd.getColumnName(i+1);
				if(map.containsKey( field )) {
					Class<?> fieldType = (Class<?>) map.get( field );
					Method method = clz.getMethod("set" + getMethodName( field ), fieldType);
					if( "int".equals(fieldType.toString()) ) method.invoke(obj, rs.getString( field ));
					if( "class java.lang.String".equals(fieldType.toString()) ) method.invoke(obj, rs.getString( field ));
					if( "class java.lang.Integer".equals(fieldType.toString()) ) method.invoke(obj, rs.getInt( field ));
					if( "class java.lang.Double".equals(fieldType.toString()) ) method.invoke(obj, rs.getDouble( field ));
					if( "class java.lang.Float".equals(fieldType.toString()) ) method.invoke(obj, rs.getFloat( field ));
					if( "class java.lang.Boolean".equals(fieldType.toString()) ) method.invoke(obj, rs.getBoolean( field ));
					if( "class java.lang.Short".equals(fieldType.toString()) ) method.invoke(obj, rs.getShort( field ));
					if( "class java.util.Date".equals(fieldType.toString()) ) method.invoke(obj, rs.getDate( field ));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static Object ResultSetToBean(ResultSet rs, Class<?> clz, String... columnName) {
		Object obj = null;
		try {
			obj = clz.newInstance();
			Field[] fields = clz.getDeclaredFields();
			for (int i = 0; i < columnName.length; i++) {
				Class<?> fieldType = fields[i].getType();
				Method method = clz.getMethod("set" + getMethodName( fields[i].getName() ), fieldType);
				if( isExistColumn(rs, columnName[i].trim()) ) {
					if( "int".equals(fieldType.toString()) ) method.invoke(obj, rs.getInt(columnName[i].trim()));
					if( "class java.lang.String".equals(fieldType.toString()) ) method.invoke(obj, rs.getString(columnName[i].trim()));
					if( "class java.lang.Integer".equals(fieldType.toString()) ) method.invoke(obj, rs.getInt(columnName[i].trim()));
					if( "class java.lang.Double".equals(fieldType.toString()) ) method.invoke(obj, rs.getDouble(columnName[i].trim()));
					if( "class java.lang.Float".equals(fieldType.toString()) ) method.invoke(obj, rs.getFloat(columnName[i].trim()));
					if( "class java.lang.Boolean".equals(fieldType.toString()) ) method.invoke(obj, rs.getBoolean(columnName[i].trim()));
					if( "class java.lang.Short".equals(fieldType.toString()) ) method.invoke(obj, rs.getShort(columnName[i].trim()));
					if( "class java.util.Date".equals(fieldType.toString()) ) method.invoke(obj, rs.getDate(columnName[i].trim()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static List<Object> DynamicToBean(List<DynamicBean> dynamicBeans) {
		List<Object> beans = new ArrayList<Object>();
		for (DynamicBean bean : dynamicBeans) {
			beans.add(bean.getObject());
		}
		return beans;
	}
	
	public static List<String> title(List<String> title) {
		return title;
		
	}
	
	
	
	
	
}
