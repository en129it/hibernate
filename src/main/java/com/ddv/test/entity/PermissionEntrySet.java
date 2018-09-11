package com.ddv.test.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="PERMISSION_ENTRY_SET")
public class PermissionEntrySet {

	@Column(name="GLOBAL_SESSION_ID", length=36)
	private String globalSessionId;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PERMISSION_ENTRY_SET_ID", precision=19)
	private Long permissionEntrySetId;
	
	@Column(name="LAST_ACCESS_TIMESTAMP")
	private LocalDateTime lastAccessTimestamp;

	public String getGlobalSessionId() {
		return globalSessionId;
	}

	public void setGlobalSessionId(String globalSessionId) {
		this.globalSessionId = globalSessionId;
	}

	public Long getPermissionEntrySetId() {
		return permissionEntrySetId;
	}

	public void setPermissionEntrySetId(Long permissionEntrySetId) {
		this.permissionEntrySetId = permissionEntrySetId;
	}

	public LocalDateTime getLastAccessTimestamp() {
		return lastAccessTimestamp;
	}

	public void setLastAccessTimestamp(LocalDateTime lastAccessTimestamp) {
		this.lastAccessTimestamp = lastAccessTimestamp;
	}
	
	
}
