
package com.standingegg.band.sleeping;

import java.util.ArrayList;

import com.standingegg.band.R;
import com.standingegg.band.activity.ActivityTodayDetailActivity;
import com.standingegg.band.util.TodayPreferences;
import com.standingegg.band.util.ULog;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Gallery.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SleepTodayDetailActivity extends Activity implements OnClickListener{
	String TAG = Thumbnails.class.getSimpleName();
	Context mContext;
	LinearLayout lchart;
	HorizontalScrollView mHorizentalScrollView;

	TextView mSleepHourTv, mSleepDeepTv, mSleepLowTv;
	TextView mSleepStartTv, mWakeupTv, mWakingTv;
	
	ImageButton mActTodayBtn;

	// SleepTodayDetail
	// int mDeepSleepHour = 0; // 숙면 시간
	int mDeepSleepMin = 0; // 숙면 분

	// int mLowSleepHour = 0; // 얕은 잠 시간
	int mLowSleepMin = 0; // 얕은 잠 분

	int mAwakeMin = 0; // 깨어있던시간

	String mSleepTime = "00:00"; // 잠든 시간
	String mWakeUpTime = "00:00";// 깬 시간
	int sleepWidth = 68;

	private ArrayList<Integer> mTodaySleepHeight; // graph 표시 height/ 숙면,얕은잠,깬시간
													// flag
	private ArrayList<Integer> mTodaySleepCnt; // graph 표시 실제 수면시간(진행시간)

	private TodayPreferences mTodayPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = this;
		mTodayPreferences = TodayPreferences.getInstance(mContext);

		setContentView(R.layout.sleep_today);
		mTodaySleepHeight = new ArrayList<Integer>();
		mTodaySleepCnt = new ArrayList<Integer>();
		
		setDate();
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(android.R.color.transparent);
		actionBar.setTitle("TODAY");
		setComponent();
	}

	private void setDate() {
		// mDeepSleepHour = mTodayPreferences.getDeepSleepHour();
		mDeepSleepMin = mTodayPreferences.getDeepSleepMin();

		// mLowSleepHour = mTodayPreferences.getLowSleepHour();
		mLowSleepMin = mTodayPreferences.getDeepSleepMin();
		mAwakeMin = mTodayPreferences.getAwakeMin();

		mSleepTime = mTodayPreferences.getSleepTime();
		mWakeUpTime = mTodayPreferences.getWakeUpTime();
		if("".equals(mSleepTime) || mSleepTime == null){
			mSleepTime = "00:00";
		}else{
			mSleepTime = mSleepTime.replace(":", "");
			mSleepTime = mSleepTime.substring(0,2)+":" +mSleepTime.substring(2);
		}
		
		if("".equals(mWakeUpTime) || mWakeUpTime == null){
			mWakeUpTime = "00:00";
		}else{
			mWakeUpTime = mWakeUpTime.replace(":", "");
			mWakeUpTime = mWakeUpTime.substring(0,2)+":" +mWakeUpTime.substring(2);
		}
		

		ULog.e(TAG, "mSleepTime, " + mSleepTime);
		ULog.e(TAG, "mWakeUpTime, " + mWakeUpTime);
		if(mTodayPreferences.getTodaySleepHeight() != null){
			mTodaySleepHeight = mTodayPreferences.getTodaySleepHeight();
			mTodaySleepCnt = mTodayPreferences.getTodaySleepCnt();
		}
		
	}

	public void setComponent() {
		// mProgress = (ProgressBar) findViewById(R.id.progressBar);
		lchart = (LinearLayout) findViewById(R.id.ichart);
		mHorizentalScrollView = (HorizontalScrollView) findViewById(R.id.graph_view);

		mSleepHourTv = (TextView) findViewById(R.id.sleep_h);
		mSleepDeepTv = (TextView) findViewById(R.id.sleep_deep);
		mSleepLowTv = (TextView) findViewById(R.id.sleep_low);
		mSleepStartTv = (TextView) findViewById(R.id.sleep_start);
		mWakeupTv = (TextView) findViewById(R.id.sleep_wakeup);
		mWakingTv = (TextView) findViewById(R.id.sleep_waking);
		mActTodayBtn = (ImageButton) findViewById(R.id.activity_today_btn);
		
		mActTodayBtn.setOnClickListener(this);
		ULog.d("mHorizentalScrollView.getMeasuredWidth()=>" + mHorizentalScrollView.getMeasuredWidth());
		ULog.d("mHorizentalScrollView.getWidth()=>" + mHorizentalScrollView.getWidth());

		updateUI();

		int height[] = { 320, 320, 400, 400, 320, 320, 320, 320, 400, 400, 321, 320, 400, 320, 320, 320, 320 };

		int a = 0;
		for (int j = 0; j < mTodaySleepHeight.size(); j++) {
			drawChart(j, mTodaySleepHeight.get(j), mTodaySleepCnt.get(j));
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		ULog.e("Layout Width - ", String.valueOf(mHorizentalScrollView.getWidth()));
		sleepWidth = (mHorizentalScrollView.getWidth() / (mTodaySleepHeight.size() + 1));
		ULog.e("BAR Width = " + sleepWidth);
	}

	private void updateUI() {
		int allMin = mDeepSleepMin + mLowSleepMin;

		mSleepHourTv.setText((allMin / 60) + " 시간  " + (allMin % 60) + " 분");
		mSleepDeepTv.setText((mDeepSleepMin / 60) + " 시간  " + (mDeepSleepMin % 60) + " 분");
		mSleepLowTv.setText((mLowSleepMin / 60) + " 시간   " + (mLowSleepMin % 60) + " 분");
		mSleepStartTv.setText(mSleepTime);
		mWakeupTv.setText(mWakeUpTime);
		mWakingTv.setText(mAwakeMin + "분");

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

	int a2 = 0;

	
	
	//(그래프 가로 길이 = width/전체 수면 시간) * 해당 sleep(막대) 진행 시간 
	
	private void drawChart(int i, int ab,int sleepingtime) {
		int color = Color.WHITE;
		LinearLayout aa = new LinearLayout(this);

		lchart.setOrientation(LinearLayout.HORIZONTAL);

		for (int j = 0; j < 1; j++) {

			// 2시간 12개 / 10분마다 data set /
			aa.setBackgroundColor(Color.TRANSPARENT);
			aa.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			LinearLayout.LayoutParams p12 = (LinearLayout.LayoutParams) aa.getLayoutParams();
			p12.setMargins(0, 0, 0, 0);
			aa.setOrientation(LinearLayout.HORIZONTAL);
			aa.setGravity(Gravity.BOTTOM);
			aa.setLayoutParams(p12);

			View view = new View(this);

			if (ab > 350) {
				view.setBackgroundResource(R.color.graph_sleep_low);
			} else {
				view.setBackgroundResource(R.color.graph_sleep_deep);
			}

			if (ab == 321) {
				view.setBackgroundResource(R.color.graph_sleep_wake);
			}
			switch (ab) {
			case 101:
				view.setBackgroundResource(R.color.blue2);
				break;
			case 202:
				view.setBackgroundResource(R.color.blue);
				break;
			default:
				view.setBackgroundResource(R.color.black);
				break;
			}
			
			

			view.setLayoutParams(new LinearLayout.LayoutParams(sleepWidth, ab));

			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
			params.setMargins(0, 0, 0, 0);
			view.setLayoutParams(params);
			aa.addView(view);

			LinearLayout a = new LinearLayout(this);
			a.setBackgroundColor(Color.TRANSPARENT);

			a.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			LinearLayout.LayoutParams p1 = (LinearLayout.LayoutParams) a.getLayoutParams();
			a.setGravity(Gravity.LEFT);
			p1.setMargins(0, 0, 0, 0);
			a.setOrientation(LinearLayout.VERTICAL);
			a.setLayoutParams(p1);

			TextView view2 = new TextView(this);
			view2.setBackgroundColor(Color.TRANSPARENT);
			view2.setTextColor(color);
			view2.setTextSize(8);

			if (i == 0) {
				view2.setText(mSleepTime);
			}

			if (i == mTodaySleepHeight.size() - 1) {
				view2.setText(mWakeUpTime);
			}
			// view2.setText(a2 + ":00"); // 00:00 부터 ~ 실제 시간 set
			// view2.setTextColor(color);
			// a2 = a2 + 2;

			view2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			View view3 = new View(this); // 실제 그래프 막대
			
			view3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 5));

			a.addView(aa);
			a.addView(view3);
			a.addView(view2);
			lchart.addView(a);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.activity_today_btn:
			Intent sleep = new Intent(getApplicationContext(), ActivityTodayDetailActivity.class);
			sleep.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(sleep);
			finish();
			break;

		default:
			break;
		}
		
	}

}
