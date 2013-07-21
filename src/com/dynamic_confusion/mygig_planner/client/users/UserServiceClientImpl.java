package com.dynamic_confusion.mygig_planner.client.users;

import java.util.HashMap;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class UserServiceClientImpl implements UserServiceClientInt{

	private UserServiceAsync service;
	
	public UserServiceClientImpl(String url) {
		this.service = (UserServiceAsync)GWT.create(UserService.class);
		ServiceDefTarget endPoint = (ServiceDefTarget)this.service;
		endPoint.setServiceEntryPoint(url);
	}
	
	@Override
	public void attemptRegister(HashMap<String, Object> newUser) {
		// TODO Auto-generated method stub
		attemptRegister(newUser, new DefaultCallback());
	}
	
	@Override
	public void attemptRegister(HashMap<String, Object> newUser, AsyncCallback callback) {
		 
		// TODO Auto-generated method stub
		this.service.attemptRegister(newUser, callback);
	}

	public void attemptLogin(String username, String password){
		
		// Tell the asynchronous service to attempt login
		attemptLogin(username, password, new DefaultCallback());
	}
	
	/**
	 * Allow users to have a custom login callback
	 */
	public void attemptLogin(String username, String password, AsyncCallback callback){
		
		// Tell the asynchronous service to attempt login
		this.service.attemptLogin(username, password, callback);
	}
	
	public void attemptLogout(AsyncCallback callback){
		
		// Tell the asynchronous service to attempt to logout
		this.service.attemptLogout(callback);
	}
	
	public void attemptLogout(){
		
		// Tell the asynchrnous service to attempt to logout
		attemptLogout(new DefaultCallback());
	}
	
	public void getUsers(AsyncCallback callback){
		
		this.service.getUsers(callback);
	}
	
	public void getUsers(){
		
		getUsers(new DefaultCallback());
	}
	
	private class DefaultCallback implements AsyncCallback{
		
		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			System.out.println("An error has occured!");
		}
		
		@Override
		public void onSuccess(Object result) {
			// TODO Auto-generated method stub
			System.out.println("Login success!");
		}
	
	}
}
