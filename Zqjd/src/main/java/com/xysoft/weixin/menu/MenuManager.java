package com.xysoft.weixin.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.xysoft.common.ElementConst;
import com.xysoft.dao.DiymenDao;
import com.xysoft.entity.Diymen;
import com.xysoft.util.NullUtils;
import com.xysoft.weixin.pojo.AccessToken;
import com.xysoft.weixin.pojo.Button;
import com.xysoft.weixin.pojo.CommonButton;
import com.xysoft.weixin.pojo.ComplexButton;
import com.xysoft.weixin.pojo.Menu;
import com.xysoft.weixin.util.WeixinUtil;

/** 
 * 菜单管理器类 .
 */ 
@Component
public class MenuManager {
	@Resource
	private DiymenDao diymenDao;
	
	private static Logger log = LoggerFactory.getLogger(MenuManager.class);  
	
	/**
	 * 生成自定义菜单.
	 */
	public Boolean createMenu() {
		// 第三方用户唯一凭证  
        String appId = ElementConst.Wx_AppId;  
        // 第三方用户唯一凭证密钥  
        String appSecret = ElementConst.Wx_AppSecret;  
        if(NullUtils.isEmpty(appId) || NullUtils.isEmpty(appSecret)) return false;
        // 调用接口获取access_token  
        AccessToken at = WeixinUtil.getAccessToken(appId, appSecret);  
        if (null != at) {  
            // 调用接口创建菜单  
            int result = WeixinUtil.createMenu(this.getMenu(), at.getToken());  
            // 判断菜单创建结果  
            if (0 == result)  {
            	log.info("菜单创建成功！"); 
            	System.out.println("菜单创建成功！");
            	return true;
            } else { 
                log.info("菜单创建失败，错误码：" + result);  
                System.out.println("菜单创建失败，错误码：" + result);
            }
        }  
        return false;
	}
	  
    /** 
     * 组装菜单数据. 
     */  
    private Menu getMenu() {  
    	
    	List<Diymen> diymenes = this.diymenDao.getDiymenes();
		Map<String, List<Diymen>> maps = new HashMap<String, List<Diymen>>();
		for (Diymen diymen : diymenes) {
			if (NullUtils.isEmpty(diymen.getParentId())) continue;
			List<Diymen> hms = null;
			if (maps.containsKey(diymen.getParentId())) {
				hms = maps.get(diymen.getParentId());
			} else {
				hms = new ArrayList<Diymen>();
			}
			hms.add(diymen);
			maps.put(diymen.getParentId(), hms);
		}
		
		List<Button> lstButton = new ArrayList<Button>();
		
		for (Diymen diymen : diymenes) {
			if (diymen.getVisible() && NullUtils.isEmpty(diymen.getParentId())) {
				if (maps.containsKey(diymen.getId())) {
					List<Diymen> lst = maps.get(diymen.getId());
					ComplexButton button = new ComplexButton();
					button.setName(diymen.getName());
					List<CommonButton> lstSubButton = new ArrayList<CommonButton>();
					for (Diymen diy : lst) {
						CommonButton subButton = new CommonButton();  
						subButton.setName(diy.getName());  
						subButton.setKey(diy.getMarkcode());  
						subButton.setUrl(diy.getUrl());
						if (NullUtils.isEmpty(diy.getUrl())) {
							subButton.setType("click");  
						} else {
							subButton.setType("view");
						}
						lstSubButton.add(subButton);
					}
					Button[] tmpButtons = new Button[lstSubButton.size()];
					button.setSub_button(lstSubButton.toArray(tmpButtons));
					lstButton.add(button);
				} else {
					CommonButton button = new CommonButton();  
					button.setName(diymen.getName());  
					button.setKey(diymen.getMarkcode());  
					button.setUrl(diymen.getUrl());
					if (NullUtils.isEmpty(diymen.getUrl())) {
						button.setType("click");  
					} else {
						button.setType("view");
					}
					lstButton.add(button);
				}
			}
		}
  
        Menu menu = new Menu();  
        Button[] mainButtons = new Button[lstButton.size()];
        menu.setButton(lstButton.toArray(mainButtons));  
        return menu;  
    }  
}
