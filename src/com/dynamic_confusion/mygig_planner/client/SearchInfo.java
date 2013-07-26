package com.dynamic_confusion.mygig_planner.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchInfo implements IsSerializable {
	
	public String genre = null;
	
	public Date date = null;
	
	public boolean isAvailable = false;
	public boolean hasPA = false;
	public boolean hasSoundPerson = false;
	public boolean onlyOriginalMusic = false;
	public boolean hasHospitalityPack = false;
}
