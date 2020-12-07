package com.standingegg.band.util;

import java.util.ArrayList;
import java.util.Arrays;

import android.R.bool;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class TodayPreferences {
	// Constants
	private static final String TAG = "TodayPreferences";
	// Instance
	private static TodayPreferences mInstance = null;

	private Context mContext;

	private String mTodayDate = null;
	private int mTodayDateY = 2016;
	private int mTodayDateM = 1;
	private int mTodayDateD = 6;
	private int mActivityAllCount = 0;
	// private Float mActivityAllKm = 0.00f;

	// preferences 값
	private String mTodayActCnt; // graph 표시 - ActivityTodayDetailst
	private String mTodayActCntTimeHour; // graph 표시
	private String mTodayActCntTimeMin; // graph 표시
	private int mWalkDistance = 0; // ActivityTodayDetail - 걸은 거리
	// private int mWalkHour = 0; // ActivityTodayDetail - 걸은 시간
	private int mWalkMin = 0; // ActivityTodayDetail - 걸은 분..? 왜나눴지...?
	private int mWalkCal = 0; // ActivityTodayDetail - 걸음으로 소모한 cal량

	private int mRunDistance = 0; // ActivityTodayDetail - 뛴 거리
	// private int mRunHour = 0; // ActivityTodayDetail - 뛴 시간
	private int mRunMin = 0; // ActivityTodayDetail - 뛴 분
	private int mRunCal = 0; // ActivityTodayDetail - 뛰어선 소모한 cal량

	// private int mDeepSleepHour = 0;
	private int mDeepSleepMin = 0;

	// private int mLowSleepHour = 0;
	private int mLowSleepMin = 0;
	private int mAwakeMin = 0;

	private String mSleepTime = "00:00";
	private String mWakeUpTime = "00:00";
	
	private String mTodaySleepHeight;
	private String mTodaySleepCnt;
	
	private String mtimeLine1Text;
	private String mtimeLine2Text;
	
	private boolean mActGoalOK = false;

	private TodayPreferences(Context c) {
		mContext = c;

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);

		mActivityAllCount = prefs.getInt(Constants.TODAY_ACTIVITY_CNT, 0);
		mTodayActCnt = prefs.getString(Constants.TODAY_ACT_GRAPH_CNT, null);
		mTodayActCntTimeHour = prefs.getString(Constants.TODAY_ACT_GRAPH_TIME_HOUR, null);
		mTodayActCntTimeMin = prefs.getString(Constants.TODAY_ACT_GRAPH_TIME_MIN, null);
		mWalkDistance = prefs.getInt(Constants.TODAY_ACT_WALK_DIS, 0);
		mWalkMin = prefs.getInt(Constants.TODAY_ACT_WALK_M, 0);
		mWalkCal = prefs.getInt(Constants.TODAY_ACT_WALK_CAL, 0);

		mRunDistance = prefs.getInt(Constants.TODAY_ACT_RUN_DIS, 0);
		mRunMin = prefs.getInt(Constants.TODAY_ACT_RUN_M, 0);
		mRunCal = prefs.getInt(Constants.TODAY_ACT_RUN_CAL, 0);

		mDeepSleepMin = prefs.getInt(Constants.TODAY_DEEP_SLEEP_M, 0);
		mLowSleepMin = prefs.getInt(Constants.TODAY_LOW_SLEEP_M, 0);
		mAwakeMin = prefs.getInt(Constants.TODAY_AWAKE_SLEEP_M, 0);
		
		mSleepTime = prefs.getString(Constants.TODAY_SLEEP_START, "00:00");
		mWakeUpTime = prefs.getString(Constants.TODAY_SLEEP_WAKEUP, "00:00");
		
		mTodaySleepHeight = prefs.getString(Constants.TODAY_SLEEP_GRAPH_HEIGHT, null);
		mTodaySleepCnt = prefs.getString(Constants.TODAY_SLEEP_GRAPH_CNT, null);
		
		mTodayDate = prefs.getString(Constants.TODAY_SAVE_DATA_DATE, null);
		
		mtimeLine1Text = prefs.getString(Constants.TODAY_TIME_LINE_1, null);
		mtimeLine2Text = prefs.getString(Constants.TODAY_TIME_LINE_2, null);
		
		mActGoalOK = prefs.getBoolean(Constants.TODAY_ACT_GOAL_OK, false);
	}

	/**
	 * Single pattern
	 */
	public synchronized static TodayPreferences getInstance(Context c) {
		if (mInstance == null) {
			if (c != null)
				mInstance = new TodayPreferences(c);
			else
				return null;
		}
		return mInstance;
	}
	

	/**
	 * Remember device name for future use
	 * 
	 * @param name
	 *            device name
	 */
	// public void setVersion(String name) {
	// SharedPreferences prefs =
	// mContext.getSharedPreferences(Constants.PREFERENCE_NAME,
	// Context.MODE_PRIVATE);
	// SharedPreferences.Editor editor = prefs.edit();
	// editor.putString(Constants.PREFERENCE_VERSION_OF_API, mVersion);
	// editor.commit();
	// }

	
	public void setTodayDate(String date, int y, int m, int d){
		mTodayDate = date;
		mTodayDateY = y;
		mTodayDateM = m;
		mTodayDateD = d;
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Constants.TODAY_SAVE_DATA_DATE, mTodayDate);
		editor.putInt(Constants.TODAY_SAVE_DATA_YEAR, mTodayDateY);
		editor.putInt(Constants.TODAY_SAVE_DATA_MONTH, mTodayDateM);
		editor.putInt(Constants.TODAY_SAVE_DATA_DAY, mTodayDateD);
	}
	public String getDataDate() {
		return mTodayDate;
	}
	public int getDataYear() {
		return mTodayDateY;
	}
	public int getDataMonth() {
		return mTodayDateM;
	}
	public int getDataDay() {
		return mTodayDateD;
	}

	public void setTodayActData(Bundle info) {
		mActivityAllCount = info.getInt(Constants.TODAY_ACTIVITY_CNT);

		mTodayActCnt = integerConvertToString(info.getIntegerArrayList(Constants.TODAY_ACT_GRAPH_CNT)); // 보낼때
		mTodayActCntTimeHour = integerConvertToString(info.getIntegerArrayList(Constants.TODAY_ACT_GRAPH_TIME_HOUR));
		mTodayActCntTimeMin = integerConvertToString(info.getIntegerArrayList(Constants.TODAY_ACT_GRAPH_TIME_MIN));
		
		mWalkDistance = info.getInt(Constants.TODAY_ACT_WALK_DIS);
		mWalkMin = info.getInt(Constants.TODAY_ACT_WALK_M);
		mWalkCal = info.getInt(Constants.TODAY_ACT_WALK_CAL);
		
		mRunDistance = info.getInt(Constants.TODAY_ACT_RUN_DIS);
		mRunMin = info.getInt(Constants.TODAY_ACT_RUN_M);
		mRunCal = info.getInt(Constants.TODAY_ACT_RUN_CAL);

		
		mActGoalOK = info.getBoolean(Constants.TODAY_ACT_GOAL_OK);
		
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_ACTIVITY_CNT, mActivityAllCount);
		editor.putString(Constants.TODAY_ACT_GRAPH_CNT, mTodayActCnt);
		editor.putString(Constants.TODAY_ACT_GRAPH_TIME_HOUR, mTodayActCntTimeHour);
		editor.putString(Constants.TODAY_ACT_GRAPH_TIME_MIN, mTodayActCntTimeMin);

		editor.putInt(Constants.TODAY_ACT_WALK_DIS, mWalkDistance);
		editor.putInt(Constants.TODAY_ACT_WALK_M, mWalkMin);
		editor.putInt(Constants.TODAY_ACT_WALK_CAL, mWalkCal);

		editor.putInt(Constants.TODAY_ACT_RUN_DIS, mRunDistance);
		editor.putInt(Constants.TODAY_ACT_RUN_M, mRunMin);
		editor.putInt(Constants.TODAY_ACT_RUN_CAL, mRunCal);
		
		
		editor.putBoolean(Constants.TODAY_ACT_GOAL_OK, mActGoalOK);
		
		editor.commit();
	}
	
	public void setTimeLine(Bundle info){
		mtimeLine1Text = convertToString(info.getStringArrayList(Constants.TODAY_TIME_LINE_1));
		mtimeLine2Text = convertToString(info.getStringArrayList(Constants.TODAY_TIME_LINE_2));
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString(Constants.TODAY_TIME_LINE_1, mtimeLine1Text);
		editor.putString(Constants.TODAY_TIME_LINE_2, mtimeLine2Text);
		
		editor.commit();
		
	}
	
	public void setTodaySleepData(Bundle info) {

		mDeepSleepMin = info.getInt(Constants.TODAY_DEEP_SLEEP_M);
		mLowSleepMin = info.getInt(Constants.TODAY_LOW_SLEEP_M);
		mAwakeMin = info.getInt(Constants.TODAY_AWAKE_SLEEP_M);
		
		mSleepTime = info.getString(Constants.TODAY_SLEEP_START);
		mWakeUpTime = info.getString(Constants.TODAY_SLEEP_WAKEUP); 
		
		mTodaySleepHeight = integerConvertToString(info.getIntegerArrayList(Constants.TODAY_SLEEP_GRAPH_HEIGHT));
		mTodaySleepCnt = integerConvertToString(info.getIntegerArrayList(Constants.TODAY_SLEEP_GRAPH_CNT));
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		//수면 data
		editor.putInt(Constants.TODAY_DEEP_SLEEP_M, mDeepSleepMin);
		editor.putInt(Constants.TODAY_LOW_SLEEP_M, mLowSleepMin);
		editor.putInt(Constants.TODAY_AWAKE_SLEEP_M, mAwakeMin);
		
		editor.putString(Constants.TODAY_SLEEP_START, mSleepTime);
		editor.putString(Constants.TODAY_SLEEP_WAKEUP, mWakeUpTime);
		
		editor.putString(Constants.TODAY_SLEEP_GRAPH_HEIGHT, mTodaySleepHeight);
		editor.putString(Constants.TODAY_SLEEP_GRAPH_CNT, mTodaySleepCnt);
		
		editor.commit();
	}
	/**
	 * data 새로 집어 넣을 때만 해라....... 제발........ Reset connection info
	 */
	public void resetActivityPreferences() {
		
		mActivityAllCount = 0;

		mTodayActCnt = null; // 보낼때
		mTodayActCntTimeHour = null;
		mTodayActCntTimeMin = null;

		
		mWalkDistance = 0;
		mWalkMin = 0;
		mWalkCal = 0;

		
		mRunDistance = 0;
		mRunMin = 0;
		mRunCal = 0;
		
		mtimeLine1Text = null;
		mtimeLine2Text = null;
		
		mActGoalOK =  false;

		ULog.e("********************************************************");
		ULog.e("reset Activity Preferences");
		ULog.e("reset Activity Preferences");
		SharedPreferences pref = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        
        editor.clear();
        
        editor.putInt(Constants.TODAY_ACTIVITY_CNT, mActivityAllCount);
		editor.putString(Constants.TODAY_ACT_GRAPH_CNT, mTodayActCnt);
		editor.putString(Constants.TODAY_ACT_GRAPH_TIME_HOUR, mTodayActCntTimeHour);
		editor.putString(Constants.TODAY_ACT_GRAPH_TIME_MIN, mTodayActCntTimeMin);

		editor.putInt(Constants.TODAY_ACT_WALK_DIS, mWalkDistance);
		editor.putInt(Constants.TODAY_ACT_WALK_M, mWalkMin);
		editor.putInt(Constants.TODAY_ACT_WALK_CAL, mWalkCal);

		editor.putInt(Constants.TODAY_ACT_RUN_DIS, mRunDistance);
		editor.putInt(Constants.TODAY_ACT_RUN_M, mRunMin);
		editor.putInt(Constants.TODAY_ACT_RUN_CAL, mRunCal);
		editor.putString(Constants.TODAY_TIME_LINE_1, mtimeLine1Text);
		editor.putString(Constants.TODAY_TIME_LINE_2, mtimeLine2Text);
		
		editor.putBoolean(Constants.TODAY_ACT_GOAL_OK, mActGoalOK);
		
        
        editor.commit();
	}
	public void resetSleepPreferences() {
		mDeepSleepMin = 0;
		mLowSleepMin = 0;
		mAwakeMin = 0;
		
		mSleepTime = null;
		mWakeUpTime = null; 
		
		mTodaySleepHeight = null;
		mTodaySleepCnt = null;
		ULog.e("********************************************************");
		ULog.e("reset Activity Preferences");
		ULog.e("reset Activity Preferences");
		SharedPreferences pref = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.clear();
		
		//수면 data
		editor.putInt(Constants.TODAY_DEEP_SLEEP_M, mDeepSleepMin);
		editor.putInt(Constants.TODAY_LOW_SLEEP_M, mLowSleepMin);
		editor.putInt(Constants.TODAY_AWAKE_SLEEP_M, mAwakeMin);
		
		editor.putString(Constants.TODAY_SLEEP_START, mSleepTime);
		editor.putString(Constants.TODAY_SLEEP_WAKEUP, mWakeUpTime);
		
		editor.putString(Constants.TODAY_SLEEP_GRAPH_HEIGHT, mTodaySleepHeight);
		editor.putString(Constants.TODAY_SLEEP_GRAPH_CNT, mTodaySleepCnt);
		
		
		editor.commit();
	}
	
	
	/**
	 * data 새로 집어 넣을 때만 해라....... 제발........
	 */
	public void resetTodayDate() {
		mTodayDate = null;
	}

	

	
	public int getActivityAllCount() {
		return mActivityAllCount;
	}

	// public Float getActivityAllKm() {
	// return mActivityAllKm;
	// }

	public ArrayList<Integer> getTodayActCnt() {
		return convertToIntegerArray(mTodayActCnt);
	}

	public ArrayList<Integer> getTodayActCntTimeHour() {
		ULog.v(TAG, "GET Hour");
		return convertToIntegerArray(mTodayActCntTimeHour);
	}

	public ArrayList<Integer> getTodayActCntTimeMin() {
		ULog.v(TAG, "GET Min");
		return convertToIntegerArray(mTodayActCntTimeMin);
	}
	public ArrayList<String> getTodayTimeLine1() {
		ULog.v(TAG, "GET timeLine");
		return convertToArray(mtimeLine1Text);
	}
	public ArrayList<String> getTodayTimeLine2() {
		ULog.v(TAG, "GET timeLine");
		return convertToArray(mtimeLine2Text);
	}
	
	public boolean getUserActGoalOK(){
		return mActGoalOK;
	}

	public int getWalkDistance() {
		return mWalkDistance;
	}

	// public int getWalkHour() {
	// return mWalkHour;
	// }

	public int getWalkMin() {
		return mWalkMin;
	}

	public int getWalkCal() {
		return mWalkCal;
	}

	public int getRunDistance() {
		return mRunDistance;
	}

	// public int getRunHour() {
	// return mRunHour;
	// }

	public int getRunMin() {
		return mRunMin;
	}

	public int getRunCal() {
		return mRunCal;
	}
	public int getDeepSleepMin() {
		return mDeepSleepMin;
	}

	public int getLowSleepMin() {
		return mLowSleepMin;
	}
	
	public int getAwakeMin() {
		return mAwakeMin;
	}

	public String getSleepTime() {
		return mSleepTime;
	}

	public String getWakeUpTime() {
		return mWakeUpTime;
	}
	
	public ArrayList<Integer> getTodaySleepHeight() {
		ULog.v(TAG, "GET height / flag");
		return convertToIntegerArray(mTodaySleepHeight);
	}
	
	public ArrayList<Integer> getTodaySleepCnt() {
		ULog.v(TAG, "실제 수면 시간");
		return convertToIntegerArray(mTodaySleepCnt);
	}

	private String integerConvertToString(ArrayList<Integer> list) {
		StringBuilder sb = new StringBuilder();
		String delim = "";
		for (int s : list) {
			sb.append(delim);
			sb.append(s);
			delim = ",";
		}
		return sb.toString();
	}

	private ArrayList<Integer> convertToIntegerArray(String string) {

		try {
			if (string == null || string.length() == 0)
				return new ArrayList<Integer>();
			Integer[] tmp = new Integer[string.split(",").length];
			String[] b = string.split(",");
			ULog.v(string);
			for (int i = 0; i < string.split(",").length; i++) {
				tmp[i] = Integer.parseInt(b[i]);
			}

			ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(tmp));
			return list;
		} catch (Exception e) {
			return null;
		}
	
	}
	private String convertToString(ArrayList<String> list) {

		StringBuilder sb = new StringBuilder();
		String delim = "";
		for (String s : list) {
			sb.append(delim);
			sb.append(s);
			;
			delim = ",";
		}
		return sb.toString();
	}

	private ArrayList<String> convertToArray(String string) {
		try {
			ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(",")));
			return list;
		} catch (Exception e) {
			return null;
		}
		
	}

}
