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

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fortuityframework.core.annotation.ioc.OnFortuityEvent;
import com.fortuityframework.core.dispatch.EventContext;
import com.fortuityframework.spring.broker.events.MessageEvent;
import com.fortuityframework.spring.broker.events.ResponseEvent;

/**
 * @author Jeroen Steenbeeke
 *
 */

@Component
public class TestResponder {
	private List<String> queue;

	/**
	 * 
	 */
	public TestResponder() {
		queue = new LinkedList<String>();
	}

	@OnFortuityEvent(MessageEvent.class)
	public void onReceiveMessage(EventContext<String> context) {
		queue.add(context.getEvent().getSource());

		if (queue.size() > 1) {
			context.triggerEvent(new ResponseEvent());
		}
	}

	@OnFortuityEvent(ResponseEvent.class)
	public void onReceiveResponse(EventContext<Void> context) {
		queue.remove(0);
	}

	/**
	 * @return the queue
	 */
	public List<String> getQueue() {
		return queue;
	}
}
