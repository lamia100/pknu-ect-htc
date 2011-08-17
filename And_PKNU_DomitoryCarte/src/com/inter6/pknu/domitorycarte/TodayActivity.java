package com.inter6.pknu.domitorycarte;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.inter6.pknu.domitorycarte.data.Const;
import com.inter6.pknu.domitorycarte.data.TodayCarte;
import com.inter6.pknu.domitorycarte.logic.ParseCarte;

public class TodayActivity extends Activity {
	private RadioGroup rg_where;
	
	private TextView tv_date;
	private TextView tv_breakfast;
	private TextView tv_lunch;
	private TextView tv_dinner;
	
	private Button bt_changeToWeek;
	private Button bt_refresh;
	
	private int where = Const.WHERE_D;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.today);
        
        rg_where = (RadioGroup)findViewById(R.id.rg_today_where);
        rg_where.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_today_whereD:
					where = Const.WHERE_D;
					break;
				case R.id.rb_today_whereY:
					where = Const.WHERE_Y;
					break;
				}
				
				refreshTodayCarte();
			}
		});
        
        tv_date = (TextView)findViewById(R.id.tv_today_date);
        tv_breakfast = (TextView)findViewById(R.id.tv_today_breakfast);
        tv_lunch = (TextView)findViewById(R.id.tv_today_lunch);
        tv_dinner = (TextView)findViewById(R.id.tv_today_dinner);
        
        bt_changeToWeek = (Button)findViewById(R.id.bt_today_changeToWeek);
        bt_changeToWeek.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeToWeek();
			}
		});
        
        bt_refresh = (Button)findViewById(R.id.bt_today_refresh);
        bt_refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				refreshTodayCarte();
			}
		});
        
        Bundle fromWeeklyBundle = getIntent().getExtras();
        
        if (fromWeeklyBundle != null) {
        	where = fromWeeklyBundle.getInt("where");
        }
        
        switch (where) {
        case Const.WHERE_D:
        	rg_where.check(R.id.rb_today_whereD);
        	break;
        case Const.WHERE_Y:
        	rg_where.check(R.id.rb_today_whereY);
        	break;
        }
    }
    
    private void changeToWeek() {
    	Intent toWeeklyIntent = new Intent(TodayActivity.this, WeeklyActivity.class);
    	toWeeklyIntent.putExtra("where", where);
    	
    	startActivity(toWeeklyIntent);
    	
    	finish();
    }
    
    private void refreshTodayCarte() {
    	TodayCarte todayCarte = null;
    	
    	switch (where) {
    	case Const.WHERE_D:
    		todayCarte = ParseCarte.getTodayCarteD();
    		break;
    	case Const.WHERE_Y:
    		todayCarte = ParseCarte.getTodayCarteY();
    		break;
    	}
    	
    	tv_date.setText(todayCarte.getDate());
    	tv_breakfast.setText(todayCarte.getCarte(Const.WHEN_BREAKFAST));
    	tv_lunch.setText(todayCarte.getCarte(Const.WHEN_LUNCH));
    	tv_dinner.setText(todayCarte.getCarte(Const.WHEN_DINNER));
    }
}