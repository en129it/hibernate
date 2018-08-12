package com.ddv.test;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

	@Autowired
	@Qualifier("bean1")
	private String bean;
	
	
	@Bean
	public Calendar create() {
		System.out.println("###### Calendar " + bean);
		return null;
	}
}
