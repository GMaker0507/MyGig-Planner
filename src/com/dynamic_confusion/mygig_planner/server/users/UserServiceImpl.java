package com.dynamic_confusion.mygig_planner.server.users;

import java.util.HashMap;
import java.util.List;

import com.dynamic_confusion.mygig_planner.client.users.UserService;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UserServiceImpl extends RemoteServiceServlet implements UserService {

	@Override
	public boolean attemptLogin(String username, String password) {
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// We want a query on the user entities
		Query usersQuery = new Query("User");

		// Set the username and password filter
		usersQuery.setFilter(new FilterPredicate("username", FilterOperator.EQUAL, username));
		usersQuery.setFilter(new FilterPredicate("password", FilterOperator.EQUAL, password));
		
		// Prepare the query on the datastore
		PreparedQuery pq = datastore.prepare(usersQuery);
		
		// Return if we have a valid user entity
		return pq.asList(FetchOptions.Builder.withDefaults()).size()>0;
	}
	
	@Override
	public boolean attemptLogout(){
		
		// Do we have an active user
		if(Cookies.getCookie("activeUser")==null) return false;
		
		// Remove the active user cookie
		Cookies.removeCookie("activeUser");
		
		// We successfully logged
		return true;
	}

	@Override
	public boolean attemptRegister(HashMap<String, Object> newUser) {
		
		// Make sure we have these fields
		if(!newUser.containsKey("username"))return false;
		if(!newUser.containsKey("password"))return false;
		if(!newUser.containsKey("type"))return false;
		
		// Createa  new user entity
		Entity newUserEntity = new Entity("User",(String)newUser.get("username"));
		
		Object[] keys = newUser.keySet().toArray();
		
		// For each object i nthe hash map
		for(int i=0;i<keys.length;i++){
			
			// GEt the keys
			String key = (String)keys[i];
			
			// Set the property as in the hashmap
			newUserEntity.setProperty(key,newUser.get(key));
		}
		
		// TODO Auto-generated method stub
		return attemptRegister(newUserEntity);
	}
	
	public boolean attemptRegister(Entity newUser){
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Two queries on the datastorie
		Query checkUsername = new Query("User");
		Query checkEmail = new Query("User");

		// Set the username and email filter
		checkUsername.setFilter(new FilterPredicate("username", FilterOperator.EQUAL, newUser.getProperty("username")));
		checkEmail.setFilter(new FilterPredicate("email", FilterOperator.EQUAL, newUser.getProperty("email")));
		
		// Prepare the query on the datastore
		PreparedQuery pqUsername = datastore.prepare(checkUsername);
		PreparedQuery pqEmail = datastore.prepare(checkEmail);
		
		// Is this a valid register
		boolean valid = pqUsername.asSingleEntity()==null&&pqEmail.asSingleEntity()==null;
		
		// If this is valid
		if(valid){
			
			// Add the new user
			datastore.put(newUser);
		}
		
		return valid;
	}
}
