package com.dynamic_confusion.mygig_planner.server.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class ProfileServlet extends HttpServlet {
	private void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PrintWriter out = resp.getWriter();
		
		String user = req.getParameter("user");
		
		try {
			
			Date date = new Date(Integer.parseInt(req.getParameter("year")),
								 Integer.parseInt(req.getParameter("month")),
								 Integer.parseInt(req.getParameter("date")));
						 
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			Collection<Filter> filter = new ArrayList<Filter>();
			
			// We want the date and username to match
			filter.add(new FilterPredicate("dateAvailable",FilterOperator.EQUAL,date));
			filter.add(new FilterPredicate("bandName",FilterOperator.EQUAL,user));
			
			// A query to get availabilities with the set iflters
			Query getDate = new Query("Availability").setFilter(new CompositeFilter(CompositeFilterOperator.AND,filter));
			
			PreparedQuery pqGetDate = datastore.prepare(getDate);
			
			Entity dateEntity = null;
			
			// If we dont have an entity that matches
			if((dateEntity = pqGetDate.asSingleEntity())==null){
				
				// Create an entitiy for availabiility
				dateEntity = new Entity("Availability");
				
				// SEt the properties
				dateEntity.setProperty("dateAvailable",date);
				dateEntity.setProperty("bandName", user);
				
				// Add to the datastore
				datastore.put(dateEntity);
			}else{
				
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
