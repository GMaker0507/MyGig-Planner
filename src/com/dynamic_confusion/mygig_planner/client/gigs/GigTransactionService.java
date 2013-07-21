package com.dynamic_confusion.mygig_planner.client.gigs;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import com.dynamic_confusion.mygig_planner.client.GigTransaction;
import com.google.gwt.user.client.rpc.RemoteService;

public interface GigTransactionService extends RemoteService {

	String getErrorMessage();
	
	/**
	 * Try to send an offer to an user with the given details.
	 */
	String sendOffer(GigTransaction gig);
	
	/**
	 * Accept an offer from a user
	 */
	String acceptOffer(GigTransaction gig);
	
	/**
	 * Reject an offer from a user
	 */
	String rejectOffer(GigTransaction gig);

	/**
	 * Get all offers within a given range
	 */
	GigTransaction[] getOffers(String user, Date startRange, Date endRange);
}