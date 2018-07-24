package com.ddv.test;

import javax.servlet.http.HttpSessionListener;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ddv.test.util.SessionLifecycleListener;

@Configuration
public class HttpConfig {
	
	@Bean
	public HttpSessionListener sessionListener() {
		System.out.println("#### HttpConfig");
		return new SessionLifecycleListener();
	}
}
