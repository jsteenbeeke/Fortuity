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

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

import com.fortuityframework.core.dispatch.EventBroker;
import com.fortuityframework.core.dispatch.broker.FIFOQueueEventBroker;

/**
 * @author Jeroen Steenbeeke
 *
 */
public class FortuityTestApplication extends WebApplication {
	private EventBroker broker;

	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return FortuityTestIndexPage.class;
	}

	/**
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	@Override
	protected void init() {
		super.init();

		broker = new FIFOQueueEventBroker();
		WicketEventListenerLocator locator = new WicketEventListenerLocator();
		broker.setEventListenerLocator(locator);

		addComponentInstantiationListener(new FortuityComponentInstantiationListener(
				locator));
	}

	/**
	 * @return This application's event broker
	 */
	public EventBroker getEventBroker() {
		return broker;
	}
}
