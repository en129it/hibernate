package com.ddv.test.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ddv.test.service.TxnSseEvent;

@Service
public class SseService {

	private HashMap<String, SseEmitter> sessionIdToSseEmitterMap = new HashMap<String, SseEmitter>();
	
	public synchronized SseEmitter createSseEmitterForSession(String aSessionId) {
		SseEmitter rslt = new SseEmitter();
		sessionIdToSseEmitterMap.put(aSessionId, rslt);
		return rslt;
	}
	
	public synchronized void removeSseEmitterForSession(String aSessionId) {
		sessionIdToSseEmitterMap.remove(aSessionId);
	}
	
	public synchronized void removeSseEmitter(SseEmitter anEmitter) {
		Map.Entry<String, SseEmitter> entry = null;
		Iterator<Map.Entry<String, SseEmitter>> iter = sessionIdToSseEmitterMap.entrySet().iterator();
		while (iter.hasNext()) {
			entry = iter.next();
			if (entry.getValue().equals(anEmitter)) {
				iter.remove();
				break;
			}
		}
	}
	
	public synchronized void emitEvent(TxnSseEvent anEvent) {
		ArrayList<SseEmitter> brokenEmitterList = null; 
		for (SseEmitter emitter : sessionIdToSseEmitterMap.values()) {
			try {
				emitter.send(anEvent, MediaType.APPLICATION_JSON_UTF8);
			} catch (Exception ex) {
				if (brokenEmitterList==null) {
					brokenEmitterList = new ArrayList<SseEmitter>();
				}
				brokenEmitterList.add(emitter);
			}
		}
		
		if (brokenEmitterList!=null) {
			for (SseEmitter brokenEmitter : brokenEmitterList) {
				removeSseEmitter(brokenEmitter);
			}
		}
	}
}
