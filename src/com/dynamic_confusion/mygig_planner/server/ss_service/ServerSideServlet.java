package com.dynamic_confusion.mygig_planner.server.ss_service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dynamic_confusion.mygig_planner.client.UserInfo;
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

@SuppressWarnings("serial")
public class ServerSideServlet extends HttpServlet {

	public DatastoreService datastore = null;
	public PrintWriter out = null;
	public String action = null;
	public String[] split = null;
	
	private void processGigTransactionRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PrintWriter out = resp.getWriter();

		String action = req.getParameter("action");
		
		// If we are accepting or rejecting
		if(action.equals("accept")||action.equals("reject")){

			// Get the 2nd and 3rd parameters
			String id = req.getParameter("key");
			String username = req.getParameter("recipientUser");			
		
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			try {		
				Entity getEntity = datastore.get(KeyFactory.stringToKey(id));
				
				if(getEntity.getProperty("recipientUser").equals(username)){
					
					//Set the status to the right property
					if(action.equals("accept")){
						getEntity.setProperty("status",1);						
						
						// Get each user affected
						Entity user1Entity = datastore.get(KeyFactory.createKey("User", (String)getEntity.getProperty("sendUser")));
						Entity user2Entity = datastore.get(KeyFactory.createKey("User",req.getParameter("recipientUser")));
						
						// Increase the gig counts
						user1Entity.setProperty("gigCount", Integer.parseInt(user1Entity.getProperty("gigCount").toString())+1);
						user2Entity.setProperty("gigCount", Integer.parseInt(user2Entity.getProperty("gigCount").toString())+1);
					    
						// Update
						datastore.put(user2Entity);
						datastore.put(user1Entity);
						
					}else if(action.equals("reject"))getEntity.setProperty("status",-1);
					
					// Set the date it was sent
					getEntity.setProperty("dateReplied",new Date());
					
					datastore.put(getEntity);

					out.print("success");
				}else out.println("The given user is not the recipient of this gig transfer.");
				
			} catch (Exception e) {
	
	
				out.println("failure: "+id);
				out.println("failure: "+username);
			}
			
		// If we are sending
		}else if(action.equals("send")){
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			Entity gigOffer = new Entity("Gig");
			
			int zero = 0;
			
			// Set the gig information
			gigOffer.setProperty("name",req.getParameter("name"));
			gigOffer.setProperty("sendUser",req.getParameter("sendUser"));
			gigOffer.setProperty("recipientUser", req.getParameter("recipientUser"));
			gigOffer.setProperty("dateSent",new Date());
			gigOffer.setProperty("status",zero);
			
			// Put it in the datastore
			datastore.put(gigOffer);
		}
		
		// Close the output
		out.close();
	}
	
	
	private void generateUsers(int num){
		
		UserInfo newUser = new UserInfo();
		
		Entity newEntity = null;
		
		for(int i=0;i<num;i++){
			
			newUser.randomize();
			
			newEntity = new Entity("User",newUser.username);				

			// Set the other user entity properties
			newEntity.setProperty("username",newUser.username);
			newEntity.setProperty("type", newUser.type);
			newEntity.setProperty("email",newUser.email);
			newEntity.setProperty("genre", newUser.genre);
			newEntity.setProperty("password", newUser.password);
			newEntity.setProperty("hasHospitalityPack", newUser.hasHospitalityPack);
			newEntity.setProperty("hasPA", newUser.hasPA);
			newEntity.setProperty("hasSoundPerson", newUser.hasSoundPerson);
			newEntity.setProperty("onlyOriginalMusic", newUser.onlyOriginalMusic);
			newEntity.setProperty("firstName", newUser.firstName);
			newEntity.setProperty("lastName", newUser.lastName);
			newEntity.setProperty("priceRange", newUser.priceRange);
			newEntity.setProperty("capacity", newUser.capacity);
			newEntity.setProperty("openHours", newUser.openHours);			
			newEntity.setProperty("gigCount", newUser.gigCount);	
			newEntity.setProperty("dateJoined", newUser.dateJoined);
			
			out.println("Creating account ["+newUser.username+"] for "+newUser.firstName+" "+newUser.lastName+"");
			
			// Add to the datastore
			datastore.put(newEntity);
		}
	}

	
	private void generateAvailability(){
		
		Query allUsers = new Query("User");
		
		PreparedQuery pqAllUsers = datastore.prepare(allUsers);
		
		List<Entity> entityAllUsers = pqAllUsers.asList(FetchOptions.Builder.withDefaults());
		
		// For each user
		for(int h=0;h<entityAllUsers.size();h++){

			Date today = new Date();
			int year = today.getYear();
			int month = today.getMonth();
			int months = 1+(int)Math.floor(Math.random()*3.0);
			
			// For each month
			for(int j=0;j<months;j++){
				
				
				/// The dates in a month
				int[] days = new int[]{
					1,2,3,4,5,6,7,8,9,10,
					11,1,2,13,14,15,16,17,18,19,20,
					21,22,23,24,25,26,27,28,29,30,31
				};
				
				int numDays = 1+(int)Math.floor(Math.random()*30.0);
				
				// For each day
				for(int k=0;k<numDays;k++){
					
					int ni = (int)Math.floor(Math.random()*31.0);
					
					// Dont use it if its negative one
					if(days[ni]==-1)continue;
					
					// Whats the negative one
					int date = days[ni];
					
					days[ni] = -1;
					
					String user = (String) entityAllUsers.get(h).getProperty("username");

					Date dt  = new Date(year,month+j,date);
					Entity newAvailability = new Entity("Availability",user+dt.toString());
					
					
					// Set the bandis availablie on this date
					newAvailability.setProperty("dateAvailable",dt);
					newAvailability.setProperty("bandName", user);
					
					// Add to the datastore
					datastore.put(newAvailability);
					
					out.println(entityAllUsers.get(h).getProperty("username")+" is available on "+dt.toString());
				
				}
			}	
		}
	}
	
	private void generateGigs(){
		

		Query allUsers = new Query("User");
		
		PreparedQuery pqAllUsers = datastore.prepare(allUsers);
		
		List<Entity> entityAllUsers = pqAllUsers.asList(FetchOptions.Builder.withDefaults());
		
		int num = (int)Math.floor(Math.random()*20.0);
		
		for(int i=0;i<entityAllUsers.size();i++){

			Entity user1Entity = entityAllUsers.get(i);
			
			for(int j=0;j<num;j++){
				
				int r1 = (int)Math.floor(Math.random()*((double)entityAllUsers.size()));
				
				Entity user2Entity = entityAllUsers.get(r1);
	
				Entity gigOffer = new Entity("Gig");
				
				int zero = 0;
				
				int m = 1+(int)Math.floor(Math.random()*((double)12));
				int d = 1+(int)Math.floor(Math.random()*((double)31));
				int m2 = m==12 ? m : m+(int)Math.floor(Math.random()*((double)(12-m)));
				int d2 = d==31 ? d : d+(int)Math.floor(Math.random()*((double)(31-d)));
				
				boolean rep = Math.random()>0.3;
				
				int y = (int)Math.floor(Math.random()*((double)3));
				
				int repN = Math.random()> 0.5 ? -1 : 1;
				
				// Set the gig information
				gigOffer.setProperty("name",user1Entity.getProperty("random-gig-1."+Math.random()));
				gigOffer.setProperty("sendUser",user1Entity.getProperty("username"));
				gigOffer.setProperty("recipientUser", user2Entity.getProperty("username"));
				gigOffer.setProperty("dateSent",new Date(113-y,m,d));
				gigOffer.setProperty("dateReplied",rep ? new Date(113,m2,d2) : null);
				gigOffer.setProperty("status",rep ? zero : repN);
				
				String rpd = repN == 1 ? "Accepted" : "Rejected";
				
				if(repN==1){
					
					// Increase the gig counts
					user1Entity.setProperty("gigCount", Integer.parseInt(user1Entity.getProperty("gigCount").toString())+1);
					user2Entity.setProperty("gigCount", Integer.parseInt(user2Entity.getProperty("gigCount").toString())+1);
					
					// Update
					datastore.put(user2Entity);
					datastore.put(user1Entity);
				}
				
				out.println("Gig from "+(String)gigOffer.getProperty("sendUser")+" to "+
							(String)gigOffer.getProperty("recipientUser")+", sent on "+
							(Date)gigOffer.getProperty("dateSent")+". "+
							(rep ? (rpd+" on "+gigOffer.getProperty("dateReplied")) :"No reply yet"));
						
				
				// Put it in the datastore
				datastore.put(gigOffer);
			
			}
		
		}
	}
	
	private void clearTable(String table){
		
		Query users = new Query(table);
		
		PreparedQuery pUsers = datastore.prepare(users);
		
		// Get the user entities
		List<Entity> usersList = pUsers.asList(FetchOptions.Builder.withDefaults());
		
		out.println("Removing "+usersList.size()+" from "+table);
		
		while(usersList.size()>0){
			
			// Delete this user
			datastore.delete(usersList.get(0).getKey());
			
			// Remove from the list
			usersList.remove(0);
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private void processAdminRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String adminAction=split[1];
		
		try{
		
			// Are we clearing tables
			if(adminAction.equals("clear")){
				
				// Do we want to clear all
				if(split[2].equals("all")){

					// Clear all
					clearTable("User");
					clearTable("Availability");
					clearTable("Gig");
				}else{
				
					// Clear just that table
					clearTable(split[2]);
				}
				
				out.println("success");
				
			// Do we want to generate
			}else if(adminAction.equals("generate")){
				
				// Do we want to perform a full test
				if(split[2].equals("FullTest")){
					
					int num = 20 + (int)Math.floor(Math.random()*25);
					
					// Clear all
					clearTable("User");
					clearTable("Availability");
					clearTable("Gig");
					
					// Generate some random users
					generateUsers(num);
					
					// Generate user availability
					generateAvailability();
					
					// Generate random gigs
					generateGigs();
					
					out.println("success");
					
				}else if(split[2].equals("User")){
					
					int num = Integer.parseInt(split[3]);
					
					// Generate random users
					generateUsers(num);
					
					out.println("success");
					
				}else if(split[2].equals("Availability")){
					
					// Generate user availability
					generateAvailability();
					
					out.println("success");
				}else if(split[2].equals("Gig")){
						
					generateGigs();
					
					out.println("success");
				}else{
					
					throw new Exception("No valid generate field");
				}
			}
			out.println("success");
		}catch(Exception e){
			
			e.printStackTrace();
			
			out.println("error - "+e.toString());
		}
		
	}
	

	private void processLoginRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		try{

			// Get the username and password
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			
			// Get the datastore
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			Collection<Filter> filterCompCollection = new ArrayList<Filter>();
			
			// Add the username/password predicates
			filterCompCollection.add(new FilterPredicate("username",FilterOperator.EQUAL,username));
			filterCompCollection.add(new FilterPredicate("password",FilterOperator.EQUAL,password));
			
			// Combine in one filter
			CompositeFilter filterComp = new CompositeFilter(CompositeFilterOperator.AND,filterCompCollection);
			
			// Create the query
			Query getUser = new Query("User").setFilter(filterComp);
			
			// PRepare the query
			PreparedQuery pqGetUser = datastore.prepare(getUser);
			
			// Try to get the entity
			Entity userEntity = pqGetUser.asSingleEntity();
			
			if(userEntity!=null){			
				
				out.print("success");
				
				System.out.println("Logged in as "+username+" with password: "+password);
				
			}else throw new Exception("Invalid username/password combination");
			
			
		}
		catch(Exception e){
			
			e.printStackTrace();
			
			out.println(e.getMessage());
		}
	}

	private void processRegistrationRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PrintWriter out = resp.getWriter();
		
		try{
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			List<Filter> cuaeFilters = new ArrayList<Filter>();
			
			// Check email and password
			cuaeFilters.add(new FilterPredicate("username",FilterOperator.EQUAL,req.getParameter("username")));
			cuaeFilters.add(new FilterPredicate("email",FilterOperator.EQUAL,req.getParameter("email")));
			
			CompositeFilter checkUsernameAndEmail = new CompositeFilter(CompositeFilterOperator.OR,cuaeFilters);
			
			Query cuaeQuery = new Query("User").setFilter(checkUsernameAndEmail);
			
			int count = datastore.prepare(cuaeQuery).countEntities(FetchOptions.Builder.withDefaults());
			
			// Throw and exception
			if(count>0)throw new Exception("That username and/or email is already taken!");
			
			Entity newUser = new Entity("User",req.getParameter("username"));
			
			// Set the other user entity properties
			newUser.setProperty("username", req.getParameter("username"));
			newUser.setProperty("type", req.getParameter("venue"));
			newUser.setProperty("email", req.getParameter("email"));
			newUser.setProperty("genre", req.getParameter("genre"));
			newUser.setProperty("password", req.getParameter("password"));
			newUser.setProperty("hasHospitalityPack", req.getParameter("hasHospitalityPack")=="true");
			newUser.setProperty("hasPA", req.getParameter("hasPA")=="true");
			newUser.setProperty("hasSoundPerson", req.getParameter("hasSoundPerson")=="true");
			newUser.setProperty("onlyOriginalMusic", req.getParameter("onlyOriginalMusic")=="true");
			newUser.setProperty("firstName", req.getParameter("firstName"));
			newUser.setProperty("lastName", req.getParameter("lastName"));
			newUser.setProperty("priceRange", Double.parseDouble(req.getParameter("priceRange")));
			newUser.setProperty("capacity", Integer.parseInt(req.getParameter("capacity")));
			newUser.setProperty("openHours", req.getParameter("openHours"));
			newUser.setProperty("gigCount", Integer.parseInt(req.getParameter("gigCount")));
			newUser.setProperty("dateJoined", new Date());
			
			// Add the new user to the datastore
			datastore.put(newUser);
			
			out.print("success");
		}catch(Exception e){
			
			e.printStackTrace();
			out.print(e.getMessage());
		}
		
		out.close();
	}
	
	private void processProfileRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException{
		
		try{
			
			Entity newUser = datastore.get(KeyFactory.createKey("User", req.getParameter("username")));
			
			// Set the other user entity properties
			newUser.setProperty("hasHospitalityPack", req.getParameter("hasHospitalityPack")=="true");
			newUser.setProperty("hasPA", req.getParameter("hasPA")=="true");
			newUser.setProperty("hasSoundPerson", req.getParameter("hasSoundPerson")=="true");
			newUser.setProperty("onlyOriginalMusic", req.getParameter("onlyOriginalMusic")=="true");
			newUser.setProperty("openHours", req.getParameter("openHours"));
		
			// Add the new user to the datastore
			datastore.put(newUser);
		
		}catch(Exception e){
			
			
		}
	}
	
	
	private void processAvailabilityRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String user = req.getParameter("user");
		
		try {
			
			Date date = new Date(Integer.parseInt(req.getParameter("year")),
								 Integer.parseInt(req.getParameter("month")),
								 Integer.parseInt(req.getParameter("date")));
			
			System.out.println("PRofile request on: "+date);
						 
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			Entity dateEntity = null;
			
			try{
				
				dateEntity = datastore.get(KeyFactory.createKey("Availability", user+date.toString()));
			}catch(Exception e){
				dateEntity = null;
			}
			
			
			
			// If we dont have an entity that matches
			if(dateEntity ==null){
				
				System.out.println("Date Entity not found");
				
				// Create an entitiy for availabiility
				dateEntity = new Entity("Availability",user+date.toString());
				
				// SEt the properties
				dateEntity.setProperty("dateAvailable",date);
				dateEntity.setProperty("bandName", user);
				
				// Add to the datastore
				datastore.put(dateEntity);
			}else{
				
				System.out.println("Date Entity found");
				
				// Delete for mthe data store
				datastore.delete(dateEntity.getKey());
			}
			
			// Print success
			out.print("success");
			
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			out.print(e.getMessage());
		}
		
		out.close();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Do the same thing as in post
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String extras = req.getPathInfo().substring(1);
		
		String[] split = extras.split("/");		
		
		// Whats the servlet action
		String action=split[0];		
		
		this.datastore = DatastoreServiceFactory.getDatastoreService();
		this.out = resp.getWriter();
		this.action = action;
		this.split = split;
		
		// For each in the split
		for(int i=0;i<split.length;i++)
		{
			// Output the contents of the split
			System.out.println((i+1)+". "+split[i]);
		}
		
		// Process accordingly
		if(action.equals("admin"))processAdminRequest(req, resp);
		else if(action.equals("login"))processLoginRequest(req, resp);
		else if(action.equals("register"))processRegistrationRequest(req, resp);
		else if(action.equals("profile"))processProfileRequest(req, resp);
		else if(action.equals("availability"))processAvailabilityRequest(req, resp);
		else if(action.equals("gig"))processGigTransactionRequest(req, resp);
		
		out.close();
	}

}
