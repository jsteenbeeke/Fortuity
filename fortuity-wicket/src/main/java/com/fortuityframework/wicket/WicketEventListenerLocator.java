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
package com.fortuityframework.wicket;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.wicket.Component;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fortuityframework.core.annotation.ioc.OnFortuityEvent;
import com.fortuityframework.core.dispatch.EventContext;
import com.fortuityframework.core.dispatch.EventException;
import com.fortuityframework.core.dispatch.EventListener;
import com.fortuityframework.core.dispatch.EventListenerLocator;
import com.fortuityframework.core.event.Event;

/**
 * Fortuity Event Listeners locator for use with Wicket components. When used in combination with a {@link FortuityComponentInstantiationListener},
 * this locator will delegate Fortuity events to Wicket components. It will also delegate events to other event listeners if need be (for
 * example, if using Fortuity with Spring).
 * 
 * To use this locator, you should use the following code in your WicketApplication's init() method.
 * 
 * <pre>
 * {@code
 * WicketEventListenerLocator locator 
 * 	= new WicketEventListenerLocator(Duration.minutes(15));
 * FortuityComponentInstantiationListener listener 
 * 	= new FortuityComponentInstantiationListener(locator);
 * }
 * </pre>
 * 
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class WicketEventListenerLocator implements EventListenerLocator {
	private EventListenerLocator chainedLocator;
	private Duration duration;

	Map<Class<?>, Set<ComponentEventListener>> listeners = new HashMap<Class<?>, Set<ComponentEventListener>>();

	private static Logger log = LoggerFactory
			.getLogger(WicketEventListenerLocator.class);

	/**
	 * Creates a new event listener locator that does not chain, and expires components after one hour
	 */
	public WicketEventListenerLocator() {
		this.chainedLocator = new NullChainedLocator();
		this.duration = Duration.ONE_HOUR;
	}

	/**
	 * Creates a new event listener locator that does not chain, and expires after the specified duration has passed
	 * @param duration How long a component can be held until it expires
	 */
	public WicketEventListenerLocator(Duration duration) {
		this.chainedLocator = new NullChainedLocator();
		this.duration = duration;
	}

	/**
	 * Creates a new event listener locator that chains to the given locator, and expires after the specified duration has passed
	 * @param chainedLocator
	 * @param duration How long a component can be held until it expires
	 */
	public WicketEventListenerLocator(EventListenerLocator chainedLocator,
			Duration duration) {
		this.chainedLocator = chainedLocator;
		this.duration = duration;
	}

	/**
	 * Called by the FortuityComponentInstantiationListener when a new component has been created. Scans the component's class structure
	 * and adds handlers for any event response method in the component
	 * @param component The component that was added
	 */
	synchronized void onComponentAdded(Component component) {
		Class<?> componentClass = component.getClass();

		for (Method m : componentClass.getMethods()) {
			OnFortuityEvent metadata = m.getAnnotation(OnFortuityEvent.class);
			if (metadata != null) {
				if (m.getParameterTypes().length == 1
						&& m.getParameterTypes()[0] == EventContext.class) {
					for (Class<? extends Event> event : metadata.value()) {
						if (!listeners.containsKey(event)) {
							listeners.put(event,
									new HashSet<ComponentEventListener>());
						}

						listeners.get(event).add(
								new ComponentEventListener(component, m));
					}
				} else {
					log
							.error("Fortuity event listener has incorrect parameter sequence for method "
									+ m.toGenericString()
									+ " on component "
									+ component.getPath()
									+ " of page "
									+ component.getPage().toString());
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
		List<EventListener> result = new LinkedList<EventListener>();

		// Dispatch to any chained listeners. We assume that Wicket components
		// will be updated last,
		// and any even listeners given by the chained locator are to ensure
		// data integrity
		result.addAll(chainedLocator.getEventListeners(eventClass));

		Class<?> next = eventClass;

		while (Event.class.isAssignableFrom(next)) {
			// If we have listeners for this event then dispatch the event
			if (listeners.containsKey(next)) {
				// Set for removing inactive listeners. Components expire when
				// either they have been
				// garbage collected, or they have expired according to their
				// duration. However,
				// we must prevent a ConcurrentModificationException so we
				// simply put those that should be removed
				// in a set, and remove it outside the loop
				Set<ComponentEventListener> removeListeners = new HashSet<ComponentEventListener>();

				for (ComponentEventListener listener : listeners.get(next)) {
					// Only dispatch to active listeners
					if (!listener.isExpired()) {
						result.add(listener);
					} else {
						// Remove inactive ones
						removeListeners.add(listener);
					}
				}

				// Perform the remove
				listeners.get(next).removeAll(removeListeners);
			}

			// Check superclass to ensure polymorphic event handling
			next = next.getSuperclass();
		}

		return result;
	}

	/**
	 * Event listener tied to a Wicket Component
	 * 
	 * @author Jeroen Steenbeeke
	 */
	private class ComponentEventListener implements EventListener {
		private long until;
		private WeakReference<Component> componentRef;
		private Method eventMethod;

		/**
		 * Create a new event listener for the given method of the given component
		 * @param component The component that contains the method
		 * @param eventMethod The method to invoke when it's event occurs
		 */
		public ComponentEventListener(Component component, Method eventMethod) {
			this.until = System.currentTimeMillis()
					+ duration.getMilliseconds();
			this.componentRef = new WeakReference<Component>(component);
			this.eventMethod = eventMethod;
		}

		/**
		 * @see com.fortuityframework.core.dispatch.EventListener#dispatchEvent(com.fortuityframework.core.dispatch.EventContext)
		 */
		@Override
		public void dispatchEvent(EventContext context) throws EventException {
			try {
				// Check if the component still exists
				Component component = componentRef.get();
				if (component != null) {
					// Invoke the event
					eventMethod.invoke(component, context);
					// Detach the component
					if (component.hasBeenRendered()) {
						component.detach();
					}
				}
			} catch (IllegalArgumentException e) {
				throw new EventException(e.getMessage());
			} catch (IllegalAccessException e) {
				throw new EventException(e.getMessage());
			} catch (InvocationTargetException e) {
				throw new EventException(e.getMessage());
			}
		}

		public boolean isExpired() {
			return System.currentTimeMillis() > until
					|| componentRef.get() == null;
		}

	}

	/**
	 * Null object for delegated event listener locator
	 * 
	 * @author Jeroen Steenbeeke
	 */
	private static class NullChainedLocator implements EventListenerLocator {
		/**
		 * @see com.fortuityframework.core.dispatch.EventListenerLocator#getEventListeners(java.lang.Class)
		 */
		@Override
		public List<EventListener> getEventListeners(
				Class<? extends Event> eventClass) {
			return new LinkedList<EventListener>();
		}
	}

}
