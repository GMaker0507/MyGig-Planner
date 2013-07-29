package com.dynamic_confusion.mygig_planner.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dynamic_confusion.mygig_planner.client.TabUpdateEvent.Handler;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideServiceClientImpl;
import com.dynamic_confusion.mygig_planner.client.ui.Calendar;
import com.dynamic_confusion.mygig_planner.client.ui.Home;
import com.dynamic_confusion.mygig_planner.client.ui.Logbook;
import com.dynamic_confusion.mygig_planner.client.ui.Login;
import com.dynamic_confusion.mygig_planner.client.ui.LoginRegister;
import com.dynamic_confusion.mygig_planner.client.ui.Logout;
import com.dynamic_confusion.mygig_planner.client.ui.ProjectFilesAndInformation;
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
	
	public Logout logout;
	public Home home;
	public Search search;
	public Logbook logbook;
	public ProjectFilesAndInformation projectFilesAndInformation;
	public ViewEditProfile viewEditProfile;
	
	private TabPanel tp = null;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		// Add a header
		RootPanel.get().add(new HTML("<h1>MyGig Planner</h1>"));
		
		tp = new TabPanel();
		tp.getElement().setId("mgpTabPanel");

		// Set the width
		tp.setWidth("800px");	
		
		// Create our states
		home = new Home(ssService,tp);
		logbook = new Logbook(ssService);
		search = new Search(ssService);
		logout = new Logout(tp);
		viewEditProfile = new ViewEditProfile(ssService);
		projectFilesAndInformation = new ProjectFilesAndInformation();
			
		
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

		// Listen for when we change login states
		tp.addHandler(new ChangeLoginStateEvent.Handler(){

			@Override
			public void onChangeLoginState(TabUpdateEvent event) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				
				System.out.println("We are updating the main module");
				
				// Tell things to update
				update();
				
			}
			
		},ChangeLoginStateEvent.TYPE);
		
		update();
		
		// Add to the proper container
		RootPanel.get("mgpContent").add(tp);
		
		// Populate the service lists
		SiteDesign.PopulateUsersLists(ssService);
		
		DOM.setElementAttribute(tp.getElement(), "id","mainTabPanel");
	}
	
	private void update(){
		
		// Clear all panels
		tp.clear();
		
		// Add some default panels
		tp.add(home,"Home");
		
		// If we have an active user
		if(Cookies.getCookie("activeUser")!=null){
			
			// Add the appropriate states
			tp.add(logout, "Log Out");	
			tp.add(logbook,"Logbook");
			tp.add(viewEditProfile,"View/Edit Profile");
			tp.add(search,"Search/Browse");
		
		}
		
		tp.add(new HTML("tab 3"),"Help");	
		tp.add(projectFilesAndInformation,"Project Files & Information");	
	}
}
