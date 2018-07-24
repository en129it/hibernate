package com.ddv.test.util;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;

import com.ddv.test.model.SseService;

@WebListener
public class SessionLifecycleListener implements HttpSessionListener {

	@Autowired
	private SseService sseService;
	
	@Override
	public void sessionCreated(HttpSessionEvent anEvent) {
		System.out.println("###### session created " + anEvent.getSession().getId());
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent anEvent) {
		sseService.removeSseEmitterForSession(anEvent.getSession().getId());
		System.out.println("###### session destroyed " + anEvent.getSession().getId());
		// TODO Auto-generated method stub
		
	}

}
