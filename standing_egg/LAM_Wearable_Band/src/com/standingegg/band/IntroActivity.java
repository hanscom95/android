
package com.standingegg.band;

import java.util.Date;
import java.util.HashMap;

import com.standingegg.band.bluetooth.ConnectionInfo;
import com.standingegg.band.bluetooth.DeviceScanListActivity;
import com.standingegg.band.contents.DBManager;
import com.standingegg.band.ota.File;
import com.standingegg.band.util.AppSettings;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.TodayPreferences;
import com.standingegg.band.util.ULog;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends Activity {

	private static final String TAG = "IntroActivity";

	private static Context mContext;
	private Preferences mPreference = null;
	private TodayPreferences mTodayPreference = null;

	private Button mStartBtn;
	private DBManager dbManager;

	private ConnectionInfo mConnectionInfo = null;
	private BluetoothAdapter mBtAdapter;
	

    public static final String STRSAVEPATH = Environment.getExternalStorageDirectory()+"/LamBand/";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// *
		mContext = this;
		AppSettings.initializeAppSettings(mContext);

		// *
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		mPreference = Preferences.getInstance(mContext);
		mTodayPreference = TodayPreferences.getInstance(mContext);
		// if(mPreference.getVersion() == null) {
		dbManager = new DBManager(getApplicationContext(), "lamband.db", null, 1);

//		dbManager.dropDB();
//		 dbManager.deleteData();
//		 dbManager.deleteData2();
		
		
		// if today date 가 있으면 before date에 저장
		if (mPreference.getVersion() == null) {
			mPreference.setVersion("1.0");

			dbManager.insertUser(mPreference.getUserId(), 
					mPreference.getUserPw(), 
					mPreference.getUserTall(),
					mPreference.getUserWeight(), 
					mPreference.getUserGender(), 
					mConnectionInfo.getDeviceAddress(),
					mPreference.getUserName(), 
					mPreference.getUserBirth(), 
					mPreference.getUserActivityGoal(),
					mPreference.getUserSleepGoal());

		} else {
			// mPreference.setVersion("버전업");
		}
//		mTodayPreference.setTodayDate("2016-01-08", 2016, 1, 8);
		
			//sqlite에 insert
		ULog.d("mTodayPreference.getDataDate():"+mTodayPreference.getDataDate() );
		ULog.v("mTodayPreference.getDataDate():"+mTodayPreference.getDataDate() );
		ULog.i("mTodayPreference.getDataDate():"+mTodayPreference.getDataDate() );
		ULog.e("mTodayPreference.getDataDate():"+mTodayPreference.getDataDate() );
		
		ULog.i("mTodayPreference.getDataYear() :"+mTodayPreference.getDataYear()  );
		ULog.e("mTodayPreference.getDataMonth():"+mTodayPreference.getDataMonth() );
		ULog.e("mTodayPreference.getDataDay():"+mTodayPreference.getDataDay() );

			
		if(mTodayPreference.getDataDate() != null){
			Date today_date = new Date();
			int today_date_y = today_date.getYear()+1900;
			int today_date_m = today_date.getMonth()+1;
			int today_date_d = today_date.getDate();
			
			
			if(today_date_y != mTodayPreference.getDataYear() || 
					today_date_m != mTodayPreference.getDataMonth() || today_date_d != mTodayPreference.getDataDay()){
				//sqlite에 insert
				ULog.d("mTodayPreference.getDataDate():"+mTodayPreference.getDataDate() );
				ULog.v("mTodayPreference.getDataDate():"+mTodayPreference.getDataDate() );
				ULog.i("mTodayPreference.getDataDate():"+mTodayPreference.getDataDate() );
				ULog.e("mTodayPreference.getDataDate():"+mTodayPreference.getDataDate() );
				
				
				
				
				
				

				if(dbManager.dailyActDate(mTodayPreference.getDataDate()) == 0){
					
					
					dbManager.intertTodayData(mTodayPreference.getDataDate(),
							mTodayPreference.getActivityAllCount(), 
							mTodayPreference.getWalkDistance(), 
							mTodayPreference.getWalkMin(),
							mTodayPreference.getWalkCal(), 
							mTodayPreference.getRunDistance(), 
							mTodayPreference.getRunMin(),
							mTodayPreference.getRunCal()
							);
					dbManager.intertTempTodayData(mTodayPreference.getDataDate(),
							mTodayPreference.getActivityAllCount(), 
							mTodayPreference.getWalkDistance(), 
							mTodayPreference.getWalkMin(),
							mTodayPreference.getWalkCal(), 
							mTodayPreference.getRunDistance(), 
							mTodayPreference.getRunMin(),
							mTodayPreference.getRunCal()
							);
					
				}else{
					
					dbManager.updateTempDailyData(mTodayPreference.getDataDate(),
							mTodayPreference.getActivityAllCount(),
							mTodayPreference.getWalkDistance(),
							mTodayPreference.getWalkMin(),
							mTodayPreference.getWalkCal(),
							mTodayPreference.getRunDistance(),
							mTodayPreference.getRunMin(),
							mTodayPreference.getRunCal());
					dbManager.updateDailyData(mTodayPreference.getDataDate(),
							mTodayPreference.getActivityAllCount(), 
							mTodayPreference.getWalkDistance(), 
							mTodayPreference.getWalkMin(),
							mTodayPreference.getWalkCal(), 
							mTodayPreference.getRunDistance(), 
							mTodayPreference.getRunMin(),
							mTodayPreference.getRunCal());
					
				}
				
				
				
				mTodayPreference.resetActivityPreferences();
				
			}
		}
		
	
		
//		수면날짜 / /깊은 수면 시간 (분)/얕은 수면 시간 (분)/잠든 시간 (14:30:21)/깬 시간( 14:30:21)/깨어있는 시간 (분) 
//		dbManager.intertTodaySleepData("2015-12-27", 270, 91 , "11:59:11", "06:00:30", 20) ;
//		dbManager.intertTodaySleepData("2015-12-28", 330, 151 , "10:59:22", "07:00:45", 30) ;
//		dbManager.intertTodaySleepData("2015-12-29", 300, 50 , "1:10:21", "07:00:17", 10) ; //29일에 깬거
//		dbManager.intertTodaySleepData("2015-12-30", 240, 32 , "2:33:00", "07:05:55", 55) ; //30일에 깬거(오늘)
//		
//		dbManager.updateDailyData("2015-12-22", 1, 1, 1, 1, 1, 1, 1);
//		dbManager.intertTodayData("2015-12-22", 12003, 600, 111, 500, 112, 9, 100);
//		dbManager.intertTodayData("2015-12-21", 0, 0, 0, 0, 0, 0, 0);
//		dbManager.intertTodayData("2015-12-20", 7654, 500, 80, 466, 77, 5, 88);
//		dbManager.intertTodayData("2015-12-23", 0, 0, 0, 0, 344, 44, 456);
//		dbManager.intertTodayData("2015-12-24", 0, 0, 0, 0, 0, 0, 0);
//		dbManager.intertTodayData("2015-12-25", 7654, 500, 80, 466, 77, 5, 88);
//		dbManager.intertTodayData("2015-12-26", 7654, 500, 80, 466, 77, 5, 88);
//		dbManager.intertTodayData("2015-12-27", 7654, 500, 80, 466, 77, 5, 88);
//		dbManager.intertTodayData("2015-12-28", 6654, 400, 70, 366, 67, 4, 78);
//		
//		dbManager.intertTodayData("2015-12-29",
//				mTodayPreference.getActivityAllCount(), 
//				mTodayPreference.getWalkDistance(), 
//				mTodayPreference.getWalkMin(),
//				mTodayPreference.getWalkCal(), 
//				mTodayPreference.getRunDistance(), 
//				mTodayPreference.getRunMin(),
//				mTodayPreference.getRunCal()
//				);
//
//		
//		mTodayPreference.resetActivityPreferences();
		
		//sqlite temp table에 data가 있으면 server에 먼저 update

		setContentView(R.layout.intro);

		// RecvSendPacket.signout();

		mStartBtn = (Button) findViewById(R.id.start_btn);
		// final Intent intent = new Intent(getApplicationContext(),
		// LoginActivity.class);

		mConnectionInfo = ConnectionInfo.getInstance(mContext);

		mStartBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mBtAdapter == null) {
					DialogSimple("이 기기는 블루투스를 지원하지 않습니다.",0);
					return;
				}
				
				BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				if (mBluetoothAdapter == null) {
				    // Device does not support Bluetooth
				} else {
				    if (!mBluetoothAdapter.isEnabled()) {
				    	DialogSimple("블루투스를 켜주세요.",2);
				    	return;
				    }
				    else
				    {
				    	try {
							
							ConnectivityManager manager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
							NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
							NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
							         
							if (mobile.isConnected() || wifi.isConnected()){
							      if (mobile.getState() == NetworkInfo.State.CONNECTED || mobile.getState() == NetworkInfo.State.CONNECTING) {
										DialogSimple("Wi-Fi를 제외한 3G, LTE, LTE-A를 사용 서비스를 이용할 경우 추가 요금이 발생할 수 있습니다.",1);
							      }else{
							    	  next();
							      }
							}else{
								DialogSimple("네트워크 신호가 약하거나, \n네트워크가 연결 되어 있지 않습니다. ",0);
								return;
							}

						} catch (NullPointerException e) {
							DialogSimple("네트워크 신호가 약하거나, \n네트워크가 연결 되어 있지 않습니다. ",0);
							return;
						}
				    }
				}
				

			}
		});
		File.createFileDirectories(this);
	}
	
	public void next(){

		// get Login info
		final Intent intent2 = new Intent(getApplicationContext(), DeviceScanListActivity.class);
		final Intent autoLogin = new Intent(getApplicationContext(), MainActivity.class);

		if (mConnectionInfo.getDeviceAddress() != null && mPreference.getUserId() != null) {
			if(mConnectionInfo.getDeviceAddress().length() < 0 || mPreference.getUserId().length() < 0)
				return;
			// mTodayPreference.resetActivityPreferences();
			
			HashMap<String, String> account = dbManager.selectAccunt(mPreference.getUserId());

			ULog.v(account.get("TALL") + " , " + account.get("WEIGHT") + " , " + account.get("GENDER") + " , "
					+ account.get("DEVICE_ADDRESS") + " , " + account.get("NAME") + " , " + account.get("BIRTH")
					+ " , " + account.get("ACTIVITY_GOAL") + " , " + account.get("SLEEP_GOAL"));

			// * @param date
			// * @param all_cnt
			// * all_dis = walk_dis+run_dis
			// * all_cal = walk_cal+run_cal
			// * @param walk_dis
			// * @param walk_tim
			// * @param walk_cal
			// * @param run_dis
			// * @param run_tim
			// * @param run_cal

			// dbManager.intertTodayData("2015-12-22",12003,600,111,500,112,9,100);
			// dbManager.intertTodayData("2015-12-21",0,0,0,0,0,0,0);
			// dbManager.intertTodayData("2015-12-20",7654,500,80,466,77,5,88);
//			sleep_date text primary key,  integer, integer,  integer,  text,  text ,  integer
			 Cursor cursor = dbManager.selectDailySleep();
			 while (cursor.moveToNext()) {
				 ULog.d(TAG, "sleep_date:"+cursor.getString(0) +
				 ", total_sleep:" + cursor.getInt(1) +
				 ", deep_sleep:" + cursor.getInt(2) +
				 ", light_sleep:" + cursor.getInt(3) +
				 ", sleep_start:" + cursor.getString(4) +
				 ", sleep_end:" + cursor.getString(5) +
				 ", awake_time:" + cursor.getInt(6)  );
			 }

			Bundle bundle = new Bundle();
			bundle.putString(Constants.EXTRA_DEVICE_ADDRESS, mConnectionInfo.getDeviceAddress());
			bundle.putString(Constants.USER_ID, mPreference.getUserId());
			bundle.putString(Constants.USER_PW, mPreference.getUserPw());
			bundle.putString(Constants.USER_NAME, mPreference.getUserName());
			bundle.putInt(Constants.UID, mPreference.getUid());
			autoLogin.putExtras(bundle);
			autoLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(autoLogin);
			finish();
		} else {
			intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent2);
			finish();
		}
	}
	
	private void DialogSimple(String s,final int flag) {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(s).setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				if(flag == 1) next();
			}
		});
		
		if(flag != 2){
			alt_bld.setNegativeButton("취소", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
//					moveTaskToBack(true);
//					finish();
//					android.os.Process.killProcess(android.os.Process.myPid());
				}
			});
		}
		AlertDialog alert = alt_bld.create();
		alert.setTitle("알림");
		alert.show();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}