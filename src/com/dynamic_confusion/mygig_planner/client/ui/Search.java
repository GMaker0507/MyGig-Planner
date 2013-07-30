package com.dynamic_confusion.mygig_planner.client.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dynamic_confusion.mygig_planner.client.SearchInfo;
import com.dynamic_confusion.mygig_planner.client.UserInfo;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideServiceClientImpl;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.datepicker.client.DatePicker;

public class Search extends Composite {
	VerticalPanel form;
	private TextBox searchTextBox;
	private ServerSideServiceClientImpl ssService;
	private ScrollPanel scrollPanel;
	
	private Button searchButton = null;

	private CheckBox chckbxRock = null;
	private CheckBox chckbxPop = null;
	private CheckBox chckbxCountry = null;
	private CheckBox chckbxSka = null;
	private CheckBox chckbxReggae = null;
	private CheckBox chckbxBluegrass = null;
	private CheckBox chckbxHipHop = null;
	private CheckBox chckbxIndustrial = null;
	private CheckBox chckbxRockabilly = null;
	private CheckBox checkBox_9 = null;
	private CheckBox chckbxHasSoundPerson = null;
	private CheckBox chckbxOnlyOriginal = null;
	private CheckBox chckbxHasHospitality = null;
	private CheckBox chckbxHasPA = null;
	private TextBox txtMinimumPay = null;
	private TextBox txtMinimumCapacity = null;
	
	private DatePicker datePicker = null;
	
	private AbsolutePanel resultsPanel = null;
	
	private RadioButton radAvailable = null;
	private RadioButton radNotAvailable = null;
	
	private TabPanel tpParent = null;
	
	public Search(final ServerSideServiceClientImpl ssService, TabPanel tpParent) {
		
		this.ssService = ssService;
		this.tpParent = tpParent;
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		initWidget(absolutePanel);
		absolutePanel.setSize("800px", "612px");
		
		Label label = new Label("Available");
		absolutePanel.add(label, 10, 39);
		
		ClickHandler ch = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				getResults();
			}
			
		};
		
		ValueChangeHandler<Boolean> vch = new ValueChangeHandler<Boolean>(){

			@Override
			public void onValueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				getResults();
			}
			
			
		};
		
		ValueChangeHandler<String> vch2 = new ValueChangeHandler<String>(){

			@Override
			public void onValueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				getResults();
			}
			
			
		};
		
		ValueChangeHandler<Date> vch3 = new ValueChangeHandler<Date>(){

			@Override
			public void onValueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				getResults();
			}
			
			
		};
		
		datePicker = new DatePicker();
		absolutePanel.add(datePicker,20,20);
		datePicker.addValueChangeHandler(vch3);
		
		int h = 150;
		
		radNotAvailable = new RadioButton("available");
		radNotAvailable.setText("Not Available");
		radNotAvailable.setFormValue("available");
		absolutePanel.add(radNotAvailable, 20, 61+h);
		radNotAvailable.setSize("110px", "20px");
		radNotAvailable.addValueChangeHandler(vch);
		
		radAvailable= new RadioButton("available");
		radAvailable.setText("Available Only");
		radAvailable.setFormValue("available");
		absolutePanel.add(radAvailable, 20, 87+h);
		radAvailable.setSize("215px", "25");
		radAvailable.addValueChangeHandler(vch);
		
		Label label_1 = new Label("Genre");
		absolutePanel.add(label_1, 10, 122+h);
		
		chckbxRock = new CheckBox("Rock");
		chckbxRock.setName("rock");
		absolutePanel.add(chckbxRock, 20, 146+h);
		chckbxRock.setSize("72px", "20px");
		chckbxRock.addValueChangeHandler(vch);
		
		chckbxPop = new CheckBox("Pop");
		chckbxPop.setName("pop");
		absolutePanel.add(chckbxPop, 20, 166+h);
		chckbxPop.setSize("72px", "20px");
		chckbxPop.addValueChangeHandler(vch);
		
		chckbxCountry = new CheckBox("Country");
		chckbxCountry.setName("country");
		absolutePanel.add(chckbxCountry, 20, 186+h);
		chckbxCountry.addValueChangeHandler(vch);
		
		chckbxSka = new CheckBox("Ska");
		chckbxSka.setName("ska");
		absolutePanel.add(chckbxSka, 20, 206+h);
		chckbxSka.addValueChangeHandler(vch);
		
		chckbxReggae = new CheckBox("Reggae");
		chckbxReggae.setName("reggae");
		absolutePanel.add(chckbxReggae, 20, 226+h);
		chckbxReggae.setSize("100px", "20px");
		chckbxReggae.addValueChangeHandler(vch);
		
		chckbxBluegrass = new CheckBox("Bluegrass");
		chckbxBluegrass.setName("rock");
		absolutePanel.add(chckbxBluegrass, 20, 246+h);
		chckbxBluegrass.setSize("110px", "20px");
		chckbxBluegrass.addValueChangeHandler(vch);
		
		chckbxHipHop = new CheckBox("Hiphop");
		absolutePanel.add(chckbxHipHop, 20, 266+h);
		chckbxHipHop.setSize("100px", "20px");
		chckbxHipHop.addValueChangeHandler(vch);
		chckbxHipHop.setName(Genre.HIPHOOP);
		
		chckbxIndustrial = new CheckBox("Industrial");
		chckbxIndustrial.setName("industrial");
		absolutePanel.add(chckbxIndustrial, 20, 286+h);
		chckbxIndustrial.setSize("100px", "20px");
		chckbxIndustrial.addValueChangeHandler(vch);
		
		chckbxRockabilly = new CheckBox("Rockabilly");
		chckbxRockabilly.setName("rockabilly");
		absolutePanel.add(chckbxRockabilly, 20, 306+h);
		chckbxRockabilly.setSize("110px", "20px");
		chckbxRockabilly.addValueChangeHandler(vch);

		
		Label label_2 = new Label("Requirements:");
		absolutePanel.add(label_2, 10, 332+h);
		
		
		chckbxHasPA = new CheckBox("Requires PA");
		absolutePanel.add(chckbxHasPA, 20, 352+h);
		chckbxHasPA.addValueChangeHandler(vch);
		
		chckbxHasHospitality = new CheckBox("Hospitality Package");
		chckbxHasHospitality.setHTML("Hospitality Package");
		absolutePanel.add(chckbxHasHospitality, 20, 372+h);
		chckbxHasHospitality.setSize("231px", "18px");
		chckbxHasHospitality.addValueChangeHandler(vch);
		
		chckbxOnlyOriginal = new CheckBox("Original Band");
		absolutePanel.add(chckbxOnlyOriginal, 20, 392+h);
		chckbxOnlyOriginal.addValueChangeHandler(vch);
		
		chckbxHasSoundPerson = new CheckBox("Sound Person");
		absolutePanel.add(chckbxHasSoundPerson, 20, 414+h);
		chckbxHasSoundPerson.setSize("190px", "18px");
		chckbxHasSoundPerson.addValueChangeHandler(vch);
		
		// Holds the textbox and search button
		FlexTable searchFlexTable = new FlexTable();
		absolutePanel.add(searchFlexTable, 219, 8);
		searchFlexTable.setSize("571px", "62px");
		
		// Search Textbox
		searchTextBox = new TextBox();
		searchFlexTable.setWidget(0, 0, searchTextBox);
		searchTextBox.setWidth("448px");
		
		// Search Button
		searchButton = new Button();
		searchFlexTable.setWidget(0, 1, searchButton);
		searchButton.setStyleName("mgp-Button");
		searchButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				
				searchButton.setText("Processing...");
				searchButton.setEnabled(false);
				
				getResults();
				
			}
		});
		
		searchButton.setText("Search");
		
		// Search Title label
		Label searchLabel = new Label("Search");
		absolutePanel.add(searchLabel);
		searchLabel.setStyleName("mgp-Label");
		searchLabel.setSize("57px", "19px");
		
		
		
		ScrollPanel scrollPanel_1 = new ScrollPanel();
		absolutePanel.add(scrollPanel_1, 219, 86);
		scrollPanel_1.setSize("600px", "502px");

		scrollPanel_1.setStyleName("scroller");

		resultsPanel = new AbsolutePanel();
		
		scrollPanel_1.setWidget(resultsPanel);
		
		scrollPanel = scrollPanel_1;
		
		getResults();
	}
	
	private void getResults() {
		
		// Clear the resuls panel
		resultsPanel.clear();
		
		resultsPanel.add(new HTML("<p>Searching...</p>"));
		
		final String[] searchWords = searchTextBox.getText().split(",||(\\s)");
		
		for(int i=0;i<searchWords.length;i++){
			
			System.out.println("Word "+(i+1)+": "+searchWords[i]);
		}

		SearchInfo searchInfo = new SearchInfo();
		
		List<String> searchGenres = new ArrayList<String>();
		
		if(chckbxBluegrass.getValue())searchGenres.add(Genre.BLUEGRASS.toLowerCase());
		if(chckbxCountry.getValue())searchGenres.add(Genre.COUNTR.toLowerCase());
		if(chckbxRock.getValue())searchGenres.add(Genre.ROCK.toLowerCase());
		if(chckbxSka.getValue())searchGenres.add(Genre.SKA.toLowerCase());
		if(chckbxReggae.getValue())searchGenres.add(Genre.REGGAE.toLowerCase());
		if(chckbxHipHop.getValue())searchGenres.add(Genre.HIPHOOP.toLowerCase());
		if(chckbxIndustrial.getValue())searchGenres.add(Genre.INDUSTRIAL.toLowerCase());
		if(chckbxRockabilly.getValue())searchGenres.add(Genre.ROCKABILLY.toLowerCase());
		
		searchInfo.hasHospitalityPack = chckbxHasHospitality.getValue();
		searchInfo.hasPA= chckbxHasPA.getValue();
		searchInfo.onlyOriginalMusic = chckbxOnlyOriginal.getValue();
		searchInfo.hasSoundPerson = chckbxHasSoundPerson.getValue();
		
		if(radAvailable.getValue())searchInfo.date = datePicker.getValue();
		
		searchInfo.genre= new String[searchGenres.size()];
		
		for(int i=0;i<searchGenres.size();i++){
			
			searchInfo.genre[i] = searchGenres.get(i);
		}
		
		
		final Button searchButtonFinal = searchButton;		
		final TabPanel tpParentFinal = tpParent;

		ssService.search(searchInfo, new AsyncCallback() {

			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				// Clear the resuls panel
				resultsPanel.clear();
				
				searchButtonFinal.setText("Search");
				searchButtonFinal.setEnabled(true);		
			}

			public void onSuccess(Object result) {
				final UserInfo[] userInfo = (UserInfo[]) result;
				// Clear the resuls panel
				resultsPanel.clear();
				
				searchButtonFinal.setText("Search");
				searchButtonFinal.setEnabled(true);				

				// Sets result of search to tables
				
				scrollPanel.setWidget(resultsPanel);
	
				// Sets result of browse to tables
				int numRows = 0;
				for(int i = 0 ; i < userInfo.length ; i++) {
					Anchor username = new Anchor(userInfo[i].username);
					Label email = new Label(userInfo[i].email);

					final String strUsername = userInfo[i].username;
					username.addClickHandler(new ClickHandler(){

						@Override
						public void onClick(ClickEvent event) {
							
							final ViewEditProfile vep = (ViewEditProfile)tpParentFinal.getWidget(1);
							
							ssService.getUser(strUsername, new AsyncCallback(){

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									Window.alert(caught.getMessage());
								}

								@Override
								public void onSuccess(Object result) {
									// TODO Auto-generated method stub
									UserInfo ui = (UserInfo)result;
									
									if(result==null)Window.alert("No user found!");
									else vep.showUser = ui;
									
									// TODO Auto-generated method stub
									tpParentFinal.selectTab(1,true);
								}
								
								
							});
						}
						
						
						
					});
					
					
					// Create Send Gig Offer button
					Button btnsendGigOffer = new Button("Send Gig Offer");
					final int userNum = i;
					
					btnsendGigOffer.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							
							String getVariables = "action=send&"+
												  "name=gig&"+
												  "sendUser="+Cookies.getCookie("activeUser")+"&"+
												  "recipientUser="+strUsername;

							RequestBuilder rb = new RequestBuilder(RequestBuilder.GET,"/servlet/gig?"+getVariables);
							
							try {
								rb.sendRequest(getVariables, new RequestCallback(){

									@Override
									public void onResponseReceived(Request request,
											Response response) {
										// TODO Auto-generated method stub
										String results = response.getText();

										// Tell the user whats up
										Window.alert(results);
									}

									@Override
									public void onError(Request request,
											Throwable exception) {
										// TODO Auto-generated method stub
										Window.alert(exception.getMessage());
									}
									
									
								});
							} catch (RequestException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Window.alert(e.getMessage());
							}
						}
					});
					
					VerticalPanel vp = new VerticalPanel();
					
					vp.setSize("100%", "64px");
					
					Grid cUserGrid = new Grid(1,3);
					cUserGrid.setSize("100%","30px");
					
					cUserGrid.setWidget(0, 0, username);
					cUserGrid.setWidget(0, 1, email);
					cUserGrid.setWidget(0, 2, btnsendGigOffer);
					
					Grid detailsGrid = new Grid(1,5);
					detailsGrid.setSize("100%","30px");
					
					detailsGrid.setHTML(0, 0, "<p><span style=\"font-weight:bold\">Genre:</span> "+userInfo[i].genre+"</p>");
					detailsGrid.setHTML(0, 1, "<p><span style=\"font-weight:bold\">PA:</span> "+userInfo[i].hasPA+"</p>");
					detailsGrid.setHTML(0, 2, "<p><span style=\"font-weight:bold\">Original Music:</span> "+userInfo[i].onlyOriginalMusic+"</p>");
					detailsGrid.setHTML(0, 3, "<p><span style=\"font-weight:bold\">Sound Person:</span> "+userInfo[i].hasSoundPerson+"</p>");
					detailsGrid.setHTML(0, 4, "<p><span style=\"font-weight:bold\">Hospitality:</span> "+userInfo[i].hasHospitalityPack+"</p>");
					
					vp.add(cUserGrid);
					vp.add(detailsGrid);
					
					resultsPanel.add(vp);
				}
			}
		});
	}
}
