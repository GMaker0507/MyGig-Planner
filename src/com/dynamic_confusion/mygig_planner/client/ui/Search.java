package com.dynamic_confusion.mygig_planner.client.ui;

import com.dynamic_confusion.mygig_planner.client.SearchInfo;
import com.dynamic_confusion.mygig_planner.client.UserInfo;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideServiceClientImpl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

public class Search extends Composite {
	VerticalPanel form;
	private TextBox searchTextBox;
	private ServerSideServiceClientImpl ssService;
	private ScrollPanel scrollPanel;
	
	public Search(final ServerSideServiceClientImpl ssService) {
		
		this.ssService = ssService;
		
		form = new VerticalPanel();
		initWidget(form);
		form.setSize("800px", "512px");
		
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
			}
		});
		searchButton.setText("Search");
		
		// Advanced Search text
		Label advancedSearchText = new Label("Advanced Search... ");
		advancedSearchText.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				form.clear();
				form.add(new Browse(ssService));
			}
		});
		searchFlexTable.setWidget(1, 1, advancedSearchText);
		advancedSearchText.setStyleName("mgp-Link");
		
		// Results from search are shown in this panel
		ScrollPanel scrollPanel_1 = new ScrollPanel();
		form.add(scrollPanel_1);
		scrollPanel_1.setHeight("442px");
		
		
		
		
		
		
	}
	
	private void getResults() {
		scrollPanel.clear();
		final String[] searchWords = searchTextBox.getText().split(" ");

		SearchInfo searchInfo = new SearchInfo();
		

		ssService.search(searchInfo, new AsyncCallback() {

			public void onFailure(Throwable caught) {
				String errMsg ="<p>" + caught.getMessage() + "</p>";
				scrollPanel.clear();
				scrollPanel.add(new Label(errMsg));
			}

			public void onSuccess(Object result) {
				UserInfo[] userInfo = (UserInfo[]) result;

				// Sets result of search to tables
				
				AbsolutePanel resultsPanel = new AbsolutePanel();
				scrollPanel.setWidget(resultsPanel);
	
				// Sets result of browse to tables
				int numRows = 0;
				for(int i = 0 ; i < userInfo.length ; i++) {
					Label username = new Label(userInfo[i].username);
					Label email = new Label(userInfo[i].email);
					
					for(String word : searchWords)
						if(username.getText().contains(word))
							resultsPanel.add(username, 10, 50*(numRows+5));
							resultsPanel.add(email, 200, 50*(numRows+5));
						numRows++;
				}
			}
		});
	}
}
