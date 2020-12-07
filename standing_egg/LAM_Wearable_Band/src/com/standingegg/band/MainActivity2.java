//package com.standingegg.band;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import org.acra.ReportField;
//import org.acra.ReportingInteractionMode;
//import org.acra.annotation.ReportsCrashes;
//
//import com.standingegg.band.activity.ActivityDetailPartActivity;
//import com.standingegg.band.activity.ActivityFragment;
//import com.standingegg.band.activity.TimeLineAdapter;
//import com.standingegg.band.bluetooth.ConnectionInfo;
//import com.standingegg.band.login.LoginDataLoadingFragment;
//import com.standingegg.band.login.UserProfileActivity;
//import com.standingegg.band.service.BTCTemplateService;
//import com.standingegg.band.setting.AlarmAcitivity;
//import com.standingegg.band.setting.SettingActivity;
//import com.standingegg.band.setting.SettingActivity.MyReceiver1;
//import com.standingegg.band.setting.SupportActivity;
//import com.standingegg.band.sleeping.SleepDetailPartActivity;
//import com.standingegg.band.sleeping.SleepFragment;
//import com.standingegg.band.util.AppSettings;
//import com.standingegg.band.util.Constants;
//import com.standingegg.band.util.Preferences;
//import com.standingegg.band.util.RecycleUtils;
//import com.standingegg.band.util.TodayPreferences;
//import com.standingegg.band.util.ULog;
//
//import android.app.ActionBar;
//import android.app.ActionBar.Tab;
//import android.app.Activity;
//import android.app.ActivityManager;
//import android.app.ActivityManager.RunningServiceInfo;
//import android.app.Fragment;
//import android.app.FragmentTransaction;
//import android.bluetooth.BluetoothAdapter;
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.content.pm.PackageManager;
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.support.v4.app.ActionBarDrawerToggle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.DrawerLayout;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.Gallery.LayoutParams;
//
//@ReportsCrashes(formUri = "", // will not be used
//mailTo = "jess.choi@standing-egg.co.kr", customReportContent = { ReportField.APP_VERSION_CODE,
//		ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA,
//		ReportField.STACK_TRACE,
//		ReportField.LOGCAT }, mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)
//public class MainActivity2 extends FragmentActivity implements ActionBar.TabListener, IFragmentListener {
//
//	private static final String TAG = "MainActivity";
//
//	private Context mContext;
//	private static BTCTemplateService mService;
//	private ActivityHandler mActivityHandler;
//	private Preferences mPreference = null;
//	private TodayPreferences mTodayPreference = null;
//
//	String address;
//	private Timer mRefreshTimer = null;
//
//	// UI
//	private FragmentManager mFragmentManager;
//	private static FragmentAdapter mSectionsPagerAdapter;
//	private ViewPager mViewPager;
//	private FrameLayout mMainFrame;
//	private DrawerLayout mDrawerLayout;
//	private ActionBarDrawerToggle mDrawerToggle;
//	private ListView mDrawerList;
//	private ImageView mPagerIcon;
//	
//	ListView mListView;
//	private TimeLineAdapter timeline;
//
//	// test
//	private ImageView mImageBT = null;
//	private TextView mTextStatus = null;
//
//	// ble 통신
//	boolean flagSendDT = false;
//	boolean flagRUSER = false;
//	boolean flagUR = false;
//	boolean flagRA = false;
//	boolean flagRS = false;
//	// bat
//	static boolean flagBAT = false;
//	// 알람
//	static boolean flagCWUTS = false;
//
//	static boolean feedback = false;
//// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
//	boolean initialized = false; // Preferences로 빼야함
//									// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//// ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
//	int ligthSleepCnt = 0;
//	int ligthSleepCntRt = 0;
//
//	static int mActivityAllCount = 0;
//	static Float mActivityAllKm = 0.00f;
//	static Float mActivityAllKcal = 0.00f;
//
//	// preferences 값
//	ArrayList<Integer> mTodayActCnt;  	 //graph 표시  - ActivityTodayDetail
//	ArrayList<Integer> mTodayActCntTimeHour; //graph 표시	//시간
//	ArrayList<Integer> mTodayActCntTimeMin; //graph 표시   //분 앞자리
//
//	ArrayList<Integer> mTodaySleepCnt;  	 //graph 표시  - ActivityTodayDetail
//	ArrayList<Integer> mTodaySleepCntTimeHour; //graph 표시	//시간
//	ArrayList<Integer> mTodaySleepCntTimeMin; //graph 표시   //분 앞자리
//	
//	
//	Float mWalkDistance = 0.00f; //ActivityTodayDetail - 걸은 거리
//	int mWalkHour = 0;	    //ActivityTodayDetail - 걸은 시간
//	int mWalkMin = 0;	    //ActivityTodayDetail - 걸은 분..? 왜나눴지...?
//	Float mWalkKcal = 0.00f; //ActivityTodayDetail - 걸음으로 소모한 cal량
//	
//	Float mRunDistance = 0.00f;  //ActivityTodayDetail - 뛴 거리
//	int mRunHour = 0;	   		//ActivityTodayDetail - 뛴 시간
//	int mRunMin = 0;	   		//ActivityTodayDetail - 뛴 분
//	Float mRunKcal = 0.00f; 		//ActivityTodayDetail - 뛰어선 소모한 cal량
//
//	
//	//SleepTodayDetail 
//	int mDeepSleepHour = 0; // 숙면 시간
//	int mDeepSleepMin = 0;	// 숙면 분
//	
//	int mLowSleepHour = 0;	// 얕은 잠 시간
//	int mLowSleepMin = 0;	// 얕은 잠 분
//	
//	String mSleepTime = null; // 잠든 시간
//	String mWakeUpTime = null;// 깬 시간
//	
//	
//	
//	String mToday;
//	
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		Intent intent = getIntent();
//		address = intent.getStringExtra(Constants.EXTRA_DEVICE_ADDRESS);
//
//		// ----- System, Context
//		mContext = this; // .getApplicationContext();
//		mActivityHandler = new ActivityHandler();
//
//		mPreference = Preferences.getInstance(mContext);
//		mTodayPreference = TodayPreferences.getInstance(mContext);
//
//		// backgound service
//		AppSettings.initializeAppSettings(mContext);
//
//		setContentView(R.layout.main);
//
//		// loading fr on
//		loadingFragment();
//		// actionbar Custom
//
//		
//		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
//		getActionBar().setCustomView(R.layout.action_bar);
//		getActionBar().setDisplayShowHomeEnabled(true);
//		getActionBar().setHomeAsUpIndicator(R.drawable.element_menu);
//		
//		
//		mFragmentManager = getSupportFragmentManager();
//		mSectionsPagerAdapter = new FragmentAdapter(mFragmentManager, mContext, this, mActivityHandler);
//
//		mViewPager = (ViewPager) findViewById(R.id.pager);
//		mViewPager.setAdapter(mSectionsPagerAdapter);
//		
//		mPagerIcon = (ImageView) findViewById(R.id.main_pager);
//
//		mMainFrame = (FrameLayout) findViewById(R.id.frame);
//
//		
//		
//		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[6];
//		 
//		drawerItem[0] = new ObjectDrawerItem(R.drawable.element_menu_home, "홈");
//		drawerItem[1] = new ObjectDrawerItem(R.drawable.element_menu_profile, "프로필");
//		drawerItem[2] = new ObjectDrawerItem(R.drawable.element_menu_setting, "설정");
//		drawerItem[3] = new ObjectDrawerItem(R.drawable.element_menu_alarm, "알람");
//		drawerItem[4] = new ObjectDrawerItem(R.drawable.element_menu_support, "지원");
//		drawerItem[5] = new ObjectDrawerItem(R.drawable.element_menu_logout, "로그아웃");
//		
//		
//		mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
//		
//		mDrawerToggle = new ActionBarDrawerToggle(
//		        this,
//		        mDrawerLayout,
//		        R.drawable.element_menu, 
//		        R.string.menu_connect, 
//		        R.string.menu_connect
//		        ) {
//		    
//		    /** Called when a drawer has settled in a completely closed state. */
//		    public void onDrawerClosed(View view) {
//		        super.onDrawerClosed(view);
////		        getActionBar().setTitle(mTitle);
//		    }
//		 
//		    /** Called when a drawer has settled in a completely open state. */
//		    public void onDrawerOpened(View drawerView) {
//		        super.onDrawerOpened(drawerView);
////		        getActionBar().setTitle(mDrawerTitle);
//		    }
//		};
//		 
//		mDrawerLayout.setDrawerListener(mDrawerToggle);
//		
//		
//		
//		DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.navigator_list, drawerItem);
//		mDrawerLayout.setDrawerListener(mDrawerToggle);
//		// getActionBar().setDisplayHomeAsUpEnabled(true);
//		getActionBar().setHomeButtonEnabled(true);
//		// Fragment view Pager
//		mDrawerList = (ListView) findViewById(R.id.navi_list);
//		mDrawerList.setAdapter(adapter);
//		
//		// mDrawerList.setSty
//		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//		
//		mViewPager.setOnPageChangeListener( new
//		ViewPager.SimpleOnPageChangeListener() {
//			@Override 
//			public void onPageSelected(int position) { // When swiping between pages, select the // corresponding tab.
//				switch (position) {
//				case 0:
//					mPagerIcon.setImageResource(R.drawable.element_swipe_icon_1);
//					break;
//
//				case 1:
//					SleepFragment frg = (SleepFragment) mSectionsPagerAdapter .getItem(FragmentAdapter.FRAGMENT_POS_SLEEP);
//					frg.updateUi();
//					mPagerIcon.setImageResource(R.drawable.element_swipe_icon_2);
//					break;
//				}
//			} 
//		});
//			
//
//		// Setup views
//		mImageBT = (ImageView) findViewById(R.id.status_title);
//		mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
//		mTextStatus = (TextView) findViewById(R.id.status_text);
//		mTextStatus.setText(getResources().getString(R.string.bt_state_init));
//
//		mListView = (ListView) findViewById(R.id.listView);
//
//		//
//		timeline = new TimeLineAdapter(getApplicationContext(), R.layout.day_flow_draw);
//
//		mListView.setAdapter(timeline);
//
//		/**
//		 * 
//		 * test 용!!
//		 * 
//		 * 
//		 */
//		
//		Date today = new Date();
//		
//		
//		String ty = Integer.toString(today.getYear() + 1900 - 2000);
//		String tmonth = Integer.toString(today.getMonth() + 1);
//		String tmm = tmonth.length() == 1 ? "0" + tmonth : tmonth;
//		String tdate = Integer.toString(today.getDate());
//		String td = tdate.length() == 1 ? "0" + tdate : tdate;
//		
//		
//		mToday=ty+""+tmm+"" +td;
//		
//		mTodayActCnt  = new ArrayList<Integer>();
//		mTodayActCntTimeHour = new ArrayList<Integer>();
//		mTodayActCntTimeMin = new ArrayList<Integer>();
//		
//		
//		ConnectionInfo mConnectionInfo = null;
//		mConnectionInfo = ConnectionInfo.getInstance(mContext);
//		mConnectionInfo.setDeviceAddress(address);
//		
//		
//		mTodayPreference.resetSleepPreferences();
//
//		// Do data initialization after service started and binded
//		doStartService();
//	}
//	
//	
//	public class ObjectDrawerItem {
//	    public int icon;
//	    public String name;
//	 
//	    // Constructor.
//	    public ObjectDrawerItem(int icon, String name) {
//	        this.icon = icon;
//	        this.name = name;
//	    }
//	}
//	
//	public class DrawerItemCustomAdapter extends ArrayAdapter<ObjectDrawerItem> {
//		 
//	    Context mContext;
//	    int layoutResourceId;
//	    ObjectDrawerItem data[] = null;
//	 
//	    public DrawerItemCustomAdapter(Context mContext, int layoutResourceId, ObjectDrawerItem[] data) {
//	 
//	        super(mContext, layoutResourceId, data);
//	        this.layoutResourceId = layoutResourceId;
//	        this.mContext = mContext;
//	        this.data = data;
//	    }
//	 
//	    @Override
//	    public View getView(int position, View convertView, ViewGroup parent) {
//	        View listItem = convertView;
//	 
//	        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
//	        listItem = inflater.inflate(layoutResourceId, parent, false);
//	      
////	        if(position == 5){
////	        	RelativeLayout.LayoutParams d =  (RelativeLayout.LayoutParams) listItem.getLayoutParams();
////	        	d.pa
////	        	
////	        	listItem.setLayoutParams(d);
////	        }
//	 
//	        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.imageViewIcon);
//	        
//	        TextView textViewName = (TextView) listItem.findViewById(R.id.menu_txt);
//	        
//	        ObjectDrawerItem folder = data[position];
//	        imageViewIcon.setImageResource(folder.icon);
//	        textViewName.setText(folder.name);
//	        if(position == 5){
//	        	RelativeLayout.LayoutParams parms = (RelativeLayout.LayoutParams)imageViewIcon.getLayoutParams();
//	        	parms.height = 1194;
//	        	imageViewIcon.setLayoutParams(parms);
//	        	textViewName.setHeight(1194);
//	        	imageViewIcon.setPaddingRelative(0, 894, 0, 0);
//	        	textViewName.setPaddingRelative(0, 894, 0, 0);
//	        }
//	        
//	        return listItem;
//	    }
//	}
//	
//	
//	
//
//	public void setTimeLine() {
//		DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
//		int height = dm.heightPixels;
//
//		ULog.v(TAG , "height === > "  + height);
//		
////		FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, height/2-202);
////		params1.gravity = Gravity.BOTTOM;
////		mListView.setLayoutParams(params1);
////
////		// android:layout_marginBottom="-10dp"
////
////		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
////		params.setMargins(0, 0, 0, height/2-202+70);
////
////		mViewPager.setLayoutParams(params);
//
//		timeline.removeAll();
//		
//		// mListView.setVisibility(View.VISIBLE);
//		timeline.add("오늘의 목표를 달성했습니다.", "축하합니다!");
//		timeline.add("19:11 ~ 19:54 | 총 활동 20 분 | 약 1km", "55Kcal 소모");
//		timeline.add("20:19 ~ 20:32 | 805 보 | 약 0.5km", "3Kcal 소모");
//		timeline.add("20:47 ~ 20:56 | 총 활동 6 분 | 약 0.1km", "10Kcal 소모");
//
//		timeline.notifyDataSetChanged();
//	}
//
//	public void initctivityInfo() {
//		// prefernces 엣 ㅓ값을 가져올것인지..... sqlite에서 값을 가져올 것인지.....8ㅅ8...
//
//		mPreference.getUserId();
//
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.detail, menu);
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onMenuItemSelected(int featureId, MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.action:
//
//			if (mViewPager.getCurrentItem() == 0) {
//				Intent i = new Intent(mContext, ActivityDetailPartActivity.class);
//				startActivityForResult(i, Constants.ACTIVITY_SLEEP_FLAG);
//			} else {
//				Intent i = new Intent(mContext, SleepDetailPartActivity.class);
//				startActivityForResult(i, Constants.ACTIVITY_SLEEP_FLAG);
//			}
//
//			break;
//
//		default:
//
//		}
//		return super.onMenuItemSelected(featureId, item);
//	}
//
//	FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
//
//	public void loadingFragment() {
//		//menu bar 슬라이드 막기 
//		
//		
//		
//		
//		getActionBar().hide();
//		Fragment fragment = new LoginDataLoadingFragment();
//		Bundle args = new Bundle();
//		// args.putInt(LoginDataLoadingFragment.ARG_PLANET_NUMBER, position);
//		fragment.setArguments(args);
//
//		// Insert the fragment by replacing any existing fragment
//
//		fragmentManager.replace(R.id.frame2, fragment).commit();
//	}
//
//	int cnt = 0;
//
//	@Override
//	public synchronized void onStart() {
//		super.onStart();
//		/*
//		 * TimerTask timerTask = new TimerTask() {
//		 * 
//		 * @Override public void run() { if(cnt !=0 ){ ActivityFragment frg2 =
//		 * (ActivityFragment)
//		 * mSectionsPagerAdapter.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//		 * frg2.showsendMessage("PKSTVCALL"); } cnt++;
//		 * 
//		 * } };
//		 * 
//		 * Timer timer = new Timer(); timer.schedule(timerTask, 1000, 1000);
//		 */
//
//	}
//
//	@Override
//	public synchronized void onPause() {
//		super.onPause();
//	}
//
//	@Override
//	public void onStop() {
//		// Stop the timer
//		if (mRefreshTimer != null) {
//			mRefreshTimer.cancel();
//			mRefreshTimer = null;
//		}
//		super.onStop();
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		finalizeActivity();
//	}
//
//	@Override
//	public void onLowMemory() {
//		super.onLowMemory();
//		// onDestroy is not always called when applications are finished by
//		// Android system.
//		finalizeActivity();
//	}
//
//	@Override
//	public void onBackPressed() {
//		moveTaskToBack(true);
//		finish();
//		android.os.Process.killProcess(android.os.Process.myPid());
//		// disconnect
//	}
//
//	boolean connection = false;
//
//	@Override
//	protected void onPostCreate(Bundle savedInstanceState) {
//		super.onPostCreate(savedInstanceState);
//		mDrawerToggle.syncState();
//	}
//
//	@Override
//	public void onConfigurationChanged(Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//		mDrawerToggle.onConfigurationChanged(newConfig);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (mDrawerToggle.onOptionsItemSelected(item)) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}
//
//	private class DrawerItemClickListener implements ListView.OnItemClickListener {
//		@Override
//		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
//			switch (position) {
//			case 0:
//
//				break;
//			case 1:
//				Intent profileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
//				startActivity(profileIntent);
//
//				break;
//			case 2:
//				Intent setting = new Intent(getApplicationContext(), SettingActivity.class);
//				startActivity(setting);
//
//				break;
//			case 3:
//				Intent alarm = new Intent(getApplicationContext(), AlarmAcitivity.class);
//				startActivity(alarm);
//
//				break;
//			case 4:
//				Intent support = new Intent(getApplicationContext(), SupportActivity.class);
//				startActivity(support);
//
//				break;
//			}
//			mDrawerLayout.closeDrawer(mDrawerList); // 추가됨
//		}
//	};
//
//	public void findBand() {
//
//		if (mService != null) {
//			mService.sendMessageToRemote("VIB");
//		}
//	}
//
//	@Override
//	public void onTabSelected(Tab tab, FragmentTransaction ft) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//		// TODO Auto-generated method stubg
//
//	}
//
//	/**
//	 * Service connection
//	 */
//	private ServiceConnection mServiceConn = new ServiceConnection() {
//
//		public void onServiceConnected(ComponentName className, IBinder binder) {
//			Log.d(TAG, "Activity - Service connected");
//
//			mService = ((BTCTemplateService.ServiceBinder) binder).getService();
//
//			// Activity couldn't work with mService until connections are made
//			// So initialize parameters and settings here. Do not initialize
//			// while running onCreate()
//			initialize();
//		}
//
//		public void onServiceDisconnected(ComponentName className) {
//			mService = null;
//		}
//	};
//
//	/**
//	 * Start service if it's not running
//	 */
//	private void doStartService() {
//		Log.d(TAG, "# Activity - doStartService()");
//		startService(new Intent(this, BTCTemplateService.class));
//		bindService(new Intent(this, BTCTemplateService.class), mServiceConn, Context.BIND_AUTO_CREATE);
//
//		ULog.d("address =======>  " + address);
//		// if(!connection)
//		// connection = !connection;
//		/*
//		 * else{ connection = false; mService.disConnect(); }
//		 */
//	}
//
//	/**
//	 * Stop the service
//	 */
//	private void doStopService() {
//		Log.d(TAG, "# Activity - doStopService()");
//		mService.finalizeService();
//		stopService(new Intent(this, BTCTemplateService.class));
//
//		// 제거
//		unbindService(mServiceConn);
//	}
//
//	/**
//	 * Initialization / Finalization
//	 */
//	private void initialize() {
//		ULog.d(TAG, "# Activity - initialize()");
//
//		// Use this check to determine whether BLE is supported on the device.
//		// Then
//		// you can selectively disable BLE-related features.
//		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//			Toast.makeText(this, R.string.bt_ble_not_supported, Toast.LENGTH_SHORT).show();
//			finish();
//		}
//
//		mService.setupService(mActivityHandler);
//
//		// If BT is not on, request that it be enabled.
//		// RetroWatchService.setupBT() will then be called during
//		// onActivityResult
//		if (!mService.isBluetoothEnabled()) {
//			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//			startActivityForResult(enableIntent, Constants.REQUEST_ENABLE_BT);
//		}
//
//		// Load activity reports and display
//		if (mRefreshTimer != null) {
//			mRefreshTimer.cancel();
//		}
//
//		if (address != null && mService != null) {
//			mService.connectDevice(address); // BTCTemplate
//		}
//
//		// Use below timer if you want scheduled job
//		// mRefreshTimer = new Timer();
//		// mRefreshTimer.schedule(new RefreshTimerTask(), 3000);
//
//		// Timer timerMTimer = new Timer();
//		//
//		// timerMTimer.schedule(new TimerTask() {
//		// @Override
//		// public void run() {
//		// ActivityFragment frg2 = (ActivityFragment) mSectionsPagerAdapter
//		// .getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//		// frg2.showsendMessage("PKSTVCALL");
//		// }
//		// }, 1000, 1000);
//
//	}
//
//	private class RefreshTimerTask extends TimerTask {
//		public RefreshTimerTask() {
//		}
//
//		public void run() {
//			mActivityHandler.post(new Runnable() {
//				public void run() {
//					// TODO:
//					if (flag & flag2) {
//						// LoginDataLoading.finished();
//					}
//				}
//			});
//		}
//	}
//
//	boolean flag2 = false;
//
//	@Override
//	protected void onActivityResult(int request, int result, Intent Data) {
//		ULog.d(TAG, "onActivityResult " + request);
//
//		switch (request) {
//		case Constants.REQUEST_ENABLE_BT:
//			// When the request to enable Bluetooth returns
//			if (result == Activity.RESULT_OK) {
//				// Bluetooth is now enabled, so set up a BT session
//				mService.setupBLE();
//			} else {
//				// User did not enable Bluetooth or an error occured
//				ULog.e(TAG, "BT is not enabled");
//				Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
//			}
//			break;
//
//		case Constants.REQUEST_LOGIN:
//			if (result == Activity.RESULT_OK) {
//				flag2 = true;
//
//			} else {
//			}
//
//		case Constants.ACTIVITY_SLEEP_FLAG:
//			if (result == Activity.RESULT_OK) {
//				// 수면 back인지 활동 back인지 구분
//			}
//
//			break;
//		} // End of switch(requestCode)
//	}
//
//	boolean flag = false;
//
//	// int i = 1;
//	public class ActivityHandler extends Handler {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case Constants.MESSAGE_BT_STATE_INITIALIZED:
//				mTextStatus.setText(getResources().getString(R.string.bt_title) + ": "
//						+ getResources().getString(R.string.bt_state_init));
//				mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
//				// 연결 재시도 ?
//				// if(address != null && mService != null)
//				// mService.connectDevice(address);
//				break;
//			case Constants.MESSAGE_BT_STATE_LISTENING:
//				mTextStatus.setText(getResources().getString(R.string.bt_title) + ": "
//						+ getResources().getString(R.string.bt_state_wait));
//				mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_invisible));
//
//				break;
//			case Constants.MESSAGE_BT_STATE_CONNECTING:
//				mTextStatus.setText(getResources().getString(R.string.bt_title) + ": "
//						+ getResources().getString(R.string.bt_state_connect));
//				mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_away));
//				break;
//			case Constants.MESSAGE_BT_STATE_CONNECTED:
//				if (mService != null) {
//					String deviceName = mService.getDeviceName();
//					if (deviceName != null) {
//						mTextStatus.setText(getResources().getString(R.string.bt_title) + ": "
//								+ getResources().getString(R.string.bt_state_connected) + " " + deviceName);
//						mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_online));
//					} else {
//						mTextStatus.setText(getResources().getString(R.string.bt_title) + ": "
//								+ getResources().getString(R.string.bt_state_connected) + " no name");
//						mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_online));
//					}
//
//					flag = true;
//					// loadingFragment()
//					flagSendDT = false;
//					flagRUSER = false;
//					flagUR = false;
//					flagRA = false;
//					flagRS = false;
//					flagBAT = false;
//					flagCWUTS = false;
//					feedback = false;
//				}
//				break;
//			case Constants.MESSAGE_BT_STATE_ERROR:
//				mTextStatus.setText(getResources().getString(R.string.bt_state_error));
//				mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_busy));
//				break;
//
//			// BT Command status
//			case Constants.MESSAGE_CMD_ERROR_NOT_CONNECTED:
//				mTextStatus.setText(getResources().getString(R.string.bt_cmd_sending_error));
//				mImageBT.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_busy));
//		
//				
//				
//				//send Message 실패시!!!
//				
//				
//				
//				
//				
//				
//				
//				break;
//
//			///////////////////////////////////////////////
//			// When there's incoming packets on bluetooth
//			// do the UI works like below
//			///////////////////////////////////////////////
//
//			case Constants.MESSAGE_WRITE_CHAT_DATA:
//				String message2 = (String) msg.obj;
//				ActivityFragment frg2 = (ActivityFragment) mSectionsPagerAdapter
//						.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//				frg2.showsendMessage(message2);
//				break;
//
//			case Constants.MESSAGE_READ_CHAT_DATA:
//
//				ULog.d("Constants.MESSAGE_READ_CHAT_DATA");
//				ULog.d("Constants.MESSAGE_READ_CHAT_DATA");
//
//				if (msg.obj != null) {
//					// ActivityFragment frg = (ActivityFragment)
//					// .getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//					String message = (String) msg.obj;
//					ULog.i(TAG, "message:" + message);
//					ULog.i(TAG, "feedback:" + feedback);
//
//					ULog.i(TAG, "flagSendDT:" + flagSendDT);
//					ULog.i(TAG, "flagRUSER:" + flagRUSER);
//					ULog.i(TAG, "flagUR:" + flagUR);
//					ULog.i(TAG, "flagRA:" + flagRA);
//					ULog.i(TAG, "flagRS:" + flagRS);
//					ULog.i(TAG, "flagBAT:" + flagBAT);
//					ULog.i(TAG, "flagCWUTS:" + flagCWUTS);
//
//					// message 끝 두자리가 \r\n이 아닐 경우!!!!!!!!!! 에는 패킷을 이어
//					// 받아야함!!!!!!!!!
//
//					// frg.showMessage(message);
//
//					// App이 처음 실행되거나 혹은 휴대폰에서 App을 재실행해서 Wearable Band와
//					// Connection을 할 때
//
//					/*
//					 * if (message.contains("RYMDT")) { getActionBar().show();
//					 * Fragment mFragment =
//					 * getFragmentManager().findFragmentById(R.id.frame2);
//					 * fragmentManager.remove(mFragment);
//					 * mMainFrame.setVisibility(View.VISIBLE); }
//					 * 
//					 * 
//					 * if("BAT".equals(message.substring(0, 3))){
//					 * SettingActivity.setBat(message.replace("BAT","")); }
//					 * 
//					 * 
//					 * // flag 하나 더 만들어서 thread 응답 지날 때까지 대기하는 .... thread.....
//					 * 
//					 * if ("W".equals(message.substring(0, 1))) { try { int
//					 * sindex = message.indexOf("S"); int eindex =
//					 * message.indexOf("E"); int cindex = message.indexOf("C");
//					 * int dindex = message.indexOf("D");
//					 * 
//					 * String walkCnt = message.substring(1, sindex); String
//					 * hour = message.substring(sindex + 1, eindex); String time
//					 * = message.substring(eindex + 1, cindex); String cal =
//					 * message.substring(cindex + 1, dindex); String m =
//					 * message.substring(dindex + 1);
//					 * 
//					 * Toast.makeText(mContext, walkCnt + "걸음, " + hour +
//					 * "에 시작해서 " + time + "동안 /" + cal + "cal 소모, " + m + "m",
//					 * 3000);
//					 * 
//					 * ActivityFragment frg = (ActivityFragment)
//					 * mSectionsPagerAdapter
//					 * .getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//					 * frg.showMessage(message); frg.sendMessage("OK");
//					 * 
//					 * } catch (Exception e) { } } // U-R Walking End
//					 * 
//					 * if ("R".equals(message.substring(0, 1))) { try { int
//					 * sindex = message.indexOf("S"); int eindex =
//					 * message.indexOf("E"); int cindex = message.indexOf("C");
//					 * int dindex = message.indexOf("D");
//					 * 
//					 * String walkCnt = message.substring(1, sindex); String
//					 * hour = message.substring(sindex + 1, eindex); String time
//					 * = message.substring(eindex + 1, cindex); String cal =
//					 * message.substring(cindex + 1, dindex); String m =
//					 * message.substring(dindex + 1);
//					 * 
//					 * Toast.makeText(mContext, walkCnt + "뜀, " + hour +
//					 * "에 시작해서 " + time + "동안 /" + cal + "cal 소모, " + m + "m",
//					 * 3000);
//					 * 
//					 * ActivityFragment frg = (ActivityFragment)
//					 * mSectionsPagerAdapter
//					 * .getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//					 * frg.showMessage(message); frg.sendMessage("OK"); } catch
//					 * (Exception e) { }
//					 * 
//					 * } // U-R Running End
//					 * 
//					 * if ("L".equals(message.substring(0, 1))) { try { int
//					 * eindex = message.indexOf("E");
//					 * 
//					 * String lightSleep = message.substring(1, eindex); String
//					 * sleeping = message.substring(eindex + 1);
//					 * Toast.makeText(mContext, "Light SLEEP : " + lightSleep +
//					 * "에 시작해서 " + sleeping + " 동안 ", 3000);
//					 * 
//					 * ActivityFragment frg = (ActivityFragment)
//					 * mSectionsPagerAdapter
//					 * .getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//					 * frg.showMessage(message); frg.sendMessage("OK");
//					 * 
//					 * } catch (Exception e) { } } // U-R Light End
//					 * 
//					 * if ("D".equals(message.substring(0, 1))) { try { int
//					 * eindex = message.indexOf("E"); String deepSleep =
//					 * message.substring(1, eindex); String sleeping =
//					 * message.substring(eindex + 1);
//					 * 
//					 * Toast.makeText(mContext, "Deep SLEEP : " + deepSleep +
//					 * "에 시작해서 " + sleeping + " 동안 ", 3000);
//					 * 
//					 * ActivityFragment frg = (ActivityFragment)
//					 * mSectionsPagerAdapter
//					 * .getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//					 * frg.showMessage(message); frg.sendMessage("OK"); } catch
//					 * (Exception e) { // TODO: handle exception } } // U-R
//					 * DeepSleep End
//					 * 
//					 * if ("AW".equals(message.substring(0, 2))) {
//					 * 
//					 * int eindex = message.indexOf("E"); int cindex =
//					 * message.indexOf("C"); int dindex = message.indexOf("D");
//					 * 
//					 * String walkcnt = message.substring(2, eindex); String
//					 * times = message.substring(eindex + 1, cindex); String cal
//					 * = message.substring(cindex + 1, dindex); String m =
//					 * message.substring(dindex + 1);
//					 * 
//					 * Toast.makeText(mContext, times + "동안 " + walkcnt +
//					 * " Walk / " + cal + "calories 소모, " + m + "m", 30000);
//					 * 
//					 * ActivityFragment frg = (ActivityFragment)
//					 * mSectionsPagerAdapter
//					 * .getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//					 * frg.showMessage(message); frg.sendMessage("OK"); } else
//					 * if ("AR".equals(message.substring(0, 2))) {
//					 * 
//					 * int eindex = message.indexOf("E"); int cindex =
//					 * message.indexOf("C"); int dindex = message.indexOf("D");
//					 * 
//					 * String runcnt = message.substring(2, eindex); String
//					 * times = message.substring(eindex + 1, cindex); String cal
//					 * = message.substring(cindex + 1, dindex); String m =
//					 * message.substring(dindex + 1);
//					 * 
//					 * Toast.makeText(mContext, times + "동안 " + runcnt +
//					 * " Run / " + cal + "calories 소모, " + m + "m", 30000);
//					 * 
//					 * ActivityFragment frg = (ActivityFragment)
//					 * mSectionsPagerAdapter
//					 * .getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//					 * frg.showMessage(message); frg.sendMessage("OK"); }
//					 */
//					Date tdays = new Date();
//					String tday = Integer.toString(tdays.getDay());
//					String ty = Integer.toString(tdays.getYear() + 1900 - 2000);
//					String tmonth = Integer.toString(tdays.getMonth() + 1);
//					String tmm = tmonth.length() == 1 ? "0" + tmonth : tmonth;
//					String tdate = Integer.toString(tdays.getDate());
//					String td = tdate.length() == 1 ? "0" + tdate : tdate;
//
//					String thour = Integer.toString(tdays.getHours());
//					String th = thour.length() == 1 ? "0" + thour : thour;
//					String tminute = Integer.toString(tdays.getMinutes());
//					String tm = tminute.length() == 1 ? "0" + tminute : tminute;
//					String tsecond = Integer.toString(tdays.getSeconds());
//					String ts = tsecond.length() == 1 ? "0" + tsecond : tsecond;
//					
//					
//					if(!initialized){
//						initialized = true;
//						Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame2);
//						fragmentManager.remove(mFragment);
//						findViewById(R.id.frame2).setVisibility(View.GONE);
//						mMainFrame.setVisibility(View.VISIBLE);
//						getActionBar().show();
//						setTimeLine();
//						ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//								.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//						frg.sendMessage("YMDT"+ ty + "" + tmm + "" + td + "" + th + "" + tm + "" + ts+"" +tday);
////						flagSendDT = false;
////						flagRUSER = false;
////						flagUR = false;
////						flagRS = false;
////						flagBAT = false;
////						flagCWUTS = false;
////						feedback = false;
//					}
//					
//					if (message.contains("U-R") && flagSendDT) {
//						flagUR = true; 
//						feedback = true; 
//						
//						flagSendDT = false;
//						flagRUSER = false;
//						flagRS = false;
//						flagBAT = false;
//						flagCWUTS = false;
//						
//						ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//								.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY); // frg.showMessage(message);
//						frg.showMessage(message);
//						
//						ULog.d(TAG, "재연결 U-R ");
//						frg.sendMessage("OK");
//						mService.sendMessageToRemote("OK");
//					}
//					
//					if (message.contains("R-A") && flagSendDT) {
//						flagRA = true; 
//						feedback = true; 
//						
//						flagUR = false; 
//						flagSendDT = false;
//						flagRUSER = false;
//						flagRS = false;
//						flagBAT = false;
//						flagCWUTS = false;
//						
//						ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//								.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY); // frg.showMessage(message);
//						
//						ULog.d(TAG, "재연결 R-A ");
//						frg.showMessage(message);
//						frg.sendMessage("OK");
//					}
//					
//					
//					
//					if (!feedback) {
//						if (message.contains("RYMDT")) {
//							flagSendDT = !flagSendDT; // false -> true
//							ULog.i(TAG, "flagSendDT:" + flagSendDT);
//							feedback = !feedback; // false -> true
//							Fragment mFragment = getFragmentManager().findFragmentById(R.id.frame2);
//							fragmentManager.remove(mFragment);
//						
//							mMainFrame.setVisibility(View.VISIBLE);
//							getActionBar().show();
//							setTimeLine();
//
//							
//							ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//									.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//							frg.sendMessage("YMDT"+ ty + "" + tmm + "" + td + "" + th + "" + tm + "" + ts+"" +tday);
//						}
//
//						if (message.contains("RUSER")) {
//							feedback = !feedback;// false -> true
//							flagRUSER = !flagRUSER; // false -> true
//							ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//									.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//							frg.showMessage(message);
//							frg.sendMessage("USER178078G100");
//						}
//						
//						if (message.contains("U-R")) {
//							flagUR = !flagUR; // false-> true
//							feedback = !feedback; // false -> true
//							ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//									.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY); // frg.showMessage(message);
//							frg.showMessage(message);
//							frg.sendMessage("OK");
//							mService.sendMessageToRemote("OK");
//						}
//						
//
//						if (message.contains("R-A")) {
//							flagRA = !flagRA; // false-> true
//							feedback = !feedback; // false -> true
//							ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//									.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY); // frg.showMessage(message);
//							frg.showMessage(message);
//							frg.sendMessage("OK");
//						}
//
//						if (message.contains("R-S")) {
//							flagRS = !flagRS; // false-> true
//							feedback = !feedback; // false -> true
//							ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//									.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//							// frg.showMessage(message);
//							frg.sendMessage("OK");
//						}
//						
//
//						if (flagCWUTS) {
//							if (message.contains("OK")) {
//								feedback = !feedback; // false->true
//								
////								mService.sendAlarmsMessage(alrams_day,alrams);
//								mService.sendMessageToRemote(alrams);
//							}
//						}
//
//					} // feekback false end
//
//					if (feedback) {
//						if (flagSendDT ) {
//							if (message.contains("OK")) { // 5초안에 ok가 안오면!! 다시
//															// send
//								flagSendDT = !flagSendDT; // true->false
//								feedback = !feedback; // true->false;
//								ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//								frg.showMessage(message);
//							}
//						}  
//
//						if (flagRUSER) {
//							if (message.contains("OK")) {// 5초안에 ok가 안오면!! 다시
//															// send
////								initialized = !initialized;// false -> true
//								flagRUSER = !flagRUSER; // true -> false
//								feedback = !feedback; // true -> false
//								// feedback은
//								// 그대로 true
//								ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//										.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//								frg.showMessage(message);
//							}
//						}
//						
//						
//						if (flagUR) {
//							if ("UW".equals(message.substring(0, 2))) {
//								// feedback = !feedback;//false->true
//								// UW/ 0367 /S/ 15 05 28 13 45 /E/ 02 36 /C/
//								// 01234 /D/ 01234
//								try {
//									int sindex = message.indexOf("S");
//									int eindex = message.indexOf("E");
//									int cindex = message.indexOf("C");
//									int dindex = message.indexOf("D");
//
//									String walkCnt = message.substring(2, sindex);
//									String date = message.substring(sindex + 1, eindex);
//									String time = message.substring(eindex + 1, cindex);
//									String cal = message.substring(cindex + 1, dindex);
//									String m = message.substring(dindex + 1);
//									
//		//*************date == 오늘 날짜가 아니면  ***************************************************************
//		//*************daily 통계 데이터로 변경!!!!!!!!!!!!!!!!!!!********************************
//									
//									if(Integer.parseInt(walkCnt)>=0 && Float.parseFloat(m) >= 0.00f && Float.parseFloat(cal) >= 0.00f ){
//										if((ty+""+tmm+""+td).equals(date.substring(0,6))){   //if(mToday
//											//if(mTodayPreference.getDataDate().equals(date.substring(0,6))){
//												mActivityAllCount = mActivityAllCount + Integer.parseInt(walkCnt);
//												mActivityAllKm = mActivityAllKm + (Float.parseFloat(m) / 1000);
//												mActivityAllKcal = mActivityAllKcal +  (Float.parseFloat(cal) / 1000);
//												
//												mWalkDistance = mWalkDistance + (Float.parseFloat(m) / 1000.00f);
//												mWalkKcal = mWalkKcal + (Float.parseFloat(cal) / 1000.00f);
//												
//												String hour = time.substring(0,2);
//												String min = time.substring(3);
//												mWalkHour  = mWalkHour+ Integer.parseInt(hour);
//												mWalkMin  = mWalkMin+ Integer.parseInt(min);
//												
////												mTodayActCnt.add(Integer.parseInt(walkCnt));
////												mTodayActCntTime.add(hour+":"+min);
//											}else{
//												
//												//통계
//											}
//											
//											
//											ULog.d(TAG, walkCnt + "걸음, " + date + "에 시작해서 " + time + "동안 /" + cal
//													+ "cal 소모, " + m + "m");
//
//									}
//										
//
//									ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//											.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//									frg.showMessage(message);
//									frg.sendMessage("OK");
//									
//
//								} catch (Exception e) {
//									// TODO: handle exception
//								}
//							} // U-R Walking End
//
//							else if ("UR".equals(message.substring(0, 2))) {
//								// feedback = !feedback;//false->true
//								// UR/ 0367 /S/ 15 05 28 13 45 /E/02 36 /C/ 01234
//								// /D/ 01234
//								try {
//									int sindex = message.indexOf("S");
//									int eindex = message.indexOf("E");
//									int cindex = message.indexOf("C");
//									int dindex = message.indexOf("D");
//
//									String runCnt = message.substring(2, sindex);
//									String date = message.substring(sindex + 1, eindex);
//									String time = message.substring(eindex + 1, cindex);
//									String cal = message.substring(cindex + 1, dindex);
//									String m = message.substring(dindex + 1);
//
//									
//									
//			//*************date == 오늘 날짜가 아니면  ***************************************************************
//			//*************daily 통계 데이터로 변경!!!!!!!!!!!!!!!!!!!********************************
//									ULog.i(TAG,"mToday==>" + mToday);
//									ULog.i(TAG,"date.substring(0,6)==>" + date.substring(0,6));
//									
//									if(Integer.parseInt(runCnt)>=0 && Float.parseFloat(m) >= 0.00f && Float.parseFloat(cal) >= 0.00f ){
//										if((ty+""+tmm+""+td).equals(date.substring(0,6))){  //mToday.equals
//										//if(mTodayPreference.getDataDate().equals(date.substring(0,6))){
//											ULog.i(TAG,"runCnt==>" + runCnt);
//											mActivityAllCount = mActivityAllCount + Integer.parseInt(runCnt);
//											ULog.i(TAG,"mActivityAllCount==>" + mActivityAllCount);
//											mActivityAllKm = mActivityAllKm + (Float.parseFloat(m) / 1000.00f);
//
//											mActivityAllKcal = mActivityAllKcal +  (Float.parseFloat(cal) / 1000);
//											ULog.i(TAG,"m==>" + m);
//											ULog.i(TAG,"mActivityAllKm" + mActivityAllKm);
//											mRunDistance = mRunDistance + (Float.parseFloat(m) / 1000.00f);
//											mRunKcal = mRunKcal + (Float.parseFloat(cal) / 1000.00f);
//	
//											ULog.i(TAG,"mRunDistance" + mRunDistance);
//											
//											
//											String hour = time.substring(0,2);
//											String min = time.substring(3);
//											mRunHour  = mRunHour+ Integer.parseInt(hour);
//											mRunMin  = mRunMin+ Integer.parseInt(min);
//											
//	//										mTodayActCnt.add(Integer.parseInt(runCnt));
//	//										mTodayActCntTime.add(hour+":"+min);
//										}else{
//											
//											//통계
//											
//											
//	//										오늘 - runCnt == 1 이면  전날
//	//													== 2 이면 전전날
//	//													== 3 ... 이런식으로?
//											
//											//년 월 일 다 비교해야함..
//										}
//									
//									}
//									
//									ULog.i(TAG, runCnt + "뜀, " + date + "에 시작해서 " + time + "동안 /" + cal + "cal 소모, " + m + "m");
//
//									ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//											.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//									frg.showMessage(message);
//									frg.sendMessage("OK");
//								} catch (Exception e) {
//									// TODO:handle exception
//								}
//							} // U-R Running End
//
//							else if ("UL".equals(message.substring(0, 2))) {
//								// UL/15 05 28 1345 /E/ 02 36
//								ligthSleepCnt++;
//								try {
//									int eindex = message.indexOf("E");
//
//									String lightSleep = message.substring(2, eindex);
//									String sleeping = message.substring(eindex + 1);
//									
//									
//									mLowSleepHour =mLowSleepHour+ Integer.parseInt(sleeping.substring(0, 2));
//									mLowSleepMin =mLowSleepMin+ Integer.parseInt(sleeping.substring(2));
//									
////									
////									if(mTodayPreference.getDataDate().equals(lightSleep.substring(0,6)) /*||  오늘날짜-어제날짜 == 1*/ ){
////										mLowSleepHour = Integer.parseInt(sleeping.substring(0, 2));
////										mLowSleepMin = Integer.parseInt(sleeping.substring(2));
////									}else{
////										
////										//통계
////									}
//									
//								
//									
//									ULog.d(TAG,"Deep SLEEP "+mLowSleepHour+"시간 " + mLowSleepMin+"분 동안 Deep Sleep ");
//
//									ULog.d(TAG,"Light SLEEP : " + lightSleep + "에 시작해서 " + sleeping + " 동안 ");
//
//									
//									SleepFragment frg = (SleepFragment) mSectionsPagerAdapter .getItem(FragmentAdapter.FRAGMENT_POS_SLEEP);
//									frg.showMessage(message);
//									frg.sendMessage("OK"); // deep Sleep 사이사이에
//															// light sleep
//									switch (ligthSleepCnt) {
//									case 0:
//										break;
//									case 1:
//									}
//								} catch (Exception e) { // TODO: handle
//														// exception
//
//								}
//							} // U-R Light End
//
//							else if ("UD".equals(message.substring(0, 2))) {
//								// D/15 05 28 13 45 /E/ 02 36
//								try {
//									int eindex = message.indexOf("E");
//									String deepSleep = message.substring(2, eindex);
//									String sleeping = message.substring(eindex + 1);
//
//									
//									
//									
//									
//									mDeepSleepHour = mDeepSleepHour+ Integer.parseInt(sleeping.substring(0, 2));
//									mDeepSleepMin =mDeepSleepMin +  Integer.parseInt(sleeping.substring(2));
//
////									mTodaySleepCnt 
//									
////									if(mTodayPreference.getDataDate().equals(deepSleep.substring(0,6)) /*||  오늘날짜-어제날짜 == 1*/ ){
////										mDeepSleepHour = Integer.parseInt(sleeping.substring(0, 2));
////										mDeepSleepMin = Integer.parseInt(sleeping.substring(2));
////									}else{
////										
////										//통계
////									}
//									ULog.d(TAG,"Deep SLEEP "+mDeepSleepHour+"시간 " + mDeepSleepMin+"분 동안 ");
//									ULog.d(TAG,"Deep SLEEP : " + deepSleep + "에 시작해서 " + sleeping + " 동안 ");
//
//									SleepFragment frg = (SleepFragment) mSectionsPagerAdapter .getItem(FragmentAdapter.FRAGMENT_POS_SLEEP);
//									frg.showMessage(message);
//									frg.sendMessage("OK"); 
//								} catch (Exception e) {
//									// TODO:handle exception
//								}
//							} // U-R DeepSleep End
//
//							else if (message.contains("FIN")) {
//								feedback = !feedback; // true -> false;
//								flagUR = !flagUR;// true -> false
//								ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//										.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//								frg.showMessage(message);
//								frg.sendMessage("OK");
//							}
//						} // U-R if(flagUR) end
//
//						// 계속 통신중
//						if (flagRA) {
//							if ("AW".equals(message.substring(0, 2))) {
//								// AW 3866 E 0236 C 01234 D 01234
//								feedback = !feedback; // true -> false;
//								flagRA = !flagRA;// true -> false;
//
//								int eindex = message.indexOf("E");
//								int cindex = message.indexOf("C");
//								int dindex = message.indexOf("D");
//
//								String walkcnt = message.substring(2, eindex);
//								String times = message.substring(eindex + 1, cindex);
//								String cal = message.substring(cindex + 1, dindex);
//								String m = message.substring(dindex + 1);
//
//								if(Integer.parseInt(walkcnt)>=0 && Float.parseFloat(m) >= 0.00f && Float.parseFloat(cal) >= 0.00f ){
//									
//									//R-A를 하던중에 날짜가 바뀌면.......... 
//									//R-A를 하던중에 날짜가 바뀌면.......... 
//									//R-A를 하던중에 날짜가 바뀌면.......... 
//									//R-A를 하던중에 날짜가 바뀌면..........  예외처리
//									Date today = new Date();
//									
//									String h = Integer.toString(today.getHours());
//									String mi = Integer.toString(today.getMinutes());
//									
//									
//									
//									mActivityAllCount = mActivityAllCount + Integer.parseInt(walkcnt);
//									mActivityAllKm = mActivityAllKm + (Float.parseFloat(m) / 1000.00f);
//									mActivityAllKcal = mActivityAllKcal +  (Float.parseFloat(cal) / 1000);
//	
//									mWalkDistance = mWalkDistance+ (Float.parseFloat(m) / 1000.00f);
//									mWalkKcal = mWalkKcal + (Float.parseFloat(cal) / 1000.00f);
//	
//									String hour = times.substring(0,2);
//									String min = times.substring(3);
//									
//									/**
//									 * 현재 시간 - 운동시간 = 운동 시작 시각
//									 */
//									int activityStartHour= Integer.parseInt(h) - Integer.parseInt(hour);
//									
//									/**
//									 * 현재 시간 - 운동시간 = 운동 시작 분  / b가 음수면 a-1
//									 */
//									int activityStartMin = Integer.parseInt(mi) - Integer.parseInt(min);
//									
//									ULog.d(TAG, " activityStartHour==>" + activityStartHour);
//									ULog.d(TAG, " activityStartMin==>" + activityStartMin);
//									ULog.d(TAG, " Integer.parseInt(h)==>" + Integer.parseInt(h));
//									ULog.d(TAG, " Integer.parseInt(mi)==>" + Integer.parseInt(mi));
//									
//									
//									insertTodayData(activityStartMin, activityStartHour, walkcnt);
//	
//									ULog.d(TAG, " mTodayActCnt.size()==>" + mTodayActCnt.size());
//									ULog.d(TAG, " mTodayActCntTimeHour.size()==>" + mTodayActCntTimeHour.size());
//									ULog.d(TAG, " mTodayActCntTimeMin.size()==>" + mTodayActCntTimeMin.size());
//									
//									
//									mWalkHour  = mWalkHour+ Integer.parseInt(hour);
//									mWalkMin  = mWalkMin+ Integer.parseInt(min);
//									
//									ULog.d(TAG,times + "동안 " + walkcnt + " Walk / " + cal + "calories 소모, " + m + "m");
//								
//								}
//								
//
//								ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//										.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//								frg.showMessage(message);
//								frg.sendMessage("OK");
//							} else if ("AR".equals(message.substring(0, 2))) {
//								// AR 3866 E 0236 C 01234 D 01234
//								feedback = !feedback; // true -> false;
//								flagRA = !flagRA; // true -> false;
//
//								int eindex = message.indexOf("E");
//								int cindex = message.indexOf("C");
//								int dindex = message.indexOf("D");
//
//								String runcnt = message.substring(2, eindex);
//								String times = message.substring(eindex + 1, cindex);
//								String cal = message.substring(cindex + 1, dindex);
//								String m = message.substring(dindex + 1);
//								
//								
//								int[] mSleepConnFlag;
//								
//								boolean mDaySleepDataFlag;  //누적 데이터중 Sleep 데이터 누적 중이었을 경우!
//								int mSleepConnCnt;    //누적 데이터중 Sleep 데이터 누적 cnt int mSleepConnFlag
//								
//								ArrayList<Integer> mTempSleepCnt;   	   //sleep 누적 cnt(graph height/deep, low, wake 구분) 1
//								ArrayList<Integer> mTempSleepDate;   	   //sleep 누적 cnt(graph height/deep, low, wake 구분) 1
//								ArrayList<Integer> mTempSleepCntTimeHour; 
//								ArrayList<Integer> mTempSleepCntTimeMin;  
//								
//								if(Integer.parseInt(runcnt)>=0 && Float.parseFloat(m) >= 0.00f && Float.parseFloat(cal) >= 0.00f ){
//								
//									mActivityAllCount = mActivityAllCount + Integer.parseInt(runcnt);
//									mActivityAllKm = mActivityAllKm + (Float.parseFloat(m) / 1000.00f);
//									mActivityAllKcal = mActivityAllKcal +  (Float.parseFloat(cal) / 1000);
//									
//									mRunDistance = mRunDistance + (Float.parseFloat(m) / 1000.00f);
//									mRunKcal = mRunKcal + (Float.parseFloat(cal) / 1000.00f);
//	
//									String hour = times.substring(0,2);
//									String min = times.substring(3);
//									mRunHour  = mRunHour+ Integer.parseInt(hour);
//									mRunMin  = mRunMin+ Integer.parseInt(min);
//									
//									Date today = new Date();
//									
//									String h = Integer.toString(today.getHours());
//									String mi = Integer.toString(today.getMinutes());
//									
//									/**
//									 * 현재 시간 - 운동시간 = 운동 시작 시각
//									 */
//									int activityStartHour= Integer.parseInt(h) - Integer.parseInt(hour);
//									
//									/**
//									 * 현재 시간 - 운동시간 = 운동 시작 분  / b가 음수면 a-1
//									 */
//									int activityStartMin = Integer.parseInt(mi) - Integer.parseInt(min);
//									
//									ULog.d(TAG, " activityStartHour==>" + activityStartHour);
//									ULog.d(TAG, " activityStartMin==>" + activityStartMin);
//									ULog.d(TAG, " Integer.parseInt(h)==>" + Integer.parseInt(h));
//									ULog.d(TAG, " Integer.parseInt(mi)==>" + Integer.parseInt(mi));
//									
//									
//									insertTodayData(activityStartMin, activityStartHour, runcnt);									
//									
//									ULog.d(TAG, " mTodayActCnt.size()==>" + mTodayActCnt.size());
//									ULog.d(TAG, " mTodayActCntTimeHour.size()==>" + mTodayActCntTimeHour.size());
//									ULog.d(TAG, " mTodayActCntTimeMin.size()==>" + mTodayActCntTimeMin.size());
//									
//									ULog.d(TAG, times + "동안 " + runcnt + " Run / " + cal + "calories 소모, " + m + "m");
//								}
//								
//								ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//										.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//								frg.showMessage(message);
//								frg.sendMessage("OK");
//							}
//						} // R-A flagRA end
//
//						if (flagRS) {
//							if ("SD".equals(message.substring(0, 2))) {
//								// SD 1505281345 E 0236
//								feedback = !feedback; // true -> false;
//								flagRS = !flagRS; // true ->false
//
//								int eindex = message.indexOf("E");
//
//								String deepseelpStart = message.substring(2, eindex);
//								String times = message.substring(eindex + 1);
//								
//								
//								mDeepSleepHour = mDeepSleepHour+ Integer.parseInt(times.substring(0, 2));
//								mDeepSleepMin = mDeepSleepMin+ Integer.parseInt(times.substring(2));
//								ULog.d(TAG,"Deep SLEEP "+mDeepSleepHour+"시간 " + mDeepSleepMin+"분 동안 Deep Sleep ");
//
//								ULog.d(TAG, deepseelpStart + "에 자기 시작해서 " + times + "동안 Deep Sleep");
//
//								SleepFragment frg = (SleepFragment) mSectionsPagerAdapter .getItem(FragmentAdapter.FRAGMENT_POS_SLEEP);
//								frg.showMessage(message);
//								frg.sendMessage("OK"); 
//							} else if ("SL".equals(message.substring(0, 2))) {
//								// SL 1505281345/E/ 0236
//								feedback = !feedback; // true -> false;
//								flagRS = !flagRS; // true -> false;
//								ligthSleepCntRt++;
//
//								int eindex = message.indexOf("E");
//
//								String lightseelpStart = message.substring(2, eindex);
//								String times = message.substring(eindex + 1);
//
//								
//								
//								mLowSleepHour = mLowSleepHour+Integer.parseInt(times.substring(0, 2));
//								mLowSleepMin = mLowSleepMin+Integer.parseInt(times.substring(2));
//								
//								ULog.d(TAG,"Deep SLEEP "+mLowSleepHour+"시간 " + mLowSleepMin+"분  동안 Low Sleep");
//								ULog.d(TAG, lightseelpStart + "에 자기 시작해서 " + times + "동안 Light Sleep");
//
//								// deep Sleep 사이사이에 light sleep
//								switch (ligthSleepCntRt) {
//								case 1:
//
//									break;
//								case 2:
//
//									break;
//								default:
//									break;
//								}
//
//								SleepFragment frg = (SleepFragment) mSectionsPagerAdapter .getItem(FragmentAdapter.FRAGMENT_POS_SLEEP);
//								frg.showMessage(message);
//								frg.sendMessage("OK"); 
//							}
//						}
//
//						if (flagCWUTS) {
//							if (message.contains("OK")) {
//								feedback = !feedback; // true->false
//								flagCWUTS = !flagCWUTS; // true->false;
//								ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//										.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//								frg.showMessage(message);
//							}
//						}
//
//						if (flagBAT) {
//							if ("BAT".equals(message.substring(0, 3))) {
//								// BAT00
//								flagBAT = !flagBAT; // true-> flase
//								feedback = !feedback; // true -> false
//								ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter
//										.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//								frg.showMessage(message);
//
//								Intent intent = new Intent(getApplicationContext(), MyReceiver1.class);
//								message = message.replace("BAT", "");
//								message = message.replace("\r\n", "");
//
//								intent.putExtra(Constants.BROADCAST_RECEIVER, message);
//								sendBroadcast(intent);
//
//								frg.sendMessage("OK");
//
//							}
//						}
//
//					} // feedback true end
//					
//					try {
//						ActivityFragment frg = (ActivityFragment) mSectionsPagerAdapter.getItem(FragmentAdapter.FRAGMENT_POS_ACTIVITY);
//						frg.setWalking(Integer.toString(mActivityAllCount), mActivityAllKm, mActivityAllKcal);
//						SleepFragment frg21 = (SleepFragment) mSectionsPagerAdapter.getItem(FragmentAdapter.FRAGMENT_POS_SLEEP); 
//						frg21.setSleep((mDeepSleepHour*60)+mDeepSleepMin, (mLowSleepHour*60)+mLowSleepMin);
//					
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//					
//					
//					try {
//						Bundle bundle = new Bundle();
//						
//						//날짜 업데이트를 언제할건지 다시 생각!!!! = timeline update 될때
//						
//						bundle.putString(Constants.TODAY_SAVE_DATA_DATE, ty + "" + tmm + "" + td);
//						bundle.putInt(Constants.TODAY_ACTIVITY_CNT, mActivityAllCount);
//						
//						
//						bundle.putIntegerArrayList(Constants.TODAY_ACT_GRAPH_CNT, mTodayActCnt);
//						
//
////						ULog.v(TAG, "1695번줄 hour:" + mTodayActCntTimeHour);
//						bundle.putIntegerArrayList(Constants.TODAY_ACT_GRAPH_TIME_HOUR, mTodayActCntTimeHour);
//						
//						
////						ULog.v(TAG, "1695번줄 hour2:" + bundle.getIntegerArrayList(Constants.TODAY_ACT_GRAPH_TIME_HOUR));
//						bundle.putIntegerArrayList(Constants.TODAY_ACT_GRAPH_TIME_MIN, mTodayActCntTimeMin);
//						
//						bundle.putFloat(Constants.TODAY_ACT_WALK_DIS, mWalkDistance);
////						bundle.putInt(Constants.TODAY_ACT_WALK_H, mWalkHour);
//						bundle.putInt(Constants.TODAY_ACT_WALK_M, (mWalkHour*60)+mWalkMin);
//						bundle.putFloat(Constants.TODAY_ACT_WALK_KCAl, mWalkKcal);
//						
//						bundle.putFloat(Constants.TODAY_ACT_RUN_DIS, mRunDistance);
////						bundle.putInt(Constants.TODAY_ACT_RUN_H, mRunHour);
//						bundle.putInt(Constants.TODAY_ACT_RUN_M, (mRunHour*60)+mRunMin);
//						bundle.putFloat(Constants.TODAY_ACT_RUN_CAL, mRunKcal);
//						
//						
////						bundle.putInt(Constants.TODAY_DEEP_SLEEP_H, mDeepSleepHour);
//						ULog.v(TAG , "message: " +(mDeepSleepHour*60)+mDeepSleepMin);
//						bundle.putInt(Constants.TODAY_DEEP_SLEEP_M, (mDeepSleepHour*60)+mDeepSleepMin);
////						bundle.putInt(Constants.TODAY_LOW_SLEEP_H, mLowSleepHour);
//						ULog.v(TAG , "message: " + (mLowSleepHour*60)+mLowSleepMin);
//						bundle.putInt(Constants.TODAY_LOW_SLEEP_M, (mLowSleepHour*60)+mLowSleepMin);
//						
//						
//						mTodayPreference.setTodayData(bundle);
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//
//				}
//				break;
//
//			default:
//				break;
//			}
//
//			super.handleMessage(msg);
//		}
//	}
//	
//	/**
//	 * today detail graph data in
//	 * @param activityStartMin
//	 * @param activityStartHour
//	 * @param walkcnt
//	 */
//	public void insertTodayData(int activityStartMin, int activityStartHour, String walkcnt){
//		int compareMin = 0, compareHour=0;
//		
//		if(mTodayActCnt.size()>0){
//			compareMin = mTodayActCntTimeMin.get(mTodayActCntTimeMin.size()-1);
//			compareHour = mTodayActCntTimeHour.get(mTodayActCntTimeHour.size()-1);
//			
//			ULog.d(TAG, " compareMin==>" + compareMin);
//			ULog.d(TAG, " compareHour==>" + compareHour);
//			
//			
//			int activityStartHour2 = 0;
//			int activityStartMin2 = 0;
//			
//			if(activityStartMin<0){
//				activityStartHour2 = activityStartHour-1;
//				activityStartMin2 = (60+activityStartMin)/10;
//			}else{
//				activityStartHour2 = activityStartHour;
//				activityStartMin2 = activityStartMin/10;
//			}
//			
//			ULog.d(TAG, " activityStartHour2==>" + activityStartHour2);
//			ULog.d(TAG, " activityStartMin2==>" + activityStartMin2);
//			
//			//아래 if 전에 날짜 먼저 비교 날짜가 같은지!
//			
//			if(activityStartHour2 < 0){
//				//  전날 통계 데이터로 들어감
//				return;
//			}
//			
//			
//			if(    compareMin == activityStartMin2     &&    compareHour == activityStartHour2    ){   
//				
//				ULog.v(" 시간 , 분 전부 같을 경우 이전 array에 in");
//				mTodayActCnt.set(mTodayActCnt.size()-1, mTodayActCnt.get(mTodayActCnt.size()-1) + Integer.parseInt(walkcnt));
//				
//			}else if( activityStartHour2 == (compareMin-1)&& activityStartMin2 == 0 ){
//				ULog.v(" 59분에서 정각으로 !! ");
//				mTodayActCnt.add(Integer.parseInt(walkcnt));
//				mTodayActCntTimeHour.add(activityStartHour2);
//				mTodayActCntTimeMin.add(0);
//			}else{
//				
//				
//				if(      (  activityStartMin2-1 )   ==  compareMin && compareHour == activityStartHour2     ){
//					
//					ULog.v(" 시간 같고 분만 1 차이남!");
//					mTodayActCnt.add(Integer.parseInt(walkcnt));
//					ULog.v(TAG, "1354번줄 hour" + activityStartHour2);
//					mTodayActCntTimeHour.add(activityStartHour2);
//					mTodayActCntTimeMin.add(activityStartMin2);
//				}else {
//					
//					int emptyMin = (activityStartMin2*10) - (compareMin*10);
//					int emptyHour = activityStartHour2 - compareHour;
//					
//					if(emptyMin < 0 ){
//						emptyHour = emptyHour - 1; 
//						emptyMin = emptyMin + 60;
//					}
//					
//					emptyHour = emptyHour * 60;
//					int j = 0 ;
//					for(int i=0; i<(emptyMin+emptyHour)/10;i++){
//						mTodayActCnt.add(0);
//						
//						
//						// 00 10 20 30 40 50 60 70 80 90 100 110 120 130 140
//						// x  x  x  x   x x  00  x  x  x  x  x   00
//						ULog.v(TAG, "1375번줄 hour" + emptyHour + j);
//						mTodayActCntTimeHour.add(compareHour + j);
//						mTodayActCntTimeMin.add(i-j);
//						
//						if( (i+1)%6 == 0 && i != 0){     
//							j++;
//						}
//						
//					}
//					
//					ULog.v(" 시간, 분 다 차이나서 곱하고 빼고...");
//				}
//				
//			}
//		}else{
//			ULog.v(" 오늘의 첫번째 데이터.....는  hour->0:minutes->0 이 아니면 앞 배열 전부 cnt 0으로 set");
//			
//			
//
//			int k = 0 ;
//			for(int i= 0; i < activityStartHour*6 ; i++){
//				mTodayActCnt.add(0);
//				
//				mTodayActCntTimeHour.add(0 + k);
//				mTodayActCntTimeMin.add(i-(k*6));
//				ULog.v(TAG, "k=======>" + k);
//				if( (i+1)%6 == 0 && i != 0){     
//					k++;
//				}
//				
//			}
//			
//			for(int i= 0; i < activityStartMin/10 ; i++){
//				mTodayActCnt.add(0);
//				mTodayActCntTimeHour.add(activityStartHour);
//				mTodayActCntTimeMin.add(i);
//			}
//			ULog.v(TAG, "1556번줄 hour:" + mTodayActCntTimeHour);
//			ULog.v(TAG, "1557번줄 hour:" + mTodayActCntTimeMin);
//
//			
//			
//			mTodayActCnt.add(Integer.parseInt(walkcnt));
//			ULog.v(TAG, "1395번줄 hour:" + activityStartHour);
//			mTodayActCntTimeHour.add(activityStartHour);
//			mTodayActCntTimeMin.add(activityStartMin/10);
//		}
//	}
//
//
//	private void finalizeActivity() {
//		ULog.d(TAG, "# Activity - finalizeActivity()");
//
//		if (!AppSettings.getBgService()) {
//			doStopService();
//		} else {
//		}
//
//		// Clean used resources
//		RecycleUtils.recursiveRecycle(getWindow().getDecorView());
//		System.gc();
//	}
//
//	@Override
//	public void OnFragmentCallback(int msgType,String arg2) {
//		switch (msgType) {
//		case IFragmentListener.CALLBACK_RUN_IN_BACKGROUND:
//			if (mService != null)
//				mService.startServiceMonitoring();
//
//			// background에서 실행
//
//			break;
//		case IFragmentListener.CALLBACK_SEND_MESSAGE:
//			ULog.i(TAG, "FragmentCall Back ");
//
//			if (mService != null && arg2 != null)
//
//				mService.sendMessageToRemote(arg2);
//		default:
//			break;
//		}
//
//	}
//
//	static String alrams = "";
//	static byte alrams_day = 0;
//
//	public static class MyReceiver extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			Bundle bundle = intent.getExtras();
//			int s = bundle.getInt(Constants.BROADCAST_RECEIVER);
//
//			switch (s) {
//			case Constants.BROADCAST_FIND:
//				// Toast.makeText(context, String.format("%s를 수신했습니다.", s),
//				// Toast.LENGTH_SHORT).show();
//				mService.sendMessageToRemote("VIB");
//				break;
//
//			case Constants.BROADCAST_BAT:
//				// 다시 broadCastReceiver Setting
//				feedback = true;
//				flagBAT = !flagBAT; // false -> true
//				mService.sendMessageToRemote("RBAT");
//				break;
//			case Constants.BROADCAST_ALARM_SET:
//
//				byte alarms = bundle.getByte("ALARMS_DAY");
//				String strings = bundle.getString("ALARMS_TIME");
//				flagCWUTS = !flagCWUTS; // false->true
//				feedback = false;
//				alrams = strings;
//				alrams_day = alarms;
//				mService.sendMessageToRemote("CWUTS");
//				break;
//			default:
//				break;
//			}
//		}
//	}
//
//	@Override
//	public void onTabReselected(Tab tab, FragmentTransaction ft) {
//		// TODO Auto-generated method stub
//
//	}
//
//	private RunningServiceInfo isRunningService(Context context, Class<?> cls) {
//		boolean isRunning = false;
//
//		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);
//
//		if (info != null) {
//			for (ActivityManager.RunningServiceInfo serviceInfo : info) {
//				ComponentName compName = serviceInfo.service;
//				String className = compName.getClassName();
//
//				if (className.equals(cls.getName())) {
//					return serviceInfo;
//					// break;
//				}
//			}
//		}
//		// return isRunning;
//		return null;
//	}
//
//}
