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
 * Entity update event that is aware of the previous values of the entity
 * 
 * @author Jeroen Steenbeeke
 *
 * @param <T> The type of event source for this event
 */
public abstract class JPAPreviousValueAwareEntityUpdateEvent<T> extends
		JPAEntityUpdateEvent<T> {
	private final Map<String, Object> oldValues;

	/**
	 * Creates a new previous value aware entity update event for the given entity and values
	 * @param entity The entity that was updated
	 * @param newValues A map of changed properties with their new values
	 * @param oldValues A map of changed properties with their old values
	 */
	protected JPAPreviousValueAwareEntityUpdateEvent(T entity,
			Map<String, Object> newValues, Map<String, Object> oldValues) {
		super(entity, newValues);
		this.oldValues = oldValues;
	}

	/**
	 * The previous values of the properties
	 * @return The previous values of the properties
	 */
	public final Map<String, Object> getOldValues() {
		return Collections.unmodifiableMap(oldValues);
	}

}
