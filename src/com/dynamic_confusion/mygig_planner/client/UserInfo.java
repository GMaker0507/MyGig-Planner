package com.dynamic_confusion.mygig_planner.client;

import java.io.Serializable;

public class UserInfo implements Serializable {

	public String username = "username";
	public String password = "password";
	
	public String email = "email";
	
	public String description = "We are a band!";
	
	public String[] requirements = null;
	public String[] genres = null;
	
	public UserInfo(){
	}
	
	public UserInfo(String username) {
		// TODO Auto-generated constructor stub
		
		this.username = username;
	}

	public static String[] getFields() {
		// TODO Auto-generated method stub
		return new String[]{"username","password","email","description"};
	}

	public Object getField(String field) {
		// TODO Auto-generated method stub
		if(field.equals("username"))return username;
		if(field.equals("password"))return password;
		if(field.equals("email"))return email;
		if(field.equals("description"))return description;
		if(field.equals("genres"))return genres;
		return null;
	}

}
