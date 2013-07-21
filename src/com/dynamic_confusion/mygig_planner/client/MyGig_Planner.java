package com.dynamic_confusion.mygig_planner.client;

import java.util.HashMap;




import com.dynamic_confusion.mygig_planner.client.gigs.GigTransactionServiceClientImpl;
import com.dynamic_confusion.mygig_planner.client.users.UserServiceClientImpl;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MyGig_Planner implements EntryPoint {
	
	public static String error = "No Error";

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		// Add a header
		RootPanel.get().add(new HTML("<h1>MyGig Planner</h1>"));
		
		TabPanel tp = new TabPanel();

		// Set the width
		tp.setWidth("800px");
		
		Grid filesGrid = new Grid(6, 1);
		
		filesGrid.setHTML(0, 0, "Project Management Plan");
		filesGrid.setHTML(1, 0, "Software Requirements Specification");
		filesGrid.setHTML(2, 0, "High Level Design");
		filesGrid.setHTML(3, 0, "Low Level Design");
		
		final AbsolutePanel home = new AbsolutePanel();
		final AbsolutePanel loginPanel = new AbsolutePanel();
		
		final FormPanel form = new FormPanel();
		final FormPanel loginForm = new FormPanel();
		
		loginForm.setWidget(loginPanel);
		loginForm.setEncoding(FormPanel.ENCODING_URLENCODED);
		loginForm.setMethod(FormPanel.METHOD_GET);
		loginForm.setAction("LoginServlet");
		
		form.setWidget(home);
		form.setEncoding(FormPanel.ENCODING_URLENCODED);
		form.setMethod(FormPanel.METHOD_GET);
		form.setAction("RegisterServlet");
		
		// Add some default panels
		tp.add(home,"Home");
		
		// If we have an active user
		if(Cookies.getCookie("activeUser")!=null){
			
			Button logout = null;
			
			RootPanel.get().add(logout = new Button("Logout"));
			
			logout.addClickHandler(new ClickHandler(){
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					
					Cookies.removeCookie("activeUser");
					
					// Reload the page
					Window.Location.reload();
					
				}
			});
		
			tp.add(new HTML("tab 1"),"Logbook");
			tp.add(new HTML("tab 1"),"Calender");
			tp.add(new HTML("tab 1"),"Search/Browse");
		
		}else tp.add(loginPanel,"Login");
		
		tp.add(new HTML("tab 3"),"Help");
		tp.add(filesGrid,"Project Files & Information");
		
		final TextBox tbUsername = new TextBox();
		final TextBox tbPassword = new TextBox();
		final ListBox lbType = new ListBox();
		
		// Add the two possible types
		lbType.addItem("Venue");
		lbType.addItem("Musician");
		
		// Set the name for the form		
		lbType.setName("type");
		tbPassword.setName("username");
		tbUsername.setName("password");
		
		// A button for registering
		final Button registerButton = new Button("Register");
		final Button loginButton = new Button("Login");
		
		final TextBox tbLoginUsername = new TextBox();
		final TextBox tbLoginPassword = new TextBox();
		
		// Set the name for the form	
		tbLoginPassword.setName("username");
		tbLoginUsername.setName("password");
		

		registerButton.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				
				registerButton.setText("Proccessing...");
				registerButton.setEnabled(false);
				
				UserServiceClientImpl registerServ = new UserServiceClientImpl(GWT.getModuleBaseURL()+"users");
				
				HashMap<String,Object> userInfo = new HashMap<String,Object>();
				
				userInfo.put("username", tbUsername.getText());
				userInfo.put("password", tbPassword.getText());
				userInfo.put("type", lbType.getItemText(lbType.getSelectedIndex()));
				
				// Attempt to login
				registerServ.attemptRegister(userInfo, new CustomCallback(){
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						super.onFailure(caught);
						
						registerButton.setText("Register");
						registerButton.setEnabled(true);
					
						// Show the message
						RootPanel.get().add(new HTML(caught.toString()));
					}
					
					@Override
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
						super.onSuccess(result);
						
						if((Boolean)result==true){
						
							RootPanel.get().add(new HTML("Registration Successful!"));
							
							registerButton.setText("Success");
							//registerButton.setEnabled(true);
						}else{
							
							registerButton.setText("Register");
							registerButton.setEnabled(true);
							
							RootPanel.get().add(new HTML("That username and/or email is already in use."));
						}
					}
				});
				
			}
		});
		
			
		loginButton.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				
				loginButton.setText("Processing...");
				loginButton.setEnabled(false);
				
				UserServiceClientImpl loginServ = new UserServiceClientImpl(GWT.getModuleBaseURL()+"users");				
				
				// Attempt to login
				loginServ.attemptLogin(tbLoginUsername.getText(), tbLoginPassword.getText(), new CustomCallback(){
					
					@Override
					public void onFailure(Throwable caught) {
						
						// TODO Auto-generated method stub
						super.onFailure(caught);						

						// Show the message
						RootPanel.get().add(new HTML(caught.toString()));
						
						loginButton.setText("Login");
						loginButton.setEnabled(true);
					
					}
					
					@Override
					public void onSuccess(Object result) {
						
						// TODO Auto-generated method stub
						super.onSuccess(result);						

						if((Boolean)result==true){
							
							// Set the active user cookie
							Cookies.setCookie("activeUser", tbLoginUsername.getText());

							// Reload the page
							Window.Location.reload();
						}else{
							
							loginButton.setText("Login");
							loginButton.setEnabled(true);
							
							RootPanel.get().add(new HTML("[ "+result+" ] That username/password combination is invalid."));
						}
					
					}
				});
			}
		});
		

		loginPanel.add(new Label("Username:"));		
		loginPanel.add(tbLoginUsername);
		
		loginPanel.add(new Label("Password:"));
		loginPanel.add(tbLoginPassword);

		
		loginPanel.add(loginButton);
		

		
		// If we have an active user
		if(Cookies.getCookie("activeUser")!=null){
			
			UserServiceClientImpl getUsers = new UserServiceClientImpl(GWT.getModuleBaseURL()+"users");

			
			getUsers.getUsers(new AsyncCallback(){
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
					RootPanel.get().add(new HTML("<p>"+caught.getMessage()+"</p>"));
				}
				
				public void onSuccess(Object result) {
					
					final String[] users = (String[])result;
					

					
					home.add(new HTML("<h3>Users</h3>"));
					
					for(int i=0;i<users.length;i++){
						
						final String currentUserGridUser = users[i];
						
						Anchor sendOfferLink = new Anchor(currentUserGridUser +" - Send an Offer");
						
						home.add(sendOfferLink);
						
						sendOfferLink.addClickHandler(new ClickHandler(){
							
							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								GigTransaction newGig = new GigTransaction();
								
								newGig.setSendUser( Cookies.getCookie("activeUser"));
								newGig.setRecipientUser(currentUserGridUser);
								
								GigTransactionServiceClientImpl sendOffer = new GigTransactionServiceClientImpl(GWT.getModuleBaseURL()+"gigs");
								
								sendOffer.sendOffer(newGig, new AsyncCallback(){
									
									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub

										RootPanel.get().add(new HTML("<p>"+caught.getMessage()+"</p>"));
									}
									
									@Override
									public void onSuccess(Object result) {
										// TODO Auto-generated method stub

										if(((String)result).trim().equals("success"))RootPanel.get().add(new HTML("<p>Gig offer sent!</p>"));
										else RootPanel.get().add(new HTML("<p>Gig offer failed to send!</p><p>"+(String)result+"</p>"));
									}
									
								});
								
								
							}
						});
						
						//<a href=\""+GWT.getModuleBaseURL()+"sendOffer\">Send an Offer</a>");
						
					}
				};
			});
			
			final GigTransactionServiceClientImpl getOffers = new GigTransactionServiceClientImpl(GWT.getModuleBaseURL()+"gigs");
			
			getOffers.getOffers(new AsyncCallback(){
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

					RootPanel.get().add(new HTML("<p>"+caught.getMessage()+"</p>"));
				}
				
				@Override
				public void onSuccess(Object result) {
					
					if(result == null){
						
						getOffers.getErrorMessage(new AsyncCallback(){
							
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								home.add(new HTML("<p>Error getting gig offers!</p><br><p>"+caught.getMessage()+"</p>"));
								
							}
							
							@Override
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								home.add(new HTML("<p>Error getting gig offers!</p><br><p>"+result+"</p>"));
							}
						});
						
						
					}else{
					
						// TODO Auto-generated method stub
						GigTransaction[] gigs = (GigTransaction[])result;
						
						Grid gigGrid = new Grid(gigs.length,1);
						
						for(int i=0;i<gigs.length;i++){
							
							
							if(gigs[i].getStatus()==0&&
							   gigs[i].getRecipientUser().equals(Cookies.getCookie("activeUser"))){
								home.add(new HTML("<p>Offer by "+gigs[i].getSendUser()+"</p>"));
								
								
							}else if(gigs[i].getStatus()==-1&&
							   gigs[i].getRecipientUser().equals(Cookies.getCookie("activeUser"))){
								home.add(new HTML("<p>You rejected "+gigs[i].getSendUser()+"'s Offer</p>"));
							}else if(gigs[i].getStatus()==1&&
							   gigs[i].getRecipientUser().equals(Cookies.getCookie("activeUser"))){
								home.add(new HTML("<p>You accepted "+gigs[i].getSendUser()+"'s Offer</p>"));
							}else if(gigs[i].getStatus()==-1&&
							   gigs[i].getSendUser().equals(Cookies.getCookie("activeUser"))){
								home.add(new HTML("<p>"+gigs[i].getRecipientUser()+"rejected your offer</p>"));
							}else if(gigs[i].getStatus()==1&&
							   gigs[i].getSendUser().equals(Cookies.getCookie("activeUser"))){
								home.add(new HTML("<p>"+gigs[i].getRecipientUser()+" accepted your offer</p>"));
							}
						}
						
						
	
						home.add(new HTML("<h3>Gig Offers</h3>"));
						home.add(gigGrid);
					}
				}
				
			});
			
		}else{
		
			home.add(new Label("Username:"));		
			home.add(tbUsername);
			
			home.add(new Label("Password:"));
			home.add(tbPassword);
			
			home.add(new Label("Type:"));
			home.add(lbType);
			
			home.add(registerButton);
		}
		
		RootPanel.get().add(tp);
		DOM.setElementAttribute(tp.getElement(), "id","mainTabPanel");
	}
	

	public class CustomCallback implements AsyncCallback{
		
		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onSuccess(Object result) {
			// TODO Auto-generated method stub
			
		}
	}
}
