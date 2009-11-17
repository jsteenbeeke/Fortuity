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
package com.fortuityframework.core.annotation.ioc;

import java.lang.annotation.*;

import com.fortuityframework.core.event.Event;

/**
 * Annotation that indicates that the given method should be called when a
 * FortuityEvent occurs. Note that this will only happen if the class the annotated
 * method is in is in some way connected to the Elan event broker.
 * 
 * @author Jeroen Steenbeeke
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface OnFortuityEvent {
	/**
	 * Indicated which events will trigger this method
	 * 
	 * @return An array of events to respond to
	 */
	Class<? extends Event>[] value() default {};
}
