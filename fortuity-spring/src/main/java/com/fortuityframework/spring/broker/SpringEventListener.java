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
package com.fortuityframework.spring.broker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.fortuityframework.core.dispatch.EventContext;
import com.fortuityframework.core.dispatch.EventException;
import com.fortuityframework.core.dispatch.EventListener;

/**
 * Event listener that dispatches events to annotated Spring Bean methods
 * 
 * @author Jeroen Steenbeeke
 */
class SpringEventListener implements EventListener {
	private final String beanName;
	private final Method method;
	private final ApplicationContext applicationContext;

	private static final Logger log = LoggerFactory
			.getLogger(SpringEventListener.class);

	/**
	 * Creates a new Event Listener based on Spring
	 * 
	 * @param beanName
	 *            The name of the bean that responds to this event
	 * @param method
	 *            The method to invoke
	 * @param context
	 *            The context to get the bean from
	 */
	SpringEventListener(String beanName, Method method,
			ApplicationContext context) {
		this.beanName = beanName;
		this.method = method;
		this.applicationContext = context;
	}

	/**
	 * @see com.fortuityframework.core.dispatch.EventListener#dispatchEvent(com.fortuityframework.core.dispatch.EventContext)
	 */
	@Override
	public void dispatchEvent(EventContext<?> context) throws EventException {
		Object bean = applicationContext.getBean(beanName);

		try {
			if (!method.getDeclaringClass().isAssignableFrom(bean.getClass())) {
				try {
					Proxy.getInvocationHandler(bean).invoke(bean, method,
							new Object[] { context });
				} catch (Throwable e) {
					throw new EventException("Could not invoke proxy method", e);
				}
			} else {
				method.invoke(bean, context);
			}
		} catch (IllegalArgumentException e) {
			log.error("Could not invoke Spring event bean method", e);
			throw new EventException(e);
		} catch (IllegalAccessException e) {
			log.error("Could not invoke Spring event bean method", e);
			throw new EventException(e);
		} catch (InvocationTargetException e) {
			log.error("Could not invoke Spring event bean method", e);
			throw new EventException(e);
		}

	}

}
