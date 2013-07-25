package com.dynamic_confusion.mygig_planner.client.ui;

import com.google.gwt.user.client.ui.Image;

public class Band {
	private String username;
	private String displayName;
	private Image profileImage;
	
	public Band(String username, String displayName) {
		this.username = username;
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setProfileImage(Image profileImage) {
		this.profileImage = profileImage;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
