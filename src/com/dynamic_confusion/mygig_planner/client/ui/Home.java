package com.dynamic_confusion.mygig_planner.client.ui;

import com.dynamic_confusion.mygig_planner.client.ChangeLoginStateEvent;
import com.dynamic_confusion.mygig_planner.client.TabUpdateEvent;
import com.dynamic_confusion.mygig_planner.client.TabUpdateEvent.Handler;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideServiceClientImpl;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class Home extends SimplePanel{
	
	public ServerSideServiceClientImpl ssService = null;
	
	public Calendar calendar = null;
	public LoginRegister loginRegister = null;
	
	public Widget content =null;
	private TabPanel tpParent= null;

	public Home(ServerSideServiceClientImpl ssService, TabPanel tpParent) {
		// TODO Auto-generated constructor stub
		
		this.ssService = ssService;
		this.tpParent = tpParent;
		

		this.addHandler(new TabUpdateEvent.Handler(){

			@Override
			public void onUpdate(TabUpdateEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println("We are updating the home page");
				
				// Tell things to update
				update();
			}
			
		},TabUpdateEvent.TYPE);
		
		// Auto create both
		calendar = new Calendar(ssService);
		loginRegister = new LoginRegister(ssService,this);
		
		update();
	}
	
	private void update(){
		

		// What is our content
		content = Cookies.getCookie("activeUser")!=null? calendar: loginRegister;
		
		// Clear us
		this.clear();
		
		// Add the content
		this.add(content);
		
		// CReate the event to fire
		GwtEvent<TabUpdateEvent.Handler> g = new GwtEvent<TabUpdateEvent.Handler>(){

			@Override
			public com.google.gwt.event.shared.GwtEvent.Type<TabUpdateEvent.Handler> getAssociatedType() {
				// TODO Auto-generated method stub
				return TabUpdateEvent.GetType();
			}

			@Override
			protected void dispatch(TabUpdateEvent.Handler handler) {
				// TODO Auto-generated method stub
				handler.onUpdate(null);
			}
			
		};
		
		content.fireEvent(g);
	}
	
	public void updateLoginState(){
		
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
		
		tpParent.fireEvent(clsEvent);
	}

}
