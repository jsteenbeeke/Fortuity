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

import java.util.Collections;
import java.util.Map;

/**
 * Event that is triggered when a given entity is updated
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public abstract class JPAEntityUpdateEvent extends JPAEntityEvent {
	private final Map<String, Object> newValues;

	/**
	 * Creates a new update event for the given entity, with the given set of
	 * values
	 * 
	 * @param entity
	 *            The entity that has been updated
	 * @param newValues
	 *            The values that were set to properties after the update
	 */
	protected JPAEntityUpdateEvent(Object entity, Map<String, Object> newValues) {
		super(entity);
		this.newValues = newValues;
	}

	/**
	 * @return the oldValues
	 */
	public final Map<String, Object> getNewValues() {
		return Collections.unmodifiableMap(newValues);
	}
}
