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
package com.fortuityframework.openjpa;

import org.apache.openjpa.event.LifecycleEvent;
import org.apache.openjpa.event.LifecycleListener;

import com.fortuityframework.core.dispatch.EventBroker;

/**
 * Lifecycle Listener for generating Fortuity events based on OpenJPA lifecycle events 
 * 
 * @author Jeroen Steenbeeke
 *
 */
public class FortuityLifeCycleListener implements LifecycleListener {
	private final EventBroker broker;

	/**
	 * Creates a new FortuityLifeCycleListener that dispatches events to the given event broker
	 * @param broker The broker to dispatch events to
	 */
	private FortuityLifeCycleListener(EventBroker broker) {
		this.broker = broker;
	}

	/**
	 * @see org.apache.openjpa.event.PersistListener#afterPersist(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void afterPersist(LifecycleEvent event) {

	}

	/**
	 * @see org.apache.openjpa.event.PersistListener#beforePersist(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void beforePersist(LifecycleEvent event) {
	}

	/**
	 * @see org.apache.openjpa.event.LoadListener#afterLoad(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void afterLoad(LifecycleEvent event) {
		// TODO handle onLoad
	}

	/**
	 * @see org.apache.openjpa.event.LoadListener#afterRefresh(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void afterRefresh(LifecycleEvent event) {
	}

	/**
	 * @see org.apache.openjpa.event.StoreListener#afterStore(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void afterStore(LifecycleEvent event) {
	}

	/**
	 * @see org.apache.openjpa.event.StoreListener#beforeStore(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void beforeStore(LifecycleEvent event) {
		// TODO handle onSave
	}

	/**
	 * @see org.apache.openjpa.event.ClearListener#afterClear(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void afterClear(LifecycleEvent event) {
	}

	/**
	 * @see org.apache.openjpa.event.ClearListener#beforeClear(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void beforeClear(LifecycleEvent event) {
	}

	/**
	 * @see org.apache.openjpa.event.DeleteListener#afterDelete(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void afterDelete(LifecycleEvent event) {
	}

	/**
	 * @see org.apache.openjpa.event.DeleteListener#beforeDelete(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void beforeDelete(LifecycleEvent event) {
		// TODO handle onDelete
		Object entity = event.getSource();

	}

	/**
	 * @see org.apache.openjpa.event.DirtyListener#afterDirty(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void afterDirty(LifecycleEvent event) {
	}

	/**
	 * @see org.apache.openjpa.event.DirtyListener#afterDirtyFlushed(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void afterDirtyFlushed(LifecycleEvent event) {
	}

	/**
	 * @see org.apache.openjpa.event.DirtyListener#beforeDirty(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void beforeDirty(LifecycleEvent event) {
	}

	/**
	 * @see org.apache.openjpa.event.DirtyListener#beforeDirtyFlushed(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void beforeDirtyFlushed(LifecycleEvent event) {
		// TODO handle onSave
	}

	/**
	 * @see org.apache.openjpa.event.DetachListener#afterDetach(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void afterDetach(LifecycleEvent event) {
	}

	/**
	 * @see org.apache.openjpa.event.DetachListener#beforeDetach(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void beforeDetach(LifecycleEvent event) {
	}

	/**
	 * @see org.apache.openjpa.event.AttachListener#afterAttach(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void afterAttach(LifecycleEvent event) {
	}

	/**
	 * @see org.apache.openjpa.event.AttachListener#beforeAttach(org.apache.openjpa.event.LifecycleEvent)
	 */
	@Override
	public void beforeAttach(LifecycleEvent event) {
	}
}
