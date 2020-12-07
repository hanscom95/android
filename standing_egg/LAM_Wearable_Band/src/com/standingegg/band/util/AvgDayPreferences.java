/*package com.standingegg.band.util;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class AvgDayPreferences {
	// Constants
	private static final String TAG = "AvgDayPreferences";
	// Instance
	private static AvgDayPreferences mInstance = null;

	private Context mContext;

	private String mDayDate = "151122";
	private int mActivityAllCount = 0;
	private Float mActivityAllKm = 0.00f;

	// preferences 값
	private String mDayDate; // graph 표시 - ActivityDayDetail
	private String mDayActCntTimeHour; // graph 표시
	private String mDayActCntTimeMin; // graph 표시
	private Float mWalkDistance = 0.00f; // ActivityDayDetail - 걸은 거리
	private int mWalkHour = 0; // ActivityDayDetail - 걸은 시간
	private int mWalkMin = 0; // ActivityDayDetail - 걸은 분..? 왜나눴지...?
	private Float mWalkKcal = 0.00f; // ActivityDayDetail - 걸음으로 소모한 cal량

	private Float mRunDistance = 0.00f; // ActivityDayDetail - 뛴 거리
	private int mRunHour = 0; // ActivityDayDetail - 뛴 시간
	private int mRunMin = 0; // ActivityDayDetail - 뛴 분
	private Float mRunKcal = 0.00f; // ActivityDayDetail - 뛰어선 소모한 cal량

	private int mDeepSleepHour = 0;
	private int mDeepSleepMin = 0;

	private int mLowSleepHour = 0;
	private int mLowSleepMin = 0;

	private String mSleepTime = "00:00";
	private String mWakeUpTime = "00:00";

	private AvgDayPreferences(Context c) {
		mContext = c;

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);

		mActivityAllCount = prefs.getInt(Constants.Day_ACTIVITY_CNT, 0);
		mDayActCntTimeHour = prefs.getString(Constants.Day_ACT_GRAPH_TIME_HOUR, null);
		mDayActCntTimeMin = prefs.getString(Constants.Day_ACT_GRAPH_TIME_MIN, null);
		mWalkDistance = prefs.getFloat(Constants.Day_ACT_WALK_KM, 0.00f);
//		mWalkHour = prefs.getInt(Constants.Day_ACT_WALK_H, 0);
		mWalkMin = prefs.getInt(Constants.Day_ACT_WALK_M, 0);
		mWalkKcal = prefs.getFloat(Constants.Day_ACT_WALK_KCAl, 0.00f);

		mRunDistance = prefs.getFloat(Constants.Day_ACT_RUN_KM, 0.00f);
//		mRunHour = prefs.getInt(Constants.Day_ACT_RUN_H, 0);
		mRunMin = prefs.getInt(Constants.Day_ACT_RUN_M, 0);
		mRunKcal = prefs.getFloat(Constants.Day_ACT_RUN_KCAL, 0.00f);

//		mDeepSleepHour = prefs.getInt(Constants.Day_DEEP_SLEEP_H, 0);
		mDeepSleepMin = prefs.getInt(Constants.Day_DEEP_SLEEP_M, 0);
//		mLowSleepHour = prefs.getInt(Constants.Day_LOW_SLEEP_H, 0);
		mLowSleepMin = prefs.getInt(Constants.Day_LOW_SLEEP_M, 0);
		mSleepTime = prefs.getString(Constants.Day_SLEEP_START, "00:00");
		mWakeUpTime = prefs.getString(Constants.Day_SLEEP_WAKEUP, "00:00");
	}

	*//**
	 * Single pattern
	 *//*
	public synchronized static AvgDayPreferences getInstance(Context c) {
		if (mInstance == null) {
			if (c != null)
				mInstance = new AvgDayPreferences(c);
			else
				return null;
		}
		return mInstance;
	}

	*//**
	 * data 새로 집어 넣을 때만 해라....... 제발........
	 *//*
	public void resetDayDate() {
		mDayDate = null;
	}

	*//**
	 * data 새로 집어 넣을 때만 해라....... 제발........ Reset connection info
	 *//*
	public void resetActivityPreferences() {
		mActivityAllCount = 0;
		mDayActCntTimeHour = null;
		mDayActCntTimeMin = null;
		mWalkDistance = 0.0f;
		mWalkHour = 0;
		mWalkMin = 0;
		mWalkKcal = 0.0f;

		mRunDistance = 0.0f;
		mRunHour = 0;
		mRunMin = 0;
		mRunKcal = 0.0f;
	}

	*//**
	 * data 새로 집어 넣을 때만 해라....... 제발........ Reset connection info
	 *//*
	public void resetSleepPreferences() {
		mDeepSleepHour = 0;
		mDeepSleepMin = 0;

		mLowSleepHour = 0;
		mLowSleepMin = 0;

		mSleepTime = "00:00";
		mWakeUpTime = "00:00";
	}

	*//**
	 * Remember device name for future use
	 * 
	 * @param name
	 *            device name
	 *//*
	// public void setVersion(String name) {
	// SharedPreferences prefs =
	// mContext.getSharedPreferences(Constants.PREFERENCE_NAME,
	// Context.MODE_PRIVATE);
	// SharedPreferences.Editor editor = prefs.edit();
	// editor.putString(Constants.PREFERENCE_VERSION_OF_API, mVersion);
	// editor.commit();
	// }

	public void setDayData(Bundle info) {
		mDayDate = info.getString(Constants.DAY_SAVE_DATA_DATE);
		mActivityAllCount = info.getInt(Constants.DAY_ACTIVITY_CNT);
		ULog.v(TAG, "GET bundle cnt:" + info.getIntegerArrayList(Constants.DA_ACT_GRAPH_CNT) );
		
		
		mDayActCntTimeHour = integerConvertToString(info.getIntegerArrayList(Constants.Day_ACT_GRAPH_TIME_HOUR));
		
		ULog.v(TAG, "GET bundle hour2:" + mDayActCntTimeHour );
		
		ULog.v(TAG, "GET bundle min:" + info.getIntegerArrayList(Constants.Day_ACT_GRAPH_TIME_MIN) );
		mDayActCntTimeMin = integerConvertToString(info.getIntegerArrayList(Constants.Day_ACT_GRAPH_TIME_MIN));
		
		
		
		mWalkDistance = info.getFloat(Constants.Day_ACT_WALK_KM);
//		mWalkHour = info.getInt(Constants.Day_ACT_WALK_H);
		mWalkMin = info.getInt(Constants.Day_ACT_WALK_M);
		mWalkKcal = info.getFloat(Constants.Day_ACT_WALK_KCAl);

		mRunDistance = info.getFloat(Constants.Day_ACT_RUN_KM);
//		mRunHour = info.getInt(Constants.Day_ACT_RUN_H);
		mRunMin = info.getInt(Constants.Day_ACT_RUN_M);
		mRunKcal = info.getFloat(Constants.Day_ACT_RUN_KCAL);

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Constants.Day_ACTIVITY_CNT, mActivityAllCount);
		// editor.putIntegerArrayList(Constants.Day_ACT_GRAPH_CNT ,
		// mDayActCnt );
		// editor.putIntegerArrayList(Constants.Day_ACT_GRAPH_TIME ,
		// mDayActCntTime );
		editor.putFloat(Constants.Day_ACT_WALK_KM, mWalkDistance);
//		editor.putInt(Constants.Day_ACT_WALK_H, mWalkHour);
		editor.putInt(Constants.Day_ACT_WALK_M, mWalkMin);
		editor.putFloat(Constants.Day_ACT_WALK_KCAl, mWalkKcal);
		editor.putFloat(Constants.Day_ACT_RUN_KM, mRunDistance);
//		editor.putInt(Constants.Day_ACT_RUN_H, mRunHour);
		editor.putInt(Constants.Day_ACT_RUN_M, mRunMin);
		editor.putFloat(Constants.Day_ACT_RUN_KCAL, mRunKcal);
		editor.commit();
	}

	public String getDataDate() {
		return mDayDate;
	}

	public int getActivityAllCount() {
		return mActivityAllCount;
	}

	public Float getActivityAllKm() {
		return mActivityAllKm;
	}

	public ArrayList<Integer> getDayActCntTimeMin() {
		ULog.v(TAG, "GET Min");
		return convertToIntegerArray(mDayActCntTimeMin);
	}

	public Float getWalkDistance() {
		return mWalkDistance;
	}

	public int getWalkHour() {
		return mWalkHour;
	}

	public int getWalkMin() {
		return mWalkMin;
	}

	public Float getWalkKcal() {
		return mWalkKcal;
	}

	public Float getRunDistance() {
		return mRunDistance;
	}

	public int getRunHour() {
		return mRunHour;
	}

	public int getRunMin() {
		return mRunMin;
	}

	public Float getRunKcal() {
		return mRunKcal;
	}

	public int getDeepSleepHour() {
		return mDeepSleepHour;
	}
	public int getDeepSleepMin() {
		return mDeepSleepMin;
	}

	public int getLowSleepHour() {
		return mLowSleepHour;
	}

	public int getLowSleepMin() {
		return mLowSleepMin;
	}

	public String getSleepTime() {
		return mSleepTime;
	}

	public String getWakeUpTime() {
		return mWakeUpTime;
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

		ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(",")));
		return list;
	}

	private String integerConvertToString(ArrayList<Integer> list) {
		ULog.v(TAG, ""+ list);
		
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
		
		if(string ==null || string.length() == 0) 
			return new ArrayList<Integer>();
		Integer[] tmp = new Integer[string.split(",").length];
		String[] b = string.split(",");
		ULog.v(string);
		for (int i = 0; i < string.split(",").length; i++) {
			ULog.v(TAG,b[i]);
			tmp[i] = Integer.parseInt(b[i]);
		}

		ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(tmp));
		return list;
	}

}
*/