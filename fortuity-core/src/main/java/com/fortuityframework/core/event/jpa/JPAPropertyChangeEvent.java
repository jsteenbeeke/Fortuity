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
 * Event that is triggered when a property is changed on the given entity
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public class JPAPropertyChangeEvent implements Event {
	private Object entity;

	private String propertyName;

	private Object newValue;

	/**
	 * Creates a new changed property event for the given entity, where the
	 * property with the given name has a new value
	 * 
	 * @param entity
	 *            The updated entity
	 * @param propertyName
	 *            The altered property
	 * @param newValue
	 *            The property's new value
	 */
	protected JPAPropertyChangeEvent(Object entity, String propertyName,
			Object newValue) {
		this.entity = entity;
		this.propertyName = propertyName;
		this.newValue = newValue;
	}

	/**
	 * @see com.fortuityframework.core.event.Event#getSource()
	 */
	@Override
	public Object getSource() {
		return entity;
	}

	/**
	 * Gets the property's value after the update
	 * 
	 * @return the new value
	 */
	public Object getNewValue() {
		return newValue;
	}

	/**
	 * Gets the name of the property that was updated
	 * 
	 * @return The name of the property
	 */
	public String getPropertyName() {
		return propertyName;
	}
}
