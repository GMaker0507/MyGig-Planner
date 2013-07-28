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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ServerSideServiceImpl extends RemoteServiceServlet implements ServerSideService {

	public String errorMessage = "No Message";
	
	public String getErrorMessage(){
		
		return errorMessage;
	}
	
	
	/**
	 * Get offers on the given date
	 */
	@Override
	public GigInfo[] getOffers(String user, Date date){
		
		return getOffers(user,date,date);
	}

	/**
	 * Get offers in the given range
	 */
	@Override
	public GigInfo[] getOffers(String user, Date startRange, Date endRange) {
		
		// An empty set of gigs
		GigInfo[] gigs = new GigInfo[0];
		
		try{
			
			// Get the datastore
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			// Match recipients or senders
			FilterPredicate recipient = new FilterPredicate("recipientUser",FilterOperator.EQUAL,user);
			FilterPredicate send = new FilterPredicate("sendUser",FilterOperator.EQUAL,user);
			
			Collection<Filter> userCollection = new ArrayList<Filter>();
			Collection<Filter> dateCollection = new ArrayList<Filter>();
			Collection<Filter> userDateCollection = new ArrayList<Filter>();
			
			userCollection.add(recipient);
			userCollection.add(send);
			
			long toDays = 1000*60*60*24;
			long numDays = 1+(endRange.getTime() - startRange.getTime()) / toDays;
			
			System.out.println("We Have "+numDays+" Days from "+startRange+" to "+endRange);

			Date d = new Date();
			
			for(int i=0;i<numDays;i++){
				
				// Set the days
				d.setTime(startRange.getTime()+toDays*i);
				
				// Clear the minor parts
				d.setHours(0);
				d.setMinutes(0);
				d.setSeconds(0);	
				
				System.out.println("Adding for day: "+d);
				
				// Match date sent or replied
				dateCollection.add(new FilterPredicate("dateSent",FilterOperator.EQUAL,d));
				dateCollection.add(new FilterPredicate("dateReplied",FilterOperator.EQUAL,d));
			}
			
			// We or the recipient or sender
			CompositeFilter userFilter = new CompositeFilter(CompositeFilterOperator.OR,userCollection);
			
			// We or the date sent or replied
			CompositeFilter dateFilter = new CompositeFilter(CompositeFilterOperator.OR,dateCollection);
			
			userDateCollection.add(userFilter);
			userDateCollection.add(dateFilter);
			
			// We and the two previous date filter and user filter
			CompositeFilter userDateFilter = new CompositeFilter(CompositeFilterOperator.AND,userDateCollection);
			
			// Get gigs meeting this query
			Query getGigs = new Query("Gig").setFilter(dateFilter);
			
			PreparedQuery pqGetGigs = datastore.prepare(getGigs);
			
			// Get all the entities that match
			List<Entity> gigEntities = pqGetGigs.asList(FetchOptions.Builder.withDefaults());			
			
			// Reformat the empty array
			gigs = new GigInfo[gigEntities.size()];
			
			System.out.println("We found "+gigEntities.size()+" gigs");
			
			for(int i=0;i<gigEntities.size();i++){
				
				// Populate the array
				gigs[i] = new GigInfo();
				gigs[i].key = KeyFactory.keyToString(gigEntities.get(i).getKey());
				gigs[i].sendUser=((String) gigEntities.get(i).getProperty("sendUser"));
				gigs[i].recipientUser=((String) gigEntities.get(i).getProperty("recipientUser"));
				gigs[i].status=(Integer.parseInt(""+gigEntities.get(i).getProperty("status")));
				gigs[i].name=((String)gigEntities.get(i).getProperty("name"));
				gigs[i].dateSent = (Date)gigEntities.get(i).getProperty("dateSent");
				gigs[i].dateReplied = (Date)gigEntities.get(i).getProperty("dateReplied");
				
				System.out.println(gigs[i].toString());
			}
			
		}catch(Exception e){
			
			System.out.println(errorMessage = e.getMessage());
			
			return null;
		}
		
		// Return true by default
		return gigs;
	}
	
	/**
	 * Search with a page and limit
	 */
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
		
		FetchOptions limitFetch = FetchOptions.Builder.withLimit(limit).offset(limit*offset);
		FetchOptions firstFetch = info.date==null ? limitFetch : FetchOptions.Builder.withDefaults();
		
		List<Entity> sqEntities = pqSearchQuery.asList(firstFetch);
		
		if(info.date!=null){
			
			System.out.println(sqEntities.size()+" entities before date");
			
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
			dateQuery = new Query("Availability").setFilter(new FilterPredicate("dateAvailable",FilterOperator.EQUAL,info.date));
			
			PreparedQuery pqDateQuery = datastore.prepare(dateQuery);
			
			List<Entity> dqEntities = pqDateQuery.asList(FetchOptions.Builder.withDefaults());
			
			System.out.println(dqEntities.size()+"Entities after availablity query");
			
			if(dqEntities.size()>0){
				
				List<Filter> userDateOrs = new ArrayList<Filter>();
				
				sqEntities.clear();
				
				for(int i=0;i<dqEntities.size();i++){

					try {
						sqEntities.add(datastore.get(KeyFactory.createKey("User", (String)dqEntities.get(i).getProperty("bandName"))));
					} catch (EntityNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.println("ERror with: "+(String)dqEntities.get(i).getProperty("bandName")+"\n"+e.toString());
					}
				}
				
				System.out.println(sqEntities.size()+" Entities after search query with date");
			
			}else sqEntities.clear();
		}
		
		UserInfo[] returnUsers = new UserInfo[sqEntities.size()];
		
		// Set users from the found entities
		setUsersFromEntities(returnUsers, sqEntities);		
		
		return returnUsers;
	}
	
	/**
	 * Normal search
	 */
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
	
	/**
	 * Get the top users
	 */
	public UserInfo[] getNewestUsers(int count){
		
		UserInfo[] users = null;
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Two queries on the datastorie
		Query allUsers = new Query("User");

		// Sort on descending
		allUsers.addSort("dateJoined",SortDirection.DESCENDING);
		
		PreparedQuery pqAllUsers = datastore.prepare(allUsers);

		// Get all user entitiesin a list
		List<Entity> userEntities = pqAllUsers.asList(FetchOptions.Builder.withLimit(count));
		
		users = new UserInfo[userEntities.size()];
		
		setUsersFromEntities(users, userEntities);
		
		return users;
		
	}
	
	/**
	 * Get the top users
	 */
	@Override
	public UserInfo[] getTopUsers(int count){
		
		UserInfo[] users = null;
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Two queries on the datastorie
		Query allUsers = new Query("User");

		// Sort on descending
		allUsers.addSort("gigCount",SortDirection.DESCENDING);
		
		PreparedQuery pqAllUsers = datastore.prepare(allUsers);

		// Get all user entitiesin a list
		List<Entity> userEntities = pqAllUsers.asList(FetchOptions.Builder.withLimit(count));
		
		users = new UserInfo[userEntities.size()];
		
		setUsersFromEntities(users, userEntities);
		
		return users;
		
	}
	
	/**
	 * Get the top venues
	 */
	@Override
	public UserInfo[] getTopVenues(int count){
		
		UserInfo[] users = null;
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Two queries on the datastorie
		Query allUsers = new Query("User").setFilter(new FilterPredicate("type",FilterOperator.EQUAL,"venue"));

		// Sort on descending
		allUsers.addSort("gigCount",SortDirection.DESCENDING);
		
		PreparedQuery pqAllUsers = datastore.prepare(allUsers);

		// Get all user entitiesin a list
		List<Entity> userEntities = pqAllUsers.asList(FetchOptions.Builder.withLimit(count));
		
		users = new UserInfo[userEntities.size()];
		
		setUsersFromEntities(users, userEntities);
		
		return users;
		
	}
	
	/**
	 * Get the top musicians
	 */
	@Override
	public UserInfo[] getTopMusicians(int count){
		
		UserInfo[] users = null;
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Two queries on the datastorie
		Query allUsers = new Query("User").setFilter(new FilterPredicate("type",FilterOperator.EQUAL,"musician"));

		// Sort on descending
		allUsers.addSort("gigCount",SortDirection.DESCENDING);
		
		PreparedQuery pqAllUsers = datastore.prepare(allUsers);

		// Get all user entitiesin a list
		List<Entity> userEntities = pqAllUsers.asList(FetchOptions.Builder.withLimit(count));
		
		users = new UserInfo[userEntities.size()];
		
		setUsersFromEntities(users, userEntities);
		
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
		user.gigCount = Integer.parseInt(entity.getProperty("gigCount").toString());
		user.dateJoined = (Date) entity.getProperty("dateJoined");
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
	
	/**
	 * Get the dates a specific user is available within a given range
	 */
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
