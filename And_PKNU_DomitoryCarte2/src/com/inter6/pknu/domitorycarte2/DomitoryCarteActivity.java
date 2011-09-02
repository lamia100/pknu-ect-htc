package com.inter6.pknu.domitorycarte2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.inter6.pknu.domitorycarte2.data.Const;
import com.inter6.pknu.domitorycarte2.logic.ParseCarte;

public class DomitoryCarteActivity extends Activity {
	private RadioGroup rg_where;
	private ViewFlipper vf_flip;
	private Button bt_flip;
	private Button bt_refresh;
	
	private float startPointX;
	private float endPointX;
	
	private int where;
	private int currentCarte;
	
	private Toast toast;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // 컴포넌트 할당
        rg_where = (RadioGroup)findViewById(R.id.rg_where);
        rg_where.setOnCheckedChangeListener(radioChangeEvent);
        
        vf_flip = (ViewFlipper)findViewById(R.id.vf_flip);
        vf_flip.setOnTouchListener(flipTouchEvent);
        
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vf_flip.addView(layoutInflater.inflate(R.layout.today, null), Const.TODAY_CARTE);
        vf_flip.addView(layoutInflater.inflate(R.layout.week, null), Const.WEEKLY_CARTE);
        
        bt_refresh = (Button)findViewById(R.id.bt_refresh);
        bt_refresh.setOnClickListener(refreshEvent);
        
        bt_flip = (Button)findViewById(R.id.bt_flip);
        bt_flip.setOnClickListener(flipEvent);
        
        // 첫 화면에는 세종관 일일식단표를 띄움
        currentCarte = Const.TODAY_CARTE;
        rg_where.check(R.id.rb_whereD);
    }
    
    
    /* 동작들 */
    
    /**
     * 화면 전환 트리거
     */
    private void flip() {
    	switch (currentCarte) {
		case Const.TODAY_CARTE:
			flipWeek();
			break;
		case Const.WEEKLY_CARTE:
			flipToday();
			break;
		}
    	
    	refresh();
    }
    
    /**
     * 일일식단표로 플립
     */
    private void flipToday() {
    	// 애니메이션
    	vf_flip.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.today_in));
    	vf_flip.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.today_out));
    	
    	if (currentCarte == Const.TODAY_CARTE) {
    		return;
    	}
    	
    	currentCarte = Const.TODAY_CARTE;
    	
    	vf_flip.showPrevious();  	
    }
    
    /**
     * 주간식단표로 플립
     */
    private void flipWeek() {
    	// 애니메이션
    	vf_flip.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.week_in));
    	vf_flip.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.week_out));
    	
    	if (currentCarte == Const.WEEKLY_CARTE) {
    		return;
    	}
    	
    	currentCarte = Const.WEEKLY_CARTE;
    	
    	vf_flip.showNext();
    }
    
    /**
     * 갱신 트리거
     */
    private void refresh() {
    	switch (where) {
    	case Const.WHERE_D:
    		refreshD();
    		break;
    	case Const.WHERE_Y:
    		refreshY();
    		break;
    	}
    	
    	toast.show();
    }

    /**
     * 세종관 갱신
     */
    private void refreshD() {
    	View currentView = vf_flip.getChildAt(currentCarte);
    	
    	switch (currentCarte) {
    	case Const.TODAY_CARTE:
    		ParseCarte.getTodayCarteD().setView(currentView);
    		
        	toast = Toast.makeText(this, "세종관 - 일일식단표", Const.TOAST_DUR);
        	
        	break;
    	case Const.WEEKLY_CARTE:
    		ParseCarte.getWeeklyCarteD().setView(currentView);
    		
    		toast = Toast.makeText(this, "세종관 - 주간식단표", Const.TOAST_DUR);
    		
    		break;
    	}
    }
    
    /**
     * 광개토관 갱신
     */
    private void refreshY() {
    	View currentView = vf_flip.getChildAt(currentCarte);
    	
    	switch (currentCarte) {
    	case Const.TODAY_CARTE:
    		ParseCarte.getTodayCarteY().setView(currentView);
    		
        	toast = Toast.makeText(this, "광개토관 - 일일식단표", Const.TOAST_DUR);
        	
    		break;
    	case Const.WEEKLY_CARTE:
    		ParseCarte.getWeeklyCarteY().setView(currentView);
    		
    		toast = Toast.makeText(this, "광개토관 - 주간식단표", Const.TOAST_DUR);
    		
    		break;
    	}
    }
    
    /* 리스너들 */
    
    // 라디오버튼 체크가 변경될 때
    private OnCheckedChangeListener radioChangeEvent = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			switch (checkedId) {
			case R.id.rb_whereD:
				where = Const.WHERE_D;
				break;
			case R.id.rb_whereY:
				where = Const.WHERE_Y;
				break;
			}
			
			refresh();
		}
	};
	
	// 플립뷰에서 터치할 때
	private OnTouchListener flipTouchEvent = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub			
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				startPointX = event.getX();
			}
			else if (event.getAction() == MotionEvent.ACTION_UP) {
				endPointX = event.getY();
				
				if (startPointX < endPointX) {
					flipToday();
				}
				else if (startPointX > endPointX) {
					flipWeek();
				}
			}
			
			return true;
		}
	};
	
	// 전환 버튼을 눌렀을 때
	private OnClickListener flipEvent = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			flip();
		}
	};
	
	// 갱신 버튼을 눌렀을 때
	private OnClickListener refreshEvent = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			refresh();
		}
	};
}