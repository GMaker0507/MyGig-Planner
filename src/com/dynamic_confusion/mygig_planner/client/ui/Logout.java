package com.dynamic_confusion.mygig_planner.client.ui;

import com.dynamic_confusion.mygig_planner.client.ChangeLoginStateEvent;
import com.dynamic_confusion.mygig_planner.client.TabUpdateEvent;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;

public class Logout extends AbsolutePanel {

	public TabPanel tpParent =null;
	public Button logoutButton = null;
	
	public Logout(TabPanel tpParent){
		
		this.tpParent = tpParent;
		
		logoutButton = new Button("Log Out");
		this.add(logoutButton);

		final Button logoutButtonFinal = logoutButton;		
		final TabPanel tpFinal = tpParent;
		
		logoutButton.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				logoutButtonFinal.setText("Wait...");
				logoutButtonFinal.setEnabled(false);

				Cookies.setCookie("activeUser", null);
				Cookies.removeCookie("activeUser");
				
				// CReate the event to fire
				GwtEvent<ChangeLoginStateEvent.Handler> clsEvent = new GwtEvent<ChangeLoginStateEvent.Handler>(){

					@Override
					public com.google.gwt.event.shared.GwtEvent.Type<ChangeLoginStateEvent.Handler> getAssociatedType() {
						// TODO Auto-generated method stub
						return ChangeLoginStateEvent.GetType();
					}

					@Override
					protected void dispatch(ChangeLoginStateEvent.Handler handler) {
						// TODO Auto-generated method stub
						handler.onChangeLoginState(null);
					}
					
				};
				
				// Update the login state of the tabs
				tpFinal.fireEvent(clsEvent);

				// Select the first tab
				// Do fire events
				tpFinal.selectTab(0, true);
				
			}
		});

		this.addHandler(new TabUpdateEvent.Handler(){

			@Override
			public void onUpdate(TabUpdateEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println("We are updating the logout");
				
				// Tell things to update
				update();
			}
			
		},TabUpdateEvent.TYPE);
		
	}
	
	public void update(){
		
		logoutButton.setText("Logout");
		logoutButton.setEnabled(true);
		
	}
}
