package com.xysoft.tag;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

import java.io.IOException;
import java.util.Map;
import javax.annotation.Resource;
import com.xysoft.support.DataPager;
import com.xysoft.tag.param.ProductParam;
import com.xysoft.tag.service.ProductListTagService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings({"unchecked","rawtypes"})
public class ProductListTag implements TemplateDirectiveModel{
	
	@Resource
	private ProductListTagService productListTagService;

	@Override
	public void execute(Environment env, Map param, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		try {
			ProductParam params = new ProductParam(param);
			DataPager<Object> res = this.productListTagService.getProducts(params.getProductClass(), params.getPage(), params.getCount(), params.getName());
			env.setVariable("res", DEFAULT_WRAPPER.wrap(res));
			body.render(env.getOut());
		} catch (Exception e) {
			e.printStackTrace();
			env.getOut().write("标签使用错误...");
		}
	}
	
}
