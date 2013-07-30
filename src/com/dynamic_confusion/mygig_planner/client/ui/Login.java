package com.dynamic_confusion.mygig_planner.client.ui;

import com.dynamic_confusion.mygig_planner.client.ChangeLoginStateEvent;
import com.dynamic_confusion.mygig_planner.client.TabUpdateEvent;
import com.dynamic_confusion.mygig_planner.client.TabUpdateEvent.Handler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

public class Login extends Composite {

	public Home homeParent = null;
	
	public Login(Home homeParent) {
		
		this.homeParent = homeParent;
		
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
		
		final Home homeParentFinal = homeParent;
		
		loginForm.addSubmitCompleteHandler(new SubmitCompleteHandler(){
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				// TODO Auto-generated method stub
				
				loginButton.setText("Login");
				loginButton.setEnabled(true);
				
				String loginResults = event.getResults().trim();
				
				// If it says success
				if(loginResults.indexOf("success") != -1){
					
					// Set the cookie
					Cookies.setCookie("activeUser", userTextBox.getText());	

					
					// CReate the event to fire
					GwtEvent<TabUpdateEvent.Handler> g = new GwtEvent<TabUpdateEvent.Handler>(){

						@Override
						public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
							// TODO Auto-generated method stub
							return TabUpdateEvent.GetType();
						}

						@Override
						protected void dispatch(Handler handler) {
							// TODO Auto-generated method stub
							handler.onUpdate(null);
						}
						
					};
					
					homeParentFinal.fireEvent(g);
					homeParentFinal.updateLoginState();
				}
				else {
					
					// TODO handle output of error message
					//RootPanel.get().add(new HTML(errorMessage));
					Window.alert(loginResults);
					loginButton.setText("Log in");	
				}
			}
		});
		
		loginButton.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				
				loginButton.setText("Proccessing...");
				loginButton.setEnabled(false);

				// Submit the form
				loginForm.submit();
			}
		});
		

	}
}
