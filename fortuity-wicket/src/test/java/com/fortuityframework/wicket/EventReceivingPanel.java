package com.fortuityframework.wicket;

import org.apache.wicket.markup.html.panel.Panel;

import com.fortuityframework.core.annotation.ioc.OnFortuityEvent;
import com.fortuityframework.core.dispatch.EventContext;

/**
 * @author Jeroen Steenbeeke
 */
public class EventReceivingPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private int value;

	/**
	 * Creates a new panel that can receive events
	 * @param id The id of the panel
	 */
	public EventReceivingPanel(String id) {
		super(id);

		this.value = 1;
	}

	/**
	 * Responds to ExampleEvents
	 * @param context The context containing the event
	 */
	@OnFortuityEvent(ExampleEvent.class)
	public void receiveExample(EventContext<ExampleEvent> context) {
		this.value++;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}
}
