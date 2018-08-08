package com.ddv.test;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.ddv.test.dao.DaoSupport;

@Configuration
@Order(100)
public class DaoConfig {

	@Autowired()
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	@Bean
	public DaoSupport daoSupport() {
		DaoSupport support = new DaoSupport();
		support.setSessionFactory(sessionFactory);
		return support;
	}
	
}
