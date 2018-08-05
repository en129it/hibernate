package com.ddv.test.service.state;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.springframework.scheduling.TaskScheduler;

public class StateContext {

	private static Class<? extends AbstractState>[] ALL_STATES = new Class[]{SyncAnnounceState.class, SynchronizingState.class, SynchronizedState.class};
	
	private long serviceId;
	private TaskScheduler taskScheduler;
	private ArrayList<AbstractState> stateTemplates = new ArrayList<AbstractState>();
	private AbstractState currentState;
	private int cacheRefreshTimeIntervalInMsec;
	
	
	public StateContext(int aCacheRefreshTimeIntervalInMin, TaskScheduler aTaskScheduler) {
		taskScheduler = aTaskScheduler;
		invalidateServiceId();
		cacheRefreshTimeIntervalInMsec = aCacheRefreshTimeIntervalInMin * 60000;
		for (Class<? extends AbstractState> possibleState : ALL_STATES) {
			stateTemplates.add(createState(possibleState));
		}
		changeState(SyncAnnounceState.class, 0);
	}
	
	long getServiceId() {
		return serviceId;
	}

	TaskScheduler getTaskScheduler() {
		return taskScheduler;
	}
	
	boolean hasOtherServicePrecedence(long anOtherServiceId) {
		return !hasThisServicePrecedence(anOtherServiceId);
	}
	
	boolean hasThisServicePrecedence(long anOtherServiceId) {
		return serviceId<anOtherServiceId; 
	}
	
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
	
	Class<? extends AbstractState> getStateTypeOfCode(String aStateCode) {
		for (AbstractState state : stateTemplates) {
			if (aStateCode.equals(state.getStateCode())) {
				return state.getClass();
			}
		}
		return null;
	}
	
	private AbstractState createState(Class<? extends AbstractState> aStateType) {
		try {
			return aStateType.getDeclaredConstructor(StateContext.class).newInstance(this);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
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

	synchronized void changeState(Class<? extends AbstractState> aNewStateType, int aTimeInMsecToWaitForNewStateApplied) {
		changeState(createState(aNewStateType), aTimeInMsecToWaitForNewStateApplied);
	}
	
	public synchronized void onMessage(long anOtherInstanceId, String anOtherStateCode) {
		currentState.onMessage(anOtherInstanceId, getStateTypeOfCode(anOtherStateCode));
	}
	
	Date addOffsetToCurrentTimestamp(int anOffsetInMsec) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MILLISECOND, anOffsetInMsec);
		return now.getTime();
	}
	
	void broadcastCurrentState() {
		// Broadcast message (serviceId, currentState.getStatusCode())
	}
	
	void invalidateServiceId() {
		serviceId = System.currentTimeMillis();
	}
	
	int getCacheRefreshTimeIntervalInMsec() {
		return cacheRefreshTimeIntervalInMsec;
	}
}
