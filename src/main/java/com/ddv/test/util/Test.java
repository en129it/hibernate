package com.ddv.test.util;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestOperations;

public class Test {

}
/**


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
		
		restOperations.execute(url, HttpMethod.GET, new RequestCallback() {
			
			@Override
			public void doWithRequest(ClientHttpRequest request) throws IOException {
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
				int byteCount = 0;
				byte[] buffer = new byte[100];
				int bytesRead = -1;
				while ((bytesRead = response.getBody().read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
					printOut(buffer, bytesRead);
					byteCount += bytesRead;
					out.flush();
				}
				out.flush();
				return null;
			}
		}, 
		new Object[0]);
		return null;
	}
	
}


*/