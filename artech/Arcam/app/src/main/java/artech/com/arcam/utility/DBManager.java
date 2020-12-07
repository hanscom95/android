package artech.com.arcam.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {
	public String name;
	public String password;
	public int location;
	public String team;
	public int sex;
	public String date;
	public String email;

	public DBManager(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// user Account의 count는 항상 1
		db.execSQL("create table account( "
				+ "name primary key,"
				+ "password text,"
				+ "location integer,"
				+ "team text,"
				+ "sex text,"
				+ "date text,"
				+ "url text,"
				+ "email text);");


		db.execSQL("create table equipment( "
				+ "riser_brand_code integer,"
				+ "riser_brand_text text,"
				+ "riser_inch_code integer,"
				+ "riser_inch_text text,"
				+ "limbs_brand_code integer,"
				+ "limbs_brand_text text,"
				+ "limbs_inch_code integer,"
				+ "limbs_inch_text text,"
				+ "limbs_pound text,"
				+ "stabilizer_brand_code integer,"
				+ "stabilizer_brand_text text,"
				+ "longstaff_inch_code integer,"
				+ "longstaff_inch_text text,"
				+ "sidestabi_inch_code integer,"
				+ "sidestabi_inch_text text,"
				+ "fingertab_brand_code integer,"
				+ "fingertab_brand_text text,"
				+ "sight_brand_code integer,"
				+ "sight_brand_text text,"
				+ "cushionplunger_brand_code integer,"
				+ "cushionplunger_brand_text text,"
				+ "rest_brand_code integer,"
				+ "rest_brand_text text,"
				+ "arrow_brand_code integer,"
				+ "arrow_brand_text text,"
				+ "auto integer,"
				+ "brace_high integer,"
				+ "tiller_high integer,"
				+ "nocking_point integer,"
				+ "weather integer,"
				+ "wind integer,"
				+ "distance integer);");


		db.execSQL("create table score( "
				+ "col text primary key,"
				+ "date date,"
				+ "day_of_week integer,"
				+ "total integer,"
				+ "round integer,"
				+ "arrow integer,"
				+ "zero_score integer,"
				+ "one_score integer,"
				+ "two_score integer,"
				+ "three_score integer,"
				+ "four_score integer,"
				+ "five_score integer,"
				+ "six_score integer,"
				+ "seven_score integer,"
				+ "eigh_score integer,"
				+ "nine_score integer,"
				+ "ten_score integer,"
				+ "x_ten_score integer,"
				+ "weather integer,"
				+ "wind integer,"
				+ "distance integer,"
				+ "brace_high integer,"
				+ "tiller_high integer,"
				+ "nocking_point integer,"
				+ "comment text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void dropDB() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("account", null, null);
		db.delete("equipment", null, null);
		db.delete("score", null, null);
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

		Cursor cursor = db.rawQuery("select name from account;", null);
		while (cursor.moveToNext()) {
			_id = cursor.getString(0);
		}
		return _id;
	}

	public String selectEmail() {
		SQLiteDatabase db = getReadableDatabase();
		String _email = "";

		Cursor cursor = db.rawQuery("select email from account;", null);
		while (cursor.moveToNext()) {
			_email = cursor.getString(0);
		}
		return _email;
	}

	public String selectUrl() {
		SQLiteDatabase db = getReadableDatabase();
		String _url = "";

		Cursor cursor = db.rawQuery("select url from account;", null);
		while (cursor.moveToNext()) {
			_url = cursor.getString(0);
		}
		return _url;
	}

    /**
     *
     * 예약자 추가
     *
     * @param array
	 *        name
	 *        password
     *        location
     *        team
     *        sex
     *        date
	 *        url
	 *        email
     */
	public void insertUser(ArrayList<Object> array) {
		String _query = "insert into account values('" + array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3) + "', '"
				+ array.get(4) + "', '" + array.get(5) + "', '" + array.get(6) + "', '" + array.get(7)+"');";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

    /**
     * 예약자 정보 수정
     *
     * @param array
	 *        location
	 *        team
	 *        sex
	 *        date
	 *        url
	 *        email
     */
	public void updateUser(ArrayList<Object> array) {
		String _query = "update account set location='" + array.get(0) + "', team='" + array.get(1) + "', sex='" + array.get(2) + "', date='" + array.get(3)+ "', url='" + array.get(4)  + "', email='" + array.get(5) + "';";

		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	/**
	 * 예약자 정보 수정
	 *
	 * @param url
	 */
	public void updateUrl(String url) {
		String _query = "update account set url='" + url + "';";

		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}



	public Cursor selectUser() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from `account` ;", null);
	}

	public void removeUser(String name) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from `account` where name='" + name + "';");
		db.close();
	}



	/**
	 *
	 * 장비 추가
	 *
	 * @param array
	 *        riser_brand_code
	 *        riser_brand_text
	 *        riser_inch_code
	 *        riser_inch_text
	 *        limbs_brand_code
	 *        limbs_brand_text
	 *        limbs_inch_code
	 *        limbs_inch_text
	 *        limbs_pound
	 *        stabilizer_brand_code
	 *        stabilizer_brand_text
	 *        longstaff_inch_code
	 *        longstaff_inch_text
	 *        sidestabi_inch_code
	 *        sidestabi_inch_text
	 *        fingertab_brand_code
	 *        fingertab_brand_text
	 *        sight_brand_code
	 *        sight_brand_text
	 *        cushionplunger_brand_code
	 *        cushionplunger_brand_text
	 *        rest_brand_code
	 *        rest_brand_text
	 *        arrow_brand_code
	 *        arrow_brand_text
	 *        auto
	 *        brace_high
	 *        tiller_high
	 *        nocking_point
	 *        weather
	 *        wind
	 *        distance
	 */
	public void insertEquipment(ArrayList<Object> array) {
		String _query = "insert into equipment values('" + array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3) + "', '" + array.get(4)
				+ "', '" + array.get(5) + "', '" + array.get(6) + "', '" + array.get(7) + "', '" + array.get(8) + "', '" + array.get(9) + "', '" + array.get(10)
				+ "', '" + array.get(11) + "', '" + array.get(12) + "', '" + array.get(13) + "', '" + array.get(14) + "', '" + array.get(15) + "', '" + array.get(16)
				+ "', '" + array.get(17) + "', '" + array.get(18) + "', '" + array.get(19) + "', '" + array.get(20) + "', '" + array.get(21) + "', '" + array.get(22)
				+ "', '" + array.get(23) + "', '" + array.get(24) + "', '" + array.get(25) + "', '" + array.get(26) + "', '" + array.get(27) + "', '" + array.get(28)
				+ "', '" + array.get(29) + "', '" + array.get(30) + "', '" + array.get(31) +"');";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}



	/**
	 * 예약자 정보 수정
	 *
	 * @param array
	 *        riser_brand_code
	 *        riser_brand_text
	 *        riser_inch_code
	 *        riser_inch_text
	 *        limbs_brand_code
	 *        limbs_brand_text
	 *        limbs_inch_code
	 *        limbs_inch_text
	 *        limbs_pound
	 *        stabilizer_brand_code
	 *        stabilizer_brand_text
	 *        longstaff_inch_code
	 *        longstaff_inch_text
	 *        sidestabi_inch_code
	 *        sidestabi_inch_text
	 *        fingertab_brand_code
	 *        fingertab_brand_text
	 *        sight_brand_code
	 *        sight_brand_text
	 *        cushionplunger_brand_code
	 *        cushionplunger_brand_text
	 *        rest_brand_code
	 *        rest_brand_text
	 *        arrow_brand_code
	 *        arrow_brand_text
	 *        auto
	 */
	public void updateEquipment(ArrayList<Object> array) {
		String _query = "update equipment set riser_brand_code='" + array.get(0) + "', riser_brand_text='" + array.get(1) + "', riser_inch_code='" + array.get(2) + "', riser_inch_text='" + array.get(3)
				+ "', limbs_brand_code='" + array.get(4) + "', limbs_brand_text='" + array.get(5) + "', limbs_inch_code='" + array.get(6) +"', limbs_inch_text='" + array.get(7)
				+ "', limbs_pound='" + array.get(8) + "', stabilizer_brand_code='" + array.get(9) + "', stabilizer_brand_text='" + array.get(10) +"', longstaff_inch_code='" + array.get(11)
				+ "', longstaff_inch_text='" + array.get(12) + "', sidestabi_inch_code='" + array.get(13) + "', sidestabi_inch_text='" + array.get(14) +"', fingertab_brand_code='" + array.get(15)
				+ "', fingertab_brand_text='" + array.get(16) + "', sight_brand_code='" + array.get(17) + "', sight_brand_text='" + array.get(18) +"', cushionplunger_brand_code='" + array.get(19)
				+ "', cushionplunger_brand_text='" + array.get(20) + "', rest_brand_code='" + array.get(21) + "', rest_brand_text='" + array.get(22) +"', arrow_brand_code='" + array.get(23)
				+ "', arrow_brand_text='" + array.get(24) + "', auto='" + array.get(25) + "';";

		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}



	/**
	 * 예약자 정보 수정
	 *
	 * @param array
	 *        brace_high
	 *        tiller_high
	 *        nocking_point
	 *        weather
	 *        wind
	 *        distance
	 */
	public void updateFineTuning(ArrayList<Object> array) {
		String _query = "update equipment set brace_high='" + array.get(0) + "', tiller_high='" + array.get(1) + "', nocking_point='" + array.get(2) + "', weather='" + array.get(3)
				+ "', wind='" + array.get(4) + "', distance='" + array.get(5)+ "';";

		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}



	public void removeEquipment() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from `equipment`;");
		db.close();
	}


	public Cursor selectEquipment() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from `equipment` ;", null);
	}

	public Cursor selectTuningInfo() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select brace_high, tiller_high, nocking_point, weather, wind, distance from `equipment` ;", null);
	}





	/**
	 *
	 * 스코어 추가
	 *
	 * @param array
	 *        col
	 *        total
	 *        round
	 *        arrow
	 *        zero_score
	 *        one_score
	 *        two_score
	 *        three_score
	 *        four_score
	 *        five_score
	 *        six_score
	 *        seven_score
	 *        eigh_score
	 *        nine_score
	 *        ten_score
	 *        x_ten_score
	 *        weather
	 *        wind
	 *        distance
	 *        brace_high
	 *        tiller_high
	 *        nocking_point
	 *        comment
	 *        date
	 *        day_of_week
	 */
	public String insertScore(ArrayList<Object> array) {
		SQLiteDatabase selectDb = getReadableDatabase();
		Cursor cursor = selectDb.rawQuery(
				"select count(col)+1 as col from `score` where date like '%"+ array.get(0)+"%';", null);

		String id = array.get(0) + "_";
		while (cursor.moveToNext()) {
			id = id + cursor.getInt(0);
		}
		String _query = "insert into `score` values('"+id+"', '" + array.get(23) + "', '" + array.get(24) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3) + "', '"
				+ array.get(4) + "', '" + array.get(5) + "', '" + array.get(6) + "', '" + array.get(7) + "', '" + array.get(8) + "', '" + array.get(9) + "', '" + array.get(10) + "', '" + array.get(11)
				+ "', '" + array.get(12) + "', '" + array.get(13) + "', '" + array.get(14) + "', '" + array.get(15) + "', '" + array.get(16) + "', '" + array.get(17) + "', '" + array.get(18) + "', '" + array.get(19)
				+ "', '" + array.get(20) + "', '" + array.get(21) + "', '" + array.get(22) + "');";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();

		return id;
	}

	public void updateScore(String col, String comment) {
		String _query = "update `score` set comment = '"+comment+"' where col='"+col+"' ";

		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void updateScoreTuning(String col, int brace, int tiller, int nocking, int weather, int wind, int distance, String comment) {
		String _query = "update `score` set brace_high='" + brace + "', tiller_high='" + tiller + "', nocking_point='" + nocking + "', weather='" + weather
				+ "', wind='" + wind + "', distance='" + distance + "', comment = '"+comment+"' where col='"+col+"' ";

		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public Cursor selectIdDateScore(String col) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select * from `score` where col like '%"+col+"%';", null);
	}

	public Cursor selectDateScore(String date) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select * from `score` where date like '%"+date+"%';", null);
	}

	public Cursor selectDateMaxScore(String date) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select max(total), col, date, day_of_week, round, arrow," +
						"zero_score, one_score, two_score, three_score, four_score, five_score, six_score, " +
						"seven_score, eigh_score, nine_score, ten_score, x_ten_score, weather, wind, " +
						"distance, brace_high, tiller_high, nocking_point, comment from `score` where date like '%"+date+"%';", null);
	}


	public Cursor selectDateSumScore(String date) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select sum(total), sum(round), sum(arrow)," +
						"sum(zero_score), sum(one_score), sum(two_score), sum(three_score), sum(four_score), sum(five_score), sum(six_score), " +
						"sum(seven_score), sum(eigh_score), sum(nine_score), sum(ten_score), sum(x_ten_score)" +
						"from `score` where date like '%"+date+"%';", null);
	}

	public Cursor selectWeatherScore(String date, int weather) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select * from `score` where date like '%"+date+"%' and weather='"+weather+"';", null);
	}

	public Cursor selectWeatherMaxScore(String date, int weather) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select max(total), col, date, day_of_week, round, arrow," +
						"zero_score, one_score, two_score, three_score, four_score, five_score, six_score, " +
						"seven_score, eigh_score, nine_score, ten_score, x_ten_score, weather, wind, " +
						"distance, brace_high, tiller_high, nocking_point, comment from `score` where date like '%"+date+"%' and weather='"+weather+"';", null);
	}

	public Cursor selectWindScore(String date, int wind) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select * from `score` where date like '%"+date+"%' and wind='"+wind+"';", null);
	}

	public Cursor selectWindMaxScore(String date, int wind) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select max(total), col, date, day_of_week, round, arrow," +
						"zero_score, one_score, two_score, three_score, four_score, five_score, six_score, " +
						"seven_score, eigh_score, nine_score, ten_score, x_ten_score, weather, wind, " +
						"distance, brace_high, tiller_high, nocking_point, comment from `score` where date like '%"+date+"%' and wind='"+wind+"';", null);
	}

	public Cursor selectDistanceScore(String date, int distance) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select * from `score` where date like '%"+date+"%' and distance='"+distance+"';", null);
	}

	public Cursor selectDistanceMaxScore(String date, int distance) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select max(total), col, date, day_of_week, round, arrow," +
						"zero_score, one_score, two_score, three_score, four_score, five_score, six_score, " +
						"seven_score, eigh_score, nine_score, ten_score, x_ten_score, weather, wind, " +
						"distance, brace_high, tiller_high, nocking_point, comment from `score` where date like '%"+date+"%' and distance='"+distance+"';", null);
	}

	public void removeScore(String col) {
		String _query = "delete from `score`  where `col` like '%"+col+"%' ";

		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}
}
