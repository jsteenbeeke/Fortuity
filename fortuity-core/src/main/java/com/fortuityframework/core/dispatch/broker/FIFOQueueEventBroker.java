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

import com.fortuityframework.core.dispatch.EventBroker;
import com.fortuityframework.core.dispatch.EventContext;
import com.fortuityframework.core.event.Event;

/**
 * Event broker that uses a First-In-First-Out ordering for events
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public class FIFOQueueEventBroker extends EventBroker {

	/**
	 * @see com.fortuityframework.core.dispatch.EventBroker#createContext(com.fortuityframework.core.event.Event)
	 */
	@Override
	protected final EventContext createContext(final Event contextEvent) {
		return new EventContext() {
			/**
			 * @see com.fortuityframework.core.dispatch.EventContext#getEvent()
			 */
			@Override
			public Event getEvent() {
				return contextEvent;
			}

			/**
			 * @see com.fortuityframework.core.dispatch.EventContext#triggerEvent(com.fortuityframework.core.event.Event)
			 */
			@Override
			public void triggerEvent(Event event) {
				getQueue().add(event);
			}

		};
	}

}
