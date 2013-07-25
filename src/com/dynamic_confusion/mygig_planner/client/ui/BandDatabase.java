package com.dynamic_confusion.mygig_planner.client.ui;

import java.util.ArrayList;

public class BandDatabase {

	private ArrayList<Band> database;
	
	
	public BandDatabase() {
		database = new ArrayList<Band>();
		database.add(new Band("skrillex", "Skrillex"));
		database.add(new Band("corn", "Corn"));
		database.add(new Band("zork", "Zork"));
	}
	
	public Integer getSize() {
		return database.size();
	}
	
	public Band[] getBands() {
		return (Band[]) database.toArray();
	}
}
