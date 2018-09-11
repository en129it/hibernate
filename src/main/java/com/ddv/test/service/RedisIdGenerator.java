package com.ddv.test.service;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * RedisIdGenerator is an IdGenerator that uses a Redis (key, value) pair to
 * generate unique identifiers. This generator should be used when multiple
 * instances of this application are launched. It guarantees that these 
 * instances will never generate the same identifiers.
 *
 */
public class RedisIdGenerator implements IdGenerator {

	/** The Redis key that will hold the last generated unique identifier value. */
	private static final byte[] idGeneratorKey = "idGenerator".getBytes();
	/** The Redis connection. */
	private RedisConnection connection;

	/**
	 * Create a new RedisIdGenerator and initialize it.
	 * @param aConnectionFactory Non-null Redis connection.
	 */
	public RedisIdGenerator(RedisConnectionFactory aConnectionFactory) {
		connection = aConnectionFactory.getConnection();
	}
	
	@Override
	public int getNextId() {
		Long rslt = connection.incr(idGeneratorKey);
		System.out.println("#### redis.getNextId() " + rslt);
		return rslt.intValue();
	}

}
