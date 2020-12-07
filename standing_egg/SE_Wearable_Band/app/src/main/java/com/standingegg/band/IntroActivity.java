
package com.standingegg.band;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class IntroActivity extends Activity {

	private static final String TAG = "IntroActivity";

	private static Context mContext;
	private Preferences mPreference = null;
	private TodayPreferences mTodayPreference = null;

	// private Button mStartBtn;
	private DBManager dbManager;
	private ViewGroup mIntroLayout;

	private ConnectionInfo mConnectionInfo = null;
	private BluetoothAdapter mBtAdapter;

	private boolean btnClickFlag = false;

	private ArrayList<String> timeLine1Text;
	private ArrayList<String> timeLine2Text;
	private ArrayList<Integer> timeLineFlag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// *
		mContext = this;
		AppSettings.initializeAppSettings(mContext);

		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		mPreference = Preferences.getInstance(mContext);
		mTodayPreference = TodayPreferences.getInstance(mContext);
		//dbManager = new DBManager(getApplicationContext(), "lamband.db", null, 1);
		dbManager = new DBManager(getApplicationContext(), "seband.db", null, 1);
		btnClickFlag = false;
		// dbManager.dropDB();
		// dbManager.deleteData();
		// dbManager.deleteData2();

		// if today date 가 있으면 before date에 저장

		if (mPreference.getVersion() == null) {
			ULog.d("getVersion: null true");
			mPreference.setVersion("1.0");

			dbManager.insertUser(mPreference.getUserId(), mPreference.getUserPw(), mPreference.getUserTall(),
					mPreference.getUserWeight(), mPreference.getUserGender(), mConnectionInfo.getDeviceAddress(),
					mPreference.getUserName(), mPreference.getUserBirth(), mPreference.getUserActivityGoal(),
					mPreference.getUserSleepGoal(), mPreference.getUid());

		} else {
			ULog.d("getVersion: null false");
			// mPreference.setVersion("버전업");
		}
		ULog.d("getVersion:" + mPreference.getVersion());
		// mTodayPreference.setTodayDate("2016-01-09", 2016, 1, 9);

		// sqlite에 insert

		/*if (mTodayPreference.getDataDate() != null) {
			Date today_date = new Date();
			int today_date_y = today_date.getYear() + 1900;
			int today_date_m = today_date.getMonth() + 1;
			int today_date_d = today_date.getDate();

			if (today_date_y != mTodayPreference.getDataYear() || today_date_m != mTodayPreference.getDataMonth()
					|| today_date_d != mTodayPreference.getDataDay()) {

				mTodayPreference.resetTimeLine();
				timeLine1Text = new ArrayList<String>();
				timeLine2Text = new ArrayList<String>();
				timeLineFlag = new ArrayList<Integer>();
				// sqlite에 insert
				ULog.v("mTodayPreference.getDataDate():" + mTodayPreference.getDataDate());
				try {
					Date d = new SimpleDateFormat("yyyyMMdd").parse(mTodayPreference.getDataDate().replaceAll("-", ""));
					if (today_date.getDate() - d.getDate() > 7) {
						timeLine1Text.add(getString(R.string.timeline_start_4) + mPreference.getUserName());
						timeLine2Text.add(getString(R.string.timeline_start_5));
						timeLineFlag.add(0);
					}

					if (!mTodayPreference.getUserActGoalOK() && (today_date.getDate() == (d.getDate() - 1))) {
						mPreference.setAcGoalCnt(0);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if (dbManager.selectDailyActCnt() == 0) {

					dbManager.intertTodayData(mTodayPreference.getDataDate(), mTodayPreference.getWalkHour(),
							mTodayPreference.getWalkCnt(), mTodayPreference.getWalkDistance(),
							mTodayPreference.getWalkCal());
					dbManager.intertTempTodayData(mTodayPreference.getDataDate(),
							mTodayPreference.getActivityAllCount(), mTodayPreference.getWalkDistance(),
							mTodayPreference.getWalkMin(), mTodayPreference.getWalkCal(),
							mTodayPreference.getRunDistance(), mTodayPreference.getRunMin(),
							mTodayPreference.getRunCal());

				} else {

					dbManager.updateTempDailyData(mTodayPreference.getDataDate(),
							mTodayPreference.getActivityAllCount(), mTodayPreference.getWalkDistance(),
							mTodayPreference.getWalkMin(), mTodayPreference.getWalkCal(),
							mTodayPreference.getRunDistance(), mTodayPreference.getRunMin(),
							mTodayPreference.getRunCal());
					dbManager.updateDailyData(mTodayPreference.getDataDate(), mTodayPreference.getWalkHour(),
							mTodayPreference.getWalkCnt(), mTodayPreference.getWalkDistance(),
							mTodayPreference.getWalkCal());
				}

				mTodayPreference.resetActivityPreferences();
				mTodayPreference.resetSleepPreferences();

				if (timeLine1Text != null) {
					try {
						Bundle bundle = new Bundle();
						bundle.putStringArrayList(Constants.TODAY_TIME_LINE_1, timeLine1Text);
						bundle.putStringArrayList(Constants.TODAY_TIME_LINE_2, timeLine2Text);
						bundle.putIntegerArrayList(Constants.TODAY_TIME_LINE_FLAG, timeLineFlag);

						mTodayPreference.setTimeLine(bundle);
					} catch (Exception e) {
					}
				}
			}
		}*/

		// sqlite temp table에 data가 있으면 server에 먼저 update

		setContentView(R.layout.intro);

		mIntroLayout = (ViewGroup) findViewById(R.id.intro_layout);

		mConnectionInfo = ConnectionInfo.getInstance(mContext);

		mIntroLayout.setOnClickListener(new View.OnClickListener() {
					@Override
			public void onClick(View v) {

				if (btnClickFlag)
					return;

				btnClickFlag = true;
				if (mBtAdapter == null) {
					DialogSimple(getString(R.string.bt_ble_not_supported), 0);
					return;
				} else {
					if (!mBtAdapter.isEnabled()) {
						Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
						startActivityForResult(enableBtIntent, Constants.REQUEST_ENABLE_BT);
					}else{
						next();
					}
				}

			}
		});
		File.createFileDirectories(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == Constants.REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK) {

				next();
				/*try {

					ConnectivityManager manager = (ConnectivityManager) mContext
							.getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
					NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

					if (mobile.isConnected() || wifi.isConnected()) {
						if (mPreference.getLTE4GFlag()) {
							next();
						} else {
							if (mobile.getState() == NetworkInfo.State.CONNECTED
									|| mobile.getState() == NetworkInfo.State.CONNECTING) {
								DialogSimple(getString(R.string.data_over), 1);
							} else {
								next();
							}
						}
					} else {
						DialogSimple(getString(R.string.network_connect_fail), 0);
						return;
					}

				} catch (NullPointerException e) {
					DialogSimple(getString(R.string.network_connect_fail), 0);
					return;
				}*/
				
				
//				Toast.makeText(getApplicationContext(), "BlueTooth is now Enabled", Toast.LENGTH_LONG).show();
			}
			if (resultCode == RESULT_CANCELED) {
				btnClickFlag = false;
//				Toast.makeText(getApplicationContext(), "Error occured while enabling.Leaving the application..",
//						Toast.LENGTH_LONG).show();
				/// finish();
			}
		}
	}// onActivityResult

	public void next() {

		// get Login info
		final Intent intent2 = new Intent(getApplicationContext(), DeviceScanListActivity.class);
		final Intent autoLogin = new Intent(getApplicationContext(), MainActivity.class);

		if (mConnectionInfo.getDeviceAddress() != null && mPreference.getUserId() != null) {
			if (mConnectionInfo.getDeviceAddress().length() < 0 || mPreference.getUserId().length() < 0)
				return;
			// mTodayPreference.resetActivityPreferences();

			ULog.d("getUserId : " + mPreference.getUserId());
			ULog.d("getUserNm : " + mPreference.getUserName());
			HashMap<String, String> account = dbManager.selectAccount(mPreference.getUserId());
			int cnt = dbManager.selectAccountCnt();
			ULog.d("cnt : " + cnt);

			/*ULog.v("user account : " + account.get("TALL") + " , " + account.get("WEIGHT") + " , " + account.get("GENDER") + " , "
					+ account.get("DEVICE_ADDRESS") + " , " + account.get("NAME") + " , " + account.get("BIRTH") + " , "
					+ account.get("ACTIVITY_GOAL") + " , " + account.get("SLEEP_GOAL"));*/
			ULog.v("user account : " + account.get("tall") + " , " + account.get("weight") + " , " + account.get("gender") + " , "
					+ account.get("device_address") + " , " + account.get("name") + " , " + account.get("birth") + " , "
					+ account.get("activity_goal") + " , " + account.get("sleep_goal"));

			Bundle bundle = new Bundle();
			bundle.putString(Constants.EXTRA_DEVICE_ADDRESS, mConnectionInfo.getDeviceAddress());
			bundle.putString(Constants.USER_ID, mPreference.getUserId());
			bundle.putString(Constants.USER_PW, mPreference.getUserPw());
			bundle.putString(Constants.USER_NAME, mPreference.getUserName());
			bundle.putInt(Constants.UID, mPreference.getUid());
			autoLogin.putExtras(bundle);
			autoLogin.addFlags(
					Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(autoLogin);
			finish();
		} else {
			intent2.addFlags(
					Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent2);
			finish();
		}
	}

	private void DialogSimple(String s, final int flag) {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);

		final CheckBox mCheckBox;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.checkbox_dialog_layout, null);
		alt_bld.setView(view);

		mCheckBox = (CheckBox) view.findViewById(R.id.dialog_txt);
		mCheckBox.setText(getString(R.string.re_none));

		alt_bld.setMessage(s).setCancelable(false).setPositiveButton(getString(R.string.set),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						btnClickFlag = false;
						if (mCheckBox.isChecked()) {
							mPreference.setLTE4GFlag(true);
						}
						if (flag == 1)
							next();
					}
				});

		if (flag != 2) {
			alt_bld.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					btnClickFlag = false;
					dialog.cancel();
					// moveTaskToBack(true);
					// finish();
					// android.os.Process.killProcess(android.os.Process.myPid());
				}
			});
		}
		AlertDialog alert = alt_bld.create();
		alert.setTitle(getString(R.string.login_arlam));
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