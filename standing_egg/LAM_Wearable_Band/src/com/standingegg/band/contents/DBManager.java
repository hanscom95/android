package com.standingegg.band.contents;

import java.util.HashMap;
import java.util.List;

import com.standingegg.band.util.ULog;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
	public String tall, weight, gender, deviceaddress, name, birth, activitygoal, sleepgoal = "";

	public String TABLE_ACC = "ACCOUNT";
	public String TABLE_ACT = "ACTIVITY";
	public String TABLE_SLE = "SLEEP";

	public String ACC_KEY_ID = "_ID";

	public DBManager(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// user Account의 count는 항상 1

		db.execSQL("create table account( _id text primary key,pw text,tall integer,weight integer,gender text"
				+ ",deviceaddress text,name text,birth text,activity_goal integer,sleep_goal integer);");

		// 활동날짜/총걸음합계(걷기+뛰기)/총거리합계/총소모cal합계/ 걸은거리합계/걸은시간합계/걸어서소모한칼로리합계
		// /뛴거리합계/뛴시간합계/뛰어서소모한칼로리합계
		db.execSQL("create table daily_act("
				+ "act_date text primary key"
				+ ", all_cnt integer"
				+ ", all_dis integer"
				+ ", all_cal integer"
				+ ", walk_dis integer, walk_tim integer , walk_cal integer , run_dis integer "
				+ ", run_tim integer, run_cal integer);");

		// server에 안들어가있는데이터 서버에 update 되면 쿼리 전체 delete
		db.execSQL("create table daily_act_temp("
				+ "act_date text primary key, "
				+ "all_cnt integer, "
				+ "all_dis integer, "
				+ "all_cal integer, "
				+ "walk_dis integer, "
				+ "walk_tim integer , "
				+ "walk_cal integer , "
				+ "run_dis integer ,"
				+ "run_tim integer, "
				+ "run_cal integer);");

		// 수면날짜  2015-01-01 /총 수면 시간 (분)/깊은 수면 시간 (분)/얕은 수면 시간 (분)/잠든 시간 (14:30:21)/깬 시간( 14:30:21)/깨어있는 시간 (분)
		db.execSQL("create table daily_sleep("
				+ "sleep_date text primary key, "
				+ "total_sleep integer, "
				+ "deep_sleep integer, "
				+ "light_sleep integer, "
				+ "sleep_start text, "
				+ "sleep_end text, "
				+ "awake_time integer);");
		
		
		// server에 안들어가있는데이터 서버에 update 되면 쿼리 전체 delete
		db.execSQL("create table daily_sleep_temp("
				+ "sleep_date text primary key"
				+ ", total_sleep integer"
				+ ", deep_sleep integer"
				+ ", light_sleep integer"
				+ ", sleep_start text"
				+ ", sleep_end text"
				+ ", awake_time integer);");

		
		
		// server에 안들어가있는데이터 서버에 update 되면 쿼리 전체 delete
		db.execSQL("create table weelky_act("
				+ "weekly_start text not null, "// 0
				+ "weekly_end text not null, "// 1
				+ "avg_walk_dist integer, " //2
				+ "avg_walk_time integer, " //3
				+ "avg_walk_cal integer, " //4
				+ "avg_run_dist integer, " //5
				+ "avg_run_time integer, " //6 
				+ "avg_run_cal integer, " //7
				+ "day_count integer," //8 
				+ "total_dist integer, " //9
				+ "total_time integer, " //10
				+ "total_cal integer," //11
				+ "avg_total_dist integer," //12
				+ "avg_total_step integer," //13
				+ "avg_total_cal integer, primary key(weekly_start,weekly_end )"
				//14
				+ ");");
		
		// server에 안들어가있는데이터 서버에 update 되면 쿼리 전체 delete
		db.execSQL("create table monthly_act("   
				+ "monthly_date text primary key, "  //0
				+ "avg_walk_dist integer, "    //1
				+ "avg_walk_time integer, "    //2
				+ "avg_walk_cal integer, "    //3
				+ "avg_run_dist integer, "	//4
				+ "avg_run_time integer, "	//5
				+ "avg_run_cal integer, "	//6
				+ "day_count integer,"		//7
				+ "total_dist integer, "	//8
				+ "total_step integer, "	//9
				+ "total_cal integer,"		//10
				+ "avg_total_dist integer,"	//11
				+ "avg_total_step integer,"	//12
				+ "avg_total_cal integer"	//13
				+ ");");


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACC);

		// Create tables again
		// onCreate(db);

	}
	
	public void dropDB(){
		//account 
		//daily_act
		//daily_act_temp
		//daily_sleep
		//daily_sleep_temp
		//weelky_act
		//monthly_act
		SQLiteDatabase db = getWritableDatabase();
		db.delete("account" , null, null);
		db.delete( "daily_act", null, null);
		db.delete("daily_act_temp" , null, null);
		db.delete( "daily_sleep", null, null);
		db.delete( "daily_sleep_temp", null, null);
		db.delete( "weelky_act", null, null);
		db.delete( "monthly_act", null, null);
		db.close();
		
	}

	public void insert(String _query) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();

		// "insert into ACCOUNT values('"+id+"', '" + pw + "', '"+tall+"',
		// '"+weight+"', '"+deviceadress+"', '"+name+"', '"++birth"',
		// '"+activityGoal+"', '"sleepGoal"');"
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

	public HashMap<String, String> selectAccunt(String id) {
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

			account.put("tall", tall);
			account.put("weight", weight);
			account.put("gender", gender);
			account.put("device_address", deviceaddress);
			account.put("name", name);
			account.put("birth", birth);
			account.put("activity_goal", activitygoal);
			account.put("sleep_goal", sleepgoal);

			ULog.d("user:id:" + id + ", pw:" + cursor.getString(1) + ", tall:" + tall + ", weight:" + weight
					+ ", gender : " + gender + ", deviceadd:" + deviceaddress + ", name:" + name + ", birth:" + birth
					+ ", acitivity:" + activitygoal + ", sleep:" + sleepgoal);
		}
		return account;
	}

	/**
	 * id count 조회
	 * 
	 * @return cnt
	 */
	public int selectAccountCnt() {
		SQLiteDatabase db = getReadableDatabase();
		// int cnt = 0;
		Cursor cursor = db.rawQuery("SELECT * FROM ACCOUNT;", null);
		// while (cursor.moveToNext()) {
		// cnt = cursor.getInt(0);
		// }
		return cursor.getCount();
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
			String name, String birth, int activityGoal, int sleepGoal) {
		String _query = "insert into account values('" + _id + "', '" + pw + "', " + tall + ", " + weight + ", '"
				+ gender + "', '" + deviceAddress + "', '" + name + "', '" + birth + "', " + activityGoal + ", '"
				+ sleepGoal + "');";
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
	 * 
	 * @param date
	 * @param all_cnt
	 *            all_dis = walk_dis+run_dis all_cal = walk_cal+run_cal
	 * @param walk_dis
	 * @param walk_tim
	 * @param walk_cal
	 * @param run_dis
	 * @param run_tim
	 * @param run_cal
	 */
	public void intertTodayData(String date, int all_cnt, int walk_dis, int walk_tim, int walk_cal, int run_dis,
			int run_tim, int run_cal) {
		String _query = "insert into daily_act values('" + date + "', " + all_cnt + ", " + (walk_dis + run_dis) + ", "
				+ (walk_cal + walk_cal) + ", " + walk_dis + ", " + walk_dis + ", " + walk_cal + ", " + run_dis + ", "
				+ run_tim + ", " + run_cal + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void updateDailyData(String date, int all_cnt, int walk_dis, int walk_tim, int walk_cal, int run_dis,
			int run_tim, int run_cal) {
		String _query = "update daily_act set " +
				"all_cnt = ((select all_cnt from daily_act where act_date='" +  date + "')+" + all_cnt + ")" + 
			    ", all_dis = ((select all_dis from daily_act where act_date='" + date + "')+"+ (walk_dis + run_dis) + ")" + 
				", all_cal = ((select all_cal from daily_act where act_date='" + date+ "')+" + (walk_cal + run_cal) + ")" + 
			    ", walk_dis = ((select walk_dis from daily_act where act_date='"+ date + "')+" + walk_dis + ")" + 
				", walk_tim = ((select walk_tim from daily_act where act_date='"+ date + "')+" + walk_tim + ")" + 
			    ", walk_cal = ((select walk_cal from daily_act where act_date='"+ date + "')+" + walk_cal + ")" + 
				", run_dis = ((select run_dis from daily_act where act_date='" + date+ "')+" + run_dis + ")" + 
			    ", run_tim = ((select run_tim from daily_act where act_date='" + date + "')+"+ run_tim + ")" + 
				", run_cal = ((select run_cal from daily_act where act_date='" + date + "')+"+ run_cal + ")" + 
			    " where act_date='" + date + "';";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void intertTempTodayData(String date, int all_cnt, int walk_dis, int walk_tim, int walk_cal, int run_dis,
			int run_tim, int run_cal) {
		String _query = "insert into daily_act_temp values('" + date + "', " + all_cnt + ", " + (walk_dis + run_dis)
				+ ", " + (walk_cal + walk_cal) + ", " + walk_dis + ", " + walk_dis + ", " + walk_cal + ", " + run_dis
				+ ", " + run_tim + ", " + run_cal + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public void updateTempDailyData(String date, int all_cnt, int walk_dis, int walk_tim, int walk_cal, int run_dis,
			int run_tim, int run_cal) {
		String _query = "update daily_act_temp set " +
				"all_cnt = ((select all_cnt from daily_act_temp where act_date='" +  date + "')+" + all_cnt + ")" + 
			    ", all_dis = ((select all_dis from daily_act_temp where act_date='" + date + "')+"+ (walk_dis + run_dis) + ")" + 
				", all_cal = ((select all_cal from daily_act_temp where act_date='" + date+ "')+" + (walk_cal + run_cal) + ")" + 
			    ", walk_dis = ((select walk_dis from daily_act_temp where act_date='"+ date + "')+" + walk_dis + ")" + 
				", walk_tim = ((select walk_tim from daily_act_temp where act_date='"+ date + "')+" + walk_tim + ")" + 
			    ", walk_cal = ((select walk_cal from daily_act_temp where act_date='"+ date + "')+" + walk_cal + ")" + 
				", run_dis = ((select run_dis from daily_act_temp where act_date='" + date+ "')+" + run_dis + ")" + 
			    ", run_tim = ((select run_tim from daily_act_temp where act_date='" + date + "')+"+ run_tim + ")" + 
				", run_cal = ((select run_cal from daily_act_temp where act_date='" + date + "')+"+ run_cal + ")" + 
			    " where act_date='" + date + "';";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}

	public Cursor selectDailyActivity() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from daily_act order by act_date desc;", null);
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

	public int dailyActDate(String date) {
		ULog.e("INSERT DATE SEARCH ==>" + date);
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
		ULog.e("INSERT DATE SEARCH ==>" + date);
		SQLiteDatabase db = getReadableDatabase();

		HashMap<String, String> account = new HashMap<String, String>();
		Cursor cursor = db.rawQuery("select * from daily_sleep where sleep_date='" + date + "';", null);
		int cnt = 0;
		while (cursor.moveToNext()) {
			cnt++;
		}
		return cnt;
	}
	

//	수면날짜 /총 수면 시간 (분)/깊은 수면 시간 (분)/얕은 수면 시간 (분)/잠든 시간 (14:30:21)/깬 시간( 14:30:21)/깨어있는 시간 (분) 
	public void intertTodaySleepData(String date, int deep_sleep, int light_sleep, String sleep_start, String sleep_end,
			int awake_time) {
		String _query = "insert into daily_sleep values('" + date + "', " + (light_sleep+deep_sleep) + ", " + deep_sleep + ", "
				+ light_sleep + ", '" + sleep_start + "', '" + sleep_end + "', " + awake_time + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}
	public void intertTempTodaySleepData(String date, int deep_sleep, int light_sleep, String sleep_start, String sleep_end,
			int awake_time) {
		String _query = "insert into daily_sleep_temp values('" + date + "', " + (light_sleep+deep_sleep) + ", " + deep_sleep + ", "
				+ light_sleep + ", '" + sleep_start + "', '" + sleep_end + "', " + awake_time + ");";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(_query);
		db.close();
	}
	//수면 update 할일은 ..... 테스트 할때 빼고 없음...
//	public void updateDailySleepData(String date, int deep_sleep, int light_sleep, String sleep_start, String sleep_end,
//			int awake_time) {
//		String _query = "update daily_sleep set " +
//			"total_sleep = ((select total_sleep from daily_sleep where sleep_date='" +  date + "')+" + (deep_sleep+light_sleep) + ")" + 
//		    ", deep_sleep = ((select deep_sleep from daily_sleep where sleep_date='" + date + "')+"+ deep_sleep + ")" + 
//			", light_sleep = ((select light_sleep from daily_sleep where sleep_date='" + date+ "')+" + light_sleep + ")" + 
//		    ", sleep_end = '" + sleep_end + "')" + 
//			", walk_tim = ((select walk_tim from daily_sleep where sleep_date='"+ date + "')+" + walk_tim + ")" + 
//		    ", walk_cal = ((select walk_cal from daily_sleep where sleep_date='"+ date + "')+" + walk_cal + ")" + 
//			", run_dis = ((select run_dis from daily_sleep where sleep_date='" + date+ "')+" + run_dis + ")" + 
//		    ", run_tim = ((select run_tim from daily_sleep where sleep_date='" + date + "')+"+ run_tim + ")" + 
//			", run_cal = ((select run_cal from daily_sleep where sleep_date='" + date + "')+"+ run_cal + ")" + 
//		    " where act_date='" + date + "'";
//		SQLiteDatabase db = getWritableDatabase();
//		db.execSQL(_query);
//		db.close();
//	}
	
	public Cursor selectDailySleep() {
		SQLiteDatabase db = getReadableDatabase();
		return db.rawQuery("select * from daily_sleep order by sleep_date desc;", null);
	}
	public void deleteData() {
		SQLiteDatabase db = getWritableDatabase();
		
		db.delete("daily_sleep", null, null);
		ULog.e("DELETE daily_sleep DATA");
		db.close();
	}
	public void deleteData2() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("daily_act", null, null);
		ULog.e("DELETE daily_act DATA");
		db.close();
	}
	public void deleteActWeekData() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("weelky_act", null, null);
		ULog.e("DELETE weelky_act DATA");
		db.close();
	}
	
	public void deleteActMonthData() {
		SQLiteDatabase db = getWritableDatabase();
		db.delete("monthly_act", null, null);
		ULog.e("DELETE monthly_act DATA");
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
		
		if(cnt!=0)
			return cnt;
		else
			return 0;
	}

	public void removeActDaily(String date) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from daily_act_temp where act_date='"+date+"';"); // 기존 data 삭제
		db.close();
	}
	
	public void removeSleepDailyTemp(String date) {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from daily_sleep_temp where sleep_date='"+date+"';"); // 기존 data 삭제
		db.close();
	}
	
	
	// server에 안들어가있는데이터 서버에 update 되면 쿼리 전체 delete
//	db.execSQL("create table weelky_act("
//			+ "avg_cnt integer, primary key(weekly_start,weekly_end )"
//			+ ");");
	
	public void insertWeekyData(String weekly_start,
								String weekly_end,
								int avg_walk_dist, 
								int avg_walk_time,
								int avg_walk_cal, 
								int avg_run_dist,
								int avg_run_time,
								int avg_run_cal,
								int day_count,
								int total_dist, 
								int total_time,
								int total_cal,
								int avg_total_dist,
								int avg_total_step,
								int avg_total_cal) {
		String _query = "insert into weelky_act values('" 
								   + weekly_start + "', '"  
								   + weekly_end + "', "   
								   + avg_walk_dist + ", "
								   + avg_walk_time + ", "
								   + avg_walk_cal + ", " 
								   + avg_run_dist + ", " 
								   + avg_run_time + ", "
								   + avg_run_cal + ", "
								   + day_count + ", "
								   + total_dist+ ", "
								   + total_time+","
								   + total_cal+","
								   + avg_total_dist+","
								   + avg_total_step+","
								   + avg_total_cal+");";
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
	
	
	// server에 안들어가있는데이터 서버에 update 되면 쿼리 전체 delete
//		db.execSQL("create table monthly_act("
//				+ "avg_walk_dist integer, "
//				+ "avg_walk_time integer, "
//				+ "avg_walk_cal integer, "
//				+ "avg_run_dist integer, "
//				+ "avg_run_time integer, "
//				+ "avg_run_cal integer, "
//				+ "total_dist integer, "
//				+ "total_time integer, "
//				+ "total_cal integer,"
//				+ "avg_cnt integer, primary key(monthly_start,monthly_end )"
//				+ ");");
	
	public void insertMontlyData(String monthly_date,
								 int avg_walk_dist, 
								 int avg_walk_time,
								 int avg_walk_cal, 
								 int avg_run_dist,
								 int avg_run_time,
								 int avg_run_cal,
								 int day_count,
								 int total_dist, 
								 int total_step,
								 int total_cal,
								 int avg_total_dist,
								 int avg_total_step,
								 int avg_total_cal) {
		String _query = "insert into monthly_act values('" 
										+ monthly_date + "', '" 
										+ avg_walk_dist + ", "
										+ avg_walk_time + ", "
										+ avg_walk_cal + ", " 
										+ avg_run_dist + ", " 
										+ avg_run_time + ", "
										+ avg_run_cal + ", "
										+ day_count + ", "
										+ total_dist+ ", "
										+ total_step+","
										+ total_cal+","
										+ avg_total_dist+","
										+ avg_total_step+","
										+ avg_total_cal+");";
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
