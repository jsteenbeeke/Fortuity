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
package com.fortuityframework.hibernate.eventtest;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Random;

import org.hibernate.Transaction;
import org.junit.Test;

import com.fortuityframework.core.annotation.ioc.OnFortuityEvent;
import com.fortuityframework.core.dispatch.EventContext;
import com.fortuityframework.hibernate.HibernateTest;
import com.fortuityframework.hibernate.events.UserCreateEvent;
import com.fortuityframework.hibernate.events.UserMailChangeEvent;
import com.fortuityframework.hibernate.testentities.User;

/**
 * @author Jeroen Steenbeeke
 *
 */
public class ChainedHibernateActionsTest extends HibernateTest {
	private boolean triggered;
	private boolean chained;

	@Test
	public void testChain() {
		triggered = false;
		chained = false;

		Transaction t = getSession().beginTransaction();

		User user = makeUser();
		getSession().save(user);
		t.commit();

		assertTrue(triggered);
		assertTrue(chained);
	}

	@OnFortuityEvent(UserCreateEvent.class)
	public void handleCreateEvent(EventContext<UserCreateEvent> context) {
		User user = context.getEvent().getSource();

		user.setEmail("scramble!");

		triggered = true;

		getSession().update(user);
	}

	@OnFortuityEvent(UserMailChangeEvent.class)
	public void handleUpdateEvent(EventContext<UserMailChangeEvent> context) {
		User user = context.getEvent().getSource();

		assertEquals("scramble!", user.getEmail());
		assertTrue(triggered);

		chained = true;

	}

	private static final Random random = new Random();

	private User makeUser() {
		User user = new User();
		user.setId(random.nextLong());
		user.setEmail("test@test.com");
		user.setLastActivity(new Date());
		user.setPassword("test");
		user.setUsername("test");
		return user;
	}
}
