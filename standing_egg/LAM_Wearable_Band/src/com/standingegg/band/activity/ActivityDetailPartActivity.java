package com.standingegg.band.activity;

import java.util.ArrayList;

import com.standingegg.band.R;
import com.standingegg.band.contents.DBManager;
import com.standingegg.band.sleeping.SleepDetailPartActivity;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.TodayPreferences;
import com.standingegg.band.util.ULog;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityDetailPartActivity extends Activity implements OnClickListener {
	private Context mContext = null;
	private String TAG = "ActivityDetailPartActivity";
	// private Gallery galleryLoop = null;
	private Gallery graphLoop = null;
	private ImageView mPoint = null;

	private LinearLayout mLinear, mLinear2, mLinear3;
	private TextView mActivityTxt, mActivityWalkTxt, mActivityRunTxt;

	private TextView mActivityKmAvg, mActivityStepAvg, mActivityKcalAvg;
	private TextView mWalkKmAvg, mWalkHourAvg, mWalkKcalAvg;
	private TextView mRunKmAvg, mRunHourAvg, mRunKcalAvg;
	private TextView mActivityKmAll, mActivityStepAll, mActivityKcalAll;
	private ImageButton mMinusBtn, mSleepBtn, mPlusBtn;

	private TodayPreferences mTodayPreference = null;
	private DBManager dbManager;
	int UNIT;

	private ArrayList<String> dailyDate = new ArrayList<String>();
	private ArrayList<Integer> dailyAllCnt = new ArrayList<Integer>();
	private ArrayList<Integer> dailyAllDistance = new ArrayList<Integer>();
	private ArrayList<Integer> dailyAllCal = new ArrayList<Integer>();
	private ArrayList<Integer> dailyWalkDistance = new ArrayList<Integer>();
	private ArrayList<Integer> dailyWalkMinutes = new ArrayList<Integer>();
	private ArrayList<Integer> dailyWalkCal = new ArrayList<Integer>();
	private ArrayList<Integer> dailyRunDistance = new ArrayList<Integer>();
	private ArrayList<Integer> dailyRunMinutes = new ArrayList<Integer>();
	private ArrayList<Integer> dailyRunCal = new ArrayList<Integer>();
	private ArrayList<Integer> imageHeight = new ArrayList<Integer>();
	
	
	
	private ArrayList<String> weeklyDate = new ArrayList<String>();
//	활동량 평균5.78 km  / time / kcal
	//거리 / 7,800 걸음  / 420 kcal
	private ArrayList<Integer> weeklyAvgDistance = new ArrayList<Integer>();
	private ArrayList<Integer> weeklyAvgStep = new ArrayList<Integer>();
	private ArrayList<Integer> weeklyAvgCal = new ArrayList<Integer>();
	
	//걷기 평균  5.08 km / time / kcal
	private ArrayList<Integer> weeklyAvgWalkDistance = new ArrayList<Integer>();
	private ArrayList<Integer> weeklyAvgWalkMinute = new ArrayList<Integer>();
	private ArrayList<Integer> weeklyAvgWalkCal = new ArrayList<Integer>();
	
	//평균 낸 day count
	private ArrayList<Integer> weeklyAvgDayCount = new ArrayList<Integer>();
	
	//달리기 평균 0.78 km  / time / kcal
	private ArrayList<Integer> weeklyAvgRunDistance = new ArrayList<Integer>();
	private ArrayList<Integer> weeklyAvgRunMinutes = new ArrayList<Integer>();
	private ArrayList<Integer> weeklyAvgRunCal = new ArrayList<Integer>();
	
	//총합계 km/ cnt/ cal
	private ArrayList<Integer> weeklyAllDisatance = new ArrayList<Integer>();
	private ArrayList<Integer> weeklyAllStep = new ArrayList<Integer>();
	private ArrayList<Integer> weeklyAllCal = new ArrayList<Integer>();
	
	private ArrayList<Integer> weeklyImageHeight = new ArrayList<Integer>();
	
	
	private ArrayList<String> monthlyDate = new ArrayList<String>();
//	활동량 평균5.78 km  / time / kcal
	//거리 / 7,800 걸음  / 420 kcal
	private ArrayList<Integer> monthlyAvgDistance = new ArrayList<Integer>();
	private ArrayList<Integer> monthlyAvgStep = new ArrayList<Integer>();
	private ArrayList<Integer> monthlyAvgCal = new ArrayList<Integer>();
	
	//걷기 평균  5.08 km / time / kcal
	private ArrayList<Integer> monthlyAvgWalkDistance = new ArrayList<Integer>();
	private ArrayList<Integer> monthlyAvgWalkMinute = new ArrayList<Integer>();
	private ArrayList<Integer> monthlyAvgWalkCal = new ArrayList<Integer>();
	
	//평균 낸 day count
	private ArrayList<Integer> monthlyAvgDayCount = new ArrayList<Integer>();
	
	//달리기 평균 0.78 km  / time / kcal
	private ArrayList<Integer> monthlyAvgRunDistance = new ArrayList<Integer>();
	private ArrayList<Integer> monthlyAvgRunMinutes = new ArrayList<Integer>();
	private ArrayList<Integer> monthlyAvgRunCal = new ArrayList<Integer>();
	
	//총합계 km/ cnt/ cal
	private ArrayList<Integer> monthlyAllDisatance = new ArrayList<Integer>();
	private ArrayList<Integer> monthlyAllStep = new ArrayList<Integer>();
	private ArrayList<Integer> monthlyAllCal = new ArrayList<Integer>();
	
	private ArrayList<Integer> monthlyImageHeight = new ArrayList<Integer>();


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_part);

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(android.R.color.transparent);

		actionBar.setTitle("오늘");

		mContext = this;
		dbManager = new DBManager(getApplicationContext(), "lamband.db", null, 1);
		mTodayPreference = TodayPreferences.getInstance(mContext);
		setComponent();
		setData();
	}

	int goal = 10000;

	int graph_width = 70;

	public void setComponent() {
		mPoint = (ImageView) findViewById(R.id.point);
		mActivityKmAvg = (TextView) findViewById(R.id.activity_km_avg);
		mActivityStepAvg = (TextView) findViewById(R.id.activity_h_avg);
		mActivityKcalAvg = (TextView) findViewById(R.id.activity_kcal_avg);

		mActivityTxt = (TextView) findViewById(R.id.all_txt);
		mActivityWalkTxt = (TextView) findViewById(R.id.walk_txt);
		mActivityRunTxt = (TextView) findViewById(R.id.run_txt);
		mLinear = (LinearLayout) findViewById(R.id.act_all_linear);
		mLinear2 = (LinearLayout) findViewById(R.id.act_all_linear_2);
		mLinear3 = (LinearLayout) findViewById(R.id.act_all_linear_3);

		mWalkKmAvg = (TextView) findViewById(R.id.walk_km_avg);
		mWalkHourAvg = (TextView) findViewById(R.id.walk_h_avg);
		mWalkKcalAvg = (TextView) findViewById(R.id.walk_kcal_avg);
		mRunKmAvg = (TextView) findViewById(R.id.run_km_avg);
		mRunHourAvg = (TextView) findViewById(R.id.run_h_avg);
		mRunKcalAvg = (TextView) findViewById(R.id.run_kcal_avg);
		
		mActivityKmAll = (TextView) findViewById(R.id.all_km);
		mActivityStepAll = (TextView) findViewById(R.id.all_cnt);
		mActivityKcalAll = (TextView) findViewById(R.id.all_kcal);
		
		mMinusBtn = (ImageButton) findViewById(R.id.minus_btn);
		mSleepBtn = (ImageButton) findViewById(R.id.sleep_btn);
		mPlusBtn = (ImageButton) findViewById(R.id.plus_btn);

		mMinusBtn.setOnClickListener(this);
		mSleepBtn.setOnClickListener(this);
		mPlusBtn.setOnClickListener(this);
		try {
			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();

			int unit = bundle.getInt(Constants.DAY_UNIT);
			switch (unit) {
			case Constants.DAILY:
				setDaily();
				break;
			case Constants.WEEKLY:
				setWeekly();
				break;
			case Constants.MONTHLY:
				setMonthly();
				break;
			}
		} catch (Exception e) {
			UNIT = Constants.DAILY;
//			setDaily();
			mPlusBtn.setEnabled(false);
		}
		setUIText();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		default:

		}
		return super.onOptionsItemSelected(item);
	}

	private void setUIText() {
		if (UNIT == Constants.DAILY) {
			
			mLinear.setVisibility(View.INVISIBLE);
			mLinear2.setVisibility(View.INVISIBLE);
			mLinear3.setVisibility(View.INVISIBLE);
			mActivityTxt.setText(getString(R.string.active_mass));
			mActivityWalkTxt.setText(getString(R.string.walking));
			mActivityRunTxt.setText(getString(R.string.running));
			
		} else {
			mLinear.setVisibility(View.VISIBLE);
			mLinear2.setVisibility(View.VISIBLE);
			mLinear3.setVisibility(View.VISIBLE);
			
			mActivityKmAll.setVisibility(View.VISIBLE);
			mActivityStepAll.setVisibility(View.VISIBLE);
			mActivityKcalAll.setVisibility(View.VISIBLE);
			
			mActivityTxt.setText(getString(R.string.active_mass_avg));
			mActivityWalkTxt.setText(getString(R.string.walking_avg));
			mActivityRunTxt.setText(getString(R.string.running_avg));
		}
	}

	private void setData() {
		Cursor cursor = dbManager.selectDailyActivity();
		// String date[] = new String[cursor.is];

		dailyDate.add("오늘");
		dailyAllCnt.add(mTodayPreference.getActivityAllCount());
		dailyAllDistance.add((mTodayPreference.getWalkDistance() + mTodayPreference.getRunDistance()));
		dailyAllCal.add((mTodayPreference.getWalkCal() + mTodayPreference.getRunCal()));

		dailyWalkDistance.add(mTodayPreference.getWalkDistance());
		dailyWalkMinutes.add(mTodayPreference.getWalkMin());
		dailyWalkCal.add(mTodayPreference.getWalkCal());

		dailyRunDistance.add(mTodayPreference.getRunDistance());
		dailyRunMinutes.add(mTodayPreference.getRunMin());
		dailyRunCal.add(mTodayPreference.getRunCal());

		imageHeight.add(mTodayPreference.getActivityAllCount());

		while (cursor.moveToNext()) {
			ULog.d(TAG,
					"DATE:" + cursor.getString(0) 
					+ ", all_cnt:" + cursor.getInt(1) 
					+ ", all_dis:" + cursor.getInt(2)
					+ ", all_cal:" + cursor.getInt(3)
					+ ", walk_dis:" + cursor.getInt(4) 
					+ ", walk_tim:" + cursor.getInt(5) 
					+ ", walk_cal:" + cursor.getInt(6) 
					+ ", run_dis:" + cursor.getInt(7) 
					+ ", run_tim:" + cursor.getInt(8) 
					+ ", run_cal:" + cursor.getInt(9));

			// 년도 달라지면 보이게
			String year = cursor.getString(0).substring(0, 4);

			dailyDate.add( cursor.getString(0).substring(5));
			dailyAllCnt.add(cursor.getInt(1));
			dailyAllDistance.add(cursor.getInt(2));
			dailyAllCal.add(cursor.getInt(3));

			dailyWalkDistance.add(cursor.getInt(4));
			dailyWalkMinutes.add(cursor.getInt(5));
			dailyWalkCal.add(cursor.getInt(6));

			dailyRunDistance.add(cursor.getInt(7));
			dailyRunMinutes.add(cursor.getInt(8));
			dailyRunCal.add(cursor.getInt(9));

			imageHeight.add(cursor.getInt(1));
		}
		
		setWeeklyData();
		setMonthlyData();
		
		

		if (UNIT == Constants.DAILY) {
			// // return 단위 + 천단위 콤마 찍은 공통함수 !!!
			// mActivityKmAvg.setText((mTodayPreference.getWalkDistance()+mTodayPreference.getRunDistance())
			// + "km"); //mActivityKmAll
			// mActivityStepAvg.setText(mTodayPreference.getActivityAllCount()
			// +"걸음"); //mActivityStepAll
			// mActivityKcalAvg.setText((mTodayPreference.getWalkCal() +
			// mTodayPreference.getRunCal()) + "kcal"); //mActivityKcalAll
			//
			// mWalkKmAvg.setText(mTodayPreference.getWalkDistance()+"km");
			// mWalkHourAvg.setText((mTodayPreference.getWalkMin()/60)+"시간 " +
			// (mTodayPreference.getWalkMin()%60)+"분");
			// mWalkKcalAvg.setText(mTodayPreference.getWalkCal()+"kcal");
			//
			// mRunKmAvg.setText(mTodayPreference.getRunDistance()+"km");
			// mRunHourAvg.setText((mTodayPreference.getRunMin()/60)+"시간 " +
			// (mTodayPreference.getRunMin()%60)+"분");
			// mRunKcalAvg.setText(mTodayPreference.getRunCal()+"kcal");
		} else {

		}

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(graph_width, 100);
		params.gravity = Gravity.CENTER;
		mPoint.setLayoutParams(params);

		graphLoop = (Gallery) findViewById(R.id.graphLoop);
		graphLoop.setAdapter(new LoopAdapter(this));
		graphLoop.setScaleX(-1.0f);

		graphLoop.setSpacing(10);

		graphLoop.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			}
		});
		graphLoop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

				/*
				 * dailyDate.add("오늘"); dailyAllCnt.add(
				 * mTodayPreference.getActivityAllCount() );
				 * dailyAllDistance.add(
				 * (mTodayPreference.getWalkDistance()+mTodayPreference.
				 * getRunDistance())/1000.0f ); dailyAllCal.add(
				 * (mTodayPreference.getWalkCal() +
				 * mTodayPreference.getRunCal())/1000.0f );
				 * 
				 * dailyWalkDistance.add(
				 * mTodayPreference.getWalkDistance()/1000.0f );
				 * dailyWalkMinutes.add(mTodayPreference.getWalkMin());
				 * dailyWalkCal.add(mTodayPreference.getWalkCal()/1000.0f );
				 * 
				 * dailyRunDistance.add(mTodayPreference.getWalkDistance()/1000.
				 * 0f); dailyRunMinutes.add(mTodayPreference.getRunMin());
				 * dailyRunCal.add(mTodayPreference.getRunCal()/1000.0f );
				 */

				imageHeight.add(mTodayPreference.getActivityAllCount());

				if (UNIT == Constants.DAILY) {
					getActionBar().setTitle(dailyDate.get(pos));

					mActivityKmAvg.setText(convertThird(dailyAllDistance.get(pos)) + "km");
					mActivityStepAvg.setText(dailyAllCnt.get(pos) + "걸음");
					mActivityKcalAvg.setText(convertThird(dailyAllCal.get(pos)) + "Kcal");
					mWalkKmAvg.setText(convertThird(dailyWalkDistance.get(pos)) + "km");
					mWalkHourAvg.setText((dailyWalkMinutes.get(pos) / 60) + "시간 "
							+ (dailyWalkMinutes.get(pos) % 60) + "분");
					mWalkKcalAvg.setText(convertThird(dailyWalkCal.get(pos)) + "Kcal");
					mRunKmAvg.setText(convertThird(dailyRunDistance.get(pos)) + "km");
					mRunHourAvg.setText( (dailyRunMinutes.get(pos) / 60) + "시간 " + (dailyRunMinutes.get(pos) % 60) + "분");
					mRunKcalAvg.setText(convertThird(dailyRunCal.get(pos)) + "Kcal");

				} else if (UNIT == Constants.WEEKLY) {
					getActionBar().setTitle(weeklyDate.get(pos));
					
					mActivityKmAvg.setText(convertThird(weeklyAvgDistance.get(pos)) + "km");
					mActivityStepAvg.setText(weeklyAvgStep.get(pos)+ "걸음");
					mActivityKcalAvg.setText(convertThird(weeklyAvgCal.get(pos)) + "Kcal");
					
					String walk_time = ((weeklyAvgRunMinutes.get(pos)/60)==0? "":(weeklyAvgRunMinutes.get(pos)/60)+"시간 ")+
								(weeklyAvgRunMinutes.get(pos)%60)+"분";
					
					mWalkKmAvg.setText(convertThird(weeklyAvgWalkDistance.get(pos)) + "Km");
					mWalkHourAvg.setText(walk_time);
					mWalkKcalAvg.setText(convertThird(weeklyAvgWalkCal.get(pos))+"Kcal");
					
					String run_time = ((weeklyAvgRunMinutes.get(pos)/60) == 0? "":(weeklyAvgRunMinutes.get(pos)/60)+"시간 ")+
							(weeklyAvgRunMinutes.get(pos)%60)+"분";
					
					mRunKmAvg.setText(convertThird(weeklyAvgRunDistance.get(pos)) + "Km");
					mRunHourAvg.setText(run_time);
					mRunKcalAvg.setText(convertThird(weeklyAvgRunCal.get(pos))+"Kcal");
					
					mActivityKmAll.setText(convertThird(weeklyAllDisatance.get(pos))+"Km");
					mActivityStepAll.setText( Integer.toString(weeklyAllStep.get(pos)));
					mActivityKcalAll.setText(convertThird(weeklyAllCal.get(pos)) +"Kcal");

				} else if (UNIT == Constants.MONTHLY) {
					getActionBar().setTitle(monthlyDate.get(pos));
					
					mActivityKmAvg.setText(convertThird(monthlyAvgDistance.get(pos)) + "km");
					mActivityStepAvg.setText(monthlyAvgStep.get(pos)+ "걸음");
					mActivityKcalAvg.setText(convertThird(monthlyAvgCal.get(pos)) + "Kcal");
					
					String walk_time = ((monthlyAvgRunMinutes.get(pos)/60)==0? "":(monthlyAvgRunMinutes.get(pos)/60)+"시간 ")+
								(monthlyAvgRunMinutes.get(pos)%60)+"분";
					
					mWalkKmAvg.setText(convertThird(monthlyAvgWalkDistance.get(pos)) + "Km");
					mWalkHourAvg.setText(walk_time);
					mWalkKcalAvg.setText(convertThird(monthlyAvgWalkCal.get(pos))+"Kcal");
					
					String run_time = ((monthlyAvgRunMinutes.get(pos)/60) == 0? "":(monthlyAvgRunMinutes.get(pos)/60)+"시간 ")+
							(monthlyAvgRunMinutes.get(pos)%60)+"분";
					
					mRunKmAvg.setText(convertThird(monthlyAvgRunDistance.get(pos)) + "Km");
					mRunHourAvg.setText(run_time);
					mRunKcalAvg.setText(convertThird(monthlyAvgRunCal.get(pos))+"Kcal");
					
					mActivityKmAll.setText(convertThird(monthlyAllDisatance.get(pos))+"Km");
					mActivityStepAll.setText( Integer.toString(monthlyAllStep.get(pos)));
					mActivityKcalAll.setText(convertThird(monthlyAllCal.get(pos)) +"Kcal");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	/**
	 * set 소숫점 두자리
	 * @param number
	 * @return
	 */
	private String convertThird(int number){ 
		String text = String.format("%.2f", (number/1000.0f));
		return text;
	}
	
	
	
	//?????????? 이번주 data에 오늘 data insert
	private void setWeeklyData() {

		Cursor weekly = dbManager.selectWeeklyActivity();
		
		//오늘 데이터도 이번주에 추가 해야함  
//		dailyDate.add("이번주");
//		dailyAllCnt.add(mTodayPreference.getActivityAllCount());
//		dailyAllDistance.add((mTodayPreference.getWalkDistance() + mTodayPreference.getRunDistance()) / 1000.0f);
//		dailyAllCal.add((mTodayPreference.getWalkCal() + mTodayPreference.getRunCal()) / 1000.0f);
//
//		dailyWalkDistance.add(mTodayPreference.getWalkDistance() / 1000.0f);
//		dailyWalkMinutes.add(mTodayPreference.getWalkMin());
//		dailyWalkCal.add(mTodayPreference.getWalkCal() / 1000.0f);
//
//		dailyRunDistance.add(mTodayPreference.getWalkDistance() / 1000.0f);
//		dailyRunMinutes.add(mTodayPreference.getRunMin());
//		dailyRunCal.add(mTodayPreference.getRunCal() / 1000.0f);
//
//		imageHeight.add(mTodayPreference.getActivityAllCount());
		
		
		if(weekly.getCount() == 0){
			weeklyDate.add("이번주");
			
			weeklyAvgDistance.add( (mTodayPreference.getWalkDistance() + mTodayPreference.getRunDistance())  );
			weeklyAvgStep.add(  mTodayPreference.getActivityAllCount() );
			weeklyAvgCal.add( (mTodayPreference.getWalkCal() + mTodayPreference.getRunCal()) );
			
			weeklyAvgWalkDistance.add( mTodayPreference.getWalkDistance() );   //avg_walk_dist
			weeklyAvgWalkMinute.add( mTodayPreference.getWalkMin() );
			weeklyAvgWalkCal.add( mTodayPreference.getWalkCal() );
			
			weeklyAvgRunDistance.add( mTodayPreference.getRunDistance() );
			weeklyAvgRunMinutes.add( mTodayPreference.getRunMin() );
			weeklyAvgRunCal.add( mTodayPreference.getRunCal() );
			                      
			weeklyAvgDayCount.add( 1 );     
			                      
			weeklyAllDisatance.add( (mTodayPreference.getWalkDistance() + mTodayPreference.getRunDistance()) );          
			weeklyAllStep.add( mTodayPreference.getActivityAllCount() );         
			weeklyAllCal.add( (mTodayPreference.getWalkCal() + mTodayPreference.getRunCal()) );         
			
			weeklyImageHeight.add(mTodayPreference.getActivityAllCount());
		}
		
		while (weekly.moveToNext()) {
			ULog.d(TAG,
				"MONTH DATE:" + weekly.getString(0) + "~" + weekly.getString(1)+
				", day_count:" + weekly.getInt(8) + 
				", avg_walk_dist:" + weekly.getInt(2)
				+ ", avg_walk_time:" + weekly.getInt(3) 
				+ ", avg_walk_cal:" + weekly.getInt(4) 
				+ ", avg_run_dist:" + weekly.getInt(5) 
				+ ", avg_run_time:" + weekly.getInt(6) 
				+ ", avg_run_cal:" + weekly.getInt(7) 
				+ ", total_dist:" + weekly.getInt(9) 
				+ ", total_time:" + weekly.getInt(10) 
				+ ", total_cal:" + weekly.getInt(11) 
				+ ", avg_total_dist:" + weekly.getInt(12) 
				+ ", avg_total_step:" + weekly.getInt(13)
				+ ", avg_total_cal:" + weekly.getInt(14) );

			// 년도 달라지면 보이게
			String start_date  = weekly.getString(0).substring(5) ;
			String end_date = weekly.getString(1).substring(5);
		
			weeklyDate.add(start_date+"\n"+end_date);
			
			weeklyAvgDistance.add( weekly.getInt(12) );
			weeklyAvgStep.add( weekly.getInt(13) );
			weeklyAvgCal.add( weekly.getInt(14) );
			
			weeklyAvgWalkDistance.add( weekly.getInt(2) );   //avg_walk_dist
			weeklyAvgWalkMinute.add( weekly.getInt(3) );
			weeklyAvgWalkCal.add( weekly.getInt(4) );
			weeklyAvgRunDistance.add( weekly.getInt(5) );
			weeklyAvgRunMinutes.add( weekly.getInt(6) );
			weeklyAvgRunCal.add( weekly.getInt(7) );
			                      
			weeklyAvgDayCount.add( weekly.getInt(8) );     
			                      
			weeklyAllDisatance.add( weekly.getInt(9) );          
			weeklyAllStep.add( weekly.getInt(10) );         
			weeklyAllCal.add( weekly.getInt(11) );         
			
			weeklyImageHeight.add( weekly.getInt(13) );
		}
		
		
	}
	
	
	private void setMonthlyData() {

		Cursor monthly = dbManager.selectMonthlyActivity();
		
		//오늘 데이터도 이번주에 추가 해야함  
//		dailyDate.add("이번주");
//		dailyAllCnt.add(mTodayPreference.getActivityAllCount());
//		dailyAllDistance.add((mTodayPreference.getWalkDistance() + mTodayPreference.getRunDistance()) / 1000.0f);
//		dailyAllCal.add((mTodayPreference.getWalkCal() + mTodayPreference.getRunCal()) / 1000.0f);
//
//		dailyWalkDistance.add(mTodayPreference.getWalkDistance() / 1000.0f);
//		dailyWalkMinutes.add(mTodayPreference.getWalkMin());
//		dailyWalkCal.add(mTodayPreference.getWalkCal() / 1000.0f);
//
//		dailyRunDistance.add(mTodayPreference.getWalkDistance() / 1000.0f);
//		dailyRunMinutes.add(mTodayPreference.getRunMin());
//		dailyRunCal.add(mTodayPreference.getRunCal() / 1000.0f);
//
//		imageHeight.add(mTodayPreference.getActivityAllCount());
		
		
		if(monthly.getCount() == 0){
			monthlyDate.add("이번달");
			
			monthlyAvgDistance.add( (mTodayPreference.getWalkDistance() + mTodayPreference.getRunDistance())  );
			monthlyAvgStep.add(  mTodayPreference.getActivityAllCount() );
			monthlyAvgCal.add( (mTodayPreference.getWalkCal() + mTodayPreference.getRunCal()) );
			
			monthlyAvgWalkDistance.add( mTodayPreference.getWalkDistance() );   //avg_walk_dist
			monthlyAvgWalkMinute.add( mTodayPreference.getWalkMin() );
			monthlyAvgWalkCal.add( mTodayPreference.getWalkCal() );
			
			monthlyAvgRunDistance.add( mTodayPreference.getRunDistance() );
			monthlyAvgRunMinutes.add( mTodayPreference.getRunMin() );
			monthlyAvgRunCal.add( mTodayPreference.getRunCal() );
			                  
			monthlyAvgDayCount.add( 1 );     
			                  
			monthlyAllDisatance.add( (mTodayPreference.getWalkDistance() + mTodayPreference.getRunDistance()) );          
			monthlyAllStep.add( mTodayPreference.getActivityAllCount() );         
			monthlyAllCal.add( (mTodayPreference.getWalkCal() + mTodayPreference.getRunCal()) );         
			
			monthlyImageHeight.add(mTodayPreference.getActivityAllCount());
		}
		
		while (monthly.moveToNext()) {
			String[] date  = monthly.getString(0).split("-");
			
			ULog.d(TAG,
				"DATE:" + date[1]
				+ ", day_count:" + monthly.getInt(7)
				+ ", avg_walk_dist:" + monthly.getInt(1)
				+ ", avg_walk_time:" + monthly.getInt(2)
				+ ", avg_walk_cal:" + monthly.getInt(3) 
				+ ", avg_run_dist:" + monthly.getInt(4) 
				+ ", avg_run_time:" + monthly.getInt(5) 
				+ ", avg_run_cal:" + monthly.getInt(6)  
				+ ", total_dist:" + monthly.getInt(8) 
				+ ", total_step:" + monthly.getInt(9) 
				+ ", total_cal:" + monthly.getInt(10) 
				+ ", avg_total_dist:" + monthly.getInt(11) 
				+ ", avg_total_step:" + monthly.getInt(12)
				+ ", avg_total_cal:" + monthly.getInt(13) );

			// 년도 달라지면 보이게
		
			monthlyDate.add(date[1]+"월");
			
			monthlyAvgDistance.add( monthly.getInt(11) );
			monthlyAvgStep.add( monthly.getInt(12) );
			monthlyAvgCal.add( monthly.getInt(13) );
			
			monthlyAvgWalkDistance.add( monthly.getInt(1) );   //avg_walk_dist
			monthlyAvgWalkMinute.add( monthly.getInt(2) );
			monthlyAvgWalkCal.add( monthly.getInt(3) );
			monthlyAvgRunDistance.add( monthly.getInt(4) );
			monthlyAvgRunMinutes.add( monthly.getInt(5) );
			monthlyAvgRunCal.add( monthly.getInt(6) );
			                      
			monthlyAvgDayCount.add( monthly.getInt(7) );     
			                      
			monthlyAllDisatance.add( monthly.getInt(8) );          
			monthlyAllStep.add( monthly.getInt(9) );         
			monthlyAllCal.add( monthly.getInt(10) );         
			
			monthlyImageHeight.add( monthly.getInt(12) );
		}
		
		
	}
	
	// class LoopImageAdapter extends BaseAdapter {
	// private Context context;
	//
	// public LoopImageAdapter(Context context) {
	// this.context = context;
	// }
	//
	//
	//
	// public Object getItem(int position) {
	// return position;
	// }
	//
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// public int getCount() {
	// return Integer.MAX_VALUE;
	// }
	//
	// public View getView(int position, View convertView, ViewGroup parent) {
	// ImageView imageView = new ImageView(context);
	// imageView.setImageResource(R.drawable.activity_graph_point);
	// imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	// imageView
	// .setLayoutParams(new Gallery.LayoutParams(graph_width,
	// imageHeight[position % imageHeight.length]));
	// return imageView;
	// }
	//
	// }

	class LoopAdapter extends BaseAdapter {
		private Context context;

		public LoopAdapter(Context context) {
			this.context = context;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public int getCount() {
			int cnt = 0;
			if (UNIT == Constants.DAILY) {
				cnt = dailyDate.size();
			} else if (UNIT == Constants.WEEKLY) {
				cnt = weeklyDate.size();
			} else if (UNIT == Constants.MONTHLY) {
				cnt = monthlyDate.size();
			}
			return cnt;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout l = new LinearLayout(context);
			// LinearLayout.LayoutParams params =
			// (LinearLayout.LayoutParams)l.getLayoutParams();
			// params.gravity = Gravity.CENTER_HORIZONTAL;
			// l.setLayoutParams(params);
			l.setOrientation(LinearLayout.VERTICAL);

			ImageView imageView = new ImageView(context);
			imageView.setImageResource(R.drawable.activity_graph_point);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			if(UNIT == Constants.WEEKLY ){
				imageView.setLayoutParams(new Gallery.LayoutParams(graph_width, weeklyImageHeight.get(position) / 30));
			}else if(UNIT == Constants.DAILY){
				imageView.setLayoutParams(new Gallery.LayoutParams(graph_width, imageHeight.get(position) / 30));
			}else if(UNIT == Constants.MONTHLY ){
				imageView.setLayoutParams(new Gallery.LayoutParams(graph_width, monthlyImageHeight.get(position) / 30));
			}
		

			TextView tv = new TextView(context);
			tv.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			if (UNIT == Constants.DAILY) {
				tv.setText(dailyDate.get(position));
			} else if (UNIT == Constants.WEEKLY) {
				tv.setText(weeklyDate.get(position));
			} else if (UNIT == Constants.MONTHLY) {
				tv.setText(monthlyDate.get(position));
			}
			// tv.setGravity(Gravity.CENTER);
			tv.setScaleX(-1.0f);
			tv.setTextColor(Color.WHITE);
			tv.setTextSize(8);

			l.addView(imageView);
			l.addView(tv);

			return l;

		}
	}

	private void setDaily() {
		UNIT = Constants.DAILY;
		graph_width = 100;

		 getActionBar().setTitle("오늘");

		// LinearLayout.LayoutParams params = new
		// LinearLayout.LayoutParams(graph_width, 100);
		// params.gravity = Gravity.CENTER;
		// mPoint.setLayoutParams(params);
		((BaseAdapter) graphLoop.getAdapter()).notifyDataSetChanged();
		graphLoop.setSelection(0);
		mPlusBtn.setEnabled(false);
		mMinusBtn.setEnabled(true);
		setUIText();
	}

	private void setWeekly() {
		UNIT = Constants.WEEKLY;
		graph_width = 140;
		getActionBar().setTitle("이번주");

		// galleryLoop.invalidate();
		((BaseAdapter) graphLoop.getAdapter()).notifyDataSetChanged();
		graphLoop.setSelection(0);
		mPlusBtn.setEnabled(true);
		mMinusBtn.setEnabled(true);
		
		setUIText();
	}

	private void setMonthly() {
		UNIT = Constants.MONTHLY;
		graph_width = 180;

		 getActionBar().setTitle("이번달");

		// galleryLoop.invalidate();
		((BaseAdapter) graphLoop.getAdapter()).notifyDataSetChanged();
		graphLoop.setSelection(0);
		mMinusBtn.setEnabled(false);
		mPlusBtn.setEnabled(true);
		setUIText();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.minus_btn:
			if (UNIT == Constants.DAILY) {
				setWeekly();
			} else if (UNIT == Constants.WEEKLY) {
				setMonthly();
			}
			;

			break;
		case R.id.sleep_btn:
			Intent sleep = new Intent(getApplicationContext(), SleepDetailPartActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt(Constants.DAY_UNIT, UNIT);
			sleep.putExtras(bundle);
			sleep.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(sleep);
			finish();
			break;
		case R.id.plus_btn:
			if (UNIT == Constants.WEEKLY) {
				setDaily();

			} else if (UNIT == Constants.MONTHLY) {
				setWeekly();
			}
			;

			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent();
		this.setResult(RESULT_OK, intent);

		finish();
	}
}
