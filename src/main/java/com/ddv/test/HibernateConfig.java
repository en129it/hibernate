package com.ddv.test;

import java.util.Properties;

import javax.sql.DataSource;

import org.h2.Driver;
import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@Order(10)
public class HibernateConfig {
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.ddv.test.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
 
        return sessionFactory;
	}
	

    @Bean
    public DataSource dataSource() {
    	JdbcDataSource ds = new JdbcDataSource();
    	ds.setURL("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
    	ds.setUser("sa");
    	ds.setPassword("sa");
    	return ds;
/*    	
    	Driver driver;
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("sa");
 
        return dataSource;
        */
    }
 	
    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
    
    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
 
        return hibernateProperties;
    }
    
}
