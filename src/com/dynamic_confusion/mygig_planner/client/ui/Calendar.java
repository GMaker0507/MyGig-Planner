package com.dynamic_confusion.mygig_planner.client.ui;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DatePicker;

public class Calendar extends Composite {
	
	String dateString;
	VerticalPanel verticalPanel;
	Label currentDate;
	
	public Calendar() {
		
		final HorizontalPanel horizontalPanel = new HorizontalPanel();
		initWidget(horizontalPanel);

		final DatePicker datePicker = new DatePicker();
		datePicker.setStyleName("gwt-Label-Login");
		datePicker.setSize("500px", "500px");
        
		datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {

            public void onValueChange(ValueChangeEvent<Date> event) {
                // TODO Auto-generated method stub

                Date date=event.getValue();
                dateString = DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
                // Put the panel in another class to update the panel
         
                if (dateString.matches("2013-07-24")) {
                	
                	// Info on this tab, include:
                	// Band that is playing 
                	// Create new CalendarPanel class to represent the activities
                	CalendarPanel calendarPanel = new CalendarPanel(dateString);
                	verticalPanel = new VerticalPanel();
                	currentDate = calendarPanel.CurrentDate();
                	verticalPanel.add(currentDate);
                	horizontalPanel.add(verticalPanel);
                }
                else
                	verticalPanel.remove(currentDate);
            }
        });	
	
		horizontalPanel.add(datePicker);
	}
}
