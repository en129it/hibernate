package com.ddv.test.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LOCK")
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
	
}
