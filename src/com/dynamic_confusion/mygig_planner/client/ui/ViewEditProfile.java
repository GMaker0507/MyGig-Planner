package com.dynamic_confusion.mygig_planner.client.ui;

import java.util.Date;

import com.dynamic_confusion.mygig_planner.client.TabUpdateEvent;
import com.dynamic_confusion.mygig_planner.client.UserInfo;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;

public class ViewEditProfile extends Composite {

	// Custom click handler to take into consideration
	class VEPClickHandler implements ClickHandler{
		
		public DatePicker aCalen = null;
		public UserInfo user = new UserInfo();			

		@Override
		public void onClick(ClickEvent event) {
			// TODO Auto-generated method stub
			if(aCalen==null)return;
			
			final Date selectedDate = aCalen.getValue();
			
			btnChangeAvailability.setText("Proccessing...");
			btnChangeAvailability.setEnabled(false);
			
			final Button caButton = btnChangeAvailability;
			
			RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,"servlet/availability"+
					"?user="+user.username+
					"&year="+selectedDate.getYear()+
					"&month="+selectedDate.getMonth()+
					"&date="+selectedDate.getDate());
			try {
				rb.sendRequest(null, new RequestCallback(){

									@Override
									public void onResponseReceived(
											Request request, Response response) {
										// TODO Auto-generated method stub
										Window.alert(response.getText());
										
										btnChangeAvailability.setText("Change Availability");
										btnChangeAvailability.setEnabled(true);
										
										if(aCalen.getStyleOfDate(selectedDate).equals("datePickerDayIsHighlighted")){
											
											aCalen.removeStyleFromDates("datePickerDayIsHighlighted", selectedDate);
										}else aCalen.addStyleToDates("datePickerDayIsHighlighted", selectedDate);
									}

									@Override
									public void onError(Request request,
											Throwable exception) {
										// TODO Auto-generated method stub
										Window.alert(exception.getMessage());
										
										btnChangeAvailability.setText("Change Availability");
										btnChangeAvailability.setEnabled(true);
										
									}
					
					
				});
			} catch (RequestException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Window.alert(e.getMessage());
			}
			
		}
	}
	
	public UserInfo showUser = null;
	public UserInfo defaultUser = null;
	public boolean edit = false;
	
	public ServerSideServiceClientImpl ssService = null;
	private Label lblUsername;
	private Label lblEmail;
	private Label lblJoinDate;
	private CheckBox chckbxHasPASystem;
	private CheckBox chckbxHasSoundPerson;
	private CheckBox chckbxRequiresHospitalityPack;
	private TextBox txtOpenHours;
	private Button btnSaveChanges;
	private TextBox pswCurrentPassword;
	private PasswordTextBox pswNewPassword;
	private PasswordTextBox pswConfirmPassword;
	private DatePicker availabilityCalendar;
	private Button btnChangeAvailability;
	private Label lblName;
	private Label lblGenreLabel;
	private Label lblExperience;
	private Label lblGigCount;
	private Label lblGenre;
	private Label lblFullName;
	
	private VEPClickHandler vepch;
	private CheckBox chckbxOnlyOriginalMusic;
	
	public ViewEditProfile(ServerSideServiceClientImpl ssService) {
		
		this.ssService = ssService;
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		
		final FormPanel editForm = new FormPanel();
		
		editForm.setAction("/servlet/profile");
		
		// Set the widget for the form
		editForm.setWidget(absolutePanel);
		
		absolutePanel.setStyleName("none");
		initWidget(editForm);
		absolutePanel.setSize("800px", "512px");
		
		Label lblNewLabel = new Label("Username:");
		lblNewLabel.setStyleName("none");
		absolutePanel.add(lblNewLabel, 30, 126);
		
		lblUsername = new Label("SomeUsernameHere");
		lblUsername.setStyleName("none");
		absolutePanel.add(lblUsername, 90, 126);
		lblUsername.setSize("158px", "15px");
		
		Label lblEmailLabel = new Label("Email:");
		lblEmailLabel.setStyleName("none");
		absolutePanel.add(lblEmailLabel, 30, 148);
		lblEmailLabel.setSize("57px", "15px");
		
		lblEmail = new Label("something@something.com");
		lblEmail.setStyleName("none");
		absolutePanel.add(lblEmail, 90, 148);
		lblEmail.setSize("158px", "15px");
		
		lblJoinDate = new Label("July 28th, 2013");
		lblJoinDate.setStyleName("none");
		absolutePanel.add(lblJoinDate, 90, 169);
		lblJoinDate.setSize("158px", "15px");
		
		Label lblJoinDateLabel = new Label("Join Date:");
		lblJoinDateLabel.setStyleName("none");
		absolutePanel.add(lblJoinDateLabel, 30, 169);
		lblJoinDateLabel.setSize("57px", "15px");
		
		chckbxHasPASystem = new CheckBox("Has PA System");
		absolutePanel.add(chckbxHasPASystem, 30, 203);
		
		chckbxHasSoundPerson = new CheckBox("Has Sound Person");
		absolutePanel.add(chckbxHasSoundPerson, 30, 228);
		chckbxHasSoundPerson.setSize("158px", "19px");
		
		chckbxRequiresHospitalityPack = new CheckBox("Requires Hospitality Pack");
		absolutePanel.add(chckbxRequiresHospitalityPack, 30, 253);
		chckbxRequiresHospitalityPack.setSize("218px", "19px");
		
		availabilityCalendar = new DatePicker();
		absolutePanel.add(availabilityCalendar, 461, 126);
		
		btnSaveChanges = new Button("Save Changes");
		absolutePanel.add(btnSaveChanges, 30, 361);
		btnSaveChanges.setSize("171px", "28px");
		
		btnChangeAvailability = new Button("Change Availability");
		absolutePanel.add(btnChangeAvailability, 461, 315);
		btnChangeAvailability.setSize("190px", "28px");
		
		pswCurrentPassword = new TextBox();
		absolutePanel.add(pswCurrentPassword, 255, 246);
		
		Label lblCurrentPassword = new Label("Current Password:");
		absolutePanel.add(lblCurrentPassword, 255, 226);
		
		Label lblNewPassword = new Label("New Password:");
		absolutePanel.add(lblNewPassword, 255, 282);
		lblNewPassword.setSize("101px", "15px");
		
		Label lblConfirmNewPassword = new Label("Confirm New Password:");
		absolutePanel.add(lblConfirmNewPassword, 255, 338);
		lblConfirmNewPassword.setSize("171px", "15px");
		
		pswNewPassword = new PasswordTextBox();
		absolutePanel.add(pswNewPassword, 255, 302);
		
		pswConfirmPassword = new PasswordTextBox();
		absolutePanel.add(pswConfirmPassword, 255, 359);
		
		Label lblOpenHours = new Label("Open Hours:");
		absolutePanel.add(lblOpenHours, 30, 307);
		lblOpenHours.setSize("101px", "15px");
		
		txtOpenHours = new TextBox();
		absolutePanel.add(txtOpenHours, 30, 327);
		txtOpenHours.setSize("163px", "18px");
		
		lblFullName = new Label("John Smith");
		lblFullName.setStyleName("none");
		absolutePanel.add(lblFullName, 297, 126);
		lblFullName.setSize("158px", "15px");
		
		lblGenre = new Label("Rock and Roll");
		lblGenre.setStyleName("none");
		absolutePanel.add(lblGenre, 297, 148);
		lblGenre.setSize("158px", "15px");
		
		lblGigCount = new Label("22 Gigs");
		lblGigCount.setStyleName("none");
		absolutePanel.add(lblGigCount, 297, 169);
		lblGigCount.setSize("158px", "15px");
		
		lblName = new Label("Name:");
		lblName.setStyleName("none");
		absolutePanel.add(lblName, 237, 126);
		lblName.setSize("57px", "15px");
		
		lblGenreLabel = new Label("Genre:");
		lblGenreLabel.setStyleName("none");
		absolutePanel.add(lblGenreLabel, 237, 148);
		lblGenreLabel.setSize("57px", "15px");
		
		lblExperience = new Label("Experience:");
		lblExperience.setStyleName("none");
		absolutePanel.add(lblExperience, 226, 169);
		lblExperience.setSize("57px", "15px");
		
		final DatePicker aCalen = this.availabilityCalendar;
		
		chckbxOnlyOriginalMusic = new CheckBox("Only Original Music");
		chckbxOnlyOriginalMusic.setValue((Boolean) null);
		absolutePanel.add(chckbxOnlyOriginalMusic, 30, 278);
		chckbxOnlyOriginalMusic.setSize("218px", "19px");
		

		// Add a click handler
		btnChangeAvailability.addClickHandler((ClickHandler)(vepch = new VEPClickHandler()));
		
		
		this.addHandler(new TabUpdateEvent.Handler(){

			@Override
			public void onUpdate(TabUpdateEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println("We are updating the view edit profile");
				
				// Tell things to update
				update();
			}
			
		},TabUpdateEvent.TYPE);
		
		btnSaveChanges.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				// Swap if we are editing
				edit = !edit;
				
				// If we arent editingany more
				if(!edit){
					
					// Submit the form
					editForm.submit();
				}
				
				// Update the layout
				update();
				
			}
			
			
		});
		
		update();
	}
	
	
	
	public void update(){
		
		// Are we editing
		btnSaveChanges.setText(edit ? "Save Changes" : "Edit Profile");
		
		String activeUser = Cookies.getCookie("activeUser");

		if(defaultUser==null){
		
			// Get the active user
			ssService.getUser(activeUser, new AsyncCallback(){
	
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
					Window.alert(caught.getMessage());
				}
	
				@Override
				public void onSuccess(Object result) {
					
					// TODO Auto-generated method stub
					defaultUser = (UserInfo)result;
					
					update();
				}
				
				
			});
		
		}
		
		final UserInfo user = showUser == null ? defaultUser : showUser;
		
		if(user==null)return;
		
		vepch.user = user;
		vepch.aCalen = this.availabilityCalendar;
		
		lblUsername.setText(user.username);
		lblEmail.setText(user.email);
		lblJoinDate.setText(user.dateJoined.toString());
		lblFullName.setText(user.firstName+" "+user.lastName);
		lblGenre.setText(user.genre);
		lblGigCount.setText(user.gigCount+" Gigs");
		txtOpenHours.setText(user.openHours);
		chckbxHasSoundPerson.setValue(user.hasSoundPerson);
		chckbxRequiresHospitalityPack.setValue(user.hasHospitalityPack);
		chckbxHasPASystem.setValue(user.hasPA);
		chckbxOnlyOriginalMusic.setValue(user.onlyOriginalMusic);
		
		txtOpenHours.setEnabled(edit);
		chckbxHasSoundPerson.setEnabled(edit);
		chckbxRequiresHospitalityPack.setEnabled(edit);
		chckbxHasPASystem.setEnabled(edit);
		chckbxOnlyOriginalMusic.setEnabled(edit);
		
		Date start = availabilityCalendar.getFirstDate();
		Date end = availabilityCalendar.getLastDate();
		
		final DatePicker aCalen = this.availabilityCalendar;
		
		ssService.getDatesAvailable(user.username, start, end, new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(Object result) {
				// TODO Auto-generated method stub
				Date[] availableDates = (Date[])result;
				
				for(int i=0;i<availableDates.length;i++){
					
					System.out.println("Adding style for: "+availableDates[i]);
					
					aCalen.addStyleToDates("datePickerDayIsHighlighted", availableDates[i]);
				}
			}
			
		});
		
	}
}
