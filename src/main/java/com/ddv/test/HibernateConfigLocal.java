package com.ddv.test;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("!cloud")
public class HibernateConfigLocal {
	    @Bean(name="appDataSource")
	    public DataSource appDataSource() {
	    	System.out.println("##### appDataSource creation");
	    	
	    	JdbcDataSource ds = new JdbcDataSource();
	    	ds.setURL("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
	    	ds.setUser("sa");
	    	ds.setPassword("sa");
	    	return ds;
	    	
	    	//return null;
	    }

		
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
}
