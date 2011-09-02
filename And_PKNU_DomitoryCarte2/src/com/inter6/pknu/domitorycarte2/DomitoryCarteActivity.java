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
        
        // ������Ʈ �Ҵ�
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
        
        // ù ȭ�鿡�� ������ ���ϽĴ�ǥ�� ���
        currentCarte = Const.TODAY_CARTE;
        rg_where.check(R.id.rb_whereD);
    }
    
    
    /* ���۵� */
    
    /**
     * ȭ�� ��ȯ Ʈ����
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
     * ���ϽĴ�ǥ�� �ø�
     */
    private void flipToday() {
    	// �ִϸ��̼�
    	vf_flip.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.today_in));
    	vf_flip.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.today_out));
    	
    	if (currentCarte == Const.TODAY_CARTE) {
    		return;
    	}
    	
    	currentCarte = Const.TODAY_CARTE;
    	
    	vf_flip.showPrevious();  	
    }
    
    /**
     * �ְ��Ĵ�ǥ�� �ø�
     */
    private void flipWeek() {
    	// �ִϸ��̼�
    	vf_flip.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.week_in));
    	vf_flip.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.week_out));
    	
    	if (currentCarte == Const.WEEKLY_CARTE) {
    		return;
    	}
    	
    	currentCarte = Const.WEEKLY_CARTE;
    	
    	vf_flip.showNext();
    }
    
    /**
     * ���� Ʈ����
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
     * ������ ����
     */
    private void refreshD() {
    	View currentView = vf_flip.getChildAt(currentCarte);
    	
    	switch (currentCarte) {
    	case Const.TODAY_CARTE:
    		ParseCarte.getTodayCarteD().setView(currentView);
    		
        	toast = Toast.makeText(this, "������ - ���ϽĴ�ǥ", Const.TOAST_DUR);
        	
        	break;
    	case Const.WEEKLY_CARTE:
    		ParseCarte.getWeeklyCarteD().setView(currentView);
    		
    		toast = Toast.makeText(this, "������ - �ְ��Ĵ�ǥ", Const.TOAST_DUR);
    		
    		break;
    	}
    }
    
    /**
     * ������� ����
     */
    private void refreshY() {
    	View currentView = vf_flip.getChildAt(currentCarte);
    	
    	switch (currentCarte) {
    	case Const.TODAY_CARTE:
    		ParseCarte.getTodayCarteY().setView(currentView);
    		
        	toast = Toast.makeText(this, "������� - ���ϽĴ�ǥ", Const.TOAST_DUR);
        	
    		break;
    	case Const.WEEKLY_CARTE:
    		ParseCarte.getWeeklyCarteY().setView(currentView);
    		
    		toast = Toast.makeText(this, "������� - �ְ��Ĵ�ǥ", Const.TOAST_DUR);
    		
    		break;
    	}
    }
    
    /* �����ʵ� */
    
    // ������ư üũ�� ����� ��
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
	
	// �ø��信�� ��ġ�� ��
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
	
	// ��ȯ ��ư�� ������ ��
	private OnClickListener flipEvent = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			flip();
		}
	};
	
	// ���� ��ư�� ������ ��
	private OnClickListener refreshEvent = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			refresh();
		}
	};
}