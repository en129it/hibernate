package com.ddv.test;

import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class RestTemplateExt extends RestTemplate {

	@Override
	protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor) throws RestClientException {
		// TODO Auto-generated method stub
		return super.doExecute(url, method, requestCallback, responseExtractor);
	}
	
}
