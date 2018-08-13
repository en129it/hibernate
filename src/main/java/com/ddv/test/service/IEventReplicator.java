package com.ddv.test.service;

public interface IEventReplicator {

	public void emitEvent(int anId, TxnSseEvent anEvent);
	
	public void subscribe(EventDeliveryCallback aCallback);
	
	public static interface EventDeliveryCallback {
		public void onEvent(int anId, TxnSseEvent anEvent);
	}
}
