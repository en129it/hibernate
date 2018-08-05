package com.ddv.test.service.state;

/**
 * SynchronizedState represents the synchronization step after a successful
 * synchronization of the cache with the data source of trust. This state will
 * then remains until the cache needs to be resynchronized. That information is
 * given by the refresh time interval of the cache. When that point is reached,
 * the state is moved to {@link SyncAnnounceState}. 
 * During all its life this state replies to messages received from the other 
 * services with information about its status.
 */
public class SynchronizedState extends AbstractState {

	public static final String STATE_CODE = "SYNCHRONIZED";
	
	/**
	 * Create a new SynchronizedState instance.
	 * @param aContext Non-null context that gives access synchronization 
	 * context information.
	 */
	public SynchronizedState(StateContext aContext) {
		super(aContext);
	}

	@Override
	public String getStateCode() {
		return STATE_CODE;
	}

	@Override
	public int getMinLifeTimeInMsec() {
		return context.getCacheRefreshTimeIntervalInMsec();
	}

	@Override
	public void onStateEnter() {
		context.broadcastCurrentState();
		context.changeState(SyncAnnounceState.class, getMinLifeTimeInMsec());
	}

	@Override
	public void onMessage(long anOtherServiceId, Class<? extends AbstractState> anOtherStateType) {
		context.broadcastCurrentState();
	}
	
}
