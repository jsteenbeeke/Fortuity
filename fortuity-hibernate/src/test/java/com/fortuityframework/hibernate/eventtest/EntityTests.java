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

import org.junit.Test;

import com.fortuityframework.core.annotation.ioc.OnFortuityEvent;
import com.fortuityframework.core.dispatch.EventContext;
import com.fortuityframework.hibernate.HibernateTest;
import com.fortuityframework.hibernate.events.*;
import com.fortuityframework.hibernate.testentities.User;

/**
 * Tests if entities properly cause events to be thrown
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class EntityTests extends HibernateTest {
	private boolean created = false;
	private boolean loaded = false;
	private boolean updated = false;
	private boolean deleted = false;
	private boolean propUpdated = false;

	@Test
	public void testCreate() {
		created = false;
		User user = makeUser();
		getSession().save(user);

		assertTrue(created);
	}

	@Test
	public void testLoad() {
		loaded = false;
		User user = makeUser();
		getSession().save(user);

		Long id = user.getId();

		getSession().evict(user);

		user = null;

		getSession().get(User.class, id);

		assertTrue(loaded);
	}

	@Test
	public void testDelete() {
		deleted = false;
		User user = makeUser();
		getSession().save(user);
		getSession().delete(user);

		assertTrue(deleted);
	}

	@Test
	public void testUpdate() {
		updated = false;
		User user = makeUser();
		getSession().save(user);
		user.setUsername("!test");
		getSession().update(user);
		getSession().flush();

		assertTrue(updated);
	}

	@Test
	public void testUpdateProp() {
		propUpdated = false;
		User user = makeUser();
		getSession().save(user);
		user.setEmail("!test@test.com");
		getSession().update(user);
		getSession().flush();

		assertTrue(propUpdated);
	}

	@OnFortuityEvent(UserDeleteEvent.class)
	public void onDelete(EventContext context) {
		deleted = true;
	}

	@OnFortuityEvent(UserCreateEvent.class)
	public void onCreate(EventContext context) {
		created = true;
	}

	@OnFortuityEvent(UserLoadEvent.class)
	public void onLoad(EventContext context) {
		loaded = true;
	}

	@OnFortuityEvent(UserUpdateEvent.class)
	public void onUpdate(EventContext context) {
		updated = true;
	}

	@OnFortuityEvent(UserMailChangeEvent.class)
	public void onPropertyUpdate(EventContext context) {
		propUpdated = true;
	}

	/**
	 * @return
	 */
	private User makeUser() {
		User user = new User();
		user.setEmail("test@test.com");
		user.setLastActivity(new Date());
		user.setPassword("test");
		user.setUsername("test");
		return user;
	}

}
