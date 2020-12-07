package artech.com.fivics.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBManager extends SQLiteOpenHelper {
	public String id;
	public String pw;
	public String name;
	public String birth;
	public String email;
	public int tall;
	public int weight;
	public int gender;
	public String date;
	public int age;
	public String device_address;
	public int hand;
	public String nationality;
	public String team;
	public String ip;
	public String etc;

	public String TABLE_ACC = "ACCOUNT";
	public String TABLE_ACT = "ACTIVITY";
	public String TABLE_FIT = "FITNESS";
	public String TABLE_SLE = "SLEEP";
	public String TABLE_HEA = "HEART";
	public String TABLE_GOA = "GOAL";
	public String TABLE_OPT = "OPTION";

	public String KEY_ID = "_ID";

	public DBManager(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			// user Account의 count는 항상 1
			db.execSQL("create table account( "
					+ "_id varchar primary key,"
					+ "pw varchar,"
					+ "name text,"
					+ "birth date,"
					+ "email text,"
					+ "tall integer,"
					+ "weight integer,"
					+ "gender integer,"
					+ "date datetime,"
					+ "age int,"
					+ "deviceaddress text,"
					+ "hand integer,"
					+ "nationality text,"
					+ "team text,"
					+ "ip text,"
					+ "etc integer);");

			//activity walk/run
			db.execSQL("create table activity("
					+ "col varchar primary key, "
					+ "type integer, "
					+ "distance integer, "
					+ "count integer, "
					+ "kcal integer, "
					+ "time time, "
					+ "date date, "
					+ "duration integer);");

			//fitness
			db.execSQL("create table fitness("
					+ "col varchar primary key, "
					+ "motion integer, "
					+ "count integer, "
					+ "kcal integer, "
					+ "time time, "
					+ "date date, "
					+ "duration integer);");

			//목표치 walk/run/fitness
			db.execSQL("create table goal("
					+ "col varchar primary key, "
					+ "motion integer, "
					+ "goal integer, "
					+ "kcal integer);");

			//심박
			db.execSQL("create table heart("
					+ "col varchar primary key, "
					+ "rate integer, "
					+ "time time, "
					+ "date date);");

			//수면
			db.execSQL("create table sleep("
					+ "col varchar primary key, "
					+ "type integer, "
					+ "s_sleep time, "
					+ "e_sleep time, "
					+ "date date);");

			// Archery 옵션
			db.execSQL("create table se_option("
					+ "col varchar primary key, "
					+ "ota_version text, "
					+ "fit_video text, "
					+ "account_date date, "
					+ "date date);");

			//상세옵션
			db.execSQL("create table option("
					+ "col varchar primary key, "
					+ "move integer, "
					+ "start_move time, "
					+ "call integer, "
					+ "sms integer, "
					+ "sns_first text, "
					+ "sns_second text, "
					+ "sns_third text, "
					+ "account_date date, "
					+ "date date);");

			//archery
			db.execSQL("create table archery("
					+ "col varchar primary key, "
					+ "game text, "
					+ "round integer, "
					+ "score_one integer, "
					+ "direction_one integer, "
					+ "score_two integer, "
					+ "direction_two integer, "
					+ "score_three integer, "
					+ "direction_three integer, "
					+ "score_four integer, "
					+ "direction_four integer, "
					+ "score_five integer, "
					+ "direction_five integer, "
					+ "score_six integer, "
					+ "direction_six integer);");

			//archery setting
			db.execSQL("create table archery_setting("
					+ "col varchar primary key, "
					+ "game text, "
					+ "score integer, "
					+ "hit text, "
					+ "xs integer, "
					+ "ten integer, "
					+ "avg integer, "
					+ "type text, "
					+ "distance text, "
					+ "equipment text, "
					+ "sight text, "
					+ "date date, "
					+ "time time, "
					+ "weather text, "
					+ "wind text, "
					+ "name text, "
					+ "nationality text, "
					+ "team text);");

			//archery setting
			db.execSQL("create table archery_bigdata("
					+ "col varchar primary key, "
					+ "setup integer, "
					+ "drawing integer, "
					+ "anchor integer, "
					+ "shooting integer, "
					+ "heartRate integer);");
		}catch (Exception e) {
			dropDB();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void dropDB() {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete("account", null, null);
			db.delete("activity", null, null);
			db.delete("fitness", null, null);
			db.delete("goal", null, null);
			db.delete("heart", null, null);
			db.delete("sleep", null, null);
			db.delete("se_option", null, null);
			db.delete("archery", null, null);
			db.delete("archery_setting", null, null);
			db.delete("archery_bigdata", null, null);
			db.close();
		}catch (Exception e) {
			e.printStackTrace();
		}

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

	public HashMap<String, Object> selectAccunt() {
		SQLiteDatabase db = getReadableDatabase();
		HashMap<String, Object> account = new HashMap<String, Object>();

		Cursor cursor = db.rawQuery("select * from account;", null);
		while (cursor.moveToNext()) {
			id = cursor.getString(0);
			pw = cursor.getString(1);
			name = cursor.getString(2);
			birth = cursor.getString(3);
			email = cursor.getString(4);
			tall = cursor.getInt(5);
			weight = cursor.getInt(6);
			gender = cursor.getInt(7);
			date = cursor.getString(8);
			age = cursor.getInt(9);
			device_address = cursor.getString(10);
			hand = cursor.getInt(11);
			nationality = cursor.getString(12);
			team = cursor.getString(13);
			ip = cursor.getString(14);
			etc = cursor.getString(15);

			account.put("id", id);
			account.put("pw", pw);
			account.put("name", name);
			account.put("birth", birth);
			account.put("email", email);
			account.put("tall", tall);
			account.put("weight", weight);
			account.put("gender", gender);
			account.put("date", date);
			account.put("age", age);
			account.put("device_address", device_address);
			account.put("hand", hand);
			account.put("nationality", nationality);
			account.put("team", team);
			account.put("ip", ip);
			account.put("etc", etc);
		}
		return account;
	}

    /**
     *
     * 사용자 추가
     *
     * @param array
     *        _id
     *        pw
     *        name
     *        birth
	 *        email
     *        tall
     *        weight
     *        gender ( 0: 여자 / 1: 남자)
     *        age
     *        device_address
	 *        hand
	 *        nationality
	 *        team
	 *        ip
	 *        etc
     */
	public void insertUser(ArrayList<Object> array) {
		String _query = "insert into account values('" + array.get(14) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3) + "', '"
				+ array.get(0) + "', '" + array.get(4) + "', '" + array.get(5) + "', '" + array.get(6) + "', current_timestamp, '"
				+ array.get(7) + "', '" + array.get(8) + "', '"+ array.get(9) + "', '" + array.get(10) + "', '" + array.get(11) + "', '" + array.get(12) + "', '"  + array.get(13) + "');";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}


	/**
	 *
	 * 사용자 추가
	 *
	 * @param array
	 *        email
	 *        pw
	 *        name
	 *        birth
	 *        gender
	 *        device_address
	 */
	/*public void insertUser(ArrayList<Object> array) {
		String _query = "insert into account values('" + array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3) + "', '"
				+ array.get(0) + "', '0', '0', current_timestamp, '"+ array.get(4) + "', '0', '"+ array.get(5) + "');";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}*/

    /**
     * 사용자 정보 수정
     *
     * @param array
	 *        _id
	 *        pw
	 *        name
	 *        birth
	 *        tall
	 *        weight
	 *        gender
	 *        age
	 *        device_address
	 *        hand
     */
	public void updateUser(ArrayList<Object> array) {
        String _query = "update account set name='" + array.get(2) + "', birth='" + array.get(3) + "', email='"
                + array.get(0) + "', tall='" + array.get(4) + "', weight='" + array.get(5) + "', gender='" + array.get(6) + "', date=current_timestamp, age='"
                + array.get(7) + "', deviceaddress='" + array.get(8) + "', hand='" + array.get(9) + "', nationality='" + array.get(10) + "', team='" + array.get(11) + "', ip='" + array.get(12) +
				"', etc='" + array.get(13) + "'"+ " where _id= '"+array.get(14)+"';";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
	}

    /**
     * acitivty 추가
     *
     * @param array
     *        type
     *        walk/run
     *        distance
     *        count
     *        kcal
     *        time
     *        date
	 *        duration Second
     */
    public void insertActivity(ArrayList<Object> array) {
        String _query = "insert into activity values((select count(col)+1 from activity), '"
                + array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3) + "', '" + array.get(4) + "', '" + array.get(5) + "', '" + array.get(6) + "');";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }


    /**
     * fitness
     *
     * @param array
     *        motion
     *        11가지 동작(추가 될 수도 있음)
     *        count
     *        kcal
     *        time
     *        date
	 *        duration
     */
    public void insertFitness(ArrayList<Object> array) {
        String _query = "insert into fitness values((select count(col)+1 from fitness), '"
                + array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3) + "', '" + array.get(4) + "', '" + array.get(5) + "');";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    /**
     * 목표치 설정
     *
     * @param array
     *        motion
     *        goal
     *        kcal
     */
    public void insertGoal(ArrayList<Object> array) {
        String _query = "insert into goal values((select count(col)+1 from goal), '"
                + array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "');";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    /**
     * 목표치 수정
     *
     * @param array
     *        motion
     *        goal
     *        kcal
     */
    public void updateGoal(ArrayList<Object> array) {
        String _query = "update goal set goal= '" + array.get(1) + "', kcal='" + array.get(2) + "' where motion='" + array.get(0) + "';";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    /**
     * 수면 설정
     *
     * @param array
     *        type
     *        s_sleep
     *        e_sleep
     *        date
     */
    public void insertSleep(ArrayList<Object> array) {
        String _query = "insert into sleep values((select count(col)+1 from sleep), '"
                + array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3) + "');";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    /**
     * 심박 추가
     *
     *        rate
     *        time
     *        date
     */
    public void insertHeart(ArrayList<Object> array) {
        String _query = "insert into heart values((select count(col)+1 from heart), '"
                + array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "');";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

	/**
	 * band option
	 *
	 * @param array
	 *        ota veriosn
	 *        fitness video url link
	 *        계정 생성 날짜
	 *        최종 수정 날짜
	 */
	public void insertOption(ArrayList<Object> array) {
		String _query = "insert into se_option values((select count(col)+1 from se_option), '"
				+ array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3) + "');";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

    /**
     * band option
     *
     * @param array
     *        ota veriosn
     *        fitness video url link
     *        계정 생성 날짜
     *        최종 수정 날짜
     */
    public void updateOption(ArrayList<Object> array) {
        String _query = "update se_option set ota_version='"
                + array.get(0) + "', fit_video='" + array.get(1) + "', account_date='" + array.get(2) + "', date='" + array.get(3) + "');";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }


	/**
	 * archery
	 *
	 * @param array
	 * 		  game
	 *        round
	 *        score_one
	 *        direction_one
	 *        score_two
	 *        direction_two
	 *        score_three
	 *        direction_three
	 *        score_four
	 *        direction_four
	 *        score_five
	 *        direction_five
	 *        score_six
	 *        direction_six
	 */
	public void insertArchery(ArrayList<Object> array) {
		String _query = "insert into archery values((select count(col)+1 from archery), '"
				+ array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3) + "', '" + array.get(4) + "', '" + array.get(5) + "', '" + array.get(6) + "', '" + array.get(7)  + "', '"
				+ array.get(8)  + "', '" + array.get(9) + "', '" + array.get(10) + "', '" + array.get(11) + "', '" + array.get(12) + "', '" + array.get(13)+ "');";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}



	public Cursor selectArchery(String id) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select * from `archery` where `game` = '" + id + "';"
				//where `date` = strftime('%Y-%m-%d', 'now');
				, null);
	}



	public Cursor selectArchery() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select * from `archery`;"
				//where `date` = strftime('%Y-%m-%d', 'now');
				, null);
	}


	public Cursor selectArcherySetting() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select * from `archery_setting`;"
				//where `date` = strftime('%Y-%m-%d', 'now');
				, null);
	}


	public HashMap selectArcherySetting(String id) {
		SQLiteDatabase db = getReadableDatabase();
		HashMap<String, Object> archery = new HashMap<String, Object>();

		Cursor cursor = db.rawQuery("select * from `archery_setting` where `game` = '" + id + "';", null);

		while (cursor.moveToNext()) {
			archery.put("game", cursor.getString(1));
			archery.put("score", cursor.getInt(2));
			archery.put("hit", cursor.getString(3));
			archery.put("xs", cursor.getInt(4));
			archery.put("ten", cursor.getInt(5));
			archery.put("avg", cursor.getInt(6));
			archery.put("type", cursor.getString(7));
			archery.put("distance", cursor.getString(8));
			archery.put("equipment", cursor.getString(9));
			archery.put("sight", cursor.getString(10));
			archery.put("date", cursor.getString(11));
			archery.put("time", cursor.getString(12));
			archery.put("weather", cursor.getString(13));
			archery.put("wind", cursor.getString(14));
			archery.put("name", cursor.getString(15));
			archery.put("nationality", cursor.getString(16));
			archery.put("team", cursor.getString(17));
		}
		return archery;
	}


	/**
	 * archery setting
	 *
	 * @param array
	 * 		  game
	 *        score
	 *        hit
	 *        xs
	 *        ten
	 *        avg
	 *        type
	 *        distance
	 *        equipment
	 *        sight
	 *        date
	 *        time
	 *        weather
	 *        wind
	 *        name
	 *        nationality
	 *        team
	 */
	public void insertArcherySetting(ArrayList<Object> array) {
		String _query = "insert into archery_setting values((select count(col)+1 from archery_setting), '"
				+ array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3) + "', '" + array.get(4) + "', '" + array.get(5) + "', '" + array.get(6) + "', '" + array.get(7)  + "', '"
				+ array.get(8)  + "', '" + array.get(9) + "', '" + array.get(10) + "', '" + array.get(11) + "', '" + array.get(12) + "', '" + array.get(13)+ "', '" + array.get(14)+ "', '" + array.get(15)+ "', '" + array.get(16)   + "');";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}


	/**
	 * archery bigdata
	 *
	 * @param array
	 * 		  setup
	 *        drawing
	 *        anchor
	 *        shootingTime
	 *        heart rate
	 */
	public void insertBigDataSetting(ArrayList<Object> array) {
		String _query = "insert into archery_bigdata values((select count(col)+1 from archery_bigdata), '"
				+ array.get(0) + "', '" + array.get(1) + "', '" + array.get(2) + "', '" + array.get(3) + "', '" + array.get(4) + "');";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void updateBigDataSetting(ArrayList<Object> array) {
		String _query = "update archery_bigdata set setup='" + array.get(0) + "', drawing='" + array.get(1) + "', anchor='" + array.get(2) + "', shooting='" + array.get(3) + "', heartRate='" + array.get(4) +  "';";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}


	public Cursor selectBigDataSetting() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select * from `archery_bigdata`;"
				, null);
	}


	public Cursor selectDailyActivity() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select type, sum(count) as count, sum(distance) as distance, sum(kcal) as kcal, strftime('%H',time) as a_time, date, sum(duration) as duration from `activity` where `date` = strftime('%Y-%m-%d', 'now') group by type, a_time;"
//				"select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '00' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and `time` like '00:%'" + //당일 00시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '00' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '00:%'" + //당일 00시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '01' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '01:%'" + //당일 01시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '01' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '01:%'" + //당일 01시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '02' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '02:%'" + //당일 02시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '02' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '02:%'" + //당일 02시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '03' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '03:%'" + //당일 03시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '03' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '03:%'" + //당일 03시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '04' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '04:%'" + //당일 04시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '04' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '04:%'" + //당일 04시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '05' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '05:%'" + //당일 05시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '05' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '05:%'" + //당일 05시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '06' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '06:%'" + //당일 06시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '06' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '06:%'" + //당일 06시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '07' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '07:%'" + //당일 07시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '07' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '07:%'" + //당일 07시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '08' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '08:%'" + //당일 08시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '08' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '08:%'" + //당일 08시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '09' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '09:%'" + //당일 09시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '09' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '09:%'" + //당일 09시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '10' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '10:%'" + //당일 10시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '10' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '10:%'" + //당일 10시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '11' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '11:%'" + //당일 11시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '11' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '11:%'" + //당일 11시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '12' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '12:%'" + //당일 12시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '12' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '12:%'" + //당일 12시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '13' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '13:%'" + //당일 13시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '13' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '13:%'" + //당일 13시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '14' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '14:%'" + //당일 14시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '14' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '14:%'" + //당일 14시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '15' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '15:%'" + //당일 15시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '15' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '15:%'" + //당일 15시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '16' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '16:%'" + //당일 16시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '16' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '16:%'" + //당일 16시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '17' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '17:%'" + //당일 17시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '17' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '17:%'" + //당일 17시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '18' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '18:%'" + //당일 18시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '18' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '18:%'" + //당일 18시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '19' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '19:%'" + //당일 19시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '19' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '19:%'" + //당일 19시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '20' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '20:%'" + //당일 20시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '20' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '20:%'" + //당일 20시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '21' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '21:%'" + //당일 21시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '21' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '21:%'" + //당일 21시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '22' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '22:%'" + //당일 22시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '22' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '22:%'" + //당일 22시 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '23' as date from `activity` where `type`=0 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '23:%'" + //당일 23시 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '23' as date from `activity` where `type`=1 and `date` = strftime('%Y-%m-%d', 'now') and datetime('now') and `time` like '23:%'"   //당일 23시 run
				, null);
	}

    public Cursor selectWeeklyActivity() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
				"select type, sum(count) as count, sum(distance) as distance, sum(kcal) as kcal, date, sum(duration) as duration from `activity` where `date` between datetime('now', '-7 day') and datetime('now') group by date, type;"
//				"select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m-%d', 'now') as date  from `activity` where `type`=0 and `date` between datetime('now', '-1 day') and datetime('now')" + //당일 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m-%d', 'now') as date  from `activity` where `type`=1 and `date` between datetime('now', '-1 day') and datetime('now')" + //당일 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m-%d', datetime('now', '-1 day')) as date  from `activity` where `type`=0 and `date` between datetime('now', '-2 day') and datetime('now', '-1 day')" + //-1 일 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m-%d', datetime('now', '-1 day')) as date  from `activity` where `type`=1 and `date` between datetime('now', '-2 day') and datetime('now', '-1 day')" + //-1 일 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m-%d', datetime('now', '-2 day')) as date  from `activity` where `type`=0 and `date` between datetime('now', '-3 day') and datetime('now', '-2 day')" + //-2 일 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m-%d', datetime('now', '-2 day')) as date  from `activity` where `type`=1 and `date` between datetime('now', '-3 day') and datetime('now', '-2 day')" + //-2 일 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m-%d', datetime('now', '-3 day')) as date  from `activity` where `type`=0 and `date` between datetime('now', '-4 day') and datetime('now', '-3 day')" + //-3 일 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m-%d', datetime('now', '-3 day')) as date  from `activity` where `type`=1 and `date` between datetime('now', '-4 day') and datetime('now', '-3 day')" + //-3 일 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m-%d', datetime('now', '-4 day')) as date  from `activity` where `type`=0 and `date` between datetime('now', '-5 day') and datetime('now', '-4 day')" + //-4 일 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m-%d', datetime('now', '-4 day')) as date  from `activity` where `type`=1 and `date` between datetime('now', '-5 day') and datetime('now', '-4 day')" + //-4 일 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m-%d', datetime('now', '-5 day')) as date  from `activity` where `type`=0 and `date` between datetime('now', '-6 day') and datetime('now', '-5 day')" + //-5 일 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m-%d', datetime('now', '-5 day')) as date  from `activity` where `type`=1 and `date` between datetime('now', '-6 day') and datetime('now', '-5 day')" + //-5 일 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m-%d', datetime('now', '-6 day')) as date  from `activity` where `type`=0 and `date` between datetime('now', '-7 day') and datetime('now', '-6 day')" + //-6 일 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m-%d', datetime('now', '-6 day')) as date  from `activity` where `type`=1 and `date` between datetime('now', '-7 day') and datetime('now', '-6 day')"	 //-6 일 run
				, null);
    }

	public Cursor selectMonthlyActivity() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery(
				"select type, sum(count) as count, sum(distance) as distance, sum(kcal) as kcal, " +
						"(case when `date` between datetime('now', '-7 day') and datetime('now') then '1'" +
						"	   when `date` between datetime('now', '-14 day') and datetime('now', '-7 day') then '2'" +
						"	   when `date` between datetime('now', '-21 day') and datetime('now', '-14 day') then '3'" +
						"	   when `date` between datetime('now', '-28 day') and datetime('now', '-21 day') then '4'" +
						"	   when `date` between datetime('now', '-35 day') and datetime('now', '-28 day') then '5'" +
						"end) as week, sum(duration) as duration from `activity` where  `date` between datetime('now', '-1 month') and datetime('now') group by week, type;"
//				"select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '1' as date from `activity` where `date` between datetime('now', '-6 day') and datetime('now')" + //당일 주 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '1' as date from `activity` where `type`=1 and `date` between datetime('now', '-6 day') and datetime('now')" + //당일 주 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '2' as date from `activity` where `type`=0 and `date` between datetime('now', '-13 day') and datetime('now', '-6 day')" + //-1 주 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '2' as date from `activity` where `type`=1 and `date` between datetime('now', '-13 day') and datetime('now', '-6 day')" + //-1 주 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '3' as date from `activity` where `type`=0 and `date` between datetime('now', '-20 day') and datetime('now', '-13 day')" + //-2 주 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '3' as date from `activity` where `type`=1 and `date` between datetime('now', '-20 day') and datetime('now', '-13 day')" + //-2 주 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '4' as date from `activity` where `type`=0 and `date` between datetime('now', '-27 day') and datetime('now', '-20 day')" + //-3 주 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '4' as date from `activity` where `type`=1 and `date` between datetime('now', '-27 day') and datetime('now', '-20 day')" + //-3 주 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, '5' as date from `activity` where `type`=0 and `date` between datetime('now', '-34 day') and datetime('now', '-27 day')" + //-4 주 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, '5' as date from `activity` where `type`=1 and `date` between datetime('now', '-34 day') and datetime('now', '-27 day');"   //-4 주 run
				, null);
	}

	public Cursor selectYearlyActivity() {
		SQLiteDatabase db = getReadableDatabase();

		return db.rawQuery(
				"select type, sum(count) as count, sum(distance) as distance, sum(kcal) as kcal, strftime('%Y-%m',date) as month, sum(duration) as duration from `activity` where `date` between datetime('now', '-1 year') and datetime('now') group by month, type;"
//				"select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m', 'now') as date from `activity` where `type`=0 and `date` like '%'||strftime('%Y-%m', 'now')||'%'" + // + //당월 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m', 'now') as date from `activity` where `type`=1 and `date` like '%'||strftime('%Y-%m', 'now')||'%'" + // + //당월 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m', datetime('now', '-1 month')) as date from `activity` where `type`=0 and `date` like '%'||strftime('%Y-%m', datetime('now', '-1 month'))||'%'"+ //-1월 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m', datetime('now', '-1 month')) as date from `activity` where `type`=1 and `date` like '%'||strftime('%Y-%m', datetime('now', '-1 month'))||'%'"+ //-1월 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m', datetime('now', '-2 month')) as date from `activity` where `type`=0 and `date` like '%'||strftime('%Y-%m', datetime('now', '-2 month'))||'%'"+ //-2월 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m', datetime('now', '-2 month')) as date from `activity` where `type`=1 and `date` like '%'||strftime('%Y-%m', datetime('now', '-2 month'))||'%'"+ //-2월 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m', datetime('now', '-3 month')) as date from `activity` where `type`=0 and `date` like '%'||strftime('%Y-%m', datetime('now', '-3 month'))||'%'"+ //-3월 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m', datetime('now', '-3 month')) as date from `activity` where `type`=1 and `date` like '%'||strftime('%Y-%m', datetime('now', '-3 month'))||'%'"+ //-3월 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m', datetime('now', '-4 month')) as date from `activity` where `type`=0 and `date` like '%'||strftime('%Y-%m', datetime('now', '-4 month'))||'%'"+ //-4월 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m', datetime('now', '-4 month')) as date from `activity` where `type`=1 and `date` like '%'||strftime('%Y-%m', datetime('now', '-4 month'))||'%'"+ //-4월 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m', datetime('now', '-5 month')) as date from `activity` where `type`=0 and `date` like '%'||strftime('%Y-%m', datetime('now', '-5 month'))||'%'"+ //-5월 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m', datetime('now', '-5 month')) as date from `activity` where `type`=1 and `date` like '%'||strftime('%Y-%m', datetime('now', '-5 month'))||'%'"+ //-5월 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m', datetime('now', '-6 month')) as date from `activity` where `type`=0 and `date` like '%'||strftime('%Y-%m', datetime('now', '-6 month'))||'%'"+ //-6월 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m', datetime('now', '-6 month')) as date from `activity` where `type`=1 and `date` like '%'||strftime('%Y-%m', datetime('now', '-6 month'))||'%'"+ //-6월 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m', datetime('now', '-7 month')) as date from `activity` where `type`=0 and `date` like '%'||strftime('%Y-%m', datetime('now', '-7 month'))||'%'"+ //-7월 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m', datetime('now', '-7 month')) as date from `activity` where `type`=1 and `date` like '%'||strftime('%Y-%m', datetime('now', '-7 month'))||'%'"+ //-7월 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m', datetime('now', '-8 month')) as date from `activity` where `type`=0 and `date` like '%'||strftime('%Y-%m', datetime('now', '-8 month'))||'%'"+ //-8월 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m', datetime('now', '-8 month')) as date from `activity` where `type`=1 and `date` like '%'||strftime('%Y-%m', datetime('now', '-8 month'))||'%'"+ //-8월 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m', datetime('now', '-9 month')) as date from `activity` where `type`=0 and `date` like '%'||strftime('%Y-%m', datetime('now', '-9 month'))||'%'"+ //-9월 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m', datetime('now', '-9 month')) as date from `activity` where `type`=1 and `date` like '%'||strftime('%Y-%m', datetime('now', '-9 month'))||'%'"+ //-9월 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m', datetime('now', '-10 month')) as date from `activity` where `type`=0 and `date` like '%'||strftime('%Y-%m', datetime('now', '-10 month'))||'%'"+ //-10월 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m', datetime('now', '-10 month')) as date from `activity` where `type`=1 and `date` like '%'||strftime('%Y-%m', datetime('now', '-10 month'))||'%'"+ //-10월 run
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '0' as type, strftime('%Y-%m', datetime('now', '-11 month')) as date from `activity` where `type`=0 and `date` like '%'||strftime('%Y-%m', datetime('now', '-11 month'))||'%'"+ //-11월 walk
//				"union all select ifnull(sum(`count`),0) as count, ifnull(sum(`distance`),0) as distance, ifnull(sum(kcal),0) as kcal, '1' as type, strftime('%Y-%m', datetime('now', '-11 month')) as date from `activity` where `type`=1 and `date` like '%'||strftime('%Y-%m', datetime('now', '-11 month'))||'%';"  //-11월 run
				, null);
	}


    public Cursor selectDailyFitness() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
				"select motion, sum(count) as count, sum(kcal) as kcal, strftime('%H',time) as time, date, sum(duration) as duration from `fitness` where `date` = strftime('%Y-%m-%d', 'now') group by motion, time;"
				, null);
    }

    public Cursor selectWeeklyFitness() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
				"select motion, sum(count) as count, sum(kcal) as kcal, time, date, sum(duration) as duration from `fitness` where `date` between datetime('now', '-7 day') and datetime('now') group by date, motion;"
				, null);
    }

    public Cursor selectMonthlyFitness() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
				"select motion, sum(count) as count, sum(kcal) as kcal, " +
				"(case when `date` between datetime('now', '-7 day') and datetime('now') then '1'" +
				"	   when `date` between datetime('now', '-14 day') and datetime('now', '-7 day') then '2'" +
				"	   when `date` between datetime('now', '-21 day') and datetime('now', '-14 day') then '3'" +
				"	   when `date` between datetime('now', '-28 day') and datetime('now', '-21 day') then '4'" +
				"	   when `date` between datetime('now', '-35 day') and datetime('now', '-28 day') then '5'" +
				"end) as week, sum(duration) as duration from `fitness` where `date` between datetime('now', '-1 month') and datetime('now') group by week, motion;"
				, null);
    }

    public Cursor selectYearlyFitness() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
				"select motion, sum(count) as count, sum(kcal) as kcal, strftime('%Y-%m',date) as month, sum(duration) as duration from `fitness` where `date` between datetime('now', '-1 year') and datetime('now') group by month, motion;"
				, null);
    }


    public Cursor selectGoal() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("select * from `goal`;", null);
    }


    public Cursor selectDailySleep() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
				"select type, s_sleep, e_sleep, strftime('%H:%M', datetime(strftime('%s',e_sleep) - strftime('%s',s_sleep), 'unixepoch')) as sleep, date from `sleep` where `date` between datetime('now', '-1 day') and datetime('now');"
				, null);
    }

    public Cursor selectWeeklySleep() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
				"select type, strftime('%H:%M', datetime(sum(strftime('%s',e_sleep) - strftime('%s',s_sleep)), 'unixepoch')) as sleep, date from `sleep` where `date` between datetime('now', '-7 day') and datetime('now') group by date, type;"
				, null);
    }

    public Cursor selectMonthlySleep() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
				"select type, strftime('%H:%M', datetime(sum(strftime('%s',e_sleep) - strftime('%s',s_sleep)), 'unixepoch')) as sleep," +
				"(case when `date` between datetime('now', '-7 day') and datetime('now') then '1'"+
				"	   when `date` between datetime('now', '-14 day') and datetime('now', '-7 day') then '2'" +
				"	   when `date` between datetime('now', '-21 day') and datetime('now', '-14 day') then '3'" +
				"	   when `date` between datetime('now', '-28 day') and datetime('now', '-21 day') then '4'" +
				"	   when `date` between datetime('now', '-35 day') and datetime('now', '-28 day') then '5'" +
				"end) as week from `sleep` where `date` between datetime('now', '-1 month') and datetime('now') group by week, type;"
				, null);
    }

    public Cursor selectYearlySleep() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
//				"select type, strftime('%H:%M', datetime(sum(strftime('%s',e_sleep) - strftime('%s',s_sleep)), 'unixepoch')) as sleep, strftime('%Y-%m',date) as month from `sleep` where `date` between datetime('now', '-1 year') and datetime('now') group by month, type;"
				"select type, strftime('%H:%M', datetime(sum(strftime('%s',e_sleep) - strftime('%s',s_sleep)), 'unixepoch'))/count(s_sleep) as sleep, strftime('%Y-%m',date) as month from `sleep` where `date` between datetime('now', '-1 year') and datetime('now') group by month, type;"
				, null);
    }


    public Cursor selectDailyHeart() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
				"select avg(rate) as rate, min(rate) as min, max(rate) as max, strftime('%H',time) as hour from `heart` where `date` = strftime('%Y-%m-%d', 'now') group by hour;"
				, null);
    }

    public Cursor selectWeeklyHeart() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
				"select avg(rate) as rate, min(rate) as min, max(rate) as max, date from `heart` where `date` between datetime('now', '-7 day') and datetime('now') group by date;"
				, null);
    }

    public Cursor selectMonthlyHeart() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
				"select avg(rate) as rate, min(rate) as min, max(rate) as max," +
				"(case when `date` between datetime('now', '-7 day') and datetime('now') then '1'"+
				"	   when `date` between datetime('now', '-14 day') and datetime('now', '-7 day') then '2'" +
				"	   when `date` between datetime('now', '-21 day') and datetime('now', '-14 day') then '3'" +
				"	   when `date` between datetime('now', '-28 day') and datetime('now', '-21 day') then '4'" +
				"	   when `date` between datetime('now', '-35 day') and datetime('now', '-28 day') then '5'" +
				"end) as week from `heart` where `date` between datetime('now', '-1 month') and datetime('now') group by week;"
				, null);
    }

    public Cursor selectYearlyHeart() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
				"select avg(rate) as rate, min(rate) as min, max(rate) as max, strftime('%Y-%m',date) as month from `heart` where `date` between datetime('now', '-1 year') and datetime('now') group by month;"
				, null);
    }


	public Cursor selectOption() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from `se_option`;", null);
	}


	public int selectDailyActCnt() {
		SQLiteDatabase db = getReadableDatabase();
		HashMap<String, String> account = new HashMap<String, String>();
		Cursor cursor = db.rawQuery("select * from daily_act;", null);
		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt++;
		}
		return cnt;

	}

	public int selectDailySleepCnt() {
		SQLiteDatabase db = getReadableDatabase();
		HashMap<String, String> account = new HashMap<String, String>();
		Cursor cursor = db.rawQuery("select * from daily_sleep;", null);
		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt++;
		}
		return cnt;

	}

	public int dailyActDate(String date) {
		SQLiteDatabase db = getReadableDatabase();

		HashMap<String, String> account = new HashMap<String, String>();
		Cursor cursor = db.rawQuery("select * from daily_act where act_date='" + date + "';", null);
		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt++;
		}
		return cnt;
	}

	public int dailySleepDate(String date) {
		SQLiteDatabase db = getReadableDatabase();

		HashMap<String, String> account = new HashMap<String, String>();
		Cursor cursor = db.rawQuery("select * from daily_sleep where sleep_date='" + date + "';", null);
		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt++;
		}
		return cnt;
	}

	public Cursor dailySleepTodayData(String date) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from daily_sleep where sleep_date='" + date + "';", null);
	}

	// 수면날짜 /총 수면 시간 (분)/깊은 수면 시간 (분)/얕은 수면 시간 (분)/잠든 시간 (14:30:21)/깬 시간(
	// 14:30:21)/깨어있는 시간 (분)
	public void intertTodaySleepData(String date, int deep_sleep, int light_sleep, String sleep_start, String sleep_end,
									 int awake_time) {
		String _query = "insert into daily_sleep values('" + date + "', " + (light_sleep + deep_sleep) + ", "
				+ deep_sleep + ", " + light_sleep + ", '" + sleep_start + "', '" + sleep_end + "', " + awake_time
				+ ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void intertTempTodaySleepData(String date, int deep_sleep, int light_sleep, String sleep_start,
										 String sleep_end, int awake_time) {
		String _query = "insert into daily_sleep_temp values('" + date + "', " + (light_sleep + deep_sleep) + ", "
				+ deep_sleep + ", " + light_sleep + ", '" + sleep_start + "', '" + sleep_end + "', " + awake_time
				+ ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void updateDailySleepData(String date, int light_sleep) {
		String _query = "update daily_sleep set light_sleep = " + light_sleep + " where sleep_date='" + date + "'";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}
	public void updateTempDailySleepData(String date, int light_sleep) {
		String _query = "update daily_sleep_temp set light_sleep = " + light_sleep + " where sleep_date='" + date + "'";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void deleteGoal() {
		SQLiteDatabase db = getWritableDatabase();

		db.delete("goal", null, null);
		db.close();
	}

	public void deleteData() {
		SQLiteDatabase db = getWritableDatabase();

		db.delete("daily_sleep_temp", null, null);
		db.close();
	}
	public void deleteSleepDaily() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("daily_sleep", null, null);
		db.close();
	}

	public void deleteData2() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("daily_act", null, null);
		db.close();
	}

	public void deleteActWeekData() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("weelky_act", null, null);
		db.close();
	}

	public void deleteSleepWeekData() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("weekly_sleep", null, null);
		db.close();
	}

	public void deleteActMonthData() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("monthly_act", null, null);
		db.close();
	}

	public void deleteSleepMonthData() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("monthly_sleep", null, null);
		db.close();
	}
	
	public int getAllDistance() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select sum(all_dis) from daily_act", null);

		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt = cursor.getInt(0);
		}

		return cnt;
	}

	public int getAvgActCnt() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select avg(all_cnt) from daily_act where all_cnt<>0;", null);

		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt = cursor.getInt(0);
		}

		if (cnt != 0)
			return cnt;
		else
			return 0;
	}

	public void removeActDaily(String date) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from daily_act_temp where act_date='" + date + "';"); // 기존
																					// data
																					// 삭제
		db.close();
	}

	public void removeSleepDailyTemp(String date) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from daily_sleep_temp where sleep_date='" + date + "';"); // 기존
																						// data
																						// 삭제
		db.close();
	}

	public void insertWeekyData(String weekly_start, String weekly_end, int avg_walk_dist, int avg_walk_time,
								int avg_walk_cal, int avg_run_dist, int avg_run_time, int avg_run_cal, int day_count, int total_dist,
								int total_step, int total_cal, int avg_total_dist, int avg_total_step, int avg_total_cal) {
		String _query = "insert into weelky_act values('" + weekly_start + "', '" + weekly_end + "', " + avg_walk_dist
				+ ", " + avg_walk_time + ", " + avg_walk_cal + ", " + avg_run_dist + ", " + avg_run_time + ", "
				+ avg_run_cal + ", " + day_count + ", " + total_dist + ", " + total_step + "," + total_cal + ","
				+ avg_total_dist + "," + avg_total_step + "," + avg_total_cal + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void insertWeeklySleepData(String weekly_start, String weekly_end, int avg_sleep_time, int avg_deep_sleep,
									  int avg_light_sleep, int day_count) {
		String _query = "insert into weekly_sleep values('" + weekly_start + "', '" + weekly_end + "', "
				+ avg_sleep_time + ", " + avg_deep_sleep + ", " + avg_light_sleep + ", " + day_count + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void insertMonthlySleepData(String monthly_date, int avg_sleep_time, int avg_deep_sleep, int avg_light_sleep,
									   int day_count) {
		String _query = "insert into monthly_sleep values('" + monthly_date + "', " + avg_sleep_time + ", "
				+ avg_deep_sleep + ", " + avg_light_sleep + ", " + day_count + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public int selectWeeklyActCnt() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from weelky_act;", null);
		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt++;
		}
		return cnt;
	}

	public int selectWeeklySleepCnt() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from weekly_sleep;", null);
		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt++;
		}
		return cnt;
	}

	public int selectMonthlySleepCnt() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from monthly_sleep;", null);
		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt++;
		}
		return cnt;
	}

	public void insertMontlyData(String monthly_date, int avg_walk_dist, int avg_walk_time, int avg_walk_cal,
								 int avg_run_dist, int avg_run_time, int avg_run_cal, int day_count, int total_dist, int total_step,
								 int total_cal, int avg_total_dist, int avg_total_step, int avg_total_cal) {
		String _query = "insert into monthly_act values('" + monthly_date + "', " + avg_walk_dist + ", "
				+ avg_walk_time + ", " + avg_walk_cal + ", " + avg_run_dist + ", " + avg_run_time + ", " + avg_run_cal
				+ ", " + day_count + ", " + total_dist + ", " + total_step + "," + total_cal + "," + avg_total_dist
				+ "," + avg_total_step + "," + avg_total_cal + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public int selectMontlyActCnt() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from monthly_act;", null);
		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt++;
		}
		return cnt;

	}

	public int selectAllDistance() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select sum(all_dis) from daily_act;", null);
		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt = cursor.getInt(0);
		}
		return cnt;

	}


}
