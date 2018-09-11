package com.ddv.test.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ddv.test.entity.Lock;
import com.ddv.test.entity.Transaction;
import com.ddv.test.model.TxnSearchFilter;
import com.ddv.test.service.EventService;
import com.ddv.test.service.TransactionService;
import com.ddv.test.service.TxnSseEvent;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService service;
	@Autowired
	private EventService eventService;
	
	@RequestMapping(path="txn/{txnId}/lock/{userId}/{action}", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void acquireLock(@PathVariable("txnId") Long aTxnId, @PathVariable("userId") Long aUserId, @PathVariable("action") String aLockAction) {
		System.out.println("########################### acquirelock");
	}
	
	@RequestMapping(path="txn/{txnId}/unlock/{userId}", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void releaseLock(@PathVariable("txnId") Long aTxnId, @PathVariable("userId") Long aUserId) {
		
	}
	
	@RequestMapping(path="txn/{txnId}/repaired", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void txnRepaired(@PathVariable("txnId") Long aTxnId) {
		
	}
	
	@RequestMapping(path="txn", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Transaction> getTxns(HttpServletResponse aResponse) throws IOException {
		aResponse.sendRedirect("http://localhost:9090/hello");
		return service.findTransactions();
	}

	@RequestMapping(path="txn/insertdata", method=RequestMethod.GET)
	public void init() {
		service.init();
	}

	@RequestMapping(path="txn/firstlock", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Lock findFirstLock() {
		return service.findFirstLock();
	}

	@RequestMapping(path="txn/delealllocks", method=RequestMethod.GET)
	public void deleteAllLocks() {
		int rslt = service.deleteAllLocks();
		System.out.println("####### nb deleted locks = " + rslt);
	}

	@RequestMapping(path="redis", method=RequestMethod.GET)
	public void redis() {
		eventService.emitEvent(new TxnSseEvent("lock", 123L));
		System.out.println("####### event emitted");
	}
	
}

