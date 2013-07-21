package com.dynamic_confusion.mygig_planner.client;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable
public class GigTransaction implements Serializable{
	
	@Persistent
	private String sendUser;

	@Persistent
	private String recipientUser;

	@Persistent
	private String name;

	@Persistent
	private String description;

	@Persistent
	private float cost;

	@Persistent
	private String key;

	@Persistent
	private int status = 0;
	
	public GigTransaction() {
		// TODO Auto-generated constructor stub
	}
	
	public String getEntityName(){
		return sendUser+recipientUser+name;
	}
	
	/**
	 * Gets the recipient user
	 * @return A string
	 */
	public String getRecipientUser() {
		return recipientUser;
	}

	/**
	 * Sets the recipient user
	 * @param recipientUser The user who recieves
	 */
	public void setRecipientUser(String recipientUser) {
		this.recipientUser = recipientUser;
	}

	/**
	 * Gets the name
	 * @return A string
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * @param name The new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description
	 * @return A string
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description for this transaction
	 * @param description The transaction description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	
	/**
	 * Gets the cost
	 * @return A floating point umber
	 */
	public float getCost() {
		return cost;
	}

	/**
	 * Sets the cost for this transaction
	 * @param cost How much it will cost
	 */
	public void setCost(float cost) {
		this.cost = cost;
	}

	/**
	 * gets the long key for this entity
	 * @return A long integer
	 */
	public String getKey() {
		return key;
	}

	/*
	 * Sets the long key for this entity
	 * @param key The long id for this entity
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Gets the status of this transaction
	 * @return An integer
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status of this transaction
	 * @param status The value of the new status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Gets the sending user
	 * @return A string
	 */
	public String getSendUser(){ 
		return sendUser;
	}		
	
	/**
	 * Sets the sending user
	 * @param value The username of who sends
	 */
	public void setSendUser(String value){ 
		sendUser = value;
	}
}
