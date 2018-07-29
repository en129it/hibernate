package com.ddv.test.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TXNLOCK")
public class Lock {

	@Id
	@Column(name="TXN_ID", precision=0)
	private Long txnId;
	
	@Column(name="USER_ID", precision=0)
	private Long userId;
	
	@Column(name="START_TIMESTAMP")
	private Date startTimestamp;
	
	@Column(name="ACTION", length=20)
	private String action;

	public Long getTxnId() {
		return txnId;
	}

	public void setTxnId(Long txnId) {
		this.txnId = txnId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(Date startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
