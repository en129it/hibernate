package com.ddv.test.service.state;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class SyncAnnounceStateTest {

	private TaskScheduler taskScheduler;
	private StateContext stateContext;
	private SyncAnnounceState fixture;
	
	@Before
	public void init() {
		taskScheduler = Mockito.mock(TaskScheduler.class);
		stateContext = Mockito.mock(StateContext.class);
		fixture = new SyncAnnounceState(stateContext);
		
		when(stateContext.getTaskScheduler()).thenReturn(taskScheduler);
	}
	
	@Test
	public void testOnStateEnter_NotAbortedByOtherApplicationInstances() {
		final Runnable[] beforeRunnables = new Runnable[]{
			new Runnable() {
				public void run() {
					// no other application instance reply to the state change
				}
			}
		};
		final int[] posInBeforeRunnables = new int[]{0};
		
		
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Runnable runnable = (Runnable)invocation.getArguments()[0];
				beforeRunnables[posInBeforeRunnables[0]].run();
				runnable.run();
				posInBeforeRunnables[0]++;
				return null;
			}
		}).when(taskScheduler).schedule(any(Runnable.class), any(Date.class));

		fixture.onStateEnter();

		verify(stateContext, times(1)).broadcastCurrentState();
		verify(stateContext, times(1)).changeState(SynchronizingState.class, 0);
	}		
	
	@Test
	public void testOnStateEnter_AbortedByOtherApplicationInstance() {
		final Runnable[] beforeRunnables = new Runnable[]{
			new Runnable() {
				public void run() {
					// other application instance replied to the state change by calling onMessage(). That
					// method computes a value for timeInMsecBeforeRedoAnnouncement
					ReflectionTestUtils.setField(fixture, "timeInMsecBeforeRedoAnnouncement", 10000);
				}
			},
			null,
			new Runnable() {
				public void run() {
					// on the second trial to initiate a synchronization, no other 
					// application instance reply to the state change
				}
			}
		};
		final int[] posInBeforeRunnables = new int[]{0};
		
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				System.out.println("---- " + posInBeforeRunnables[0]);
				Runnable runnable = (Runnable)invocation.getArguments()[0];
				if (beforeRunnables[posInBeforeRunnables[0]]!=null) {
					beforeRunnables[posInBeforeRunnables[0]].run();
				}
				posInBeforeRunnables[0]++;
				runnable.run();
				return null;
			}
		}).when(taskScheduler).schedule(any(Runnable.class), any(Date.class));

		fixture.onStateEnter();

		verify(stateContext, times(2)).broadcastCurrentState();
		verify(stateContext, times(1)).changeState(SynchronizingState.class, 0);
		ArgumentCaptor<Integer> waitTimeCaptor = ArgumentCaptor.forClass(Integer.TYPE); 
		verify(stateContext, times(3)).addOffsetToCurrentTimestamp(waitTimeCaptor.capture());
		
		assertEquals(new Integer(1000), waitTimeCaptor.getAllValues().get(0));	// ANNOUNCEMENT_PERIOD_IN_MSEC
		assertEquals(new Integer(10000), waitTimeCaptor.getAllValues().get(1));
		assertEquals(new Integer(1000), waitTimeCaptor.getAllValues().get(2)); // ANNOUNCEMENT_PERIOD_IN_MSEC
	}		
	
}
