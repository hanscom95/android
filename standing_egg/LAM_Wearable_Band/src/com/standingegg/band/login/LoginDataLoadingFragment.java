/**
 * 
 */
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.login;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.standingegg.band.R;
import com.standingegg.band.contents.DBManager;
import com.standingegg.band.packet.RecvSendPacket;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.TodayPreferences;
import com.standingegg.band.util.ULog;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 로그인 정보 로딩
 * 
 * @author cczzee
 */
public class LoginDataLoadingFragment extends Fragment {
	
	private static TextView mLoadingTxt;
	private DBManager dbManager;
	private Bundle bundle;
	private Preferences mPreference = null;
	private Context mContext = null;
	private TodayPreferences mToday = null;

	private String pw = "";
	@Override
	public void setArguments(Bundle args) {
		bundle = args;

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.login_loading, container, false);
		mContext = getActivity().getApplicationContext();
		// Intent intent = getActivity().getIntent();
		// bundle = intent.getExtras();

		dbManager = new DBManager(getActivity().getApplicationContext(), "lamband.db", null, 1);
		mPreference = Preferences.getInstance(getActivity().getApplicationContext());

		mToday = TodayPreferences.getInstance(mContext);
		
		
		mLoadingTxt = (TextView) v.findViewById(R.id.loading_txt);

		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		// if (mTodayPreference.getDataDate()!= null){
		// mPreference.setLastUseDate(mTodayPreference.getDataDate());
		//
		// //???? 해야하나?
		// //mTodayPreference.resetTodayDate();
		// }
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		
		
		pw = bundle.getString(Constants.USER_PW);
		
		setUser();
		
		
		insertNewDataToServer();
		
		
		
		/**
		 * server랑sqlite data가 다를경우 
		 */
		setDatas();
		
		
		// listener 추가 --> Main 호출    -> 이후에 bluetooth  연결 
		

		return v;
	}
	
	
	public static void setConnecingTxt(){
		mLoadingTxt.setText("연결중...");
	}
	

	public static void set(){
		mLoadingTxt.setText("업데이트...");
	}
	
	
	/**
	 * 1. Temp에 있던 daily Act server에 저장 
	 */
	private void insertNewDataToServer() {
		
		try {
			Cursor c = dbManager.selectTempDailyActivity();
			while (c.moveToNext()) {
				Bundle daily_data = new Bundle();
				daily_data.putInt("uid", bundle.getInt(Constants.UID));
				daily_data.putString("daily_date", c.getString(0));
				daily_data.putInt("total_step", c.getInt(1));
				daily_data.putInt("walk_dis", c.getInt(4));
				daily_data.putInt("walk_time", c.getInt(5));
				daily_data.putInt("walk_cal", c.getInt(6));
				daily_data.putInt("run_dist", c.getInt(7));
				daily_data.putInt("run_time", c.getInt(8));
				daily_data.putInt("run_cal", c.getInt(9));
				
				while (true) {
					int r_cd = RecvSendPacket.insertDailyData(daily_data);
					if(r_cd == 200){ 
						//서버로 이전한 temp data 삭제 
						dbManager.removeActDaily(c.getString(0));
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
//		+ "sleep_date text primary key" 0
//		+ ", total_sleep integer" 1
//		+ ", deep_sleep integer" 2
//		+ ", light_sleep integer" 3 
//		+ ", sleep_start text"4 
//		+ ", sleep_end text" 5
//		+ ", awake_time integer) 6
		try {
			Cursor c = dbManager.selectTempDailySleep();
			while (c.moveToNext()) {
				
//				"(int)  uid
//				(int)  total_sleep
//				(int)  deep_sleep
//				(int)  light_sleep
//				(String)  sleep_start
//				(String)  sleep_end
//				(int)  awake_time
//				(String) daily_date"
				
				Bundle daily_data = new Bundle();
				daily_data.putInt("uid", bundle.getInt(Constants.UID));
				daily_data.putString("daily_date", c.getString(0));
				daily_data.putInt("total_sleep", c.getInt(1));
				daily_data.putInt("deep_sleep", c.getInt(2));
				daily_data.putInt("light_sleep", c.getInt(3));
				daily_data.putString("sleep_start", c.getString(4)+":00"); // 서버 수정 응답 이후 :00 제거
				daily_data.putString("sleep_end", c.getString(5)+":00");
				daily_data.putInt("awake_time", c.getInt(6));
				
				while (true) {
					int r_cd = RecvSendPacket.insertSleepDailyData(daily_data);
					if(r_cd == 200){ 
						//서버로 이전한 temp data 삭제 
						dbManager.removeSleepDailyTemp(c.getString(0));
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

	//===> main Activity로 이동!
	private void setDatas() {
		// SQLite의 daily cnt == 서버의 daily cnt 이면 SQLite에서 update 할 필요 없음 -> 그래프
		// 조회는 무조건 SQLite
		JSONObject daily_result;
		JSONObject weekly_result;
		JSONObject monthly_result;

		try {
			daily_result = new JSONObject(RecvSendPacket.getDailyData(bundle.getInt(Constants.UID)));
			JSONArray daily_d = new JSONArray(daily_result.getString("data"));

			ULog.d("XXXXXXXXXXXXXXX", "daily_d.length() ======>" + daily_d.length());
			ULog.d("XXXXXXXXXXXXXXX", "dbManager.selectDailyActCnt() ======>" + dbManager.selectDailyActCnt());

			if (daily_d.length() != dbManager.selectDailyActCnt()) {
				dbManager.deleteData2();
				for(int i=0; i<daily_result.length(); i++){
					JSONObject j = daily_d.getJSONObject(i);
					
					int walk_dist= j.getInt("walk_dist");
					int walk_time= j.getInt("walk_time");
					int walk_cal= j.getInt("walk_cal");
					int run_dist= j.getInt("run_dist");
					int run_time= j.getInt("run_time");
					int run_cal= j.getInt("run_cal");
					int total_dist= j.getInt("total_dist");
					int total_step= j.getInt("total_step");
					int total_cal= j.getInt("total_cal");
					long daily_date= Long.parseLong(j.getString("daily_date"));
					
					ULog.v("Main Search Daily", "UID," + bundle.getInt(Constants.UID));
					ULog.v("Main Search Daily", "walk_dist," + walk_dist);
					ULog.v("Main Search Daily", "walk_time," + walk_time);
					ULog.v("Main Search Daily", "walk_cal," + walk_cal);
					ULog.v("Main Search Daily", "run_dist," + run_dist);
					ULog.v("Main Search Daily", "run_time," + run_time);
					ULog.v("Main Search Daily", "run_cal," + run_cal);
					ULog.v("Main Search Daily", "total_dist," + total_dist);
					ULog.v("Main Search Daily", "total_step," + total_step);
					ULog.v("Main Search Daily", "total_cal," + total_cal);
					ULog.v("Main Search Daily", "daily_date," + daily_date);
					
					Date ddd = new Date(daily_date);

					String y = Integer.toString(ddd.getYear()+1900);
					String m = Integer.toString(ddd.getMonth()+1);
					m = m.length()==1? "0"+m:m;
					String d = Integer.toString(ddd.getDate());
					d = d.length()==1? "0"+d:d;
					
					dbManager.intertTodayData(y+"-"+m+"-"+d, total_step, walk_dist, walk_time, walk_cal, run_dist, run_time, run_cal);
				}
				
				// dbManager.Daily Act update
			}
			// sqlite에  다 넣고 나서
			int allDistance = dbManager.getAllDistance();
			mPreference.setAllDis(allDistance);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		try {
			weekly_result = new JSONObject(RecvSendPacket.getWeeklyData(bundle.getInt(Constants.UID)));
			JSONArray weekly_d = new JSONArray(weekly_result.getString("data"));
			ULog.d("XXXXXXXXXXXXXXX", "weekly_d.length() ======>" + weekly_d.length());
			ULog.d("XXXXXXXXXXXXXXX", "dbManager.selectWeeklyActCnt() ======>" + dbManager.selectWeeklyActCnt());

			
			if (weekly_d.length() != dbManager.selectWeeklyActCnt()) {
				
				// dbManager.weekly insert 
				dbManager.deleteActWeekData();
				for(int i=0; i<weekly_result.length(); i++){
					JSONObject j = weekly_d.getJSONObject(i);
					
					int avg_walk_dist= j.getInt("avg_walk_dist");
					int avg_walk_time= j.getInt("avg_walk_time");
					int avg_walk_cal= j.getInt("avg_walk_cal");
					
					int avg_run_dist= j.getInt("avg_run_dist");
					int avg_run_time= j.getInt("avg_run_time");
					int avg_run_cal= j.getInt("avg_run_cal");
					
					int total_dist= j.getInt("total_dist");
					int total_step= j.getInt("total_step");
					int total_cal= j.getInt("total_cal");
					
					int day_count = j.getInt("day_count");
					
					int avg_total_dist= j.getInt("avg_total_dist");
					int avg_total_step= j.getInt("avg_total_step");
					int avg_total_cal= j.getInt("avg_total_cal");
					
					long weekly_start= Long.parseLong(j.getString("weekly_start"));
					long weekly_end= Long.parseLong(j.getString("weekly_end"));
					
					ULog.v("Main Search Weekly", "UID," + bundle.getInt(Constants.UID));
					ULog.v("Main Search Weekly", "avg_walk_dist," + avg_walk_dist);
					ULog.v("Main Search Weekly", "avg_walk_time," + avg_walk_time);
					ULog.v("Main Search Weekly", "avg_walk_cal," + avg_walk_cal);
					ULog.v("Main Search Weekly", "avg_run_dist," + avg_run_dist);
					ULog.v("Main Search Weekly", "avg_run_time," + avg_run_time);
					ULog.v("Main Search Weekly", "avg_run_cal," + avg_run_cal);
					ULog.v("Main Search Weekly", "total_dist," + total_dist);
					ULog.v("Main Search Weekly", "total_step," + total_step);
					ULog.v("Main Search Weekly", "total_cal," + total_cal);
					ULog.v("Main Search Weekly", "day_count," + day_count);
					ULog.v("Main Search Weekly", "avg_total_dist" + avg_total_dist);
					ULog.v("Main Search Weekly", "avg_total_step," + avg_total_step);
					ULog.v("Main Search Weekly", "avg_total_cal," + avg_total_cal);
					
					Date weekly_start_d = new Date(weekly_start);
					Date weekly_end_d = new Date(weekly_end);
					
					String start_y = Integer.toString(weekly_start_d.getYear()+1900);
					String start_m = Integer.toString(weekly_start_d.getMonth()+1);
					start_m = start_m.length()==1? "0"+start_m:start_m;
					String start_d = Integer.toString(weekly_start_d.getDate());
					start_d = start_d.length()==1? "0"+start_d:start_d;
					
					String end_y = Integer.toString(weekly_end_d.getYear()+1900);
					String end_m = Integer.toString(weekly_end_d.getMonth()+1);
					end_m = end_m.length()==1? "0"+end_m:end_m;
					String end_d = Integer.toString(weekly_end_d.getDate());
					end_d = end_d.length()==1? "0"+end_d:end_d;
					
					
					ULog.v("Main Search Weekly Start", "weekly_start," + start_y+"-"+start_m+"-"+start_d);
					ULog.v("Main Search Weekly End", "weekly_end," + end_y+"-"+end_m+"-"+end_d);
					
					dbManager.insertWeekyData(
							start_y+"-"+start_m+"-"+start_d, 
							end_y+"-"+end_m+"-"+end_d, 
							avg_walk_dist, 
							avg_walk_time,
							avg_walk_cal, 
							avg_run_dist, 
							avg_run_time, 
							avg_run_cal, 
							day_count,
							total_dist, 
							total_step, 
							total_cal, 
							avg_total_dist,
							avg_total_step,
							avg_total_cal);
				}
				
				
				
			
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			monthly_result = new JSONObject(RecvSendPacket.getMontlyData(bundle.getInt(Constants.UID)));
			JSONArray monthly_d = new JSONArray(monthly_result.getString("data"));
			
			
			ULog.d("XXXXXXXXXXXXXXX", "monthly_d.length() ======>" + monthly_d.length());
			ULog.d("XXXXXXXXXXXXXXX", "dbManager.selectMontlyActCnt() ======>" + dbManager.selectMontlyActCnt());

			if (monthly_d.length() != dbManager.selectMontlyActCnt()) {
				// dbManager.montly insert
				dbManager.deleteActMonthData();
				for(int i=0; i<monthly_result.length(); i++){
					JSONObject month = monthly_d.getJSONObject(i);
					
					int avg_walk_dist= month.getInt("avg_walk_dist");
					int avg_walk_time= month.getInt("avg_walk_time");
					int avg_walk_cal= month.getInt("avg_walk_cal");
					
					int avg_run_dist= month.getInt("avg_run_dist");
					int avg_run_time= month.getInt("avg_run_time");
					int avg_run_cal= month.getInt("avg_run_cal");
					
					int total_dist= month.getInt("total_dist");
					int total_step= month.getInt("total_step");
					int total_cal= month.getInt("total_cal");
					
					int day_count = month.getInt("day_count");
					
					int avg_total_dist= month.getInt("avg_total_dist");
					int avg_total_step= month.getInt("avg_total_step");
					int avg_total_cal= month.getInt("avg_total_cal");
					
					long monthly_date= Long.parseLong(month.getString("monthly_date"));
					
					ULog.v("Main Search Weekly", "UID," + bundle.getInt(Constants.UID));
					ULog.v("Main Search Weekly", "avg_walk_dist," + avg_walk_dist);
					ULog.v("Main Search Weekly", "avg_walk_time," + avg_walk_time);
					ULog.v("Main Search Weekly", "avg_walk_cal," + avg_walk_cal);
					ULog.v("Main Search Weekly", "avg_run_dist," + avg_run_dist);
					ULog.v("Main Search Weekly", "avg_run_time," + avg_run_time);
					ULog.v("Main Search Weekly", "avg_run_cal," + avg_run_cal);
					ULog.v("Main Search Weekly", "total_dist," + total_dist);
					ULog.v("Main Search Weekly", "total_step," + total_step);
					ULog.v("Main Search Weekly", "total_cal," + total_cal);
					ULog.v("Main Search Weekly", "day_count," + day_count);
					ULog.v("Main Search Weekly", "avg_total_dist" + avg_total_dist);
					ULog.v("Main Search Weekly", "avg_total_step," + avg_total_step);
					ULog.v("Main Search Weekly", "avg_total_cal," + avg_total_cal);
					
					Date monthly_date_d = new Date(monthly_date);
					
					String start_y = Integer.toString(monthly_date_d.getYear()+1900);
					String start_m = Integer.toString(monthly_date_d.getMonth()+1);
					start_m = start_m.length()==1? "0"+start_m:start_m;
					String start_d = Integer.toString(monthly_date_d.getDate());
					start_d = start_d.length()==1? "0"+start_d:start_d;
					
					
					ULog.v("Main Search Weekly Start", "weekly_start," + start_y+"-"+start_m+"-"+start_d);
					
					dbManager.insertMontlyData(
							start_y+"-"+start_m+"-"+start_d, 
							avg_walk_dist, 
							avg_walk_time,
							avg_walk_cal, 
							avg_run_dist, 
							avg_run_time, 
							avg_run_cal, 
							day_count,
							total_dist, 
							total_step, 
							total_cal, 
							avg_total_dist,
							avg_total_step,
							avg_total_cal);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void setSleepData(){
		
	}
	
	
	

	private void setUser() {
		// path /user/info 회원정보 출력
		JSONObject jObject;

		try {
			jObject = new JSONObject(RecvSendPacket.getUserInfo(bundle.getInt(Constants.UID)));

			int result_code = jObject.getInt("result");
			// String msg = jObject.getString("message");
			String data = jObject.getString("data");

			switch (result_code) {
			case 200:
				// "조회 성공"
				JSONObject datas = new JSONObject(data);

				int uid = datas.getInt("id");
				String user_id = datas.getString("user_id");
				String gender = datas.getString("gender");
				String name = datas.getString("name");
				int tall = datas.getInt("tall");
				int weight = datas.getInt("weight");
				long birth = Long.parseLong(datas.getString("birth"));
				int activity_goal = datas.getInt("activity_goal");
				int sleep_goal = datas.getInt("sleep_goal");
				long join_date = Long.parseLong(datas.getString("join_date"));

				String device_address = bundle.getString(Constants.EXTRA_DEVICE_ADDRESS);

				String tallunit = "cm";
				String weightunit = "kg";
				
				Date today_d = new Date();
				Date join_d = new Date(join_date);
				
				ULog.e("Join_y , " + (join_d.getYear()+1900));
				ULog.e("Join_m , " + (join_d.getMonth()+1));
				ULog.e("Join_d , " + join_d.getDate());
				
				ULog.e("today_y , " + (today_d.getYear()+1900));
				ULog.e("today_m , " + (today_d.getMonth()+1));
				ULog.e("today_d , " + today_d.getDate());
				
				
				ULog.e("Join_d long , " + join_d.getTime() );
				ULog.e("today_d long , " + today_d.getTime() );

				long diff = (today_d.getDate() - join_d.getDate());
			ULog.d("diffdiffdiffdiffdiff, " +diff );
			/*	long diffDays;
				if( (diff /( 24 * 60 * 60 * 1000)) == 0 && (diff %( 24 * 60 * 60 * 1000))>0 ){
					diffDays = 1;
				}else{
					 diffDays = (diff /( 24 * 60 * 60 * 1000));
				}*/
				
//				01-08 15:48:27.565: D/LAM Band(14219): diffdiffdiffdiffdiff, 68144564

				Bundle b = new Bundle();
				
				Date birth_date = new Date(birth);
				String y = Integer.toString((birth_date.getYear()+1900));
				String m = Integer.toString((birth_date.getMonth()+1));
				m = m.length()==1? "0"+m:m;
				String d = Integer.toString(birth_date.getDate());
				d = d.length()==1? "0"+d:d;
				
				b.putInt(Constants.UID, uid);
				b.putString(Constants.USER_ID, user_id);
				b.putString(Constants.USER_PW, pw);
				b.putString(Constants.USER_NAME, name);
				b.putString(Constants.USER_GENDER, gender);
				b.putString(Constants.USER_BIRTH, y+"-"+m+"-"+d);
				b.putInt(Constants.USER_TALL, tall);
				b.putInt(Constants.USER_WEIGHT, weight);
				b.putInt(Constants.USER_ACTIVITY_GOAL, activity_goal);
				b.putInt(Constants.USER_SLEEP_GOAL, sleep_goal);
				b.putString(Constants.USER_TALL_UNIT, tallunit);
				b.putString(Constants.USER_WEIGHT_UNIT, weightunit);
				b.putString(Constants.EXTRA_DEVICE_ADDRESS, device_address);
				ULog.v("USER", "사용일/가입일," + diff);
				b.putInt(Constants.PREFERENCE_USING_DATE, (int)diff +1 );

				// 이용일도 같이 조회
				mPreference.setUserInfo(b);

				if (dbManager.selectAccountCnt() > 0) {
					// update
					dbManager.updatePerson(user_id, pw, tall, weight, gender, device_address, name, y+"-"+m+"-"+d,activity_goal, sleep_goal);

				} else {
					dbManager.insertUser(user_id,pw, tall, weight, gender, device_address, name,  y+"-"+m+"-"+d,activity_goal, sleep_goal);
				}

				
				break;
			case 460:
				Toast.makeText(getActivity(), "user id does not exist", 3000);
				// " user id does not exist" / "아이디가 존재하지 않음""
				break;

			default:
				break;
			}

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Toast.makeText(getActivity(), "서버 에러", 3000);
		}

	}

}