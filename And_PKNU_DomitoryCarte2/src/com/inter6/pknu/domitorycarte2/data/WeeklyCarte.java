package com.inter6.pknu.domitorycarte2.data;

import java.util.ArrayList;

import com.inter6.pknu.domitorycarte2.R;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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
	
	public void setView(View weeklyView) {
		TextView tv_week = (TextView)weeklyView.findViewById(R.id.tv_week);
		tv_week.setText(getWeek());
    	
		ListView lv_today_list = (ListView)weeklyView.findViewById(R.id.lv_carte);
		lv_today_list.setAdapter(new WeeklyCarteAdapter(weeklyView.getContext(), R.layout.carte, this));
	}
}