package com.xysoft.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.xysoft.admin.service.OrderService;
import com.xysoft.common.ElementConst;
import com.xysoft.dao.AdminDao;
import com.xysoft.dao.FlowStepDao;
import com.xysoft.dao.FlowStepRecDao;
import com.xysoft.dao.FlowStepRecPostDao;
import com.xysoft.dao.OrderDao;
import com.xysoft.dao.UserDao;
import com.xysoft.dao.WxUserDao;
import com.xysoft.entity.Admin;
import com.xysoft.entity.FlowStep;
import com.xysoft.entity.FlowStepRec;
import com.xysoft.entity.FlowStepRecPost;
import com.xysoft.entity.Orders;
import com.xysoft.entity.User;
import com.xysoft.entity.WxUser;
import com.xysoft.support.DynamicBean;
import com.xysoft.support.JdbcDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.util.DateUtil;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;
import com.xysoft.util.RequestUtil;
import com.xysoft.weixin.template.First;
import com.xysoft.weixin.template.Keyword;
import com.xysoft.weixin.template.OrderData;
import com.xysoft.weixin.template.Remark;
import com.xysoft.weixin.template.Template;
import com.xysoft.weixin.util.WeixinCacheUtil;
import com.xysoft.weixin.util.WeixinUtil;

@Component
@SuppressWarnings("unchecked")
public class OrderServiceImpl implements OrderService {
	
	@Resource
	private AdminDao adminDao;
	@Resource
	private OrderDao orderDao;
	@Resource
	private UserDao userDao;
	@Resource
	private WxUserDao wxUserDao;
	@Resource
	private FlowStepDao flowStepDao;
	@Resource
	private FlowStepRecDao flowStepRecDao;
	@Resource
	private FlowStepRecPostDao flowStepRecPostDao;
	@Resource
	private JdbcDao<DynamicBean> jdbcDao;

	@Transactional(readOnly = true)
	public String getOrders(PageParam page, String phone) {
		String sql = "SELECT us.id AS `userId`, us.`name`, us.phone, us.address, od.id, od.createDate, od.modifyDate, od.ordernumber, fs.id AS flowsteprec, ad.`name` AS operator, GROUP_CONCAT(ro.`name`) AS posts FROM orders od JOIN `user` us ON od.`user` = us.id JOIN flowsteprec fs ON fs.orders = od.id JOIN admin ad ON ad.id=fs.admin JOIN flowsteprecpost fsp ON fs.id = fsp.flowStepRec JOIN role ro ON ro.id=fsp.posts WHERE us.phone LIKE ? GROUP BY od.id";
		Pager<DynamicBean> objects = this.jdbcDao.queryForPager(sql, page, "%"+phone+"%");
		Pager<Object> pager = new Pager<Object>();
		List<Object> list = new ArrayList<Object>();
		for (DynamicBean dynamicBean : objects.getDatas()) {
			list.add(dynamicBean.getObject());
		}
		pager.setDatas(list);
		pager.setTotal(objects.getTotal());
		pager.setPageCount(objects.getPageCount());
		pager.setPageIndex(objects.getPageIndex());
		return JsonUtil.toStringFromObject(pager);
	}

	@Transactional(readOnly = true)
	public String getUsersByPhone(String phone) {
		List<User> users  = this.userDao.getUsersByField("phone", phone);
		return JsonUtil.toString(users);
	}

	@Transactional
	public String saveOrders(String userId, User user, Orders order, String[] roles, String nextstep) {
		Admin admin = this.adminDao.getAdmin(RequestUtil.getUsername());
		User us = null;
		if(NullUtils.isEmpty(userId)) {
			us = new User();
			us.setUserType(0);
			us.setIsAccountEnabled(true);
			us.setIsAccountExpired(false);
			us.setIsAccountLocked(false);
			us.setIsCredentialsExpired(false);
			us.setLoginFailureCount(0);
			us.setUsername(user.getPhone());
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			us.setPassword(encoder.encodePassword(user.getPhone().trim(), user.getPhone().trim()));
		}else {
			us = this.userDao.getUserById(userId);
		}
		BeanUtils.copyProperties(user, us, new String[]{"id", "loginDate", "loginIp", "createDate", "username", "wxUser",
				"password", "userType", "isAccountEnabled", "isAccountExpired", "isAccountLocked", "isCredentialsExpired", "loginFailureCount"});
		
		this.userDao.saveUser(us);
		
		Orders or = null;
		if(NullUtils.isEmpty(order.getId())) {
			or = new Orders();
		}else {
			or = this.orderDao.getOrder(order.getId());
		}
		if(NullUtils.isEmpty(order.getOrdernumber())) or.setOrdernumber(DateUtil.toStrYyyyMmDdHhMmSs(new Date()));
		else or.setOrdernumber(order.getOrdernumber());
		or.setUser(us.getId());
		this.orderDao.saveOrder(or);
		
		//如果下一步是空，则是最后一步
		FlowStep flowStep = null;
		if(NullUtils.isEmpty(nextstep)) {
			flowStep = this.flowStepDao.getLastStep();
		}else {
			flowStep = this.flowStepDao.getFlowStep(nextstep);
		}
		FlowStepRec flowStepRec = this.flowStepRecDao.getCurrentStep(flowStep.getId(), or.getId());
		if(flowStepRec == null) {
			flowStepRec = new FlowStepRec();
			flowStepRec.setFlowstep(flowStep.getId());
			flowStepRec.setOrders(or.getId());
			flowStepRec.setAdmin(admin.getId());
			flowStepRec.setState(0);
			this.flowStepRecDao.saveFlowStepRec(flowStepRec);
		}
		
		//处理岗位
		List<FlowStepRecPost>  flowStepRecPosts = this.flowStepRecPostDao.getFlowStepRecPostByFlowStepRec(flowStepRec.getId());
		for (FlowStepRecPost flowStepRecPost : flowStepRecPosts) {
			this.flowStepRecPostDao.deleteFlowStepRecPost(flowStepRecPost);
		}
		if(roles != null) {
			for (String role : roles) {
				FlowStepRecPost frp = new FlowStepRecPost();
				frp.setFlowStepRec(flowStepRec.getId());
				frp.setPosts(role);
				this.flowStepRecPostDao.saveFlowStepRecPost(frp);
			}
		}
		return JsonUtil.toRes("保存成功");
	}
	
	@Transactional(readOnly = true)
	public String getOrderPosts(String flowsteprec) {
		List<FlowStepRecPost> flowStepRecPosts = this.flowStepRecPostDao.getFlowStepRecPostByFlowStepRec(flowsteprec);
		return JsonUtil.toString(flowStepRecPosts);
	}

	@Transactional
	public String deleteOrders(String id) {
		Orders order = this.orderDao.getOrder(id);
		if(order != null) {
			List<FlowStepRec> flowStepRecs = this.flowStepRecDao.getFlowStepRecsByOrder(order.getId());
			for (FlowStepRec flowStepRec : flowStepRecs) {
				List<FlowStepRecPost> flowStepRecPosts = this.flowStepRecPostDao.getFlowStepRecPostByFlowStepRec(flowStepRec.getId());
				for (FlowStepRecPost flowStepRecPost : flowStepRecPosts) {
					this.flowStepRecPostDao.deleteFlowStepRecPost(flowStepRecPost);
				}
				this.flowStepRecDao.deleteFlowStepRec(flowStepRec);
			}
			this.orderDao.deleteOrders(order);
		}
		return JsonUtil.toRes("删除成功");
	}
	
	@Transactional
	public String sendNotice(String order) {
		Orders orders = this.orderDao.getOrder(order);
		User user = this.userDao.getUserById(orders.getUser());
		if(NullUtils.isEmpty(user.getWxUser()))  return JsonUtil.toRes("该用户还未关联微信号，无法发送！");
		WxUser wxUser = this.wxUserDao.getWxUser(user.getWxUser());
		if(wxUser == null) return JsonUtil.toRes("找不到用户相关微信号");
		
		First first = new First("您的订单已创建成功，详情点击查看", "#173177");
		Keyword orderno = new Keyword(orders.getOrdernumber(), "#173177");
		Keyword refundno = new Keyword("保密", "#173177");
		Keyword refundproduct = new Keyword("保密", "#173177");
		Remark remark = new Remark("如您还有疑问，请联系客服0758-2839292，或卖场主管", "#173177");
		OrderData orderData = new OrderData(first, orderno, refundno, refundproduct, remark);
		
		String url = ElementConst.Service_Address+"/orderlist.jhtml?wxUser="+wxUser.getId()+"&phone="+user.getPhone();
		Template template = new Template(wxUser.getOpenid(), "4eGIAhCFDB4LCPntsexJxokm1FL3dIfDwt43-lJNxIg", url, orderData);
		int result = WeixinUtil.senTemplate(template, WeixinCacheUtil.getAccessToken(ElementConst.Wx_Token, ElementConst.Wx_AppId, ElementConst.Wx_AppSecret).getToken());
		if(result > 0) JsonUtil.toRes("发送失败，微信错误代码："+result);
		return JsonUtil.toRes("发送成功");
	}


}
