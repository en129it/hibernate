package com.ddv.test;
/*
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
*/

//@Configuration
public class RedisConfig {

	// https://docs.run.pivotal.io/buildpacks/java/configuring-service-connections/spring-service-bindings.html
	
	private static final String APPSYNC_CHANNEL_NAME = "AppSync"; 
/*
	@Bean
	public RedisConnectionFactory connectionFactory(@Value("redis.host") String anHost, @Value("redis.port") int aPort, @Value("redis.timeout") int aTimeoutInMsec) {
		JedisConnectionFactory rslt = new JedisConnectionFactory();
		rslt.setHostName(anHost);
		rslt.setPort(aPort);
		rslt.setTimeout(aTimeoutInMsec);
		rslt.setUsePool(true);
		return rslt;
	}

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
	*/
}
