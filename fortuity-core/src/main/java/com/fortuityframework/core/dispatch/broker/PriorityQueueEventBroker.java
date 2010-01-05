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

import java.util.Collections;
import java.util.Comparator;

import com.fortuityframework.core.dispatch.EventBroker;
import com.fortuityframework.core.dispatch.EventContext;
import com.fortuityframework.core.event.Event;
import com.fortuityframework.core.event.PrioritizedEvent;

/**
 * Event broker that orders additional events using a priority queue. To take advantage
 * of this queue, events should implement the {@link PrioritizedEvent} interface. Events
 * that do not implement this interface will get a priority of {@link Integer.MIN_VALUE}
 * 
 * @author Jeroen Steenbeeke
 */
public class PriorityQueueEventBroker extends EventBroker {
	/**
	 * @see com.fortuityframework.core.dispatch.EventBroker#createContext(com.fortuityframework.core.event.Event)
	 */
	@Override
	protected final <T extends Event<?>> EventContext<T> createContext(
			final T contextEvent) {
		return new EventContext<T>() {
			/**
			 * 			
			 * @see com.fortuityframework.core.dispatch.EventContext#getEvent()
			 */
			@Override
			public T getEvent() {
				return contextEvent;
			}

			/**
			 * @see com.fortuityframework.core.dispatch.EventContext#triggerEvent(com.fortuityframework.core.event.Event)
			 */
			@Override
			public void triggerEvent(Event<?> event) {
				enqueueEvent(event);
			}
		};
	}

	/**
	 * @see com.fortuityframework.core.dispatch.EventBroker#enqueueEvent(com.fortuityframework.core.event.Event)
	 */
	@Override
	protected void enqueueEvent(Event<?> event) {
		getQueue().add(event);

		Collections.sort(getQueue(), new PriorityComparator());
	}

	/**
	 *
	 * Comparator implementation for events. Orders events by their priority. The higher the priority
	 * value, the sooner the event will be executed. If an event has no priority {@code Integer.MIN_VALUE} is
	 * assumed
	 * 
	 * @author Jeroen Steenbeeke
	 */
	static final class PriorityComparator implements Comparator<Event<?>> {
		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Event<?> o1, Event<?> o2) {
			Integer priority1 = o1 instanceof PrioritizedEvent<?> ? ((PrioritizedEvent<?>) o1)
					.getPriority()
					: Integer.MIN_VALUE;
			Integer priority2 = o2 instanceof PrioritizedEvent<?> ? ((PrioritizedEvent<?>) o2)
					.getPriority()
					: Integer.MIN_VALUE;

			return -priority1.compareTo(priority2);
		}
	}

}
