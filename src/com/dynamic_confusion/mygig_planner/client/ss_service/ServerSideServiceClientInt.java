package com.dynamic_confusion.mygig_planner.client.ss_service;

import java.util.Date;

import com.dynamic_confusion.mygig_planner.client.GigInfo;
import com.dynamic_confusion.mygig_planner.client.SearchInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServerSideServiceClientInt {

	
	void getOffers(Date startRange, Date endRange, AsyncCallback callback);
	void getOffers(AsyncCallback callback);

	void search(SearchInfo info, AsyncCallback callback);	
	void search(SearchInfo info,int limit, int offset, AsyncCallback callback);

	void getUsers();
	void getUsers(AsyncCallback callback);
	void getUsers(Date dateAvailable,AsyncCallback callback);
	void getUsers(Date[] dateAvailable,AsyncCallback callback);
	void getUser(String string, AsyncCallback callback);

	void getDatesAvailable(String username, Date start, Date end, AsyncCallback callback);
	void getErrorMessage(AsyncCallback asyncCallback);
	void getTopUsers(int i, AsyncCallback asyncCallback);
	void getTopMusicians(int i, AsyncCallback asyncCallback);
	void getTopVenues(int i, AsyncCallback asyncCallback);
	void getNewestUsers(int i, AsyncCallback asyncCallback);
}
