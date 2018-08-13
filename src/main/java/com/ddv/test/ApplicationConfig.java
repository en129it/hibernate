package com.ddv.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ApplicationConfig {

	@Bean
	public RestTemplate endpointSrvcRestTemplate() {
		return new RestTemplate();
	}
}
/*
 
jSessionId		globalSessionId		userId							HTTP SESSION		 
permissionId	globalSessionId		isoPermissionId
permissionId	accountId
accountId		accountNumber		accountName 	ptgCode 
 

globalSessionId	permissionSetId

permissionSetId	permissionId



globalPermissionId		permissionSetEntry

permissionSetEntry		permissionId	txn_type_id
permissionSetEntry		accountId


select * from permission_set_entry_permission c, permission_set_entry_account d


 
 select * from lockable_transaction a, permission_set_entry b, permission_set_entry_permission c, permission_set_entry_account d
 where a.txn_type in ('AAA', 'BBB')
 and a.account_id in ('CCC', 'DDD')
 and b.global_session_id = 1
 and b.permission_entry_set = c.permission_entry_set
 and b.permission_entry_set = d.permission_entry_set
 and a.account_id = d.account_id
 and a.txn_type = c.txn_type_id
 
 
 
*/
