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

	public EventReceivingPanel(String id) {
		super(id);

		this.value = 1;
	}

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
