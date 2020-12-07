
/**
 * 가입 - 가입완료 ( 회원가입 정보 서버에 업데이트 )
 * @author CCZZZEE
 *
 */
package com.standingegg.band.join;

import com.standingegg.band.MainActivity;
import com.standingegg.band.R;
import com.standingegg.band.contents.DBManager;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.ULog;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

public class JoinLoadingActivity extends Activity {

	Bundle bundle;
	DBManager dbManager;
	private Preferences mPreference = null;
	boolean FLAG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_user_4);

		Intent intent = getIntent();
		bundle = intent.getExtras();
		dbManager = new DBManager(getApplicationContext(), "seband.db", null, 1);
		mPreference = Preferences.getInstance(this);
		FLAG = true;
		connectServer();
	}

	private void connectServer() {

		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				ULog.d("JoinLoadingActivity", "Constants.USER_ID : " + bundle.getString(Constants.USER_ID));
				ULog.d("JoinLoadingActivity", "Constants.USER_PW : " + bundle.getString(Constants.USER_PW));
				ULog.d("JoinLoadingActivity", "Constants.USER_TALL : " + bundle.getInt(Constants.USER_TALL));
				ULog.d("JoinLoadingActivity", "Constants.USER_WEIGHT : " + bundle.getInt(Constants.USER_WEIGHT));
				ULog.d("JoinLoadingActivity", "Constants.USER_GENDER : " + bundle.getString(Constants.USER_GENDER));
				ULog.d("JoinLoadingActivity", "Constants.USER_NAME : " + bundle.getString(Constants.USER_NAME));
				ULog.d("JoinLoadingActivity", "Constants.USER_BIRTH : " + bundle.getString(Constants.USER_BIRTH));
				ULog.d("JoinLoadingActivity", "Constants.USER_ACTIVITY_GOAL : " + bundle.getInt(Constants.USER_ACTIVITY_GOAL));
				ULog.d("JoinLoadingActivity", "Constants.USER_SLEEP_GOAL : " + bundle.getInt(Constants.USER_SLEEP_GOAL));
				ULog.d("JoinLoadingActivity", "Constants.EXTRA_DEVICE_ADDRESS : " + bundle.getString(Constants.EXTRA_DEVICE_ADDRESS));
				mPreference.setUserInfo(bundle);

				//20160802
				dbManager.insertUser(bundle.getString(Constants.USER_ID), bundle.getString(Constants.USER_PW), bundle.getInt(Constants.USER_TALL),
						bundle.getInt(Constants.USER_WEIGHT), bundle.getString(Constants.USER_GENDER), bundle.getString(Constants.EXTRA_DEVICE_ADDRESS),
						bundle.getString(Constants.USER_NAME), bundle.getString(Constants.USER_BIRTH), bundle.getInt(Constants.USER_ACTIVITY_GOAL),
						bundle.getInt(Constants.USER_SLEEP_GOAL), bundle.getInt(Constants.UID));


				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				Bundle login_data = new Bundle();
				login_data.putString(Constants.EXTRA_DEVICE_ADDRESS, bundle.getString(Constants.EXTRA_DEVICE_ADDRESS));
				login_data.putString(Constants.USER_ID, bundle.getString(Constants.USER_ID));
				login_data.putString(Constants.USER_PW, bundle.getString(Constants.USER_PW));
				login_data.putString(Constants.USER_NAME, bundle.getString(Constants.USER_NAME));
				login_data.putInt(Constants.UID, bundle.getInt(Constants.UID));
				login_data.putBoolean(Constants.JOIN_FLAG, true);

				intent.putExtras(login_data);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


				startActivity(intent);
				finish();

			}
		}, 2000);

	}

	@Override
	public void onBackPressed() {
//		android.os.Process.killProcess(android.os.Process.myPid());
		return;
	}
}