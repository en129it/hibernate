package com.ddv.test.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.ddv.test.entity.PermissionEntrySet;
import com.ddv.test.entity.PermissionEntrySetAccount;
import com.ddv.test.entity.PermissionEntrySetPermission;
import com.ddv.test.model.Account;
import com.ddv.test.model.Permission;

public class PermissionDao {

	@Autowired
	private DaoSupport daoSupport;
	
	public boolean updateLastAccessTimestamp(String aGlobalSessionId, LocalDateTime aTimestamp) {
		PermissionEntrySet permissionEntrySet = daoSupport.getHibernateTemplate().get(PermissionEntrySet.class, aGlobalSessionId);
		if (permissionEntrySet!=null) {
			permissionEntrySet.setLastAccessTimestamp(aTimestamp);
			return true;
		}
		return false;
	}
	
	public void insertPermissionEntrySet(PermissionEntrySet aPermissionEntrySet) {
		daoSupport.getHibernateTemplate().saveOrUpdate(aPermissionEntrySet);
	}

	public void insertPermissionEntrySetAccount(PermissionEntrySet aPermissionEntrySet, List<Account> anAccountList) {
		StatelessSession statelessSession = daoSupport.getHibernateTemplate().getSessionFactory().openStatelessSession();
		try {
			for (Account account : anAccountList) {
				PermissionEntrySetAccount entity = new PermissionEntrySetAccount();
				entity.setAccountId(account.getAccountId());
				entity.setAccountName(account.getAccountName());
				entity.setPtgCode(account.getPtgCode());
				entity.setPermissionEntrySet(aPermissionEntrySet);
				statelessSession.insert(entity);
			}
		} finally {
			statelessSession.close();
		}
	}
	
	public void insertPermissionEntrySetPermission(PermissionEntrySet aPermissionEntrySet, List<Permission> aPermissionList) {
		StatelessSession statelessSession = daoSupport.getHibernateTemplate().getSessionFactory().openStatelessSession();
		try {
			for (Permission permission : aPermissionList) {
				PermissionEntrySetPermission entity = new PermissionEntrySetPermission();
				entity.setPermissionId(permission.getPermissionId());
				entity.setTxnTypeId(permission.getTxnTypeId());
				entity.setPermissionEntrySet(aPermissionEntrySet);
				statelessSession.insert(entity);
			}
		} finally {
			statelessSession.close();
		}
	}
	
}
