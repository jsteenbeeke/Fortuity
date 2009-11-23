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
 * A property change event triggered by a change of value on a monitored property
 * of a JPA entity. This implementation also stores the old value of the property if the underlying
 * event generator supports it.
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class JPAPreviousValueAwarePropertyChangeEvent extends
		JPAPropertyChangeEvent {
	private Object oldValue;

	/**
	 * Intializes a new property change event that is aware of the old value of the
	 * property. Depending on the implementation used this type of event may or may not be suppored.
	 * @param entity The entity changed
	 * @param propertyName The name of the property changed 
	 * @param newValue The new value of the property
	 * @param oldValue The old value of the property
	 */
	private JPAPreviousValueAwarePropertyChangeEvent(Object entity,
			String propertyName, Object newValue, Object oldValue) {
		super(entity, propertyName, newValue);
		this.oldValue = oldValue;
	}

	/**
	 * @return The previous value of the changed property
	 */
	public Object getOldValue() {
		return oldValue;
	}

}
