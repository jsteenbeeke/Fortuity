/*
 * Copyright 2009 Jeroen Steenbeeke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.fortuityframework.core.dispatch.broker;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.fortuityframework.core.dispatch.broker.PriorityQueueEventBroker.PriorityComparator;
import com.fortuityframework.core.event.PrioritizedEvent;

/**
 * Tests if the PriorityComparator sorts events correctly
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public class PriorityComparatorTest {
	private static class TestPriorityEvent implements PrioritizedEvent {
		private int priority;

		/**
		 * @param priority
		 */
		public TestPriorityEvent(int priority) {
			this.priority = priority;
		}

		/**
		 * @see com.fortuityframework.core.event.PrioritizedEvent#getPriority()
		 */
		@Override
		public int getPriority() {
			return priority;
		}

		/**
		 * @see com.fortuityframework.core.event.Event#getSource()
		 */
		@Override
		public Object getSource() {
			return null;
		}
	}

	@Test
	public void testBasicOrdering() {
		List<PrioritizedEvent> events = new LinkedList<PrioritizedEvent>();

		events.add(new TestPriorityEvent(5));
		events.add(new TestPriorityEvent(15));

		Collections.sort(events, new PriorityComparator());

		assertEquals(15, events.get(0).getPriority());
		assertEquals(5, events.get(1).getPriority());
	}
}
