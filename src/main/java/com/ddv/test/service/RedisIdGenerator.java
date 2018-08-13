package com.ddv.test.service;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

public class RedisIdGenerator implements IdGenerator {

	private static final byte[] idGeneratorKey = "idGenerator".getBytes();
	private RedisConnection connection;

	public RedisIdGenerator(RedisConnectionFactory aConnectionFactory) {
		connection = aConnectionFactory.getConnection();
	}
	
	@Override
	public int getNextId() {
		Long rslt = connection.incr(idGeneratorKey);
		return rslt.intValue();
	}

}
