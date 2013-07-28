package com.dynamic_confusion.mygig_planner.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.CheckBox;

public class Browse extends Composite {
	private ScrollPanel scrollPanel;
	private FlexTable browseTable;
	private ServerSideServiceClientImpl ssService;
	TreeSet<Genre> genre;
	
	int minCapacity = Integer.MAX_VALUE;
	int maxPay = Integer.MAX_VALUE;
	Boolean requiresPA = false;
	Boolean requiresHospPack = false;
	Boolean original = false;
	Boolean needsSoundPerson = false;
	Boolean availableOnly = false;
	TextBox txtbxminCapacity;
	TextBox txtbxmaxPay;
	
	public Browse(ServerSideServiceClientImpl service) {
		
		ssService = service;
		genre = new TreeSet<Genre>();
		
		
		// Main display
		FlexTable form = new FlexTable();
		initWidget(form);
		form.setSize("800px", "512px");
		
		// Table displaying browse criteria
		browseTable = new FlexTable();
		form.setWidget(0, 0, browseTable);
		browseTable.setSize("250px", "490px");
		
		// Browse Label
		Label browseLabel = new Label("Browse");
		browseLabel.setStyleName("mgp-Label");
		browseTable.setWidget(0, 0, browseLabel);
		
		Label lblBackToSearch = new Label("Back to Search...");
		browseTable.setWidget(1, 0, lblBackToSearch);
		
		// Panel which includes browse options
		AbsolutePanel absolutePanel = new AbsolutePanel();
		browseTable.setWidget(2, 0, absolutePanel);
		absolutePanel.setSize("120px", "512px");
		
		// TODO Add browse options
		
		// Browse button
		Button browseButton = new Button("Browse");
		browseButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getResults();
			}
		});
		browseButton.setStyleName("mgp-Button");
		absolutePanel.add(browseButton, 10, 474);
		
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
			chckbxRock.setSize("72px", "20px");
			chckbxRock.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(genre.contains(Genre.ROCK))
						genre.remove(Genre.ROCK);
					else genre.add(Genre.ROCK);
				}
			});
			
			CheckBox chckbxPop = new CheckBox("Pop");
			absolutePanel.add(chckbxPop, 10, 130);
			chckbxPop.setSize("72px", "20px");
			chckbxPop.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(genre.contains(Genre.POP))
						genre.remove(Genre.POP);
					else genre.add(Genre.POP);
				}
			});
			
			CheckBox chckbxCountry = new CheckBox("Country");
			absolutePanel.add(chckbxCountry, 10, 150);
			chckbxCountry.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(genre.contains(Genre.COUNTRY))
						genre.remove(Genre.COUNTRY);
					else genre.add(Genre.COUNTRY);
				}
			});

			CheckBox chckbxSka = new CheckBox("Ska");
			absolutePanel.add(chckbxSka, 10, 170);
			chckbxSka.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(genre.contains(Genre.SKA))
						genre.remove(Genre.SKA);
					else genre.add(Genre.SKA);
				}
			});
			
			CheckBox chckbxReggae = new CheckBox("Reggae");
			absolutePanel.add(chckbxReggae, 10, 190);
			chckbxReggae.setSize("100px", "20px");
			chckbxReggae.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(genre.contains(Genre.REGGAE))
						genre.remove(Genre.REGGAE);
					else genre.add(Genre.REGGAE);
				}
			});

			CheckBox chckbxBluegrass = new CheckBox("Bluegrass");
			absolutePanel.add(chckbxBluegrass, 10, 210);
			chckbxBluegrass.setSize("110px", "20px");
			chckbxBluegrass.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(genre.contains(Genre.BLUEGRASS))
						genre.remove(Genre.BLUEGRASS);
					else genre.add(Genre.BLUEGRASS);
				}
			});

			CheckBox chckbxHiphop = new CheckBox("Hiphop");
			absolutePanel.add(chckbxHiphop, 10, 230);
			chckbxHiphop.setSize("100px", "20px");
			chckbxHiphop.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(genre.contains(Genre.HIPHOP))
						genre.remove(Genre.HIPHOP);
					else genre.add(Genre.ROCK);
				}
			});

			CheckBox chckbxIndustrial = new CheckBox("Industrial");
			absolutePanel.add(chckbxIndustrial, 10, 250);
			chckbxIndustrial.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(genre.contains(Genre.INDUSTRIAL))
						genre.remove(Genre.INDUSTRIAL);
					else genre.add(Genre.INDUSTRIAL);
				}
			});

			CheckBox chckbxRockabilly = new CheckBox("Rockabilly");
			absolutePanel.add(chckbxRockabilly, 10, 270);
			chckbxRockabilly.setSize("110px", "20px");
			chckbxRockabilly.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					if(genre.contains(Genre.ROCKABILLY))
						genre.remove(Genre.ROCKABILLY);
					else genre.add(Genre.ROCKABILLY);
				}
			});
		}
		
		{
			Label labelminCapacity = new Label("Minimum Capacity");
			absolutePanel.add(labelminCapacity, 10, 296);
			txtbxminCapacity = new TextBox();
			absolutePanel.add(txtbxminCapacity, 10, 312);
			txtbxminCapacity.setSize("90px", "12px");
			
			Label labelminPay = new Label("Minimum Pay");
			absolutePanel.add(labelminPay, 10, 336);
			txtbxmaxPay = new TextBox();
			absolutePanel.add(txtbxmaxPay, 10, 356);
			txtbxmaxPay.setSize("90px", "8px");
		
			CheckBox chckbxrequiresPA = new CheckBox("Requires PA");
			absolutePanel.add(chckbxrequiresPA, 10, 388);
			
			CheckBox chckbxrequiresHospPack = new CheckBox("Requires Hospitality Package");
			chckbxrequiresHospPack.setHTML(" Requires Hospitality Package");
			absolutePanel.add(chckbxrequiresHospPack, 10, 408);
			chckbxrequiresHospPack.setSize("219px", "18px");
			
			CheckBox chckbxoriginal = new CheckBox("Original Band");
			absolutePanel.add(chckbxoriginal, 10, 428);
		
			CheckBox chckbxneedsSoundPerson = new CheckBox("Needs Sound Person");
			absolutePanel.add(chckbxneedsSoundPerson, 10, 448);
			chckbxneedsSoundPerson.setSize("190px", "18px");
		}
		
		
		// Scroll panel displaying browse results
		scrollPanel = new ScrollPanel();
		form.setWidget(0, 1, scrollPanel);
		scrollPanel.setSize("750px", "490px");
		
	}
	
	private void getResults() {
		
		SearchInfo searchInfo = new SearchInfo();
		if( txtbxminCapacity.getValue().equals("") )
			minCapacity = 0;
		if( txtbxmaxPay.getValue().equals("") )
			maxPay = 0;


		ssService.search(searchInfo, new AsyncCallback() {

			public void onFailure(Throwable caught) {
				String errMsg ="<p>" + caught.getMessage() + "</p>";
				scrollPanel.setWidget(new Label(errMsg));
			}

			public void onSuccess(Object result) {
				UserInfo[] userInfo = (UserInfo[]) result;

				// Sets result of browse to tables
				for(int i = 0 ; i < userInfo.length ; i++) {
					if( genre.contains(userInfo[i].genre) || genre.isEmpty() ) {
						if( userInfo[i].capacity >= minCapacity )
							if( userInfo[i].priceRange <= maxPay )
								if(requiresPA && userInfo[i].hasPA)
									if( !(original && !userInfo[i].onlyOriginalMusic) )
										if( !(needsSoundPerson && !userInfo[i].hasSoundPerson) )
											browseTable.setWidget(2*i, 2, new Label(userInfo[i].username));
											browseTable.setWidget(2*i+1, 2, new Label(userInfo[i].email));
					
					}
				}
			}
		});
	}
}
