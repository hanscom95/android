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
	private int mActivityRunAllCount = 0;
	private int mMotionHammerAllCount = 0;
	private int mMotionShoulderAllCount = 0;
	private int mMotionSittingAllCount = 0;
	private int mMotionJumpingAllCount = 0;
	private int mMotionPushUpAllCount = 0;
	private int mMotionSitUpAllCount = 0;
	private int mMotionButterflyAllCount = 0;
	private int mMotionCrunchAllCount = 0;
	private int mMotionUprightRowAllCount = 0;
	private int mMotionToeRaiseAllCount = 0;
	private int mMotionPecFlyAllCount = 0;
	private int mMotionChestPressAllCount = 0;
	// private Float mActivityAllKm = 0.00f;

	// preferences 값
	private String mTodayActCnt; // graph 표시 - ActivityTodayDetailst
	private String mTodayActCntTimeHour; // graph 표시
	private String mTodayActCntTimeMin; // graph 표시
	private int mWalkDistance = 0; // ActivityTodayDetail - 걸은 거리
	private int mWalkHour = 0; // ActivityTodayDetail - 걸은 시간
	private int mWalkMin = 0; // ActivityTodayDetail - 걸은 분..? 왜나눴지...?
	private int mWalkCal = 0; // ActivityTodayDetail - 걸음으로 소모한 cal량
	private int mWalkCnt = 0;

	private int mRunDistance = 0; // ActivityTodayDetail - 뛴 거리
	private int mRunHour = 0; // ActivityTodayDetail - 뛴 시간
	private int mRunMin = 0; // ActivityTodayDetail - 뛴 분
	private int mRunCal = 0; // ActivityTodayDetail - 뛰어선 소모한 cal량
	private int mRunCnt = 0; // ActivityTodayDetail - 뛰어선 소모한 cal량

	private int mShoulderCnt = 0;
	private int mHammerCnt = 0;
	private int mSittingCnt = 0;
	private int mJumpingCnt = 0;
	private int mPushupCnt = 0;
	private int mSitupCnt = 0;
	private int mButterflyCnt = 0;
	private int mCrunchCnt = 0;
	private int mUprightrowCnt = 0;
	private int mToeraiseCnt = 0;
	private int mPecflyCnt = 0;
	private int mChestpressCnt = 0;

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
	private String mtimeLineFlag;
	
	private boolean mActGoalOK = false;
	private boolean mActGoalTimeLineFalse = false;
	private boolean mSleepTimeLineOK = false;

	private TodayPreferences(Context c) {
		mContext = c;

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);

		mActivityAllCount = prefs.getInt(Constants.TODAY_ACTIVITY_CNT, 0);
		mActivityRunAllCount = prefs.getInt(Constants.TODAY_ACTIVITY_RUN_CNT, 0);
		mMotionHammerAllCount = prefs.getInt(Constants.TODAY_MOTION_HAMMER_CNT, 0);
		mMotionShoulderAllCount = prefs.getInt(Constants.TODAY_MOTION_SHOULDER_CNT, 0);
		mTodayActCnt = prefs.getString(Constants.TODAY_ACT_GRAPH_CNT, null);
		mTodayActCntTimeHour = prefs.getString(Constants.TODAY_ACT_GRAPH_TIME_HOUR, null);
		mTodayActCntTimeMin = prefs.getString(Constants.TODAY_ACT_GRAPH_TIME_MIN, null);
		mWalkDistance = prefs.getInt(Constants.TODAY_ACT_WALK_DIS, 0);
		mWalkHour = prefs.getInt(Constants.TODAY_ACT_WALK_H, 0);
		mWalkMin = prefs.getInt(Constants.TODAY_ACT_WALK_M, 0);
		mWalkCal = prefs.getInt(Constants.TODAY_ACT_WALK_CAL, 0);
		mWalkCnt = prefs.getInt(Constants.TODAY_ACT_WALK_CNT, 0);

		mMotionSittingAllCount = prefs.getInt(Constants.TODAY_MOTION_SITTING_CNT, 0);
		mMotionJumpingAllCount = prefs.getInt(Constants.TODAY_MOTION_JUMPING_CNT, 0);
		mMotionPushUpAllCount = prefs.getInt(Constants.TODAY_MOTION_PUSH_UP_CNT, 0);
		mMotionSitUpAllCount = prefs.getInt(Constants.TODAY_MOTION_SIT_UP_CNT, 0);
		mMotionButterflyAllCount = prefs.getInt(Constants.TODAY_MOTION_BUTTERFLY_CNT, 0);
		mMotionCrunchAllCount = prefs.getInt(Constants.TODAY_MOTION_CRUNCH_CNT, 0);
		mMotionUprightRowAllCount = prefs.getInt(Constants.TODAY_MOTION_UPRIGHT_ROW_CNT, 0);
		mMotionToeRaiseAllCount = prefs.getInt(Constants.TODAY_MOTION_TOE_RAISE_CNT, 0);
		mMotionPecFlyAllCount = prefs.getInt(Constants.TODAY_MOTION_PEC_FLY_CNT, 0);
		mMotionChestPressAllCount = prefs.getInt(Constants.TODAY_MOTION_CHEST_PRESS_CNT, 0);

		mRunDistance = prefs.getInt(Constants.TODAY_ACT_RUN_DIS, 0);
		mRunHour = prefs.getInt(Constants.TODAY_ACT_WALK_H, 0);
		mRunMin = prefs.getInt(Constants.TODAY_ACT_RUN_M, 0);
		mRunCal = prefs.getInt(Constants.TODAY_ACT_RUN_CAL, 0);
		mRunCnt = prefs.getInt(Constants.TODAY_ACT_RUN_CNT, 0);

		mHammerCnt = prefs.getInt(Constants.TODAY_MO_HAMMER_CNT, 0);
		mShoulderCnt = prefs.getInt(Constants.TODAY_MO_SHOULDER_CNT, 0);
		mSittingCnt = prefs.getInt(Constants.TODAY_MO_SITTING_CNT, 0);
		mJumpingCnt = prefs.getInt(Constants.TODAY_MO_JUMPING_CNT, 0);
		mPushupCnt = prefs.getInt(Constants.TODAY_MO_PUSH_UP_CNT, 0);
		mSitupCnt = prefs.getInt(Constants.TODAY_MO_SIT_UP_CNT, 0);
		mButterflyCnt = prefs.getInt(Constants.TODAY_MO_BUTTERFLY_CNT, 0);
		mCrunchCnt = prefs.getInt(Constants.TODAY_MO_CRUNCH_CNT, 0);
		mUprightrowCnt = prefs.getInt(Constants.TODAY_MO_UPRIGHT_ROW_CNT, 0);
		mToeraiseCnt = prefs.getInt(Constants.TODAY_MO_TOE_RAISE_CNT, 0);
		mPecflyCnt = prefs.getInt(Constants.TODAY_MO_PEC_FLY_CNT, 0);
		mChestpressCnt = prefs.getInt(Constants.TODAY_MO_CHEST_PRESS_CNT, 0);

		mDeepSleepMin = prefs.getInt(Constants.TODAY_DEEP_SLEEP_M, 0);
		mLowSleepMin = prefs.getInt(Constants.TODAY_LOW_SLEEP_M, 0);
		mAwakeMin = prefs.getInt(Constants.TODAY_AWAKE_SLEEP_M, 0);
		
		mSleepTime = prefs.getString(Constants.TODAY_SLEEP_START, "00:00");
		mWakeUpTime = prefs.getString(Constants.TODAY_SLEEP_WAKEUP, "00:00");
		
		mTodaySleepHeight = prefs.getString(Constants.TODAY_SLEEP_GRAPH_HEIGHT, null);
		mTodaySleepCnt = prefs.getString(Constants.TODAY_SLEEP_GRAPH_CNT, null);

		mTodayDate = prefs.getString(Constants.TODAY_SAVE_DATA_DATE, null);
		mTodayDateY = prefs.getInt(Constants.TODAY_SAVE_DATA_YEAR, 2016);
		mTodayDateM = prefs.getInt(Constants.TODAY_SAVE_DATA_MONTH, 1);
		mTodayDateD = prefs.getInt(Constants.TODAY_SAVE_DATA_DAY,1);
		
		mtimeLine1Text = prefs.getString(Constants.TODAY_TIME_LINE_1, null);
		mtimeLine2Text = prefs.getString(Constants.TODAY_TIME_LINE_2, null);
		mtimeLineFlag = prefs.getString(Constants.TODAY_TIME_LINE_FLAG, null);
		
		mActGoalOK = prefs.getBoolean(Constants.TODAY_ACT_GOAL_OK, false);
		mActGoalTimeLineFalse = prefs.getBoolean(Constants.TODAY_ACT_GOAL_FALSE, false);
		mSleepTimeLineOK = prefs.getBoolean(Constants.TODAY_SLEEP_TIMELINE_OK, false);
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
		editor.commit();
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
		mWalkHour = info.getInt(Constants.TODAY_ACT_WALK_H);
		mWalkMin = info.getInt(Constants.TODAY_ACT_WALK_M);
		mWalkCal = info.getInt(Constants.TODAY_ACT_WALK_CAL);
		mWalkCnt = info.getInt(Constants.TODAY_ACT_WALK_CNT);

		mActGoalOK = info.getBoolean(Constants.TODAY_ACT_GOAL_OK);
		mActGoalTimeLineFalse = info.getBoolean(Constants.TODAY_ACT_GOAL_FALSE);
		
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_ACTIVITY_CNT, mActivityAllCount);

		editor.putString(Constants.TODAY_ACT_GRAPH_CNT, mTodayActCnt);
		editor.putString(Constants.TODAY_ACT_GRAPH_TIME_HOUR, mTodayActCntTimeHour);
		editor.putString(Constants.TODAY_ACT_GRAPH_TIME_MIN, mTodayActCntTimeMin);

		editor.putInt(Constants.TODAY_ACT_WALK_DIS, mWalkDistance);
		editor.putInt(Constants.TODAY_ACT_WALK_H, mWalkHour);
		editor.putInt(Constants.TODAY_ACT_WALK_M, mWalkMin);
		editor.putInt(Constants.TODAY_ACT_WALK_CAL, mWalkCal);
		editor.putInt(Constants.TODAY_ACT_WALK_CNT, mWalkCnt);
		
		
		editor.putBoolean(Constants.TODAY_ACT_GOAL_OK, mActGoalOK);
		editor.putBoolean(Constants.TODAY_ACT_GOAL_FALSE, mActGoalTimeLineFalse);
		
		editor.commit();
	}

	public void setTodayActRunData(Bundle info) {
		mActivityRunAllCount = info.getInt(Constants.TODAY_ACTIVITY_RUN_CNT);

		mTodayActCnt = integerConvertToString(info.getIntegerArrayList(Constants.TODAY_ACT_GRAPH_CNT)); // 보낼때
		mTodayActCntTimeHour = integerConvertToString(info.getIntegerArrayList(Constants.TODAY_ACT_GRAPH_TIME_HOUR));
		mTodayActCntTimeMin = integerConvertToString(info.getIntegerArrayList(Constants.TODAY_ACT_GRAPH_TIME_MIN));

		mRunDistance = info.getInt(Constants.TODAY_ACT_RUN_DIS);
		mRunHour = info.getInt(Constants.TODAY_ACT_RUN_H);
		mRunMin = info.getInt(Constants.TODAY_ACT_RUN_M);
		mRunCal = info.getInt(Constants.TODAY_ACT_RUN_CAL);
		mRunCnt = info.getInt(Constants.TODAY_ACT_RUN_CNT);

		mActGoalOK = info.getBoolean(Constants.TODAY_ACT_GOAL_OK);
		mActGoalTimeLineFalse = info.getBoolean(Constants.TODAY_ACT_GOAL_FALSE);


		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_ACTIVITY_RUN_CNT, mActivityRunAllCount);

		editor.putString(Constants.TODAY_ACT_GRAPH_CNT, mTodayActCnt);
		editor.putString(Constants.TODAY_ACT_GRAPH_TIME_HOUR, mTodayActCntTimeHour);
		editor.putString(Constants.TODAY_ACT_GRAPH_TIME_MIN, mTodayActCntTimeMin);

		editor.putInt(Constants.TODAY_ACT_RUN_DIS, mRunDistance);
		editor.putInt(Constants.TODAY_ACT_RUN_M, mRunMin);
		editor.putInt(Constants.TODAY_ACT_RUN_CAL, mRunCal);
		editor.putInt(Constants.TODAY_ACT_RUN_CNT, mRunCnt);


		editor.putBoolean(Constants.TODAY_ACT_GOAL_OK, mActGoalOK);
		editor.putBoolean(Constants.TODAY_ACT_GOAL_FALSE, mActGoalTimeLineFalse);

		editor.commit();
	}

	public void setTodayMoHammerData(Bundle info) {
		mMotionHammerAllCount = info.getInt(Constants.TODAY_MOTION_HAMMER_CNT);

		mHammerCnt = info.getInt(Constants.TODAY_MO_HAMMER_CNT);


		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_MOTION_HAMMER_CNT, mMotionHammerAllCount);

		editor.putInt(Constants.TODAY_MO_HAMMER_CNT, mHammerCnt);

		editor.commit();
	}

	public void setTodayMoShoulderData(Bundle info) {
		mMotionShoulderAllCount = info.getInt(Constants.TODAY_MOTION_SHOULDER_CNT);

		mShoulderCnt= info.getInt(Constants.TODAY_MO_SHOULDER_CNT);


		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_MOTION_SHOULDER_CNT, mMotionShoulderAllCount);

		editor.putInt(Constants.TODAY_MO_SHOULDER_CNT, mShoulderCnt);

		editor.commit();
	}

	//motion 추가
	public void setTodayMoSittingData(Bundle info) {
		mMotionSittingAllCount = info.getInt(Constants.TODAY_MOTION_SITTING_CNT);

		mSittingCnt = info.getInt(Constants.TODAY_MO_SITTING_CNT);


		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_MOTION_SITTING_CNT, mMotionSittingAllCount);

		editor.putInt(Constants.TODAY_MO_SITTING_CNT, mSittingCnt);

		editor.commit();
	}

	public void setTodayMoJumpingData(Bundle info) {
		mMotionJumpingAllCount = info.getInt(Constants.TODAY_MOTION_JUMPING_CNT);

		mJumpingCnt = info.getInt(Constants.TODAY_MO_JUMPING_CNT);


		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_MOTION_JUMPING_CNT, mMotionJumpingAllCount);

		editor.putInt(Constants.TODAY_MO_JUMPING_CNT, mJumpingCnt);

		editor.commit();
	}

	public void setTodayMoPushupData(Bundle info) {
		mMotionPushUpAllCount = info.getInt(Constants.TODAY_MOTION_PUSH_UP_CNT);

		mPushupCnt = info.getInt(Constants.TODAY_MO_PUSH_UP_CNT);


		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_MOTION_PUSH_UP_CNT, mMotionPushUpAllCount);

		editor.putInt(Constants.TODAY_MO_PUSH_UP_CNT, mPushupCnt);

		editor.commit();
	}

	public void setTodayMoSitupData(Bundle info) {
		mMotionSitUpAllCount = info.getInt(Constants.TODAY_MOTION_SIT_UP_CNT);

		mSitupCnt= info.getInt(Constants.TODAY_MO_SIT_UP_CNT);


		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_MOTION_SIT_UP_CNT, mMotionSitUpAllCount);

		editor.putInt(Constants.TODAY_MO_SIT_UP_CNT, mSitupCnt);

		editor.commit();
	}

	public void setTodayMoButterflyData(Bundle info) {
		mMotionButterflyAllCount = info.getInt(Constants.TODAY_MOTION_BUTTERFLY_CNT);

		mButterflyCnt = info.getInt(Constants.TODAY_MO_BUTTERFLY_CNT);


		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_MOTION_BUTTERFLY_CNT, mMotionButterflyAllCount);

		editor.putInt(Constants.TODAY_MO_BUTTERFLY_CNT, mButterflyCnt);

		editor.commit();
	}

	public void setTodayMoCrunchData(Bundle info) {
		mMotionCrunchAllCount = info.getInt(Constants.TODAY_MOTION_CRUNCH_CNT);

		mCrunchCnt = info.getInt(Constants.TODAY_MO_CRUNCH_CNT);


		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_MOTION_CRUNCH_CNT, mMotionCrunchAllCount);

		editor.putInt(Constants.TODAY_MO_CRUNCH_CNT, mCrunchCnt);

		editor.commit();
	}

	public void setTodayMoUprightrowData(Bundle info) {
		mMotionUprightRowAllCount = info.getInt(Constants.TODAY_MOTION_UPRIGHT_ROW_CNT);

		mUprightrowCnt = info.getInt(Constants.TODAY_MO_UPRIGHT_ROW_CNT);


		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_MOTION_UPRIGHT_ROW_CNT, mMotionUprightRowAllCount);

		editor.putInt(Constants.TODAY_MO_UPRIGHT_ROW_CNT, mUprightrowCnt);

		editor.commit();
	}

	public void setTodayMoToeraiseData(Bundle info) {
		mMotionToeRaiseAllCount = info.getInt(Constants.TODAY_MOTION_TOE_RAISE_CNT);

		mToeraiseCnt = info.getInt(Constants.TODAY_MO_TOE_RAISE_CNT);


		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_MOTION_TOE_RAISE_CNT, mMotionToeRaiseAllCount);

		editor.putInt(Constants.TODAY_MO_TOE_RAISE_CNT, mToeraiseCnt);

		editor.commit();
	}

	public void setTodayMoPecflyData(Bundle info) {
		mMotionPecFlyAllCount = info.getInt(Constants.TODAY_MOTION_PEC_FLY_CNT);

		mPecflyCnt = info.getInt(Constants.TODAY_MO_PEC_FLY_CNT);


		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_MOTION_PEC_FLY_CNT, mMotionPecFlyAllCount);

		editor.putInt(Constants.TODAY_MO_PEC_FLY_CNT, mPecflyCnt);

		editor.commit();
	}

	public void setTodayMoChestpressData(Bundle info) {
		mMotionChestPressAllCount = info.getInt(Constants.TODAY_MOTION_CHEST_PRESS_CNT);

		mChestpressCnt= info.getInt(Constants.TODAY_MO_CHEST_PRESS_CNT);


		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//활동 data
		editor.putInt(Constants.TODAY_MOTION_CHEST_PRESS_CNT, mMotionChestPressAllCount);

		editor.putInt(Constants.TODAY_MO_CHEST_PRESS_CNT, mChestpressCnt);

		editor.commit();
	}

	public void setTimeLine(Bundle info){
		mtimeLine1Text = convertToString(info.getStringArrayList(Constants.TODAY_TIME_LINE_1));
		mtimeLine2Text = convertToString(info.getStringArrayList(Constants.TODAY_TIME_LINE_2));
		mtimeLineFlag = integerConvertToString(info.getIntegerArrayList(Constants.TODAY_TIME_LINE_FLAG));
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString(Constants.TODAY_TIME_LINE_1, mtimeLine1Text);
		editor.putString(Constants.TODAY_TIME_LINE_2, mtimeLine2Text);
		editor.putString(Constants.TODAY_TIME_LINE_FLAG, mtimeLineFlag);
		
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
		
		mSleepTimeLineOK = info.getBoolean(Constants.TODAY_SLEEP_TIMELINE_OK);
		
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
		
		editor.putBoolean(Constants.TODAY_SLEEP_TIMELINE_OK, mSleepTimeLineOK);
		
		editor.commit();
	}
	
	public void setTodayLowSleepData(int sleep_time) {
		mLowSleepMin = sleep_time;
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		//수면 data
		editor.putInt(Constants.TODAY_LOW_SLEEP_M, mLowSleepMin);
		
		editor.commit();
	}
	
	public void resetTimeLine(){
		mtimeLine1Text = null;
		mtimeLine2Text = null;
		mtimeLineFlag = null;
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString(Constants.TODAY_TIME_LINE_1, mtimeLine1Text);
		editor.putString(Constants.TODAY_TIME_LINE_2, mtimeLine2Text);
		editor.putString(Constants.TODAY_TIME_LINE_FLAG, mtimeLineFlag);
		
		editor.commit();
	}
	
	
	/**
	 * data 새로 집어 넣을 때만 해라....... 제발........ Reset connection info
	 */
	public void resetActivityPreferences() {
		
		mActivityAllCount = 0;
		mActivityRunAllCount = 0;

		mMotionHammerAllCount = 0;
		mMotionShoulderAllCount = 0;

		mTodayActCnt = null; // 보낼때
		mTodayActCntTimeHour = null;
		mTodayActCntTimeMin = null;


		mWalkDistance = 0;
		mWalkHour = 0;
		mWalkMin = 0;
		mWalkCal = 0;
		mWalkCnt = 0;

		
		mRunDistance = 0;
		mRunHour = 0;
		mRunMin = 0;
		mRunCal = 0;
		mRunCnt = 0;
		
		mtimeLine1Text = null;
		mtimeLine2Text = null;
		
		mActGoalOK =  false;
		mActGoalTimeLineFalse =  false;
		mSleepTimeLineOK = false;

		ULog.e("********************************************************");
		ULog.e("reset Activity Preferences");
		ULog.e("reset Activity Preferences");
		SharedPreferences pref = mContext.getSharedPreferences(Constants.PREFERENCE_NAME_TODAY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        
        editor.clear();
        
        editor.putInt(Constants.TODAY_ACTIVITY_CNT, mActivityAllCount);
		editor.putInt(Constants.TODAY_ACTIVITY_RUN_CNT, mActivityRunAllCount);
		editor.putInt(Constants.TODAY_ACTIVITY_CNT, mMotionHammerAllCount);
		editor.putInt(Constants.TODAY_ACTIVITY_RUN_CNT, mMotionShoulderAllCount);
		editor.putString(Constants.TODAY_ACT_GRAPH_CNT, mTodayActCnt);
		editor.putString(Constants.TODAY_ACT_GRAPH_TIME_HOUR, mTodayActCntTimeHour);
		editor.putString(Constants.TODAY_ACT_GRAPH_TIME_MIN, mTodayActCntTimeMin);

		editor.putInt(Constants.TODAY_ACT_WALK_DIS, mWalkDistance);
		editor.putInt(Constants.TODAY_ACT_WALK_H, mWalkHour);
		editor.putInt(Constants.TODAY_ACT_WALK_M, mWalkMin);
		editor.putInt(Constants.TODAY_ACT_WALK_CAL, mWalkCal);
		editor.putInt(Constants.TODAY_ACT_WALK_CNT, mWalkCnt);

		editor.putInt(Constants.TODAY_ACT_RUN_DIS, mRunDistance);
		editor.putInt(Constants.TODAY_ACT_RUN_H, mRunHour);
		editor.putInt(Constants.TODAY_ACT_RUN_M, mRunMin);
		editor.putInt(Constants.TODAY_ACT_RUN_CAL, mRunCal);
		editor.putInt(Constants.TODAY_ACT_RUN_CNT, mRunCnt);
		editor.putString(Constants.TODAY_TIME_LINE_1, mtimeLine1Text);
		editor.putString(Constants.TODAY_TIME_LINE_2, mtimeLine2Text);
		
		editor.putBoolean(Constants.TODAY_ACT_GOAL_OK, mActGoalOK);
		editor.putBoolean(Constants.TODAY_ACT_GOAL_FALSE, mActGoalTimeLineFalse);
		editor.putBoolean(Constants.TODAY_SLEEP_TIMELINE_OK, mSleepTimeLineOK);
		
        
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

	public int getActivityRunAllCount() {
		return mActivityRunAllCount;
	}

	public int getMotionHammerAllCount() {
		return mMotionHammerAllCount;
	}

	public int getMotionShoulderAllCount() {
		return mMotionShoulderAllCount;
	}

	// public Float getActivityAllKm() {
	// return mActivityAllKm;
	// }

	public ArrayList<Integer> getTodayActCnt() {
		return convertToIntegerArray(mTodayActCnt);
	}

	public ArrayList<Integer> getTodayActCntTimeHour() {
		ULog.v(TAG, "GET Hour");
		return convertToIntegerArray(mTodayActCntTimeHour+"");
	}

	public ArrayList<Integer> getTodayActCntTimeMin() {
		ULog.v(TAG, "GET Min");
		return convertToIntegerArray(mTodayActCntTimeMin+"");
	}
	public ArrayList<String> getTodayTimeLine1() {
		ULog.v(TAG, "GET timeLine");
		return convertToArray(mtimeLine1Text);
	}
	public ArrayList<String> getTodayTimeLine2() {
		ULog.v(TAG, "GET timeLine");
		return convertToArray(mtimeLine2Text);
	}
	
	public ArrayList<Integer> getTodayTimeLineFlag() {
		ULog.v(TAG, "GET timeLineflag");
		return convertToIntegerArray(mtimeLineFlag);
	}
	
	public boolean getUserActGoalOK(){
		return mActGoalOK;
	}
	public boolean getUserActGoalTimeLineFalse(){
		return mActGoalTimeLineFalse;
	}
	
	public boolean getSleepTimeLineOK(){
		return mSleepTimeLineOK;
	}

	public int getWalkDistance() {
		return mWalkDistance;
	}

	 public int getWalkHour() {
	 	return mWalkHour;
	 }

	public int getWalkMin() {
		return mWalkMin;
	}

	public int getWalkCal() {
		return mWalkCal;
	}

	public int getWalkCnt() {
		return mWalkCnt;
	}

	public int getRunDistance() {
		return mRunDistance;
	}

	 public int getRunHour() {
		 return mRunHour;
	 }

	public int getRunMin() {
		return mRunMin;
	}

	public int getRunCal() {
		return mRunCal;
	}

	public int getRunCnt() {
		return mRunCnt;
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
		ULog.v(TAG, "실제 수면 시간 ~동안잠 ");
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
			delim = ";";
		}
		return sb.toString();
	}

	private ArrayList<String> convertToArray(String string) {
		try {
			ArrayList<String> list = new ArrayList<String>(Arrays.asList(string.split(";")));
			return list;
		} catch (Exception e) {
			return null;
		}
		
	}

}
