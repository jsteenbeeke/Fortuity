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
package com.fortuityframework.hibernate;

import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.fortuityframework.core.dispatch.EventBroker;
import com.fortuityframework.core.dispatch.broker.PriorityQueueEventBroker;
import com.fortuityframework.hibernate.testentities.User;

/**
 * @author Jeroen Steenbeeke
 *
 */
public abstract class HibernateTest {

	private static SessionFactory sessionFactory;

	private static EventBroker broker;

	private Session session;

	@BeforeClass
	public static void init() {
		AnnotationConfiguration config = new AnnotationConfiguration();

		Properties properties = new Properties();

		properties.setProperty("hibernate.connection.driver_class",
				"org.hsqldb.jdbcDriver");
		properties.setProperty("hibernate.connection.url",
				"jdbc:hsqldb:mem:test");
		properties.setProperty("hibernate.connection.username", "sa");
		properties.setProperty("hibernate.connection.password", "");

		properties.setProperty("hibernate.hbm2ddl.auto", "create");

		properties.setProperty("hibernate.dialect",
				"org.hibernate.dialect.HSQLDialect");

		config.setProperties(properties);

		config.addAnnotatedClass(User.class);

		broker = new PriorityQueueEventBroker();

		config.setInterceptor(new EventInterceptor(broker));

		sessionFactory = config.buildSessionFactory();
	}

	public Session getSession() {
		return session;
	}

	@Before
	public void openSession() {
		session = sessionFactory.openSession();
	}

	@After
	public void closeSession() {
		session.close();
	}

	@Before
	public void setLocator() {
		broker.setEventListenerLocator(new ObjectBasedLocator(this));
	}

}
