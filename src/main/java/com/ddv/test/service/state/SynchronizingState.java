package com.ddv.test.service.state;

/**
 * SynchronizingState represents the synchronization step during which this 
 * service is synchronizing the cache with the data source of trust. On 
 * successful synchronization the state becomes {@link SynchronizedState). In
 * the case of failure, the unique identifier of this synchronization service
 * is invalidated. That operation will give other services the opportunity to
 * perform synchronization on their turn. Without that invalidation, this
 * service would still keep the precedence over the other services to perform
 * synchronization. Finally on failure the state is changed to 
 * {@link SyncAnnounceState}. During all its life this state replies to messages
 * received from the other services with information about its status.
 */
public class SynchronizingState extends AbstractState {

	public static final String STATE_CODE = "SYNC_BUSY";
	private static final int SYNCHRONIZATION_MIN_PERIOD_IN_MSEC = 10000;
	
	/**
	 * Create a new SynchronizingState instance.
	 * @param aContext Non-null context that gives access synchronization 
	 * context information.
	 */
	public SynchronizingState(StateContext aContext) {
		super(aContext);
	}


	@Override
	public String getStateCode() {
		return STATE_CODE;
	}

	@Override
	public int getMinLifeTimeInMsec() {
		return SYNCHRONIZATION_MIN_PERIOD_IN_MSEC;
	}

	@Override
	public void onStateEnter() {
		context.broadcastCurrentState();
		try {
			// Perform synchronization
			context.changeState(SynchronizedState.class, 0);
		} catch (Exception ex) {
			context.invalidateServiceId();
			context.changeState(SyncAnnounceState.class, 0);
		}
	}

	@Override
	public void onMessage(long anOtherServiceId, Class<? extends AbstractState> anOtherStateType) {
		context.broadcastCurrentState();
	}
}
