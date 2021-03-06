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

import java.util.List;

import com.fortuityframework.core.event.Event;

/**
 * Locator class the finds event listeners capable of processing the given class. An EventListenerLocator is generally
 * implemented by the dispatch mechanism used for delivering events. Users generally should not have to worry about this class
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public interface EventListenerLocator {
	/**
	 * Get a list of EventListeners for the given class
	 * @param class1 The class of the event being processed
	 * @return A list of event listeners, which may be empty
	 */
	List<EventListener> getEventListeners(Class<? extends Event<?>> class1);
}
