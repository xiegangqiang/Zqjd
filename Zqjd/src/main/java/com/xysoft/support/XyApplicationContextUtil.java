package com.xysoft.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SuppressWarnings("static-access")
public class XyApplicationContextUtil implements ApplicationContextAware {
	
	private static ApplicationContext context;
	
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
	
	public static ApplicationContext getContext(){
		return context;
	}
	
	public static Object getBean(String beanname) {
		return context.getBean(beanname);
	}

}
