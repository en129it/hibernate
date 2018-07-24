package com.ddv.test.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ddv.test.entity.Transaction;
import com.ddv.test.model.TxnSearchFilter;
import com.ddv.test.service.TransactionService;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService service;
	
	@RequestMapping(path="txn/{txnId}/lock/{userId}/{action}", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void acquireLock(@PathVariable("txnId") Long aTxnId, @PathVariable("userId") Long aUserId, @PathVariable("action") String aLockAction) {
		
	}
	
	@RequestMapping(path="txn/{txnId}/unlock/{userId}", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void releaseLock(@PathVariable("txnId") Long aTxnId, @PathVariable("userId") Long aUserId) {
		
	}
	
	@RequestMapping(path="txn/{txnId}/repaired", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void txnRepaired(@PathVariable("txnId") Long aTxnId) {
		
	}
	
	@RequestMapping(path="txn", method=RequestMethod.GET, consumes=MediaType.APPLICATION_JSON_UTF8_VALUE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Transaction> getTxns(@RequestBody TxnSearchFilter aFilter) {
		return service.findTransactions();
	}
	
}

