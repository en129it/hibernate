package com.ddv.test.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddv.test.dao.TransactionDao;
import com.ddv.test.entity.Transaction;

@Service
@Transactional
public class TransactionService {

	@Autowired
	private TransactionDao dao;
	
	@PersistenceContext
	private EntityManager manager;
	
	public List<Transaction> findTransactions() {
		
		System.out.println("#### entityManager " + manager);
		
		return dao.findTransactions();
	}	
	
}
