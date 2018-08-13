package com.ddv.test.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.internal.SessionImpl;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.ddv.test.entity.Lock;
import com.ddv.test.entity.PermissionEntrySetPermission;
import com.ddv.test.entity.Lock.Tmp;
import com.ddv.test.entity.PermissionEntrySet;
import com.ddv.test.entity.PermissionEntrySetAccount;
import com.ddv.test.model.Permission;
import com.ddv.test.entity.Transaction;

@Repository
public class TransactionDao {

	@Autowired
	private DaoSupport daoSupport;
	
	public List<Transaction> findTransactions() {
		return daoSupport.getHibernateTemplate().execute(new HibernateCallback<List<Transaction>>() {
			@Override
			public List<Transaction> doInHibernate(Session session) throws HibernateException {
				System.out.println("execute query");
				return session.createQuery("from " + Transaction.class.getSimpleName()).getResultList();
			}
		});
	}
	
	public void init() {
		System.out.println("################### START INIT");
		ArrayList<Lock> locks = new ArrayList<Lock>();
		
		for (long i=0; i<100; i++) {
			Lock lock1 = new Lock();
			lock1.setUserId(1000L + i);
			
			Tmp aTmp = new Tmp();
			aTmp.setStartTimestamp(LocalDateTime.now());
			aTmp.setAction("REPAIR");

			
			lock1.setTmp(aTmp);
			locks.add(lock1);
		}
		
//		StatelessSession statelessSession = daoSupport.getHibernateTemplate().getSessionFactory().openStatelessSession();
/*		
		StatelessSession statelessSession = daoSupport.getSessionFactory().openStatelessSession();
		try {
			int count = 0;
			for (Lock lock : locks) {
//				daoSupport.getHibernateTemplate().persist(lock);
				count++;
				statelessSession.insert(lock);
			}
		} finally {
			statelessSession.close();
		}
*/		
		
//		daoSupport.getHibernateTemplate().clear();
/*		
		int count = 0;
		for (Lock lock : locks) {
			daoSupport.getHibernateTemplate().persist(lock);
			count++;
			if (count%10 == 0) {
				session
			}
			
			
		}
		*/
		
		PermissionEntrySet entry = new PermissionEntrySet();
		entry.setGlobalSessionId("TITI_GUID");
		entry.setLastAccessTimestamp(LocalDateTime.now());
		daoSupport.getHibernateTemplate().saveOrUpdate(entry);
		daoSupport.getHibernateTemplate().flush();
		daoSupport.getHibernateTemplate().evict(entry);
		
		SessionFactory sessionFactory = daoSupport.getHibernateTemplate().getSessionFactory();
		SessionImpl session = (SessionImpl)sessionFactory.getCurrentSession();
		
		StatelessSession statelessSession = daoSupport.getHibernateTemplate().getSessionFactory().openStatelessSession(session.connection());
		try {
			for (long i=0; i<4; i++) {
				PermissionEntrySetAccount acct = new PermissionEntrySetAccount();
				acct.setAccountId(i);
				acct.setAccountName("acct" + i);
				acct.setPtgCode("pgt"+ i);
				acct.setPermissionEntrySet(entry);
				statelessSession.insert(acct);
					
			}
		} finally {
			statelessSession.close();
		}
		
		
		
		
		System.out.println("################### END INIT");
	}
	
	public Lock getFirstLock() {
		return daoSupport.getHibernateTemplate().execute(new HibernateCallback<Lock>() {
			@Override
			public Lock doInHibernate(Session session) throws HibernateException {
				System.out.println("execute query");
				return (Lock)session.createQuery("from " + Lock.class.getSimpleName() + " where txnId=1").getSingleResult();
			}
		});
	}
	
	
	public int deleteAllLocks() {
		Integer rslt = (Integer)daoSupport.getHibernateTemplate().execute(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				System.out.println("execute query");
				Query query = session.createQuery("delete from " + Lock.class.getSimpleName() + " l where l.startTimestamp < :timestamp");
				query.setParameter("timestamp", LocalDateTime.now());
				return query.executeUpdate();
			}
		});
		return rslt;
	}
}
