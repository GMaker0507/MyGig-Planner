package com.dynamic_confusion.mygig_planner.client.ss_service;

import java.util.Date;

import com.dynamic_confusion.mygig_planner.client.GigInfo;
import com.dynamic_confusion.mygig_planner.client.SearchInfo;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class ServerSideServiceClientImpl implements ServerSideServiceClientInt {

	private ServerSideServiceAsync service;
	
	public ServerSideServiceClientImpl(){
		
		service = (ServerSideServiceAsync)GWT.create(ServerSideService.class);
		ServiceDefTarget endPoint = (ServiceDefTarget)this.service;
		endPoint.setServiceEntryPoint("/server-side");
	}
	

	@Override
	public void getUsers(AsyncCallback callback){
		
		this.service.getUsers(callback);
	}

	@Override
	public void getUsers(){
		
		// Get all users
		getUsers(new DefaultCallback());
	}

	@Override
	public void getUsers(Date dateAvailable, AsyncCallback callback){
		
		// Get users for this date
		this.service.getUsers(dateAvailable, callback);
	}

	@Override
	public void getUsers(Date[] datesAvailable, AsyncCallback callback){
		
		// Get users for this date
		this.service.getUsers(datesAvailable, callback);
	}

	@Override
	public void getUser(String username, AsyncCallback callback){
		
		// Get this user for this date
		this.service.getUser(username,callback);
	}

	@Override
	public void getDatesAvailable(String username, Date start, Date end, AsyncCallback callback){
		
		// Tell the service to get the dates available
		this.service.getDatesAvailable(username, start, end, callback);
	}
	
	@Override
	public void search(SearchInfo info, AsyncCallback callback) {

		// Tell our async service to search
		this.service.search(info, callback);
		
	}
	
	@Override
	public void search(SearchInfo info, int limit, int offset,
			AsyncCallback callback) {

		// Tell our async service to search
		this.service.search(info, limit, offset, callback);
		
	}

	@Override
	public void sendOffer(GigInfo gig, AsyncCallback callback) {
		
		// Get the current user
		String currentUser = Cookies.getCookie("activeUser");

		// If we dont have an active user logged in
		if(Cookies.getCookie("activeUser")==null){
			
			// Call the on failure method
			callback.onFailure(new Throwable("No active user found"));
			return;
		}
		
	
		// Tell the service to execute it
		this.service.sendOffer(gig, callback);
	}

	@Override
	public void acceptOffer(GigInfo gig, AsyncCallback callback) {
		
		// Get the current user
		String currentUser = Cookies.getCookie("activeUser");
 

		// If we dont have an active user logged in
		if(Cookies.getCookie("activeUser")==null){
			
			// Call the on failure method
			callback.onFailure(new Throwable("No active user found"));
			return;
		}
		
		// Tell the service to execute it
		this.service.acceptOffer(gig, callback);
		
	}

	@Override
	public void rejectOffer(GigInfo gig, AsyncCallback callback) {
		
		// Get the current user
		String currentUser = Cookies.getCookie("activeUser");
 

		// If we dont have an active user logged in
		if(Cookies.getCookie("activeUser")==null){
			
			// Call the on failure method
			callback.onFailure(new Throwable("No active user found"));
			return;
		}
		
		
		// Tell the service to execute it
		this.service.rejectOffer(gig, callback);
		
	}

	@Override
	public void getOffers(Date startRange, Date endRange, AsyncCallback callback) {
		
		// Get the current user
		String currentUser = Cookies.getCookie("activeUser");

		// If we dont have an active user logged in
		if(Cookies.getCookie("activeUser")==null){
			
			// Call the on failure method
			callback.onFailure(new Throwable("No active user found"));
			return;
		}

		// Tell the service to execute it
		this.service.getOffers(currentUser, startRange,endRange,callback);
		
	}

	@Override
	public void getOffers(AsyncCallback callback) {

		Date startDate = null;
		Date endDate = null;

		// Specify 
		getOffers(startDate,endDate,callback);
	}

	@Override
	public void getErrorMessage(AsyncCallback asyncCallback) {
		// TODO Auto-generated method stub
		
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
