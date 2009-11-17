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
 * Basic Event interface. Defines the contract for all events
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public interface Event {
	/**
	 * Method to determine the source of the event, i.e. the class or object
	 * that triggered the event.
	 * 
	 * @return The object that triggered the event
	 */
	Object getSource();
}
