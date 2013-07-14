package com.dynamic_confusion.mygig_planner.server;

import java.io.IOException;
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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;

public class RegisterServlet extends HttpServlet {
	
	
	private void process(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	

		
		String username = req.getParameter("username"), password = req.getParameter("password"),
			   type = req.getParameter("type");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Create an entity for the user
		Entity newUser = new Entity("User", username);

		// Set the username for the user
		newUser.setProperty("username", username);
		
		// Set the pasword for the user
		newUser.setProperty("password", password);
		
		// Set the type for the user
		newUser.setProperty("type", type);
		
		// Put the new user in the datastore
		datastore.put(newUser);

		
		Query usersQuery = new Query("User");
		
		PreparedQuery pq=datastore.prepare(usersQuery);
		
		List<Entity> le = pq.asList(FetchOptions.Builder.withDefaults());
		
		for(int i=0;i<le.size();i++){

			if(i!=0)resp.getWriter().print("##");
			resp.getWriter().print(le.get(i).getProperty("username"));
		}
		
		resp.getWriter().print("-array-");

		for(int i=0;i<le.size();i++){

			if(i!=0)resp.getWriter().print("##");
			resp.getWriter().print(le.get(i).getProperty("type"));
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		process(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		

		process(req, resp);
	}
}
