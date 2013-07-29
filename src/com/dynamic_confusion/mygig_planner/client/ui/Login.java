package com.dynamic_confusion.mygig_planner.client.ui;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class Login extends Composite {

	public Login() {
		
		final FormPanel loginForm = new FormPanel();
		//form.setEncoding(FormPanel.ENCODING_MULTIPART);
		//form.setMethod(FormPanel.METHOD_POST);

		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(loginForm);
		loginForm.setWidget(verticalPanel);
		loginForm.setEncoding(FormPanel.ENCODING_URLENCODED);
		loginForm.setMethod(FormPanel.METHOD_GET);
		loginForm.setAction("/servlet/login");
		
		Label label0 = new Label("Log In");
		label0.setStyleName("gwt-Label-Header");
		Label label1 = new Label("Sign into your account:");
		label1.setStyleName("gwt-Label-Login");
		verticalPanel.add(label0);
		verticalPanel.add(label1);
		verticalPanel.add(new Label(""));
		
		FlexTable flexTable = new FlexTable();
		verticalPanel.add(flexTable);
		
		// Start login section--------------------------------------------------------------------------------------------------------------------------------
		// Add login layout to get the user's name and password
		Label userLabel = new Label("Username"), passLabel = new Label("Password");
		final TextBox userTextBox = new TextBox();
		final PasswordTextBox passwordTextBox = new PasswordTextBox();
		
		// Set names for the form
		userTextBox.setName("username");
		passwordTextBox.setName("password");
		
		flexTable.setWidget(0, 0, userLabel);
		flexTable.setWidget(0, 1, userTextBox);
		flexTable.setWidget(1, 0, passLabel);
		flexTable.setWidget(1, 1, passwordTextBox);
		
		// Login button 
		final Button loginButton = new Button("Log In");
		flexTable.setWidget(4, 0, loginButton);
		
		loginButton.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				
				loginButton.setText("Proccessing...");
				loginButton.setEnabled(false);

				// Submit the form
				loginForm.submit();
			}
		});
		
		loginForm.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// TODO Auto-generated method stub
				
				loginButton.setText("Wait...");
				loginButton.setEnabled(true);
				
				String loginResults = event.getResults().trim();
				
				// If it says success
				if(loginResults.indexOf("success") != -1){
					
					// Set the cookie
					Cookies.setCookie("activeUser", userTextBox.getText());	
					
					// Reload
					Window.Location.reload();
				}
				else {
					
					// TODO handle output of error message
					//RootPanel.get().add(new HTML(errorMessage));
					Window.alert(loginResults);
					loginButton.setText("Log in");	
				}
			}
		});

	}
}
