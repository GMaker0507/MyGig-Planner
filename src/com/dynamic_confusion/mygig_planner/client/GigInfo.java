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
	public int sort = 0;
	
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

	public String toLogbookString(String user) {
		
		if(sort==0){
			if(user.equals(sendUser))return "You made an offer to "+recipientUser+" on "+dateSent;
			else return "You recieved an offer from "+sendUser+" on "+dateSent;
		}else{
			if(status==1) {
				if(user.equals(recipientUser))return "You accepted "+recipientUser+"'s offer on "+dateReplied;
				else return recipientUser+" accepted your offer on "+dateReplied;
			}
			else {
				if(user.equals(recipientUser)) return "You rejected "+sendUser+"'s offer on "+dateReplied;
				else return recipientUser+" rejected your offer on "+dateReplied;
			}
		}
		
	}

	public GigInfo logbookClone(int i) {
		
		
		GigInfo gi = new GigInfo();
		
		gi.sort = i;
		
		gi.name=this.name;
		gi.key = this.key;
		gi.sendUser = this.sendUser;
		gi.recipientUser= this.recipientUser;
		gi.dateSent = this.dateSent;
		gi.dateReplied = this.dateReplied;
		gi.cost = this.cost;
		gi.status = this.status;
		gi.description = this.description;
		
		// TODO Auto-generated method stub
		return gi;
	}
}
