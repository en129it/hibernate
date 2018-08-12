package com.ddv.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!cloud")
public class Config2 {

	@Bean(name = "bean1")
	public String bean1() {
		return "Toto1";
	}
	
	@Bean(name = "bean2")
	public String bean2() {
		return "Toto2";
	}
	
}
