
package com.standingegg.band.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.standingegg.band.IFragmentListener;
import com.standingegg.band.R;
import com.standingegg.band.contents.CircularProgressBar;
import com.standingegg.band.contents.DBManager;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.TodayPreferences;
import com.standingegg.band.util.ULog;

import java.util.ArrayList;

public class ActivityRunFragment extends Fragment {
	private Context mContext = null;
	private IFragmentListener mFragmentListener = null;
	private Handler mActivityHandler = null;
	private DBManager dbManager;

	// packet show
	// TextView mTextChat;
	TextView mWalkCntTv;
	TextView mKmAllCnt;
	TextView mKcalAllCnt;

	TextView mTotalDistance;
	TextView mToTalStep;
	TextView mToTalCalorie;

	TextView mWalkTotalDistance;
	TextView mWalkToTalStep;
	TextView mWalkToTalCalorie;

	ImageView mUserImg1, mUserImg2, mUserImg3;

	WebView mWebview, mPieChartWebView;

	private Preferences mPreference = null;
	private TodayPreferences mTodayPreference = null;

	private int todayWalkCnt, todayDis,todayCal ;

	float activityGoal = 1000.0f;

	public ActivityRunFragment() {};

	public ActivityRunFragment(Context c, IFragmentListener l, Handler h) {
		mContext = c;
		mFragmentListener = l;
		mActivityHandler = h;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_run, container, false);

		setContents(v);
		mPreference = Preferences.getInstance(mContext);
		activityGoal =  mPreference.getUserActivityGoal()< 1000? 1000.0f : (float)mPreference.getUserActivityGoal();
		mTodayPreference = TodayPreferences.getInstance(mContext);
		dbManager = new DBManager(mContext, "seband.db", null, 1);

		setData();
		return v;
	}

	public void setData() {
		todayWalkCnt = mTodayPreference.getActivityRunAllCount();
		todayDis = mTodayPreference.getRunDistance();
		todayCal = mTodayPreference.getRunCal();
		
		/*mWalkCntTv.setText(Integer.toString(todayWalkCnt));
		mKmAllCnt.setText(Float.toString(todayDis/1000.0f));
		mKcalAllCnt.setText(Float.toString(todayCal/1000.0f));

		mTotalDistance.setText(Float.toString(todayDis/1000.0f));
		mToTalStep.setText(Integer.toString(todayWalkCnt));
		mToTalCalorie.setText(Float.toString(todayCal/1000.0f));*/
		
		setWalking(Integer.toString(todayWalkCnt), todayDis, todayCal);
	}

	private void setContents(View v) {
		mWalkCntTv = (TextView) v.findViewById(R.id.run_cnt_tv);
		mKmAllCnt = (TextView) v.findViewById(R.id.km_all);
		mKcalAllCnt = (TextView) v.findViewById(R.id.cal_all);


		mTotalDistance = (TextView) v.findViewById(R.id.distanceText);
		mToTalStep = (TextView) v.findViewById(R.id.stepText);
		mToTalCalorie = (TextView) v.findViewById(R.id.calorieText);

		mWalkTotalDistance = (TextView) v.findViewById(R.id.walkDistanceText);
		mWalkToTalStep = (TextView) v.findViewById(R.id.walkStepText);
		mWalkToTalCalorie = (TextView) v.findViewById(R.id.walkCalorieText);

		mUserImg1 = (ImageView) v.findViewById(R.id.user1);
		mUserImg2 = (ImageView) v.findViewById(R.id.user2);
		mUserImg3 = (ImageView) v.findViewById(R.id.user3);


		mWebview = (WebView) v.findViewById(R.id.chart_webview);
		mPieChartWebView = (WebView) v.findViewById(R.id.chart_pie_webview);
		chartView();

	}


	/**
	 * set 활동 main UI walk, progress cnt
	 * 
	 * @param cnt
	 * @param mActivityAllM
	 * @param mActivityAllCal
	 */
	public void setWalking(String cnt, int mActivityAllM, int mActivityAllCal) {
		if (cnt == null || cnt.length() < 1)
			return;


		if(false) {
			mWalkCntTv.setText(cnt);
			mKmAllCnt.setText(String.format("%.2f", (mActivityAllM/1000.0f)));
			mKcalAllCnt.setText(String.format("%.2f", (mActivityAllCal/1000.0f)));

			mTotalDistance.setText(String.format("%.2f", (mActivityAllM/1000.0f)));
			mToTalStep.setText(cnt);
			mToTalCalorie.setText(String.format("%.2f", (mActivityAllCal/1000.0f)));
			return;
		}



		ULog.d("setRunning", "getData : " +  mTodayPreference.getDataDate());
		Cursor cursor = dbManager.dailyRunActDate(mPreference.getUserId(), mTodayPreference.getDataDate());
		final int[] chartArrayValue = new int[12];
		int[] disArrayValue = new int[12];
		int[] carArrayValue = new int[12];

		while (cursor.moveToNext()) {
			if(cursor.getInt(1)%2 == 1) {
				chartArrayValue[(((cursor.getInt(1))+1)/2)-1] = cursor.getInt(2);
				disArrayValue[(((cursor.getInt(1))+1)/2)-1] = cursor.getInt(3);
				carArrayValue[(((cursor.getInt(1))+1)/2)-1] = cursor.getInt(4);
			}else {
				chartArrayValue[(((cursor.getInt(1)))/2)-1] = chartArrayValue[(((cursor.getInt(1)))/2)-1] + cursor.getInt(2);
				disArrayValue[(((cursor.getInt(1)))/2)-1] = disArrayValue[(((cursor.getInt(1)))/2)-1] + cursor.getInt(3);
				carArrayValue[(((cursor.getInt(1)))/2)-1] = carArrayValue[(((cursor.getInt(1)))/2)-1] + cursor.getInt(4);
			}
			ULog.d("activityRun", "getPosition : " +cursor.getPosition() +" //date : " + cursor.getString(0) + "// hour : " + cursor.getInt(1) + "//sum : " + cursor.getInt(2));
		}

		final String data = "["+chartArrayValue[0]+", "+chartArrayValue[1]+", "+chartArrayValue[2]+", "+chartArrayValue[3]+", "+chartArrayValue[4]+", "+chartArrayValue[5]+", "+chartArrayValue[6]+"" +
				", "+chartArrayValue[7]+", "+chartArrayValue[8]+", "+chartArrayValue[9]+", "+chartArrayValue[10]+", "+chartArrayValue[11]+"]";
		ULog.d("data", "chart Log : " + data);

		/*Handler mHandler = new Handler();
		Runnable mMyTask = new Runnable() {
			@Override
			public void run() {
				// 실제 동작
				mWebview.loadUrl("javascript:setChartData("+data+")");
			}
		};
		mHandler.postDelayed(mMyTask, 1000);*/ // 3초후에 실행


		int sumCnt = 0, sumDis = 0, sumCar = 0;
		for(int i = 0; i < chartArrayValue.length; i++) {
			sumCnt = chartArrayValue[i] + sumCnt;
			sumDis = disArrayValue[i] + sumDis;
			sumCar = carArrayValue[i] + sumCar;
		}


		mWalkCntTv.setText(sumCnt + "");
		mKmAllCnt.setText(String.format("%.2f", (sumDis/1000.0f)));
		mKcalAllCnt.setText(String.format("%.2f", (sumCar/1000.0f)));

		mTotalDistance.setText(String.format("%.2f", (sumDis/1000.0f)));
		mToTalStep.setText(sumCnt + "");
		mToTalCalorie.setText(String.format("%.2f", (sumCar/1000.0f)));

		Cursor walkCursor = dbManager.dailyActDate(mPreference.getUserId(), mTodayPreference.getDataDate());
		int walkSumCnt = 0, walkSumDis = 0, walkSumCal = 0;
		while (walkCursor.moveToNext()) {
			walkSumCnt = walkCursor.getInt(2);
			walkSumDis = walkCursor.getInt(3);
			walkSumCal = walkCursor.getInt(4);
		}
		mWalkTotalDistance.setText(String.format("%.2f", (walkSumDis/1000.0f)));
		mWalkToTalStep.setText(walkSumCnt + "");
		mWalkToTalCalorie.setText(String.format("%.2f", (walkSumCal/1000.0f)));




		ArrayList<String> member =  dbManager.selectAccountMember();
		int totalMember = member.size();
		for(int i = totalMember-1; i > 0; i--) {
			if(mPreference.getUserId().equals(member.get(i))){
				member.remove(i);
			}else if(member.size() > 2) {
				member.remove(0);
			}
		}


		if(member.size() > 0) {
			final int[] chartArrayValue2 = new int[12];
			int[] disArrayValue2 = new int[12];
			int[] calArrayValue2 = new int[12];

			final int[] chartArrayValue3 = new int[12];
			int[] disArrayValue3 = new int[12];
			int[] calArrayValue3 = new int[12];

			String data2 = null;
			String data3 = null;

			if(member.size() > 0 ) {
				Cursor cursor2 = dbManager.dailyRunActDate(member.get(0), mTodayPreference.getDataDate());
				while (cursor2.moveToNext()) {
					if (cursor2.getInt(1) % 2 == 1) {
						chartArrayValue2[(((cursor2.getInt(1)) + 1) / 2) - 1] = cursor2.getInt(2);
						disArrayValue2[(((cursor2.getInt(1)) + 1) / 2) - 1] = cursor2.getInt(3);
						calArrayValue2[(((cursor2.getInt(1)) + 1) / 2) - 1] = cursor2.getInt(4);
					} else {
						chartArrayValue2[(((cursor2.getInt(1))) / 2) - 1] = chartArrayValue2[(((cursor2.getInt(1))) / 2) - 1] + cursor2.getInt(2);
						disArrayValue2[(((cursor2.getInt(1))) / 2) - 1] = disArrayValue2[(((cursor2.getInt(1))) / 2) - 1] + cursor2.getInt(3);
						calArrayValue2[(((cursor2.getInt(1))) / 2) - 1] = calArrayValue2[(((cursor2.getInt(1))) / 2) - 1] + cursor2.getInt(4);
					}
					ULog.d("activityWalk2222", "getPosition : " + cursor2.getPosition() + " //date : " + cursor2.getString(0) + "// hour : " + cursor2.getInt(1) + "//sum : " + cursor2.getInt(2));
				}

				data2 = "[" + chartArrayValue2[0] + ", " + chartArrayValue2[1] + ", " + chartArrayValue2[2] + ", " + chartArrayValue2[3] + ", " + chartArrayValue2[4] + ", " + chartArrayValue2[5] + ", " + chartArrayValue2[6] + "" +
						", " + chartArrayValue2[7] + ", " + chartArrayValue2[8] + ", " + chartArrayValue2[9] + ", " + chartArrayValue2[10] + ", " + chartArrayValue2[11] + "]";
				ULog.d("data", "chart2 Log : " + data2);

				if(member.size() > 1) {
					Cursor cursor3 = dbManager.dailyRunActDate(member.get(1), mTodayPreference.getDataDate());
					while (cursor3.moveToNext()) {
						if (cursor3.getInt(1) % 2 == 1) {
							chartArrayValue3[(((cursor3.getInt(1)) + 1) / 2) - 1] = cursor3.getInt(2);
							disArrayValue3[(((cursor3.getInt(1)) + 1) / 2) - 1] = cursor3.getInt(3);
							calArrayValue3[(((cursor3.getInt(1)) + 1) / 2) - 1] = cursor3.getInt(4);
						} else {
							chartArrayValue3[(((cursor3.getInt(1))) / 2) - 1] = chartArrayValue3[(((cursor3.getInt(1))) / 2) - 1] + cursor3.getInt(2);
							disArrayValue3[(((cursor3.getInt(1))) / 2) - 1] = disArrayValue3[(((cursor3.getInt(1))) / 2) - 1] + cursor3.getInt(3);
							calArrayValue3[(((cursor3.getInt(1))) / 2) - 1] = calArrayValue3[(((cursor3.getInt(1))) / 2) - 1] + cursor3.getInt(4);
						}
						ULog.d("activityWalk3333", "getPosition : " + cursor3.getPosition() + " //date : " + cursor3.getString(0) + "// hour : " + cursor3.getInt(1) + "//sum : " + cursor3.getInt(2));
					}

					data3 = "[" + chartArrayValue3[0] + ", " + chartArrayValue3[1] + ", " + chartArrayValue3[2] + ", " + chartArrayValue3[3] + ", " + chartArrayValue3[4] + ", " + chartArrayValue3[5] + ", " + chartArrayValue3[6] + "" +
							", " + chartArrayValue3[7] + ", " + chartArrayValue3[8] + ", " + chartArrayValue3[9] + ", " + chartArrayValue3[10] + ", " + chartArrayValue3[11] + "]";
					ULog.d("data", "chart3 Log : " + data3);

					Handler mHandler = new Handler();
					final String finalData2 = data2;
					final String finalData3 = data3;
					Runnable mMyTask = new Runnable() {
						@Override
						public void run() {
							// 실제 동작
							mWebview.loadUrl("javascript:setChartData("+data+", "+ finalData2 +", "+ finalData3 +")");
							mPieChartWebView.loadUrl("javascript:setChartData3("+totalCount(chartArrayValue)+", "+ totalCount(chartArrayValue2) +", "+ totalCount(chartArrayValue3) +")");

							mUserImg1.setVisibility(View.VISIBLE);
							mUserImg2.setVisibility(View.VISIBLE);
							mUserImg3.setVisibility(View.VISIBLE);
						}
					};
					mHandler.postDelayed(mMyTask, 1000); // 3초후에 실행
				}else {

					Handler mHandler = new Handler();
					final String finalData2 = data2;
					Runnable mMyTask = new Runnable() {
						@Override
						public void run() {
							// 실제 동작
							mWebview.loadUrl("javascript:setChartData("+data+", "+ finalData2 +")");
							mPieChartWebView.loadUrl("javascript:setChartData2("+totalCount(chartArrayValue)+", "+ totalCount(chartArrayValue2) +")");

							mUserImg1.setVisibility(View.VISIBLE);
							mUserImg2.setVisibility(View.VISIBLE);
							mUserImg3.setVisibility(View.INVISIBLE);
						}
					};
					mHandler.postDelayed(mMyTask, 1000); // 3초후에 실행
				}
			}
		}else {
			Handler mHandler = new Handler();
			Runnable mMyTask = new Runnable() {
				@Override
				public void run() {
					// 실제 동작
					mWebview.loadUrl("javascript:setChartData("+data+")");
					mPieChartWebView.loadUrl("javascript:setChartData1("+totalCount(chartArrayValue)+")");

					mUserImg1.setVisibility(View.VISIBLE);
					mUserImg2.setVisibility(View.INVISIBLE);
					mUserImg3.setVisibility(View.INVISIBLE);
				}
			};
			mHandler.postDelayed(mMyTask, 1000); // 3초후에 실행
		}


	}

	public void chartSampleData(){
		String data1 = "[0, 0, 0, 0, 0, 200, 0, 0, 0, 3575, 2048, 0]";
		String data2 = "[0, 0, 0, 0, 0, 0, 0, 0, 106, 2990, 3874, 0]";
		String data3 = "[0, 0, 0, 0, 0, 102, 0, 606, 302, 7680, 5470, 0]";
		mWebview.loadUrl("javascript:setChartData("+data1+", "+ data2 +", "+ data3 +")");

		String data4 = "5823";
		String data5 = "6970";
		String data6 = "14160";
		mPieChartWebView.loadUrl("javascript:setChartData3("+data4+", "+ data5 +", "+ data6 +")");

		mUserImg1.setVisibility(View.VISIBLE);
		mUserImg2.setVisibility(View.VISIBLE);
		mUserImg3.setVisibility(View.VISIBLE);
	}

	private void chartView() {
		//line chart
		mWebview.getSettings().setJavaScriptEnabled(true);
		mWebview.setBackgroundColor(Color.TRANSPARENT);
		mWebview.setWebChromeClient(new WebChromeClient());
		mWebview.loadUrl("file:///android_asset/user_line_chart.html");

		mWebview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
			}

			@Override
			public void onPageFinished(WebView view, String url) {
			}
		});



		//pie chart
		mPieChartWebView.getSettings().setJavaScriptEnabled(true);
		mPieChartWebView.setBackgroundColor(Color.TRANSPARENT);
		mPieChartWebView.setWebChromeClient(new WebChromeClient());
		mPieChartWebView.loadUrl("file:///android_asset/user_pie_chart.html");

		mPieChartWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
			}
			@Override
			public void onPageFinished(WebView view, String url) {
			}
		});

	}

	private String totalCount(int[] data) {
		int sum = 0;

		for (int i : data)
			sum += i;
		return sum+"";
	}
}