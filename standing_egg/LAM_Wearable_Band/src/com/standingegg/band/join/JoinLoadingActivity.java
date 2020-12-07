
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class JoinLoadingActivity extends Activity {

	Bundle bundle;
	DBManager dbManager;
	boolean FLAG;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_user_4);

		Intent intent = getIntent();
		bundle = intent.getExtras();
		dbManager = new DBManager(getApplicationContext(), "lamband.db", null, 1);
		FLAG = true;
		connectServer();
	}

	private void connectServer() {

		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
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