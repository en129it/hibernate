package com.ddv.test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.cloud.service.ServiceInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("cloud")
public class HibernateConfigCloud extends AbstractCloudConfig {
		@FlywayDataSource
	    @Bean(name="appDataSource")
	    public DataSource appDataSource() {
	    	System.out.println("##### appDataSource creation");
	    	
	    	CloudFactory cloudFactory = new CloudFactory();
	        Cloud cloud = cloudFactory.getCloud();
	        List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
	        for (ServiceInfo serviceInfo : serviceInfos) {
	        	System.out.println("######## serviceInfo " + serviceInfo.getId());
	        }
	        
	    	DataSource ds = connectionFactory().dataSource("app-db-service");
	    	System.out.println("##### appDataSource creation " + ds);
	    	
	    	Connection conn = null;

	    	try {
	    		conn = ds.getConnection();
		    	DatabaseMetaData md = conn.getMetaData();
		    	ResultSet rs = md.getTables(null, null, "%", null);
		    	while (rs.next()) {
		    	  System.out.println(rs.getString(3));
		    	}
	  	    	System.out.println("##### list all tables done");
    		} catch (Exception ex) {
    			ex.printStackTrace();
	    	} finally {
	    		if (conn!=null) {
		    		try {
			    		conn.close();
		    		} catch (Exception ex) {
		    			ex.printStackTrace();
		    		}
	    		}
	    	}
	    	
	    	try {
	    		conn = ds.getConnection();
	    		Statement stmt = conn.createStatement();
	    	      
	    	      String sql = "select * from flyway_schema_history"; 

	    	      ResultSet rs = stmt.executeQuery(sql);
	    	      
	    	      ResultSetMetaData rsmd = rs.getMetaData();
	    	      int columnsNumber = rsmd.getColumnCount();
	    	      
	    	      while (rs.next()) {
	    	    	  for (int i=0; i<columnsNumber; i++) {
	    	    		  System.out.println("Col " + i + " : " + rs.getObject(i+1));
	    	    	  }
	    	      }
	  	    	System.out.println("##### flyway history done");
    		} catch (Exception ex) {
    			ex.printStackTrace();
	    	} finally {
	    		if (conn!=null) {
		    		try {
			    		conn.close();
		    		} catch (Exception ex) {
		    			ex.printStackTrace();
		    		}
	    		}
	    	}

	    	
	    	try {
	    		conn = ds.getConnection();
	    		Statement stmt = conn.createStatement();
	    	      
	    	      String sql = "drop table flyway_schema_history"; 

	    	      stmt.executeUpdate(sql);
	  	    	System.out.println("##### drop flyway history done");
    		} catch (Exception ex) {
    			ex.printStackTrace();
	    	} finally {
	    		if (conn!=null) {
		    		try {
			    		conn.close();
		    		} catch (Exception ex) {
		    			ex.printStackTrace();
		    		}
	    		}
	    	}
	    	
	    	return ds;
	    }

		@Bean("hibernateProperties")
	    public Properties hibernateProperties() {
	        Properties hibernateProperties = new Properties();
//	        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
	        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
	 
	        return hibernateProperties;
	    }

/*		
	    @Bean(name="iiiDataSource")
	    public DataSource iiiDataSource() {
	    	System.out.println("##### iiiDataSource creation");
	   
	    	return connectionFactory().dataSource("iii-db-service");
	    }
*/	    
}
