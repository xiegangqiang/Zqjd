package com.xysoft.tag;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import com.xysoft.support.DataPager;
import com.xysoft.tag.param.ProductClassParam;
import com.xysoft.tag.service.ProductClassTagService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings({"unchecked","rawtypes"})
public class ProductClassListTag implements TemplateDirectiveModel {
	@Resource
	private ProductClassTagService productClassTagService;

	@Override
	public void execute(Environment env, Map param, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		try {
			ProductClassParam params = new ProductClassParam(param);
			DataPager<Object> res = this.productClassTagService.getProductClasses(params.getPage(), params.getCount());
			env.setVariable("res", DEFAULT_WRAPPER.wrap(res));
			body.render(env.getOut());
		} catch (Exception e) {
			e.printStackTrace();
			env.getOut().write("标签使用错误...");
		}
	}
}
