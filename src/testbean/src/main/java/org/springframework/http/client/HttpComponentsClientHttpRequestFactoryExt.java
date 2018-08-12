package org.springframework.http.client;

import java.io.IOException;
import java.net.URI;

import org.apache.http.client.HttpClient;
import org.springframework.http.HttpMethod;

/**
 * HttpComponentsClientHttpRequestFactoryExt is an extension of 
 * {@link HttpComponentsClientHttpRequestFactory} that decorates the by this
 * factory created {@link ClientHttpRequest} with a 
 * {@link ClientHttpRequestDelegate}. This is done to gain access to the HTTP
 * context which can give access to the HTTP connection.
 *
 */
public class HttpComponentsClientHttpRequestFactoryExt extends HttpComponentsClientHttpRequestFactory {

	public HttpComponentsClientHttpRequestFactoryExt(HttpClient aHttpClient) {
		super(aHttpClient);
	}
	
	@Override
	public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
		return new ClientHttpRequestDelegate(super.createRequest(uri, httpMethod));
	}
	
}
