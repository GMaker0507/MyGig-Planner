package com.dynamic_confusion.mygig_planner.client;

import com.dynamic_confusion.mygig_planner.client.TabUpdateEvent.Handler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class TabUpdateEvent extends GwtEvent {
	
	public interface Handler extends EventHandler{
	
	public void onUpdate(TabUpdateEvent event);
	
	}
	

	
	public static Type<Handler> TYPE = new Type<Handler>();

	@Override
	public Type getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	protected void dispatch(Object handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void dispatch(EventHandler handler) {
		// TODO Auto-generated method stub
		
	}

	public static com.google.gwt.event.shared.GwtEvent.Type<Handler> GetType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

}
