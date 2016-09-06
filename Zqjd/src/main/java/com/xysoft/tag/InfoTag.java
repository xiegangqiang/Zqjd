package com.xysoft.tag;
import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Resource;
import com.xysoft.tag.param.InfoParam;
import com.xysoft.tag.service.InfoTagService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings({"unchecked","rawtypes"})
public class InfoTag implements TemplateDirectiveModel {
	
	@Resource
	private InfoTagService infoTagService;

	@Override
	public void execute(Environment env, Map param, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		try {
			InfoParam params = new InfoParam(param);
			Object res = this.infoTagService.getInfo(params.getMarkcode());
			env.setVariable("res", DEFAULT_WRAPPER.wrap(res));
			body.render(env.getOut());
		} catch (Exception e) {
			e.printStackTrace();
			env.getOut().write("标签使用错误...");
		}
		
	}

}