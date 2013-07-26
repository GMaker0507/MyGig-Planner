package com.dynamic_confusion.mygig_planner.client.ui;

/*
public enum GenreEnum {
	ROCK, COUNTR, METAL, PUNK, SKA, REGGAE, BLUEGRASS, POP, HIPHOOP, INDUSTRIAL, ROCKABILLY
}
*/

public class Genre{
	public static String ROCK="Rock";
	public static String COUNTR="Country";
	public static String METAL="Metal";
	public static String PUNK="Pink";
	public static String SKA="Ska";
	public static String REGGAE="Reggae";
	public static String BLUEGRASS="Bluegrass";
	public static String POP="Pop";
	public static String HIPHOOP="Hip-Hop";
	public static String INDUSTRIAL="Industrial";
	public static String ROCKABILLY="Rockabilly";
	
	public static String GetRandomGenre(){
		
		String[] allGenres = new String[]{
			ROCK,COUNTR, METAL, PUNK, SKA, REGGAE, BLUEGRASS, POP,HIPHOOP, INDUSTRIAL, ROCKABILLY	
		};
		
		return allGenres[(int)Math.floor(Math.random()*((double)allGenres.length))];
	}
	
}