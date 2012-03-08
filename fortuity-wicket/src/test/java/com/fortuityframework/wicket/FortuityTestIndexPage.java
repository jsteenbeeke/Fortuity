package com.fortuityframework.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

/**
 * @author Jeroen Steenbeeke
 */
public class FortuityTestIndexPage extends WebPage {
	private static final long serialVersionUID = 1L;

	/**
	 * Create a new test index page
	 */
	public FortuityTestIndexPage() {
		add(new Link<Void>("next") {
			private static final long serialVersionUID = 1L;

			/**
			 * @see org.apache.wicket.markup.html.link.Link#onClick()
			 */
			@Override
			public void onClick() {
				setResponsePage(new StatefulComponentPage(1));
			}
		});
	}
}
