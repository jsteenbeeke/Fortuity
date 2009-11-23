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
 * Basic class for dispatching events to event listeners. The EventBroker is the basic
 * worker class for the Fortuity framework. To use events, a reference to an event broker
 * is required.
 * 
 * When using an inversion of control framework (such as Spring), specific implementations of
 * this class exist to take advantage of the capabilities of said framework to allow "beans"
 * to respond to events.
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public abstract class EventBroker {
	private EventListenerLocator locator;

	private List<Event> queue = new LinkedList<Event>();

	/**
	 * Sets the Event Listener locator, which is the mechanism for finding
	 * event listeners for a given event.
	 * 
	 * @param locator The locator to use
	 */
	public final void setEventListenerLocator(EventListenerLocator locator) {
		this.locator = locator;
	}

	/**
	 * Get a reference to the event queue of this broker. When an implementing class
	 * receives a new event, it can use a specific ordering mechanism to modify the queue.
	 * 
	 * Normally this method should not be invoked except by the framework.
	 * 
	 * @return A reference to the queue of this broker
	 */
	protected final List<Event> getQueue() {
		return queue;
	}

	/**
	 * Dispatch an event to event listeners. This method will cause any other method
	 * listening for the given event to be called. Event listeners can trigger new events in turn,
	 * in which case the implementing class can decide in which order they should be executed.
	 * 
	 * @param event The event to dispatch
	 * @throws EventException If the execution of the event goes awry
	 */
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

	/**
	 * Creates an event context for the given event. Depending on the mechanism of event dispatching,
	 * which is decided by the ListenerLocator and the implementing class, the behavior of this class
	 * may differ 
	 * @param event The event to create a context for
	 * @return A context for the given event, capable of dispatching new events
	 */
	protected abstract EventContext createContext(Event event);
}
