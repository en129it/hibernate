package org.springframework.http.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;

/**
 * ClientHttpRequestDelegate delegates all operations to another 
 * {@link ClientHttpRequest}.
 *
 */
public class ClientHttpRequestDelegate implements ClientHttpRequest {

	private ClientHttpRequest delegate;
	
	/**
	 * Create a new ClientHttpRequestDelegate instance.
	 * @param aDelegate Non-null delegate.
	 */
	public ClientHttpRequestDelegate(ClientHttpRequest aDelegate) {
		delegate = aDelegate;
	}

	/**
	 * Get the underlying HTTP context. This method returns a non-null value 
	 * only if the delegate is of type {@link HttpComponentsClientHttpRequest}.
	 * @return Nullable HTTP context.
	 */
	public HttpContext getHttpContext() {
		if (delegate instanceof HttpComponentsClientHttpRequest) {
			return ((HttpComponentsClientHttpRequest)delegate).getHttpContext();
		}
		return null;
	}
	
	@Override
	public HttpMethod getMethod() {
		return delegate.getMethod();
	}

	@Override
	public URI getURI() {
		return delegate.getURI();
	}

	@Override
	public HttpHeaders getHeaders() {
		return delegate.getHeaders();
	}

	@Override
	public OutputStream getBody() throws IOException {
		return delegate.getBody();
	}

	@Override
	public ClientHttpResponse execute() throws IOException {
		return delegate.execute();
	}

}
