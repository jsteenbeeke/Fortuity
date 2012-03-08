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

import com.fortuityframework.core.event.jpa.JPAPropertyChangeEvent;

/**
 * Annotation indicating that the given property should be monitored for change,
 * triggering events if a change is detected
 * 
 * @author Jeroen Steenbeeke
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Inherited
public @interface FortuityProperty {
	/**
	 * A list of events that should be triggered if the property is changed
	 * 
	 * @return An array of triggered events
	 */
	Class<? extends JPAPropertyChangeEvent<?>>[] onChange() default {};
}
