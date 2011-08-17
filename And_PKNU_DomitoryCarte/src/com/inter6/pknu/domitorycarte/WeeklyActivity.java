package com.inter6.pknu.domitorycarte;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.inter6.pknu.domitorycarte.data.Const;
import com.inter6.pknu.domitorycarte.data.WeeklyCarte;
import com.inter6.pknu.domitorycarte.data.WeeklyCarteAdapter;
import com.inter6.pknu.domitorycarte.logic.ParseCarte;

public class WeeklyActivity extends ListActivity {
	private RadioGroup rg_where;
	
	private TextView tv_week;
	
	private Button bt_changeToToday;
	private Button bt_refresh;
	
	private int where;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.week);
		
		rg_where = (RadioGroup)findViewById(R.id.rg_week_where);
        rg_where.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_week_whereD:
					where = Const.WHERE_D;
					break;
				case R.id.rb_week_whereY:
					where = Const.WHERE_Y;
					break;
				}
				
				refreshWeeklyCarte();
			}
		});
		
        tv_week = (TextView)findViewById(R.id.tv_week_week);
        
        bt_changeToToday = (Button)findViewById(R.id.bt_week_changeToToday);
        bt_changeToToday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeToToday();
			}
		});
        
        bt_refresh = (Button)findViewById(R.id.bt_week_refresh);
        bt_refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				refreshWeeklyCarte();
			}
		});
        
        Bundle fromTodayBundle = getIntent().getExtras();
        
        if (fromTodayBundle != null) {
        	where = fromTodayBundle.getInt("where");
        }
        
        switch (where) {
        case Const.WHERE_D:
        	rg_where.check(R.id.rb_week_whereD);
        	break;
        case Const.WHERE_Y:
        	rg_where.check(R.id.rb_week_whereY);
        	break;
        }
	}

	private void changeToToday() {
		Intent toTodayIntent = new Intent(WeeklyActivity.this, TodayActivity.class);
		toTodayIntent.putExtra("where", where);
    	
		startActivity(toTodayIntent);
		
		finish();
	}
	
	private void refreshWeeklyCarte() {
		WeeklyCarte weeklyCarte = null;
    	
    	switch (where) {
    	case Const.WHERE_D:
    		weeklyCarte = ParseCarte.getWeeklyCarteD();
    		break;
    	case Const.WHERE_Y:
    		weeklyCarte = ParseCarte.getWeeklyCarteY();
    		break;
    	}
    	
    	tv_week.setText(weeklyCarte.getWeek());
    	
    	setListAdapter(new WeeklyCarteAdapter(this, R.layout.carte, weeklyCarte));
	}
}