package com.dynamic_confusion.mygig_planner.server.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class RegistrationServlet extends HttpServlet {

	private void processRequest(HttpServletRequest req, HttpServletResponse resp)
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
			
			// Add the new user to the datastore
			datastore.put(newUser);
			
			out.print("success");
		}catch(Exception e){
			
			out.print(e.getMessage());
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
