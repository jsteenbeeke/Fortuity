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

/**
 * Fortuity event listener. An EventListener has the ability to respond to events and dispatch
 * them further depending on the context. Users of Fortuity generally do not have to worry about this interface.
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public interface EventListener {
	/**
	 * Dispatch an event with the given context
	 * @param context The context of the current event
	 * @throws EventException If the processing of the event raises an exception or encounters an error
	 */
	void dispatchEvent(EventContext context) throws EventException;
}
