package com.dynamic_confusion.mygig_planner.server.users;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class AdminServlet extends HttpServlet {
	
	
	private void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String extras = req.getPathInfo().substring(1);
		
		String[] split = extras.split("/");
		
		String action=split[0];
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		PrintWriter out = resp.getWriter();
		
		for(int i=0;i<split.length;i++)
		{
			out.println((i+1)+". "+split[i]);
		}
		
		try{
		
			if(action.equals("clear")){
				
				Query users = new Query(split[1]);
				
				PreparedQuery pUsers = datastore.prepare(users);
				
				// Get the user entities
				List<Entity> usersList = pUsers.asList(FetchOptions.Builder.withDefaults());
				
				while(usersList.size()>0){
					
					// Delete this user
					datastore.delete(usersList.get(0).getKey());
					
					// Remove from the list
					usersList.remove(0);
				}
				
				
				out.println("success");
				
			}else if(action.equals("generate")){
				
				if(split[1].equals("User")){
					
					int num = Integer.parseInt(split[2]);
					
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
						
						out.println("Creating account ["+newUser.username+"] for "+newUser.firstName+" "+newUser.lastName+"");
						
						// Add to the datastore
						datastore.put(newEntity);
					}
					
				}else if(split[1].equals("Availability")){
					
					Query allUsers = new Query("User");
					
					PreparedQuery pqAllUsers = datastore.prepare(allUsers);
					
					List<Entity> entityAllUsers = pqAllUsers.asList(FetchOptions.Builder.withDefaults());
					
					for(int h=0;h<entityAllUsers.size();h++){
	
						Date today = new Date();
						int year = today.getYear();
						int month = today.getMonth();
						int months = (int)Math.floor(Math.random()*3.0);
						
						for(int j=0;j<months;j++){
							
							int[] days = new int[]{
								1,2,3,4,5,6,7,8,9,10,
								11,1,2,13,14,15,16,17,18,19,20,
								21,22,23,24,25,26,27,28,29,30,31
							};
							
							int numDays = (int)Math.floor(Math.random()*31.0);
							
							// For each day
							for(int k=0;k<numDays;k++){
								
								int ni = (int)Math.floor(Math.random()*31.0);
								
								// Dont use it if its negative one
								if(days[ni]==-1)continue;
								
								// Whats the negative one
								int date = days[ni];
								
								days[ni] = -1;
								
								Entity newAvailability = new Entity("Availability");
								
								Date dt = null;
								
								// Set the bandis availablie on this date
								newAvailability.setProperty("dateAvailable",dt = new Date(year,month+j,date));
								newAvailability.setProperty("bandName", entityAllUsers.get(k).getProperty("username"));
								
								// Add to the datastore
								datastore.put(newAvailability);
								
								out.println(entityAllUsers.get(k).getProperty("username")+" is available on "+dt.toString());
							
							}
						}	
					}
						
				}else{
					
					throw new Exception();
				}
			}
			out.println("success");
		}catch(Exception e){
			
			out.println("Split 1 is "+split[1]);
			if(split.length>2)out.println("Split 2 is "+split[2]);
			out.println("error");
		}
		
		
		
		out.close();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(req, resp);
	}
}
