package com.ddv.test;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("cloud")
public class HibernateConfigCloud extends AbstractCloudConfig {
	    @Bean(name="appDataSource")
	    public DataSource appDataSource() {
	    	System.out.println("##### appDataSource creation");
	    	
	    	return connectionFactory().dataSource("app-db-service");
	    }

		
	    @Bean(name="iiiDataSource")
	    public DataSource iiiDataSource() {
	    	System.out.println("##### iiiDataSource creation");
	   
	    	return connectionFactory().dataSource("iii-db-service");
	    }
}
