package com.dynamic_confusion.mygig_planner.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dynamic_confusion.mygig_planner.client.TabUpdateEvent.Handler;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideServiceClientImpl;
import com.dynamic_confusion.mygig_planner.client.ui.Calendar;
import com.dynamic_confusion.mygig_planner.client.ui.Logbook;
import com.dynamic_confusion.mygig_planner.client.ui.Login;
import com.dynamic_confusion.mygig_planner.client.ui.Register;
import com.dynamic_confusion.mygig_planner.client.ui.Search;
import com.dynamic_confusion.mygig_planner.client.ui.ViewEditProfile;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MyGig_Planner implements EntryPoint {


	final ServerSideServiceClientImpl ssService = new ServerSideServiceClientImpl();
	
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
		
		final AbsolutePanel home = new AbsolutePanel();
		final AbsolutePanel profilePanel = new AbsolutePanel();
		final AbsolutePanel search = new AbsolutePanel();
		final AbsolutePanel logbook = new AbsolutePanel();
		final AbsolutePanel logout = new AbsolutePanel();
		
		
		final FormPanel form = new FormPanel();
		final FormPanel profileForm = new FormPanel();
		
		profileForm.setWidget(profilePanel);
		profileForm.setEncoding(FormPanel.ENCODING_URLENCODED);
		profileForm.setMethod(FormPanel.METHOD_POST);
		profileForm.setAction("/server-side/profile");
		
		// If we have an active user
		if(Cookies.getCookie("activeUser")!=null){
			
			// Add some default panels
			tp.add(new Calendar(ssService),"Home");
			tp.add(logout, "Log Out");
			
			final Button logoutButton = new Button("Log Out");
			
			logout.add(logoutButton);
			
			logoutButton.addClickHandler(new ClickHandler(){
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					
					logoutButton.setText("Wait...");
					Cookies.removeCookie("activeUser");
					
					// Reload the page
					Window.Location.reload();
					
				}
			});
		
			tp.add(new Logbook(ssService),"Logbook");
			tp.add(new ViewEditProfile(ssService),"View/Edit Profile");
			tp.add(new Search(ssService),"Search/Browse");
		
		}//else tp.add(loginForm,"Login");
		
		tp.add(new HTML("tab 3"),"Help");
		
		final TextBox tbLoginUsername = new TextBox();
		final TextBox tbLoginPassword = new TextBox();
		
		// Set the name for the form	
		tbLoginPassword.setName("username");
		tbLoginUsername.setName("password");
		
		
		form.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// TODO Auto-generated method stub
				
				String registerResults = event.getResults().trim();
				
				// If it says success
				if(registerResults.equals("success")){
					
					// Set the cookie
					Cookies.setCookie("activeUser", tbLoginUsername.getText());	
					
					// Reload
					Window.Location.reload();

				}else{


					String errorMessage = registerResults;
					
					// TODO handle output of error message
					
					home.add(new HTML(errorMessage));
				}
			}
		});
					
		final Button searchButton = new Button("Search!");
		Button addDateButton = new Button("Add Date");
		
		final TextBox searchBox = new TextBox();
		final DatePicker datePicker = new DatePicker();		
		
		searchButton.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {

				
				searchButton.setText("Searching...");
				searchButton.setEnabled(false);
				
				SearchInfo info = new SearchInfo();
				
				// TODO populate other fields of the search info 
				
				
				// Call the search function
				ssService.search(info, new CustomCallback(){
					
					@Override
					public void onFailure(Throwable caught) {
						
						RootPanel.get().add(new HTML("<p>"+caught.getMessage()+"</p>"));
					}
					
					@Override
					public void onSuccess(Object result) {
						
						UserInfo[] users = (UserInfo[])result;
						
						searchButton.setText("Search");
						searchButton.setEnabled(true);
						
						RootPanel.get().add(new HTML("Done with "+users.length+" results!"));
						
						// TODO handle search results
						for(int i=0;i<users.length;i++){

							RootPanel.get().add(new HTML("<p>"+users[i].username+"</p>"));
						}
						
					}
					
				});
			}
		});
		
		search.add(datePicker);
		search.add(addDateButton);
		search.add(searchBox);
		search.add(searchButton);
				
		
		// If we have an active user
		if(Cookies.getCookie("activeUser")!=null){
			
			ssService.getOffers(new AsyncCallback(){
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

					logbook.add(new HTML("<p>"+caught.getMessage()+"</p>"));
				}
				
				@Override
				public void onSuccess(Object result) {
					
					if(result == null){
						
						ssService.getErrorMessage(new AsyncCallback(){
							
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								logbook.add(new HTML("<p>Error getting gig offers!</p><br><p>"+caught.getMessage()+"</p>"));
								
							}
							
							@Override
							public void onSuccess(Object result) {
								// TODO Auto-generated method stub
								logbook.add(new HTML("<p>Error getting gig offers!</p><br><p>"+result+"</p>"));
							}
						});
						
						
					}else{
					
						// TODO Auto-generated method stub
						GigInfo[] gigs = (GigInfo[])result;

						// Add the header
						logbook.add(new HTML("<h3>Gig Offers</h3>"));
						
						for(int i=0;i<gigs.length;i++){
							
							
							if(gigs[i].status==0&&
							    gigs[i].recipientUser.equals(Cookies.getCookie("activeUser"))){
								
								// Add a paragraph
								logbook.add(new HTML("<p>Offer by "+gigs[i].sendUser+"</p><br/>"+
										"<form action=\"/gig/accept\" method=\"post\">"
											+ "<input type=\"hidden\" name=\"action\" value=\"accept\">"
											+ "<input type=\"hidden\" name=\"recipientUser\" value=\""+Cookies.getCookie("activeUser")+"\">"
											+ "<input type=\"hidden\" name=\"key\" value=\""+gigs[i].key+"\">"
											+ "<input type=\"submit\" value=\"accept\">"
										+ "</form>"+
										"<form action=\"/gig/reject\" method=\"post\">"
											+ "<input type=\"hidden\" name=\"action\" value=\"reject\">"
											+ "<input type=\"hidden\" name=\"recipientUser\" value=\""+Cookies.getCookie("activeUser")+"\">"
											+ "<input type=\"hidden\" name=\"key\" value=\""+gigs[i].key+"\">"
											+ "<input type=\"submit\" value=\"reject\">"
										+ "</form>"));
								
								
							}else if(gigs[i].status==-1&&
							    gigs[i].recipientUser.equals(Cookies.getCookie("activeUser"))){
								
								// Alert the user what happened
								logbook.add(new HTML("<p>You rejected "+gigs[i].sendUser+"'s Offer on "+gigs[i].dateReplied+"</p>"));
							}else if(gigs[i].status==1&&
							    gigs[i].recipientUser.equals(Cookies.getCookie("activeUser"))){
								
								// Alert the user what happened
								logbook.add(new HTML("<p>You accepted "+gigs[i].sendUser+"'s Offer on "+gigs[i].dateReplied+"</p>"));
							}else if(gigs[i].status==-1&&
							    gigs[i].sendUser.equals(Cookies.getCookie("activeUser"))){
								
								// Alert the user what happened
								logbook.add(new HTML("<p>"+gigs[i].recipientUser+"rejected your offer on "+gigs[i].dateReplied+"</p>"));
							}else if(gigs[i].status==1&&
							    gigs[i].sendUser.equals(Cookies.getCookie("activeUser"))){
								
								// Alert the user what happened
								logbook.add(new HTML("<p>"+gigs[i].recipientUser+" accepted your offer on "+gigs[i].dateReplied+"</p>"));
							}
						}
					}
				}
				
			});
			
		
			
			String profileUser = Window.Location.getParameter("user") ==null ? Cookies.getCookie("activeUser") : Window.Location.getParameter("user");
			
			// If we have a bad profile user
			if (profileUser==null) {
				
				// Show we have nothing good
				profilePanel.add(new HTML("No valid user given to show!"));
			}
			else {
				
				// ARe we editing
				final boolean editing = (Window.Location.getParameter("user")==null||
										(Window.Location.getParameter("user")==Cookies.getCookie("activeUser")&&Cookies.getCookie("activeUser")!=null))
										&&Window.Location.getParameter("edit")!=null;
				Button profileButton = null;
				
				// If we dont have a user
				if(Window.Location.getParameter("user")==null){
					
					// If the edit parameter is set
					if(Window.Location.getParameter("edit")!=null){
						
						// Add the done editing parameter
						profilePanel.add(profileButton = new Button("Done Editing!"));
						
						// Lisen for when it is clicked
						profileButton.addClickHandler(new ClickHandler(){
							
							@Override
							public void onClick(ClickEvent event) {

								profileForm.addSubmitHandler(new SubmitHandler(){

									@Override
									public void onSubmit(
											SubmitEvent event) {
										// TODO Auto-generated method stub
										
										String wlNoParameters = Window.Location.getHref();
										int noParamIndex =wlNoParameters.indexOf("?");
										if(noParamIndex>=0)wlNoParameters = wlNoParameters.substring(0,noParamIndex);
										
										String codesvr = "";
										if(Window.Location.getParameter("gwt.codesvr")!=null)
											codesvr="&gwt.codesvr="+Window.Location.getParameter("gwt.codesvr");
										
										Window.Location.replace(wlNoParameters+"?tab=5"+codesvr);										
									}
									
								});
								
								profileForm.submit();
							}
						});
						
						
					}
					else {
						
						// Add an edit button
						profilePanel.add(profileButton = new Button("Edit Profile"));
						
						// Listen for when it is clicked
						profileButton.addClickHandler(new ClickHandler(){
							
							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								
								String wlNoParameters = Window.Location.getHref();
								int noParamIndex =wlNoParameters.indexOf("?");
								if(noParamIndex>=0)wlNoParameters = wlNoParameters.substring(0,noParamIndex);
								
								String codesvr = "";
								if(Window.Location.getParameter("gwt.codesvr")!=null)
									codesvr="&gwt.codesvr="+Window.Location.getParameter("gwt.codesvr");
								
								Window.Location.replace(wlNoParameters+"?tab=5&edit=true"+codesvr);
							}
						});
					}
					
				}
	
				
				// Get the info for the profile user
				ssService.getUser(profileUser, new AsyncCallback(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
						profilePanel.add(new HTML(caught.getMessage()));
					}

					@Override
					public void onSuccess(Object result) {
						// TODO Auto-generated method stub
							
						UserInfo userObj = (UserInfo)result;
						
						// Show we have nothing good
						if(userObj==null)profilePanel.add(new HTML("No valid user given to show!"));
						else{
						
						String[] fields = UserInfo.getFields();
						
						for(int i=0;i<fields.length;i++){
							
							Label lbl = new Label(fields[i]+": ");
							Widget tb = editing ? new TextBox() : new Label(userObj.getField(fields[i]).toString());
							
							// Set the name if we are editing
							if(editing)((TextBox)tb).setName(fields[i]);
							
							// Add the elements
							profilePanel.add(lbl);
							profilePanel.add(tb);
							
							// ADd a line break
							profilePanel.add(new HTML("<br/>"));
						}
						
						}
					}
					
					
				});
				
			}
			
		}
		else {
			
			HorizontalPanel horizontalPanel = new HorizontalPanel();
			tp.add(horizontalPanel, "Home");
			
			// Adding the login section
			// login button is implemented in Login class
			Login loginSection = new Login();
			horizontalPanel.add(loginSection);
			
			// Adding space between login and register section
			VerticalPanel spacing = new VerticalPanel();
			spacing.setWidth("200px");
			horizontalPanel.add(spacing);
			
			// Adding the register section
			// register button is implemented in Register class
			Register registerSection = new Register();
			horizontalPanel.add(registerSection);	
		}
		
		// When a tab is selected
		tp.addSelectionHandler(new SelectionHandler<Integer>(){

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
			
				
				// Get its associated widget
				Widget w = ((TabPanel)event.getSource()).getWidget(event.getSelectedItem());
				
				// Fire a tabupdate event
				w.fireEvent(new GwtEvent<TabUpdateEvent.Handler>(){

					@Override
					public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
						// TODO Auto-generated method stub
						return TabUpdateEvent.GetType();
					}

					@Override
					protected void dispatch(Handler handler) {
						// TODO Auto-generated method stub
						handler.onUpdate(null);
					}
					
				});
			}
			
		});
		
		// Add to the proper container
		RootPanel.get("mgpContent").add(tp);
		
		// Populate the service lists
		SiteDesign.PopulateUsersLists(ssService);
		
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
