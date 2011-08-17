package com.inter6.gradecalculator;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class GradeCalculator extends ListActivity {
	private GradeDbAdapter dbAdapter;
	
	private Button bt_add;
	private TextView tv_totalResult;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		dbAdapter = new GradeDbAdapter(this);
		dbAdapter.open();
		
		tv_totalResult = (TextView)findViewById(R.id.tv_totalResult);
		
		bt_add = (Button)findViewById(R.id.bt_add);
		bt_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				createSubject();
			}
		});
		
		refreshList();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		// Edit ∫‰∑Œ ¿Ãµø
		Intent createIntent = new Intent(this, SubjectEdit.class);
		createIntent.putExtra(GradeDbAdapter.KEY_ROWID, id);
		
		startActivityForResult(createIntent, Const.ACTIVITY_EDIT);
	}

	private void createSubject() {
		Intent createIntent = new Intent(this, SubjectEdit.class);
		
		startActivityForResult(createIntent, Const.ACTIVITY_CREATE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		refreshList();
	}
	
	private void refreshList() {
		Cursor recordsCursor = dbAdapter.fetchAllNotes();
		startManagingCursor(recordsCursor);
		
		String grade_s = null;
		String result_s = null;
		int grade = 0;
		float result = 0f;
		int totalGrade = 0;
		float totalResult = 0f;
		
		recordsCursor.moveToFirst();
		while (!recordsCursor.isAfterLast()) {
			grade_s = recordsCursor.getString(recordsCursor.getColumnIndexOrThrow(GradeDbAdapter.KEY_GRADE));
			result_s = recordsCursor.getString(recordsCursor.getColumnIndexOrThrow(GradeDbAdapter.KEY_RESULT));
			
			for (int i = 0; i < Const.grades.length; i++) {
				if (Const.grades[i].equals(grade_s)) {
					grade = i + 1;
				}
			}
			
			for (int i = 0; i < Const.results.length; i++) {
				if (Const.results[i].equals(result_s)) {
					result = 4.5f - (float)i * 0.5f;
				}
			}
			
			totalGrade += grade;
			totalResult += ((float)grade * result);
			
			recordsCursor.moveToNext();
		}
		
		if (totalGrade != 0f) {
			totalResult /= (float)totalGrade;
		}
		
		tv_totalResult.setText(String.format("%.2f", totalResult));
		
		String[] fromData = {
				GradeDbAdapter.KEY_SUBJECT, GradeDbAdapter.KEY_GRADE, GradeDbAdapter.KEY_RESULT
		};
		int[] toView = {
				R.id.tv_subject, R.id.tv_grade, R.id.tv_result
		};
		
		setListAdapter(new SimpleCursorAdapter(this, R.layout.list_record, recordsCursor, fromData, toView));
	}
}