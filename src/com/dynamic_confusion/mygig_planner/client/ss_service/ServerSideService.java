package com.dynamic_confusion.mygig_planner.client.ss_service;

import java.util.Date;

import com.dynamic_confusion.mygig_planner.client.GigInfo;
import com.dynamic_confusion.mygig_planner.client.SearchInfo;
import com.dynamic_confusion.mygig_planner.client.UserInfo;
import com.google.gwt.user.client.rpc.RemoteService;

public interface ServerSideService extends RemoteService {
	
	UserInfo[] getUsers();
	UserInfo[] getUsers(Date dateAvailable);
	UserInfo[] getUsers(Date[] dateAvailable);
	UserInfo getUser(String username);
	
	Date[] getDatesAvailable(String username, Date start, Date end);

	UserInfo[] search(SearchInfo info);
	UserInfo[] search(SearchInfo info,int limit, int offset);

	String getErrorMessage();
	
	String sendOffer(GigInfo gig);
	String acceptOffer(GigInfo gig);
	String rejectOffer(GigInfo gig);
	GigInfo[] getOffers(String user, Date startRange, Date endRange);

}
