package com.ddv.test.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ddv.test.model.SseService;

/**
 * SseController is a controller exposing one REST end point to subscribe to 
 * SSE (Server Send Event) emitted events.
 *
 */
@RestController
public class SseController {

	@Autowired
	private SseService sseService;
	
    @RequestMapping(path="proxy/sse/subscribe", method=RequestMethod.GET)
	public SseEmitter subscribe(HttpServletRequest aRequest, HttpServletResponse aResponse) throws Exception {
    	aResponse.sendRedirect("https://www.google.com");
    	return null;
    	
//    	System.out.println("############################### subscribe");
//		return sseService.createSseEmitterForSession(createHttpSession(aRequest).getId());
	}

	private HttpSession createHttpSession(HttpServletRequest aRequest) {
		return aRequest.getSession(true);
	}
	
}
