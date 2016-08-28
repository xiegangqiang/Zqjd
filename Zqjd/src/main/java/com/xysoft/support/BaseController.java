package com.xysoft.support;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.xysoft.util.JsonUtil;

@SuppressWarnings("unchecked")
public class BaseController {
	
	@ExceptionHandler({Exception.class})   
    public String exception(Exception e, HttpServletResponse response) throws IOException {   
		e.printStackTrace();
		String res = JsonUtil.toResOfFail("保存失败", "操作异常");
		response.getWriter().print(res);
		return null;
    } 
    
}
