package com.inter6.pknu.domitorycarte2;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.inter6.pknu.domitorycarte2.data.Const;
import com.inter6.pknu.domitorycarte2.data.TodayCarte;
import com.inter6.pknu.domitorycarte2.data.WeeklyCarte;
import com.inter6.pknu.domitorycarte2.logic.ParseCarte;

public class DomitoryCarteActivity extends Activity {	
	private RadioGroup rg_where;
	private ViewFlipper vf_flip;
	private Button bt_carte;
	private Button bt_refresh;
	
	private int currentWhere;
	private int currentCarte;
	
	private EventHandler eventHandler;
	private boolean isWait;
	
	// private ProgressDialog progressDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // ������Ʈ �Ҵ�
        rg_where = (RadioGroup)findViewById(R.id.rg_where);
        rg_where.setOnCheckedChangeListener(changeWhereEvent);
        
        vf_flip = (ViewFlipper)findViewById(R.id.vf_flip);
        
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vf_flip.addView(layoutInflater.inflate(R.layout.today, null), Const.TODAY_CARTE);
        vf_flip.addView(layoutInflater.inflate(R.layout.week, null), Const.WEEKLY_CARTE);
        
        bt_refresh = (Button)findViewById(R.id.bt_refresh);
        bt_refresh.setOnClickListener(refreshEvent);
        
        bt_carte = (Button)findViewById(R.id.bt_flip);
        bt_carte.setOnClickListener(changeCarteEvent);
        
        eventHandler = new EventHandler();
        isWait = false;
        
        // ù ȭ�鿡�� ������ ���ϽĴ�ǥ�� ���
        currentWhere = Const.WHERE_D;
        currentCarte = Const.TODAY_CARTE;
        // bt_refresh.performClick();
    }
    
    
    /* ���۵� */
    
    private void changeCarte() {
    	boolean isRefresh = false;
    	
    	switch (currentCarte) {
		case Const.TODAY_CARTE:
			isRefresh = refresh(currentWhere, Const.WEEKLY_CARTE);
			break;
		case Const.WEEKLY_CARTE:
			isRefresh = refresh(currentWhere, Const.TODAY_CARTE);
			break;
		}
    	
    	if (!isRefresh) {
    		return;
    	}
    	
    	switch (currentCarte) {
    		case Const.TODAY_CARTE:
    			changeToWeeklyCarte();
    			break;
    		case Const.WEEKLY_CARTE:
    			changeToTodayCarte();
    			break;
    	}
    }
    
    private void changeToTodayCarte() {
    	// �ִϸ��̼�
    	vf_flip.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.today_in));
    	vf_flip.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.today_out));
    	vf_flip.showPrevious();
    	
    	bt_carte.setText("����");
    	
    	currentCarte = Const.TODAY_CARTE; 
    }
    
    private void changeToWeeklyCarte() {    	
    	// �ִϸ��̼�
    	vf_flip.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.week_in));
    	vf_flip.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.week_out));
    	vf_flip.showNext();
    	
    	bt_carte.setText("�ְ�");
    	
    	currentCarte = Const.WEEKLY_CARTE;
    }
    
    private void changeWhere() {
    	boolean isRefresh = false;
    	
    	switch (currentWhere) {
		case Const.WHERE_D:
			isRefresh = refresh(Const.WHERE_Y, currentCarte);
			break;
		case Const.WHERE_Y:
			isRefresh = refresh(Const.WHERE_D, currentCarte);
			break;
		}
    	
    	if (!isRefresh) {
    		return;
    	}
    	
    	switch (currentWhere) {
    		case Const.WHERE_D:
    			changeToY();
    			break;
    		case Const.WHERE_Y:
    			changeToD();
    			break;
    	}
    }
    
    private void changeToD() {
    	currentWhere = Const.WHERE_D;
    }
    
    private void changeToY() {
    	currentWhere = Const.WHERE_Y;
    }
    
    /**
     * ���� Ʈ����
     */
    private boolean refresh(int where, int carte) {
    	boolean result = false;
    	
    	switch (where) {
    	case Const.WHERE_D:
    		result = refreshD(carte);
    		break;
    	case Const.WHERE_Y:
    		result = refreshY(carte);
    		break;
    	}
    	
    	if (!result) {
    		Toast.makeText(this, "���� �ð� �ʰ�", Const.TOAST_DUR).show();
    	}
    	
    	return result;
    }

    /**
     * ������ ����
     */
    private boolean refreshD(int carte) {
    	View currentView = vf_flip.getChildAt(carte);
    	
    	switch (carte) {
    	case Const.TODAY_CARTE:
    		TodayCarte todayCarteD = ParseCarte.getTodayCarteD();
    		if (todayCarteD == null) {
    			return false;
    		}
    		
    		todayCarteD.setView(currentView);
        	Toast.makeText(this, "������ - ���ϽĴ�ǥ", Const.TOAST_DUR).show();
        	
        	break;
    	case Const.WEEKLY_CARTE:
    		WeeklyCarte weeklyCarteD = ParseCarte.getWeeklyCarteD();
    		if (weeklyCarteD == null) {
    			return false;
    		}
    		
    		weeklyCarteD.setView(currentView);
    		Toast.makeText(this, "������ - �ְ��Ĵ�ǥ", Const.TOAST_DUR).show();
    		
    		break;
    	}
    	
    	return true;
    }
    
    /**
     * ������� ����
     */
    private boolean refreshY(int carte) {
    	View currentView = vf_flip.getChildAt(carte);
    	
    	switch (carte) {
    	case Const.TODAY_CARTE:
    		TodayCarte todayCarteY = ParseCarte.getTodayCarteY();
    		if (todayCarteY == null) {
    			return false;
    		}
    		
    		todayCarteY.setView(currentView);
        	Toast.makeText(this, "������� - ���ϽĴ�ǥ", Const.TOAST_DUR).show();
        	
    		break;
    	case Const.WEEKLY_CARTE:
    		WeeklyCarte weeklyCarteY = ParseCarte.getWeeklyCarteY();
    		if (weeklyCarteY == null) {
    			return false;
    		}
    		
    		weeklyCarteY.setView(currentView);
    		Toast.makeText(this, "������� - �ְ��Ĵ�ǥ", Const.TOAST_DUR).show();
    		
    		break;
    	}
    	
    	return true;
    }
    
    /* �����ʵ� */
    
    // ������ư üũ�� ����� ��
    private OnCheckedChangeListener changeWhereEvent = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int checkWhere = -1;
			
			switch (checkedId) {
			case R.id.rb_whereD:
				checkWhere = Const.WHERE_D;
				break;
			case R.id.rb_whereY:
				checkWhere = Const.WHERE_Y;
				break;
			}
			
			if (checkWhere == currentWhere) {
				return;
			}
			
			// showDialog(Const.DIALOG_PROGRESS);
			
			Message msg = eventHandler.obtainMessage();
			msg.what = Const.REQUEST_CHANGE_WHERE;
			eventHandler.sendMessage(msg);
		}
	};
	
	// ��ȯ ��ư�� ������ ��
	private OnClickListener changeCarteEvent = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// showDialog(Const.DIALOG_PROGRESS);
			
			Message msg = eventHandler.obtainMessage();
			msg.what = Const.REQUEST_CHANGE_CARTE;
			eventHandler.sendMessage(msg);
		}
	};
	
	// ���� ��ư�� ������ ��
	private OnClickListener refreshEvent = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// showDialog(Const.DIALOG_PROGRESS);
			
			Message msg = eventHandler.obtainMessage();
			msg.what = Const.REQUEST_REFRESH;
			eventHandler.sendMessage(msg);
		}
	};
	
	
	/* �ڵ鷯 */
	
	private class EventHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (isWait) {
				return;
			}
			
			isWait = true;
			
			switch (msg.what) {
				case Const.REQUEST_REFRESH:
					Log.d("[EventHandler]", "REQUEST_REFRESH");
					refresh(currentWhere, currentCarte);
					break;
				case Const.REQUEST_CHANGE_WHERE:
					Log.d("[EventHandler]", "REQUEST_CHANGE_WHERE");
					changeWhere();
					break;
				case Const.REQUEST_CHANGE_CARTE:
					Log.d("[EventHandler]", "REQUEST_CHANGE_CARTE");
					changeCarte();
					break;
			}
			
			// progressDialog.dismiss();
			
			isWait = false;
			
			super.handleMessage(msg);
		}
	}
	
	/*
	@Override
	protected Dialog onCreateDialog(int id) {		
		switch (id) {
			case Const.DIALOG_PROGRESS:
				progressDialog = ProgressDialog.show(DomitoryCarteActivity.this, "", "�ε� ��");
				break;
			default:
				progressDialog = null;
				break;
		}
		
		return progressDialog;
	}
	*/
}