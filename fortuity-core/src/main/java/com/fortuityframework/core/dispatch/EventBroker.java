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
package com.fortuityframework.core.dispatch;

import java.util.LinkedList;
import java.util.List;

import com.fortuityframework.core.event.Event;

/**
 * 
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public abstract class EventBroker {
	private EventListenerLocator locator;

	private List<Event> queue = new LinkedList<Event>();

	public final void setEventListenerLocator(EventListenerLocator locator) {
		this.locator = locator;
	}

	protected final List<Event> getQueue() {
		return queue;
	}

	public final synchronized void dispatchEvent(Event event)
			throws EventException {
		EventContext context = createContext(event);

		for (EventListener listener : locator.getEventListeners(event
				.getClass())) {
			listener.dispatchEvent(context);
		}

		if (!getQueue().isEmpty()) {
			Event next = getQueue().remove(0);
			dispatchEvent(next);
		}
	}

	protected abstract EventContext createContext(Event event);
}
