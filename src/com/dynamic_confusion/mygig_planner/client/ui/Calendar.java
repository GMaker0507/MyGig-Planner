package com.dynamic_confusion.mygig_planner.client.ui;

import java.util.Date;

import com.dynamic_confusion.mygig_planner.client.GigInfo;
import com.dynamic_confusion.mygig_planner.client.TabUpdateEvent;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideServiceClientImpl;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class Calendar extends Composite {
	
	VerticalPanel verticalPanel;
	Label currentDate;
	ServerSideServiceClientImpl ssService;
	Date date;
	
	public Calendar(ServerSideServiceClientImpl ssService) {
		
		// Set ssService to this class
		this.ssService = ssService;
		
		final HorizontalPanel horizontalPanel = new HorizontalPanel();
		initWidget(horizontalPanel);

		final DatePicker datePicker = new DatePicker();
		datePicker.setStyleName("gwt-Label-Login");
		datePicker.setSize("500px", "500px");
		
		final ServerSideServiceClientImpl ssService2 = ssService;
        
		datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {

            public void onValueChange(ValueChangeEvent<Date> event) {
                // TODO Auto-generated method stub

                date = event.getValue();
                final String dateString = DateTimeFormat.getFormat("yyyy-mm-dd").format(date);

                ssService2.getOffers(date,date,new AsyncCallback() {

        			@Override
        			public void onFailure(Throwable caught) {
        				// TODO Auto-generated method stub
        				Window.alert(caught.getMessage());
        			}

        			@Override
        			public void onSuccess(Object result) {
        	
        				// TODO Auto-generated method stub
        				GigInfo[] offers = (GigInfo[]) result;
        				int i = 0, offer;

        				// Go through each offers
        				for (i = 0; i < offers.length; i++) {
        					
        					// If the offer for the musician is confirmed, then get the date replied and put to calendar
        					offer = offers[i].status;
    						
    						Date dateTemp = offers[i].dateReplied;
    						String dateStringToCompare = DateTimeFormat.getFormat("yyyy-mm-dd").format(dateTemp);
    						
    						if (dateString.matches(dateStringToCompare)) {
    							
    							// Create new CalendarPanel class to represent the activities
    		                	//CalendarPanel calendarPanel = new CalendarPanel(dateRep);
    		                	verticalPanel = new VerticalPanel();
    		                	
    		                	// Print the band info on the panel corresponding with the selected date
    		                	verticalPanel.add(new Label(offers[i].getEntityName()));
    		                	verticalPanel.add(new Label("Confirm"));     
    		                	horizontalPanel.add(verticalPanel);
    						}
    						else {
    							
    							verticalPanel.remove(0);
    							verticalPanel.remove(1);
    						}
    							

        				}
        			}
        		});
                // Put the panel in another class to update the panel
            }
        });	
	
		horizontalPanel.add(datePicker);
		
		this.addHandler(new TabUpdateEvent.Handler() {
			
			@Override
			public void onUpdate(TabUpdateEvent event) {
				// TODO Auto-generated method stub
			}
		}, TabUpdateEvent.GetType());
	}
}
