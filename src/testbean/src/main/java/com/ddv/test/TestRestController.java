package com.ddv.test;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpClientConnection;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestDelegate;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestOperations;

@RestController
@RequestMapping("proxy")
public class TestRestController {

	@Autowired
	private RestOperations restOperations;
	
	@RequestMapping(value="/**")
	public Object test(HttpServletRequest aRequest, HttpServletResponse aResponse) throws Exception {
		
		String string = aRequest.getServletPath();
		System.out.println("################ " + string);
		string = string.substring("/proxy/".length());
		String url = "http://localhost:8080/" + string;
		System.out.println("################ Final " + url);
		

		ServletOutputStream out = aResponse.getOutputStream();
		aResponse.setContentType("text/event-stream;charset=UTF-8");
		aResponse.setHeader("Transfer-Encoding", "chuncked");
		aResponse.setStatus(HttpStatus.OK.value());
		
		HttpContext[] httpContext = new HttpContext[1];
		
		restOperations.execute(url, HttpMethod.GET, new RequestCallback() {
			
			@Override
			public void doWithRequest(ClientHttpRequest aRequest) throws IOException {
				if (aRequest instanceof ClientHttpRequestDelegate) {
					httpContext[0] = ((ClientHttpRequestDelegate)aRequest).getHttpContext();
				}
			}
		}, new ResponseExtractor<Object>() {

			private void printOut(byte[] buffer, int count) {
				for (int i=0; i<count; i++) {
					System.out.print((char)buffer[i]);
				}
				System.out.println();
			}
			
			@Override
			public Object extractData(ClientHttpResponse response) throws IOException {
				try {
					byte[] buffer = new byte[100];
					int bytesRead = -1;
					while ((bytesRead = response.getBody().read(buffer)) != -1) {
						try {
							out.write(buffer, 0, bytesRead);
							printOut(buffer, bytesRead);
							out.flush();
						} catch (IOException ex) {
							// Case : an error occurred while writing to the Servlet response's output stream
							// Action : close the connection with the proxied application. This can be done
							// using InputStream.close() but for a ChunkedInputStream this will result in the 
							// thread that calls the close() to be blocked indefinitely. This is because
							// ChunkedInputStream close() implementation performs the close only if no data is
							// coming which is never the case for long polling or SSE. So the solution is to
							// close the connection instead
							HttpClientConnection connection = (HttpClientConnection)httpContext[0].getAttribute(HttpCoreContext.HTTP_CONNECTION);
							connection.shutdown();
						}
					}
					out.flush();
					return null;
				} catch (Exception ex) {
					// Case : an exception raised either because the proxied application closed the connection or either because this thread
					// forced its closure because of an error detected with the Servlet response's output stream
					return null;
				}
			}
		}, 
		new Object[0]);
		return null;
	}
	
}
