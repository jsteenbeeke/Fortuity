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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fortuityframework.core.event.Event;

/**
 * Basic class for dispatching events to event listeners. The EventBroker is the
 * basic worker class for the Fortuity framework. To use events, a reference to
 * an event broker is required.
 * 
 * When using an inversion of control framework (such as Spring), specific
 * implementations of this class exist to take advantage of the capabilities of
 * said framework to allow "beans" to respond to events.
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public abstract class EventBroker implements IEventBroker {
	private static final Logger log = LoggerFactory
			.getLogger(EventBroker.class);

	private EventListenerLocator locator;

	private List<Event<?>> queue = new LinkedList<Event<?>>();

	private ErrorPolicy eventErrorPolicy = ErrorPolicy.THROW;

	private ErrorPolicy runtimeExceptionPolicy = ErrorPolicy.THROW;

	private ThreadLocal<Boolean> inProcessor = new ThreadLocal<Boolean>() {
		protected Boolean initialValue() {
			return false;
		};
	};

	/**
	 * Sets the Event Listener locator, which is the mechanism for finding event
	 * listeners for a given event.
	 * 
	 * @param locator
	 *            The locator to use
	 */
	public final void setEventListenerLocator(EventListenerLocator locator) {
		this.locator = locator;
	}

	/**
	 * Get a reference to the event queue of this broker. When an implementing
	 * class receives a new event, it can use a specific ordering mechanism to
	 * modify the queue.
	 * 
	 * Normally this method should not be invoked except by the framework.
	 * 
	 * @return A reference to the queue of this broker
	 */
	protected final List<Event<?>> getQueue() {
		return queue;
	}

	/**
	 * Determines what the event broker should do in case of Fortuity errors
	 * (usually the case when the calling of event handlers fails). Default is
	 * THROW.
	 * 
	 * @param errorPolicy
	 *            The policy to set
	 */
	public void setEventErrorPolicy(ErrorPolicy eventErrorPolicy) {
		this.eventErrorPolicy = eventErrorPolicy;
	}

	/**
	 * Determines what the event broker should do in case of runtime errors
	 * (usually happens inside event handlers). Default is THROW.
	 * 
	 * @param errorPolicy
	 *            The policy to set
	 */
	public void setRuntimeExceptionPolicy(ErrorPolicy runtimeExceptionPolicy) {
		this.runtimeExceptionPolicy = runtimeExceptionPolicy;
	}

	/**
	 * Dispatch a single event to be processed. WARNING: Do not call this method
	 * from within a method that is itself an event responder! Use the
	 * triggerEvent method of EventContext instead
	 * 
	 * @param event
	 *            The event to dispatch
	 * @throws EventException
	 *             If an event encountered an error
	 */
	@Override
	public final void dispatchEvent(Event<?> event) throws EventException {
		enqueueEvent(event);

		processEvents();
	}

	/**
	 * Dispatch multiple events to be processed, using the queuing mechanism to
	 * take care of ordering. WARNING: Do not call this method from within a
	 * method that is itself an event responder! Use the triggerEvent method of
	 * EventContext instead
	 * 
	 * @param events
	 *            The events to dispatch
	 * @throws EventException
	 *             If an event encountered an error
	 */
	@Override
	public final void dispatchEvents(List<Event<?>> events)
			throws EventException {
		for (Event<?> event : events) {
			enqueueEvent(event);
		}

		processEvents();
	}

	/**
	 * Dispatch all queued events to event listeners. This method will cause any
	 * other method listening for the given events to be called. Event listeners
	 * can trigger new events in turn, in which case the implementing class can
	 * decide in which order they should be executed.
	 * 
	 * @throws EventException
	 *             If the execution of the event goes awry
	 */
	protected final synchronized void processEvents() throws EventException {
		// This method may not be recursively called - it may break event
		// ordering
		if (!inProcessor.get()) {
			try {
				inProcessor.set(true);
				events: while (!getQueue().isEmpty()) {
					Event<?> event = getQueue().remove(0);

					log.debug("Processing event of type "
							+ event.getClass().getName());

					EventContext<?> context = createContext(event);

					Class<? extends Event<?>> eventClass = getEventClass(event);

					// Prevent NullPointerException
					if (locator != null) {
						listeners: for (EventListener listener : locator
								.getEventListeners(eventClass)) {
							log.trace("Dispatching to listener of type "
									+ listener.getClass().getName());
							try {
								listener.dispatchEvent(context);
							} catch (EventException e) {
								log.error("Event processing error message");
								log.error(e.getMessage(), e);
								log.trace(String.format(
										"Event error policy: %s",
										eventErrorPolicy.name()));
								switch (eventErrorPolicy) {
									case THROW:
										throw e;
									case IGNORE_LISTENER:
										continue listeners;
									case IGNORE_EVENT:
										continue events;
									case STOP:
										return;
								}
							} catch (RuntimeException e) {
								log.error("Event processing error message");
								log.error(e.getMessage(), e);
								log.trace(String.format(
										"Runtime exception policy: %s",
										runtimeExceptionPolicy.name()));
								switch (runtimeExceptionPolicy) {
									case THROW:
										throw e;
									case IGNORE_LISTENER:
										continue listeners;
									case IGNORE_EVENT:
										continue events;
									case STOP:
										return;
								}
							}

						}
					} else {
						log.warn("No event listener locator");
					}
				}

			} finally {
				// Prevent deadlocking the event processor
				inProcessor.set(false);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Event<?>> getEventClass(Event<?> event) {
		return (Class<? extends Event<?>>) event.getClass();
	}

	/**
	 * Adds the given even to the queue. The exact behavior of this method
	 * depends on the implementation provided by the subclass
	 * 
	 * @param event
	 *            The event to enqueue
	 */
	protected abstract void enqueueEvent(Event<?> event);

	/**
	 * Creates an event context for the given event. Depending on the mechanism
	 * of event dispatching, which is decided by the ListenerLocator and the
	 * implementing class, the behavior of this class may differ
	 * 
	 * @param event
	 *            The event to create a context for
	 * @return A context for the given event, capable of dispatching new events
	 */
	protected abstract <T extends Event<?>> EventContext<T> createContext(
			T event);
}
