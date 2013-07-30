package com.dynamic_confusion.mygig_planner.client;

import java.io.Serializable;
import java.util.Date;

import com.dynamic_confusion.mygig_planner.client.ui.Genre;

public class UserInfo implements Serializable {

	public String username = "username";
	public String password = "password";	
	public String email = "email";
	public String type = "musician";
	
	public String firstName = "first";
	public String lastName = "last";
	
	public int gigCount = 0;
	public Date dateJoined = new Date();
	
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
		
		dateJoined = new Date();
		
		dateJoined.setHours(0);
		dateJoined.setMinutes(0);
		dateJoined.setSeconds(0);
	}
	
	public void randomize(){
		
		// Set the joined date
		dateJoined.setYear(100+(int)Math.floor(Math.random()*13.0));
		dateJoined.setMonth(1+(int)Math.floor(Math.random()*12.0));
		dateJoined.setDate(1+(int)Math.floor(Math.random()*30.0));
		
		// Dont add any hours or minutes
		dateJoined.setHours(0);
		dateJoined.setMinutes(0);
		dateJoined.setSeconds(0);

		gigCount = (int)Math.floor(Math.random()*25.0);
		
		priceRange = 1005 + Math.random() * 1000;
		
		phoneNumber = (100+(int)Math.floor(Math.random()*899.0))+"-"+
					  (100+(int)Math.floor(Math.random()*899.0))+"-"+
					  (1000+(int)Math.floor(Math.random()*8999.0));
		
		capacity = 25 + (int)Math.floor(Math.random() * 5000.0);
		
		hasPA = Math.random() < 0.5;
		hasSoundPerson = Math.random() < 0.5;
		onlyOriginalMusic = Math.random() < 0.5;
		hasHospitalityPack = Math.random() < 0.5;
		
		String[] firsts = new String[]{
				"Bill","Robert","Jason","Johnny","Frank","Eric","Marcus","David","Will","John","Bill",
				"George","Mike","Allen","Larry","Joe","Bob","Rashard","David","Travis","Ashely","Kimberly","Kim","Jermy","Theodore","Teddy","Donald","Joel"};
		String[] lasts = new String[]{
				"Clinton","Bush","Johnson","Smith","Hobbs","Lin","Jennings","West","Vick","Williams","Ma","Jenkins","Bryant","O'neal",
				"Morrow","Blankenship","Jones","Jackson","Thorton","Baley","Woods","White"};
		
		firstName = firsts[(int) ((int)(Math.random()*((double)firsts.length)))];
		lastName = lasts[(int) ((int)(Math.random()*((double)lasts.length)))];
		
		String[] emails1 = new String[]{"a","g","b","c",firstName.toLowerCase()};
		String[] emails2 = new String[]{"Game","Bot","Girl","Man","Freak","Sauce",lastName,"Baller","Dancer"};
		String[] emails4 = new String[]{"hotmail","gmail","live","yahoo","aol"};
		
		String emails3 = ((int)Math.floor(Math.random()*9999.0))+"";
		
		genre = Genre.GetRandomGenre();
		
		type = Math.random() > 0.5 ? "venue" : "musician";
		
		email = emails1[(int) ((int)(Math.random()*((double)emails1.length)))]+
				emails2[(int) ((int)(Math.random()*((double)emails2.length)))]+emails3+"@"+
				emails4[(int) ((int)(Math.random()*((double)emails4.length)))]+".com";
		
		username = emails1[(int) ((int)(Math.random()*((double)emails1.length)))]+
				emails2[(int) ((int)(Math.random()*((double)emails2.length)))]+emails3;
		
		int h = (int)(Math.random()*((double)11))+1;
		
		int m1 = (int)(Math.random()*((double)9));
		int m2 = (int)(Math.random()*((double)9));
		

		int h2 = (int)(Math.random()*((double)11))+1;
		
		int m12 = (int)(Math.random()*((double)9));
		int m22 = (int)(Math.random()*((double)9));
		
		openHours = h+":"+m1+""+m2+(Math.random()>0.5?"AM":"PM") +" - "+
					h2+":"+m12+""+m22+(Math.random()>0.5?"AM":"PM");
	
		password = "password";
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
