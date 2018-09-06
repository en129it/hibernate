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

/**
 * RedisEventReplicator is an {@link IEventReplicator} that relies upon the 
 * Redis Topics and Publish/Subscribe facilities to exchange events with the 
 * other running instances of this application. 
 *
 */
public class RedisEventReplicator implements IEventReplicator, MessageListener {
	/** The Java to JSON bi-directional converter. */
	private ObjectMapper objectMapper = new ObjectMapper();
	/** The callback to be notified each time an external running application instance generated an event. */
	private EventDeliveryCallback eventDeliveryCallback;
	/** The Redis template that facilitates the communication with Redis. */
	private StringRedisTemplate redisTemplate;
	/** The Redis topic channel on which the events will be exchanged between the different running instances of this application. */
	private ChannelTopic channel;

	/**
	 * Create a new RedisEventReplicator instance.
	 * @param aRedisTemplate Non-null Redis template that facilitates the 
	 * communication with Redis. 
	 * @param aChannel Non-null Redis topic channel on which the events will be
	 * exchanged between the different running instances of this application.
	 */
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

	/**
	 * IdentifiableTxnSseEvent assigns an event with an unique identifier.
	 *
	 */
	public static class IdentifiableTxnSseEvent {
		/** The unique identifier assigned to the event. */
		private int id;
		/** The event. */
		private TxnSseEvent event;
		
		/**
		 * Create a new IdentifiableTxnSseEvent instance.
		 */
		public IdentifiableTxnSseEvent() {
			// default constructor
		}
		
		/**
		 * Create a new IdentifiableTxnSseEvent instance.
		 * @param anId The unique identifier assigned to the event.
		 * @param anEvent Non-null event.
		 */
		public IdentifiableTxnSseEvent(int anId, TxnSseEvent anEvent) {
			id = anId;
			event = anEvent;
		}

		/**
		 * Get the unique identifier assigned to the event.
		 * @return An identifier.
		 */
		public int getId() {
			return id;
		}

		/**
		 * Set the unique identifier assigned to the event.
		 * @param id Identifier.
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * Get the event.
		 * @return Non-null event.
		 */
		public TxnSseEvent getEvent() {
			return event;
		}

		/**
		 * Set the event
		 * @param event Non-null event.
		 */
		public void setEvent(TxnSseEvent event) {
			this.event = event;
		}
		
	}
	
}
