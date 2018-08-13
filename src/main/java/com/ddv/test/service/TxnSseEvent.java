package com.ddv.test.service;

public class TxnSseEvent {

	private String eventType;
	private Long txnId;
	
	public TxnSseEvent() {
		
	}
	
	public TxnSseEvent(String eventType, Long txnId) {
		this.eventType = eventType;
		this.txnId = txnId;
	}
	
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public Long getTxnId() {
		return txnId;
	}
	public void setTxnId(Long txnId) {
		this.txnId = txnId;
	}
	
}
