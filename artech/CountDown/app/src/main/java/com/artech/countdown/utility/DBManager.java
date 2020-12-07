package com.artech.countdown.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {
	public String name;

	public DBManager(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// user Account의 count는 항상 1
		db.execSQL("create table setting( "
				+ "speaker integer,"
				+ "font integer,"
				+ "background integer,"
				+ "language integer);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void dropDB() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("setting", null, null);
		db.close();

	}

	public void insert(String _query) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void update(String _query) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void delete(String _query) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public int selectCount() {
		SQLiteDatabase db = getReadableDatabase();
		int count = -1;

		Cursor cursor = db.rawQuery("select count(speaker) from setting;", null);
		while (cursor.moveToNext()) {
			count = cursor.getInt(0);
		}
		return count;
	}

	public Cursor selectSetting() {
		SQLiteDatabase db = getReadableDatabase();

		Cursor cursor = db.rawQuery("select speaker, font, background, language from setting;", null);
		return cursor;
	}
    /**
     *
     * 예약자 추가
     *
     * @param array
	 *        speaker
	 *        font
     *        background
     */
	public void insertSetting(ArrayList<Integer> array) {
		String _query = "insert into setting values('" + array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3)+"');";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

    /**
     * 예약자 정보 수정
     *
     * @param array
	 *        speaker
	 *        font
	 *        background
     */
	public void updateUser(ArrayList<Integer> array) {
		String _query = "update setting set speaker='" + array.get(0) + "', font='" + array.get(1) + "', background='" + array.get(2) + "', language='" + array.get(3) + "';";

		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}
}
