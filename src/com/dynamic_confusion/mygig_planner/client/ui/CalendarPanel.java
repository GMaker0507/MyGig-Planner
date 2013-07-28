package com.dynamic_confusion.mygig_planner.client.ui;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CalendarPanel {
	
	String dateString;
	
	CalendarPanel(String dateString) {
		
		this.dateString = dateString;
	}
	
	Label CurrentDate () {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		Label lblDate = new Label(dateString);
		verticalPanel.add(lblDate);
		return lblDate;
	}
}
