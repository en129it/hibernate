package com.ddv.test.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

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
	
}
