package artech.com.manager.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBManager extends SQLiteOpenHelper {
	public String id;
	public String name;
	public String date;
	public String email;
	public String team;
	public String bookings_name;
	public String bookings_phone;
	public String bookings_number;
	public String bookings_team_number;
	public String program;
	public String etc;

	public String TABLE_ACT = "ACCOUNT";

	public String KEY_ID = "_ID";

	public DBManager(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// user Account의 count는 항상 1
		db.execSQL("create table account( "
				+ "_id datetime primary key,"
				+ "bookings_date text,"
				+ "email text,"
				+ "team text,"
				+ "bookings_name text,"
				+ "bookings_phone text,"
				+ "bookings_number text,"
				+ "bookings_team_number text,"
				+ "program text,"
				+ "etc text,"
				+ "bookings_persons text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void dropDB() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("account", null, null);
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

	public String selectId() {
		SQLiteDatabase db = getReadableDatabase();
		String _id = "";

		Cursor cursor = db.rawQuery("select _id from account;", null);
		while (cursor.moveToNext()) {
			_id = cursor.getString(0);
		}
		return _id;
	}

    /**
     *
     * 예약자 추가
     *
     * @param array
	 *        bookings_date
	 *        email
     *        team
     *        bookings_name
     *        bookings_phone
     *        bookings_number
	 *        bookings_team_number
	 *        program
	 *        etc
	 *        persons
     */
	public void insertUser(ArrayList<Object> array) {
		String _query = "insert into account values(current_timestamp, '" + array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3) + "', '"
				+ array.get(4) + "', '" + array.get(5) + "', '" + array.get(6) + "', '" + array.get(7) + "', '" + array.get(8) + "', '" + array.get(9)+"');";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

    /**
     * 예약자 정보 수정
     *
     * @param array
	 *        _id
	 *        bookings_date
	 *        email
	 *        team
	 *        bookings_name
	 *        bookings_phone
	 *        bookings_number
	 *        bookings_team_number
	 *        program
	 *        etc
     */
	public void updateUser(ArrayList<Object> array) {
		String _query = "update account set bookings_date='" + array.get(1) + "', email='"
				+ array.get(2) + "', team='" + array.get(3) + "', bookings_name='" + array.get(4) + "', bookings_phone='" + array.get(5) + "', bookings_number='"
				+ array.get(6) + "', bookings_team_number='" + array.get(7) + "', program='" + array.get(8) + "', etc='" + array.get(9) + "', bookings_persons='" + array.get(10) + "'"
				+ " where _id= '" + array.get(0) + "';";

		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}


	public Cursor selectUser() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from `account` ;", null);
	};

	public void removeUser(String _id) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from `account` where _id='" + _id + "';");
		db.close();
	}

}
