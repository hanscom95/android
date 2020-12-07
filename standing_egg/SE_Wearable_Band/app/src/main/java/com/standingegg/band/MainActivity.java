package com.standingegg.band;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.protobuf.UninitializedMessageException;
import com.standingegg.band.activity.ActivityFragment;
import com.standingegg.band.activity.ActivityRunFragment;
import com.standingegg.band.bluetooth.ConnectionInfo;
import com.standingegg.band.contents.DBManager;
import com.standingegg.band.fragment.HeartRateFragment;
import com.standingegg.band.fragment.SensorDataFragment;
import com.standingegg.band.fragment.UserOptionFragment;
import com.standingegg.band.login.LoginDataLoadingFragment;
import com.standingegg.band.motion.HammerFragment;
import com.standingegg.band.motion.MotionFragmentAdapter;
import com.standingegg.band.ota.BluetoothManager;
import com.standingegg.band.ota.DeviceConnectTask;
import com.standingegg.band.sensor.SensorChartFragment;
import com.standingegg.band.service.BTCTemplateService;
import com.standingegg.band.util.AppSettings;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.LogFile;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.RecycleUtils;
import com.standingegg.band.util.StnEggPkt;
import com.standingegg.band.util.TodayPreferences;
import com.standingegg.band.util.ULog;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@ReportsCrashes(formUri = "", // will not be used
		mailTo = "taehoon@standing-egg.co.kr", customReportContent = { ReportField.APP_VERSION_CODE,
		ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA,
		ReportField.STACK_TRACE,
		ReportField.LOGCAT }, mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)
public class MainActivity extends FragmentActivity implements IFragmentListener {

	private static final String TAG = "MainActivity";
	private BackPressCloseHandler backPressCloseHandler;
	private Context mContext;
	public static BTCTemplateService mService;
	private ActivityHandler mActivityHandler;
	private Preferences mPreference = null;
	private TodayPreferences mTodayPreference = null;
	private DBManager dbManager;

	String address,id;
	String pw;
	String name;
	private int uid;
	private boolean JOIN_FLAG;
	private Timer mRefreshTimer = null;
	private Timer mRYMDTTimer = null;
	private Timer mStartTimer = null;
	private Timer mHeartRateTimer = null;

	// UI
	private FragmentManager mFragmentManager;
	private static FragmentAdapter mSectionsPagerAdapter;
	private static MotionFragmentAdapter mMotionSectionsPagerAdapter;
	private ViewPager mActivityViewPager, mMotionViewPager;
	private FrameLayout mActivityFrame, mMotionFrame;
	private RadioGroup mActivityPagerRadioGroup, mMotionPagerRadioGroup;
	private RadioButton mActivityPagerRadioLeft, mActivityPagerRadioRight, mMotionPagerRadioLeft, mMotionPagerRadioRight;

	// test
	private ImageView mImageBT = null;
	private TextView mTextStatus = null;


	// ble 통신
	private boolean connect_flag = false;
	boolean flagSendDT = false;
	boolean flagSendDTOK = false;
	boolean flagRUSER = false;
	boolean flagUR = false;
	boolean flagRA = false;
	boolean flagRS = false;
	// bat
	static boolean flagBAT = false;
	//vsersion
	static boolean flagVersion = false;
	// 알람
	static boolean flagCWUTS = false;

	static boolean feedback = false;

	//fragement change
	boolean flagWalking = false;
	boolean flagRunning = false;
	boolean flagHammer = false;
	boolean flagShoulder = false;
	// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	boolean initialized = false; // Preferences로 빼야함
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★

	static int mActivityAllCount = 0;
	static int mActivityAllM = 0;
	static int mActivityAllCal = 0;

	static int mActivityRunAllCount = 0;
	static int mActivityRunAllM = 0;
	static int mActivityRunAllCal = 0;

	static int mMotionSittingAllCount = 0;
	static int mMotionJumpingAllCount = 0;
	static int mMotionPushUpAllCount = 0;
	static int mMotionSitUpAllCount = 0;
	static int mMotionButterflyAllCount = 0;
	static int mMotionCrunchAllCount = 0;
	static int mMotionHammerAllCount = 0;
	static int mMotionShoulderAllCount = 0;
	static int mMotionUprightRowAllCount = 0;
	static int mMotionToeRaiseAllCount = 0;
	static int mMotionPecFlyAllCount = 0;
	static int mMotionChestPressAllCount = 0;

	// preferences 값
	ArrayList<Integer> mTodayActCnt;  	 //graph 표시  - ActivityTodayDetail
	ArrayList<Integer> mTodayActCntTimeHour; //graph 표시	//시간
	ArrayList<Integer> mTodayActCntTimeMin; //graph 표시   //분 앞자리

	int mWalkDistance = 0; 	//ActivityTodayDetail - 걸은 거리
	int mWalkHour = 0;	    //ActivityTodayDetail - 걸은 시간
	int mWalkMin = 0;	    //ActivityTodayDetail - 걸은 분..? 왜나눴지...?
	int mWalkCal = 0; //ActivityTodayDetail - 걸음으로 소모한 cal량
	int mWalkCnt = 0; //걸음 count

	int mRunDistance = 0; 	 //ActivityTodayDetail - 뛴 거리
	int mRunHour = 0;	   		//ActivityTodayDetail - 뛴 시간
	int mRunMin = 0;	   		//ActivityTodayDetail - 뛴 분
	int mRunCal = 0; 		//ActivityTodayDetail - 뛰어선 소모한 cal량
	int mRunCnt = 0; 		//뛴 걸음 수

	int mHammerCnt = 0; 	//Hammer Curl 수
	int mShoulderCnt = 0; 		//Shoulder Press  수

	int initPacket = 0;

	boolean showLoading =false;

	private boolean mActGoalOK = false;
	private boolean mActGoalTimeLineFalse = false;

	private boolean mMenuDrawableOnOff = false;

	String mToday;

	//connection정보
	private static ConnectionInfo mConnectionInfo = null;

	//main activity
	static boolean flagMainMenu = true;
	static boolean flagUserMenu = false;
	static boolean flagHeartRateMenu = false;
	static boolean flagSensorDataMenu = false;
	static boolean flagSensorChartMenu = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Intent intent = getIntent();

		Bundle d = intent.getExtras();
		address = d.getString(Constants.EXTRA_DEVICE_ADDRESS);
		id = d.getString(Constants.USER_ID);
		pw = d.getString(Constants.USER_PW);
		uid = d.getInt(Constants.UID,0);
		JOIN_FLAG = d.getBoolean(Constants.JOIN_FLAG,false);
		name = d.getString(Constants.USER_NAME, null);

		ULog.e(TAG , "USER_ID*****************" + id +"*************");
		ULog.e(TAG , "UID*****************" + uid +"*************");
		ULog.e(TAG , "PW*****************" + pw +"*************");
		ULog.e(TAG , "JOIN_FLAG*****************" + JOIN_FLAG +"*************");
		ULog.e(TAG , "name*****************" + name +"*************");

		// ----- System, Context
		mContext = this; // .getApplicationContext();
		mActivityHandler = new ActivityHandler();

		mPreference = Preferences.getInstance(mContext);
		mTodayPreference = TodayPreferences.getInstance(mContext);

		// backgound service
		AppSettings.initializeAppSettings(mContext);

		setContentView(R.layout.main);

		// loading fr on
		loadingFragment();

		// actionbar Custom
		connect_flag= false;

		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.action_bar);

		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(false);

		getActionBar().setHomeAsUpIndicator(R.drawable.element_menu);

		dbManager = new DBManager(getApplicationContext(), "seband.db", null, 1);


		mActivityFrame = (FrameLayout) findViewById(R.id.frame_activity);
		mMotionFrame = (FrameLayout) findViewById(R.id.frame_motion);


		backPressCloseHandler = new BackPressCloseHandler(this);


		getActionBar().setHomeButtonEnabled(true);


		// Setup views
		mImageBT = (ImageView) findViewById(R.id.status_title);
		mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
		mTextStatus = (TextView) findViewById(R.id.status_text);
		mTextStatus.setText(getResources().getString(R.string.bt_state_init));

		/**
		 *
		 * test 용!!
		 *
		 *
		 */
		Date today = new Date();

		String ty = Integer.toString(today.getYear() + 1900 - 2000);
		String tmonth = Integer.toString(today.getMonth() + 1);
		String tmm = tmonth.length() == 1 ? "0" + tmonth : tmonth;
		String tdate = Integer.toString(today.getDate());
		String td = tdate.length() == 1 ? "0" + tdate : tdate;


		mToday=ty+""+tmm+"" +td;
		mTodayPreference.setTodayDate(
				("20"+ty+"-"+tmm+"-" +td),
				Integer.parseInt(ty)+2000,
				Integer.parseInt(tmm),
				Integer.parseInt(td));

		mTodayActCnt  = new ArrayList<Integer>();
		mTodayActCntTimeHour = new ArrayList<Integer>();
		mTodayActCntTimeMin = new ArrayList<Integer>();

		mConnectionInfo = ConnectionInfo.getInstance(mContext);
		mConnectionInfo.setDeviceAddress(address);


		setTodayValues();


		// Do data initialization after service started and binded
		doStartService();


//		dbautoupdate();

		if(name==null){
			name = mPreference.getUserName();
		}

	}

	public void setActivityFragment() {
		if(mFragmentManager != null){
			mFragmentManager.getFragments().clear();
		}
		mMotionSectionsPagerAdapter = null;
		/*if(mMotionViewPager != null) {
			mMotionViewPager.removeAllViews();
		}*/



		mFragmentManager = getSupportFragmentManager();
		mSectionsPagerAdapter = new FragmentAdapter(mFragmentManager, mContext, this, mActivityHandler);

		//activity
		mActivityViewPager = (ViewPager) findViewById(R.id.pager_activity);
		mActivityViewPager.setAdapter(mSectionsPagerAdapter);

		mActivityPagerRadioGroup = (RadioGroup) findViewById(R.id.act_pager_radio_button);
		mActivityPagerRadioLeft = (RadioButton) findViewById(R.id.warking_pager);
		mActivityPagerRadioRight = (RadioButton) findViewById(R.id.running_pager);

		mActivityPagerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId)  {
					case R.id.warking_pager:
						mActivityPagerRadioGroup.setBackground(getResources().getDrawable(R.drawable.element_view_pager_left));
						mActivityPagerRadioLeft.setTypeface(Typeface.DEFAULT_BOLD);
						mActivityPagerRadioRight.setTypeface(Typeface.DEFAULT);
						mActivityViewPager.setCurrentItem(0);
						break;
					case R.id.running_pager:
						mActivityPagerRadioGroup.setBackground(getResources().getDrawable(R.drawable.element_view_pager_right));
						mActivityPagerRadioLeft.setTypeface(Typeface.DEFAULT);
						mActivityPagerRadioRight.setTypeface(Typeface.DEFAULT_BOLD);
						mActivityViewPager.setCurrentItem(1);
						break;
					default:
						break;
				}

			}
		});

		mActivityViewPager.setOnPageChangeListener( new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) { // When swiping between pages, select the // corresponding tab.
				switch (position) {
					case 0:
						mActivityPagerRadioLeft.setChecked(true);
						flagWalking = true;
						flagRunning = false;
//					mPagerRadioGroup.setBackground(getResources().getDrawable(R.drawable.element_view_pager_left));
						break;

					case 1:
//					ActivityRunFragment frg = (ActivityRunFragment) mSectionsPagerAdapter .getItem(FragmentAdapter.FRAGMENT_POS_RUN_ACTIVITY);
						//20160822 SleepFragment frg = (SleepFragment) mSectionsPagerAdapter .getItem(FragmentAdapter.FRAGMENT_POS_SLEEP);
						//20160822 frg.updateUi();
						mActivityPagerRadioRight.setChecked(true);
						flagWalking = false;
						flagRunning = true;
//					mPagerRadioGroup.setBackground(getResources().getDrawable(R.drawable.element_view_pager_right));
						break;
				}
			}
		});
	}

	public void setMotionFragment() {
//		mMotionFragmentManager = getChildFragmentManager();
//		mMotionSectionsPagerAdapter = new MotionFragmentAdapter(mMotionFragmentManager, mContext, this, mActivityHandler);


//		mFragmentManager.getFragments();
		if(mFragmentManager != null){
			mFragmentManager.getFragments().clear();
		}
		mSectionsPagerAdapter = null;
		/*if(mMotionViewPager != null) {
			mActivityViewPager.removeAllViews();
		}*/


		mFragmentManager = getSupportFragmentManager();
		mMotionSectionsPagerAdapter = new MotionFragmentAdapter(mFragmentManager, mContext, this, mActivityHandler);
		//motion
		mMotionViewPager = (ViewPager) findViewById(R.id.pager_motion);
		mMotionViewPager.setAdapter(mMotionSectionsPagerAdapter);
		mMotionViewPager.beginFakeDrag();//swipe disable

		mMotionPagerRadioGroup = (RadioGroup) findViewById(R.id.motion_pager_radio_button);
		mMotionPagerRadioLeft = (RadioButton) findViewById(R.id.hammer_pager);
		mMotionPagerRadioRight = (RadioButton) findViewById(R.id.shoulder_pager);


		mMotionPagerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId)  {
					case R.id.hammer_pager:
						mMotionPagerRadioGroup.setBackground(getResources().getDrawable(R.drawable.element_view_pager_left));
						mMotionPagerRadioLeft.setTypeface(Typeface.DEFAULT_BOLD);
						mMotionPagerRadioRight.setTypeface(Typeface.DEFAULT);
						mMotionViewPager.setCurrentItem(0);
						break;
					case R.id.shoulder_pager:
						mMotionPagerRadioGroup.setBackground(getResources().getDrawable(R.drawable.element_view_pager_right));
						mMotionPagerRadioLeft.setTypeface(Typeface.DEFAULT);
						mMotionPagerRadioRight.setTypeface(Typeface.DEFAULT_BOLD);
						mMotionViewPager.setCurrentItem(1);
						break;
					default:
						break;
				}

			}
		});

		mMotionViewPager.setOnPageChangeListener( new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) { // When swiping between pages, select the // corresponding tab.
				switch (position) {
					case 0:
						mMotionPagerRadioLeft.setChecked(true);
						break;

					case 1:
						mMotionPagerRadioRight.setChecked(true);
						break;
				}
			}
		});
	}


	private void setTodayValues() {
		mActivityAllCount = mTodayPreference.getActivityAllCount();
		mActivityAllM = mTodayPreference.getWalkDistance() + mTodayPreference.getRunDistance();
		mActivityAllCal = mTodayPreference.getWalkCal()+mTodayPreference.getRunCal();

		mTodayActCnt = mTodayPreference.getTodayActCnt();
		mTodayActCntTimeHour = mTodayPreference.getTodayActCntTimeHour();
		mTodayActCntTimeMin = mTodayPreference.getTodayActCntTimeMin();


		mWalkDistance = mTodayPreference.getWalkDistance();
		mWalkHour= mTodayPreference.getWalkHour();
		mWalkMin = mTodayPreference.getWalkMin()%60;
		mWalkCal = mTodayPreference.getWalkCal();

		mRunDistance  = mTodayPreference.getWalkDistance();
		mRunHour = mTodayPreference.getRunHour();
		mRunMin = mTodayPreference.getRunMin()%60;
		mRunCal = mTodayPreference.getRunCal();


	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.detail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		ULog.d("onRequestPermissionsResult", "requestCode : " + requestCode);
		switch (requestCode) {
			case 1:

				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					ULog.d("onRequestPermissionsResult", "PERMISSION_GRANTED");
					// 권한 허가
					// 해당 권한을 사용해서 작업을 진행할 수 있습니다
				} else {
					// 권한 거부
					// 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
				}
				return;
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
			case R.id.action:

				if(flagHeartRateMenu) {
					heartRateMessageSend();
					return true;
				}

				if(flagWalking) {
					ActivityFragment ActFrg = (ActivityFragment) mSectionsPagerAdapter.getItem(FragmentAdapter.FRAGMENT_POS_WALK_ACTIVITY);
					ActFrg.chartSampleData();
				}

				if(flagRunning) {
					ActivityRunFragment ActRunFrg = (ActivityRunFragment) mSectionsPagerAdapter.getItem(FragmentAdapter.FRAGMENT_POS_RUN_ACTIVITY);
					ActRunFrg.chartSampleData();
				}

				if(false) {
					ArrayList<String> sample = GetUserContactsList("01099999999");
					ULog.d("sample", "sample 1 : " + sample.get(0));
					ULog.d("sample", "sample 2 : " + sample.get(1));
					ULog.d("sample", "sample 3 : " + sample.get(2));


					flagMainMenu = false;

					totalDataFragment();

					TextView title_sensor = (TextView) getActionBar().getCustomView().findViewById(R.id.action_bar_title);
					title_sensor.setText(R.string.sensor_data_title);
				}

				break;

			case android.R.id.home:
				if(!flagMainMenu) {
					flagMainMenu = true;

					mActivityFrame.setVisibility(View.GONE);
					if(mMotionFrame != null) {
						mMotionFrame.setVisibility(View.GONE);
					}

					if(flagUserMenu || flagHeartRateMenu || flagSensorDataMenu || flagSensorChartMenu) {
						Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame_loading);
						fragmentManager.remove(mFragment);
						mainMenuFragment();
						flagUserMenu = false;
						flagHeartRateMenu = false;
						flagSensorDataMenu = false;
						flagSensorChartMenu = false;
					}

					findViewById(R.id.frame_loading).setVisibility(View.VISIBLE);

					TextView title = (TextView)getActionBar().getCustomView().findViewById(R.id.action_bar_title);
					title.setText(R.string.main_title);
				}
				break;

			default:

		}
		return super.onMenuItemSelected(featureId, item);
	}

	FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();

	public void loadingFragment() {
		//menu bar 슬라이드 막기 
		showLoading = true;

		getActionBar().hide();
		Fragment fragment = new LoginDataLoadingFragment();
		Bundle args = new Bundle();
		args.putString(Constants.EXTRA_DEVICE_ADDRESS, this.address);
		args.putString(Constants.USER_ID, id);
		args.putString(Constants.USER_PW, pw);
		args.putInt(Constants.UID, uid);
		args.putBoolean(Constants.JOIN_FLAG, JOIN_FLAG);
		// args.putInt(LoginDataLoadingFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);
		//((LoginDataLoadingFragment) fragment).setConnecingTxt();

		// Insert the fragment by replacing any existing fragment

		fragmentManager.replace(R.id.frame_loading, fragment).commit();
	}

	public void mainMenuFragment() {
		//menu bar 슬라이드 막기
		showLoading = false;

		Fragment fragment = new MainMenuFragment();
		Bundle args = new Bundle();
		args.putString(Constants.EXTRA_DEVICE_ADDRESS, this.address);
		args.putString(Constants.USER_ID, id);
		args.putString(Constants.USER_PW, pw);
		args.putInt(Constants.UID, uid);
		args.putBoolean(Constants.JOIN_FLAG, JOIN_FLAG);
		// args.putInt(LoginDataLoadingFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);
		//((LoginDataLoadingFragment) fragment).setConnecingTxt();

		// Insert the fragment by replacing any existing fragment

		fragmentManager = getFragmentManager().beginTransaction();
		fragmentManager.replace(R.id.frame_loading, fragment).commit();
	}

	public void userOptionFragment() {
		Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame_loading);
		fragmentManager.remove(mFragment);
		flagUserMenu = true;

		Fragment fragment = new UserOptionFragment();
		Bundle args = new Bundle();
		args.putString(Constants.EXTRA_DEVICE_ADDRESS, this.address);
		args.putString(Constants.USER_ID, id);
		args.putString(Constants.USER_PW, pw);
		args.putInt(Constants.UID, uid);
		args.putBoolean(Constants.JOIN_FLAG, JOIN_FLAG);
		// args.putInt(LoginDataLoadingFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);

		fragmentManager = getFragmentManager().beginTransaction();
		fragmentManager.replace(R.id.frame_loading, fragment).commit();
	}

	public void heartRateFragment() {
		Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame_loading);
		fragmentManager.remove(mFragment);
		flagHeartRateMenu = true;

		Fragment fragment = new HeartRateFragment();

		fragmentManager = getFragmentManager().beginTransaction();
		fragmentManager.replace(R.id.frame_loading, fragment).commit();


		mHeartRateTimer = new Timer();
		mHeartRateTimer.schedule(new HeartRateTimerTask(),3000);
	}

	public void sensorDataFragment() {
		Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame_loading);
		fragmentManager.remove(mFragment);
		flagSensorDataMenu = true;

		Fragment fragment = new SensorDataFragment();

		fragmentManager = getFragmentManager().beginTransaction();
		fragmentManager.replace(R.id.frame_loading, fragment).commit();
	}

	public void totalDataFragment() {
		Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame_loading);
		fragmentManager.remove(mFragment);
		flagSensorChartMenu = true;

		Fragment fragment = new SensorChartFragment();

		fragmentManager = getFragmentManager().beginTransaction();
		fragmentManager.replace(R.id.frame_loading, fragment).commit();
	}

	int cnt = 0;
	private void DialogSimple(String s) {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(s).setCancelable(false).setNegativeButton(getString(R.string.set), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				doStopService();
				android.os.Process.killProcess(android.os.Process.myPid());
				dialog.cancel();
				moveTaskToBack(true);
				finish();
			}
		});
		AlertDialog alert = alt_bld.create();
		alert.setTitle(getString(R.string.login_arlam));
		alert.show();
	}

	public void appFinish() {
		doStopService();
		android.os.Process.killProcess(android.os.Process.myPid());
		moveTaskToBack(true);
		finish();
	}


	@Override
	public synchronized void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		// Stop the timer
		if (mRefreshTimer != null) {
			connect_fail_dialog =false;
			mRefreshTimer.cancel();
			mRefreshTimer = null;
		}
		if (mRYMDTTimer != null) {
			connect_fail_dialog = false;
			mRYMDTTimer.cancel();
			mRYMDTTimer = null;
		}


		if(mStartTimer != null) {
			connect_fail_dialog = false;
			mStartTimer.cancel();
			mStartTimer = null;
		}

		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		finalizeActivity();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		// onDestroy is not always called when applications are finished by
		// Android system.
		finalizeActivity();
	}
	public class BackPressCloseHandler {
		private long backKeyPressedTime = 0;
		private Toast toast;

		private Activity activity;

		public BackPressCloseHandler(Activity context) {
			this.activity = context;
		}
		public void onBackPressed() {
			if(!flagMainMenu) {
				flagMainMenu = true;

				//activity menu
				if(mActivityFrame.getVisibility() == View.VISIBLE) {
					mActivityPagerRadioLeft.setChecked(true);
					mActivityFrame.setVisibility(View.GONE);
				}

				//motion menu
				if(mMotionFrame.getVisibility() == View.VISIBLE) {
					mMotionPagerRadioLeft.setChecked(true);
					mMotionFrame.setVisibility(View.GONE);


					//command
					StnEggPkt.StnEggPacket.Builder sendBuilder = StnEggPkt.StnEggPacket.newBuilder();
					sendBuilder.setSerpCommand(MainActivity.mService.mBleManager.MESSAGE_BAND_MODE_REQUEST);
					sendBuilder.setUserUint01(3);

					try {
						byte[] encoded = new byte[sendBuilder.build().toByteArray().length + 5];
						byte[] builderTobyte = sendBuilder.build().toByteArray();

						encoded[0] = 'S';
						encoded[1] = 'E';
						int i;
						for (i = 0; i < builderTobyte.length; i++) {
							encoded[i + 2] = builderTobyte[i];
						}
						encoded[i + 2] = 'E';
						encoded[i + 3] = '\r';
						encoded[i + 4] = '\n';
						MainActivity.mService.sendMessageToRemote(encoded);

					} catch (UninitializedMessageException e) {
						e.printStackTrace();
					}
				}

				//user menu
				if(flagUserMenu) {
					Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame_loading);
					fragmentManager.remove(mFragment);
					mainMenuFragment();
					flagUserMenu = false;
				}

				//heart rate menu
				if(flagHeartRateMenu) {
					Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame_loading);
					fragmentManager.remove(mFragment);
					mainMenuFragment();
					flagHeartRateMenu = false;

					if(mHeartRateTimer != null) {
						mHeartRateTimer.cancel();
						mHeartRateTimer = null;
					}
				}

				//sensor data menu
				if(flagSensorDataMenu) {
					Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame_loading);
					fragmentManager.remove(mFragment);
					mainMenuFragment();
					flagSensorDataMenu = false;
				}

				//sensor chart menu
				if(flagSensorChartMenu) {
					Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame_loading);
					fragmentManager.remove(mFragment);
					mainMenuFragment();
					flagSensorChartMenu = false;


					//command
					StnEggPkt.StnEggPacket.Builder sendBuilder = StnEggPkt.StnEggPacket.newBuilder();
					sendBuilder.setSerpCommand(MainActivity.mService.mBleManager.MESSAGE_BAND_MODE_REQUEST);
					sendBuilder.setUserUint01(3);

					try {
						byte[] encoded = new byte[sendBuilder.build().toByteArray().length + 5];
						byte[] builderTobyte = sendBuilder.build().toByteArray();

						encoded[0] = 'S';
						encoded[1] = 'E';
						int i;
						for (i = 0; i < builderTobyte.length; i++) {
							encoded[i + 2] = builderTobyte[i];
						}
						encoded[i + 2] = 'E';
						encoded[i + 3] = '\r';
						encoded[i + 4] = '\n';
						MainActivity.mService.sendMessageToRemote(encoded);

					} catch (UninitializedMessageException e) {
						e.printStackTrace();
					}
				}

				findViewById(R.id.frame_loading).setVisibility(View.VISIBLE);

				TextView title = (TextView)getActionBar().getCustomView().findViewById(R.id.action_bar_title);
				title.setText(R.string.main_title);
				return;
			}


			if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
				backKeyPressedTime = System.currentTimeMillis();
				showGuide();
				return;
			}
			if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
				doStopService();
				android.os.Process.killProcess(android.os.Process.myPid());
				moveTaskToBack(true);
				finalizeActivity();
				finish();
				toast.cancel();
			}
		}
		private void showGuide() {
			toast = Toast.makeText(activity,getString(R.string.backpressed),
					Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	@Override
	public void onBackPressed() {
		if(mMenuDrawableOnOff){
//			mDrawerLayout.closeDrawer(mMenuDrawer);
			return;
		}

		backPressCloseHandler.onBackPressed();
	}

	boolean connection = false;

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
//		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
//		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		if (mDrawerToggle.onOptionsItemSelected(item)) {
//			return true;
//		}
		switch (item.getItemId()) {
			case R.id.action:

				Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame_loading);
				TextView logText = (TextView)mFragment.getActivity().findViewById(R.id.log_text);
				String logData = logText.getText().toString();

				item.setTitle("Sleep Log");
				LogFile.main(logData);
				break;
		};

		return super.onOptionsItemSelected(item);
	}

	public void findBand() {
		if (mService != null) {
			mService.sendMessageToRemote("VIB");
		}
	}

	/**
	 * Service connection
	 */
	private ServiceConnection mServiceConn = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			ULog.d(TAG, "Activity - Service connected");

			mService = ((BTCTemplateService.ServiceBinder) binder).getService();

			// Activity couldn't work with mService until connections are made
			// So initialize parameters and settings here. Do not initialize
			// while running onCreate()
			initialize();
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
		}
	};

	/**
	 * Start service if it's not running
	 */
	private void doStartService() {
		ULog.d("# Activity - doStartService()");
		startService(new Intent(this, BTCTemplateService.class));
		bindService(new Intent(this, BTCTemplateService.class), mServiceConn, Context.BIND_AUTO_CREATE);

		ULog.d("address =======>  " + address);
	}

	/**
	 * Stop the service
	 */
	private void doStopService() {
		ULog.d("# Activity - doStopService()");
		mService.finalizeService();
		stopService(new Intent(this, BTCTemplateService.class));
	}

	/**
	 * Initialization / Finalization
	 */
	private void initialize() {
		ULog.d(TAG, "# Activity - initialize()");

		// Use this check to determine whether BLE is supported on the device.
		// Then
		// you can selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.bt_ble_not_supported, Toast.LENGTH_SHORT).show();
			finish();
		}

		mService.setupService(mActivityHandler);

		// If BT is not on, request that it be enabled.
		// RetroWatchService.setupBT() will then be called during
		// onActivityResult
		if (!mService.isBluetoothEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
		}

		// Load activity reports and display
		if (mRefreshTimer != null) {
			connect_fail_dialog = false;
			mRefreshTimer.cancel();
		}

		if(mService !=null){
			mService.connectDevice(address); // BTCTemplate
		}

		// Use below timer if you want scheduled job
		mRefreshTimer = new Timer();
		mRefreshTimer.schedule(new RefreshTimerTask(),8000);

	}

	private class StartTimerTask extends TimerTask {
		public StartTimerTask() {
		}

		public void run() {
			mActivityHandler.post(new Runnable() {
				public void run() {
					initPacket++;
					ULog.d("mStartTimer cancel run ==================== ");

					StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
					builder.setSerpCommand(mService.mBleManager.MESSAGE_DATE_TIME_REQUEST);

					try {
						byte[] encoded = new byte[builder.build().toByteArray().length + 5];
						byte[] builderTobyte = builder.build().toByteArray();

						encoded[0] = 'S';
						encoded[1] = 'E';
						int i;
						for (i = 0; i < builderTobyte.length; i++) {
							encoded[i + 2] = builderTobyte[i];
						}
						int lastBuffer = builder.build().toByteArray().length + 5;
						encoded[i + 2] = 'E';
						encoded[i + 3] = '\r';
						encoded[i + 4] = '\n';
						mService.sendMessageToRemote(encoded);

					} catch (UninitializedMessageException e) {
						e.printStackTrace();
					}

					if(initPacket < 6) {
						mStartTimer.schedule(new StartTimerTask(), 1000);
					}else {
						if(mStartTimer != null) {
							mStartTimer.cancel();
							mStartTimer = null;
						}
					}
				}
			});
		}
	}



	private class HeartRateTimerTask extends TimerTask {
		public HeartRateTimerTask() {
		}

		public void run() {
			mActivityHandler.post(new Runnable() {
				public void run() {
					heartRateMessageSend();

					mHeartRateTimer.schedule(new HeartRateTimerTask(),3000);
				}
			});
		}
	}

	private class RefreshTimerTask extends TimerTask {
		public RefreshTimerTask() {
		}

		public void run() {
			mActivityHandler.post(new Runnable() {
				public void run() {
					ULog.e(TAG, "showLoading1:"+showLoading);
					if(!connect_flag && !connect_fail_dialog){
						mConnecting = true;

						AlertDialog.Builder alt_bld = new AlertDialog.Builder(MainActivity.this);
						alt_bld.setMessage(mContext.getResources().getString(R.string.bluetooth_fail)).setCancelable(false).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								connect_fail_dialog = false;
								doStopService();
								android.os.Process.killProcess(android.os.Process.myPid());
								dialog.cancel();
								moveTaskToBack(true);
								finish();
							}
						}).setPositiveButton(getString(R.string.set), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mConnecting = true;
								connect_fail_dialog = false;
								doStopService();
								setLoadingEnd();
								mainMenuFragment();// main menu fr on
								mPreference.setAllDis(dbManager.selectAllDistance());
							}
						});
						AlertDialog alert = alt_bld.create();
						alert.setTitle(getString(R.string.login_arlam));
						connect_fail_dialog = true;
						alert.show();
					}
				}
			});
		}
	}

	Handler mRYMDTHandler = new Handler();
	private class RYMDTTimerTask extends TimerTask {
		public RYMDTTimerTask() {
		}

		public void run() {
			mRYMDTHandler.post(new Runnable() {
				public void run() {
					if(!connect_fail_dialog && showLoading){
						mConnecting = true;

						AlertDialog.Builder alt_bld = new AlertDialog.Builder(MainActivity.this);
						alt_bld.setMessage(mContext.getResources().getString(R.string.bluetooth_fail)).setCancelable(false).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								connect_fail_dialog = false;
								doStopService();
								android.os.Process.killProcess(android.os.Process.myPid());
								dialog.cancel();
								moveTaskToBack(true);
								finish();
							}
						}).setPositiveButton(getString(R.string.set), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								mConnecting = true;
								connect_fail_dialog = false;
								doStopService();
								setLoadingEnd();
								mainMenuFragment();// main menu fr on
								mPreference.setAllDis(dbManager.selectAllDistance());
							}
						});
						AlertDialog alert = alt_bld.create();
						alert.setTitle(getString(R.string.login_arlam));
						connect_fail_dialog = true;
						alert.show();
					}
				}
			});
		}
	}

	boolean connect_fail_dialog = false;

	private boolean mWakeFlag = false;
	boolean mConnecting = false;

	boolean flag2 = false;

	@Override
	protected void onActivityResult(int request, int result, Intent Data) {
		ULog.d(TAG, "onActivityResult " + request);

		switch (request) {
			case Constants.REQUEST_ENABLE_BT:
				// When the request to enable Bluetooth returns
				if (result == Activity.RESULT_OK) {
					// Bluetooth is now enabled, so set up a BT session
					mService.setupBLE();
					if (address != null && mService != null) {
						mService.connectDevice(address); // BTCTemplate
					}
				} else {
					// User did not enable Bluetooth or an error occured
					ULog.e(TAG, "BT is not enabled");
					Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
				}
				break;

			case Constants.REQUEST_LOGIN:
				if (result == Activity.RESULT_OK) {
					flag2 = true;

				} else {
				}

			case Constants.ACTIVITY_SLEEP_FLAG:
				if (result == Activity.RESULT_OK) {
					ULog.d("Data.getExtras().getInt('PAGE_FLAG':)" + Data.getExtras().getInt("PAGE_FLAG"));
					if(Data.getExtras().getInt("PAGE_FLAG") == 0)
						mActivityViewPager.setCurrentItem(0);
					else
						// 수면 back인지 활동 back인지 구분
						mActivityViewPager.setCurrentItem(1);
				}

				break;
			case Constants.REQUEST_SIGN_OUT: // 탈퇴
				if (result == Activity.RESULT_OK) {
					mTodayPreference.resetActivityPreferences();
					mPreference.resetPreferences();
					mPreference.resetAlarms();
					dbManager.dropDB();
					mConnectionInfo.resetConnectionInfo();
					doStopService();

					Intent intro = new Intent(getApplicationContext(), IntroActivity.class);
					intro.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intro.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intro);
					MainActivity.this.finish();
				}else if(result == Constants.RESULT_FIRM_WARE){
					doStopService();

					Intent intro = new Intent(getApplicationContext(), IntroActivity.class);
					intro.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intro.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					finalizeActivity();
					startActivity(intro);
					MainActivity.this.finish();
					android.os.Process.killProcess(android.os.Process.myPid());
					doStopService();
				}

				break;
		} // End of switch(requ estCode)
	}
	boolean flag = false;

	public boolean connected = false;

	public boolean getConnection(){
		return connected;
	}

	int conneting_chk =0;

	private boolean RYMDTimer = false;

	private int mSuccessfull = 0;
	/*Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			// 메세지를 처리하고 또다시 핸들러에 메세지 전달 (1000ms 지연)
			if(mSuccessfull > 5) {
				mSuccessfull++;
				StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
				builder.setSerpCommand(mService.mBleManager.MESSAGE_DATE_TIME_REQUEST);

				try {
					byte[] encoded = new byte[builder.build().toByteArray().length + 5];
					byte[] builderTobyte = builder.build().toByteArray();

					encoded[0] = 'S';
					encoded[1] = 'E';
					int i;
					for (i = 0; i < builderTobyte.length; i++) {
						encoded[i + 2] = builderTobyte[i];
					}
					int lastBuffer = builder.build().toByteArray().length + 5;
					encoded[i + 2] = 'E';
					encoded[i + 3] = '\r';
					encoded[i + 4] = '\n';
					MainActivity.mService.sendMessageToRemote(encoded);

				} catch (UninitializedMessageException e) {
					e.printStackTrace();
				}
			}
			mHandler.sendEmptyMessageDelayed(0,1000);
		}
	};*/

	public class ActivityHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

//			ActivityFragment ActFrg = (ActivityFragment) mSectionsPagerAdapter.getItem(FragmentAdapter.FRAGMENT_POS_WALK_ACTIVITY);

			switch (msg.what) {
				case Constants.MESSAGE_BT_STATE_INITIALIZED:
					mTextStatus.setText(getResources().getString(R.string.bt_title) + ": "
							+ getResources().getString(R.string.bt_state_init));
					mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
					connected = false;
					ULog.d("MESSAGE_BT_STATE_INITIALIZED ============================= : " + getResources().getString(R.string.bt_title) + ": "
							+ getResources().getString(R.string.bt_state_init));



					ULog.d("conneting_chk : " + conneting_chk );
					if(conneting_chk>4){
						DialogSimple(getString(R.string.bluetooth_discon));
					}



					// 연결 재시도 ?
					// if(address != null && mService != null)
					// mService.connectDevice(address);
					break;
				case Constants.MESSAGE_BT_STATE_LISTENING:
					mTextStatus.setText(getResources().getString(R.string.bt_title) + ": "
							+ getResources().getString(R.string.bt_state_wait));
					mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
					connected = false;
					break;
				case Constants.MESSAGE_BT_STATE_CONNECTING:
					mTextStatus.setText(getResources().getString(R.string.bt_title) + ": "
							+ getResources().getString(R.string.bt_state_connect));
					mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_away));
					connected = false;
					break;
				case Constants.MESSAGE_BT_STATE_CONNECTED:
					if (mService != null) {
						String deviceName = mService.getDeviceName();
						if (deviceName != null) {
							mTextStatus.setText(getResources().getString(R.string.bt_title) + ": "
									+ getResources().getString(R.string.bt_state_connected) + " " + deviceName);
							mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_online));
						} else {
//						mTextStatus.setText(getResources().getString(R.string.bt_title) + ": "
//								+ getResources().getString(R.string.bt_state_connected) + " no name");
//						mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_online));
						}

						flag = true;
						flagSendDT = false;
						flagRUSER = false;
						flagUR = false;
						flagRA = false;
						flagRS = false;
						flagBAT = false;
						flagVersion = false;
						flagCWUTS = false;
						feedback = false;
						mWakeFlag = false;
						connected = true;

						if(mRefreshTimer != null && conneting_chk < 5){
							ULog.e("Connected 후 mRefreshTimer cancel");
							mRefreshTimer.cancel();
							mRefreshTimer = null;
						}

						if(mRYMDTTimer == null && !mConnecting){
							mRYMDTTimer = new Timer();
							mRYMDTTimer.schedule(new RYMDTTimerTask(),8000);
						}
					}
					break;
				case Constants.MESSAGE_BT_STATE_ERROR:
					mTextStatus.setText(getResources().getString(R.string.bt_state_error));
					mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_busy));
					connected = false;
					break;

				// BT Command status
				case Constants.MESSAGE_CMD_ERROR_NOT_CONNECTED:
					ULog.e("MESSAGE_CMD_ERROR_NOT_CONNECTED : " + getResources().getString(R.string.bt_cmd_sending_error));
					mTextStatus.setText(getResources().getString(R.string.bt_cmd_sending_error));
					mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_busy));
					connected = false;
					//send Message 실패시!!!

					break;

				///////////////////////////////////////////////
				// When there's incoming packets on bluetooth
				// do the UI works like below
				///////////////////////////////////////////////

				case Constants.MESSAGE_WRITE_CHAT_DATA:
					if(msg.obj instanceof byte[]) {

					}

					break;

				case Constants.MESSAGE_READ_CHAT_DATA:
					conneting_chk ++;
					ULog.d("Constants.MESSAGE_READ_CHAT_DATA");
					ULog.d("Constants.MESSAGE_READ_CHAT_DATA");






					if (msg.obj != null) {
						connect_flag = true;


						ULog.d("메시지는 있나??? : " + msg.obj.toString());
						if(msg.obj instanceof StnEggPkt.StnEggPacket) {
							StnEggPkt.StnEggPacket builder = (StnEggPkt.StnEggPacket) msg.obj;
							ULog.d("builder:  " + builder.getSerpCommand());
							mSuccessfull = 6;
							ULog.d("mStartTimer cancel ==================== ");
							if(mStartTimer != null) {
								mStartTimer.cancel();
								mStartTimer = null;
							}

							//20160819 start
							connect_fail_dialog=true;
							if(mRYMDTTimer != null){
								mRYMDTTimer.cancel();
								mRYMDTTimer = null;
							}

							if(showLoading) {
								setLoadingEnd();
								mainMenuFragment();// main menu fr on
							}

							if(mRefreshTimer != null){
								ULog.e("Connected 후 mRefreshTimer cancel");
								mRefreshTimer.cancel();
								mRefreshTimer = null;
							}



							Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame_loading);




							Date tdays = new Date();
							String tday = Integer.toString(tdays.getDay());
							String ty = Integer.toString(tdays.getYear() + 1900 - 2000);
							String tmonth = Integer.toString(tdays.getMonth() + 1);
							String tmm = tmonth.length() == 1 ? "0" + tmonth : tmonth;
							String tdate = Integer.toString(tdays.getDate());
							String td = tdate.length() == 1 ? "0" + tdate : tdate;

							String thour = Integer.toString(tdays.getHours());
							String th = thour.length() == 1 ? "0" + thour : thour;
							String tminute = Integer.toString(tdays.getMinutes());
							String tm = tminute.length() == 1 ? "0" + tminute : tminute;
							String tsecond = Integer.toString(tdays.getSeconds());
							String ts = tsecond.length() == 1 ? "0" + tsecond : tsecond;


							StnEggPkt.StnEggPacket.Builder sendBuilder;

							if (builder.getSerpCommand() == mService.mBleManager.MESSAGE_DATE_TIME_REQUEST) {


								ULog.d("여기 들옴 ????? MESSAGE_DATE_TIME_REQUEST");
								sendBuilder = StnEggPkt.StnEggPacket.newBuilder();
								//command
								sendBuilder.setSerpCommand(mService.mBleManager.MESSAGE_DATE_TIME_CONTENT);

								// date
								sendBuilder.getDateBuilder().setYear(tdays.getYear() + 1900);
								sendBuilder.getDateBuilder().setMonth(tdays.getMonth() + 1);
								sendBuilder.getDateBuilder().setDay(tdays.getDate());
								sendBuilder.getDateBuilder().setHour(tdays.getHours());
								sendBuilder.getDateBuilder().setMinutes(tdays.getMinutes());
								sendBuilder.getDateBuilder().setSeconds(tdays.getSeconds());
								if (tdays.getDay() == 0) {
									sendBuilder.getDateBuilder().setWeekDay(7);
								} else {
									sendBuilder.getDateBuilder().setWeekDay(tdays.getDay());
								}

								ULog.d("222 getYear : " + sendBuilder.getDate().getYear() + "//getMonth : " + sendBuilder.getDate().getMonth() + "//getDay : " + sendBuilder.getDate().getDay()
										+ "//getHour : " + sendBuilder.getDate().getHour() + "//getMinutes : " + sendBuilder.getDate().getMinutes() + "//getSeconds : " + sendBuilder.getDate().getSeconds() + "//getWeekDay : " + sendBuilder.getDate().getWeekDay());

								//send data
								try {
									byte[] encoded = new byte[sendBuilder.build().toByteArray().length + 5];
									byte[] builderTobyte = sendBuilder.build().toByteArray();

									encoded[0] = 'S';
									encoded[1] = 'E';
									int i;
									for (i = 0; i < builderTobyte.length; i++) {
										encoded[i + 2] = builderTobyte[i];
									}
									encoded[i + 2] = 'E';
									encoded[i + 3] = '\r';
									encoded[i + 4] = '\n';

									mService.sendMessageToRemote(encoded);

								} catch (UninitializedMessageException e) {
									e.printStackTrace();
								}



							} else if (builder.getSerpCommand() == mService.mBleManager.MESSAGE_DATE_TIME_RECEIVED || builder.getSerpCommand() == mService.mBleManager.MESSAGE_PHYSICAL_ATTRIBUTES_ACCEPT) {


								ULog.d("여기 들옴 ????? MESSAGE_DATE_TIME_RECEIVED or MESSAGE_DATE_TIME_RECEIVED");
								sendBuilder = StnEggPkt.StnEggPacket.newBuilder();
								//command
								sendBuilder.setSerpCommand(mService.mBleManager.MESSAGE_PHYSICAL_ATTRIBUTES_CONTENT);


								ULog.d("getUserTall : " + mPreference.getUserTall() + "///getUserWeight : " + mPreference.getUserWeight());
								ULog.d("getUserWeight : " + (mPreference.getUserWeight()<10?"0"+mPreference.getUserWeight(): mPreference.getUserWeight()));
								if(mPreference != null || mPreference.getUserTall() > 0) {
									sendBuilder.getPhysiqueBuilder().setHeight(mPreference.getUserTall());
									sendBuilder.getPhysiqueBuilder().setWeight(mPreference.getUserWeight());
									sendBuilder.getPhysiqueBuilder().setHearthRateHigh(130);
									sendBuilder.getPhysiqueBuilder().setHearthRateLow(70);
								} else {
									sendBuilder.getPhysiqueBuilder().setHeight(185);
									sendBuilder.getPhysiqueBuilder().setWeight(61);
									sendBuilder.getPhysiqueBuilder().setHearthRateHigh(130);
									sendBuilder.getPhysiqueBuilder().setHearthRateLow(70);
								}

								try {
									byte[] encoded = new byte[sendBuilder.build().toByteArray().length + 5];
									byte[] builderTobyte = sendBuilder.build().toByteArray();

									encoded[0] = 'S';
									encoded[1] = 'E';
									int i;
									for (i = 0; i < builderTobyte.length; i++) {
										encoded[i + 2] = builderTobyte[i];
									}
									encoded[i + 2] = 'E';
									encoded[i + 3] = '\r';
									encoded[i + 4] = '\n';
									mService.sendMessageToRemote(encoded);

								} catch (UninitializedMessageException e) {
									e.printStackTrace();
								}





							}else if (builder.getSerpCommand() == mService.mBleManager.MESSAGE_SAVED_ACTIVITY_REPORT_REQUEST || builder.getSerpCommand() == mService.mBleManager.MESSAGE_PHYSICAL_ATTRIBUTES_RECEIVED) {
								ULog.d("여기 들옴 ????? MESSAGE_SAVED_ACTIVITY_REPORT_REQUEST or MESSAGE_PHYSICAL_ATTRIBUTES_RECEIVED");
								sendBuilder = StnEggPkt.StnEggPacket.newBuilder();

								//command
								sendBuilder.setSerpCommand(mService.mBleManager.MESSAGE_SAVED_ACTIVITY_REPORT_ACCEPT);

								try {
									byte[] encoded = new byte[sendBuilder.build().toByteArray().length + 5];
									byte[] builderTobyte = sendBuilder.build().toByteArray();

									encoded[0] = 'S';
									encoded[1] = 'E';
									int i;
									for (i = 0; i < builderTobyte.length; i++) {
										encoded[i + 2] = builderTobyte[i];
									}
									encoded[i + 2] = 'E';
									encoded[i + 3] = '\r';
									encoded[i + 4] = '\n';
									mService.sendMessageToRemote(encoded);

								} catch (UninitializedMessageException e) {
									e.printStackTrace();
								}




							//} else if (builder.getSerpCommand() == mService.mBleManager.MESSAGE_SAVED_ACTIVITY_REPORT_CONTENT) {
							} else if (false) {
								ULog.d("여기 들옴 ????? MESSAGE_SAVED_ACTIVITY_REPORT_CONTENT");
								sendBuilder = StnEggPkt.StnEggPacket.newBuilder();

								ULog.d("sendBuilder.getMotion" + builder.getMotion());
								ULog.d("sendBuilder.getYear" + builder.getDate().getYear());
								ULog.d("sendBuilder.getMonth" + builder.getDate().getMonth());
								ULog.d("sendBuilder.getDay" + builder.getDate().getDay());
								ULog.d("sendBuilder.getHour" + builder.getDate().getHour());
								ULog.d("sendBuilder.getMinutes" + builder.getDate().getMinutes());
								ULog.d("sendBuilder.getSeconds" + builder.getDate().getSeconds());
								ULog.d("sendBuilder.getDurationMinutes" + builder.getDurationMinutes());
								ULog.d("sendBuilder.getCalories" + builder.getCalories());
								ULog.d("sendBuilder.getDistanceMeters" + builder.getDistanceMeters());

								if(builder.getMotion() == 2) {//walking
									mActivityAllCount = mActivityAllCount + builder.getCount();
									mActivityAllM = mActivityAllM + builder.getDistanceMeters();
									mActivityAllCal = mActivityAllCal + builder.getCalories();

									mWalkDistance = mWalkDistance + builder.getCalories();
									mWalkCal = mWalkCal + builder.getCalories();
									mWalkCnt = mWalkCnt + builder.getCount();

									//걸음 시간대 저장
									mWalkHour  = mWalkHour + builder.getDate().getHour();
									mWalkMin  = mWalkMin + builder.getDate().getMinutes();
								}else if(builder.getMotion() == 3) {//running
									mActivityRunAllCount = mActivityRunAllCount + builder.getCount();
									mActivityRunAllM = mActivityRunAllM + builder.getDistanceMeters();
									mActivityRunAllCal = mActivityRunAllCal + builder.getCalories();

									mRunHour = builder.getDate().getHour();
									mRunMin  = builder.getDate().getMinutes();
									mRunCnt = builder.getCount();
								}else if(builder.getMotion() == 9) {//biceps_curl

								}else if(builder.getMotion() == 10) {//shoulder_press

								}



								//command
								sendBuilder.setSerpCommand(mService.mBleManager.MESSAGE_SAVED_ACTIVITY_REPORT_RECEIVED);

								try {
									byte[] encoded = new byte[sendBuilder.build().toByteArray().length + 5];
									byte[] builderTobyte = sendBuilder.build().toByteArray();

									encoded[0] = 'S';
									encoded[1] = 'E';
									int i;
									for (i = 0; i < builderTobyte.length; i++) {
										encoded[i + 2] = builderTobyte[i];
									}
									encoded[i + 2] = 'E';
									encoded[i + 3] = '\r';
									encoded[i + 4] = '\n';
									mService.sendMessageToRemote(encoded);

								} catch (UninitializedMessageException e) {
									e.printStackTrace();
								}






							} else if (builder.getSerpCommand() == mService.mBleManager.MESSAGE_REAL_TIME_ACTIVITY_REPORT_CONTENT || builder.getSerpCommand() == mService.mBleManager.MESSAGE_SAVED_ACTIVITY_REPORT_CONTENT) {
								ULog.d("여기 들옴 ????? MESSAGE_REAL_TIME_ACTIVITY_REPORT_CONTENT");
								sendBuilder = StnEggPkt.StnEggPacket.newBuilder();

								ULog.d("sendBuilder.getMotion" + builder.getMotion());
								ULog.d("sendBuilder.getYear" + builder.getDate().getYear());
								ULog.d("sendBuilder.getMonth" + builder.getDate().getMonth());
								ULog.d("sendBuilder.getDay" + builder.getDate().getDay());
								ULog.d("sendBuilder.getHour" + builder.getDate().getHour());
								ULog.d("sendBuilder.getMinutes" + builder.getDate().getMinutes());
								ULog.d("sendBuilder.getSeconds" + builder.getDate().getSeconds());
								ULog.d("sendBuilder.getCalories" + builder.getCalories());
								ULog.d("sendBuilder.getDistanceMeters" + builder.getDistanceMeters());
								ULog.d("sendBuilder.getCount" + builder.getCount());


								if(builder.getMotion() == Constants.MOTION_WALKING) {//MOTION_WALKING
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty)+2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty)+2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));

									mActivityAllCount = mActivityAllCount + builder.getCount();
									mActivityAllM = mActivityAllM + builder.getDistanceMeters();
									mActivityAllCal = mActivityAllCal + builder.getCalories();

//									mActivityAllCount = builder.getCount();
//									mActivityAllM = builder.getDistanceMeters();
//									mActivityAllCal = builder.getCalories();

									mWalkHour = builder.getDate().getHour();
									mWalkMin  = builder.getDate().getMinutes();
									mWalkCnt = builder.getCount();

									dbManager.intertTodayData(
											mPreference.getUserId(),
											mTodayPreference.getDataDate(),
											mWalkHour,
											mWalkCnt,
											builder.getDistanceMeters(),
											builder.getCalories()
									);




									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_ACTIVITY_CNT, mActivityAllCount);

										bundle.putInt(Constants.TODAY_ACT_GRAPH_TIME_HOUR, mWalkHour);
										bundle.putInt(Constants.TODAY_ACT_GRAPH_TIME_MIN, mWalkMin);

										bundle.putInt(Constants.TODAY_ACT_WALK_H, mWalkHour);
										bundle.putInt(Constants.TODAY_ACT_WALK_M, mWalkMin);
										bundle.putInt(Constants.TODAY_ACT_WALK_DIS, mActivityAllM);
										bundle.putInt(Constants.TODAY_ACT_WALK_CAL, mActivityAllCal);
										bundle.putInt(Constants.TODAY_ACT_WALK_CNT, mWalkCnt);

										mTodayPreference.setTodayActData(bundle);

									} catch (Exception e) {
										// TODO: handle exception
									}

									try {
										if (mActivityViewPager != null) {
											if (mActivityViewPager.getCurrentItem() == 0) {
//									if(flagWalking) {
												ActivityFragment ActFrg = (ActivityFragment) mSectionsPagerAdapter.getItem(FragmentAdapter.FRAGMENT_POS_WALK_ACTIVITY);
												ActFrg.setWalking(Integer.toString(mActivityAllCount), mActivityAllM, mActivityAllCal);
											}
										}
									}catch (NullPointerException e) {
										e.printStackTrace();
									}






								}else if(builder.getMotion() == Constants.MOTION_RUNNING) {//running
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty)+2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty)+2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));

									mActivityRunAllCount = mActivityRunAllCount + builder.getCount();
									mActivityRunAllM = mActivityRunAllM + builder.getDistanceMeters();
									mActivityRunAllCal = mActivityRunAllCal + builder.getCalories();

//									mActivityRunAllCount = builder.getCount();
//									mActivityRunAllM = builder.getDistanceMeters();
//									mActivityRunAllCal = builder.getCalories();


									mRunHour = builder.getDate().getHour();
									mRunMin  = builder.getDate().getMinutes();
									mRunCnt = builder.getCount();


									dbManager.intertTodayRunData(
											mPreference.getUserId(),
											mTodayPreference.getDataDate(),
											mRunHour,
											mRunCnt,
											builder.getDistanceMeters(),
											builder.getCalories()
									);




									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_ACTIVITY_RUN_CNT, mActivityRunAllCount);

										bundle.putInt(Constants.TODAY_ACT_GRAPH_TIME_HOUR, mRunHour);
										bundle.putInt(Constants.TODAY_ACT_GRAPH_TIME_MIN, mRunMin);

										bundle.putInt(Constants.TODAY_ACT_RUN_H, mRunHour);
										bundle.putInt(Constants.TODAY_ACT_RUN_M, mRunMin);
										bundle.putInt(Constants.TODAY_ACT_RUN_DIS, mActivityRunAllM);
										bundle.putInt(Constants.TODAY_ACT_RUN_CAL, mActivityRunAllCal);
										bundle.putInt(Constants.TODAY_ACT_RUN_CNT, mRunCnt);

										mTodayPreference.setTodayActRunData(bundle);
									} catch (Exception e) {
										// TODO: handle exception
									}



									try {
										if (mActivityViewPager != null) {
											if (mActivityViewPager.getCurrentItem() == 1) {
//									if(flagRunning) {
												ActivityRunFragment ActFrg = (ActivityRunFragment) mSectionsPagerAdapter.getItem(FragmentAdapter.FRAGMENT_POS_RUN_ACTIVITY);
												ActFrg.setWalking(Integer.toString(mActivityRunAllCount), mActivityRunAllM, mActivityRunAllCal);
											}
										}
									}catch (NullPointerException e) {
										e.printStackTrace();
									}






								//motion 추가
								}else if(builder.getMotion() == Constants.MOTION_STANDING) {//MOTION_STANDING
									try {
										if(mMotionViewPager != null || mMotionSectionsPagerAdapter != null) {
											if (mMotionViewPager.getCurrentItem() == 0) {
												HammerFragment MoFrg = (HammerFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_HAMMER_ACTIVITY);
												MoFrg.setCount(Integer.toString(0), 0, 0, builder.getMotion());
											}
										}
									}catch (NullPointerException e) {
										e.printStackTrace();
									}




								}else if(builder.getMotion() == Constants.MOTION_SITTING) {//MOTION_SITTING
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty)+2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty)+2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));


									mMotionSittingAllCount = mMotionSittingAllCount + builder.getCount();


									/*dbManager.intertTodayHammerData(
											mTodayPreference.getDataDate(),
											builder.getDate().getHour(),
											builder.getCount(),
											builder.getCalories()
									);*/

									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_MOTION_SITTING_CNT, mMotionSittingAllCount);

										bundle.putInt(Constants.TODAY_MO_SITTING_CNT, builder.getCount());

										mTodayPreference.setTodayMoSittingData(bundle);
									} catch (Exception e) {
										// TODO: handle exception
									}

									try {
										if(mMotionViewPager != null || mMotionSectionsPagerAdapter != null) {
											if (mMotionViewPager.getCurrentItem() == 0) {
												HammerFragment MoFrg = (HammerFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_HAMMER_ACTIVITY);
												MoFrg.setCount(Integer.toString(mMotionSittingAllCount), 0, 0, builder.getMotion());
											}
										}
									}catch (NullPointerException e) {
										e.printStackTrace();
									}






								}else if(builder.getMotion() == Constants.MOTION_JUMPING) {//MOTION_JUMPING
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty)+2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty)+2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));


									mMotionJumpingAllCount = mMotionJumpingAllCount + builder.getCount();

									/*dbManager.intertTodayHammerData(
											mTodayPreference.getDataDate(),
											builder.getDate().getHour(),
											mHammerCnt,
											builder.getCalories()
									);*/

									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_MOTION_JUMPING_CNT, mMotionJumpingAllCount);

										bundle.putInt(Constants.TODAY_MO_JUMPING_CNT, builder.getCount());

										mTodayPreference.setTodayMoJumpingData(bundle);
									} catch (Exception e) {
										// TODO: handle exception
									}

									try {
										if(mMotionViewPager != null || mMotionSectionsPagerAdapter != null) {
											if (mMotionViewPager.getCurrentItem() == 0) {
												HammerFragment MoFrg = (HammerFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_HAMMER_ACTIVITY);
												MoFrg.setCount(Integer.toString(mMotionJumpingAllCount), 0, 0, builder.getMotion());
											}
										}
									}catch (NullPointerException e) {
										e.printStackTrace();
									}




								}else if(builder.getMotion() == Constants.MOTION_PUSH_UP) {//MOTION_PUSH_UP
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty)+2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty)+2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));


									mMotionPushUpAllCount = mMotionPushUpAllCount + builder.getCount();


									/*dbManager.intertTodayHammerData(
											mTodayPreference.getDataDate(),
											builder.getDate().getHour(),
											builder.getCount(),
											builder.getCalories()
									);*/

									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_MOTION_PUSH_UP_CNT, mMotionPushUpAllCount);

										bundle.putInt(Constants.TODAY_MO_PUSH_UP_CNT, builder.getCount());

										mTodayPreference.setTodayMoPushupData(bundle);
									} catch (Exception e) {
										// TODO: handle exception
									}

									try {
										if(mMotionViewPager != null || mMotionSectionsPagerAdapter != null) {
											if (mMotionViewPager.getCurrentItem() == 0) {
												HammerFragment MoFrg = (HammerFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_HAMMER_ACTIVITY);
												MoFrg.setCount(Integer.toString(mMotionPushUpAllCount), 0, 0, builder.getMotion());
											}
										}
									}catch (NullPointerException e) {
										e.printStackTrace();
									}






								}else if(builder.getMotion() == Constants.MOTION_SIT_UP) {//MOTION_SIT_UP
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty)+2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty)+2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));


									mMotionSitUpAllCount = mMotionSitUpAllCount + builder.getCount();


									/*dbManager.intertTodayHammerData(
											mTodayPreference.getDataDate(),
											builder.getDate().getHour(),
											mHammerCnt,
											builder.getCalories()
									);*/

									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_MOTION_SIT_UP_CNT, mMotionSitUpAllCount);

										bundle.putInt(Constants.TODAY_MO_SIT_UP_CNT, builder.getCount());

										mTodayPreference.setTodayMoSitupData(bundle);
									} catch (Exception e) {
										// TODO: handle exception
									}

									try {
										if(mMotionViewPager != null || mMotionSectionsPagerAdapter != null) {
											if (mMotionViewPager.getCurrentItem() == 0) {
												HammerFragment MoFrg = (HammerFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_HAMMER_ACTIVITY);
												MoFrg.setCount(Integer.toString(mMotionSitUpAllCount), 0, 0, builder.getMotion());
											}
										}
									}catch (NullPointerException e) {
										e.printStackTrace();
									}







								}else if(builder.getMotion() == Constants.MOTION_BUTTERFLY) {//MOTION_BUTTERFLY
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty)+2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty)+2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));


									mMotionButterflyAllCount = mMotionButterflyAllCount + builder.getCount();



									/*dbManager.intertTodayHammerData(
											mTodayPreference.getDataDate(),
											builder.getDate().getHour(),
											mHammerCnt,
											builder.getCalories()
									);*/

									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_MOTION_BUTTERFLY_CNT, mMotionButterflyAllCount);

										bundle.putInt(Constants.TODAY_MO_BUTTERFLY_CNT, builder.getCount());

										mTodayPreference.setTodayMoButterflyData(bundle);
									} catch (Exception e) {
										// TODO: handle exception
									}

									try {
										if(mMotionViewPager != null || mMotionSectionsPagerAdapter != null) {
											if (mMotionViewPager.getCurrentItem() == 0) {
												HammerFragment MoFrg = (HammerFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_HAMMER_ACTIVITY);
												MoFrg.setCount(Integer.toString(mMotionButterflyAllCount), 0, 0, builder.getMotion());
											}
										}
									}catch (NullPointerException e) {
										e.printStackTrace();
									}









								}else if(builder.getMotion() == Constants.MOTION_BICEPS_CURL) {//MOTION_BICEPS_CURL
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty)+2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty)+2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));


									mMotionHammerAllCount = mMotionHammerAllCount + builder.getCount();

									mHammerCnt = builder.getCount();


									dbManager.intertTodayHammerData(
											mTodayPreference.getDataDate(),
											builder.getDate().getHour(),
											mHammerCnt,
											builder.getCalories()
									);

									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_MOTION_HAMMER_CNT, mMotionHammerAllCount);

										bundle.putInt(Constants.TODAY_MO_HAMMER_CNT, mHammerCnt);

										mTodayPreference.setTodayMoHammerData(bundle);
									} catch (Exception e) {
										// TODO: handle exception
									}

									try {
										if(mMotionViewPager != null || mMotionSectionsPagerAdapter != null) {
											if (mMotionViewPager.getCurrentItem() == 0) {
												HammerFragment MoFrg = (HammerFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_HAMMER_ACTIVITY);
												MoFrg.setCount(Integer.toString(mMotionHammerAllCount), 0, 0, builder.getMotion());
											}
										}
									}catch (NullPointerException e) {
										e.printStackTrace();
									}





								}else if(builder.getMotion() == Constants.MOTION_SHOULDER_PRESS) {//MOTION_SHOULDER_PRESS
//									mMotionPagerRadioGroup.check(R.id.shoulder_pager);
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty)+2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty)+2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));

									mMotionShoulderAllCount = mMotionShoulderAllCount + builder.getCount();

									mShoulderCnt = builder.getCount();


									dbManager.intertTodayShoulderData(
											mTodayPreference.getDataDate(),
											builder.getDate().getHour(),
											mShoulderCnt,
											builder.getCalories()
									);


									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_MOTION_SHOULDER_CNT, mMotionShoulderAllCount);

										bundle.putInt(Constants.TODAY_MO_SHOULDER_CNT, mShoulderCnt);

										mTodayPreference.setTodayMoShoulderData(bundle);
									} catch (Exception e) {
										// TODO: handle exception
									}

									try {
										if(mMotionViewPager != null || mMotionSectionsPagerAdapter != null) {
											if (mMotionViewPager.getCurrentItem() == 0) {
//												ShoulderFragment MoFrg = (ShoulderFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_SHOULDER_ACTIVITY);
//												MoFrg.setCount(Integer.toString(mMotionHammerAllCount), 0, 0);

												HammerFragment MoFrg = (HammerFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_HAMMER_ACTIVITY);
												MoFrg.setCount(Integer.toString(mMotionShoulderAllCount), 0, 0, builder.getMotion());
											}
										}
									}catch (NullPointerException e) {
										e.printStackTrace();
									}





								}else if(builder.getMotion() == Constants.MOTION_CRUNCH) {//MOTION_CRUNCH
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty) + 2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty) + 2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));


									mMotionCrunchAllCount = mMotionCrunchAllCount + builder.getCount();



									/*dbManager.intertTodayHammerData(
											mTodayPreference.getDataDate(),
											builder.getDate().getHour(),
											mHammerCnt,
											builder.getCalories()
									);*/

									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_MOTION_CRUNCH_CNT, mMotionCrunchAllCount);

										bundle.putInt(Constants.TODAY_MO_CRUNCH_CNT, builder.getCount());

										mTodayPreference.setTodayMoCrunchData(bundle);
									} catch (Exception e) {
										// TODO: handle exception
									}

									try {
										if (mMotionViewPager != null || mMotionSectionsPagerAdapter != null) {
											if (mMotionViewPager.getCurrentItem() == 0) {
												HammerFragment MoFrg = (HammerFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_HAMMER_ACTIVITY);
												MoFrg.setCount(Integer.toString(mMotionCrunchAllCount), 0, 0, builder.getMotion());
											}
										}
									} catch (NullPointerException e) {
										e.printStackTrace();
									}





								}else if(builder.getMotion() == Constants.MOTION_UPRIGHT_ROW) {//MOTION_UPRIGHT_ROW
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty) + 2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty) + 2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));


									mMotionUprightRowAllCount = mMotionUprightRowAllCount + builder.getCount();


									/*dbManager.intertTodayHammerData(
											mTodayPreference.getDataDate(),
											builder.getDate().getHour(),
											mHammerCnt,
											builder.getCalories()
									);*/

									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_MOTION_UPRIGHT_ROW_CNT, mMotionUprightRowAllCount);

										bundle.putInt(Constants.TODAY_MO_UPRIGHT_ROW_CNT, builder.getCount());

										mTodayPreference.setTodayMoUprightrowData(bundle);
									} catch (Exception e) {
										// TODO: handle exception
									}

									try {
										if (mMotionViewPager != null || mMotionSectionsPagerAdapter != null) {
											if (mMotionViewPager.getCurrentItem() == 0) {
												HammerFragment MoFrg = (HammerFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_HAMMER_ACTIVITY);
												MoFrg.setCount(Integer.toString(mMotionUprightRowAllCount), 0, 0, builder.getMotion());
											}
										}
									} catch (NullPointerException e) {
										e.printStackTrace();
									}







								}else if(builder.getMotion() == Constants.MOTION_TOE_RAISE) {//MOTION_TOE_RAISE
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty) + 2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty) + 2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));


									mMotionToeRaiseAllCount = mMotionToeRaiseAllCount + builder.getCount();


									/*dbManager.intertTodayHammerData(
											mTodayPreference.getDataDate(),
											builder.getDate().getHour(),
											mHammerCnt,
											builder.getCalories()
									);*/

									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_MOTION_TOE_RAISE_CNT, mMotionToeRaiseAllCount);

										bundle.putInt(Constants.TODAY_MO_TOE_RAISE_CNT, builder.getCount());

										mTodayPreference.setTodayMoToeraiseData(bundle);
									} catch (Exception e) {
										// TODO: handle exception
									}

									try {
										if (mMotionViewPager != null || mMotionSectionsPagerAdapter != null) {
											if (mMotionViewPager.getCurrentItem() == 0) {
												HammerFragment MoFrg = (HammerFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_HAMMER_ACTIVITY);
												MoFrg.setCount(Integer.toString(mMotionToeRaiseAllCount), 0, 0, builder.getMotion());
											}
										}
									} catch (NullPointerException e) {
										e.printStackTrace();
									}







								}else if(builder.getMotion() == Constants.MOTION_PEC_FLY) {//MOTION_PEC_FLY
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty) + 2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty) + 2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));


									mMotionPecFlyAllCount = mMotionPecFlyAllCount + builder.getCount();


									/*dbManager.intertTodayHammerData(
											mTodayPreference.getDataDate(),
											builder.getDate().getHour(),
											mHammerCnt,
											builder.getCalories()
									);*/

									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_MOTION_PEC_FLY_CNT, mMotionPecFlyAllCount);

										bundle.putInt(Constants.TODAY_MO_PEC_FLY_CNT, builder.getCount());

										mTodayPreference.setTodayMoPecflyData(bundle);
									} catch (Exception e) {
										// TODO: handle exception
									}

									try {
										if (mMotionViewPager != null || mMotionSectionsPagerAdapter != null) {
											if (mMotionViewPager.getCurrentItem() == 0) {
												HammerFragment MoFrg = (HammerFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_HAMMER_ACTIVITY);
												MoFrg.setCount(Integer.toString(mMotionPecFlyAllCount), 0, 0, builder.getMotion());
											}
										}
									} catch (NullPointerException e) {
										e.printStackTrace();
									}




								}else if(builder.getMotion() == Constants.MOTION_CHEST_PRESS) {//MOTION_CHEST_PRESS
									mTodayPreference.setTodayDate(
											((Integer.parseInt(ty) + 2000) + "-" + tmm + "-" + td),
											Integer.parseInt(ty) + 2000,
											Integer.parseInt(tmm),
											Integer.parseInt(td));


									mMotionChestPressAllCount = mMotionChestPressAllCount + builder.getCount();

									mHammerCnt = builder.getCount();


									/*dbManager.intertTodayHammerData(
											mTodayPreference.getDataDate(),
											builder.getDate().getHour(),
											mHammerCnt,
											builder.getCalories()
									);*/

									try {
										Bundle bundle = new Bundle();
										//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때

										bundle.putInt(Constants.TODAY_MOTION_CHEST_PRESS_CNT, mMotionChestPressAllCount);

										bundle.putInt(Constants.TODAY_MO_CHEST_PRESS_CNT, builder.getCount());

										mTodayPreference.setTodayMoChestpressData(bundle);
									} catch (Exception e) {
										// TODO: handle exception
									}

									try {
										if (mMotionViewPager != null || mMotionSectionsPagerAdapter != null) {
											if (mMotionViewPager.getCurrentItem() == 0) {
												HammerFragment MoFrg = (HammerFragment) mMotionSectionsPagerAdapter.getItem(MotionFragmentAdapter.FRAGMENT_POS_HAMMER_ACTIVITY);
												MoFrg.setCount(Integer.toString(mMotionChestPressAllCount), 0, 0, builder.getMotion());
											}
										}
									} catch (NullPointerException e) {
										e.printStackTrace();
									}
								}




								if(flagSensorDataMenu) {
									int[] buildData = new int[5];
									buildData[0] = builder.getMotion();
									buildData[1] = builder.getCount();
									buildData[2] = builder.getDistanceMeters();
									buildData[3] = builder.getCalories();
									buildData[4] = builder.getHearthRate();

									SensorDataFragment fragment = (SensorDataFragment) getFragmentManager().findFragmentById(R.id.frame_loading);
									fragment.setData(buildData);
								}



								// save data command send
								if(builder.getSerpCommand() == mService.mBleManager.MESSAGE_SAVED_ACTIVITY_REPORT_CONTENT) {
									//command
									sendBuilder.setSerpCommand(mService.mBleManager.MESSAGE_SAVED_ACTIVITY_REPORT_RECEIVED);

									try {
										byte[] encoded = new byte[sendBuilder.build().toByteArray().length + 5];
										byte[] builderTobyte = sendBuilder.build().toByteArray();

										encoded[0] = 'S';
										encoded[1] = 'E';
										int i;
										for (i = 0; i < builderTobyte.length; i++) {
											encoded[i + 2] = builderTobyte[i];
										}
										encoded[i + 2] = 'E';
										encoded[i + 3] = '\r';
										encoded[i + 4] = '\n';
										mService.sendMessageToRemote(encoded);

									} catch (UninitializedMessageException e) {
										e.printStackTrace();
									}
								}






							} else if (builder.getSerpCommand() == mService.mBleManager.MESSAGE_BAND_MODE_REQUEST) {
								ULog.d("여기 들옴 ????? MESSAGE_START_FUNCTIONALITY_ACCEPT");


							} else if (builder.getSerpCommand() == mService.mBleManager.MESSAGE_VIBRATION_ACCEPT) {
								ULog.d("여기 들옴 ????? MESSAGE_VIBRATION_ACCEPT");


							} else if (builder.getSerpCommand() == mService.mBleManager.MESSAGE_HEART_RATE_ACCEPT) {
								ULog.d("여기 들옴 ????? MESSAGE_HEART_RATE_ACCEPT");
								ULog.d("heart Rate : " + builder.getHearthRate());

								try{

									if(flagSensorDataMenu) {
										int[] buildData = new int[5];
										buildData[0] = builder.getMotion();
										buildData[1] = builder.getCount();
										buildData[2] = builder.getDistanceMeters();
										buildData[3] = builder.getCalories();
										buildData[4] = builder.getHearthRate();

										SensorDataFragment fragment = (SensorDataFragment) getFragmentManager().findFragmentById(R.id.frame_loading);
										fragment.setData(buildData);
									}else if(flagHeartRateMenu) {
										HeartRateFragment fragment = (HeartRateFragment) getFragmentManager().findFragmentById(R.id.frame_loading);
										fragment.setHeartRate(builder.getHearthRate());
									}

								}catch (NullPointerException e) {
									e.printStackTrace();
								}





							} else if (builder.getSerpCommand() == mService.mBleManager.MESSAGE_BATTERY_CONTENT) {
								ULog.d("여기 들옴 ????? MESSAGE_BATTERY_CONTENT");

								try{
//								BatteryActivity.setBat(builder.getBattery()+"");

								}catch (NullPointerException e) {
									e.printStackTrace();
								}





							} else if (builder.getSerpCommand() == mService.mBleManager.MESSAGE_GENERAL_DATA_CONTENT) {
								ULog.d("여기 들옴 ????? MESSAGE_GENERAL_DATA_CONTENT");

								try{
									SensorChartFragment.setData(builder.getAcceleroDataG(), builder.getGyroDataDps(), builder.getMagDataUT());

								}catch (NullPointerException e) {
									e.printStackTrace();
								}





							} else if (builder.getSerpCommand() == mService.mBleManager.MESSAGE_SLEEP_STATUS_CONTENT || builder.getSerpCommand() == mService.mBleManager.MESSAGE_SLEEP_STATUS_RECEIVED) {
								ULog.d("여기 들옴 ????? MESSAGE_SLEEP_STATUS_CONTENT");
								ULog.d("MESSAGE_SLEEP_STATUS_CONTENT : " + builder.getUserUint01());

								try{

									if(flagMainMenu) {
										if(true) {
											try {
												Fragment mFragment2 = getFragmentManager().findFragmentById(R.id.frame_loading);
												TextView logText = (TextView)mFragment2.getActivity().findViewById(R.id.log_text);
												logText.setMovementMethod(new ScrollingMovementMethod());
												String logData = logText.getText().toString();
												logData += "\r\n";
												logData += "command : " + builder.getSerpCommand();
												if(builder.getUserUint01() == 0) {
													logData += "////  status : sleep";
												}else if(builder.getUserUint01() == 1) {
													logData += "////  status : wake up";
												}else if(builder.getUserUint01() == 2) {
													logData += "////  status : taken off";
												}else if(builder.getUserUint01() == 3) {
													logData += "////  status : ready";
												}

												logData += "//// year : " + builder.getDate().getYear() + ", month : " + builder.getDate().getMonth() + ", day : " + builder.getDate().getDay() +
														", hour : " + builder.getDate().getHour() + ", minutes : " + builder.getDate().getMinutes() + ", seconds : " + builder.getDate().getSeconds();

												logText.setText(logData);

											} catch (NullPointerException e) {
												e.printStackTrace();
											}
										}


										StnEggPkt.StnEggPacket.Builder builder2 = StnEggPkt.StnEggPacket.newBuilder();
										builder2.setSerpCommand(mService.mBleManager.MESSAGE_SLEEP_STATUS_RECEIVED);

										try {
											byte[] encoded = new byte[builder2.build().toByteArray().length + 5];
											byte[] builderTobyte = builder2.build().toByteArray();

											encoded[0] = 'S';
											encoded[1] = 'E';
											int i;
											for (i = 0; i < builderTobyte.length; i++) {
												encoded[i + 2] = builderTobyte[i];
											}
											int lastBuffer = builder2.build().toByteArray().length + 5;
											encoded[i + 2] = 'E';
											encoded[i + 3] = '\r';
											encoded[i + 4] = '\n';
											MainActivity.mService.sendMessageToRemote(encoded);
										} catch (UninitializedMessageException e) {
											e.printStackTrace();
										}

									}



								}catch (NullPointerException e) {
									e.printStackTrace();
								}





							}









							return;
						}
					}





					if (msg.obj != null && msg.obj instanceof String) {
						String message = (String) msg.obj;
						connect_flag=true;

						ULog.d("mStartTimer start ==================== ");
						mStartTimer = new Timer();
						mStartTimer.schedule(new StartTimerTask(),1000);

						//20160819 start
						connect_fail_dialog=true;
						if(mRYMDTTimer != null){
							mRYMDTTimer.cancel();
							mRYMDTTimer = null;
						}
						feedback = !feedback; // true -> false;

						if(showLoading) {
							setLoadingEnd();
							mainMenuFragment();// main menu fr on
//							mHandler.sendEmptyMessage(0);
						}
						//20160819 end

						if(mRefreshTimer != null){
							ULog.e("Connected 후 mRefreshTimer cancel");
							mRefreshTimer.cancel();
							mRefreshTimer = null;
						}


						if(true) {
//						feedback = !feedback;
							StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
							builder.setSerpCommand(mService.mBleManager.MESSAGE_DATE_TIME_REQUEST);

							try {
								byte[] encoded = new byte[builder.build().toByteArray().length + 5];
								byte[] builderTobyte = builder.build().toByteArray();

								encoded[0] = 'S';
								encoded[1] = 'E';
								int i;
								for (i = 0; i < builderTobyte.length; i++) {
									encoded[i + 2] = builderTobyte[i];
								}
								int lastBuffer = builder.build().toByteArray().length + 5;
								encoded[i + 2] = 'E';
								encoded[i + 3] = '\r';
								encoded[i + 4] = '\n';
								MainActivity.mService.sendMessageToRemote(encoded);

							} catch (UninitializedMessageException e) {
								e.printStackTrace();
							}
							return;
						}

						if (message.contains("U-R")){}

						if (message.contains("R-A") && flagSendDT) {}

						if (message.contains("RYMDT")) {}

						if (message.contains("RUSER")) {}

					}
					break;

				default:
					break;
			}
			super.handleMessage(msg);
		}
	}


	public int[] countCalDis(int walk_cnt, int user_height,int user_weight) {

		int datas[] = new int[2];
		float distance=0.0f;
		float cur_walk_speed = 0.0f;
		float cur_consumed_calories = 0.0f;
		if (walk_cnt <= 2) {
			distance = user_height / 500;
		} else if (walk_cnt <= 3) {
			distance = user_height / 400;
		} else if (walk_cnt <= 4) {
			distance = user_height / 300;
		} else if (walk_cnt <= 5) {
			distance = user_height / 200;
		} else if (walk_cnt <= 6) {
			distance = user_height / 120;
		} else if (walk_cnt <= 8) {
			distance = user_height / 100;
		} else {
			distance = (float) ((user_height * 1.2) / 100);
		}

		distance = ((float) walk_cnt * distance);
		datas[0] = (int)distance;

		cur_walk_speed = distance / 2;

		/*
		 * Consumed Calories calculation equations Calories (C/kg/h) = 1.25
		 * × running speed (km/h)
		 * 
		 * 1 C/kg/hour while resting
		 * 
		 * --> Calories (C/2 s) = speed × weight/400 (walking or running)
		 * Calories (C/2 s) = 1 × weight/1800 (resting)
		 */

		cur_consumed_calories += (cur_walk_speed * user_weight) / 400;
		datas[1] = (int)cur_consumed_calories;

		return datas;
	}


	/**
	 * 로딩화면 끝, actionbar show
	 */
	public void setLoadingEnd(){
		showLoading=false;

		if(mRYMDTTimer != null){
			mRYMDTTimer.cancel();
			mRYMDTTimer = null;
		}

		ULog.e(TAG, "showLoading3:"+showLoading);
		Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame_loading);

		fragmentManager.remove(mFragment);
//		findViewById(R.id.frame2).setVisibility(View.GONE);
//		mMainFrame.setVisibility(View.VISIBLE);
		getActionBar().show();
//		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
	}



	private void finalizeActivity() {
		ULog.d(TAG, "# Activity - finalizeActivity()");

		if (!AppSettings.getBgService()) {
			doStopService();
		} else {
		}

		// Clean used resources
		RecycleUtils.recursiveRecycle(getWindow().getDecorView());
		System.gc();
	}

	@Override
	public void OnFragmentCallback(int msgType, String arg2) {
		switch (msgType) {
			case IFragmentListener.CALLBACK_RUN_IN_BACKGROUND:
				if (mService != null)
					mService.startServiceMonitoring();

				// background에서 실행

				break;
			case IFragmentListener.CALLBACK_SEND_MESSAGE:
				ULog.i(TAG, "FragmentCall Back ");

				if (mService != null && arg2 != null)
					mService.sendMessageToRemote(arg2);
			default:
				break;
		}
	}

	static ArrayList<String> alarms_date;
	static ArrayList<String> alarms_OnOff;
	static int callNumber = 0;

	public static class MyReceiver extends BroadcastReceiver {
		private int pState = TelephonyManager.CALL_STATE_IDLE;
		private Context mContext = null;
		DeviceConnectTask connectTask;
		private Preferences mPreference = null;

		private com.standingegg.band.ota.BluetoothManager bluetoothManager = null;
		static final String smsACTION = "android.provider.Telephony.SMS_RECEIVED";

		ArrayList<String> phone = new ArrayList<String>();

		@Override
		public void onReceive(Context context, Intent intent) {
			final TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			Bundle bundle = intent.getExtras();
			int s = bundle.getInt(Constants.BROADCAST_RECEIVER);
			mConnectionInfo = ConnectionInfo.getInstance(context);
			mContext = context;
			mPreference = Preferences.getInstance(mContext);


			switch (s) {
				case Constants.BROADCAST_FIND:
					vibratiorMessageSend(0);
					break;

				case Constants.BROADCAST_BAT:
					feedback = true;
					flagBAT = !flagBAT; // false -> true
					//mService.sendMessageToRemote("RBAT");
					batteryMessageSend();
					break;
				case Constants.BROADCAST_ALARM_SET:

					alarms_date = bundle.getStringArrayList("ALARMS_DAYS");
					alarms_OnOff = bundle.getStringArrayList("ALARMS_OnOff");

					flagCWUTS = true; // false->true
					feedback = false;

					for(int i = alarms_date.size(); i > 0; i--) {
						if(alarms_OnOff.get(i-1).equals("Off")) {
							alarms_date.remove(i-1);
							alarms_OnOff.remove(i-1);
						}
					}

					AlarmMessageSend();
//					mService.sendMessageToRemote("CWUTS");
					break;

				case Constants.BROADCAST_WECHAT:
					try {
						if(mPreference != null && mPreference.getWechatNoti()){
							if(mConnectionInfo.mViblation != null) {
								return;
							}

							//bluetoothVibratonConnect(mPreference.getKaKaoLedColor());
							bluetoothVibratonConnect(2);
						}
					} catch (NullPointerException e) {
						// TODO: handle exception
						ULog.d("MainActivity", "wechat : "+e);
					}
					break;
				case Constants.BROADCAST_QQ:
					try {
						if(mPreference != null && mPreference.getQqNoti()){
							if(mConnectionInfo.mViblation != null) {
								return;
							}

							//bluetoothVibratonConnect(mPreference.getKaKaoLedColor());
							bluetoothVibratonConnect(2);
						}
					} catch (NullPointerException e) {
						// TODO: handle exception
						ULog.d("MainActivity", "qq : "+e);
					}
					break;
				case Constants.BROADCAST_VERSION:

					feedback = true;
					flagVersion = !flagVersion; // false -> true
					mService.sendMessageToRemote("VER");

					break;
				default:
					break;
			}

			//phone call listen
			telManager.listen(new PhoneStateListener() {
				public void onCallStateChanged(int state, String incomingNumber) {
					if (state != pState) {
						try {
							if(mPreference != null && mPreference.getCAllNoti()) {
								if (state == TelephonyManager.CALL_STATE_IDLE) {
									ULog.i("Phone", "IDLE");
									callNumber=0;
								} else if (state == TelephonyManager.CALL_STATE_RINGING) {
									if(callNumber < 1) {
										bluetoothVibratonConnect(mPreference.getCAllLedColor());
									}
									callNumber++;
									ULog.i("Phone", "RINGING incomingNumber=====" + incomingNumber);
									ULog.i("Phone", "RINGING getLine1Number=====" + telManager.getLine1Number());
									if(telManager.getLine1Number() != null) {
										phone = GetUserContactsList(telManager.getLine1Number());
									}
								} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
									ULog.i("Phone", "OFFHOOK");
								}
							}
						} catch (NullPointerException e) {
							// TODO: handle exception
							ULog.d("MainActivity", "call : "+e);
						}
						pState = state;
					}
				}
			}, PhoneStateListener.LISTEN_CALL_STATE);

			try {
				if (intent.getAction().equals(smsACTION)) {
					try {
						if(mPreference != null && mPreference.getSMSNoti()) {
							if(mConnectionInfo.mViblation != null) {
								return;
							}


							//Bundel 널 체크
							Bundle smsBundle = intent.getExtras();
							if (smsBundle == null) {
								return;
							}

							//pdu 객체 널 체크
							Object[] pdusObj = (Object[]) bundle.get("pdus");
							if (pdusObj == null) {
								return;
							}
							for (int i = 0; i < pdusObj.length; i++) {

								SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
								String phoneNumber = currentMessage.getDisplayOriginatingAddress();

								String senderNum = phoneNumber;
								String message = currentMessage.getDisplayMessageBody();

								Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

							} // end for loop


							bluetoothVibratonConnect(mPreference.getSMSLedColor());
						}
					} catch (NullPointerException e) {
						// TODO: handle exception
						ULog.d("MainActivity", "sms : "+e);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		private void bluetoothVibratonConnect(int led) {
			if(mService == null || mService.mBleManager == null || mService.mBleManager.getState() != 16) {
				Log.e(TAG, "mConnectionInfo.getDeviceAddress() =============" + mConnectionInfo.getDeviceAddress());
				if(mConnectionInfo.getDeviceAddress() != null) {
					bluetoothManager = new BluetoothManager(mContext) {
						@Override
						public void processStep(Intent intent2) {
							// TODO Auto-generated method stub
							Log.e(TAG, "test processStep : " + intent2);
						}
						@Override
						protected int getSpotaMemDev() {
							// TODO Auto-generated method stub
							Log.e(TAG, "test getSpotaMemDev =============");
							return 0;
						}
					};
					bluetoothManager.setDevice((BluetoothDevice) BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mConnectionInfo.getDeviceAddress()));

					connectTask = new DeviceConnectTask(mContext, bluetoothManager.getDevice());
					connectTask.execute();
					mConnectionInfo.isConnected("true");
					mConnectionInfo.isViblation(""+led);
					Log.e(TAG, "mConnectionInfo =============" + mConnectionInfo);
				}
			}else {
				//mService.sendMessageToRemote("VIB" + led);
				vibratiorMessageSend(led);
			}
		}

		private void vibratiorMessageSend(int led) {
			StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
			builder.setSerpCommand(mService.mBleManager.MESSAGE_VIBRATION_REQUEST);
			builder.setLedColor(led);

			try {
				byte[] encoded = new byte[builder.build().toByteArray().length + 5];
				byte[] builderTobyte = builder.build().toByteArray();

				encoded[0] = 'S';
				encoded[1] = 'E';
				int i;
				for (i = 0; i < builderTobyte.length; i++) {
					encoded[i + 2] = builderTobyte[i];
				}
				int lastBuffer = builder.build().toByteArray().length + 5;
				encoded[i + 2] = 'E';
				encoded[i + 3] = '\r';
				encoded[i + 4] = '\n';
				mService.sendMessageToRemote(encoded);
			}catch (UninitializedMessageException e){
				e.printStackTrace();
			}
		}

		private void batteryMessageSend() {
			StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
			builder.setSerpCommand(mService.mBleManager.MESSAGE_BATTERY_REQUEST);

			try {
				byte[] encoded = new byte[builder.build().toByteArray().length + 5];
				byte[] builderTobyte = builder.build().toByteArray();

				encoded[0] = 'S';
				encoded[1] = 'E';
				int i;
				for (i = 0; i < builderTobyte.length; i++) {
					encoded[i + 2] = builderTobyte[i];
				}
				int lastBuffer = builder.build().toByteArray().length + 5;
				encoded[i + 2] = 'E';
				encoded[i + 3] = '\r';
				encoded[i + 4] = '\n';
				mService.sendMessageToRemote(encoded);
			}catch (UninitializedMessageException e){
				e.printStackTrace();
			}
		}

		private void AlarmMessageSend() {
			StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
//			builder.setSerpCommand(mService.mBleManager.MESSAGE_ALARM_REQUEST);
//			builder.setAlarm(alarms_date);

			try {
				byte[] encoded = new byte[builder.build().toByteArray().length + 5];
				byte[] builderTobyte = builder.build().toByteArray();

				encoded[0] = 'S';
				encoded[1] = 'E';
				int i;
				for (i = 0; i < builderTobyte.length; i++) {
					encoded[i + 2] = builderTobyte[i];
				}
				int lastBuffer = builder.build().toByteArray().length + 5;
				encoded[i + 2] = 'E';
				encoded[i + 3] = '\r';
				encoded[i + 4] = '\n';
				mService.sendMessageToRemote(encoded);
			}catch (UninitializedMessageException e){
				e.printStackTrace();
			}
		}

		public ArrayList<String> GetUserContactsList(String phone) {
			boolean isPhone = false;
			String phoneNumber = null, phoneName = null, phoneId = null;
			ArrayList<String> phoneArray = new ArrayList<String>();

			String [] arrProjection = {
					ContactsContract.Contacts._ID,
					ContactsContract.Contacts.DISPLAY_NAME
			};
			String [] arrPhoneProjection = {
					ContactsContract.CommonDataKinds.Phone.NUMBER
			};


			// get user list
			Cursor clsCursor = mContext.getContentResolver().query (
					ContactsContract.Contacts.CONTENT_URI, arrProjection,
					ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1" ,
					null, null
			);

			while( clsCursor.moveToNext() )
			{
				String strContactId = clsCursor.getString( 0 );



				// phone number
				Cursor clsPhoneCursor = mContext.getContentResolver().query (
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						arrPhoneProjection,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + strContactId,
						null, null
				);

				while( clsPhoneCursor.moveToNext() )
				{
					if(phone.equals(clsPhoneCursor.getString( 0 ))) {
						isPhone = true;
						phoneId = clsCursor.getString( 0 );
						phoneName = clsCursor.getString( 1 );
						phoneNumber = clsPhoneCursor.getString( 0 );
					}
				}
				clsPhoneCursor.close();
			}
			clsCursor.close();

			if(isPhone) {
				phoneArray.add(phoneId);
				phoneArray.add(phoneName);
				phoneArray.add(phoneNumber);
				return phoneArray;
			}

			return null;
		}
	}

	private void heartRateMessageSend() {
		StnEggPkt.StnEggPacket.Builder builder = StnEggPkt.StnEggPacket.newBuilder();
		builder.setSerpCommand(mService.mBleManager.MESSAGE_HEART_RATE_REQUEST);

		try {
			byte[] encoded = new byte[builder.build().toByteArray().length+5];
			byte[] builderTobyte = builder.build().toByteArray();

			encoded[0] = 'S';
			encoded[1] = 'E';
			int i;
			for( i = 0; i < builderTobyte.length; i++) {
				encoded[i+2] = builderTobyte[i];
			}
			encoded[i+2] = 'E';
			encoded[i+3] = '\r';
			encoded[i+4] = '\n';
			mService.sendMessageToRemote(encoded);
		}catch (UninitializedMessageException e){
			e.printStackTrace();
		}
	}

	private RunningServiceInfo isRunningService(Context context, Class<?> cls) {
		boolean isRunning = false;

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);

		if (info != null) {
			for (ActivityManager.RunningServiceInfo serviceInfo : info) {
				ComponentName compName = serviceInfo.service;
				String className = compName.getClassName();

				if (className.equals(cls.getName())) {
					return serviceInfo;
					// break;
				}
			}
		}
		// return isRunning;
		return null;
	}

	public void logout(){
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(mContext);
		alt_bld.setMessage(mContext.getResources().getString(R.string.logout_)).setCancelable(false).setPositiveButton(getString(R.string.set), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mTodayPreference.resetActivityPreferences();
				mTodayPreference.resetSleepPreferences();
				mPreference.resetPreferences();
				mPreference.resetAlarms();
//				dbManager.dropDB();
				mConnectionInfo.resetConnectionInfo();

				Intent intro = new Intent(getApplicationContext(), IntroActivity.class);

				doStopService();

				intro.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intro.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getApplicationContext().startActivity(intro);

				startActivity(intro);
				dialog.cancel();
				MainActivity.this.finish();
			}
		}).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = alt_bld.create();
		alert.setTitle(getString(R.string.set));
		alert.show();
	}


	//20160728 start
	private static final String HEXES = "0123456789ABCDEF";
	public static String byteArrayToHexString(final byte[] array) {
		final StringBuilder sb = new StringBuilder();
		boolean firstEntry = true;
		sb.append('[');

		for (final byte b : array) {
			if (!firstEntry) {
				sb.append(", ");
			}
			sb.append(HEXES.charAt((b & 0xF0) >> 4));
			sb.append(HEXES.charAt((b & 0x0F)));
			firstEntry = false;
		}

		sb.append(']');
		return sb.toString();
	}
	//20160728 end

	public ArrayList<String> GetUserContactsList(String phone) {
		boolean isPhone = false;
		String phoneNumber = null, phoneName = null, phoneId = null;
		ArrayList<String> phoneArray = new ArrayList<String>();

		String [] arrProjection = {
				ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME
		};
		String [] arrPhoneProjection = {
				ContactsContract.CommonDataKinds.Phone.NUMBER
		};


		// get user list
		Cursor clsCursor = mContext.getContentResolver().query (
				ContactsContract.Contacts.CONTENT_URI, arrProjection,
				ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1" ,
				null, null
		);

		while( clsCursor.moveToNext() )
		{
			String strContactId = clsCursor.getString( 0 );



			// phone number
			Cursor clsPhoneCursor = mContext.getContentResolver().query (
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					arrPhoneProjection,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + strContactId,
					null, null
			);

			while( clsPhoneCursor.moveToNext() )
			{
				if(phone.equals(clsPhoneCursor.getString( 0 ))) {
					isPhone = true;
					phoneId = clsCursor.getString( 0 );
					phoneName = clsCursor.getString( 1 );
					phoneNumber = clsPhoneCursor.getString( 0 );
				}
			}
			clsPhoneCursor.close();
		}
		clsCursor.close();

		if(isPhone) {
			phoneArray.add(phoneId);
			phoneArray.add(phoneName);
			phoneArray.add(phoneNumber);
			return phoneArray;
		}

		return null;
	}
}
