package com.xysoft.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.xysoft.support.DataPager;

/**
 * 初始化多语言数据(中文为主导语言).
 */

public class LangUtil<T> {
	
	public List<T> initJavaBeanLang(List<T> beans, Locale locale) {
		for (T bean : beans) {
			initJavaBeanLang(bean, locale);
		}
		return beans;
	}
	
	public DataPager<T> initJavaBeanLang(DataPager<T> pager, Locale locale) {
		for (T bean : pager.getRows()) {
			initJavaBeanLang(bean, locale);
		}
		return pager;
	}
	
	public T initJavaBeanLang(T bean, Locale locale) {
		if ("zh".equals(locale.getLanguage())) return bean;
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
			Map<String, PropertyDescriptor> props = new HashMap<String, PropertyDescriptor>();
			for(int i = 0; i < properties.length; i++) {
				props.put(properties[i].getName(), properties[i]);
			}
			for(int i = 0; i < properties.length; i++) {
				if (properties[i].getName().startsWith(locale.getLanguage())) {
					String proName = properties[i].getName().replaceFirst(locale.getLanguage(), "");
					if (props.containsKey(proName)) {
						BeanUtils.setProperty(bean, proName, BeanUtils.getProperty(bean, properties[i].getName()));
					}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
}
