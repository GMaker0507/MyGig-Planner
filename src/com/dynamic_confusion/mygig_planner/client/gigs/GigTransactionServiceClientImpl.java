package com.dynamic_confusion.mygig_planner.client.gigs;

import java.util.Date;
import java.util.HashMap;

import com.dynamic_confusion.mygig_planner.client.GigTransaction;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class GigTransactionServiceClientImpl implements GigTransactionServiceClientInt {
	
	private GigTransactionServiceAsync service;
	
	public GigTransactionServiceClientImpl(String url){
		
		service = (GigTransactionServiceAsync)GWT.create(GigTransactionService.class);
		ServiceDefTarget endPoint = (ServiceDefTarget)this.service;
		endPoint.setServiceEntryPoint(url);
	}

	@Override
	public void sendOffer(GigTransaction gig, AsyncCallback callback) {
		
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
	public void acceptOffer(GigTransaction gig, AsyncCallback callback) {
		
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
	public void rejectOffer(GigTransaction gig, AsyncCallback callback) {
		
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
	
	public void getErrorMessage(AsyncCallback callback){
		
		this.service.getErrorMessage(callback);
	}
}
