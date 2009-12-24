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
package com.fortuityframework.spring.broker.events;

import com.fortuityframework.core.event.Event;

/**
 * @author Jeroen Steenbeeke
 *
 */
public class MessageEvent implements Event<String> {
	private String message;

	public MessageEvent(String message) {
		super();
		this.message = message;
	}

	/**
	 * @see com.fortuityframework.core.event.Event#getSource()
	 */
	@Override
	public String getSource() {
		return message;
	}

}
