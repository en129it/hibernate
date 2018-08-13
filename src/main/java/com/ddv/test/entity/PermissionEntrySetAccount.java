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
@Table(name="PERMISSION_ENTRY_SET_ACCT")
public class PermissionEntrySetAccount {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PERMISSION_ENTRY_SET_ACCOUNT_ID", precision=19)
	private Long permissionEntrySetAccountId;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="PERMISSION_ENTRY_SET_ID", nullable=false, updatable=false)
	private PermissionEntrySet permissionEntrySet;
	
	@Column(name="ACCOUNT_ID", precision=19)
	private Long accountId;
	
	@Column(name="ACCOUNT_NAME", length=50)
	private String accountName;
	
	@Column(name="PTG_CODE", length=50)
	private String ptgCode;

	public Long getPermissionEntrySetAccountId() {
		return permissionEntrySetAccountId;
	}

	public void setPermissionEntrySetAccountId(Long permissionEntrySetAccountId) {
		this.permissionEntrySetAccountId = permissionEntrySetAccountId;
	}

	public PermissionEntrySet getPermissionEntrySet() {
		return permissionEntrySet;
	}

	public void setPermissionEntrySet(PermissionEntrySet permissionEntrySet) {
		this.permissionEntrySet = permissionEntrySet;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getPtgCode() {
		return ptgCode;
	}

	public void setPtgCode(String ptgCode) {
		this.ptgCode = ptgCode;
	}
	
	
}
