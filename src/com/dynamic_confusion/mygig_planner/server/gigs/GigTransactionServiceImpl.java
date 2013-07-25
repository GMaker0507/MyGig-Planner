package com.dynamic_confusion.mygig_planner.server.gigs;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.dynamic_confusion.mygig_planner.client.GigTransaction;
import com.dynamic_confusion.mygig_planner.client.MyGig_Planner;
import com.dynamic_confusion.mygig_planner.client.gigs.GigTransactionService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class GigTransactionServiceImpl extends RemoteServiceServlet implements GigTransactionService{

	public String errorMessage = "No Message";
	
	public String getErrorMessage(){
		
		return errorMessage;
	}
	
	@Override
	public String sendOffer(GigTransaction gig) {
		
		try{
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			Entity gigOffer = new Entity("Gig");
			
			int zero = 0;
			
			// Set the gig information
			gigOffer.setProperty("name",gig.getName());
			gigOffer.setProperty("entityName",gig.getEntityName());
			gigOffer.setProperty("sendUser",gig.getSendUser());
			gigOffer.setProperty("recipientUser", gig.getRecipientUser());
			gigOffer.setProperty("status",zero);
			
			// Put it in the datastore
			datastore.put(gigOffer);

			
			
		}catch(Exception e){
			
			return e.toString();
		}
		
		// Return true by default
		return "success";
	}
	
	@Override
	public String acceptOffer(GigTransaction gig){
		
		try{
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			Query q = new Query("Gig").setFilter(new FilterPredicate("entityName",FilterOperator.EQUAL,gig.getEntityName()));
			
			// GEt this gig
			Entity gigEntity = datastore.prepare(q).asSingleEntity();

			// Set the status as 1 for accepted
			gigEntity.setProperty("status", 1);
			
			// Put the entity back in to update
			datastore.put(gigEntity);
			
			
		}catch(Exception e){
			
			return e.toString();
		}
		
		// Return true by default
		return "success";
		
	}

	@Override
	public String rejectOffer(GigTransaction gig) {
		
		try{
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			Query q = new Query("Gig").setFilter(new FilterPredicate("entityName",FilterOperator.EQUAL,gig.getEntityName()));
			
			// GEt this gig
			Entity gigEntity = datastore.prepare(q).asSingleEntity();

			// Set the status as -1 for rejected
			gigEntity.setProperty("status", -1);
			
			// Put the entity back in to update
			datastore.put(gigEntity);
			
			
		}catch(Exception e){
			
			return e.toString();
		}
		
		// Return true by default
		return "success";
	}

	@Override
	public GigTransaction[] getOffers(String user, Date startRange, Date endRange) {
		
		// An empty set of gigs
		GigTransaction[] gigs = new GigTransaction[0];
		
		try{
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			
			FilterPredicate recipient = new FilterPredicate("recipientUser",FilterOperator.EQUAL,user);
			FilterPredicate send = new FilterPredicate("sendUser",FilterOperator.EQUAL,user);
			
			Collection<Filter> predicates = new ArrayList<Filter>();
			
			predicates.add(recipient);
			predicates.add(send);
			
			Query getGigs = new Query("Gig").setFilter( new CompositeFilter(CompositeFilterOperator.OR,predicates));
			
			PreparedQuery pqGetGigs = datastore.prepare(getGigs);
			
			// Get all the entities that match
			List<Entity> gigEntities = pqGetGigs.asList(FetchOptions.Builder.withDefaults());			
			
			// Reformat the empty array
			gigs = new GigTransaction[gigEntities.size()];
			
			for(int i=0;i<gigEntities.size();i++){
				
				// Populate the array
				gigs[i] = new GigTransaction();
				gigs[i].setSendUser((String) gigEntities.get(i).getProperty("sendUser"));
				gigs[i].setRecipientUser((String) gigEntities.get(i).getProperty("recipientUser"));
				gigs[i].setStatus(Integer.parseInt(""+gigEntities.get(i).getProperty("status")));
				gigs[i].setName((String)gigEntities.get(i).getProperty("name"));
			}
			
		}catch(Exception e){
			
			errorMessage = e.getMessage();
			
			return null;
		}
		
		// Return true by default
		return gigs;
	}

}
