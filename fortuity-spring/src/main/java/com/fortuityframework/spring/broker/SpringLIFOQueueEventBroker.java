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
package com.fortuityframework.spring.broker;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;

import com.fortuityframework.core.dispatch.EventListenerLocator;
import com.fortuityframework.core.dispatch.NullEventListenerLocator;
import com.fortuityframework.core.dispatch.broker.LIFOQueueEventBroker;

/**
 * LIFO Queue Event broker for use with the Spring framework. To use this broker, only a single line of configuration
 * is needed in your Spring context.
 * 
 * {@code <bean id="eventBroker" class="com.fortuityframework.spring.broker.SpringLIFOQueueEventBroker"></bean>}
 * 
 * @author Jeroen Steenbeeke
 */
public class SpringLIFOQueueEventBroker extends LIFOQueueEventBroker implements
		ApplicationListener {
	private EventListenerLocator chainedLocator;

	/**
	 * Create a new Spring LIFO Queue event broker that does not chain the events after processing by Spring
	 */
	public SpringLIFOQueueEventBroker() {
		chainedLocator = new NullEventListenerLocator();
	}

	/**
	 * Create a new Spring LIFO Queue event broker that chains the events to an additional locator after processing by Spring
	 * @param chainedLocator The locator to chain to
	 */
	public SpringLIFOQueueEventBroker(EventListenerLocator chainedLocator) {
		this.chainedLocator = chainedLocator;
	}

	/**
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextStartedEvent) {
			SpringEventListenerLocator locator = new SpringEventListenerLocator();
			locator.onApplicationEvent(event);
			locator.setChainedLocator(chainedLocator);
			setEventListenerLocator(locator);
		}
	}
}
