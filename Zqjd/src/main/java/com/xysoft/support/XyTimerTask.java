package com.xysoft.support;
/*	格式说明：
秒（0~59）
分钟（0~59）
小时（0~23）
天（月）（0~31，但是你需要考虑你月的天数）
月（0~11）
天（星期）（1~7 1=SUN 或 SUN，MON，TUE，WED，THU，FRI，SAT）
年份（1970－2099）
下面例子：
"0 0 10,14,16 * * ?" 每天上午10点，下午2点，4点
"0 0/30 9-17 * * ?" 朝九晚五工作时间内每半小时
"0 0 12 ? * WED" 表示每个星期三中午12点 
"0 0 12 * * ?" 每天中午12点触发 
"0 15 10 ? * *" 每天上午10:15触发 
"0 15 10 * * ?" 每天上午10:15触发 
"0 15 10 * * ? *" 每天上午10:15触发 
"0 15 10 * * ? 2005" 2005年的每天上午10:15触发 
"0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发 
"0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发 
"0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发 
"0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发 
"0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发 
"0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发 
"0 15 10 15 * ?" 每月15日上午10:15触发 
"0 15 10 L * ?" 每月最后一日的上午10:15触发 
"0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发 
"0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发 
"0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发 */
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class XyTimerTask {
	
	public static final ThreadLocal<Session> sessionset = new ThreadLocal<Session>();
	
	private HibernateTemplate hibernateTemplate;
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		hibernateTemplate = new HibernateTemplate(sessionFactory);
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unused")
	private Session getCurrentSession() {
		Session session = sessionset.get();
		 if(session == null) {
			 session = sessionFactory.openSession();
             sessionset.set(session);
       }
       return session;
	}
	
	public static void closeSession() {
        Session session = sessionset.get();
        if(session != null) {
        	session.close();
        }
        sessionset.set(null);
  }
	
	@Transactional
	@Scheduled(cron = "0 0 00 * * ?")
	public void clientInfoTimer() {
		
	}

}
