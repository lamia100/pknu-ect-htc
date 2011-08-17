package com.inter6.pknu.domitorycarte.data;

import java.util.ArrayList;

public class WeeklyCarte {
	private String week;
	private ArrayList<TodayCarte> carte;
	
	public WeeklyCarte(String week, ArrayList<TodayCarte> carte) {
		this.week = week;
		this.carte = carte;
	}
	
	public String getWeek() {
		return week;
	}
	
	public ArrayList<TodayCarte> getCarte() {
		return carte;
	}
}
