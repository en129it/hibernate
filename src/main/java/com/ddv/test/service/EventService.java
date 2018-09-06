package com.ddv.test.service;

import java.util.Collection;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ddv.test.model.SseService;
import com.ddv.test.service.IEventReplicator.EventDeliveryCallback;;

@Service
public class EventService {

	private TreeMap<Integer, TxnSseEvent> eventIdToEventMap = new TreeMap<Integer, TxnSseEvent>();
	
	@Autowired
	private IdGenerator idGenerator;
	@Autowired(required=false)
	private IEventReplicator eventReplicator;
	@Autowired(required=false)
	private SseService sseService;
	@Value("${event.cache.capacity}")
	private int maxCapacity;

	@PostConstruct
	public void initService() {
		if (eventReplicator!=null) {
			eventReplicator.subscribe(new EventDeliveryCallback() {			
				@Override
				public void onEvent(int anId, TxnSseEvent anEvent) {
					addEvent(anId, anEvent);
				}
			});
		}
	}

	public void emitEvent(TxnSseEvent anEvent) {
		int id = idGenerator.getNextId();
		addEvent(id, anEvent);
		if (eventReplicator!=null) {
			eventReplicator.emitEvent(id, anEvent);
		}
	}
	
	private void addEvent(int anId, TxnSseEvent anEvent) {
		eventIdToEventMap.put(anId, anEvent);
		trimCache();
		if (sseService!=null) {
			sseService.emitEvent(anEvent);
		}
	}
	
	public Collection<TxnSseEvent> getTailEvents(int anId) {
		return eventIdToEventMap.tailMap(anId, true).values();
	}
	
	private void trimCache() {
		int diff = eventIdToEventMap.size() - maxCapacity;
		if (diff>0) {
			removeSmallestIdEvents(diff);
		}
	}
	
	private void removeSmallestIdEvents(int aCount) {
		for (int i=0; i<aCount; i++) {
			eventIdToEventMap.pollFirstEntry();
		}
	}
}
