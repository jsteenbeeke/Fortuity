package com.fortuityframework.wicket;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fortuityframework.core.annotation.ioc.OnFortuityEvent;
import com.fortuityframework.core.dispatch.EventContext;
import com.fortuityframework.core.dispatch.EventException;

/**
 * @author Jeroen Steenbeeke
 */
public class StatefulComponentPage extends WebPage {
	private static final Logger logger = LoggerFactory
			.getLogger(StatefulComponentPage.class);

	private IModel<Integer> counterModel;

	/**
	 * Creates a new stateful component page with the given state
	 * @param value The value that represents the state
	 */
	public StatefulComponentPage(int value) {
		super();

		counterModel = new Model<Integer>(value);

		add(new FeedbackPanel("feedback"));

		add(new Label("counter", counterModel).setOutputMarkupId(true));
		add(new AjaxLink<Integer>("up") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				FortuityTestApplication app = (FortuityTestApplication) getApplication();

				try {
					app.getEventBroker().dispatchEvent(new ExampleEvent());
				} catch (EventException e) {
					error(e.getMessage());
					logger.error(e.getMessage(), e);
				}

				if (target != null) {
					target.addComponent(getPage().get("counter"));
				}

			}

		});

		add(new EventReceivingPanel("receiver"));
	}

	/**
	 * Responds to ExampleEvents
	 * @param context The context containing the event
	 */
	@OnFortuityEvent(ExampleEvent.class)
	public void onTestEvent(EventContext<ExampleEvent> context) {
		counterModel.setObject(counterModel.getObject() + 1);
	}

	/**
	 * @see org.apache.wicket.Page#onDetach()
	 */
	@Override
	protected void onDetach() {
		super.onDetach();

		counterModel.detach();
	}
}
