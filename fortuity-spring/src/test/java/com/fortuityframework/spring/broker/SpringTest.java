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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fortuityframework.core.dispatch.EventBroker;
import com.fortuityframework.core.dispatch.EventException;
import com.fortuityframework.spring.broker.events.MessageEvent;
import com.fortuityframework.spring.broker.events.ResponseEvent;

/**
 * @author Jeroen Steenbeeke
 *
 */
public class SpringTest {
	private ClassPathXmlApplicationContext context;

	@Before
	public void initAppContext() {
		context = new ClassPathXmlApplicationContext("testContext.xml");
		context.start();

	}

	@Test
	public void triggerEventTest() throws EventException {
		TestResponder responder = (TestResponder) context.getBean("responder");
		EventBroker broker = (EventBroker) context.getBean("eventBroker");

		assertEquals(0, responder.getQueue().size());

		broker.dispatchEvent(new MessageEvent("Test"));

		assertEquals(1, responder.getQueue().size());

		broker.dispatchEvent(new MessageEvent("Test 2"));

		assertEquals(1, responder.getQueue().size());

		broker.dispatchEvent(new ResponseEvent());

		assertEquals(0, responder.getQueue().size());
	}

	@After
	public void closeAppContext() {
		context.stop();
		context = null;
	}
}
