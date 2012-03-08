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
package com.fortuityframework.core.annotation.jpa;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fortuityframework.core.event.jpa.JPAEntityCreateEvent;
import com.fortuityframework.core.event.jpa.JPAEntityDeleteEvent;
import com.fortuityframework.core.event.jpa.JPAEntityLoadEvent;
import com.fortuityframework.core.event.jpa.JPAEntityUpdateEvent;

/**
 * Annotation for indicating that a JPA entity is to be monitored for changes by
 * the relevant Fortuity framework
 * 
 * @author Jeroen Steenbeeke
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface FortuityEntity {
	/**
	 * A list of events that should be triggered whenever a new entity of the
	 * type having this annotation is created and first saved
	 * 
	 * @return An array of events to be triggered
	 */
	Class<? extends JPAEntityCreateEvent<?>>[] onCreate() default {};

	/**
	 * A list of events that should be triggered whenever an entity of the type
	 * having this annotation is deleted
	 * 
	 * @return An array of events to be triggered
	 */
	Class<? extends JPAEntityDeleteEvent<?>>[] onDelete() default {};

	/**
	 * A list of events that should be triggered whenever an entity of the type
	 * having this annotation is updated
	 * 
	 * @return An array of events to be triggered
	 */
	Class<? extends JPAEntityUpdateEvent<?>>[] onUpdate() default {};

	/**
	 * A list of events that should be triggered whenever an entity of the type
	 * having this annotation is loaded
	 * 
	 * @return An array of events to be triggered
	 */
	Class<? extends JPAEntityLoadEvent<?>>[] onLoad() default {};
}
