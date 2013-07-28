package com.dynamic_confusion.mygig_planner.client;

import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideServiceClientImpl;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;

public class SiteDesign {

	public static void PopulateUsersLists(ServerSideServiceClientImpl ssService){
		
		ssService.getTopMusicians(7, new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

				
				for(int i=0;i<7;i++){
					
					RootPanel.get("topMusician"+i).getElement().setInnerText("Error");
				}
				
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(Object result) {
				// TODO Auto-generated method stub
				UserInfo[] topMusicians = (UserInfo[])result;
				
				// For each user
				for(int i=0;i<topMusicians.length;i++){
					
					Anchor anchor = new Anchor(topMusicians[i].username);
					
					String urlBase = "MyGig_Planner.html?";
					
					// If we have a code server
					if(Window.Location.getParameter("gwt.codesvr")!=null){
						
						// Append it to the url base
						urlBase += "gwt.codesvr="+Window.Location.getParameter("gwt.codesvr")+"&";
					}
					
					anchor.setHref(urlBase+"tab=3&user="+topMusicians[i].username);

					// Change the associated elements inner text
					RootPanel.get("topMusician"+i).getElement().setInnerText("");
					RootPanel.get("topMusician"+i).getElement().appendChild(anchor.getElement());
				}
			}
			
		});
		
		ssService.getTopVenues(7, new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

				
				for(int i=0;i<7;i++){
					
					RootPanel.get("topPromoter"+i).getElement().setInnerText("Error");
				}
				
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(Object result) {
				// TODO Auto-generated method stub
				UserInfo[] topPromoters = (UserInfo[])result;
				
				// For each user
				for(int i=0;i<topPromoters.length;i++){

					Anchor anchor = new Anchor(topPromoters[i].username);
					
					String urlBase = "MyGig_Planner.html?";
					
					// If we have a code server
					if(Window.Location.getParameter("gwt.codesvr")!=null){
						
						// Append it to the url base
						urlBase += "gwt.codesvr="+Window.Location.getParameter("gwt.codesvr")+"&";
					}
					
					anchor.setHref(urlBase+"tab=3&user="+topPromoters[i].username);

					// Change the associated elements inner text
					RootPanel.get("topPromoter"+i).getElement().setInnerText("");
					RootPanel.get("topPromoter"+i).getElement().appendChild(anchor.getElement());
				}
			}
			
		});
		
		ssService.getNewestUsers(14, new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

				
				for(int i=0;i<14;i++){
					
					// Change the associated elements inner text
					RootPanel.get("newestUser"+i).getElement().setInnerText("Error");
				}
				
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(Object result) {
				// TODO Auto-generated method stub
				UserInfo[] newestUsers = (UserInfo[])result;
				
				// For each user
				for(int i=0;i<newestUsers.length;i++){

					Anchor anchor = new Anchor(newestUsers[i].username);
					
					String urlBase = "MyGig_Planner.html?";
					
					// If we have a code server
					if(Window.Location.getParameter("gwt.codesvr")!=null){
						
						// Append it to the url base
						urlBase += "gwt.codesvr="+Window.Location.getParameter("gwt.codesvr")+"&";
					}
					
					anchor.setHref(urlBase+"tab=3&user="+newestUsers[i].username);

					// Change the associated elements inner text
					RootPanel.get("newestUser"+i).getElement().setInnerText("");
					RootPanel.get("newestUser"+i).getElement().appendChild(anchor.getElement());
				}
			}
			
		});
		
		
	}
}
