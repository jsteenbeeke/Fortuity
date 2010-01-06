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
package com.fortuityframework.wicket;

import static org.junit.Assert.*;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jeroen Steenbeeke
 *
 */
public class WicketFortuityTest {
	private WicketTester tester;

	@Before
	public void prepareTester() {
		tester = new WicketTester(new FortuityTestApplication());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testEvents() {
		tester.startPage(FortuityTestIndexPage.class);
		tester.clickLink("next");
		tester.assertRenderedPage(StatefulComponentPage.class);

		Component component = tester
				.getComponentFromLastRenderedPage("counter");

		IModel<Integer> cModel = (IModel<Integer>) component.getDefaultModel();
		assertEquals(1, cModel.getObject().intValue());

		tester.clickLink("up", true);

		cModel = (IModel<Integer>) component.getDefaultModel();
		assertEquals(2, cModel.getObject().intValue());

		EventReceivingPanel receiver = (EventReceivingPanel) tester
				.getComponentFromLastRenderedPage("receiver");

		assertEquals(2, receiver.getValue());
	}

	@After
	public void closeTester() {
		tester.destroy();
	}
}
