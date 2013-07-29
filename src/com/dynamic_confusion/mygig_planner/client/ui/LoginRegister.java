package com.dynamic_confusion.mygig_planner.client.ui;

import com.dynamic_confusion.mygig_planner.client.TabUpdateEvent;
import com.dynamic_confusion.mygig_planner.client.TabUpdateEvent.Handler;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideServiceClientImpl;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginRegister extends HorizontalPanel {
	
	private ServerSideServiceClientImpl ssService= null;
	
	private Login loginSection = null;
	private Register registerSection = null;
	private Home homeParent = null;
	
	public LoginRegister(ServerSideServiceClientImpl ssService, Home home)
	{
		this.ssService = ssService;
		this.homeParent =home;
		
		// Adding the login section
		// login button is implemented in Login class
		loginSection = new Login(home);
		this.add(loginSection);
		
		// Adding space between login and register section
		VerticalPanel spacing = new VerticalPanel();
		spacing.setWidth("200px");
		this.add(spacing);
		
		// Adding the register section
		// register button is implemented in Register class
		registerSection = new Register(home);
		this.add(registerSection);	
		
		this.addHandler(new TabUpdateEvent.Handler(){

			@Override
			public void onUpdate(TabUpdateEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println("We are updating the login register");
				
				// Tell things to update
				update();
			}
			
		},TabUpdateEvent.TYPE);
		
	}
	
	public void update(){
		 
		// CReate the event to fire
		GwtEvent<TabUpdateEvent.Handler> g = new GwtEvent<TabUpdateEvent.Handler>(){

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
			
		};
		
		// Fire a tabupdate event in the login/register
		loginSection.fireEvent(g);
		registerSection.fireEvent(g);
	}
	
	public void updateLoginState(){
		
		homeParent.updateLoginState();
	}
}
