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
package com.fortuityframework.core.event.jpa;

/**
 * An event that is triggered when a JPA entity is deleted
 * 
 * @author Jeroen Steenbeeke
 * 
 * @param <T> The type of event source for this event
 */
public abstract class JPAEntityDeleteEvent<T> extends JPAEntityEvent<T> {
	/**
	 * Create an event for the given deleted entity
	 * 
	 * @param entity
	 *            The deleted entity
	 */
	protected JPAEntityDeleteEvent(T entity) {
		super(entity);
	}
}
