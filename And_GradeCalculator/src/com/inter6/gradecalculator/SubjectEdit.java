package com.inter6.gradecalculator;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class SubjectEdit extends ListActivity {
	private GradeDbAdapter dbAdapter;
	private Long rowID;
	private String selectedGrade;
	private int selectedResultPos;
	
	private EditText et_subject;
	private Spinner sp_grade;
	private Button bt_ok;
	private Button bt_delete;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_subject);
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, Const.results));
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		// DB 접속
		dbAdapter = new GradeDbAdapter(this);
		dbAdapter.open();
		
		rowID = null;
		selectedResultPos = 0;
		
		if (savedInstanceState != null) {
			rowID = savedInstanceState.getLong(GradeDbAdapter.KEY_ROWID);
		}
		else {
			Bundle extras = getIntent().getExtras();
			
			if (extras != null) {			
				rowID = extras.getLong(GradeDbAdapter.KEY_ROWID);
			}
			else {
				long id = dbAdapter.createNote("", "", "");
				
				if (id > 0) {
					rowID = id;
				}
			}
		}
		
		// 컴포넌트 할당
		et_subject = (EditText)findViewById(R.id.et_subject);
		
		sp_grade = (Spinner)findViewById(R.id.sp_grade);
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Const.grades);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_grade.setAdapter(spinnerAdapter);
        sp_grade.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				selectedGrade = Const.grades[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		bt_ok = (Button)findViewById(R.id.bt_ok);
		bt_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setResult(RESULT_OK);
				finish();
			}
		});
		
		bt_delete = (Button)findViewById(R.id.bt_delete);
		bt_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dbAdapter.deleteNote(rowID);
				
				setResult(RESULT_OK);
				finish();
			}
		});
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		selectedResultPos = position;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if (rowID == null) {
			return;
		}
		
		Cursor recordsCursor = dbAdapter.fetchNote(rowID);
		startManagingCursor(recordsCursor);
		
		et_subject.setText(recordsCursor.getString(recordsCursor.getColumnIndexOrThrow(GradeDbAdapter.KEY_SUBJECT)));
		
		String grade = recordsCursor.getString(recordsCursor.getColumnIndexOrThrow(GradeDbAdapter.KEY_GRADE));
		for (int i = 0; i < Const.grades.length; i++) {
			if (Const.grades[i].equals(grade)) {
				sp_grade.setSelection(i);
				selectedGrade = grade;
				break;
			}
		}
		
		String result = recordsCursor.getString(recordsCursor.getColumnIndexOrThrow(GradeDbAdapter.KEY_RESULT));
		for (int i = 0; i < Const.results.length; i++) {
			if (Const.results[i].equals(result)) {
				getListView().setItemChecked(i, true);
				selectedResultPos = i;
				break;
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putLong(GradeDbAdapter.KEY_ROWID, rowID);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		String subject = et_subject.getText().toString();
		String result = Const.results[selectedResultPos];
		
		dbAdapter.updateNote(rowID, subject, selectedGrade, result);
	}
}