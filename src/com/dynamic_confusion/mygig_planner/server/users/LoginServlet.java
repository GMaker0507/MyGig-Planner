package com.dynamic_confusion.mygig_planner.server.users;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

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
import com.google.gwt.user.client.Cookies;

public class LoginServlet extends HttpServlet {

	private void processRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		
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
			
			if(userEntity==null){			
				
				out.print("success");
				
			}else throw new Exception("Invalid username/password combination");
			
			
		}catch(Exception e){
			
			out.println(e.getMessage());
			
			
		}finally{
			
			out.close();
		}
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
