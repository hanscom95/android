package com.standingegg.acc.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBManager extends SQLiteOpenHelper {
	public String tall, weight, walk, run, total, goal;

	public String TABLE_WALK = "WALK";
	public String TABLE_RUN = "RUN";
	public String TABLE_TOTAL = "TOTAL";

	public String ACC_KEY_ID = "_ID";

	public DBManager(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// user Account의 count는 항상 1

		db.execSQL("create table total( "
				+ "_key integer,"
				+ "today text primary key,"
				+ "count integer,"
				+ "walkCnt integer,"
				+ "runCnt integer,"
				+ "percent integer,"
				+ "hour integer,"
				+ "goal integer);");

		db.execSQL("create table walk( "
				+ "today text primary key,"
				+ "count integer,"
				+ "hour integer,"
				+ "goal integer);");

		db.execSQL("create table run( "
				+ "today text primary key,"
				+ "count integer,"
				+ "hour integer,"
				+ "goal integer);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void dropDB() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("total", null, null);
		db.delete("walk", null, null);
		db.delete("run", null, null);
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




	/**
	 *
	 * @param today
	 * @param count
	 * @param walkCnt
	 * @param runCnt
	 * @param percent
	 * @param hour
	 * @param goal
	 *
	 *
	 */
	public void insertTotalData(String today, int count, int walkCnt, int runCnt, int percent, int hour, int goal) {
		String _query = "insert into total values((select count(*) from total)+1, '"+today+"', '" + count + "', "+walkCnt+", "+runCnt+", "+percent+", "+hour+", " + goal + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	/**
	 *
	 * @param today
	 * @param count
	 * @param hour
	 * @param goal
	 *
	 *
	 */
	public void insertWalkData(String today ,int count, int hour, int goal) {
		String _query = "insert into walk values('"+today+"', '" + count + "', "+hour+", " + goal + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	/**
	 *
	 * @param today
	 * @param count
	 * @param hour
	 * @param goal
	 *
	 *
	 */
	public void insertRunData(String today ,int count, int hour, int goal) {
		String _query = "insert into run values('"+today+"', '" + count + "', "+hour+", " + goal + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}


	public Cursor selectTotal() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from total;", null);
	}


	public Cursor selectWalk(String today) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from (select substr(today,1,10) as date,sum(count),goal,today, hour from walk group by hour) where date='"+today+"';", null);
	}


	public Cursor selectRun(String today) {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from (select substr(today,1,10) as date,sum(count),goal,today, hour from run group by hour) where date='"+today+"';", null);
	}





	public void removeSelectTotal(String _key) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from total where today='" + _key + "';");
		db.close();
	}


	/*public HashMap<String, String> selectAccount(String id) {
		SQLiteDatabase db = getReadableDatabase();
		HashMap<String, String> account = new HashMap<String, String>();

		Cursor cursor = db.rawQuery("select * from account where _id= '" + id + "';", null);

		while (cursor.moveToNext()) {
			tall = Integer.toString(cursor.getInt(2));
			weight = Integer.toString(cursor.getInt(3));
			gender = cursor.getString(4);
			deviceaddress = cursor.getString(5);
			name = cursor.getString(6);
			birth = cursor.getString(7);
			activitygoal = Integer.toString(cursor.getInt(8));
			sleepgoal = cursor.getString(9);
			uid = cursor.getString(10);

			account.put("tall", tall);
			account.put("weight", weight);
			account.put("gender", gender);
			account.put("device_address", deviceaddress);
			account.put("name", name);
			account.put("birth", birth);
			account.put("activity_goal", activitygoal);
			account.put("sleep_goal", sleepgoal);
			account.put("uid", uid);
		}
		return account;
	}*/

	/**
	 * member id 조회
	 *
	 * @return id
	 */
	public ArrayList<String> selectAccountMember() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM ACCOUNT;", null);
		ArrayList<String> member = new ArrayList<>();
		while (cursor.moveToNext()) {
			member.add(cursor.getString(0));
		}
		return member;
	}

	/**
	 * user info 생성
	 *
	 * @param _id
	 * @param pw
	 * @param tall
	 * @param weight
	 * @param gender
	 * @param deviceAddress
	 * @param name
	 * @param birth
	 * @param activityGoal
	 * @param sleepGoal
	 */
	public void insertUser(String _id, String pw, int tall, int weight, String gender, String deviceAddress,
						   String name, String birth, int activityGoal, int sleepGoal, int uid) {
		String _query = "insert into account values('" + _id + "', '" + pw + "', " + tall + ", " + weight + ", '"
				+ gender + "', '" + deviceAddress + "', '" + name + "', '" + birth + "', " + activityGoal + ", '"
				+ sleepGoal + "', '"+uid+"');";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	/**
	 * 사용 계정 변경
	 *
	 * @param _id
	 * @param pw
	 * @param tall
	 * @param weight
	 * @param gender
	 * @param deviceAddress
	 * @param name
	 * @param birth
	 * @param activityGoal
	 * @param sleepGoal
	 */
	public void updatePerson(String _id, String pw, int tall, int weight, String gender, String deviceAddress,
							 String name, String birth, int activityGoal, int sleepGoal) {
		String _query = "insert into account values('" + _id + "', '" + pw + "', " + tall + ", " + weight + ", '"
				+ gender + "', '" + deviceAddress + "', '" + name + "', '" + birth + "', " + activityGoal + ", "
				+ sleepGoal + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from account;"); // 기존 data 삭제
		db.execSQL(_query); // 새 정보 insert
		db.close();
	}

	/**
	 * 유저 정보 변경
	 *
	 * @param _id
	 * @param pw
	 * @param tall
	 * @param weight
	 * @param gender
	 * @param deviceAddress
	 * @param name
	 * @param birth
	 * @param activityGoal
	 * @param sleepGoal
	 */
	public void updateUserInfo(String _id, String pw, int tall, int weight, String gender, String deviceAddress,
							   String name, String birth, int activityGoal, String sleepGoal) {
		String _query = "insert into account values('" + _id + "', '" + pw + "', " + tall + ", " + weight + ", '"
				+ gender + "', '" + deviceAddress + "', '" + name + "', '" + birth + "', " + activityGoal + ", '"
				+ sleepGoal + "');";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}



	/**
	 * 유저 정보 변경
	 *
	 * @param _id
	 * @param pw
	 * @param tall
	 * @param weight
	 * @param gender
	 * @param deviceAddress
	 * @param name
	 * @param birth
	 */
	public void updateUserInfo(String _id, String pw, int tall, int weight, String gender, String deviceAddress,
							   String name, String birth) {
		String _query = "update account set"
				+ " pw = '" + pw + "', tall = " + tall + ", weight = " + weight + ", gender = '" + gender + "', deviceaddress = '" + deviceAddress + "', name = '" + name + "', birth = '" + birth +"'"
				+ " where _id = '"+_id+"';";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	/**
	 *
	 * @param _id
	 * @param date
	 * @param act_hour
	 * @param all_cnt
	 * @param all_distance
	 * @param all_cal
	 *
	 *
	 */
	public void intertTodayData(String _id ,String date, int act_hour, int all_cnt, int all_distance, int all_cal) {
		String _query = "insert into daily_act values('"+_id+"', (select count(*) from daily_act)+1, '" + date + "', " + act_hour + ", " +all_cnt+ ", "
				+ all_distance + ", " + all_cal + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void updateDailyData(String _id, String date, int act_hour, int all_cnt, int all_distance, int all_cal) {
		String _query = "update daily_act set " +
				"act_hour = ((select act_hour from daily_act where act_date='" + date+ "')+" + act_hour + ")"
				+ ", all_cnt = ((select all_cnt from daily_act where act_date='" + date + "')+"+ all_cnt + ")"
				+ ", all_distance = ((select all_distance from daily_act where act_date='" + date + "')+" + all_distance + ")"
				+ ", all_cal = ((select all_cal from daily_act where act_date='" + date + "')+" + all_cal + ")"
				+ " where _id='"+_id+"' and act_date='" + date + "';";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void intertTempTodayData(String date, int all_cnt, int walk_dis, int walk_tim, int walk_cal, int run_dis,
									int run_tim, int run_cal) {
		String _query = "insert into daily_act_temp values('" + date + "', " + all_cnt + ", " + (walk_dis + run_dis)
				+ ", " + (walk_cal + walk_cal) + ", " + walk_dis + ", " + walk_tim + ", " + walk_cal + ", " + run_dis
				+ ", " + run_tim + ", " + run_cal + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void updateTempDailyData(String date, int all_cnt, int walk_dis, int walk_tim, int walk_cal, int run_dis,
									int run_tim, int run_cal) {
		String _query = "update daily_act_temp set " + "all_cnt = ((select all_cnt from daily_act_temp where act_date='"
				+ date + "')+" + all_cnt + ")" + ", all_dis = ((select all_dis from daily_act_temp where act_date='"
				+ date + "')+" + (walk_dis + run_dis) + ")"
				+ ", all_cal = ((select all_cal from daily_act_temp where act_date='" + date + "')+"
				+ (walk_cal + run_cal) + ")" + ", walk_dis = ((select walk_dis from daily_act_temp where act_date='"
				+ date + "')+" + walk_dis + ")" + ", walk_tim = ((select walk_tim from daily_act_temp where act_date='"
				+ date + "')+" + walk_tim + ")" + ", walk_cal = ((select walk_cal from daily_act_temp where act_date='"
				+ date + "')+" + walk_cal + ")" + ", run_dis = ((select run_dis from daily_act_temp where act_date='"
				+ date + "')+" + run_dis + ")" + ", run_tim = ((select run_tim from daily_act_temp where act_date='"
				+ date + "')+" + run_tim + ")" + ", run_cal = ((select run_cal from daily_act_temp where act_date='"
				+ date + "')+" + run_cal + ")" + " where act_date='" + date + "';";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}



	/**
	 *
	 * @param _id
	 * @param date
	 * @param act_hour
	 * @param all_cnt
	 * @param all_distance
	 * @param all_cal
	 *
	 *
	 */
	public void intertTodayRunData(String _id, String date, int act_hour, int all_cnt, int all_distance, int all_cal) {
		String _query = "insert into daily_run_act values('"+_id+"',(select count(*) from daily_run_act)+1, '" + date + "', " + act_hour + ", " +all_cnt+ ", "
				+ all_distance + ", " + all_cal + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void updateDailyRunData(String _id, String date, int act_hour, int all_cnt, int all_distance, int all_cal) {
		String _query = "update daily_run_act set " +
				"act_hour = ((select act_hour from daily_run_act where act_date='" + date+ "')+" + act_hour + ")"
				+ ", all_cnt = ((select all_cnt from daily_run_act where act_date='" + date + "')+"+ all_cnt + ")"
				+ ", all_distance = ((select all_distance from daily_run_act where act_date='" + date + "')+" + all_distance + ")"
				+ ", all_cal = ((select all_cal from daily_run_act where act_date='" + date + "')+" + all_cal + ")"
				+ " where _id='"+_id+"' and act_date='" + date + "';";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}



	/**
	 *
	 * @param date
	 * @param act_hour
	 * @param all_cnt
	 * @param all_cal
	 *
	 *
	 */
	public void intertTodayHammerData(String date, int act_hour, int all_cnt, int all_cal) {
		String _query = "insert into daily_hammer_motion values((select count(*) from daily_hammer_motion)+1, '" + date + "', " + act_hour + ", " +all_cnt+ ",  " + all_cal + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void updateDailyHammerData(String date, int act_hour, int all_cnt, int all_cal) {
		String _query = "update daily_hammer_motion set " +
				"act_hour = ((select act_hour from daily_hammer_motion where act_date='" + date+ "')+" + act_hour + ")"
				+ ", all_cnt = ((select all_cnt from daily_hammer_motion where act_date='" + date + "')+"+ all_cnt + ")"
				+ ", all_cal = ((select all_cal from daily_hammer_motion where act_date='" + date + "')+" + all_cal + ")"
				+ " where act_date='" + date + "';";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}



	/**
	 *
	 * @param date
	 * @param act_hour
	 * @param all_cnt
	 * @param all_cal
	 *
	 *
	 */
	public void intertTodayShoulderData(String date, int act_hour, int all_cnt, int all_cal) {
		String _query = "insert into daily_shoulder_motion values((select count(*) from daily_shoulder_motion)+1, '" + date + "', " + act_hour + ", " +all_cnt+ ",  " + all_cal + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void updateDailyShoulderData(String date, int act_hour, int all_cnt, int all_cal) {
		String _query = "update daily_shoulder_motion set " +
				"act_hour = ((select act_hour from daily_shoulder_motion where act_date='" + date+ "')+" + act_hour + ")"
				+ ", all_cnt = ((select all_cnt from daily_shoulder_motion where act_date='" + date + "')+"+ all_cnt + ")"
				+ ", all_cal = ((select all_cal from daily_shoulder_motion where act_date='" + date + "')+" + all_cal + ")"
				+ " where act_date='" + date + "';";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public Cursor selectDailyActivity() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from daily_act order by act_date desc;", null);
	}

	public Cursor selectDailyRunActivity() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from daily_run_act order by act_date desc;", null);
	}

	public Cursor selectDailyHammerMotion() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from daily_hammer_motion order by act_date desc;", null);
	}

	public Cursor selectDailyShoulderMotion() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from daily_shoulder_motion order by act_date desc;", null);
	}

	public Cursor selectWeeklyActivity() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from weelky_act order by weekly_start desc;", null);
	}

	public Cursor selectMonthlyActivity() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from monthly_act order by monthly_date desc;", null);
	}

	public Cursor selectTempDailyActivity() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from daily_act_temp order by act_date desc;", null);
	}

	public Cursor selectTempDailySleep() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from daily_sleep_temp order by sleep_date desc;", null);
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

	public int selectDailyRunActCnt() {
		SQLiteDatabase db = getReadableDatabase();
		HashMap<String, String> account = new HashMap<String, String>();
		Cursor cursor = db.rawQuery("select * from daily_run_act;", null);
		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt++;
		}
		return cnt;

	}

	public int selectDailyHammerMotionCnt() {
		SQLiteDatabase db = getReadableDatabase();
		HashMap<String, String> account = new HashMap<String, String>();
		Cursor cursor = db.rawQuery("select * from daily_hammer_motion;", null);
		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt++;
		}
		return cnt;

	}

	public int selectDailyShoulderMotionCnt() {
		SQLiteDatabase db = getReadableDatabase();
		HashMap<String, String> account = new HashMap<String, String>();
		Cursor cursor = db.rawQuery("select * from daily_shoulder_motion;", null);
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

	public Cursor dailyActDate(String _id, String date) {
		SQLiteDatabase db = getReadableDatabase();

		return db.rawQuery("select act_date, act_hour, sum(all_cnt), sum(all_dis), sum(all_cal) from (select * from daily_act where _id='" + _id + "' AND act_date='" + date + "') group by act_hour;", null);
	}

	public Cursor dailyRunActDate(String _id, String date) {
		SQLiteDatabase db = getReadableDatabase();

		return db.rawQuery("select act_date, act_hour, sum(all_cnt), sum(all_dis), sum(all_cal) from (select * from daily_run_act where _id='" + _id + "' AND act_date='" + date + "') group by act_hour;", null);
	}


	/*public int dailyRunActDate(String date) {
		SQLiteDatabase db = getReadableDatabase();

		HashMap<String, String> account = new HashMap<String, String>();
		Cursor cursor = db.rawQuery("select * from daily_run_act where act_date='" + date + "';", null);
		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt++;
		}
		return cnt;
	}*/

	public Cursor dailyHammerMotionDate(String date) {
		SQLiteDatabase db = getReadableDatabase();

		return db.rawQuery("select act_date, act_hour, sum(all_cnt), sum(all_cal) from (select * from daily_hammer_motion where act_date='" + date + "') group by act_hour;", null);
	}

	public Cursor dailyHammerTotalCntDate(String date) {
		SQLiteDatabase db = getReadableDatabase();

		return db.rawQuery("select sum(all_cnt), sum(all_cal) from (select * from daily_hammer_motion where act_date='" + date + "') group by act_date;", null);
	}

	public Cursor dailyShoulderMotionDate(String date) {
		SQLiteDatabase db = getReadableDatabase();

		return db.rawQuery("select act_date, act_hour, sum(all_cnt), sum(all_cal) from (select * from daily_shoulder_motion where act_date='" + date + "') group by act_hour;", null);
	}

	public Cursor dailyShouldeTotalCntDate(String date) {
		SQLiteDatabase db = getReadableDatabase();

		return db.rawQuery("select sum(all_cnt), sum(all_cal) from (select * from daily_shoulder_motion where act_date='" + date + "') group by act_date;", null);
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

	public Cursor selectDailySleep() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from daily_sleep order by sleep_date desc;", null);
	}

	public Cursor selectWeeklySleep() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from weekly_sleep order by weekly_start desc;", null);
	}

	public Cursor selectMonthlySleep() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from monthly_sleep order by monthly_date desc;", null);
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

	public void deleteRunData() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("daily_run_act", null, null);
		db.close();
	}

	public void deleteHammerData() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("daily_hammer_motion", null, null);
		db.close();
	}

	public void deleteShoulderData() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("daily_shoulder_motion", null, null);
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
