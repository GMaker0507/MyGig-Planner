package com.dynamic_confusion.mygig_planner.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.google.gwt.user.client.ui.TextBox;

public class Logbook extends Composite {
	public ServerSideServiceClientImpl ssService = null;
	
	public DatePicker datePicker = null;
	public StackPanel stackPanel = null;
	private Grid grdLogbook;
	
	private GigInfo[] gigs;
	private TextBox txtGigsFor;
	
	private boolean searching = false;
	
	public int page = 0;
	public int max = 0;
	public int limit = 10;
	
	public Logbook(ServerSideServiceClientImpl ssService) {
		
		this.ssService = ssService;
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		initWidget(absolutePanel);
		absolutePanel.setSize("800px", "514px");
		
		Button btnLastButton = new Button("New button");
		btnLastButton.setText("Last Page");
		absolutePanel.add(btnLastButton, 10, 270);
		btnLastButton.setSize("197px", "28px");
		
		Label lblLogbook = new Label("Logbook");
		lblLogbook.setStyleName("gwt-Label-Header");
		absolutePanel.add(lblLogbook, 207, 11);
		
		DatePicker datePicker = new DatePicker();
		absolutePanel.add(datePicker, 10, 12);
		datePicker.setSize("157px", "181px");
		
		this.datePicker = datePicker;
		
		grdLogbook = new Grid(11, 3);
		absolutePanel.add(grdLogbook, 214, 47);
		grdLogbook.setSize("576px", "457px");
		
		Button btnMore = new Button("New button");
		btnMore.setText("Next Page");
		absolutePanel.add(btnMore, 10, 236);
		btnMore.setSize("197px", "28px");
		
		Button btnFirstButton = new Button("New button");
		btnFirstButton.setText("First Page");
		absolutePanel.add(btnFirstButton, 10, 202);
		btnFirstButton.setSize("197px", "28px");
		
		txtGigsFor = new TextBox();
		absolutePanel.add(txtGigsFor, 214, 11);
		txtGigsFor.setSize("564px", "16px");
		
		final Logbook lgb = this;
		

		
		this.addHandler(new TabUpdateEvent.Handler(){

			@Override
			public void onUpdate(TabUpdateEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println("We are updating the logbook");
				
				// Tell things to update
				update();
				updateCalendar();
			}
			
		},TabUpdateEvent.TYPE);
		
		datePicker.addValueChangeHandler(new ValueChangeHandler<Date>(){

			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				// TODO Auto-generated method stub

				Date today = ((DatePicker)event.getSource()).getValue();
				
				if(today==null)today = new Date();
				
				Date startRange = new Date(today.getYear(),today.getMonth(),today.getDate() - today.getDay());
				
				txtGigsFor.setText("Gigs for week of: "+startRange.toString());

				lgb.update();
			}
			
			
		});
		
		btnMore.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				if(gigs==null)return;
				
				if((page+1)*limit>gigs.length)return;
				
				page++;
				
				lgb.populateGrid();
			}
			
		});
		
		btnFirstButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				page=0;
				
				if(gigs==null)return;
				
				lgb.populateGrid();
			}
			
		});
		
		btnLastButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub

				if(gigs==null)return;
				
				page=gigs.length/limit;
				
				lgb.populateGrid();
			}
			
		});
		
		//update();
		//updateCalendar();
	}
	
	private void update(){
		
		if(Cookies.getCookie("activeUser")==null)return;

		Date today = datePicker.getValue();
		
		if(today==null)today = new Date();
		
		Date startRange = new Date(today.getYear(),today.getMonth(),today.getDate() - today.getDay());
		Date endRange = new Date(today.getYear(),today.getMonth(),today.getDate() + (6 - today.getDay()));
		
		startRange.setHours(0);
		startRange.setMinutes(0);
		startRange.setSeconds(0);
		
		endRange.setHours(0);
		endRange.setMinutes(0);
		endRange.setSeconds(0);
		
		if(searching)return;
		searching =true;
		
		ssService.getOffers(startRange,endRange,new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert(caught.getMessage());
				searching = false;
			}
			
			@Override
			public void onSuccess(Object result) {
				// TODO Auto-generated method stub

				searching= false;
				
				if(result==null)return;
				
				gigs = (GigInfo[])result;
				
				if(gigs==null)return;
				
				page = 0;
				
				// Populate the grid
				populateGrid();
			
			}
			
		});
	}
	
	private void updateCalendar(){
		

		
		if(Cookies.getCookie("activeUser")==null)return;

		
		final DatePicker dpFinal = datePicker;

		if(searching)return;
		searching = true;
		ssService.getOffers(new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert(caught.getMessage());
				searching= false;
			}
			
			@Override
			public void onSuccess(Object result) {
				// TODO Auto-generated method stub

				searching= false;
				if(result==null)return;
				
				gigs = (GigInfo[])result;
				
				if(gigs==null)return;
				
				
				for(int i=0;i<gigs.length;i++){

					if(gigs[i].status!=0){
						dpFinal.addStyleToDates("datePickerDayIsHighlighted", gigs[i].dateSent);
						if(gigs[i].dateReplied!=null)	dpFinal.addStyleToDates("datePickerDayIsHighlighted", gigs[i].dateReplied);
					}
				}
			
			}
			
		});
	}
		
	private void populateGrid(){
		
		if(gigs==null){
			return;
		}
		
		Date today = datePicker.getValue();
		if(today==null)today = new Date();
		
		Date startRange = new Date(today.getYear(),today.getMonth(),today.getDate() - today.getDay());
		Date endRange = new Date(today.getYear(),today.getMonth(),today.getDate() + (6 - today.getDay()));
		
		startRange.setHours(0);
		startRange.setMinutes(0);
		startRange.setSeconds(0);
		
		endRange.setHours(0);
		endRange.setMinutes(0);
		endRange.setSeconds(0);
		
		final Grid weekGrid = grdLogbook;
		
		int skip = limit*page;
		if(skip>gigs.length-limit)skip = gigs.length-limit;
		if(skip<0)skip=0;
		
		List<GigInfo> weekGigs = new ArrayList<GigInfo>();
		
		for(int i=0;i<gigs.length;i++){
			
			if(weekGigs.size()>=limit)break;
			
			Date date = gigs[i].dateSent;
			Date date2 = gigs[i].dateReplied;
			
			if(date==null)continue;
			
			if(date.before(endRange)&&date.after(startRange)){
				
				if(skip<=0) {
					weekGigs.add(gigs[i].logbookClone(0));
				}
				else skip--;
			}
			if(date2!=null){

				if(date2.before(endRange)&&date2.after(startRange)){
					
					if(skip<=0) {
						weekGigs.add(gigs[i].logbookClone(1));
					}
					else skip--;
				}
			}
		}
		
		for(int i=0;i<10;i++){
			

			weekGrid.setHTML(i,0,"");
			weekGrid.setHTML(i,1,"");
			weekGrid.setHTML(i,2,"");
		}
		
		
		for(int i=0;i<weekGigs.size();i++){
			
			int index = i+limit*page;
			
			if(index>=gigs.length){
				
				weekGrid.setHTML(i,0,"");
				weekGrid.setHTML(i,1,"");
				weekGrid.setHTML(i,2,"");
				
				continue;
			}
			
			weekGrid.setHTML(i,0,"");
			weekGrid.setHTML(i,1,"");
			weekGrid.setHTML(i,2,"");
			
			
			weekGrid.setHTML(i,0,"<p>"+weekGigs.get(index).toLogbookString(Cookies.getCookie("activeUser"))+"</p>");
			
			// If the recipient isthe active users
			if(weekGigs.get(index).recipientUser.equals(Cookies.getCookie("activeUser"))&&(weekGigs.get(index).status==0)&&(weekGigs.get(index).sort==0)){
				
				final Button acceptAnchor = new Button("Accept");
				final Button rejectAnchor = new Button("Reject");
				
				final String gigKey = weekGigs.get(index).key;
				
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
										
										update();
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

										update();
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
	}
}
