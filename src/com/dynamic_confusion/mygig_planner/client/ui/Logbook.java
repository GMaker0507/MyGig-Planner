package com.dynamic_confusion.mygig_planner.client.ui;

import java.util.Date;

import com.dynamic_confusion.mygig_planner.client.GigInfo;
import com.dynamic_confusion.mygig_planner.client.TabUpdateEvent;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideServiceClientImpl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.DatePicker;

public class Logbook extends Composite {
	public ServerSideServiceClientImpl ssService = null;
	
	public DatePicker datePicker = null;
	public StackPanel stackPanel = null;
	
	public Logbook(ServerSideServiceClientImpl ssService) {
		
		this.ssService = ssService;
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		initWidget(absolutePanel);
		absolutePanel.setSize("800px", "514px");
		
		final StackPanel stackPanel = new StackPanel();
		absolutePanel.add(stackPanel, 207, 47);
		stackPanel.setSize("462px", "339px");
		
		Button btnNewButton = new Button("New button");
		btnNewButton.setText("More");
		absolutePanel.add(btnNewButton, 588, 393);
		btnNewButton.setSize("81px", "28px");
		
		Label lblLogbook = new Label("Logbook");
		lblLogbook.setStyleName("gwt-Label-Header");
		absolutePanel.add(lblLogbook, 207, 11);
		
		DatePicker datePicker = new DatePicker();
		absolutePanel.add(datePicker, 10, 47);
		datePicker.setSize("157px", "181px");
		
		this.datePicker = datePicker;
		this.stackPanel = stackPanel;
		
		final Logbook lgb = this;
		

		
		this.addHandler(new TabUpdateEvent.Handler(){

			@Override
			public void onUpdate(TabUpdateEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println("We are updating the logbook");
				
				// Tell things to update
				update();
			}
			
		},TabUpdateEvent.TYPE);
		
		datePicker.addValueChangeHandler(new ValueChangeHandler<Date>(){

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				// TODO Auto-generated method stub
				
				lgb.update();
			}
			
			
		});
		
		update();
	}
	
	private void update(){
		
		stackPanel.clear();

		Date today = datePicker.getValue();
		if(today==null)today = new Date();
		Date startRange = new Date(today.getYear(),today.getMonth(),today.getDate() - today.getDay());
		Date endRange = new Date(today.getYear(),today.getMonth(),today.getDate() + (6 - today.getDay()));
		
		final Date startRange2 = startRange;
		
		ssService.getOffers(startRange, endRange, new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				stackPanel.add(new HTML(caught.getMessage()));
			}

			@Override
			public void onSuccess(Object result) {
				// TODO Auto-generated method stub
				
				GigInfo[] gigs = (GigInfo[])result;
				
				stackPanel.add(new HTML("<h3>Gigs for week of "+startRange2.toString()+"</h3>"));
				
				Grid weekGrid = new Grid(gigs.length,3);
				
				for(int i=0;i<gigs.length;i++){
					
					weekGrid.setHTML(i,0,"<p>"+gigs[i]+"</p>");
					
					// If the recipient isthe active users
					if(gigs[i].recipientUser.equals(Cookies.getCookie("activeUser"))){
						
						final Anchor acceptAnchor = new Anchor("Accept");
						final Anchor rejectAnchor = new Anchor("Reject");
						
						final String gigKey = gigs[i].key;
						
						acceptAnchor.addClickHandler(new ClickHandler(){

							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								
								RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,
										"/servlet/gig/accept?action=accept&"+
										"recipientUser="+Cookies.getCookie("activeUser")+"&"+
										"key="+gigKey+"&");
								
								try {
									rb.sendRequest(null, new RequestCallback(){

										@Override
										public void onResponseReceived(
												Request request, Response response) {
											// TODO Auto-generated method stub
											String results = response.getText();
											
											if(results.indexOf("success")!=-1){
												
												acceptAnchor.setText("Gig");
												acceptAnchor.setEnabled(false);
												
												rejectAnchor.setText("Accepted");
												rejectAnchor.setEnabled(false);
											}
										}

										@Override
										public void onError(Request request,
												Throwable exception) {
											// TODO Auto-generated method stub
											Window.alert(exception.getMessage());
											
										}
										
										
									});
								} catch (RequestException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							
						});
						
						rejectAnchor.addClickHandler(new ClickHandler(){

							@Override
							public void onClick(ClickEvent event) {
								// TODO Auto-generated method stub
								
								RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,
										"/servlet/gig/reject?action=reject&"+
										"recipientUser="+Cookies.getCookie("activeUser")+"&"+
										"key="+gigKey+"&");
								
								try {
									rb.sendRequest(null, new RequestCallback(){

										@Override
										public void onResponseReceived(
												Request request, Response response) {
											// TODO Auto-generated method stub
											String results = response.getText();
											
											if(results.indexOf("success")!=-1){
												
												acceptAnchor.setText("Gig");
												acceptAnchor.setEnabled(false);
												
												rejectAnchor.setText("Rejected");
												rejectAnchor.setEnabled(false);
											}
										}

										@Override
										public void onError(Request request,
												Throwable exception) {
											// TODO Auto-generated method stub
											Window.alert(exception.getMessage());
										}
										
										
									});
								} catch (RequestException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
							
						});
						
						// Set the second column
						weekGrid.setWidget(i, 1, acceptAnchor);
						
						// Set the third column
						weekGrid.setWidget(i, 2,rejectAnchor);
					}
				}
				
				stackPanel.clear();
				stackPanel.add(weekGrid);
				
				
			}
			
		});
		
		
	}
}
