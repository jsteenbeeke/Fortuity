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

import com.fortuityframework.core.event.Event;

/**
 * An event triggered by a change in a JPA entity
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public abstract class JPAEntityEvent implements Event {
	private final Object entity;

	/**
	 * Creates a new JPAEntityEvent based on a change in the given entity The
	 * nature of the change is determined by subclasses of this class
	 * 
	 * @param entity
	 *            The changed entity.
	 */
	protected JPAEntityEvent(Object entity) {
		this.entity = entity;
	}

	/**
	 * @see com.fortuityframework.core.event.Event#getSource()
	 */
	@Override
	public final Object getSource() {
		return entity;
	}

}
