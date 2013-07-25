package com.dynamic_confusion.mygig_planner.client.ui;

import com.dynamic_confusion.mygig_planner.client.SearchInfo;
import com.dynamic_confusion.mygig_planner.client.UserInfo;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideServiceClientImpl;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
	private FlexTable searchTable;
	private ServerSideServiceClientImpl ssService;
	
	public Search(ServerSideServiceClientImpl ssService) {
		
		this.ssService = ssService;
		
		VerticalPanel form = new VerticalPanel();
		initWidget(form);
		form.setSize("870px", "490px");
		
		form.add(searchTable);
		searchTable.setSize("700px", "700px");
		
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
		final String[] searchWords = searchTextBox.getText().split(" ");

		SearchInfo searchInfo = new SearchInfo();
		// TODO populate field

		ssService.search(searchInfo, new AsyncCallback() {

			public void onFailure(Throwable caught) {
				String errMsg ="<p>" + caught.getMessage() + "</p>";
				searchTable.clear();
				searchTable.add(new Label(errMsg));
			}

			public void onSuccess(Object result) {
				UserInfo[] userInfo = (UserInfo[]) result;

				// Sets result of search to tables
				int numRow = 0;
				for(int i = 0 ; i < userInfo.length ; i++) {
					for(String word : searchWords) {
						// Displays result if username contains searched word
						if(userInfo[i].username.contains(word)) {
							searchTable.setWidget(2*numRow, 0, new Label(userInfo[i].username));
							searchTable.setWidget(2*numRow+1, 0, new Label(userInfo[i].email));
							numRow++;
							break;
						}
					}
				}
			}
		});
	}
}
