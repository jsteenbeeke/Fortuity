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
package com.fortuityframework.core.event;

/**
 * Event with a priority, to be used with a PriorityQueueEventBroker
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public interface PrioritizedEvent extends Event {
	/**
	 * Get the priority of this event. The higher this number, the higher the
	 * priority
	 * 
	 * @return The priority of this event
	 */
	int getPriority();
}
