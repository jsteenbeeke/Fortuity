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
package com.fortuityframework.hibernate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fortuityframework.core.annotation.ioc.OnFortuityEvent;
import com.fortuityframework.core.dispatch.EventContext;
import com.fortuityframework.core.dispatch.EventException;
import com.fortuityframework.core.dispatch.EventListener;
import com.fortuityframework.core.dispatch.EventListenerLocator;
import com.fortuityframework.core.event.Event;

/**
 * Test implementation of an Event Listener Locator that searches a single object for responder methods
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class ObjectBasedLocator implements EventListenerLocator {
	private Map<Class<? extends Event>, List<EventListener>> listeners;

	/**
	 * Create a new locator for the given object
	 * @param myObject The object to search for event responders
	 */
	public ObjectBasedLocator(Object myObject) {
		listeners = new HashMap<Class<? extends Event>, List<EventListener>>();

		Class<?> objClass = myObject.getClass();

		for (Method m : objClass.getMethods()) {
			if (m.isAnnotationPresent(OnFortuityEvent.class)
					&& m.getParameterTypes().length == 1
					&& m.getParameterTypes()[0] == EventContext.class) {
				OnFortuityEvent metadata = m
						.getAnnotation(OnFortuityEvent.class);
				for (Class<? extends Event> event : metadata.value()) {
					if (!listeners.containsKey(event)) {
						listeners.put(event, new LinkedList<EventListener>());
					}
					listeners.get(event).add(
							new MethodEventListener(m, myObject));
				}
			}
		}

	}

	/**
	 * @see com.fortuityframework.core.dispatch.EventListenerLocator#getEventListeners(java.lang.Class)
	 */
	@Override
	public List<EventListener> getEventListeners(
			Class<? extends Event> eventClass) {
		Class<?> next = eventClass;
		List<EventListener> results = new LinkedList<EventListener>();

		while (Event.class.isAssignableFrom(next)) {
			if (listeners.containsKey(next)) {
				results.addAll(listeners.get(next));
			}

			next = next.getSuperclass();
		}

		return results;
	}

	/**
	 * Simple method invocation based event listener
	 * 
	 * @author Jeroen Steenbeeke
	 */
	private static final class MethodEventListener implements EventListener {
		private Object object;
		private Method method;

		/**
		 * Create a new event listener for the given method on the given object
		 * @param method The method to invoke
		 * @param target The object to invoke it on
		 */
		public MethodEventListener(Method method, Object target) {
			this.object = target;
			this.method = method;
		}

		/**
		 * @see com.fortuityframework.core.dispatch.EventListener#dispatchEvent(com.fortuityframework.core.dispatch.EventContext)
		 */
		@Override
		public void dispatchEvent(EventContext context) throws EventException {
			try {
				method.invoke(object, context);
			} catch (IllegalArgumentException e) {
				throw new EventException(e.getMessage());
			} catch (IllegalAccessException e) {
				throw new EventException(e.getMessage());
			} catch (InvocationTargetException e) {
				throw new EventException(e.getMessage());
			}

		}
	}

}
