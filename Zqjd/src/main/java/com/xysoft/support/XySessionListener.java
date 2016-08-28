package com.xysoft.support;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class XySessionListener implements HttpSessionAttributeListener{

	@Override
	public void attributeAdded(HttpSessionBindingEvent arg0) {
		if( arg0.getName().equals("SPRING_SECURITY_CONTEXT")) {
			//SecurityContextImpl securityContextImpl = (SecurityContextImpl)arg0.getValue();
			//AdminHm adminHm = (AdminHm) securityContextImpl.getAuthentication().getPrincipal();
			
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		if( arg0.getName().equals("SPRING_SECURITY_CONTEXT")) {
			//SecurityContextImpl securityContextImpl = (SecurityContextImpl)arg0.getValue();
			//AdminHm adminHm = (AdminHm) securityContextImpl.getAuthentication().getPrincipal();
			
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		
	}

}
