package com.dynamic_confusion.mygig_planner.server.ss_service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.dynamic_confusion.mygig_planner.client.GigInfo;
import com.dynamic_confusion.mygig_planner.client.SearchInfo;
import com.dynamic_confusion.mygig_planner.client.UserInfo;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideService;
import com.dynamic_confusion.mygig_planner.client.ui.Genre;
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
	public UserInfo[] search(SearchInfo info, int limit, int offset) {
		// TODO Auto-generated method stub
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		List<Filter> ands = new ArrayList<Filter>();
		
		// Add requirements if they were set
		if(info.genre!=null)ands.add(new FilterPredicate("genre",FilterOperator.EQUAL,info.genre));
		if(info.hasPA)ands.add(new FilterPredicate("hasPA",FilterOperator.EQUAL,true));
		if(info.hasSoundPerson)ands.add(new FilterPredicate("hasSoundPerson",FilterOperator.EQUAL,true));
		if(info.onlyOriginalMusic)ands.add(new FilterPredicate("onlyOriginalMusic",FilterOperator.EQUAL,true));
		if(info.hasHospitalityPack)ands.add(new FilterPredicate("hasHospitalityPack",FilterOperator.EQUAL,true));
		
		CompositeFilter compFilter = null;

		Query searchQuery = null;
		
		// If we have more than one
		if(ands.size()>1){
			
			// Get filter for all
			compFilter = new CompositeFilter(CompositeFilterOperator.AND,ands);
			searchQuery = new Query("User").setFilter(compFilter);	
			
		// Get the users for the first filter
		}else if(ands.size()==1)searchQuery = new Query("User").setFilter(ands.get(0));	
		
		// Get all users
		else searchQuery = new Query("User");
		
		PreparedQuery pqSearchQuery = datastore.prepare(searchQuery);
		
		List<Entity> sqEntities = pqSearchQuery.asList(FetchOptions.Builder.withLimit(limit).offset(limit*offset));
		
		if(info.date!=null){
			
			Query dateQuery = null;
			
			List<Filter> dateOrs = new ArrayList<Filter>();
			
			for(int i=0;i<sqEntities.size();i++){
				
				// Add each band name
				dateOrs.add(new FilterPredicate("bandName",FilterOperator.EQUAL,sqEntities.get(i).getProperty("username")));
			}
			
			List<Filter> dateAnds = new ArrayList<Filter>();
			
			// Add matching one of the names
			dateAnds.add(new CompositeFilter(CompositeFilterOperator.OR,dateOrs));	
			
			// Add matching the date
			dateAnds.add(new FilterPredicate("dateAvailable",FilterOperator.EQUAL,info.date));
			
			// Search for availability
			dateQuery = new Query("Availability").setFilter(new CompositeFilter(CompositeFilterOperator.AND,dateAnds));
			
			PreparedQuery pqDateQuery = datastore.prepare(dateQuery);
			
			List<Entity> dqEntities = pqDateQuery.asList(FetchOptions.Builder.withDefaults());
			
			List<Filter> userDateOrs = new ArrayList<Filter>();
			
			for(int i=0;i<sqEntities.size();i++){
				
				// Add each band name
				dateOrs.add(new FilterPredicate("username",FilterOperator.EQUAL,dqEntities.get(i).getProperty("bandName")));
			}
			
			CompositeFilter compFilterDate = new CompositeFilter(CompositeFilterOperator.OR,userDateOrs);
		
			// Recreatthe query
			searchQuery = new Query("User").setFilter(compFilterDate);

			// Re-prepare the query
			pqSearchQuery = datastore.prepare(searchQuery);
			
			// Get new entities
			sqEntities = pqSearchQuery.asList(FetchOptions.Builder.withDefaults());
		}
		
		UserInfo[] returnUsers = new UserInfo[sqEntities.size()];
		
		// Set users from the found entities
		setUsersFromEntities(returnUsers, sqEntities);		
		
		return returnUsers;
	}
	
	@Override
	public UserInfo[] search(SearchInfo info) {
		// TODO Auto-generated method stub
		return search(info,10,0);
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
		
		setUsersFromEntities(users, userEntities);
		
		// TODO Auto-generated method stub
		return users;
	}
	
	/**
	 * Get all users who are available on the given dates
	 */
	public UserInfo[] getUsers(Date[] datesAvailable){
		
		UserInfo[] users = new UserInfo[0];		
		
			
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
	
		// Set the arrayfrom the list
		setUsersFromEntities(users,availableEntities);
		
		return users;
	}
	
	private void setUsersFromEntities(UserInfo[] users,List<Entity> availableEntities){

		
		// For each user
		for(int i=0;i<users.length;i++){
			
			// Create the user info entity
			users[i] = new UserInfo((String) availableEntities.get(i).getProperty("username"));
			
			// Get the other fields from the entity
			setUserFromEntity(users[i],availableEntities.get(i));
		}
		
	}
	
	private void setUserFromEntity(UserInfo user,Entity entity){
		
		// get the other user info fields
		user.genre = (String) entity.getProperty("genre");
		user.email = (String) entity.getProperty("email");
		user.hasPA = (boolean) entity.getProperty("hasPA");
		user.password = (String) entity.getProperty("password");
		user.hasSoundPerson = (boolean) entity.getProperty("hasSoundPerson");
		user.hasHospitalityPack = (boolean) entity.getProperty("hasHospitalityPack");
		user.onlyOriginalMusic = (boolean) entity.getProperty("onlyOriginalMusic");
		user.firstName = (String) entity.getProperty("firstName");
		user.lastName = (String) entity.getProperty("lastName");
		user.priceRange = (Double) entity.getProperty("priceRange");
		user.openHours = (String) entity.getProperty("openHours");
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
		
		setUserFromEntity(gotUser, userEntity);
		
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
