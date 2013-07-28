package com.dynamic_confusion.mygig_planner.client.ss_service;

import java.util.Date;

import com.dynamic_confusion.mygig_planner.client.GigInfo;
import com.dynamic_confusion.mygig_planner.client.SearchInfo;
import com.dynamic_confusion.mygig_planner.client.UserInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ServerSideServiceAsync {

	
	void getErrorMessage(AsyncCallback callback);
	
	void getOffers(String user, Date date, AsyncCallback callback);
	void getOffers(String user, Date startRange, Date endRange, AsyncCallback callback);
	
	void getUsers(AsyncCallback callback);
	void getUsers(Date dateAvailable,AsyncCallback callback);
	void getUsers(Date[] dateAvailable,AsyncCallback callback);
	void getUser(String string, AsyncCallback callback);
	
	void getDatesAvailable(String username, Date start, Date end, AsyncCallback callback);

	void search(SearchInfo info, AsyncCallback callback);
	void search(SearchInfo info,int limit, int offset, AsyncCallback callback);
	

	void getTopUsers(int count, AsyncCallback callback);
	void getNewestUsers(int count, AsyncCallback callback);
	void getTopMusicians(int count, AsyncCallback callback);
	void getTopVenues(int count, AsyncCallback callback);
}
