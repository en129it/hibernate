package com.ddv.test.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ddv.test.CountryType;
import com.ddv.test.HeaderType;
import com.ddv.test.MyRequest;
import com.ddv.test.ObjectFactory;
import com.ddv.test.dao.TransactionDao;
import com.ddv.test.entity.Lock;
import com.ddv.test.entity.Transaction;

@Service
//@javax.transaction.Transactional
public class TransactionService {

	@Autowired
	private TransactionDao dao;
	@Autowired
	private RestTemplate template;
	@Autowired
	private RedisConnectionFactory factory;
	
	@PersistenceContext
	private EntityManager manager;
	
	public List<Transaction> findTransactions() {
		
		System.out.println("#### entityManager " + manager);
		
		
		String url = "http://localhost:8192/api/test";
		
		ObjectFactory factory = new ObjectFactory();
		HeaderType headerType = factory.createHeaderType();
		headerType.setFrom("MyFrom");
		headerType.setTo("MyTo");
		CountryType countryType = factory.createCountryType();
		countryType.setIsoCode("LU");
		countryType.setName("Luxem");
		MyRequest root = factory.createMyRequest();
		root.setId("MyId");
		root.setHeader(headerType);
		root.setCountry(countryType);
		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE);
		
		HttpEntity<MyRequest> entity = new HttpEntity<MyRequest>(root, headers);
		
		MyRequest rslt = template.postForObject(url, entity, MyRequest.class, (Object)null);
		
		System.out.println(rslt);
		
		return dao.findTransactions();
	}	
	
	@Transactional
	public void init() {
		dao.init();
		
		
		System.out.println("######### factory " + factory);
		factory.getConnection().set("toto".getBytes(), "45".getBytes());
		String rslt = new String(factory.getConnection().get("toto".getBytes()));
		System.out.println("######### rslt " + rslt);
		
	}
	
	public Lock findFirstLock() {
		return dao.getFirstLock();
	}
	
	public int deleteAllLocks() {
		return dao.deleteAllLocks();
	}
}
