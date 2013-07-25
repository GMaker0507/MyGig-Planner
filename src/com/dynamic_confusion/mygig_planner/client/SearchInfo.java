package com.dynamic_confusion.mygig_planner.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchInfo implements IsSerializable {

	public String searchString = "";
	
	public int genre = 0;
	public boolean available = false;
	
	public SearchInfo(){
		
	}
	
	public SearchInfo(String str){
		
		searchString = str;
	}
}
