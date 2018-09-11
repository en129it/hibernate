package com.ddv.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.scheduling.TaskScheduler;

import com.ddv.test.service.IEventReplicator;
import com.ddv.test.service.IdGenerator;
import com.ddv.test.service.RedisEventReplicator;
import com.ddv.test.service.RedisIdGenerator;
import com.ddv.test.service.state.StateContext;


@Configuration
@Profile("cloud")
public class RedisConfig extends AbstractCloudConfig  
{

	// https://docs.run.pivotal.io/buildpacks/java/configuring-service-connections/spring-service-bindings.html
	// https://docs.spring.io/spring-data/redis/docs/2.1.0.M3/reference/html/
	// https://github.com/pivotal-cf/cloudfoundry-certificate-truster/blob/master/src/main/java/io/pivotal/springcloud/ssl/SslCertificateTruster.java
	// https://github.com/redisson/redisson
	// https://spring.io/blog/2015/04/27/binding-to-data-services-with-spring-boot-in-cloud-foundry

	private static final String APPEVENT_CHANNEL_NAME = "AppEvent"; 
	private static final String APPSYNC_CHANNEL_NAME = "AppSync"; 

	@Bean
	public RedisConnectionFactory redisFactory() {
	    return connectionFactory().redisConnectionFactory();
	}
	
	@Bean
	public StringRedisTemplate redisTemplate(RedisConnectionFactory aConnectionFactory) {
		StringRedisTemplate rslt = new StringRedisTemplate(aConnectionFactory);
		return rslt;
	}
	
	@Bean
	public RedisMessageListenerContainer eventMessageListenerContainer(RedisConnectionFactory aConnectionFactory, RedisEventReplicator aMessageListener) {
		RedisMessageListenerContainer rslt = new RedisMessageListenerContainer();
		rslt.setConnectionFactory(aConnectionFactory);
		rslt.addMessageListener(aMessageListener, aMessageListener.getChannel());
		return rslt;
	}
	
	@Bean
	public RedisMessageListenerContainer syncMessageListenerContainer(RedisConnectionFactory aConnectionFactory, StateContext aStateContext) {
		RedisMessageListenerContainer rslt = new RedisMessageListenerContainer();
		rslt.setConnectionFactory(aConnectionFactory);
		rslt.addMessageListener(aStateContext, aStateContext.getChannel());
		return rslt;
	}

	@Bean
	public IdGenerator createIdGenerator(RedisConnectionFactory aConnectionFactory) {
		System.out.println("##### createIdGenerator");
		RedisIdGenerator rslt = new RedisIdGenerator(aConnectionFactory);
		return rslt;
	}
	
	@Bean
	public RedisEventReplicator createEventReplicator(StringRedisTemplate aRedisTemplate) {
		System.out.println("##### createEventReplicator");
		ChannelTopic eventChannelTopic = new ChannelTopic(APPEVENT_CHANNEL_NAME);
		RedisEventReplicator rslt = new RedisEventReplicator(aRedisTemplate, eventChannelTopic);
		return rslt;
	}
	
	public StateContext createSyncStateContext(StringRedisTemplate aRedisTemplate, TaskScheduler aTaskScheduler) {
		System.out.println("##### createSyncStateContext");
		ChannelTopic syncChannelTopic = new ChannelTopic(APPSYNC_CHANNEL_NAME);
		StateContext rslt = new StateContext(30, aTaskScheduler, aRedisTemplate, syncChannelTopic);
		return rslt;
	}
}
