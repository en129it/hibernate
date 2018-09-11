package com.ddv.test.service.state;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.TaskScheduler;

import com.ddv.test.service.RedisEventReplicator.IdentifiableTxnSseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * StateContext is the context used in a synchronization operation. The
 * different classes that extends {@link AbstractState} can access this context
 * to influence the synchronization operation.
 *
 */
public class StateContext implements MessageListener {

	/** The list of all the possible states during a synchronization operation. */
	private static Class<? extends AbstractState>[] ALL_STATES = new Class[]{SyncAnnounceState.class, SynchronizingState.class, SynchronizedState.class};
	
	/** The unique identifier of this service instance. That identifier determines which service instance will have the precedence to perform the synchronization. */
	private long serviceId;
	/** Task scheduler. */ 
	private TaskScheduler taskScheduler;
	/** The list made of a single instance of each class representing one of the possible state during a synchronization operation. */
	private ArrayList<AbstractState> stateTemplates = new ArrayList<AbstractState>();
	/** The current synchronization state. */
	private AbstractState currentState;
	/** The time interval in msec between two consecutive synchronization of the cache with the source of trust. */
	private int cacheRefreshTimeIntervalInMsec;
	/** The Redis template that facilitates the communication with Redis. */
	private StringRedisTemplate redisTemplate;
	/** The Redis channel to be used for the exchange of synchronization messages with the other application instances. */
	private ChannelTopic channel;
	/** The Java to JSON bi-directional converter. */
	private ObjectMapper objectMapper;
	
	
	/**
	 * Create a new StateContext instance and initialize it.
	 * @param aCacheRefreshTimeIntervalInMin The time interval in msec between
	 * two consecutive synchronization of the cache with the source of trust.
	 * @param aTaskScheduler Non-null task scheduler.
	 * @param aChannel Non-null Redis channel to be used for the exchange
	 * of synchronization messages with the other application instances.
	 */
	public StateContext(int aCacheRefreshTimeIntervalInSec, TaskScheduler aTaskScheduler, StringRedisTemplate aRedisTemplate, ChannelTopic aChannel) {
		taskScheduler = aTaskScheduler;
		redisTemplate = aRedisTemplate;
		channel = aChannel;
		objectMapper = new ObjectMapper();
		invalidateServiceId();
		cacheRefreshTimeIntervalInMsec = aCacheRefreshTimeIntervalInSec * 1000;
		for (Class<? extends AbstractState> possibleState : ALL_STATES) {
			stateTemplates.add(createState(possibleState));
		}
		changeState(SyncAnnounceState.class, 0);
	}
	
	/**
	 * Get the Redis channel to be used for the exchange of synchronization 
	 * messages with the other application instances.
	 * @return Non-null channel topic.
	 */
	public ChannelTopic getChannel() {
		return channel;
	}
	
	/**
	 * Get unique identifier of this service instance. That identifier 
	 * determines which service instance will have precedence over the other 
	 * instances to synchronize the cache with the source of trust. To give 
	 * another service instance that right, the identifier needs to be changed
	 * (see {@link #invalidateServiceId()}).  
	 * @return The unique identifier assigned to this service instance.
	 */
	long getServiceId() {
		return serviceId;
	}

	/**
	 * Get the task scheduler.
	 * @return Non-null task scheduler.
	 */
	TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}
	
	/**
	 * Determine if another service instance has precedence over this service
	 * to synchronize the cache with the source of trust. The precedence is
	 * determined by comparing the unique identifier of both service instances 
	 * (see {@link #getServiceId()}).
	 * @param anOtherServiceId The unique identifier assigned to the other 
	 * service instance.
	 * @return Flag, true if that other service has precedence, false if this 
	 * service has precedence.
	 * @see #hasThisServicePrecedence(long)
	 */
	boolean hasOtherServicePrecedence(long anOtherServiceId) {
		return !hasThisServicePrecedence(anOtherServiceId);
	}
	
	/**
	 * Determine if this service instance has precedence over another service
	 * to synchronize the cache with the source of trust. The precedence is
	 * determined by comparing the unique identifier of both services instances
	 * (see {@link #getServiceId()}).
	 * @param anOtherServiceId The unique identifier assigned to the other 
	 * service instance.
	 * @return Flag, true if this service has precedence, false if the other 
	 * service has precedence.
	 * @see #hasOtherServicePrecedence(long)
	 */
	boolean hasThisServicePrecedence(long anOtherServiceId) {
		return serviceId<anOtherServiceId; 
	}
	
	/**
	 * Get the sum of the minimum life time of a given set of states. Some 
	 * states are transient and are used to notify the other services instances
	 * about an operation that this service instance wants to perform. During
	 * that state, the other service instances may reply to forbid this instance
	 * to execute the operation. If no service makes such reply then after some
	 * time the state will automatically be moved to another that will perform
	 * the operation.
	 * @param aStateTypes Non-null array of one or more states.
	 * @return A positive number of msec.
	 */
	int getSumMinLifeTimeInMsec(Class<? extends AbstractState>... aStateTypes) {
		int rslt = 0;
		for (Class<? extends AbstractState> stateType : aStateTypes) {
			for (AbstractState state : stateTemplates) {
				if (stateType.equals(state.getClass())) {
					rslt += state.getMinLifeTimeInMsec();
					break;
				}
			}
		}
		return rslt;
	}
	
	/**
	 * Get the class that implements a state given its code.
	 * @param aStateCode Non-null unique identifier of a state.
	 * @return Nullable class that represents that state.
	 */
	Class<? extends AbstractState> getStateTypeOfCode(String aStateCode) {
		for (AbstractState state : stateTemplates) {
			if (aStateCode.equals(state.getStateCode())) {
				return state.getClass();
			}
		}
		return null;
	}
	
	/**
	 * Create an instance of a given class that represents a state in the 
	 * synchronization operation of the cache with the source of trust.
	 * @param aStateType Non-null type of the class to instantiate.
	 * @return Non-null AbstractState.
	 */
	private AbstractState createState(Class<? extends AbstractState> aStateType) {
		try {
			return aStateType.getDeclaredConstructor(StateContext.class).newInstance(this);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Change the current synchronization state to a given one but perform that
	 * transition only after a given amount of time.
	 * @param aNewState The non-null new synchronization state.
	 * @param aTimeInMsecToWaitForNewStateApplied The time in msec to wait
	 * before performing the state transition.
	 */
	private synchronized void changeState(AbstractState aNewState, int aTimeInMsecToWaitForNewStateApplied) {
		StateContext that = this;
		taskScheduler.schedule(new Runnable() {
			public void run() {
				synchronized(that) {
					currentState = aNewState;
					currentState.onStateEnter();
				}
			}
		}, addOffsetToCurrentTimestamp(aTimeInMsecToWaitForNewStateApplied));
	}

	/**
	 * Change the current synchronization state to a given one but perform that
	 * transition only after a given amount of time.
	 * @param aNewStateType Non-null type of the class that represents the new
	 * state.
	 * @param aTimeInMsecToWaitForNewStateApplied The time in msec to wait
	 * before performing the state transition.
	 */
	synchronized void changeState(Class<? extends AbstractState> aNewStateType, int aTimeInMsecToWaitForNewStateApplied) {
		changeState(createState(aNewStateType), aTimeInMsecToWaitForNewStateApplied);
	}
	
	/**
	 * Return a date that is the current time with the addition of a given 
	 * number of msec.
	 * @param anOffsetInMsec A positive or negative number of msec.
	 * @return Non-null date.
	 */
	Date addOffsetToCurrentTimestamp(int anOffsetInMsec) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MILLISECOND, anOffsetInMsec);
		return now.getTime();
	}
	
	/**
	 * Broadcast the current synchronization state of this service instance to 
	 * all the external service instances. These later instances will be 
	 * informed of the state of this instance and can perform some actions (like
	 * asking this service to cancel operations or cancel their own operations).
	 */
	void broadcastCurrentState() {
		try {
			redisTemplate.convertAndSend(channel.getTopic(), objectMapper.writeValueAsString(new SyncMessage(serviceId, currentState.getStateCode())));
		} catch (JsonProcessingException ex) {
			
		}
	}
	
	/**
	 * Invalidate the unique identifier associated with this service instance.
	 * That identifier is important because it will determine which service
	 * instance has precedence over the other instances to perform the 
	 * synchronization of the cache with the source of trust.
	 * By calling this method, this service instance will lose any precedence.
	 * This gives other service instances the opportunity to execute the
	 * synchronization. Typically this method will be invoked when this instance
	 * fails to perform the synchronization.
	 */
	void invalidateServiceId() {
		serviceId = System.currentTimeMillis();
	}
	
	/**
	 * Get the time interval in msec between two consecutive synchronization of 
	 * the cache with the source of trust.
	 * @return A positive number of msec.
	 */
	int getCacheRefreshTimeIntervalInMsec() {
		return cacheRefreshTimeIntervalInMsec;
	}

	//**************************************************************************
	//**** MessageListener implementation **************************************
	//**************************************************************************
	
	@Override
	public synchronized void onMessage(Message aMessage, byte[] aPattern) {
		try {
			SyncMessage syncMessage = objectMapper.readValue(aMessage.getBody(), SyncMessage.class);
			currentState.onMessage(syncMessage.getServiceId(), getStateTypeOfCode(syncMessage.getStatusCode()));
		} catch (IOException ex) {
			
		}
		
	}
}
