package com.ddv.test.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ddv.test.dao.TransactionDao;
import com.ddv.test.entity.Transaction;

@RestController
public class TransactionController {

	@Autowired
	private TransactionDao dao;
	
	@RequestMapping(path="txn", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Transaction> getTxns() {
		return dao.findTransactions();
	}
	
}

