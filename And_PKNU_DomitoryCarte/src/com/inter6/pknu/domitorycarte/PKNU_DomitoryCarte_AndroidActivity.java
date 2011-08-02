package com.inter6.pknu.domitorycarte;

import android.app.Activity;
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

public class PKNU_DomitoryCarte_AndroidActivity extends Activity {
	private RadioGroup rg_where;
	
	private TextView tv_todayDate;
	private TextView tv_morning;
	private TextView tv_afternoon;
	private TextView tv_evening;
	
	private Button bt_refresh;
	
	private int where = Const.WHERE_D;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        rg_where = (RadioGroup)findViewById(R.id.rg_where);
        rg_where.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.rb_whereD) {
					where = Const.WHERE_D;
				}
				else if (checkedId == R.id.rb_whereY) {
					where = Const.WHERE_Y;
				}
				
				refreshTodayCarte();
			}
		});
        
        tv_todayDate = (TextView)findViewById(R.id.tv_todayDate);
        tv_morning = (TextView)findViewById(R.id.tv_morning);
        tv_afternoon = (TextView)findViewById(R.id.tv_afternoon);
        tv_evening = (TextView)findViewById(R.id.tv_evening);
        
        bt_refresh = (Button)findViewById(R.id.bt_refresh);
        bt_refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				refreshTodayCarte();
			}
		});
        
        refreshTodayCarte();
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
    	
    	tv_todayDate.setText(todayCarte.getDate());
    	tv_morning.setText(todayCarte.getCarte(Const.WHEN_MORNING));
    	tv_afternoon.setText(todayCarte.getCarte(Const.WHEN_AFTERNOON));
    	tv_evening.setText(todayCarte.getCarte(Const.WHEN_EVENING));
    }
}