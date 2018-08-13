package com.ddv.test.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TXNLOCK")
public class Lock {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TXN_ID", precision=0)
	private Long txnId;
	
	@Column(name="USER_ID", precision=0)
	private Long userId;
	
	@Embedded
	private Tmp tmp;
	
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

	public Tmp getTmp() {
		return tmp;
	}
	public void setTmp(Tmp aTmp) {
		tmp = aTmp;
	}
	
	@Embeddable
	public static class Tmp {
		@Column(name="START_TIMESTAMP")
		private LocalDateTime startTimestamp;
		
		@Column(name="ACTION", length=20)
		private String action;


		public LocalDateTime getStartTimestamp() {
			return startTimestamp;
		}

		public void setStartTimestamp(LocalDateTime startTimestamp) {
			this.startTimestamp = startTimestamp;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}
		
	}
}
