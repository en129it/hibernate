package com.ddv.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;


@Configuration
public class RedisConfig extends AbstractCloudConfig  {

	// https://docs.run.pivotal.io/buildpacks/java/configuring-service-connections/spring-service-bindings.html
	// https://docs.spring.io/spring-data/redis/docs/2.1.0.M3/reference/html/
	// https://github.com/pivotal-cf/cloudfoundry-certificate-truster/blob/master/src/main/java/io/pivotal/springcloud/ssl/SslCertificateTruster.java
	// https://github.com/redisson/redisson
	// https://spring.io/blog/2015/04/27/binding-to-data-services-with-spring-boot-in-cloud-foundry
	
	private static final String APPSYNC_CHANNEL_NAME = "AppSync"; 

	@Bean
	public RedisConnectionFactory redisFactory() {
	    return connectionFactory().redisConnectionFactory();
	}	
/*	
	@Bean
	public RedisConnectionfactory connectionfactory(@value("redis.host") string anhost, @value("redis.port") int aport, @value("redis.timeout") int atimeoutinmsec) {
		jedisconnectionFactory rslt = new JedisConnectionFactory();
		rslt.setHostName(anHost);
		rslt.setPort(aPort);
		rslt.setTimeout(aTimeoutInMsec);
		rslt.setUsePool(true);
		return rslt;
	}
*/
	@Bean
	public StringRedisTemplate redisTemplate(RedisConnectionFactory aConnectionFactory) {
		StringRedisTemplate rslt = new StringRedisTemplate(aConnectionFactory);
		return rslt;
	}
	
	@Bean
	public RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory aConnectionFactory) {
		RedisMessageListenerContainer rslt = new RedisMessageListenerContainer();
		rslt.setConnectionFactory(aConnectionFactory);
//		rslt.addMessageListener(new MessageListenerAdapter(aMessageListener), new ChannelTopic(APPSYNC_CHANNEL_NAME));
		return rslt;
	}

}
