package com.ddv.test.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	
	@Scheduled(fixedDelay=100)
	public void schedule() {
		System.out.println("#### schedule " + System.currentTimeMillis());
	}
	
}
