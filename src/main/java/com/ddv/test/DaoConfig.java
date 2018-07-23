package com.ddv.test;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.orm.hibernate5.HibernateTemplate;

@Configuration
@Order(100)
public class DaoConfig {

	@Autowired
	private SessionFactory sessionFactory;

    
    @Bean
    public HibernateTemplate template() {
    	System.out.println("########################## " + sessionFactory);
    	return new HibernateTemplate(sessionFactory); 
    }
	
}
