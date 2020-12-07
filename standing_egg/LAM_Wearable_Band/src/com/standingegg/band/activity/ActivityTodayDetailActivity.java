package com.standingegg.band.activity;

import java.util.ArrayList;
import java.util.Date;

import com.standingegg.band.R;
import com.standingegg.band.sleeping.SleepTodayDetailActivity;
import com.standingegg.band.util.TodayPreferences;
import com.standingegg.band.util.ULog;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Gallery.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActivityTodayDetailActivity extends Activity implements OnClickListener {
	private static final String TAG = "ActivityTodayDetailActivity";
	Context mContext;
	ProgressBar mProgress;
	LinearLayout lchart;
	HorizontalScrollView mHorizentalScrollView;

	TextView mActivityAllKmTv, mActivityAllCntTv, mActivityAllKcalTv;
	TextView mWalkKmTv, mWalkHourTv, mWalkKcalTv;
	TextView mRunKmTv, mRunHourTv, mRunKcalTv;
	ImageButton mSleepTodayBtn;

	private int mActivityAllCount = 0;
	private Float mActivityAllKm = 0.00f;

	private ArrayList<Integer> mTodayActCntTimeHour; // graph 표시 //시간
	private ArrayList<Integer> mTodayActCntTimeMin; // graph 표시 //분 앞자리

	private ArrayList<Integer> mTodayActCnt; // graph 표시 - ActivityTodayDetail
	private ArrayList<Integer> mTodayActCntTime; // graph 표시
	private Float mWalkDistance = 0.0f; // ActivityTodayDetail - 걸은 거리
	// private int mWalkHour = 0; // ActivityTodayDetail - 걸은 시간
	private int mWalkMin = 0; // ActivityTodayDetail - 걸은 분..? 왜나눴지...?
	private Float mWalkKcal = 0.0f; // ActivityTodayDetail - 걸음으로 소모한 cal량

	private Float mRunDistance = 0.0f; // ActivityTodayDetail - 뛴 거리
	// private int mRunHour = 0; // ActivityTodayDetail - 뛴 시간
	private int mRunMin = 0; // ActivityTodayDetail - 뛴 분
	private Float mRunKcal = 0.0f; // ActivityTodayDetail - 뛰어선 소모한 cal량

	private TodayPreferences mTodayPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_today);
		mContext = this;
		mTodayPreferences = TodayPreferences.getInstance(mContext);

		mTodayActCnt = new ArrayList<Integer>();
		mTodayActCntTimeHour = new ArrayList<Integer>();
		mTodayActCntTimeMin = new ArrayList<Integer>();

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(android.R.color.transparent);

		setDate();

		setComponent();
		
		
		
	}

	private void setDate() {
		mActivityAllCount = mTodayPreferences.getActivityAllCount();
		// mTodayActCnt = mTodayPreferences.getTodayActCnt();
		// mTodayActCntTime = mTodayPreferences.
		mWalkDistance = mTodayPreferences.getWalkDistance()/1000.0f;
		// mWalkHour = mTodayPreferences.getWalkHour();
		mWalkMin = mTodayPreferences.getWalkMin();
		mWalkKcal = mTodayPreferences.getWalkCal()/1000.0f;

		mRunDistance = mTodayPreferences.getRunDistance()/1000.0f;
		// mRunHour = mTodayPreferences.getRunHour();
		mRunMin = mTodayPreferences.getRunMin();
		mRunKcal = mTodayPreferences.getRunCal()/1000.0f;

		if(mTodayPreferences.getTodayActCnt() != null){
			mTodayActCnt = mTodayPreferences.getTodayActCnt();
			mTodayActCntTimeHour = mTodayPreferences.getTodayActCntTimeHour();
			mTodayActCntTimeMin = mTodayPreferences.getTodayActCntTimeMin();
		}
		

		for (int i = 0; i < mTodayActCnt.size(); i++) {
			ULog.v(TAG, "mTodayActCnt:" + mTodayActCnt.get(i));
		}
		for (int i = 0; i < mTodayActCntTimeHour.size(); i++) {
			ULog.v(TAG, "mTodayActCntTimeHour:" + mTodayActCntTimeHour.get(i));
		}
		for (int i = 0; i < mTodayActCntTimeMin.size(); i++) {
			ULog.v(TAG, "mTodayActCntTimeMin:" + mTodayActCntTimeMin.get(i));
		}
	}

	public void setComponent() {
		// mProgress = (ProgressBar) findViewById(R.id.progressBar);
		lchart = (LinearLayout) findViewById(R.id.ichart);
		mHorizentalScrollView = (HorizontalScrollView) findViewById(R.id.graph_view);

		mActivityAllKmTv = (TextView) findViewById(R.id.activity_km_all);
		mActivityAllCntTv = (TextView) findViewById(R.id.activity_cnt_all);
		mActivityAllKcalTv = (TextView) findViewById(R.id.activity_kcal_all);
		mWalkKmTv = (TextView) findViewById(R.id.walk_km);
		mWalkHourTv = (TextView) findViewById(R.id.walk_h);
		mWalkKcalTv = (TextView) findViewById(R.id.walk_kcal);
		mRunKmTv = (TextView) findViewById(R.id.run_km);
		mRunHourTv = (TextView) findViewById(R.id.run_h);
		mRunKcalTv = (TextView) findViewById(R.id.run_kcal);
		mSleepTodayBtn = (ImageButton) findViewById(R.id.sleep_today_btn);
		
		mSleepTodayBtn.setOnClickListener(this);
		updateUI();

		int height[] = new int[mTodayActCnt.size()];
		int ab[] = new int[12];
		int a = 0;

		for (int i = 0; i < mTodayActCnt.size(); i++) {
			height[i] = mTodayActCnt.get(i);
		}
		ULog.d(" mTodayActCnt.size(), "+ mTodayActCnt.size());
		
		
		
		ULog.d(" height.length, "+ height.length);

		for (int j = 0; j < height.length; j++) {
			// ULog.d("배열 번호 : " + (j - a));
			ab[j - a] = (height[j] * 2);
			// ULog.d("배열 값 : " + ab[j - a]);
			if (j != 0 && (j + 1) % 12 == 0) {
				a = a + 12;
				drawChart(false, ab);
				ab = new int[12];
			}

		}

		int[] t = new int[(height.length) % 12];
		ULog.e(TAG, "heigt length ==> " + (height.length) % 12);
		for (int i = 0; i < (height.length) % 12; i++) {
			ULog.e(TAG, "값 => " + t[i]);
			
			if(height.length == 1 && t[i] == 0 ) return;
			
			t[i] = height[height.length - ((height.length) % 12) + (i)];
		}

		drawChart(true, t);
		
		mHorizentalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

	}

	int a2 = 0;

	private void drawChart(boolean endTime, int[] ab) {
		int color = Color.WHITE;

		// graph 12개묶음 layout
		LinearLayout container12 = new LinearLayout(this);

		// 2시간 12개 / 10분마다 data set /
		for (int c = 0; c < ab.length; c++) {
			container12.setBackgroundColor(Color.TRANSPARENT);

			container12.setLayoutParams(
					new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			LinearLayout.LayoutParams p12 = (LinearLayout.LayoutParams) container12.getLayoutParams();

			// p12.setMargins(10, 0, 0, 0);
			container12.setOrientation(LinearLayout.HORIZONTAL);
			container12.setGravity(Gravity.BOTTOM);
			container12.setLayoutParams(p12);

			View grapgh_bar = new View(this); // 실제 그래프 막대
			grapgh_bar.setBackgroundColor(color);
			if(ab[c] > 500){
				ab[c] = 500;
			}
			
			grapgh_bar.setLayoutParams(new LinearLayout.LayoutParams(10, (ab[c])));

			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) grapgh_bar.getLayoutParams();
			params.setMargins(3, 0, 0, 0);
			grapgh_bar.setLayoutParams(params);

			container12.addView(grapgh_bar);
		}

		lchart.setOrientation(LinearLayout.HORIZONTAL);

		// 그래프 전체 !
		LinearLayout allContain = new LinearLayout(this);
		allContain.setBackgroundColor(Color.TRANSPARENT);

		allContain.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		LinearLayout.LayoutParams p1 = (LinearLayout.LayoutParams) allContain.getLayoutParams();
		allContain.setGravity(Gravity.LEFT);
		// p1.setMargins(3, 0, 0, 0);
		allContain.setOrientation(LinearLayout.VERTICAL);
		allContain.setLayoutParams(p1);

		TextView timeText = new TextView(this);
		timeText.setBackgroundColor(Color.TRANSPARENT);

		Date d = new Date();
		String h = Integer.toString(d.getHours());
		String m = Integer.toString(d.getMinutes());

		if (endTime) {
			ULog.d(TAG, "Set Time now");
			timeText.setGravity(Gravity.RIGHT);
			timeText.setText((h.length() == 1 ? "0" + h : h) + ":" + (m.length() == 1 ? "0" + m : m)); // 00:00 부터 ~ 현재 시간  set
		} else {

			timeText.setText(a2 + ":00"); // 00:00 부터 ~ 실제 시간 set
		}

		timeText.setTextColor(color);
		timeText.setTextSize(8);
		a2 = a2 + 2;

		timeText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		View view3 = new View(this); // 실제 그래프 막대
		view3.setBackgroundColor(Color.RED);
		view3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 5));

		allContain.addView(container12);

		allContain.addView(view3);

		allContain.addView(timeText);
		lchart.addView(allContain);
	}

	public void updateUI() {
		mActivityAllKmTv.setText(String.format("%.2f", (mWalkDistance + mRunDistance)) + " km");
		mActivityAllCntTv.setText(mActivityAllCount + " 걸음");
		mActivityAllKcalTv.setText(String.format("%.2f", (mWalkKcal + mRunKcal)) + " Kcal");
		mWalkKmTv.setText(String.format("%.2f", mWalkDistance) + " km");
		mWalkHourTv.setText((mWalkMin / 60) + " 시간   " + (mWalkMin % 60) + " 분");
		mWalkKcalTv.setText(String.format("%.2f", mWalkKcal) + " Kcal");
		mRunKmTv.setText(String.format("%.2f", mRunDistance) + " km");
		mRunHourTv.setText((mRunMin / 60) + " 시간   " + (mRunMin % 60) + " 분");
		mRunKcalTv.setText(String.format("%.2f", mRunKcal) + " Kcal");
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sleep_today_btn:
			Intent sleep = new Intent(getApplicationContext(), SleepTodayDetailActivity.class);
			sleep.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(sleep);
			finish();
			break;

		default:
			break;
		}
		
	}

}
