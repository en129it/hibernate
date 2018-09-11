package com.ddv.test;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("!cloud")
public class HibernateConfigLocal {
		@FlywayDataSource
	    @Bean(name="appDataSource")
	    public DataSource appDataSource() throws SQLException {
	    	System.out.println("##### appDataSource creation");
	    	
//	    	OracleDataSource ds = new OracleDataSource();
	    	
	    	JdbcDataSource ds = new JdbcDataSource();
	    	ds.setURL("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
	    	ds.setUser("sa");
	    	ds.setPassword("sa");
	    	return ds;
	    	
	    	//return null;
	    }

		@Bean("hibernateProperties")
	    public Properties hibernateProperties() {
	        Properties hibernateProperties = new Properties();
//	        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
	        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle9Dialect");
	 
	        return hibernateProperties;
	    }

/*		
	    @Bean(name="iiiDataSource")
	    public DataSource iiiDataSource() {
	    	System.out.println("##### iiiDataSource creation");
	   
	    	JdbcDataSource ds = new JdbcDataSource();
	    	ds.setURL("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
	    	ds.setUser("sa");
	    	ds.setPassword("sa");
	    	return ds;
	    	
	    	// return null;
	    }
	    */
}
