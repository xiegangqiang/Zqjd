package com.xysoft.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.xysoft.admin.service.OrderService;
import com.xysoft.dao.AdminDao;
import com.xysoft.dao.FlowStepDao;
import com.xysoft.dao.FlowStepRecDao;
import com.xysoft.dao.FlowStepRecPostDao;
import com.xysoft.dao.OrderDao;
import com.xysoft.dao.UserDao;
import com.xysoft.entity.Admin;
import com.xysoft.entity.FlowStep;
import com.xysoft.entity.FlowStepRec;
import com.xysoft.entity.FlowStepRecPost;
import com.xysoft.entity.Orders;
import com.xysoft.entity.User;
import com.xysoft.support.DynamicBean;
import com.xysoft.support.JdbcDao;
import com.xysoft.support.PageParam;
import com.xysoft.support.Pager;
import com.xysoft.util.JsonUtil;
import com.xysoft.util.NullUtils;
import com.xysoft.util.RequestUtil;

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
	private FlowStepDao flowStepDao;
	@Resource
	private FlowStepRecDao flowStepRecDao;
	@Resource
	private FlowStepRecPostDao flowStepRecPostDao;
	@Resource
	private JdbcDao<DynamicBean> jdbcDao;

	@Transactional(readOnly = true)
	public String getOrders(PageParam page, String phone) {
		String sql = "SELECT us.id AS `user`, us.`name`, us.phone, us.address, od.id, od.createDate, od.modifyDate, od.ordernumber, fs.id AS flowsteprec, ad.`name` AS operator, GROUP_CONCAT(ro.`name`) AS posts FROM orders od JOIN `user` us ON od.`user` = us.id JOIN flowsteprec fs ON fs.orders = od.id JOIN admin ad ON ad.id=fs.admin JOIN flowsteprecpost fsp ON fs.id = fsp.flowStepRec JOIN role ro ON ro.id=fsp.posts WHERE us.phone LIKE ?";
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
		List<User> users  = this.userDao.getUserByField("phone", phone);
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
			us.setPassword(encoder.encodePassword(us.getPhone().trim(), us.getPhone().trim()));
		}else {
			us = this.userDao.getUserById(userId);
		}
		BeanUtils.copyProperties(user, us, new String[]{"id", "loginDate", "loginIp", "createDate", "username",
				"password", "userType", "isAccountEnabled", "isAccountExpired", "isAccountLocked", "isCredentialsExpired", "loginFailureCount"});
		
		this.userDao.saveUser(us);
		
		Orders or = null;
		if(NullUtils.isEmpty(order.getId())) {
			or = new Orders();
		}else {
			or = this.orderDao.getOrder(order.getId());
		}
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
		Map<String, FlowStepRecPost> map = new HashMap<String, FlowStepRecPost>();
		for (FlowStepRecPost flowStepRecPost : flowStepRecPosts) {
			map.put(flowStepRecPost.getFlowStepRec(), flowStepRecPost);
		}
		if(roles != null) {
			for (String role : roles) {
				if(map.containsKey(flowStepRec.getId())) {
					map.remove(flowStepRec.getId());
				}else {
					FlowStepRecPost frp = new FlowStepRecPost();
					frp.setFlowStepRec(flowStepRec.getId());
					frp.setPosts(role);
					this.flowStepRecPostDao.saveFlowStepRecPost(frp);
				}
			}
		}
		for (FlowStepRecPost flowStepRecPost : map.values()) {
			this.flowStepRecPostDao.deleteFlowStepRecPost(flowStepRecPost);
		}
		return JsonUtil.toRes("保存成功");
	}

}
