package com.dynamic_confusion.mygig_planner.client;

import java.io.Serializable;

import com.dynamic_confusion.mygig_planner.client.ui.Genre;

public class UserInfo implements Serializable {

	public String username = "username";
	public String password = "password";
	
	public String email = "email";
	
	public String firstName = "first";
	public String lastName = "last";
	
	public Double priceRange = 0.0;
	public String phoneNumber = "123-555-1234";
	public int capacity = 0;
	public String openHours = "";
	
	public String genre = null;
	
	public boolean hasPA = false;
	public boolean hasSoundPerson = false;
	public boolean onlyOriginalMusic = false;
	public boolean hasHospitalityPack = false;
	
	public UserInfo(){
	}
	
	public UserInfo(String username) {
		// TODO Auto-generated constructor stub
		
		this.username = username;
	}

	public static String[] getFields() {
		// TODO Auto-generated method stub
		return new String[]{"username","password","email"};
	}

	public Object getField(String field) {
		// TODO Auto-generated method stub
		if(field.equals("username"))return username;
		if(field.equals("password"))return password;
		if(field.equals("email"))return email;
		if(field.equals("genre"))return genre;
		return null;
	}

}
