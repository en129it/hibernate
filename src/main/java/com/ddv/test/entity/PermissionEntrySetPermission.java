package com.ddv.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="PERMISSION_ENTRY_SET_PERM")
public class PermissionEntrySetPermission {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PERMISSION_ENTRY_SET_PERM_ID", precision=19)
	private Long permissionEntrySetPermissionId;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="PERMISSION_ENTRY_SET_ID", nullable=false, updatable=false)
	private PermissionEntrySet permissionEntrySet;
	
	@Column(name="PERMISSION_ID", length=50)
	private String permissionId;
	
	@Column(name="TXN_TYPE_ID", length=22)
	private String txnTypeId;

	public Long getPermissionEntrySetPermissionId() {
		return permissionEntrySetPermissionId;
	}

	public void setPermissionEntrySetPermissionId(Long permissionEntrySetPermissionId) {
		this.permissionEntrySetPermissionId = permissionEntrySetPermissionId;
	}

	public PermissionEntrySet getPermissionEntrySet() {
		return permissionEntrySet;
	}

	public void setPermissionEntrySet(PermissionEntrySet permissionEntrySet) {
		this.permissionEntrySet = permissionEntrySet;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public String getTxnTypeId() {
		return txnTypeId;
	}

	public void setTxnTypeId(String txnTypeId) {
		this.txnTypeId = txnTypeId;
	}
	
	
}
