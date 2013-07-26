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

	public Calendar() {
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		initWidget(horizontalPanel);
		final VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setVisible(false);
		
		final DatePicker datePicker = new DatePicker();
		datePicker.setStyleName("gwt-Label-Login");
		datePicker.setSize("500px", "500px");
		
		final TextBox timebx =new TextBox();
        timebx.setReadOnly(true);
        verticalPanel.add(timebx);
		 
		datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {

            public void onValueChange(ValueChangeEvent<Date> event) {
                // TODO Auto-generated method stub

                Date date=event.getValue();
                timebx.setText(DateTimeFormat.getFormat("yyyy-MM-dd").format(date));
   
                if (timebx.getText().matches("2013-07-24")) {
                	
                	verticalPanel.add(new Label("You are at the correct date!"));
                	verticalPanel.setVisible(true);
                	// Info on this tab, include:
                	// Band that is playing 
                }	
                else
                	verticalPanel.setVisible(false);
            }
        });	
		
		horizontalPanel.add(datePicker);
		horizontalPanel.add(verticalPanel);
	}
}
