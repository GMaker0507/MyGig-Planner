package com.dynamic_confusion.mygig_planner.client.gigs;

import java.util.Date;
import java.util.HashMap;

import com.dynamic_confusion.mygig_planner.client.GigTransaction;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GigTransactionServiceAsync {
	
	void getErrorMessage(AsyncCallback callback);


	/**
	 * Try to send an offer to an user with the given details.
	 */
	void sendOffer(GigTransaction gig, AsyncCallback callback);
	
	/**
	 * Reject an offer from a user
	 * @param key You need the key which should be unique
	 */
	void rejectOffer(GigTransaction gig, AsyncCallback callback);
	
	/**
	 * Accept an offer from a user
	 * @param key You need the key which should be unique
	 */
	void acceptOffer(GigTransaction gig, AsyncCallback callback);
	
	/**
	 * Get all offers within a given range
	 */
	void getOffers(String user, Date startRange, Date endRange, AsyncCallback callback);
}
