package com.dynamic_confusion.mygig_planner.client.ui;

import com.dynamic_confusion.mygig_planner.client.SearchInfo;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideService;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideServiceClientImpl;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class Search extends Composite {
	private TextBox searchTextBox;
	private FlexTable resultsPanel;
	
	public Search(ServerSideServiceClientImpl ssService) {
		VerticalPanel form = new VerticalPanel();
		initWidget(form);
		form.setSize("870px", "490px");
		
		form.add(resultsPanel);
		resultsPanel.setSize("700px", "700px");
		
		// Search Title label
		Label searchLabel = new Label("Search");
		form.add(searchLabel);
		searchLabel.setStyleName("mgp-Label");
		searchLabel.setSize("57px", "19px");
		
		// Holds the textbox and search button
		FlexTable searchFlexTable = new FlexTable();
		form.add(searchFlexTable);
		searchFlexTable.setSize("571px", "62px");
		
		// Search Textbox
		searchTextBox = new TextBox();
		searchFlexTable.setWidget(0, 0, searchTextBox);
		searchTextBox.setWidth("448px");
		
		// Search Button
		Button searchButton = new Button();
		searchFlexTable.setWidget(0, 1, searchButton);
		searchButton.setStyleName("mgp-Button");
		searchButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				getResults();
				Window.alert(searchTextBox.getText());
			}
		});
		searchButton.setText("Search");
		
		// Advanced Search text
		Label advancedSearchText = new Label("Advanced Search... ");
		searchFlexTable.setWidget(1, 1, advancedSearchText);
		advancedSearchText.setStyleName("mgp-Link");
		
		
		// Results from search are shown in this panel
		ScrollPanel scrollPanel = new ScrollPanel();
		form.add(scrollPanel);
		scrollPanel.setSize("690px", "381px");
		
		FlexTable searchResultsPanel = new FlexTable();
		scrollPanel.setWidget(searchResultsPanel);
		searchResultsPanel.setSize("100%", "388px");
		
		
	}
	
	private void getResults() {
		//final String[] searchWords = searchTextBox.getText().split(" ");

		
		//TODO Write search algorithm
		
		

			Image userImage = new Image();
			userImage.setUrl("http://www.google.com/images/srpr/logo4w.png");
			
			Label username = new Label("Bill");
			
			username.setStyleName("mgp-Label");
			username.setSize("57px", "19px");
			
			Image sendGigImage = new Image();
			sendGigImage.setUrl("http://s.ytimg.com/yts/img/pixel-vfl3z5WfW.gif");
			
			resultsPanel.setWidget(0,0,userImage);
			resultsPanel.setWidget(0,1,username);
			resultsPanel.setWidget(1,1,sendGigImage);
	}

}
