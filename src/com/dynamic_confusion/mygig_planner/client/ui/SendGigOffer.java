package com.dynamic_confusion.mygig_planner.client.ui;

import com.dynamic_confusion.mygig_planner.client.UserInfo;
import com.dynamic_confusion.mygig_planner.client.ss_service.ServerSideServiceClientImpl;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class SendGigOffer extends Composite {
	AbsolutePanel form;
	public SendGigOffer(ServerSideServiceClientImpl ssService, UserInfo userInfo) {
		
		form = new AbsolutePanel();
		initWidget(form);
		form.setSize("800px", "512px");
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		form.add(absolutePanel, 10, 10);
		absolutePanel.setSize("261px", "492px");
		
		FlexTable flexTable = new FlexTable();
		absolutePanel.add(flexTable, 0, 140);
		flexTable.setSize("261px", "175px");
		
		Label lblName = new Label("Name");
		flexTable.setWidget(0, 0, lblName);
		lblName.setWidth("76px");
		
		TextBox textBoxName = new TextBox();
		textBoxName.setAlignment(TextAlignment.RIGHT);
		flexTable.setWidget(0, 1, textBoxName);
		
		Label lblPayOffer = new Label("Pay Offer");
		flexTable.setWidget(1, 0, lblPayOffer);
		
		TextBox textBoxPay = new TextBox();
		textBoxPay.setAlignment(TextAlignment.RIGHT);
		flexTable.setWidget(1, 1, textBoxPay);
		
		Label lblCapacity = new Label("Capacity");
		flexTable.setWidget(2, 0, lblCapacity);
		
		TextBox textBoxCapacity = new TextBox();
		textBoxCapacity.setAlignment(TextAlignment.RIGHT);
		flexTable.setWidget(2, 1, textBoxCapacity);
		
		
		Button btnSendGigOffer = new Button("Send Gig Offer");
		btnSendGigOffer.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				form.clear();
				form.add(new Label("Gig Offer Sent."), 400, 50);
			}
		});
		flexTable.setWidget(4, 1, btnSendGigOffer);
		
		AbsolutePanel absolutePanel_1 = new AbsolutePanel();
		form.add(absolutePanel_1, 277, 10);
		absolutePanel_1.setSize("513px", "492px");
		
		Label lblProfileName = new Label(userInfo.username);
		lblProfileName.setStyleName("ul li");
		absolutePanel_1.add(lblProfileName, 42, 48);
		lblProfileName.setSize("178px", "44px");
		
		FlexTable flexTable_1 = new FlexTable();
		absolutePanel_1.add(flexTable_1, 120, 129);
		flexTable_1.setSize("393px", "116px");
		
		Label lblGenre = new Label("Genre:");
		flexTable_1.setWidget(0, 0, lblGenre);
		Label lblProfileGenre = new Label(userInfo.genre);
		flexTable_1.setWidget(0, 1, lblProfileGenre);
		
		Label lblPayRange = new Label("PayRange:");
		flexTable_1.setWidget(1, 0, lblPayRange);
		Label lblProfilePayRange = new Label(Double.toString(userInfo.priceRange));
		flexTable_1.setWidget(1, 1, lblProfilePayRange);
		
		Label lblNeedsSoundPerson = new Label("Needs Sound Person:");
		flexTable_1.setWidget(2, 0, lblNeedsSoundPerson);
		Label lblProfileNeedsSoundPerson = new Label( (!userInfo.hasSoundPerson) ? "No" : "Yes");
		flexTable_1.setWidget(2, 1, lblProfileNeedsSoundPerson);
		
		Label lblNeedsPA = new Label("Needs PA:");
		flexTable_1.setWidget(3, 0, lblNeedsPA);
		Label lblProfileNeedsPA = new Label( !(userInfo.hasPA) ? "No" : "Yes");
		flexTable_1.setWidget(3, 1, lblProfileNeedsPA);
		
		// Set table alignments
		flexTable.getCellFormatter().setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable_1.getCellFormatter().setVerticalAlignment(2, 1, HasVerticalAlignment.ALIGN_BOTTOM);
		flexTable_1.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable_1.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable_1.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable_1.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable_1.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable_1.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable_1.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable_1.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
	}
}
