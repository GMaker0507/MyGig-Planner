package com.dynamic_confusion.mygig_planner.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class Register extends Composite {

	public Register() {
		
		final TextBox userTextBox = new TextBox(), passTextBox = new TextBox(), fnameTextBox = new TextBox(), lnameTextBox = new TextBox();
		final TextBox addressTextBox = new TextBox(), phonenumberTextBox = new TextBox(), openhoursTextBox = new TextBox(), genreTextBox = new TextBox();
		final TextBox capacityTextBox = new TextBox(), pricerangeTextBox = new TextBox(), verpassTextBox = new TextBox();
		
		// Set names for the form
		userTextBox.setName("username");		passTextBox.setName("password");			verpassTextBox.setName("verifypassword");
		fnameTextBox.setName("firstname");		lnameTextBox.setName("lastname");
		addressTextBox.setName("address");		phonenumberTextBox.setName("phonenumber");
		openhoursTextBox.setName("openhours");	genreTextBox.setName("genre");
		capacityTextBox.setName("capacity");	pricerangeTextBox.setName("pricerange");
		
		
		// Create a form panel
		final FormPanel registerForm = new FormPanel();
		//form.setAction("/myFormHandler");
		
		// Create a layout of the register section
		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(registerForm);
		registerForm.setWidget(verticalPanel);
		registerForm.setEncoding(FormPanel.ENCODING_URLENCODED);
		registerForm.setMethod(FormPanel.METHOD_POST);
		registerForm.setAction("/server-side/registration");
		
		Label lblRegisterHeading = new Label("New User? Start here!");
		lblRegisterHeading.setStyleName("gwt-Label-Header");
		Label lblRegisterNewUser = new Label("Register your information below:");
		lblRegisterNewUser.setStyleName("gwt-Label-Login");
		verticalPanel.add(lblRegisterHeading);
		verticalPanel.add(lblRegisterNewUser);
		verticalPanel.add(new Label(""));
		
		// Layout of the registering section
		FlexTable flexTable = new FlexTable();
		verticalPanel.add(flexTable);
		
		Label userLabel = new Label("Username"), passLabel = new Label("Password"), firstname = new Label("First Name"), lastname = new Label("Last Name");
		Label address = new Label("Address"), phonenumber = new Label("Phone Number"), openhours = new Label("Business Hours"), genre = new Label("Genre");
		Label capacity = new Label("Capacity"), pricerange = new Label("Price Range"), verpass = new Label("Enter password again");
		
		// Labels										Text Boxes 
		flexTable.setWidget(1, 0, userLabel);			flexTable.setWidget(1, 1, userTextBox);
		flexTable.setWidget(2, 0,  passLabel);			flexTable.setWidget(2, 1, passTextBox);
		flexTable.setWidget(3, 0, verpass);				flexTable.setWidget(3, 1, verpassTextBox);
		flexTable.setWidget(4, 0, firstname);			flexTable.setWidget(4, 1, fnameTextBox);
		flexTable.setWidget(5, 0, lastname);			flexTable.setWidget(5, 1, lnameTextBox);
		flexTable.setWidget(6, 0, address);				flexTable.setWidget(6, 1, addressTextBox);
		flexTable.setWidget(7, 0, phonenumber);			flexTable.setWidget(7, 1, phonenumberTextBox);
		flexTable.setWidget(8, 0, openhours);			flexTable.setWidget(8, 1, openhoursTextBox);
		flexTable.setWidget(9, 0, genre);				flexTable.setWidget(9, 1, genreTextBox);
		flexTable.setWidget(10, 0, capacity);			flexTable.setWidget(10, 1, capacityTextBox);
		flexTable.setWidget(11, 0, pricerange);			flexTable.setWidget(11, 1, pricerangeTextBox);
		
		Button registerButton = new Button("Register");
		registerButton.setSize("100px", "36px");
		flexTable.setWidget(14, 0, registerButton);
		
		// On clicking registerButton events
		registerButton.addClickHandler(new ClickHandler(){
					
			@Override
			public void onClick(ClickEvent event) {
						
				// Submit the form
				registerForm.submit();	
			}
		});
				
		registerForm.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// TODO Auto-generated method stub
				
				String registerResults = event.getResults().trim();
				
				// If it says success
				if(registerResults.equals("success")){
					
					// Setthe cookie
					Cookies.setCookie("activeUser", userTextBox.getText());	
					
					// Reload
					Window.Location.reload();

				}else{


					String errorMessage = registerResults;
					
					// TODO handle output of error message
					
					home.add(new HTML(errorMessage));
				}
			}
		});
	}
	
}
