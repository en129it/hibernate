package com.ddv.test;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactoryExt;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecureProxyConfiguration {

	@Bean
	public PoolingHttpClientConnectionManager getConnectionManager() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(null, null, null);
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).build();

			PoolingHttpClientConnectionManager rslt = new PoolingHttpClientConnectionManager(registry);
			rslt.setDefaultMaxPerRoute(50);
			rslt.setMaxTotal(500);
			
		} catch (Exception ex) {
			
		}
		return null;
	}
	
	@Bean
	public HttpClient getHttpClient() {
		return HttpClientBuilder.create().setConnectionManager(getConnectionManager()).disableCookieManagement().disableRedirectHandling().build();
	}
	
	@Bean
	public ClientHttpRequestFactory getHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactoryExt rslt = new HttpComponentsClientHttpRequestFactoryExt(getHttpClient());
		rslt.setConnectionRequestTimeout(30000);
		rslt.setConnectTimeout(30000);
		rslt.setReadTimeout(30000);
		return rslt;
	}
	
	@Bean
	public RestOperations createRestOperations() {
		RestTemplate restTemplate = new RestTemplate(getHttpRequestFactory());
		return restTemplate;
	}
}
