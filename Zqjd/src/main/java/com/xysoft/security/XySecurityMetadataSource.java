package com.xysoft.security;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntUrlPathMatcher;
import org.springframework.security.web.util.UrlMatcher;
import org.springframework.transaction.annotation.Transactional;

import com.xysoft.support.BaseDao;

@SuppressWarnings({"rawtypes"})
public class XySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	private UrlMatcher urlMatcher = new AntUrlPathMatcher();
    private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
	private BaseDao baseDao;

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	public XySecurityMetadataSource(BaseDao baseDao) {
		this.baseDao = baseDao;
        loadResourceDefine();
    }
    
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}
	
	@Transactional
	private void loadResourceDefine() {
        resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
//        List<Action> actions = this.baseDao.getInitList("from Action");
//        for (Action action : actions) {
//        	Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
//        	ConfigAttribute ca = new SecurityConfig(action.getKeyword());
//        	atts.add(ca);
//        	resourceMap.put(action.getUrl(), atts);
//        }
    }

	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
        String url = ((FilterInvocation)object).getRequestUrl();
        Iterator<String> ite = resourceMap.keySet().iterator();
        while (ite.hasNext()) {
            String resURL = ite.next();
            if (urlMatcher.pathMatchesUrl(resURL, url)) {
                 return resourceMap.get(resURL);
            }
        }
        return null;
	}

	public boolean supports(Class<?> arg0) {
		return true;
	}

}
