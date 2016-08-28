package com.xysoft.support;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;

import org.springframework.web.context.request.WebRequest;

public class DateBindingInitializer implements WebBindingInitializer{

	public static final DateFormat DF_LONG = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final DateFormat DF_SHORT = new SimpleDateFormat("yyyy-MM-dd");

	public void initBinder(WebDataBinder arg0, WebRequest arg1) {
		arg0.registerCustomEditor(Date.class, new DateTypeEditor());
	}
	
	class DateTypeEditor extends PropertyEditorSupport {
		public static final int SHORT_DATE = 10;

		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			text = text.trim();
			if (!StringUtils.hasText(text)) {
				setValue(null);
				return;
			}
			try {
				if (text.length() <= SHORT_DATE) {
					setValue(new java.sql.Date(DF_SHORT.parse(text).getTime()));
				} else {
					setValue(new java.sql.Timestamp(DF_LONG.parse(text).getTime()));
				}
			} catch (ParseException ex) {
				IllegalArgumentException iae = new IllegalArgumentException(
						"Could not parse date: " + ex.getMessage());
				iae.initCause(ex);
				throw iae;
			}
		}

		@Override
		public String getAsText() {
			Date value = (Date) getValue();
			return (value != null ? DF_LONG.format(value) : "");
		}
	}

}
