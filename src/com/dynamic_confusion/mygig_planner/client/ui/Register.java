package com.dynamic_confusion.mygig_planner.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class Register extends Composite {

	public Register() {
		
		final TextBox userTextBox = new TextBox(), fnameTextBox = new TextBox(), lnameTextBox = new TextBox();
		final TextBox addressTextBox = new TextBox(), phonenumberTextBox = new TextBox(), genreTextBox = new TextBox();
		final TextBox capacityTextBox = new TextBox(), pricerangeTextBox = new TextBox(), openhoursTextBox = new TextBox();
		final PasswordTextBox passTextBox = new PasswordTextBox(), verpassTextBox = new PasswordTextBox();
		// Set names for the form
		userTextBox.setName("username");		passTextBox.setName("password");			verpassTextBox.setName("verifypassword");
		fnameTextBox.setName("firstName");		lnameTextBox.setName("lastName");
		addressTextBox.setName("address");		phonenumberTextBox.setName("phoneNumber");
		openhoursTextBox.setName("openHours");	genreTextBox.setName("genre");
		capacityTextBox.setName("capacity");	pricerangeTextBox.setName("priceRange");
		
		// Create a form panel
		final FormPanel registerForm = new FormPanel();
		//form.setAction("/myFormHandler");
		
		// Create a layout of the register section
		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(registerForm);
		registerForm.setWidget(verticalPanel);
		registerForm.setEncoding(FormPanel.ENCODING_URLENCODED);
		registerForm.setMethod(FormPanel.METHOD_POST);
		registerForm.setAction("/servlet/registration");
		
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
		
		Label userLabel = new Label("*Username"), passLabel = new Label("*Password"), firstname = new Label("First Name"), lastname = new Label("Last Name");
		Label address = new Label("Address"), phonenumber = new Label("Phone Number"), openhours = new Label("Business Hours"), genre = new Label("Genre");
		Label capacity = new Label("Capacity"), pricerange = new Label("Price Range"), verpass = new Label("*Enter password again");
		
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
				
				// If the username and password and verpassword text boxes are empty, ask user to fill it
				if (userTextBox.getText().isEmpty() || passTextBox.getText().isEmpty() || verpassTextBox.getText().isEmpty()) {
					
					Window.alert("Please fill the mandatory asterisk fields.");
				}
				
				// If the password and verpassword fields don't match, alert the user
				else if (!passTextBox.getText().matches(verpassTextBox.getText())) {
					
					Window.alert("Password and Verify Password does not match, please type in right or get!!");
				}
				
				else {
					
					Window.alert("Registration successful!");
					// Submit the form
					registerForm.submit();	
					Window.Location.reload();
				}	
			}
		});
				
		registerForm.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// TODO Auto-generated method stub
				
				String registerResults = event.getResults().trim();
				
				// If it says success
				if (registerResults.indexOf("success") != -1) {
					
					Window.alert("Successfull registration!");
					
					// Set the cookie
					Cookies.setCookie("activeUser", userTextBox.getText());	
					
					// Reload
					Window.Location.reload();
				}
				else {
					
					// TODO handle output of error message
					String errorMessage = registerResults;
					
					Window.alert(errorMessage);
				}
			}
		});
	}
	
}
