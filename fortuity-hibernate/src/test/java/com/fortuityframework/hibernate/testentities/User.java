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
package com.fortuityframework.hibernate.testentities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fortuityframework.core.annotation.jpa.FortuityEntity;
import com.fortuityframework.core.annotation.jpa.FortuityProperty;
import com.fortuityframework.hibernate.events.UserCreateEvent;
import com.fortuityframework.hibernate.events.UserDeleteEvent;
import com.fortuityframework.hibernate.events.UserLoadEvent;
import com.fortuityframework.hibernate.events.UserUpdateEvent;

/**
 * @author Jeroen Steenbeeke
 *
 */
@Entity
@FortuityEntity(onCreate = UserCreateEvent.class, onDelete = UserDeleteEvent.class, onLoad = UserLoadEvent.class, onUpdate = UserUpdateEvent.class)
public class User {
	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String username;

	@Column
	private String password;

	@Column
	@FortuityProperty()
	private String email;

	@Column
	private Date lastActivity;

	/**
	 * 
	 */
	public User() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the lastActivity
	 */
	public Date getLastActivity() {
		return lastActivity;
	}

	/**
	 * @param lastActivity the lastActivity to set
	 */
	public void setLastActivity(Date lastActivity) {
		this.lastActivity = lastActivity;
	}

}
