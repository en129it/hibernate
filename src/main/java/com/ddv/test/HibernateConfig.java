package com.ddv.test;

import java.util.Properties;

import javax.sql.DataSource;

import org.h2.Driver;
import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	
/*
https://flywaydb.org/documentation/commandline/migrate#baselineOnMigrate
https://docs.oracle.com/cd/B10501_01/win.920/a97249/ch3.htm

 @FlywayDataSource

 Flyway flyway = new Flyway();

    // Set the data source
    flyway.setDataSource(dataSource);

    // Where to search for classes to be executed or SQL scripts to be found
    flyway.setLocations("net.somewhere.flyway");   classpath:com/some1/some2/some3/database/migration

    flyway.setTarget(MigrationVersion.LATEST);
    flyway.migrate();



@Configuration
public class FlywaySlaveInitializer {

     @Autowired private DataSource dataSource2;
     @Autowired private DataSource dataSource3;
     //other datasources

     @PostConstruct
     public void migrateFlyway() {
         Flyway flyway = new Flyway();
         //if default config is not sufficient, call setters here

         //source 2
         flyway.setDataSource(dataSource2);
         flyway.migrate();

         //source 3
         flyway.setDataSource(dataSource3);
         flyway.migrate();
     }
}

Call flyway during data source creation

	@Bean(name = "mysqlDb")
	@ConfigurationProperties(prefix = "spring.ds_mysql")
	public DataSource mysqlDataSource() {
		return DataSourceBuilder.create().build();
	}
	
 */
	
	@Autowired
	@Qualifier("appDataSource")
	private DataSource dataSource;
/*	
	@Autowired
	@Qualifier("iiiDataSource")
	private DataSource iiiDataSource;
*/	
	@Bean(name="sessionFactory")
	public LocalSessionFactoryBean sessionFactory(@Qualifier("hibernateProperties") Properties hibernateProperties) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.ddv.test.entity");
        sessionFactory.setHibernateProperties(hibernateProperties);
 
        return sessionFactory;
	}

/*	
	@Bean(name="iiiSessionFactory")
	public LocalSessionFactoryBean iiiSessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(iiiDataSource);
        sessionFactory.setPackagesToScan("com.ddv.test.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());
 
        return sessionFactory;
	}
*/	
    @Bean(name="appTransactionManager")
    public PlatformTransactionManager hibernateTransactionManager(@Qualifier("sessionFactory") LocalSessionFactoryBean sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory.getObject());
        return transactionManager;
    }

/*	
    @Bean(name="iiiTransactionManager")
    public PlatformTransactionManager iiiHibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(iiiSessionFactory().getObject());
        return transactionManager;
    }
 */   
    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
 
        return hibernateProperties;
    }
    
}
