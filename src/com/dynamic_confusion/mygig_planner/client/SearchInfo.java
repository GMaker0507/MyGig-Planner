package com.dynamic_confusion.mygig_planner.client;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchInfo implements IsSerializable {
	
	public String genre = "";
	
	public Date date = null;
	
	public boolean isAvailable = false;
	public boolean hasPA = false;
	public boolean hasSoundSystem = false;
	public boolean originalMusic = false;
	public boolean hasHospitalityPack = false;
}
