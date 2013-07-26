package com.dynamic_confusion.mygig_planner.server.ss_service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.dynamic_confusion.mygig_planner.client.GigInfo;
import com.dynamic_confusion.mygig_planner.client.SearchInfo;
import com.dynamic_confusion.mygig_planner.client.UserInfo;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideService;
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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ServerSideServiceImpl extends RemoteServiceServlet implements ServerSideService {

	public String errorMessage = "No Message";
	
	public String getErrorMessage(){
		
		return errorMessage;
	}
	
	@Override
	public String sendOffer(GigInfo gig) {
		
		try{
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			Entity gigOffer = new Entity("Gig",gig.getEntityName());
			
			int zero = 0;
			
			// Set the gig information
			gigOffer.setProperty("name",gig.name);
			gigOffer.setProperty("entityName",gig.getEntityName());
			gigOffer.setProperty("sendUser",gig.sendUser);
			gigOffer.setProperty("recipientUser", gig.recipientUser);
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
	public String acceptOffer(GigInfo gig){
		
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
	public String rejectOffer(GigInfo gig) {
		
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
	public GigInfo[] getOffers(String user, Date startRange, Date endRange) {
		
		// An empty set of gigs
		GigInfo[] gigs = new GigInfo[0];
		
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
			gigs = new GigInfo[gigEntities.size()];
			
			for(int i=0;i<gigEntities.size();i++){
				
				// Populate the array
				gigs[i] = new GigInfo();
				gigs[i].key = KeyFactory.keyToString(gigEntities.get(i).getKey());
				gigs[i].sendUser=((String) gigEntities.get(i).getProperty("sendUser"));
				gigs[i].recipientUser=((String) gigEntities.get(i).getProperty("recipientUser"));
				gigs[i].status=(Integer.parseInt(""+gigEntities.get(i).getProperty("status")));
				gigs[i].name=((String)gigEntities.get(i).getProperty("name"));
			}
			
		}catch(Exception e){
			
			errorMessage = e.getMessage();
			
			return null;
		}
		
		// Return true by default
		return gigs;
	}
	
	@Override
	public UserInfo search(SearchInfo info) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public UserInfo search(SearchInfo info, int limit, int offset) {
		// TODO Auto-generated method stub
		return null;
	}
	

	/**
	 * Get all users
	 */
	@Override
	public UserInfo[] getUsers() {
		
		UserInfo[] users = null;
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Two queries on the datastorie
		Query allUsers = new Query("User");

		PreparedQuery pqAllUsers = datastore.prepare(allUsers);

		// Get all user entitiesin a list
		List<Entity> userEntities = pqAllUsers.asList(FetchOptions.Builder.withDefaults());
		
		// Reformat the rray
		users = new UserInfo[userEntities.size()];
		
		// For each user entity
		for(int i=0;i<users.length;i++){
			
			// ADd the username ot the list
			users[i] = new UserInfo((String) userEntities.get(i).getProperty("username"));
		}
		
		// TODO Auto-generated method stub
		return users;
	}
	
	/**
	 * Get all users who are available on the given dates
	 */
	public UserInfo[] getUsers(Date[] datesAvailable){
		
		UserInfo[] users = new UserInfo[0];		
		
		try{
			
			// Get a ref to the datastore
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			// Initiate the filter
			Collection<Filter> datesFilter = new ArrayList<Filter>();
			
			// For each date
			for(int i=0;i<datesAvailable.length;i++){

				// Get the current date
				Date dateAvailable = datesAvailable[i];
				
				// Add to the predicate
				datesFilter.add(new FilterPredicate("dateAvailable",FilterOperator.EQUAL,dateAvailable));
			}
			
			// Create a query for the username of a user
			Query get = new Query("Availibility").setFilter(new CompositeFilter(CompositeFilterOperator.AND, datesFilter));
			
			// Prepare the query
			PreparedQuery pqGet = datastore.prepare(get);
			
			// Get the entity
			List<Entity> availableEntities = pqGet.asList(FetchOptions.Builder.withDefaults());
			
			// Make sure we have a value
			if(availableEntities==null)return null;
			
			users = new UserInfo[availableEntities.size()];
			
			// For each user
			for(int i=0;i<users.length;i++){
				
				// Create the user info entity
				users[i] = new UserInfo((String) availableEntities.get(0).getProperty("username"));
			}
			
		}catch(Exception e){
			
			users = new UserInfo[0];
		}
		
		return users;
	}
	
	/**
	 * Get users available on the given date
	 */
	public UserInfo[] getUsers(Date dateAvailable){
		
		// Get users for only this date
		return getUsers(new Date[]{dateAvailable});
	}
	
	/**
	 * Get the user with the given username
	 */
	public UserInfo getUser(String username){
		
		// Get a ref to the datastore
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Create a query for the username of a user
		Query get = new Query("User").setFilter(new FilterPredicate("username",FilterOperator.EQUAL,username));
		
		// Prepare the query
		PreparedQuery pqGet = datastore.prepare(get);
		
		// Get the entity
		Entity userEntity = pqGet.asSingleEntity();
		
		// Make sure we have a value
		if(userEntity==null)return null;
		
		// Create the instance
		UserInfo gotUser = new UserInfo(username);
		
		// Return the instance
		return gotUser;
	}
	
	public Date[] getDatesAvailable(String username, Date start, Date end){

		
		// Get a ref to the datastore
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
				
		Collection<Filter> joinFilterCollection = new ArrayList<Filter>();
		
		joinFilterCollection.add(new FilterPredicate("dateAvailable",FilterOperator.GREATER_THAN_OR_EQUAL,start));
		joinFilterCollection.add(new FilterPredicate("dateAvailable",FilterOperator.LESS_THAN_OR_EQUAL,end));
		joinFilterCollection.add(new FilterPredicate("bandName",FilterOperator.EQUAL,username));
		
		CompositeFilter joinFilters = new CompositeFilter(CompositeFilterOperator.AND, joinFilterCollection);		
		
		// Create a query for the username of a user
		Query get = new Query("Availability").setFilter(joinFilters);
		
		// Prepare the query
		PreparedQuery pqGet = datastore.prepare(get);
		
		// Get the entity
		List<Entity> dateEntities = pqGet.asList(FetchOptions.Builder.withDefaults());
		
		Date[] dates = new Date[dateEntities.size()];
		
		for(int i=0;i<dates.length;i++){
			
			dates[i] = (Date) dateEntities.get(i).getProperty("dateAvailable");
		}

		return dates;
		
	}
}