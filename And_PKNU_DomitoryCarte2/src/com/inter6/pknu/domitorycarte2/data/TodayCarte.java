package com.inter6.pknu.domitorycarte2.data;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.inter6.pknu.domitorycarte2.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TodayCarte {
	private String date;
	private String[] transCarte;
	
	public TodayCarte(String date, ArrayList<String> carte) {
		this.date = date;
		transCarte = null;
		
		if (carte != null) {
			transCarte = new String[3];
			
			int i = 0;
			for (String menu : carte) {
				StringTokenizer st = new StringTokenizer(menu);
				
				boolean isFirst = true;
				String refactMenu = "";
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					
					if (isFirst) {
						refactMenu += token;
						isFirst = false;
						continue;
					}
					
					// 메뉴 사이에 줄내림
					refactMenu += ("\n" + token);
				}
				
				transCarte[i] = refactMenu;
				i++;
			}
		}
	}
	
	public String getDate() {
		return date;
	}
	
	public String getCarte(int when) {
		return transCarte[when];
	}
	
	public void setView(View todayView) {
		LinearLayout ll_carte = (LinearLayout)todayView.findViewById(R.id.ll_carte);
		View carteView = ll_carte.getChildAt(0);
		
		if (carteView == null) {
			LayoutInflater layoutInflater = (LayoutInflater)todayView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			carteView = layoutInflater.inflate(R.layout.carte, null);
			ll_carte.addView(carteView);
		}
		
		TextView tv_date = (TextView)carteView.findViewById(R.id.tv_date);
		TextView tv_breakfast = (TextView)carteView.findViewById(R.id.tv_breakfast);
		TextView tv_lunch = (TextView)carteView.findViewById(R.id.tv_lunch);
		TextView tv_dinner = (TextView)carteView.findViewById(R.id.tv_dinner);
		
		tv_date.setText(getDate());
    	tv_breakfast.setText(getCarte(Const.WHEN_BREAKFAST));
    	tv_lunch.setText(getCarte(Const.WHEN_LUNCH));
    	tv_dinner.setText(getCarte(Const.WHEN_DINNER));
	}
}