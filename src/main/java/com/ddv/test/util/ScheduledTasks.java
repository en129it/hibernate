package com.ddv.test.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ddv.test.model.SseService;
import com.ddv.test.service.TxnSseEvent;

@Component
public class ScheduledTasks {

	
	@Autowired
	private SseService sseService;
	
	@Scheduled(fixedDelay=3000)
	public void schedule() {
//		System.out.println("#### schedule " + System.currentTimeMillis());
		sseService.emitEvent(new TxnSseEvent("HEARTBEAT", System.currentTimeMillis()));
	}
	
}
