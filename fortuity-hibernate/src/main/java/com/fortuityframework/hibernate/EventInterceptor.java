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

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.hibernate.*;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fortuityframework.core.annotation.jpa.FortuityEntity;
import com.fortuityframework.core.annotation.jpa.FortuityProperty;
import com.fortuityframework.core.dispatch.EventBroker;
import com.fortuityframework.core.dispatch.EventException;
import com.fortuityframework.core.event.Event;
import com.fortuityframework.core.event.jpa.*;

/**
 * @author Jeroen Steenbeeke
 * 
 */
public class EventInterceptor implements Interceptor {
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory
			.getLogger(EventInterceptor.class);

	private EventBroker broker;

	private Interceptor chainedInterceptor;

	public EventInterceptor(EventBroker broker) {
		this.broker = broker;
		// Anonymous class to avoid private constructor
		this.chainedInterceptor = new EmptyInterceptor() {
			private static final long serialVersionUID = 1L;
		};
	}

	public EventInterceptor(EventBroker broker, Interceptor chainedInterceptor) {
		this.broker = broker;
		this.chainedInterceptor = chainedInterceptor;
	}

	/**
	 * @see org.hibernate.EmptyInterceptor#onDelete(java.lang.Object,
	 *      java.io.Serializable, java.lang.Object[], java.lang.String[],
	 *      org.hibernate.type.Type[])
	 */
	@Override
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		Class<?> entityClass = entity.getClass();
		FortuityEntity metadata = entityClass
				.getAnnotation(FortuityEntity.class);

		if (metadata != null) {
			for (Class<? extends JPAEntityDeleteEvent<?>> eventClass : getDeleteEvents(metadata)) {
				dispatchDeleteEvent(entity, eventClass);
			}
		}

		chainedInterceptor.onDelete(entity, id, state, propertyNames, types);
	}

	/**
	 * @see org.hibernate.EmptyInterceptor#onSave(java.lang.Object,
	 *      java.io.Serializable, java.lang.Object[], java.lang.String[],
	 *      org.hibernate.type.Type[])
	 */
	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		Class<?> entityClass = entity.getClass();
		FortuityEntity metadata = entityClass
				.getAnnotation(FortuityEntity.class);

		if (metadata != null) {
			Class<? extends JPAEntityCreateEvent<?>>[] events = getCreateEvents(metadata);

			for (Class<? extends JPAEntityCreateEvent<?>> eventClass : events) {
				dispatchCreateEvent(entity, eventClass);
			}
		}

		return chainedInterceptor.onSave(entity, id, state, propertyNames,
				types);
	}

	@SuppressWarnings("unchecked")
	private Class<? extends JPAEntityCreateEvent<?>>[] getCreateEvents(
			FortuityEntity metadata) {
		Class<? extends JPAEntityCreateEvent<?>>[] events = (Class<? extends JPAEntityCreateEvent<?>>[]) metadata
				.onCreate();
		return events;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends JPAEntityUpdateEvent<?>>[] getUpdateEvents(
			FortuityEntity metadata) {
		Class<? extends JPAEntityUpdateEvent<?>>[] events = (Class<? extends JPAEntityUpdateEvent<?>>[]) metadata
				.onUpdate();
		return events;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends JPAEntityDeleteEvent<?>>[] getDeleteEvents(
			FortuityEntity metadata) {
		Class<? extends JPAEntityDeleteEvent<?>>[] events = (Class<? extends JPAEntityDeleteEvent<?>>[]) metadata
				.onDelete();
		return events;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends JPAEntityLoadEvent<?>>[] getLoadEvents(
			FortuityEntity metadata) {
		Class<? extends JPAEntityLoadEvent<?>>[] events = (Class<? extends JPAEntityLoadEvent<?>>[]) metadata
				.onLoad();
		return events;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends JPAPropertyChangeEvent<?>>[] getPropertyUpdateEvents(
			FortuityProperty metadata) {
		Class<? extends JPAPropertyChangeEvent<?>>[] events = (Class<? extends JPAPropertyChangeEvent<?>>[]) metadata
				.onChange();
		return events;
	}

	/**
	 * @see org.hibernate.EmptyInterceptor#onFlushDirty(java.lang.Object,
	 *      java.io.Serializable, java.lang.Object[], java.lang.Object[],
	 *      java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		Class<?> entityClass = entity.getClass();

		Map<String, Object> oldValues = new HashMap<String, Object>();
		Map<String, Object> newValues = new HashMap<String, Object>();

		for (int i = 0; i < propertyNames.length; i++) {
			if (previousState != null) {
				oldValues.put(propertyNames[i], previousState[i]);
			}
			newValues.put(propertyNames[i], currentState[i]);
		}

		FortuityEntity entityMetadata = entityClass
				.getAnnotation(FortuityEntity.class);
		if (entityMetadata != null) {
			for (Class<? extends JPAEntityUpdateEvent<?>> eventClass : getUpdateEvents(entityMetadata)) {
				dispatchUpdateEvent(entity, oldValues, newValues, eventClass);
			}
		}

		Class<?> next = entityClass;
		while (next.isAnnotationPresent(Entity.class)
				|| next.isAnnotationPresent(MappedSuperclass.class)) {
			for (Field f : next.getDeclaredFields()) {
				FortuityProperty propertyMetadata = f
						.getAnnotation(FortuityProperty.class);
				if (propertyMetadata != null) {
					for (Class<? extends JPAPropertyChangeEvent<?>> eventClass : getPropertyUpdateEvents(propertyMetadata)) {
						if (oldValues.containsKey(f.getName())) {
							Object oldValue = oldValues.get(f.getName());
							Object newValue = newValues.get(f.getName());
							String fieldName = f.getName();

							if ((oldValue != null && newValue != null && !oldValue
									.equals(newValue))
									|| (oldValue == null && newValue != null)) {
								dispatchPropertyChangeEvent(entity, eventClass,
										oldValue, newValue, fieldName);
							}
						}
					}
				}
			}

			next = next.getSuperclass();
		}

		return chainedInterceptor.onFlushDirty(entity, id, currentState,
				previousState, propertyNames, types);
	}

	/**
	 * @param entity
	 * @param eventClass
	 * @param oldValue
	 * @param newValue
	 * @param fieldName
	 */
	private void dispatchPropertyChangeEvent(Object entity,
			Class<? extends JPAPropertyChangeEvent<?>> eventClass,
			Object oldValue, Object newValue, String fieldName) {
		try {
			JPAPropertyChangeEvent<?> event;

			if (JPAPreviousValueAwarePropertyChangeEvent.class
					.isAssignableFrom(eventClass)) {
				Constructor<? extends JPAPropertyChangeEvent<?>> ctor = eventClass
						.getConstructor(entity.getClass(), String.class,
								Object.class, Object.class);
				event = ctor.newInstance(entity, fieldName, newValue, oldValue);
			} else {

				Constructor<? extends JPAPropertyChangeEvent<?>> ctor = eventClass
						.getConstructor(entity.getClass(), String.class,
								Object.class);

				event = ctor.newInstance(entity, fieldName, newValue);
			}

			broker.dispatchEvent(event);
		} catch (SecurityException e) {
			log.error("Declared event " + eventClass.getName()
					+ " does not have an accessible constructor", e);
		} catch (NoSuchMethodException e) {
			log
					.error(
							"Declared event "
									+ eventClass.getName()
									+ " does not have a proper default constructor. Please extend the default constructor of JPAPropertyChangeEvent or that of JPAPreviousValueAwarePropertyChangeEvent",
							e);
		} catch (IllegalArgumentException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (java.lang.InstantiationException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (IllegalAccessException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (InvocationTargetException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (EventException e) {
			log.error("Event handler raised an exception", e);
		}
	}

	/**
	 * @param entity
	 * @param oldValues
	 * @param newValues
	 * @param eventClass
	 */
	private void dispatchUpdateEvent(Object entity,
			Map<String, Object> oldValues, Map<String, Object> newValues,
			Class<? extends JPAEntityUpdateEvent<?>> eventClass) {
		try {
			Event<?> event;

			if (JPAPreviousValueAwareEntityUpdateEvent.class
					.isAssignableFrom(eventClass)) {
				Constructor<? extends JPAEntityUpdateEvent<?>> ctor = eventClass
						.getConstructor(entity.getClass(), Map.class, Map.class);
				event = ctor.newInstance(entity, newValues, oldValues);
			} else {
				Constructor<? extends JPAEntityUpdateEvent<?>> ctor = eventClass
						.getConstructor(entity.getClass(), Map.class);
				event = ctor.newInstance(entity, newValues);
			}

			broker.dispatchEvent(event);

		} catch (SecurityException e) {
			log.error("Declared event " + eventClass.getName()
					+ " does not have an accessible constructor", e);
		} catch (NoSuchMethodException e) {
			log
					.error(
							"Declared event "
									+ eventClass.getName()
									+ " does not have a proper default constructor. Please extend the default constructor of JPAEntityUpdateEvent or JPAPreviousValueAwareEntityUpdateEvent",
							e);
		} catch (IllegalArgumentException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (java.lang.InstantiationException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (IllegalAccessException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (InvocationTargetException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (EventException e) {
			log.error("Event handler raised an exception", e);
		}
	}

	/**
	 * @see org.hibernate.EmptyInterceptor#onLoad(java.lang.Object,
	 *      java.io.Serializable, java.lang.Object[], java.lang.String[],
	 *      org.hibernate.type.Type[])
	 */
	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		if (entity != null) {
			Class<?> entityClass = entity.getClass();
			FortuityEntity metadata = entityClass
					.getAnnotation(FortuityEntity.class);

			if (metadata != null) {
				for (Class<? extends JPAEntityLoadEvent<?>> eventClass : getLoadEvents(metadata)) {
					dispatchLoadEvent(entity, eventClass);
				}
			}
		}

		return chainedInterceptor.onLoad(entity, id, state, propertyNames,
				types);
	}

	/**
	 * @param entity
	 * @param eventClass
	 */
	private void dispatchLoadEvent(Object entity,
			Class<? extends JPAEntityLoadEvent<?>> eventClass) {
		try {
			Constructor<? extends JPAEntityLoadEvent<?>> ctor = eventClass
					.getConstructor(entity.getClass());
			JPAEntityLoadEvent<?> event = ctor.newInstance(entity);
			broker.dispatchEvent(event);

		} catch (SecurityException e) {
			log.error("Declared event " + eventClass.getName()
					+ " does not have an accessible constructor", e);
		} catch (NoSuchMethodException e) {
			log
					.error(
							"Declared event "
									+ eventClass.getName()
									+ " does not have a proper default constructor. Please extend the default constructor of JPAEntityLoadEvent",
							e);
		} catch (IllegalArgumentException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (java.lang.InstantiationException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (IllegalAccessException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (InvocationTargetException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (EventException e) {
			log.error("Event handler raised an exception", e);
		}
	}

	private void dispatchDeleteEvent(Object entity,
			Class<? extends JPAEntityDeleteEvent<?>> eventClass) {
		try {
			Constructor<? extends JPAEntityDeleteEvent<?>> ctor = eventClass
					.getConstructor(entity.getClass());
			JPAEntityDeleteEvent<?> event = ctor.newInstance(entity);
			broker.dispatchEvent(event);

		} catch (SecurityException e) {
			log.error("Declared event " + eventClass.getName()
					+ " does not have an accessible constructor", e);
		} catch (NoSuchMethodException e) {
			log
					.error(
							"Declared event "
									+ eventClass.getName()
									+ " does not have a proper default constructor. Please extend the default constructor of JPAEntityDeleteEvent",
							e);
		} catch (IllegalArgumentException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (java.lang.InstantiationException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (IllegalAccessException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (InvocationTargetException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (EventException e) {
			log.error("Event handler raised an exception", e);
		}
	}

	private <T> void dispatchCreateEvent(Object entity,
			Class<? extends JPAEntityCreateEvent<?>> eventClass) {
		try {
			Constructor<? extends JPAEntityCreateEvent<?>> ctor = eventClass
					.getConstructor(entity.getClass());

			JPAEntityCreateEvent<?> event = ctor.newInstance(entity);
			broker.dispatchEvent(event);

		} catch (SecurityException e) {
			log.error("Declared event " + eventClass.getName()
					+ " does not have an accessible constructor", e);
		} catch (NoSuchMethodException e) {
			log
					.error(
							"Declared event "
									+ eventClass.getName()
									+ " does not have a proper default constructor. Please extend the default constructor of JPAEntityCreateEvent",
							e);
		} catch (IllegalArgumentException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (java.lang.InstantiationException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (IllegalAccessException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (InvocationTargetException e) {
			log.error("Invocation of event constructor failed", e);
		} catch (EventException e) {
			log.error("Event handler raised an exception", e);
		}
	}

	/**
	 * @see org.hibernate.Interceptor#afterTransactionBegin(org.hibernate.Transaction)
	 */
	@Override
	public void afterTransactionBegin(Transaction tx) {
		chainedInterceptor.afterTransactionBegin(tx);
	}

	/**
	 * @see org.hibernate.Interceptor#afterTransactionCompletion(org.hibernate.Transaction)
	 */
	@Override
	public void afterTransactionCompletion(Transaction tx) {
		chainedInterceptor.afterTransactionCompletion(tx);
	}

	/**
	 * @see org.hibernate.Interceptor#beforeTransactionCompletion(org.hibernate.Transaction)
	 */
	@Override
	public void beforeTransactionCompletion(Transaction tx) {
		chainedInterceptor.beforeTransactionCompletion(tx);
	}

	/**
	 * @see org.hibernate.Interceptor#findDirty(java.lang.Object,
	 *      java.io.Serializable, java.lang.Object[], java.lang.Object[],
	 *      java.lang.String[], org.hibernate.type.Type[])
	 */
	@Override
	public int[] findDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) {
		return chainedInterceptor.findDirty(entity, id, currentState,
				previousState, propertyNames, types);
	}

	/**
	 * @see org.hibernate.Interceptor#getEntity(java.lang.String,
	 *      java.io.Serializable)
	 */
	@Override
	public Object getEntity(String entityName, Serializable id)
			throws CallbackException {
		return chainedInterceptor.getEntity(entityName, id);
	}

	/**
	 * @see org.hibernate.Interceptor#getEntityName(java.lang.Object)
	 */
	@Override
	public String getEntityName(Object object) throws CallbackException {
		return chainedInterceptor.getEntityName(object);
	}

	/**
	 * @see org.hibernate.Interceptor#instantiate(java.lang.String,
	 *      org.hibernate.EntityMode, java.io.Serializable)
	 */
	@Override
	public Object instantiate(String entityName, EntityMode entityMode,
			Serializable id) throws CallbackException {
		Object entity = chainedInterceptor.instantiate(entityName, entityMode,
				id);

		return entity;
	}

	/**
	 * @see org.hibernate.Interceptor#isTransient(java.lang.Object)
	 */
	@Override
	public Boolean isTransient(Object entity) {
		return chainedInterceptor.isTransient(entity);
	}

	/**
	 * @see org.hibernate.Interceptor#onCollectionRecreate(java.lang.Object,
	 *      java.io.Serializable)
	 */
	@Override
	public void onCollectionRecreate(Object collection, Serializable key)
			throws CallbackException {
		chainedInterceptor.onCollectionRecreate(collection, key);
	}

	/**
	 * @see org.hibernate.Interceptor#onCollectionRemove(java.lang.Object,
	 *      java.io.Serializable)
	 */
	@Override
	public void onCollectionRemove(Object collection, Serializable key)
			throws CallbackException {
		chainedInterceptor.onCollectionRemove(collection, key);
	}

	/**
	 * @see org.hibernate.Interceptor#onCollectionUpdate(java.lang.Object,
	 *      java.io.Serializable)
	 */
	@Override
	public void onCollectionUpdate(Object collection, Serializable key)
			throws CallbackException {
		chainedInterceptor.onCollectionUpdate(collection, key);
	}

	/**
	 * @see org.hibernate.Interceptor#onPrepareStatement(java.lang.String)
	 */
	@Override
	public String onPrepareStatement(String sql) {
		return chainedInterceptor.onPrepareStatement(sql);
	}

	/**
	 * @see org.hibernate.Interceptor#postFlush(java.util.Iterator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void postFlush(Iterator entities) throws CallbackException {
		chainedInterceptor.postFlush(entities);
	}

	/**
	 * @see org.hibernate.Interceptor#preFlush(java.util.Iterator)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void preFlush(Iterator entities) throws CallbackException {
		chainedInterceptor.preFlush(entities);
	}

}
