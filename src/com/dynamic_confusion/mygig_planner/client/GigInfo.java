package com.dynamic_confusion.mygig_planner.client;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

public class GigInfo implements Serializable{
	
	public String key;		
	public String name;	
	public String sendUser;	
	public String description;	
	public String recipientUser;
	
	public float cost;	
	
	public int status = 0;
	
	public Date dateSent;	
	public Date dateReplied;
	public Date[] performanceDates;
	
	public String getEntityName() {
		// TODO Auto-generated method stub
		return sendUser+recipientUser+name+dateSent.toString();
	}

	public String toString(){
		
		return "Gig offer by "+sendUser+
				" to "+recipientUser+", on "+dateSent+
				(status==0 ? ". No reply yet!" :
				(status==1 ? "Accepted on "+dateReplied.toString() :
				"Rejected on "+dateReplied.toString()));
	}
}
