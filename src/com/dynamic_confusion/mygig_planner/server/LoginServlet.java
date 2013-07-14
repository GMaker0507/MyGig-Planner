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
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Grid;

public class LoginServlet extends HttpServlet {
	
	
	private void process(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			
		String username = req.getParameter("username"), password = req.getParameter("password");
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query usersQuery = new Query("User");

		usersQuery.setFilter(new FilterPredicate("username", FilterOperator.EQUAL, username));
		usersQuery.setFilter(new FilterPredicate("password", FilterOperator.EQUAL, password));
		
		PreparedQuery pq = datastore.prepare(usersQuery);
		
		List<Entity> le = pq.asList(FetchOptions.Builder.withDefaults());
		
		resp.getWriter().print(le.size()==0?"true":"false");
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
