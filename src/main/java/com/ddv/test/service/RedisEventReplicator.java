package com.ddv.test.service;

import java.io.IOException;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RedisEventReplicator implements IEventReplicator, MessageListener {

	private ObjectMapper objectMapper = new ObjectMapper();
	private EventDeliveryCallback eventDeliveryCallback;
	private StringRedisTemplate redisTemplate;
	private ChannelTopic channel;

	public RedisEventReplicator(StringRedisTemplate aRedisTemplate, ChannelTopic aChannel) {
		redisTemplate = aRedisTemplate;
		channel = aChannel;
	}
	
	@Override
	public void emitEvent(int anId, TxnSseEvent anEvent) {
		try {
			redisTemplate.convertAndSend(channel.getTopic(), objectMapper.writeValueAsBytes(new IdentifiableTxnSseEvent(anId, anEvent)));
		} catch (JsonProcessingException ex) {
			
		}
	}

	@Override
	public void subscribe(EventDeliveryCallback aCallback) {
		eventDeliveryCallback = aCallback;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			IdentifiableTxnSseEvent rslt = objectMapper.readValue(message.getBody(), IdentifiableTxnSseEvent.class);
			if (eventDeliveryCallback!=null) {
				eventDeliveryCallback.onEvent(rslt.id, rslt.event);
			}
		} catch (JsonMappingException ex) {
			
		} catch (JsonParseException ex) {
			
		} catch (IOException ex) {
			
		}
}

	public static class IdentifiableTxnSseEvent {
		private int id;
		private TxnSseEvent event;
		
		public IdentifiableTxnSseEvent() {
			
		}
		
		public IdentifiableTxnSseEvent(int anId, TxnSseEvent anEvent) {
			id = anId;
			event = anEvent;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public TxnSseEvent getEvent() {
			return event;
		}

		public void setEvent(TxnSseEvent event) {
			this.event = event;
		}
		
	}
	
}
