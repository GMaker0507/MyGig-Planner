package com.dynamic_confusion.mygig_planner.server.gigs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;

public class GigTransactionServlet extends HttpServlet {

	private void processRequest(HttpServletRequest req, HttpServletResponse resp)
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
					if(action.equals("accept"))getEntity.setProperty("status",1);
					else if(action.equals("reject"))getEntity.setProperty("status",-1);
					
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
