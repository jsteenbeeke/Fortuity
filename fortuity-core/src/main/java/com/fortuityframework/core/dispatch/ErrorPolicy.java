package com.fortuityframework.core.dispatch;

/**
 * The policy to use when errors occur in the event handler
 * 
 * @author Jeroen Steenbeeke
 * 
 */
public enum ErrorPolicy {
	/**
	 * Throw the exception, propagating it to the method that called the
	 * EventBroker
	 */
	THROW,

	/**
	 * Stop processing the events but do not throw an exception
	 */
	STOP,
	/**
	 * Ignore the current event, but keep processing any other triggered events
	 */
	IGNORE_EVENT,
	/**
	 * Ignore the current listener but do try calling other listeners for this
	 * event
	 */
	IGNORE_LISTENER;
}
