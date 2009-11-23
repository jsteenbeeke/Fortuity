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
package com.fortuityframework.core.dispatch;

/**
 * Checked exception that is thrown whenever the execution of an event fails
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public class EventException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new Event Exception
	 */
	public EventException() {
		super();
	}

	/**
	 * Creates a new Event Exception with the given message and cause
	 * @param message The message describing the exception
	 * @param cause The exception that caused this exception
	 */
	public EventException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new Event Exception with the given message
	 * @param message The message describing the exception
	 */
	public EventException(String message) {
		super(message);
	}

	/**
	 * Creates a new Event Exception with the given cause
	 * @param cause The exception that caused this exception
	 */
	public EventException(Throwable cause) {
		super(cause);
	}
}
