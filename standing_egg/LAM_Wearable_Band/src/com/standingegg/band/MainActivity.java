package com.standingegg.band;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.apache.http.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.standingegg.band.activity.ActivityDetailPartActivity;
import com.standingegg.band.activity.ActivityFragment;
import com.standingegg.band.activity.TimeLineAdapter;
import com.standingegg.band.bluetooth.ConnectionInfo;
import com.standingegg.band.contents.DBManager;
import com.standingegg.band.login.LoginDataLoadingFragment;
import com.standingegg.band.login.UserProfileActivity;
import com.standingegg.band.ota.BluetoothManager;
import com.standingegg.band.ota.DeviceConnectTask;
import com.standingegg.band.packet.RecvSendPacket;
import com.standingegg.band.packet.serverAutoUpdate;
import com.standingegg.band.service.BTCTemplateService;
import com.standingegg.band.setting.AlarmAcitivity;
import com.standingegg.band.setting.SettingActivity;
import com.standingegg.band.setting.SettingActivity.MyReceiver1;
import com.standingegg.band.setting.SupportActivity;
import com.standingegg.band.setting.SupportActivity.MyReceiver2;
import com.standingegg.band.sleeping.SleepDetailPartActivity;
import com.standingegg.band.sleeping.SleepFragment;
import com.standingegg.band.util.AppSettings;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.RecycleUtils;
import com.standingegg.band.util.TodayPreferences;
import com.standingegg.band.util.ULog;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@ReportsCrashes(formUri = "", // will not be used
mailTo = "jess.choi@standing-egg.co.kr", customReportContent = { ReportField.APP_VERSION_CODE,
		ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA,
		ReportField.STACK_TRACE,
		ReportField.LOGCAT }, mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)
public class MainActivity extends FragmentActivity implements ActionBar.TabListener, IFragmentListener {

	private static final String TAG = "MainActivity";

	private Context mContext;
	private static BTCTemplateService mService;
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

	// UI
	private FragmentManager mFragmentManager;
	private static FragmentAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private FrameLayout mMainFrame;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerList;
	private ImageView mPagerIcon;
	
	ListView mListView;
	private TimeLineAdapter timeline;
	
	
	private ArrayList<String> timeLine1Text;
	private ArrayList<String> timeLine2Text;

	// test
	private ImageView mImageBT = null;
	private TextView mTextStatus = null;
	
	
	private boolean connect_flag = false;

	// ble 통신
	boolean flagSendDT = false;
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
// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	boolean initialized = false; // Preferences로 빼야함
									// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
	int ligthSleepCntRt = 0;

	static int mActivityAllCount = 0;
	static int mActivityAllM = 0;
	static int mActivityAllCal = 0;

	// preferences 값
	ArrayList<Integer> mTodayActCnt;  	 //graph 표시  - ActivityTodayDetail
	ArrayList<Integer> mTodayActCntTimeHour; //graph 표시	//시간
	ArrayList<Integer> mTodayActCntTimeMin; //graph 표시   //분 앞자리

	
	//sleep 배열 끊는 갯수
	ArrayList<Integer> mSleepConnFlag;
	
	boolean mDaySleepDataFlag = false;  //누적 데이터중 Sleep 데이터 누적 중이었을 경우!
	int mSleepConnCnt = 0;    //누적 데이터중 Sleep 데이터 누적 cnt int mSleepConnFlag
	
	ArrayList<Integer> mTempSleepCnt;   	   //sleep 누적 cnt(graph height/deep, low, wake 구분) 
	ArrayList<String> mTempSleepDate; 
	ArrayList<String> mTempSleepTime; 
	ArrayList<Integer> mTempSleepCntTimeMin;  
	
	
	ArrayList<Integer> mTodaySleepCnt;  	 //graph 표시  - ActivityTodayDetail
	ArrayList<Integer> mTodaySleepStartTimeHour; //graph 표시	//시간
	ArrayList<Integer> mTodaySleepStartTimeMin; //graph 표시   //분 앞자리
	ArrayList<Integer> mTodaySleepCntTimeMin; //graph 표시   //얼마 동안!
	
	int mWalkDistance = 0; 	//ActivityTodayDetail - 걸은 거리
	int mWalkHour = 0;	    //ActivityTodayDetail - 걸은 시간
	int mWalkMin = 0;	    //ActivityTodayDetail - 걸은 분..? 왜나눴지...?
	int mWalkCal = 0; //ActivityTodayDetail - 걸음으로 소모한 cal량
	
	int mRunDistance = 0; 	 //ActivityTodayDetail - 뛴 거리
	int mRunHour = 0;	   		//ActivityTodayDetail - 뛴 시간
	int mRunMin = 0;	   		//ActivityTodayDetail - 뛴 분
	int mRunCal = 0; 		//ActivityTodayDetail - 뛰어선 소모한 cal량
	
	//SleepTodayDetail 
	int mDeepSleepMin = 0;	// 숙면 분
	int mLowSleepMin = 0;	// 얕은 잠 분
	int mAwakeTime = 0;	// 깨어있는시간
	String mTodaySleepStartTime;  
	String mTodaySleepEndTime; 
	boolean showLoading =false;
	
	private boolean mActGoalOK = false;
	
	private boolean mMenuDrawableOnOff = false;
	
	String mToday;
	
	
	//preferences
	int dailyDataCnt = 0; // 오늘 - 디바이스 등록일 
	

	//connection정보
	private static ConnectionInfo mConnectionInfo = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		
		dbManager = new DBManager(getApplicationContext(), "lamband.db", null, 1);
		
		mFragmentManager = getSupportFragmentManager();
		mSectionsPagerAdapter = new FragmentAdapter(mFragmentManager, mContext, this, mActivityHandler);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		mPagerIcon = (ImageView) findViewById(R.id.main_pager);

		mMainFrame = (FrameLayout) findViewById(R.id.frame);

		
		
		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[10];
		 
		drawerItem[0] = new ObjectDrawerItem(R.drawable.element_menu_home, "홈");
		drawerItem[1] = new ObjectDrawerItem(R.drawable.element_menu_profile, "프로필");
		drawerItem[2] = new ObjectDrawerItem(R.drawable.element_menu_setting, "설정");
		drawerItem[3] = new ObjectDrawerItem(R.drawable.element_menu_alarm, "알람");
		drawerItem[4] = new ObjectDrawerItem(R.drawable.element_menu_support, "지원");
		drawerItem[5] = new ObjectDrawerItem(android.R.color.transparent, "");
		drawerItem[6] = new ObjectDrawerItem(android.R.color.transparent, "");
		drawerItem[7] = new ObjectDrawerItem(android.R.color.transparent, "");
		drawerItem[8] = new ObjectDrawerItem(android.R.color.transparent, "");
		drawerItem[9] = new ObjectDrawerItem(R.drawable.element_menu_logout, "로그아웃");
		
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);

		mDrawerToggle = new ActionBarDrawerToggle(
		        this,
		        mDrawerLayout,
		        R.drawable.element_menu, 
		        R.string.menu_connect, 
		        R.string.menu_connect
		        ) {
		    
		    /** Called when a drawer has settled in a completely closed state. */
		    public void onDrawerClosed(View view) {
		        super.onDrawerClosed(view);
//		        getActionBar().setTitle(mTitle);
		        mMenuDrawableOnOff = false;
		    }
		 
		    /** Called when a drawer has settled in a completely open state. */
		    public void onDrawerOpened(View drawerView) {
		        super.onDrawerOpened(drawerView);
		        mMenuDrawableOnOff = true;
//		        getActionBar().setTitle(mDrawerTitle);
		    }
		};
		 
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.navigator_list, drawerItem);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		// Fragment view Pager
		mDrawerList = (ListView) findViewById(R.id.navi_list);
		mDrawerList.setAdapter(adapter);
		
		// mDrawerList.setSty
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		mViewPager.setOnPageChangeListener( new
		ViewPager.SimpleOnPageChangeListener() {
			@Override 
			public void onPageSelected(int position) { // When swiping between pages, select the // corresponding tab.
				switch (position) {
				case 0:
					mPagerIcon.setImageResource(R.drawable.element_swipe_icon_1);
					break;

				case 1:
					SleepFragment frg = (SleepFragment) mSectionsPagerAdapter .getItem(FragmentAdapter.FRAGMENT_POS_SLEEP);
					frg.updateUi();
					mPagerIcon.setImageResource(R.drawable.element_swipe_icon_2);
					break;
				}
			} 
		});
			

		// Setup views
		mImageBT = (ImageView) findViewById(R.id.status_title);
		mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
		mTextStatus = (TextView) findViewById(R.id.status_text);
		mTextStatus.setText(getResources().getString(R.string.bt_state_init));

		mListView = (ListView) findViewById(R.id.listView);

		//
		timeline = new TimeLineAdapter(getApplicationContext(), R.layout.day_flow_draw);

		mListView.setAdapter(timeline);
		
		timeLine1Text = new ArrayList<String>();
		timeLine2Text = new ArrayList<String>();

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
		
		mTodayActCnt  = new ArrayList<Integer>();
		mTodayActCntTimeHour = new ArrayList<Integer>();
		mTodayActCntTimeMin = new ArrayList<Integer>();
		
		mSleepConnFlag = new ArrayList<Integer>();
		
		
		mTempSleepCnt = new ArrayList<Integer>();
		mTempSleepDate = new ArrayList<String>();
		mTempSleepTime = new ArrayList<String>(); 
		mTempSleepCntTimeMin = new ArrayList<Integer>();
		
		
		mTodaySleepCnt = new ArrayList<Integer>();
		mTodaySleepStartTimeHour = new ArrayList<Integer>();
		mTodaySleepStartTimeMin = new ArrayList<Integer>();
		mTodaySleepCntTimeMin = new ArrayList<Integer>();
		
		mConnectionInfo = ConnectionInfo.getInstance(mContext);
		mConnectionInfo.setDeviceAddress(address);
		
		
//		mTodayPreference.resetSleepPreferences();

//		Cursor cursor = dbManager.selectDailyActivity();
//		while (cursor.moveToNext()) {
//			dailyDataCnt++;
//			if(cursor.isLast()){
//				mPreference.setLastUseDate(cursor.getString(0));
//			}
//		}
		
		setTodayValues();
		setTimeLine();
		
		
		
		// Do data initialization after service started and binded
		doStartService();
		
		
		dbautoupdate();
	}
	
	
	private void setTodayValues() {
		mActivityAllCount = mTodayPreference.getActivityAllCount();
		mActivityAllM = mTodayPreference.getWalkDistance() + mTodayPreference.getRunDistance();
		mActivityAllCal = mTodayPreference.getWalkCal()+mTodayPreference.getRunCal();

		mTodayActCnt = mTodayPreference.getTodayActCnt();
		mTodayActCntTimeHour = mTodayPreference.getTodayActCntTimeHour();
		mTodayActCntTimeMin = mTodayPreference.getTodayActCntTimeMin();

		
		mWalkDistance = mTodayPreference.getWalkDistance();
		mWalkHour= mTodayPreference.getWalkMin()/60;
		mWalkMin = mTodayPreference.getWalkMin()%60;
		mWalkCal = mTodayPreference.getWalkCal();
		
		mRunDistance  = mTodayPreference.getWalkDistance();
		mRunHour = mTodayPreference.getRunMin()/60;
		mRunMin = mTodayPreference.getRunMin()%60;
		mRunCal = mTodayPreference.getRunCal();
		
		
		
		mTodaySleepStartTime = mTodayPreference.getSleepTime();
		mTodaySleepEndTime = mTodayPreference.getWakeUpTime();

		mTodaySleepCnt = mTodayPreference.getTodaySleepHeight();
		mTodaySleepCntTimeMin = mTodayPreference.getTodaySleepCnt();
		mAwakeTime = mTodayPreference.getAwakeMin();
		
		mDeepSleepMin = mTodayPreference.getDeepSleepMin();
		mLowSleepMin = mTodayPreference.getLowSleepMin();
		
		if(mTodayPreference.getTodayTimeLine1() != null )
		{
			
ULog.v("intro timeLine1Text:" + mTodayPreference.getTodayTimeLine1());
ULog.e("intro timeLine1Text:" + mTodayPreference.getTodayTimeLine1());
ULog.v("intro timeLine2Text:" + mTodayPreference.getTodayTimeLine2());
ULog.e("intro timeLine2Text:" + mTodayPreference.getTodayTimeLine2());
			timeLine1Text = mTodayPreference.getTodayTimeLine1();
			timeLine2Text = mTodayPreference.getTodayTimeLine2();
		}
		
		mActGoalOK = mTodayPreference.getUserActGoalOK();
		
		
		
	}

	public class ObjectDrawerItem {
	    public int icon;
	    public String name;
	 
	    // Constructor.
	    public ObjectDrawerItem(int icon, String name) {
	        this.icon = icon;
	        this.name = name;
	    }
	}
	
	public class DrawerItemCustomAdapter extends ArrayAdapter<ObjectDrawerItem> {
		 
	    Context mContext;
	    int layoutResourceId;
	    ObjectDrawerItem data[] = null;
	 
	    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, ObjectDrawerItem[] data) {
	 
	        super(mContext, layoutResourceId, data);
	        this.layoutResourceId = layoutResourceId;
	        this.mContext = mContext;
	        this.data = data;
	    }
	 
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View listItem = convertView;
	 
	        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
	        listItem = inflater.inflate(layoutResourceId, parent, false);
	      
//	        if(position == 5){
//	        	RelativeLayout.LayoutParams d =  (RelativeLayout.LayoutParams) listItem.getLayoutParams();
//	        	d.pa
//	        	
//	        	listItem.setLayoutParams(d);
//	        }
	 
	        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
	        
	        TextView textViewName = (TextView) listItem.findViewById(R.id.menu_txt);
	        LinearLayout line = (LinearLayout) listItem.findViewById(R.id.menu_linear);
	        
	        ObjectDrawerItem folder = data[position];
	        imageViewIcon.setImageResource(folder.icon);
	        textViewName.setText(folder.name);
	       if(position > 4){
	    	   line.setVisibility(View.INVISIBLE);
	        }
	        
	        return listItem;
	    }
	}
	

	public void setTimeLine() {
		DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
		int height = dm.heightPixels;

		ULog.v(TAG , "height === > "  + height);
		
		if(JOIN_FLAG){
			ULog.d("timeLine1Text-------"+timeLine1Text);
			timeLine1Text.add("Hercules App을 시작합니다.");
			if(name==null){
				name = mPreference.getUserName();
			}
			timeLine2Text.add(name+"님 환영합니다!");
			
			Bundle bundle = new Bundle();
			
			bundle.putStringArrayList(Constants.TODAY_TIME_LINE_1, timeLine1Text);
			bundle.putStringArrayList(Constants.TODAY_TIME_LINE_2, timeLine2Text);
			
			mTodayPreference.setTimeLine(bundle);
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.detail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action:

			if (mViewPager.getCurrentItem() == 0) {
				Intent i = new Intent(mContext, ActivityDetailPartActivity.class);
				startActivityForResult(i, Constants.ACTIVITY_SLEEP_FLAG);
			} else {
				Intent i = new Intent(mContext, SleepDetailPartActivity.class);
				startActivityForResult(i, Constants.ACTIVITY_SLEEP_FLAG);
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

		fragmentManager.replace(R.id.frame2, fragment).commit();
	}

	int cnt = 0;
	private void DialogSimple(String s) {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage(s).setCancelable(false).setNegativeButton("확인", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				doStopService();
				android.os.Process.killProcess(android.os.Process.myPid());
				dialog.cancel();
				moveTaskToBack(true);
				finish();
			}
		});
		AlertDialog alert = alt_bld.create();
		alert.setTitle("알림");
		alert.show();
	}
	
	
	@Override
	public synchronized void onStart() {
		super.onStart();
		
		/*
		 * TimerTask timerTask = new TimerTask() {
		 * 
		 * @Override public void run() { if(cnt !=0 ){ ActivityFragment frg2 =
		 * (ActivityFragment)
		 * mSectionsPagerAdapter.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
		 * frg2.showsendMessage("PKSTVCALL"); } cnt++;
		 * 
		 * } };
		 * 
		 * Timer timer = new Timer(); timer.schedule(timerTask, 1000, 1000);
		 */

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
			mRefreshTimer.cancel();
			mRefreshTimer = null;
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

	@Override
	public void onBackPressed() {
		if(mMenuDrawableOnOff){ 
			mDrawerLayout.closeDrawer(mDrawerList);
			return; 
		}
		
		finalizeActivity();
		moveTaskToBack(true);
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
		doStopService();
	}

	boolean connection = false;

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
			switch (position) {
			case 0:

				break;
			case 1:
				Intent profileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
				startActivity(profileIntent);

				break;
			case 2:
				Intent setting = new Intent(getApplicationContext(), SettingActivity.class);
				startActivity(setting);

				break;
			case 3:
				Intent alarm = new Intent(getApplicationContext(), AlarmAcitivity.class);
				startActivity(alarm);

				break;
			case 4:
				Intent support = new Intent(getApplicationContext(), SupportActivity.class);
				startActivityForResult(support, Constants.REQUEST_SIGN_OUT);
				break;
			case 9:
				
				AlertDialog.Builder alt_bld = new AlertDialog.Builder(mContext);
				alt_bld.setMessage("로그아웃 하시겠습니까?\n로그아웃하시면 오늘 데이터는 저장되지 않습니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mTodayPreference.resetActivityPreferences();
						mTodayPreference.resetSleepPreferences();
						mPreference.resetPreferences();
						mPreference.resetAlarms();
						dbManager.dropDB();
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
				}).setNegativeButton("취소", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				AlertDialog alert = alt_bld.create();
				alert.setTitle("확인");
				alert.show();
				

				break;
			}
			mDrawerLayout.closeDrawer(mDrawerList); // 추가됨
		}
	};

	public void findBand() {
		if (mService != null) {
			mService.sendMessageToRemote("VIB");
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stubg

	}

	/**
	 * Service connection
	 */
	private ServiceConnection mServiceConn = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			Log.d(TAG, "Activity - Service connected");

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
		Log.d(TAG, "# Activity - doStartService()");
		startService(new Intent(this, BTCTemplateService.class));
		bindService(new Intent(this, BTCTemplateService.class), mServiceConn, Context.BIND_AUTO_CREATE);

		ULog.d("address =======>  " + address);
		// if(!connection)
		// connection = !connection;
		/*
		 * else{ connection = false; mService.disConnect(); }
		 */
	}

	/**
	 * Stop the service
	 */
	private void doStopService() {
		Log.d(TAG, "# Activity - doStopService()");
		mService.finalizeService();
		stopService(new Intent(this, BTCTemplateService.class));

		// 제거
//		unbindService(mServiceConn);
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
			mRefreshTimer.cancel();
		}
		
		if(mService !=null){
			mService.connectDevice(address); // BTCTemplate
		}
		

		// Use below timer if you want scheduled job
		 mRefreshTimer = new Timer();
		 mRefreshTimer.schedule(new RefreshTimerTask(),15000);
	}

	private class RefreshTimerTask extends TimerTask {
		public RefreshTimerTask() {
		}

		public void run() {
			mActivityHandler.post(new Runnable() {
				public void run() {
					ULog.e(TAG, "showLoading1:"+showLoading);
					if(!connect_flag){
						DialogSimple("Bluetooth 연결에 실패했습니다. ");
					}								
				}
			});
		}
	}

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
				// 수면 back인지 활동 back인지 구분
			}

			break;
		case Constants.REQUEST_SIGN_OUT:
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
			}
			
			break;
		} // End of switch(requestCode)
	}
	boolean flag = false;

	public boolean connected = false;
	
	public boolean getConnection(){
		return connected;
	}
	
	int conneting_chk =0;
	
	private boolean flag_walk = false;
	private boolean flag_run = false;
	int RAactStartHour = 0;
	int RAactStartMin = 0;
	
	int RATimelineactStartHour = 0;
	int RATimelineactStartMin = 0;
	
	int RATempWalkCount = 0;
	int RATempRunCount= 0;
	
	int RATempActCount = 0;
	int RATempTimeLineActCount = 0;
	
	
	public class ActivityHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			
			ActivityFragment ActFrg = (ActivityFragment) mSectionsPagerAdapter.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
			SleepFragment sleepFrg = (SleepFragment) mSectionsPagerAdapter .getItem(FragmentAdapter.FRAGMENT_POS_SLEEP);
			
			
			switch (msg.what) {
			case Constants.MESSAGE_BT_STATE_INITIALIZED:
				mTextStatus.setText(getResources().getString(R.string.bt_title) + ": "
						+ getResources().getString(R.string.bt_state_init));
				mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
				connected = false;
				
				if(conneting_chk>4){
					DialogSimple("Bluetooth 연결이 끊어졌습니다. ");
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
				connected = true;
				break;
			case Constants.MESSAGE_BT_STATE_CONNECTED:
				if (mService != null) {
					String deviceName = mService.getDeviceName();
					if (deviceName != null) {
						mTextStatus.setText(getResources().getString(R.string.bt_title) + ": "
								+ getResources().getString(R.string.bt_state_connected) + " " + deviceName);
						mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_online));
					} else {
						mTextStatus.setText(getResources().getString(R.string.bt_title) + ": "
								+ getResources().getString(R.string.bt_state_connected) + " no name");
						mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_online));
					}

					flag = true;
					// loadingFragment()
					flagSendDT = false;
					flagRUSER = false;
					flagUR = false;
					flagRA = false;
					flagRS = false;
					flagBAT = false;
					flagVersion = false;
					flagCWUTS = false;
					feedback = false;
					connected = true;
//					setLoadingEnd();
				}
				break;
			case Constants.MESSAGE_BT_STATE_ERROR:
				mTextStatus.setText(getResources().getString(R.string.bt_state_error));
				mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_busy));
				connected = false;
				break;

			// BT Command status
			case Constants.MESSAGE_CMD_ERROR_NOT_CONNECTED:
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
				String message2 = (String) msg.obj;
				
				ActFrg.showsendMessage(message2);
				break;

			case Constants.MESSAGE_READ_CHAT_DATA:
				conneting_chk ++;
				ULog.d("Constants.MESSAGE_READ_CHAT_DATA");
				ULog.d("Constants.MESSAGE_READ_CHAT_DATA");

				if (msg.obj != null) { 
					String message = (String) msg.obj;
					connect_flag=true;
					ULog.i(TAG, "message:" + message);
					ULog.i(TAG, "feedback:" + feedback);

					ULog.i(TAG, "flagSendDT:" + flagSendDT);
					ULog.i(TAG, "flagRUSER:" + flagRUSER);
					ULog.i(TAG, "flagUR:" + flagUR);
					ULog.i(TAG, "flagRA:" + flagRA);
					ULog.i(TAG, "flagRS:" + flagRS);
					ULog.i(TAG, "flagBAT:" + flagBAT);
					ULog.i(TAG, "flagCWUTS:" + flagCWUTS);

					
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
					
					if(!initialized){
						initialized = true;
						flagSendDT = true; // false -> true
						feedback = true; // false -> true
						ActFrg.sendMessage("YMDT"+ ty + "" + tmm + "" + td + "" + th + "" + tm + "" + ts+"" +tday);
					}
					
					if (message.contains("U-R")){// && flagSendDT) {
						flagUR = true; 
						feedback = true; 
						flagSendDT = false;
						flagRUSER = false;
						flagRS = false;
						flagBAT = false;
						flagVersion = false;
						flagCWUTS = false;
						
						ActFrg.showMessage(message);
						
						ULog.d(TAG, "재연결 U-R ");
						ActFrg.sendMessage("OK");
						mService.sendMessageToRemote("OK");
					}
					
					if (message.contains("R-A") && flagSendDT) {
						flagRA = true; 
						feedback = true; 
						
						flagUR = false; 
						flagSendDT = false;
						flagRUSER = false;
						flagRS = false;
						flagBAT = false;
						flagVersion = false;
						flagCWUTS = false;
						
						ULog.d(TAG, "재연결 R-A ");
						ActFrg.showMessage(message);
						ActFrg.sendMessage("OK");
					}
					if (message.contains("RYMDT")) {
						flagSendDT = true; // false -> true
						feedback = true; // false -> true
						ULog.e("feedback false YMDT"+ ty + "" + tmm + "" + td + "" + th + "" + tm + "" + ts+"" +tday);
						ActFrg.sendMessage("YMDT"+ ty + "" + tmm + "" + td + "" + th + "" + tm + "" + ts+"" +tday);
					}
					
					
					if (!feedback) {
						if (message.contains("RYMDT")) {
							flagSendDT = !flagSendDT; // false -> true
							ULog.i(TAG, "flagSendDT:" + flagSendDT);
							feedback = !feedback; // false -> true
							ULog.e("feedback false YMDT"+ ty + "" + tmm + "" + td + "" + th + "" + tm + "" + ts+"" +tday);
							initialized = true;
							ActFrg.sendMessage("YMDT"+ ty + "" + tmm + "" + td + "" + th + "" + tm + "" + ts+"" +tday);
						}

						if (message.contains("RUSER")) {
							feedback = !feedback;// false -> true
							flagRUSER = !flagRUSER; // false -> true
							ActFrg.showMessage(message);
							
							
							ULog.v(TAG, " mPreference.getUserWeigth()  === > "+mPreference.getUserWeight());
							ULog.v(TAG, " mPreference.getUserActivityGoal()  === > "+mPreference.getUserActivityGoal());
//							frg.sendMessage("USER178078G100");
							
							ActFrg.sendMessage("USER"+mPreference.getUserTall()+ (mPreference.getUserWeight()<10? 
											"0"+mPreference.getUserWeight(): mPreference.getUserWeight())
											+"G"+
											( mPreference.getUserActivityGoal()<100000? "0"+mPreference.getUserActivityGoal() : mPreference.getUserActivityGoal()));
						}
						
						
						if (message.contains("U-R")) {
							flagUR = !flagUR; // false-> true
							feedback = !feedback; // false -> true
							ActFrg.showMessage(message);
							ActFrg.sendMessage("OK");
							mService.sendMessageToRemote("OK");
						}
						

						if (message.contains("R-A")) {
							flagRA = !flagRA; // false-> true
							feedback = !feedback; // false -> true
							ActFrg.showMessage(message);
							ActFrg.sendMessage("OK");
						}

						if (message.contains("R-S")) {
							flagRS = !flagRS; // false-> true
							feedback = !feedback; // false -> true
							// frg.showMessage(message);
							ActFrg.sendMessage("OK");
						}
						

						if (flagCWUTS) {
							if (message.contains("OK")) {
								feedback = !feedback; // false->true
								if(alarms_date.size() == 0) {
									mService.sendMessageToRemote("T000000000000000000"); // 알림 셋팅 값이 없을 때 
								}else if(alarms_date.size() == 1) {
									mService.sendMessageToRemote("T"+alarms_date.get(0)+"000000000000"); // 알림 셋팅 값이 1개 있을 때 
								}else if(alarms_date.size() == 2) {
									mService.sendMessageToRemote("T"+alarms_date.get(0)+alarms_date.get(1)+"000000"); // 알림 셋팅 값이 2개 있을 때
								}else if(alarms_date.size() == 3) {
									mService.sendMessageToRemote("T"+alarms_date.get(0)+alarms_date.get(1)+alarms_date.get(2));
								}
//								mService.sendMessageToRemote("T"+alrams_day+alrams);
//								if(alarms_date.size() > 0) {
//									mService.sendMessageToRemote(alarms_date.get(0));
//									alarms_date.remove(0);	
//									alarms_OnOff.remove(0);	
//								}
							}
						}

					} // feekback false end

					if (feedback) {
						if (flagSendDT ) {
							if (message.contains("OK")) { // 5초안에 ok가 안오면!! 다시
															// send
								flagSendDT = !flagSendDT; // true->false
								feedback = !feedback; // true->false;
								ActFrg.showMessage(message);
								initialized = true;
							}
						}  

						if (flagRUSER) {
							if (message.contains("OK")) {// 5초안에 ok가 안오면!! 다시
															// send
//								initialized = !initialized;// false -> true
								flagRUSER = !flagRUSER; // true -> false
								feedback = !feedback; // true -> false
								// feedback은
								// 그대로 true
								ActFrg.showMessage(message);
							}
						}
						
						
						if (flagUR) {
							if ("UW".equals(message.substring(0, 2))  || "UR".equals(message.substring(0, 2))) {
								
								ActFrg.showMessage(message);
								ActFrg.sendMessage("OK");
								if(mDaySleepDataFlag){
									mDaySleepDataFlag = false;
									mSleepConnFlag.add(mSleepConnCnt);
									mSleepConnCnt=0;
								}
								
								// feedback = !feedback;//false->true
								// UW/ 0367 /S/ 15 05 28 13 45 /E/ 02 36 /C/
								// 01234 /D/ 01234
								String runWalk = message.substring(0, 2);
								
								try {
									int sindex = message.indexOf("S");
									int eindex = message.indexOf("E");
									int cindex = message.indexOf("C");
									int dindex = message.indexOf("D");

									String cnt = message.substring(2, sindex);
									String date = message.substring(sindex + 1, eindex);
									String time = message.substring(eindex + 1, cindex);
									int cal = Integer.parseInt(message.substring(cindex + 1, dindex));
									int m = Integer.parseInt(message.substring(dindex + 1));
									
		//*************date == 오늘 날짜가 아니면  ***************************************************************
		//*************daily 통계 데이터로 변경!!!!!!!!!!!!!!!!!!!********************************
									if(Integer.parseInt(cnt)>=0 && m >= 0 && cal >= 0){
										if((ty+""+tmm+""+td).equals(date.substring(0,6))){   //if(mToday
											//if(mTodayPreference.getDataDate().equals(date.substring(0,6))){
											mActivityAllCount = mActivityAllCount + Integer.parseInt(cnt);
											mActivityAllM = mActivityAllM + m;
											mActivityAllCal = mActivityAllCal + cal;
											int hour = Integer.parseInt(time.substring(0,2));
											int min = Integer.parseInt(time.substring(3));
											if("UW".equals(runWalk)){
												mWalkDistance = mWalkDistance + m;
												mWalkCal = mWalkCal + cal;
												
												mWalkHour  = mWalkHour+ hour;
												mWalkMin  = mWalkMin+ min;
												ULog.d(TAG, cnt + "걸음, " + date + "에 시작해서 " + time + "동안 /" + cal
														+ "cal 소모, " + m + "m");
											}else{
												mRunDistance = mRunDistance + m;
												mRunCal = mRunCal + cal;
												
												mRunHour  = mRunHour+ hour;
												mRunMin  = mRunMin+ min;
												ULog.i(TAG, cnt + "뜀, " + date + "에 시작해서 " + time + "동안 /" + cal + "cal 소모, " + m + "m");
											}
											
											String startHour = date.substring(6,8);
											String startMin = date.substring(8);
											ULog.i(TAG, "startHour:"+startHour+", startMin:"+startMin);
											int URactivityStartMin = Integer.parseInt(startMin);
											int URactivityStartHour = Integer.parseInt(startHour);
											
											int endHour = Integer.parseInt(startHour)+hour;
											int endMin = Integer.parseInt(startMin)+min;
											
											if(endMin >= 60){
												endMin= endMin-60;
												endHour =endHour+1;
											}
											
											String end_h_str = Integer.toString(endHour);
											String end_m_str = Integer.toString(endMin);
											end_h_str = end_h_str.length() == 1? "0"+end_h_str:end_h_str;
											end_m_str = end_m_str.length() == 1? "0"+end_m_str:end_m_str;
											
										
											if((min>=3 || hour>0) || Integer.parseInt(cnt)>= 100){
												String txt1="",txt2="",txt3="",txt4="";
												txt1 = startHour+":"+startMin+" ~ " +end_h_str+":"+end_m_str;
												if(hour == 0){
													txt2 = "총 활동 "+min +" 분 ";
												}else {
													txt2 ="총 활동 "+hour +"시간 "+min +" 분";
												}
												
												
												
												if(m < 1000){
													txt3 = cnt+" 걸음";
												}else{
													txt3 = "약 "+ String.format("%.2f", (m/1000.0f)) +" km";
												}
												
												txt4 = String.format("%.2f", (cal/1000.0f))+ "kcal 소모 ";
													
												timeLine1Text.add(txt1 + " | "+txt2+" | "+txt3);
												timeLine2Text.add(txt4);
											
											
												if(!mActGoalOK && mActivityAllCount >= mPreference.getUserActivityGoal()){
													mActGoalOK = true;
													
													timeLine1Text.add("오늘의 활동 목표량을 달성하셨습니다!");
													timeLine2Text.add("축하합니다!");
												}
											
											}
											
//											updateTimeLine(txt1 , txt2, txt3, txt4);
											
											insertTodayData(URactivityStartMin, URactivityStartHour, cnt);
											
										}else{
//											if(mPreference.getLastUseDate()랑 오늘 날짜랑 하루 차이나면 아래 그대로 / 아니면 ... 빈날짜들 insert 0 )
											
											String yy= date.substring(0,2);
											String mm= date.substring(2,4);
											String dd= date.substring(4,6);
											
											int all_cnt = 0;
											int walk_dis = 0;
											int walk_tim = 0;
											int walk_cal = 0;
											int run_dis = 0;
											int run_tim = 0;
											int run_cal = 0;
											
											int hourr = Integer.parseInt(time.substring(0,2));
											int mmin =  Integer.parseInt(time.substring(2));
											
											if("UW".equals(runWalk)){
												all_cnt = Integer.parseInt(cnt);
												walk_dis = m;
												walk_tim = (hourr*60)+mmin;
												walk_cal = cal;
											}else{
												all_cnt = Integer.parseInt(cnt);
												run_dis = m;
												run_tim = (hourr*60)+mmin;
												run_cal = cal;
											}
											
											if(dbManager.dailyActDate("20"+yy+"-"+mm+"-"+dd) == 0){
												//insert
//												 all_cnt
//												 walk_dis
//												 walk_tim
//												 walk_cal
//												 run_dis
//												 run_tim
//												 run_cal
												dbManager.intertTodayData("20"+yy+"-"+mm+"-"+dd,all_cnt,walk_dis,walk_tim,
														walk_cal,run_dis,run_tim,run_cal );
												
												dbManager.intertTempTodayData("20"+yy+"-"+mm+"-"+dd,all_cnt,walk_dis,walk_tim,
														walk_cal,run_dis,run_tim,run_cal );
												
											}else{
												//update
												//기존데이터 + 방금 받은 데이터 
												dbManager.updateTempDailyData("20"+yy+"-"+mm+"-"+dd,all_cnt,walk_dis,walk_tim,
														walk_cal,run_dis,run_tim,run_cal);
												dbManager.updateDailyData("20"+yy+"-"+mm+"-"+dd,all_cnt,walk_dis,walk_tim,
														walk_cal,run_dis,run_tim,run_cal);
											}
											
										
										}
									}
									
									
									
									

								} catch (Exception e) {
									// TODO: handle exception
								}
							} // U-R Walking, Running End

							else if ("UL".equals(message.substring(0, 2))|| "UD".equals(message.substring(0, 2))) {
							
								sleepFrg.showMessage(message);
								sleepFrg.sendMessage("OK"); // deep Sleep 사이사이에
														// light sleep
								// UL/15 05 28 13 45 /E/ 02 36
								// UD/15 05 28 13 45 /E/ 02 36
								mDaySleepDataFlag = true;
								String deepLow = message.substring(0, 2);
								
								try {
									int eindex = message.indexOf("E");

									String sleep = message.substring(2, eindex);
									
									String sy = sleep.substring(0,2);
									String sm = sleep.substring(2,4);
									String sd = sleep.substring(4,6);
									String sh = sleep.substring(6,8);
									String smm = sleep.substring(8);
									
									String sleeping = message.substring(eindex + 1);
									// h: 수면ing 시간, m: 수면ing분  
									int h = Integer.parseInt(sleeping.substring(0, 2));
									int m = Integer.parseInt(sleeping.substring(2));
									
									int start_h = Integer.parseInt(sh);
									int start_m = Integer.parseInt(smm);
									
									
									if(mTempSleepTime.size()>0){
										//바로전 data 시작시간
										String allt = mTempSleepTime.get(mTempSleepTime.size()-1);
										
										ULog.v("**** sleep today ****" , "mTempSleepTime.get(mTempSleepTime.size()-1)==>" +allt);
										
										int compare_h = Integer.parseInt(allt.substring(0,2));
										int compare_m = Integer.parseInt(allt.substring(2));
										//바로전 data 수면량(수면시간)
										int compare_cnt = mTempSleepCntTimeMin.get(mTempSleepCntTimeMin.size()-1);
										
										ULog.v("**** sleep today ****" , "compare_cnt==>" +compare_cnt);
										ULog.v("**** sleep today ****" , "compare_h==>" + compare_h);
										ULog.v("**** sleep today ****" , "compare_m==>" + compare_m);
										ULog.v("**** sleep today ****" , "start_h==>" + start_h);
										ULog.v("**** sleep today ****" , "start_m==>" + start_m);
										ULog.v("**** sleep today ****" , "compare_cnt/60==>" + compare_cnt/60);
										
										compare_h = compare_h+(compare_cnt/60);  //수면 끝난시간
										compare_m = compare_m + (compare_cnt%60);	//수면끝난 분
										if(compare_m>=60){
											compare_m = compare_m - 60;
											compare_h = compare_h+1;
										}
										
										if(compare_m>=24)
											compare_h = compare_h-24;
										
										//new data의 시작시간이랑 , 이전 data 끝난시간이 같지 않으면 (깨어있는시간 발생)
										if(compare_h != start_h && compare_m!= start_m ){
											
											
											mTempSleepCnt.add(0);

											int h1=0,m1=0;
											h1 = start_h-compare_h; //깨어있던 시간
											m1 = start_m-compare_m; // 깨어있던 분
											
											
											if(h1 < 0)
												h1= h1+24;
											
											ULog.e("시간 음수면 + 24:"+ h1);
											
											if(m1 < 0){
												h1= h1-1;
												m1= m1+60;
											}
											
											ULog.e(h1+"시간 "+ m1+"분 동안 깨어 있었음 ");
											ULog.e("mTempSleepCntTimeMin:"+((h1*60)+m1));
											
											mTempSleepCntTimeMin.add(((h1*60)+m1));
											
											
											ULog.e("start_h: "+ start_h +"/h1:" + h1);
											ULog.e("start_m: "+ start_m +"/m1:" + m1);
											h1 = (start_h-h1) ;
											m1 = (start_m-m1);
											String ssy = sy;
											String ssm = sm;
											String ssd = sd;
											
											ULog.e("깬 시작 시간!:"+ ssy+"년 "+ssm+"월 "+ssd+"일 "+h1+"시 "+m1+"분 ");
											
											
											if(m1 < 0){
												h1=(h1-1);
												m1= m1+60;
											}

											if(h1 < 0){
												Date ddd = new Date((Integer.parseInt(ssy)-1900),Integer.parseInt(ssm)-1,Integer.parseInt(ssd));
												ddd.setDate(ddd.getDate()-1);
												ssy = Integer.toString(ddd.getYear()+1900);
												ssm = Integer.toString(ddd.getMonth()+1);
												ssm = ssm.length()==1? "0"+ssm : ssm;
												ssd = Integer.toString(ddd.getDate()-1);
												ssd = ssd.length()==1? "0"+ssd : ssd;
												h1= 24+h1;
											}
											
											
											ULog.v("**** sleep today ****" , "깨어있는시간 깨어있는시간 깨어있는시간 깨어있는시간");
											ULog.v("**** sleep today ****" , "mTempSleepDate==>" +ssy+ssm+ssd);
											ULog.v("**** sleep today ****" , "mTempSleepTime==>" +(h1<10? "0"+h1: h1) +""+(m1<10? "0"+m1: m1));
											mTempSleepDate.add(ssy+ssm+ssd);
											mTempSleepTime.add((h1<10? "0"+h1: h1) +""+(m1<10? "0"+m1: m1));
											
										}
									}
									
									
									if("UL".equals(deepLow)){
										//mLowSleepMin = mLowSleepMin+ (h*60) + m;   //FIN 의 today graph set 으로 이동
										
										ULog.d(TAG,"Light SLEEP 지금까지 총 "+ mLowSleepMin+"분 동안 Light Sleep ");
										ULog.d(TAG,"Light SLEEP : " + sleep + "에 시작해서 " + sleeping + " 동안 ");
										
										mTempSleepCnt.add(101);
										
									}else{
										//mDeepSleepMin = mDeepSleepMin + (h*60) + m ; //FIN 의 today graph set 으로 이동
									
										ULog.d(TAG,"Deep SLEEP 지금까지 총 "+ mDeepSleepMin+"분 동안 ");
										ULog.d(TAG,"Deep SLEEP : " + sleep + "에 시작해서 " + sleeping + " 동안 ");
										mTempSleepCnt.add(202);
									}
									
									mTempSleepDate.add(sy+sm+sd);
									mTempSleepTime.add(sh+smm);
									
									mTempSleepCntTimeMin.add((h*60)+m);
								
									
									
									
									mSleepConnCnt++;
									
								} catch (Exception e) { // TODO: handle exception

								}
							} // U-R Light, DeepSleep End
							
							else if (message.contains("FIN")) {
								if(mDaySleepDataFlag){
									mDaySleepDataFlag = false;
									mSleepConnFlag.add(mSleepConnCnt);
									mSleepConnCnt=0;
								}
								
								feedback = !feedback; // true -> false;
								flagUR = !flagUR;// true -> false
								
								int length=0;
								int  data_count  = 0;
								for(int mSleepConnCnt : mSleepConnFlag){
									length++;

//									int mDeepSleepMin = 0;	// 숙면 분
//									int mLowSleepMin = 0;	// 얕은 잠 분
//									int mAwakeTime = 0;	// 깨어있는시간
//									
//									String mSleepTime = null; // 잠든 시간
//									String mWakeUpTime = null;// 깬 시간
									
									// 마지막 mSleepConnCnt 이면 today에 add    // 마지막에 잔게 today가 아니라면.....ㅠㅠ
									if(length == mSleepConnFlag.size() && mSleepConnFlag.size() != 0){
										
										for(int i=0; i<mSleepConnCnt; i++ ){
											ULog.e(mTempSleepTime.get(data_count));
											//new data 시작 시간
											int h = Integer.parseInt( (mTempSleepTime.get(data_count)).substring(0, 2));
											int m = Integer.parseInt( (mTempSleepTime.get(data_count)).substring(2));
											
											switch (mTempSleepCnt.get(data_count)) {
											case 101:
												mLowSleepMin = mLowSleepMin+ mTempSleepCntTimeMin.get(data_count);
												break;
												
											case 202:
												mDeepSleepMin = mDeepSleepMin + mTempSleepCntTimeMin.get(data_count);
												break;
											case 0:
												mAwakeTime = mAwakeTime + mTempSleepCntTimeMin.get(data_count);
												break;
											}
											
											mTodaySleepCnt.add(mTempSleepCnt.get(data_count));
											mTodaySleepStartTimeHour.add(h);
											mTodaySleepStartTimeMin.add(m);
											mTodaySleepCntTimeMin.add(mTempSleepCntTimeMin.get(data_count));
											
											
											ULog.v("**** sleep today ****" , "mTempSleepCnt.get(data_count)==>" +mTempSleepCnt.get(data_count));
											ULog.v("**** sleep today ****" , "mTodaySleepStartTimeHour==>" +h);
											ULog.v("**** sleep today ****" , "mTodaySleepStartTimeMin==>" +m);
											ULog.v("**** sleep today ****" , "mTodaySleepCntTimeMin ~동안 잤다==>" +mTempSleepCntTimeMin.get(data_count));
											
											
											
											if(i==0){
												if("00:00".equals(mTodaySleepStartTime)){
													mTodaySleepStartTime = mTempSleepTime.get(data_count);
													ULog.v("**** sleep today ****" , "mTodaySleepStartTime==>" +mTodaySleepStartTime);
												}
											}
											
											if(i == (mSleepConnCnt-1)){
												int compare_cnt = mTempSleepCntTimeMin.get(data_count);
												if(compare_cnt != 0){
													h = h+(compare_cnt/60);  //수면 끝난시간
													m = m + (compare_cnt%60);	//수면끝난 분
												}
												
												if(m>=60){
													m = m - 60;
													h = h+1;
												}
												
												if(h>=24)
													h = h-24;
												
												
												mTodaySleepEndTime = (h<10? "0"+h: h) +""+(m<10? "0"+m: m);
												ULog.v("**** sleep today ****" , "mTodaySleepEndTime==>" +mTodaySleepEndTime);
											}
											data_count++;
										}
										
									}else{
										int light_sleep = 0;
										int deep_sleep = 0;
										String sleep_start="";
										String sleep_end="";
										int awake_time = 0;
										String today_date="";
										
										for(int i=0; i<mSleepConnCnt; i++ ){
											
											switch (mTempSleepCnt.get(data_count)) {
											case 101:
												light_sleep = light_sleep+ mTempSleepCntTimeMin.get(data_count);
												break;
												
											case 202:
												deep_sleep = deep_sleep + mTempSleepCntTimeMin.get(data_count);
												break;
											case 0:
												awake_time = awake_time + mTempSleepCntTimeMin.get(data_count);
												break;
											}
											
											
											if(i==0){
												sleep_start =  mTempSleepTime.get(data_count);
												if("00:00".equals(mTodaySleepStartTime)){
													mTodaySleepStartTime = mTempSleepTime.get(data_count);
													ULog.v("**** sleep today ****" , "mTodaySleepStartTime==>" +mTodaySleepStartTime);
												}
											}
											
											if(i == mSleepConnCnt-1){
												int h = Integer.parseInt( (mTempSleepTime.get(data_count)).substring(0, 2));
												int m = Integer.parseInt( (mTempSleepTime.get(data_count)).substring(2));
												today_date= mTempSleepDate.get(data_count);
												
												int compare_cnt = mTempSleepCntTimeMin.get(data_count);
												h = h+(compare_cnt/60);  //수면 끝난시간
												m = m + (compare_cnt%60);	//수면끝난 분
												
												if(m>=60){
													m = m - 60;
													h = h+1;
												}
												
												String sy = today_date.substring(0,2);
												String sm = today_date.substring(2,4);
												String sd = today_date.substring(4,6);
												
												if(m>=24){
													Date ddd = new Date((Integer.parseInt(sy)-1900),Integer.parseInt(sm)-1,Integer.parseInt(sd));
													ddd.setDate(ddd.getDate()+1);
													sy = Integer.toString(ddd.getYear()+1900);
													sm = Integer.toString(ddd.getMonth()+1);
													sm = sm.length()==1? "0"+sm : sm;
													sd = Integer.toString(ddd.getDate());
													sd = sd.length()==1? "0"+sd : sd;
													
													h = h-24;
												}
												
												today_date = sy+"-"+sm+"-" + sd;
												sleep_end = (h<10? "0"+h: h) +""+(m<10? "0"+m: m);
											
											}
											data_count++;
											//통계내서 수면 daily에 in 
											
										}
										ULog.v("**** sleep daily ****" , "today_date==>" +today_date);
										ULog.v("**** sleep daily ****" , "deep_sleep==>" +deep_sleep);
										ULog.v("**** sleep daily ****" , "light_sleep==>" +light_sleep);
										ULog.v("**** sleep daily ****" , "sleep_start==>" +sleep_start);
										ULog.v("**** sleep daily ****" , "sleep_end==>" +sleep_end);
										ULog.v("**** sleep daily ****" , "awake_time==>" +awake_time);
										
										//수면 update 할일은 ..... 테스트 할때 빼고 없음...
										dbManager.intertTodaySleepData(today_date,deep_sleep,light_sleep,
												sleep_start,sleep_end,awake_time);
										
										dbManager.intertTempTodaySleepData(today_date,deep_sleep,light_sleep,
												sleep_start,sleep_end,awake_time);
									}
								
								}
								
								if(mLowSleepMin>0 ||mDeepSleepMin >0){
									int all_sleep_min = mLowSleepMin+mDeepSleepMin;
									if(all_sleep_min<mPreference.getUserSleepGoal()){
										timeLine1Text.add("지난 밤 수면시간 : "+ all_sleep_min/60 + "시간 " +all_sleep_min%60 +"분");
										timeLine2Text.add("수면 목표량을 달성하지 못했습니다.");
									}else{
										timeLine1Text.add("지난 밤 수면시간 : "+ all_sleep_min/60 + "시간 " +all_sleep_min%60 +"분");
										timeLine2Text.add("수면 목표량을 달성하셨습니다.");
									}
								}
								
								
								ActFrg.showMessage(message);
								ActFrg.sendMessage("OK");
								//temp data server에 up
								setUpdateServer();  //->update  server 후 주간, 월간 다시 계산 
								setActWeekMonth();
//								setDatas();
								
								setLoadingEnd();
								
								mPreference.setAllDis(dbManager.selectAllDistance());
							
							}
						} // U-R if(flagUR) end

						// 계속 통신중
						if (flagRA) {
							
							if(message.contains("END")){
								feedback = !feedback; // true -> false;
								flagRA = !flagRA;
								
	
								Date today = new Date();
								
								int RAendHour = today.getHours();
								int RAendMin = today.getMinutes();
								
								int realtime_compare_min = RAendMin - RAactStartMin;  // 현재 시간 - RA시작 시간 = 지금까지 운동한 시간 
								int realtime_compare_hour = RAendHour - RAactStartHour; // 현재 시간 - RA시작 시간 = 지금까지 운동한 시간 
								
								int timelien_realtime_comp_hour =RAendHour- RATimelineactStartHour;
								int timelien_realtime_comp_min =RAendMin- RATimelineactStartMin;
								
								
								if(realtime_compare_min < 0){
									realtime_compare_min = (RAendMin+60)- RAactStartMin;
									realtime_compare_hour = (RAendHour-1) -RAactStartHour;
								}
								
								if(timelien_realtime_comp_min < 0){
									timelien_realtime_comp_min = (RAendMin+60)- RATimelineactStartMin;
									timelien_realtime_comp_hour = (RAendHour-1) -RATimelineactStartHour;
								}
								
								//RATempRunCount
								int datas[] = countCalDis(RATempActCount, mPreference.getUserTall(),mPreference.getUserWeight());
								int distance = datas[0];
								int cal = datas[1];
								
								mActivityAllM = mActivityAllM + distance;
								mActivityAllCal = mActivityAllCal + cal;
								
								if(flag_walk){ // running data
									mWalkDistance = mWalkDistance+distance;
									mWalkCal = mWalkCal + cal;
								
									mWalkHour  = mWalkHour + realtime_compare_hour;
									mWalkMin  = mWalkMin + realtime_compare_min;
								}	
							
								if(flag_run){ // running data
									mRunDistance = mRunDistance + distance;
									mRunCal = mRunCal + cal;
								
									mRunHour  = mRunHour+ realtime_compare_hour;
									mRunMin  = mRunMin+ realtime_compare_min;
								}
								
								ULog.d(TAG,realtime_compare_min + "동안 " + RATempActCount + " Walk / " + cal + "calories 소모, " + distance + "m");
								ULog.d(TAG,"TIME LINE 용 -->"+timelien_realtime_comp_min + "동안 " + RATempTimeLineActCount + " Walk / " + cal + "calories 소모, " + distance + "m");
								ULog.d(TAG,"TIME LINE 용 -->"+RATimelineactStartHour+":"+RATimelineactStartMin +"부터 "+RAendHour+":" + RAendMin+" 까지 " +timelien_realtime_comp_min + "동안 " + RATempTimeLineActCount + " Walk / " + cal + "calories 소모, " + distance + "m");
								
								
								if((timelien_realtime_comp_min>=3 || timelien_realtime_comp_hour>0) || RATempTimeLineActCount >= 100){
									String txt1="",txt2="",txt3="",txt4="";
									txt1 = RATimelineactStartHour+":"+RATimelineactStartMin+" ~ " +RAendHour+":"+RAendMin;
									
									if(realtime_compare_hour == 0){
										txt2 = "총 활동 "+timelien_realtime_comp_min +" 분 ";
									}else {
										txt2 ="총 활동 "+timelien_realtime_comp_hour +"시간 "+timelien_realtime_comp_min +" 분";
									}
									
									if(distance < 1000){
										txt3 = RATempTimeLineActCount+" 걸음";
									}else{
										txt3 = "약 "+ String.format("%.2f", (distance/1000.0f)) +" km";
									}
									
									txt4 = String.format("%.2f", (cal/1000.0f))+ "kcal 소모 ";
										
									timeLine1Text.add(txt1 + " | "+txt2+" | "+txt3);
									timeLine2Text.add(txt4);
								}
								
								if(!mActGoalOK && mActivityAllCount >= mPreference.getUserActivityGoal()){
									mActGoalOK = true;
									
									timeLine1Text.add("오늘의 활동 목표량을 달성하셨습니다!");
									timeLine2Text.add("축하합니다!");
								}
								
								insertTodayData(RAactStartMin, RAactStartHour, Integer.toString(RATempActCount));   //END로 이동
								
								
								RAactStartHour = RAendHour;
								RAactStartMin = RAendMin;
								RATimelineactStartHour = RAendHour;
								RATimelineactStartMin = RAendMin;
								RATempActCount = 0;
								RATempTimeLineActCount = 0;
								
								
								flag_run = false;
								flag_walk = false;
							}
							
							if("RW".equals(message.substring(0, 2))){
								
								try {
									int realTimeCount = Integer.parseInt(message.substring(2,3));
									mActivityAllCount = mActivityAllCount + realTimeCount;
									
									RATempActCount =RATempActCount+realTimeCount;
									RATempTimeLineActCount = RATempTimeLineActCount+ realTimeCount;
									
									Date today = new Date();
									
									int RAendHour = today.getHours();
									int RAendMin = today.getMinutes();

									int realtime_compare_min = RAendMin - RAactStartMin;  // 현재 시간 - RA시작 시간 = 지금까지 운동한 시간 
									int realtime_compare_hour = RAendHour - RAactStartHour; // 현재 시간 - RA시작 시간 = 지금까지 운동한 시간 
									
									
									if(realtime_compare_min < 0){
										realtime_compare_min = (RAendMin+60)-RAactStartMin;
										realtime_compare_hour = (RAendHour-1) -RAactStartHour;
									}
									
									if(realtime_compare_min > 10){
										//거리,칼로리 계산식 추가 
										
										//RATempRunCount
										int datas[] = countCalDis(RATempActCount, mPreference.getUserTall(),mPreference.getUserWeight());
										int distance = datas[0];
										int cal = datas[1];
										
										mActivityAllM = mActivityAllM + distance;
										mActivityAllCal = mActivityAllCal + cal;
										
										if(flag_walk){ // running data
											mWalkDistance = mWalkDistance+distance;
											mWalkCal = mWalkCal + cal;
										
											mWalkHour  = mWalkHour + realtime_compare_hour;
											mWalkMin  = mWalkMin + realtime_compare_min;
										}	
//											ULog.d(TAG,times + "동안 " + walkcnt + " Walk / " + cal + "calories 소모, " + m + "m");
										
										if(flag_run){ // running data
											mRunDistance = mRunDistance + distance;
											mRunCal = mRunCal + cal;
										
											mRunHour  = mRunHour+ realtime_compare_hour;
											mRunMin  = mRunMin+ realtime_compare_min;
//										
//											ULog.d(TAG, times + "동안 " + walkcnt + " Run / " + cal + "calories 소모, " + m + "m");
										}
										
										insertTodayData(RAactStartMin, RAactStartHour, Integer.toString(RATempActCount));   //END로 이동
										
										RAactStartMin = RAendMin;
										RAactStartHour = RAendHour;
										RATempActCount = 0;
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
							
							if ("AW".equals(message.substring(0, 2))   || "AR".equals(message.substring(0, 2))) {
								flag_walk = false;
								flag_run = false;
								RATempActCount = 0;
								RATempTimeLineActCount = 0;
								
								// AW 3866 E 0236 C 01234 D 01234
//								feedback = !feedback; // true -> false;      //"END"로 이동 
//								flagRA = !flagRA;// true -> false;
								
								
								
								mTodayPreference.setTodayDate(((Integer.parseInt(ty)+2000) + "-" + tmm + "-" + td), Integer.parseInt(ty)+2000, Integer.parseInt(tmm), Integer.parseInt(td));
								
								ULog.d("mTodayPreference.getDataDate():"+mTodayPreference.getDataDate() );
								ULog.v("mTodayPreference.getDataDate():"+mTodayPreference.getDataDate() );
								ULog.i("mTodayPreference.getDataDate():"+mTodayPreference.getDataDate() );
								ULog.e("mTodayPreference.getDataDate():"+mTodayPreference.getDataDate() );
								
								
								
								String araw = message.substring(0, 2);
								
								int eindex = message.indexOf("E");
								int cindex = message.indexOf("C");
								int dindex = message.indexOf("D");

								String walkcnt = message.substring(2, eindex);
								String times = message.substring(eindex + 1, cindex);
								String cal = message.substring(cindex + 1, dindex);
								String m = message.substring(dindex + 1);

								Date today = new Date();
								
								String nowHour = Integer.toString(today.getHours());
								String nowMin = Integer.toString(today.getMinutes());
								
								String hour = times.substring(0,2);
								String min = times.substring(3);
								
								/**
								 * 현재 시간 - 운동시간 = 운동 시작 시각
								 */
								RAactStartHour = Integer.parseInt(nowHour) - Integer.parseInt(hour);
								RATimelineactStartHour = Integer.parseInt(nowHour) - Integer.parseInt(hour);
								
								/**
								 * 현재 시간 - 운동시간 = 운동 시작 분  / b가 음수면 a-1
								 */
								RAactStartMin = Integer.parseInt(nowMin) - Integer.parseInt(min);
								RATimelineactStartMin = Integer.parseInt(nowMin) - Integer.parseInt(min);
								
								
								
								if(RAactStartHour<0){
									
									// 날짜 바뀜
									
//									SQLite 에 update
								}
								
								
								if(Integer.parseInt(walkcnt)>=0 && Integer.parseInt(m) >= 0 && Integer.parseInt(cal) >= 0){
									
									//R-A를 하던중에 날짜가 바뀌면.......... 
									//R-A를 하던중에 날짜가 바뀌면.......... 
									//R-A를 하던중에 날짜가 바뀌면.......... 
									//R-A를 하던중에 날짜가 바뀌면..........  예외처리
									
									
									mActivityAllCount = mActivityAllCount + Integer.parseInt(walkcnt);
									mActivityAllM = mActivityAllM + Integer.parseInt(m);
									mActivityAllCal = mActivityAllCal + Integer.parseInt(cal);
									
									
									RATempActCount = RATempActCount+ Integer.parseInt(walkcnt);
									RATempTimeLineActCount =  RATempTimeLineActCount+ Integer.parseInt(walkcnt);
	
									if("AW".equals(araw)){
										flag_walk = true;
										
										mWalkDistance = mWalkDistance+Integer.parseInt(m);
										mWalkCal = mWalkCal + Integer.parseInt(cal);
									
										mWalkHour  = mWalkHour+ Integer.parseInt(hour);
										mWalkMin  = mWalkMin+ Integer.parseInt(min);
										
										ULog.d(TAG,times + "동안 " + walkcnt + " Walk / " + cal + "calories 소모, " + m + "m");
									}else{
										flag_run = true;
										
										mRunDistance = mRunDistance +Integer.parseInt(m);
										mRunCal = mRunCal + Integer.parseInt(cal);
									
										mRunHour  = mRunHour+ Integer.parseInt(hour);
										mRunMin  = mRunMin+ Integer.parseInt(min);
									
										ULog.d(TAG, times + "동안 " + walkcnt + " Run / " + cal + "calories 소모, " + m + "m");
									}
								
									ULog.d(TAG, " activityStartHour==>" + RAactStartHour);
									ULog.d(TAG, " activityStartMin==>" + RAactStartMin);
									ULog.d(TAG, " Integer.parseInt(h)==>" + Integer.parseInt(nowHour));
									ULog.d(TAG, " Integer.parseInt(mi)==>" + Integer.parseInt(nowMin));
									
									
//									insertTodayData(RAactStartMin, RAactStartHour, walkcnt);   //END로 이동 
	
									ULog.d(TAG, " mTodayActCnt.size()==>" + mTodayActCnt.size());
									ULog.d(TAG, " mTodayActCntTimeHour.size()==>" + mTodayActCntTimeHour.size());
									ULog.d(TAG, " mTodayActCntTimeMin.size()==>" + mTodayActCntTimeMin.size());
								
								} 
								ActFrg.showMessage(message);
								ActFrg.sendMessage("OK");
							} 
						} // R-A flagRA end


						if (flagCWUTS) {
							if (message.contains("OK")) {
//								if(alarms_date.size() > 0) {
//									ActFrg.showMessage(message);
//									ActFrg.sendMessage(alarms_date.get(0));
//									mService.sendMessageToRemote(alarms_date.get(0));
//									alarms_date.remove(0);	
//									alarms_OnOff.remove(0);	
//								}else {
									feedback = !feedback; // true->false
									flagCWUTS = !flagCWUTS; // true->false;
									ActFrg.showMessage(message);
//								}
							}
						}

						if (flagBAT) {
							if ("BAT".equals(message.substring(0, 3))) {
								// BAT00
								flagBAT = !flagBAT; // true-> flase
								feedback = !feedback; // true -> false
								ActFrg.showMessage(message);

								Intent intent = new Intent(getApplicationContext(), MyReceiver1.class);
								message = message.replace("BAT", "");
								message = message.replace("\r\n", "");

								intent.putExtra(Constants.BROADCAST_RECEIVER, message);
								sendBroadcast(intent);

								ActFrg.sendMessage("OK");

							}
						}
						if (flagVersion) {
							if ("v_".equals(message.substring(0, 2))) {
								// v_1.0.0.4
								flagVersion = !flagVersion; // true-> flase
								feedback = !feedback; // true -> false
								ActFrg.showMessage(message);
								
								Intent intent = new Intent(getApplicationContext(), MyReceiver2.class);
								message = message.replace("v_", "");
								message = message.replace("\r\n", "");
								
								intent.putExtra(Constants.BROADCAST_RECEIVER, message);
								sendBroadcast(intent);
								
								ActFrg.sendMessage("OK");
								
							}
						}

					} // feedback true end
					
					try {
						ActFrg.setWalking(Integer.toString(mActivityAllCount), mActivityAllM, mActivityAllCal);
						sleepFrg.setSleep( mDeepSleepMin, mLowSleepMin);
					
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					
					try {
						Bundle bundle = new Bundle();
						//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때
						
						bundle.putInt(Constants.TODAY_ACTIVITY_CNT, mActivityAllCount);
						bundle.putIntegerArrayList(Constants.TODAY_ACT_GRAPH_CNT, mTodayActCnt);
						
						bundle.putIntegerArrayList(Constants.TODAY_ACT_GRAPH_TIME_HOUR, mTodayActCntTimeHour);
						bundle.putIntegerArrayList(Constants.TODAY_ACT_GRAPH_TIME_MIN, mTodayActCntTimeMin);
						
						bundle.putInt(Constants.TODAY_ACT_WALK_DIS, mWalkDistance);
						bundle.putInt(Constants.TODAY_ACT_WALK_M, (mWalkHour*60)+mWalkMin);
						bundle.putInt(Constants.TODAY_ACT_WALK_CAL, mWalkCal);
						
						bundle.putInt(Constants.TODAY_ACT_RUN_DIS, mRunDistance);
						bundle.putInt(Constants.TODAY_ACT_RUN_M, (mRunHour*60)+mRunMin);
						bundle.putInt(Constants.TODAY_ACT_RUN_CAL, mRunCal);
						
						bundle.putBoolean(Constants.TODAY_ACT_GOAL_OK, mActGoalOK);
						
						mTodayPreference.setTodayActData(bundle);
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					
					if(timeLine1Text != null){
						updateTimeLine();
						try {
							Bundle bundle = new Bundle();
							ULog.d("timeLine1Text : " +timeLine1Text);
							ULog.i("timeLine1Text : " +timeLine1Text);
							ULog.v("timeLine1Text : " +timeLine1Text);
							
							ULog.d("timeLine2Text : " +timeLine2Text);
							ULog.i("timeLine2Text : " +timeLine2Text);
							ULog.v("timeLine2Text : " +timeLine2Text);
							
							bundle.putStringArrayList(Constants.TODAY_TIME_LINE_1, timeLine1Text);
							bundle.putStringArrayList(Constants.TODAY_TIME_LINE_2, timeLine2Text);
							
							mTodayPreference.setTimeLine(bundle);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					
					
					
					try {
						Bundle bundle = new Bundle();
						//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때
						
						bundle.putInt(Constants.TODAY_DEEP_SLEEP_M, mDeepSleepMin);
						bundle.putInt(Constants.TODAY_LOW_SLEEP_M, mLowSleepMin);
						bundle.putInt(Constants.TODAY_AWAKE_SLEEP_M, mAwakeTime);
						
						bundle.putString(Constants.TODAY_SLEEP_START, mTodaySleepStartTime);
						bundle.putString(Constants.TODAY_SLEEP_WAKEUP, mTodaySleepEndTime);
						
						bundle.putIntegerArrayList(Constants.TODAY_SLEEP_GRAPH_HEIGHT, mTodaySleepCnt);
						bundle.putIntegerArrayList(Constants.TODAY_SLEEP_GRAPH_CNT, mTodaySleepCntTimeMin);
						
						mTodayPreference.setTodaySleepData(bundle);
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	}
	
	private void updateTimeLine() {
		timeline.removeAll();
		
		for(int a = 0; a <timeLine1Text.size(); a++){
			
			if(!"".equals(timeLine1Text.get(a))){
				timeline.add(timeLine1Text.get(a), timeLine2Text.get(a));
			}
		}
		
		timeline.notifyDataSetChanged();
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
	 * band 연결을 안한동안 쌓인 data server에 up
	 */
	private void setUpdateServer() {
		
		Cursor c = dbManager.selectTempDailyActivity();
		while (c.moveToNext()) {
			Bundle daily_data = new Bundle();
			daily_data.putInt("uid", uid);
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
					dbManager.removeActDaily(c.getString(0));
					break;
				}
			}
		}
	}
	
	
	private void setActWeekMonth(){
		JSONObject weekly_result, monthly_result;
		
		try {
			weekly_result = new JSONObject(RecvSendPacket.getWeeklyData(uid));
			JSONArray weekly_d = new JSONArray(weekly_result.getString("data"));
			ULog.d("XXXXXXXXXXXXXXX", "weekly_d.length() ======>" + weekly_d.length());
			
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
					
					ULog.v("Main Search Weekly", "UID," + uid);
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
			monthly_result = new JSONObject(RecvSendPacket.getMontlyData(uid));
			JSONArray monthly_d = new JSONArray(monthly_result.getString("data"));
			
			ULog.d("XXXXXXXXXXXXXXX", "monthly_d.length() ======>" + monthly_d.length());

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
					
					ULog.v("Main Search Weekly", "UID," + uid);
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
	
	
	/**
	 * 로딩화면 끝, actionbar show
	 */
	private void setLoadingEnd(){
		showLoading=false;
		ULog.e(TAG, "showLoading3:"+showLoading);
		Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame2);
		
		fragmentManager.remove(mFragment);
		findViewById(R.id.frame2).setVisibility(View.GONE);
		mMainFrame.setVisibility(View.VISIBLE);
		getActionBar().show();
	}
	
	/**
	 * today detail graph data in
	 * @param activityStartMin
	 * @param activityStartHour
	 * @param walkcnt
	 */
	public void insertTodayData(int activityStartMin, int activityStartHour, String walkcnt){
		int compareMin = 0, compareHour=0;
		
		if(mTodayActCnt.size()>0){
			compareHour = mTodayActCntTimeHour.get(mTodayActCntTimeHour.size()-1);
			compareMin = mTodayActCntTimeMin.get(mTodayActCntTimeMin.size()-1);
			
			ULog.d(TAG, " compareMin==>" + compareMin);
			ULog.d(TAG, " compareHour==>" + compareHour);
			
			
			int activityStartHour2 = 0;
			int activityStartMin2 = 0;
			
			if(activityStartMin<0){
				activityStartHour2 = activityStartHour-1;
				activityStartMin2 = (60+activityStartMin)/10;
			}else{
				activityStartHour2 = activityStartHour;
				activityStartMin2 = activityStartMin/10;
			}
			
			ULog.d(TAG, " activityStartHour2==>" + activityStartHour2);
			ULog.d(TAG, " activityStartMin2==>" + activityStartMin2);
			
			//아래 if 전에 날짜 먼저 비교 날짜가 같은지!
			
			
			//운동 도중 바꾸ㅣㅁ   실시간 이후 수정
			if(activityStartHour2 < 0){
				//  전날 통계 데이터로 들어감
				
				
				
				
				// ~ 00:00  까지 통계로 들어가고
				
				return;
			}
			
			
			if(    compareMin == activityStartMin2     &&    compareHour == activityStartHour2    ){   
				
				ULog.v(" 시간 , 분 전부 같을 경우 이전 array에 in");
				mTodayActCnt.set(mTodayActCnt.size()-1, mTodayActCnt.get(mTodayActCnt.size()-1) + Integer.parseInt(walkcnt));
				
			}else if( activityStartHour2 == (compareMin-1)&& activityStartMin2 == 0 ){
				ULog.v(" 59분에서 정각으로 !! ");
				mTodayActCnt.add(Integer.parseInt(walkcnt));
				mTodayActCntTimeHour.add(activityStartHour2);
				mTodayActCntTimeMin.add(0);
			}else{
				
				
				if(      (  activityStartMin2-1 )   ==  compareMin && compareHour == activityStartHour2     ){
					
					ULog.v(" 시간 같고 분만 1 차이남!");
					mTodayActCnt.add(Integer.parseInt(walkcnt));
					mTodayActCntTimeHour.add(activityStartHour2);
					mTodayActCntTimeMin.add(activityStartMin2);
				}else {
					
					int emptyMin = (activityStartMin2*10) - (compareMin*10);
					int emptyHour = activityStartHour2 - compareHour;
					
					if(emptyMin < 0 ){
						emptyHour = emptyHour - 1; 
						emptyMin = emptyMin + 60;
					}
					
					emptyHour = emptyHour * 60;
					int j = 0;
					for(int i=0; i<(emptyMin+emptyHour)/10;i++){
						
						if(i == ((emptyMin+emptyHour)/10-1) ){
							mTodayActCnt.add(Integer.parseInt(walkcnt));
						}else{
							mTodayActCnt.add(0);
						}
						
						
						
						// 00 10 20 30 40 50 60 70 80 90 100 110 120 130 140
						// x  x  x  x   x x  00  x  x  x  x  x   00
						ULog.v(TAG, "1375번줄 hour" + emptyHour + j); 
						
						
						mTodayActCntTimeHour.add(compareHour + j);
						mTodayActCntTimeMin.add(i-(j*6));
						
						if( (i+1)%6 == 0 && i != 0){    
							j++;;
						}
						
					}
					
					ULog.v(" 시간, 분 다 차이나서 곱하고 빼고...");
				}
				
			}
		}else{
			ULog.v(" 오늘의 첫번째 데이터.....는  hour->0:minutes->0 이 아니면 앞 배열 전부 cnt 0으로 set");
			
			

			int k = 0 ;
			for(int i= 0; i < activityStartHour*6 ; i++){
				if(i == (activityStartHour*6 -1) ){
					mTodayActCnt.add(Integer.parseInt(walkcnt));
				}else{
					mTodayActCnt.add(0);
				}
				
				
				mTodayActCntTimeHour.add(0 + k);
				mTodayActCntTimeMin.add(i-(k*6));
				ULog.v(TAG, "k=======>" + k);
				if( (i+1)%6 == 0 && i != 0){     
					k++;
				}
				
			}
			
			for(int i= 0; i < activityStartMin/10 ; i++){
				mTodayActCnt.add(0);
				mTodayActCntTimeHour.add(activityStartHour);
				mTodayActCntTimeMin.add(i);
			}
			ULog.v(TAG, "1556번줄 hour:" + mTodayActCntTimeHour);
			ULog.v(TAG, "1557번줄 hour:" + mTodayActCntTimeMin);

			
			
			mTodayActCnt.add(Integer.parseInt(walkcnt));
			mTodayActCntTimeHour.add(activityStartHour);
			mTodayActCntTimeMin.add(activityStartMin/10);
		}
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

		@Override
		public void onReceive(Context context, Intent intent) {
			TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			Bundle bundle = intent.getExtras();
			int s = bundle.getInt(Constants.BROADCAST_RECEIVER);
			mConnectionInfo = ConnectionInfo.getInstance(context);
			mContext = context;
			mPreference = Preferences.getInstance(mContext);
			

			switch (s) {
			case Constants.BROADCAST_FIND:
				// Toast.makeText(context, String.format("%s瑜� �닔�떊�뻽�뒿�땲�떎.", s),
				// Toast.LENGTH_SHORT).show();
				mService.sendMessageToRemote("VIB");
				break;

			case Constants.BROADCAST_BAT:
				// �떎�떆 broadCastReceiver Setting
				feedback = true;
				flagBAT = !flagBAT; // false -> true
				mService.sendMessageToRemote("RBAT");
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
				
				mService.sendMessageToRemote("CWUTS");
				break;
				
			case Constants.BROADCAST_KAKAOTALK:
				try {
					if(mPreference != null && mPreference.getKaKaoNoti()){
						if(mConnectionInfo.mViblation != null) {
							return;
						}
						
						bluetoothVibratonConnect(mPreference.getKaKaoLedColor());	
					}
				} catch (NullPointerException e) {
					// TODO: handle exception
					Log.d("MainActivity", "kakao : "+e);
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
									ULog.i("Phone", "RINGING=====");
								} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
									ULog.i("Phone", "OFFHOOK");
								}
							}
						} catch (NullPointerException e) {
							// TODO: handle exception
							Log.d("MainActivity", "call : "+e);
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
							
							bluetoothVibratonConnect(mPreference.getSMSLedColor());
						}
					} catch (NullPointerException e) {
						// TODO: handle exception
						Log.d("MainActivity", "sms : "+e);
					}
				}	
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		private void bluetoothVibratonConnect(int led) {
			if(mService == null || mService.mBleManager == null || mService.mBleManager.getState() != 16) {
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
			}else {
				mService.sendMessageToRemote("VIB" + led);
			}
		}
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

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
	
	
	
	
	public void dbautoupdate() {
		Log.e("###", "serverAutoUpdate");

		Intent intent = new Intent(this, serverAutoUpdate.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
		
		
		try {
			// 내일 아침 8시 10분에 처음 시작해서, 24시간 마다 실행되게
			Date tomorrow;
			Calendar today = Calendar.getInstance ( );
			today.add ( Calendar.DATE, 1 );

			Date tomorrorw_date = today.getTime();
			
			String y =  Integer.toString(tomorrorw_date.getYear()+1900);
			String m =  Integer.toString(tomorrorw_date.getMonth()+1);
			String d =  Integer.toString(tomorrorw_date.getDate());
			
			try {
				tomorrow = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(y+"-"+(m.length()==1? "0"+m: m)+"-"+(d.length()==1? "0"+d: d)+" 20:00:00");
			
				AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
				am.setInexactRepeating(AlarmManager.RTC, tomorrow.getTime(), 24 * 60 * 60 * 1000, sender);
			
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
