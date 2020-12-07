/**
 * 실제 connect는 main에서 bluetooth 연결 확인 후 !
 */
package com.standingegg.band.bluetooth;

import com.standingegg.band.MainActivity;
import com.standingegg.band.R;
import com.standingegg.band.login.LoginActivity;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.ULog;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class BandConnect extends Activity {

	// Return Intent extra
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	public static String EXTRA_DEVICE_UUID = "device_uuid";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.actionbar_device_conn_4);

		Intent getIntent = getIntent();
		final String address = getIntent.getStringExtra(EXTRA_DEVICE_ADDRESS);
		final String uuid = getIntent.getStringExtra(EXTRA_DEVICE_UUID);

		setContentView(R.layout.device_connect_4);

		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				ULog.d("ADDRESS===========>" + address);
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				// 추후에 extra 말고 bundle file 로 따로 빼서 수정****
				intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
				intent.putExtra(EXTRA_DEVICE_UUID, uuid);
				// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

				startActivity(intent);
				finish();



				//20160727 start
				/*Intent intent = new  Intent(getApplicationContext(), MainActivity.class);

				Bundle login_data = new Bundle();
				login_data.putString(Constants.EXTRA_DEVICE_ADDRESS, address);
				login_data.putString(Constants.USER_ID, "test");
				login_data.putString(Constants.USER_PW, "1111");
				login_data.putString(Constants.USER_NAME, "taehoon");
				//login_data.putInt(Constants.UID, Integer.parseInt(uuid));
				intent.putExtras(login_data);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();*/
				//20160727 end
			}
		}, 3000);

	}

}