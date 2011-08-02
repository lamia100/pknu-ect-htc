/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.inter6.gradecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class GradeDbAdapter {
	private static final String TAG = "NotesDbAdapter";
	
	/**
     * Database creation sql statement
     * 데이터베이스에 테이블이 존재하지 않을 경우, 테이블을 생성하는 SQL문
     */
    private static final String DATABASE_CREATE =
        "create table notes (_id integer primary key autoincrement, "
        + "subject text not null, grade text not null, result text not null);";

    private static final String DATABASE_NAME = "data"; // 데이터베이스 이름
    private static final String DATABASE_TABLE = "notes"; // 테이블 이름
    private static final int DATABASE_VERSION = 3; // 데이터베이스 버전
    
    // 필드 목록
	public static final String KEY_ROWID = "_id"; // 기본키
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_GRADE = "grade";
    public static final String KEY_RESULT = "result";

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private final Context mCtx;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public GradeDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    
    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 데이터베이스 및 테이블 생성
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public GradeDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @param title the title of the note
     * @param body the body of the note
     * @return rowId or -1 if failed // 정상적으로 레코드가 만들어졌다면 _id를 리턴, 아니라면 -1 리턴
     */
    public long createNote(String subject, String grade, String result) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SUBJECT, subject);
        initialValues.put(KEY_GRADE, grade);
        initialValues.put(KEY_RESULT, result);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete // 삭제할 _id
     * @return true if deleted, false otherwise // 정상 삭제 여부
     */
    public boolean deleteNote(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     * 테이블에 있는 모든 레코드를 가리키는 커서(레코드들의 집합)를 반환
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAllNotes() {
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_SUBJECT, KEY_GRADE, KEY_RESULT},
        		null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 테이블에서 _id에 해당하는 커서(레코드)를 반환
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchNote(long rowId) throws SQLException {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {
        		KEY_ROWID, KEY_SUBJECT, KEY_GRADE, KEY_RESULT}, KEY_ROWID + "=" + rowId,
        		null, null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        return mCursor;
    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 테이블에서 _id에 해당하는 레코드를 갱신
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateNote(long rowId, String subject, String grade, String result) {
        ContentValues args = new ContentValues();
        args.put(KEY_SUBJECT, subject);
        args.put(KEY_GRADE, grade);
        args.put(KEY_RESULT, result);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion +
            		", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }
}