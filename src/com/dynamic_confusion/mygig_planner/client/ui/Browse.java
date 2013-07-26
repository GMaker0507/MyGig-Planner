package com.dynamic_confusion.mygig_planner.client.ui;

import com.dynamic_confusion.mygig_planner.client.SearchInfo;
import com.dynamic_confusion.mygig_planner.client.UserInfo;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideServiceClientImpl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.CheckBox;

public class Browse extends Composite {
	private ScrollPanel scrollPanel;
	private FlexTable browseTable;
	private ServerSideServiceClientImpl ssService;
	
	int minCapacity;
	int minPay;
	Boolean requiresPA;
	Boolean requiresHospPack;
	Boolean original;
	Boolean needsSoundPerson;
	Boolean availableOnly = false;
	
	public Browse(ServerSideServiceClientImpl service) {
		
		ssService = service;
		
		// Main display
		FlexTable form = new FlexTable();
		initWidget(form);
		form.setSize("870px", "490px");
		
		// Table displaying browse criteria
		browseTable = new FlexTable();
		form.setWidget(0, 0, browseTable);
		browseTable.setSize("120px", "490px");
		
		// Browse Label
		Label browseLabel = new Label("Browse");
		browseLabel.setStyleName("mgp-Label");
		browseTable.setWidget(0, 0, browseLabel);
		
		// Panel which includes browse options
		AbsolutePanel absolutePanel = new AbsolutePanel();
		browseTable.setWidget(1, 0, absolutePanel);
		absolutePanel.setSize("120px", "450px");
		
		// TODO Add browse options
		
		// Browse button
		Button browseButton = new Button("Browse");
		browseButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getResults();
			}
		});
		browseButton.setStyleName("mgp-Button");
		absolutePanel.add(browseButton, 10, 403);
		
		// Radiio buttons for Availability
		{
			Label lblAvailable = new Label("Available");
			absolutePanel.add(lblAvailable, 0, 3);
			
			RadioButton radioBtnAll = new RadioButton("all_bands");
			radioBtnAll.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					availableOnly = false;
				}
			});
			
			radioBtnAll.setText("All Bands");
			absolutePanel.add(radioBtnAll, 10, 25);
			radioBtnAll.setSize("110px", "20px");
			
			RadioButton radioBtnAvail = new RadioButton("available");
			radioBtnAll.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					availableOnly = true;
				}
			});
			radioBtnAvail.setText("Available Only");
			absolutePanel.add(radioBtnAvail, 10, 51);
			radioBtnAvail.setSize("110px", "20px");
		}
		
		// Check boxes for Genre
		{
			Label lblGenre = new Label("Genre");
			absolutePanel.add(lblGenre, 0, 86);
			
			CheckBox chckbxRock = new CheckBox("Rock");
			absolutePanel.add(chckbxRock, 10, 110);
			
			CheckBox chckbxPop = new CheckBox("Pop");
			absolutePanel.add(chckbxPop, 10, 130);
			
			CheckBox chckbxCountry = new CheckBox("Country");
			absolutePanel.add(chckbxCountry, 10, 150);

			CheckBox chckbxSka = new CheckBox("Ska");
			absolutePanel.add(chckbxCountry, 10, 170);
			
			CheckBox chckbxReggae = new CheckBox("Reggae");
			absolutePanel.add(chckbxCountry, 10, 190);

			CheckBox chckbxBluegrass = new CheckBox("Bluegrass");
			absolutePanel.add(chckbxCountry, 10, 210);

			CheckBox chckbxHiphop = new CheckBox("Hiphop");
			absolutePanel.add(chckbxCountry, 10, 230);

			CheckBox chckbxIndustrial = new CheckBox("Industrial");
			absolutePanel.add(chckbxCountry, 10, 250);

			CheckBox chckbxRockabilly = new CheckBox("Rockabilly");
			absolutePanel.add(chckbxCountry, 10, 270);
		}
		
		
		// Scroll panel displaying browse results
		scrollPanel = new ScrollPanel();
		form.setWidget(0, 1, scrollPanel);
		scrollPanel.setSize("750px", "490px");
		
	}
	
	private void getResults() {
		// BandDatabase database = new BandDatabase();
		// Band[] bands = database.getBands();
		
		SearchInfo searchInfo = new SearchInfo();

		ssService.search(searchInfo, new AsyncCallback() {

			public void onFailure(Throwable caught) {
				String errMsg ="<p>" + caught.getMessage() + "</p>";
				scrollPanel.setWidget(new Label(errMsg));
			}

			public void onSuccess(Object result) {
				UserInfo[] userInfo = (UserInfo[]) result;

				// Sets result of browse to tables
				for(int i = 0 ; i < userInfo.length ; i++) {
					browseTable.setWidget(2*i, 2, new Label(userInfo[i].username));
					browseTable.setWidget(2*i+1, 2, new Label(userInfo[i].email));
					
				}
			}
		});
		
	}
}
