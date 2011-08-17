package com.inter6.pknu.domitorycarte.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.inter6.pknu.domitorycarte.R;

public class WeeklyCarteAdapter extends ArrayAdapter<TodayCarte> {
	public WeeklyCarteAdapter(Context context, int textViewResourceId, WeeklyCarte weeklyCarte) {
		super(context, textViewResourceId, weeklyCarte.getCarte());
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// return super.getView(position, convertView, parent);
		
		View carteView = convertView;
		
		if (carteView == null) {
			LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			carteView = layoutInflater.inflate(R.layout.carte, null);
		}
		
		TodayCarte todayCarte = getItem(position);
		
		if (todayCarte != null) {
			TextView tv_date = (TextView)carteView.findViewById(R.id.tv_week_date);
			TextView tv_breakfast = (TextView)carteView.findViewById(R.id.tv_week_breakfast);
			TextView tv_lunch = (TextView)carteView.findViewById(R.id.tv_week_lunch);
			TextView tv_dinner = (TextView)carteView.findViewById(R.id.tv_week_dinner);
			
			tv_date.setText(todayCarte.getDate());
			tv_breakfast.setText(todayCarte.getCarte(Const.WHEN_BREAKFAST));
			tv_lunch.setText(todayCarte.getCarte(Const.WHEN_LUNCH));
			tv_dinner.setText(todayCarte.getCarte(Const.WHEN_DINNER));
		}
		
		return carteView;
	}
}