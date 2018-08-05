package com.ddv.test.service.state;

import net.bytebuddy.description.modifier.SynchronizationState;

/**
 * SyncAnnounceState represents the synchronization step during which this 
 * service announce to the other services its intent to start a synchronization
 * of the cache with the data source of trust. This state will remain for a 
 * specific time period during which the other services can sent responses that
 * will determine if this service can proceed or not with the synchronization
 * itself. In that case the state will be changed to the 
 * {@link SynchronizationState} state otherwise the current state remains for 
 * a minimum guaranteed period after which a new synchronization announcement is
 * issued.  
 */
public class SyncAnnounceState extends AbstractState {

	public static final String STATE_CODE = "SYNC_ANNOUNCEMENT";
	private static final int ANNOUNCEMENT_PERIOD_IN_MSEC = 1000;
	private int timeInMsecBeforeRedoAnnouncement;
	
	/**
	 * Create a new SyncAnnounceState instance.
	 * @param aContext Non-null context that gives access synchronization 
	 * context information.
	 */
	public SyncAnnounceState(StateContext aContext) {
		super(aContext);
	}
	
	@Override
	public String getStateCode() {
		return STATE_CODE;
	}

	@Override
	public int getMinLifeTimeInMsec() {
		return ANNOUNCEMENT_PERIOD_IN_MSEC;
	}
	
	
	@Override
	public synchronized void onStateEnter() {
		timeInMsecBeforeRedoAnnouncement = -1;
		
		SyncAnnounceState that = this;
		
		context.broadcastCurrentState();
		context.getTaskScheduler().schedule(new Runnable() {
			public void run() {
				synchronized(that) {
					if (timeInMsecBeforeRedoAnnouncement>-1) {
						context.getTaskScheduler().schedule(new Runnable() {
							public void run() {
								onStateEnter();
							}
						}, context.addOffsetToCurrentTimestamp(timeInMsecBeforeRedoAnnouncement));
					} else {
						context.changeState(SynchronizingState.class, 0);
					}
				}
			}
		}, context.addOffsetToCurrentTimestamp(ANNOUNCEMENT_PERIOD_IN_MSEC));
	}
	
	@Override
	public synchronized void onMessage(long anOtherServiceId, Class<? extends AbstractState> anOtherStateType) {
		if (context.hasOtherServicePrecedence(anOtherServiceId)) {

			int timeInMsecBeforeRedoAnnouncementInit = ANNOUNCEMENT_PERIOD_IN_MSEC;
			if ((SyncAnnounceState.class.equals(anOtherStateType)) || (SynchronizingState.class.equals(anOtherStateType))) {
				timeInMsecBeforeRedoAnnouncementInit = context.getSumMinLifeTimeInMsec(SyncAnnounceState.class, SynchronizingState.class);
			} else if (SynchronizedState.class.equals(anOtherStateType)) {
				timeInMsecBeforeRedoAnnouncementInit = context.getSumMinLifeTimeInMsec(SyncAnnounceState.class, SynchronizedState.class);
			}
			timeInMsecBeforeRedoAnnouncement = Math.max(timeInMsecBeforeRedoAnnouncement, timeInMsecBeforeRedoAnnouncementInit);
		} else {
			context.broadcastCurrentState();
		}
	}
	
}
