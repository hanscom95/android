package se.com.band.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User 정보/app 버전 정보
 * 
 * @author taehoon
 *
 */
public class Preferences {
	// Constants

	// Instance
	private static Preferences mInstance = null;

	private Context mContext;
	private String mVersion = null;
	private String mLastUseDate = null;
	private int mUid = 0;

	private String mUserId = null;
	private String mUserName = null;
	private String mUserPw = null;
	private String mUserBirth = null;
	private String mUserGender = null;
	private int mUserTall = 0;
	private int mUserWeight = 0;
	private String mUserDeviceAdderess = null;
	private String mUserDeviceName = null;
	private int mUserTallUnit = 0;
	private int mUserWeigthUnit = 0;
	private int mUserDisUnit = 0;
	private int mUserHand = 0;
	private int mUserAge = 0;

	private int using_date = 0;

	// notification setting
	private boolean mCallNoti = false;
	private boolean mSmsNoti = false;
	private boolean mSnsNoti = false;
	private boolean mDeviceConn = false;
	private boolean mMoveNoti = false;

	private String mSnsFirst = null;
	private String mSnsSecond = null;
	private String mSnsThird = null;
	
	private String mAlertEveryTime = null;
	private String mAlertStart = null;
	private String mAlertEnd = null;

	private String mAlarmTimeText = null;
	private String mAlarmOnOff = null;

	private int mDailyTotalStep = 0;
	private int mDailyTotalDistance = 0;
	private int mDailyTotalKcal = 0;
	private String mDailyTotalTime = null;

	private int mWeeklyTotalStep = 0;
	private int mWeeklyTotalDistance = 0;
	private int mWeeklyTotalKcal = 0;
	private String mWeeklyTotalTime = null;

	private int mMonthlyTotalStep = 0;
	private int mMonthlyTotalDistance = 0;
	private int mMonthlyTotalKcal = 0;
	private String mMonthlyTotalTime = null;

	private int mYearlyTotalStep = 0;
	private int mYearlyTotalDistance = 0;
	private int mYearlyTotalKcal = 0;
	private String mYearlyTotalTime = null;




	private Preferences(Context c) {
		mContext = c;

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		mVersion = prefs.getString(Constants.PREFERENCE_VERSION_OF_API, "");
		mLastUseDate = prefs.getString(Constants.LAST_USE_DATE, "");

		mUserId = prefs.getString(Constants.PREFERENCE_USER_ID, "");
		mUid = prefs.getInt(Constants.PREFERENCE_UID, 0);
		mUserName = prefs.getString(Constants.PREFERENCE_USER_NM, "");
		mUserPw = prefs.getString(Constants.PREFERENCE_USER_PW, "");
		mUserBirth = prefs.getString(Constants.PREFERENCE_USER_BIRTH, "");
		mUserGender = prefs.getString(Constants.PREFERENCE_USER_GENDER, "male");
		mUserTall = prefs.getInt(Constants.PREFERENCE_USER_TALL,0);
		mUserWeight = prefs.getInt(Constants.PREFERENCE_USER_WEIGHT, 0);
		mUserTallUnit = prefs.getInt(Constants.PREFERENCE_USER_TALL_UNIT, 0);
		mUserWeigthUnit = prefs.getInt(Constants.PREFERENCE_USER_WEIGHT_UNIT, 0);
		mUserDeviceAdderess = prefs.getString(Constants.PREFERENCE_USER_DEVICEADDRESS, "");
		mUserDeviceName = prefs.getString(Constants.PREFERENCE_USER_DEVICENAME, "");
		mUserHand = prefs.getInt(Constants.PREFERENCE_USER_HAND,0);
		mUserAge = prefs.getInt(Constants.PREFERENCE_USER_AGE,0);

		mUserDisUnit = prefs.getInt(Constants.PREFERENCE_DIS_UNIT, 0);
		
		using_date = prefs.getInt(Constants.PREFERENCE_USING_DATE, 0);

		// setting
		mCallNoti = prefs.getBoolean(Constants.PREFERENCE_CALL_NOTI, false);
		mSmsNoti = prefs.getBoolean(Constants.PREFERENCE_SMS_NOTI, false);
		mSnsNoti = prefs.getBoolean(Constants.PREFERENCE_SNS_NOTI, false);
		mMoveNoti = prefs.getBoolean(Constants.PREFERENCE_MOVE_NOTI, false);
		mDeviceConn = prefs.getBoolean(Constants.PREFERENCE_DEVICE_CONN, false);

		mAlertEveryTime = prefs.getString(Constants.PREFERENCE_MOVE_EVERY_TEXT, null);
		mAlertStart = prefs.getString(Constants.PREFERENCE_MOVE_START_TEXT, null);
		mAlertEnd = prefs.getString(Constants.PREFERENCE_MOVE_END_TEXT, null);

		mAlarmTimeText = prefs.getString(Constants.PREFERENCE_ALARM_TIME_TEXT, null);
		mAlarmOnOff = prefs.getString(Constants.PREFERENCE_ALARM_ONOFF, null);

		mSnsFirst = prefs.getString(Constants.PREFERENCE_MOVE_EVERY_TEXT, null);
		mSnsSecond = prefs.getString(Constants.PREFERENCE_MOVE_START_TEXT, null);
		mSnsThird = prefs.getString(Constants.PREFERENCE_MOVE_END_TEXT, null);



		mDailyTotalStep = prefs.getInt(Constants.PREFERENCE_DAILY_TOTAL_STEP, 0);
		mDailyTotalDistance = prefs.getInt(Constants.PREFERENCE_DAILY_TOTAL_DISTANCE, 0);
		mDailyTotalKcal = prefs.getInt(Constants.PREFERENCE_DAILY_TOTAL_KCAL, 0);
		mDailyTotalTime = prefs.getString(Constants.PREFERENCE_DAILY_TOTAL_TIME, null);

		mWeeklyTotalStep = prefs.getInt(Constants.PREFERENCE_WEEKLY_TOTAL_STEP, 0);
		mWeeklyTotalDistance = prefs.getInt(Constants.PREFERENCE_WEEKLY_TOTAL_DISTANCE, 0);
		mWeeklyTotalKcal = prefs.getInt(Constants.PREFERENCE_WEEKLY_TOTAL_KCAL, 0);
		mWeeklyTotalTime = prefs.getString(Constants.PREFERENCE_WEEKLY_TOTAL_TIME, null);

		mMonthlyTotalStep = prefs.getInt(Constants.PREFERENCE_MONTHLY_TOTAL_STEP, 0);
		mMonthlyTotalDistance = prefs.getInt(Constants.PREFERENCE_MONTHLY_TOTAL_DISTANCE, 0);
		mMonthlyTotalKcal = prefs.getInt(Constants.PREFERENCE_MONTHLY_TOTAL_KCAL, 0);
		mMonthlyTotalTime = prefs.getString(Constants.PREFERENCE_MONTHLY_TOTAL_TIME, null);

		mYearlyTotalStep = prefs.getInt(Constants.PREFERENCE_YEARLY_TOTAL_STEP, 0);
		mYearlyTotalDistance = prefs.getInt(Constants.PREFERENCE_YEARLY_TOTAL_DISTANCE, 0);
		mYearlyTotalKcal = prefs.getInt(Constants.PREFERENCE_YEARLY_TOTAL_KCAL, 0);
		mYearlyTotalTime = prefs.getString(Constants.PREFERENCE_YEARLY_TOTAL_TIME, null);


	}

	/**
	 * Single pattern
	 */
	public synchronized static Preferences getInstance(Context c) {
		if (mInstance == null) {
			if (c != null)
				mInstance = new Preferences(c);
			else
				return null;
		}
		return mInstance;
	}

	/**
	 * Reset connection info
	 */
	public void resetPreferences() {
		mUid = 0;
		mUserId = "";
		mUserName = "";
		mUserBirth = null;
		mUserGender = null;
		mUserTall = 0;
		mUserWeight = 0;
		mUserTallUnit = 0;
		mUserWeigthUnit = 0;
		mUserDeviceAdderess = null;
		mUserDeviceName = null;
		using_date = 0;
		mUserDisUnit = 0;
		mUserHand = 0;
		mUserAge = 0;


		mSnsFirst = null;
		mSnsSecond = null;
		mSnsThird= null;

		mAlertEveryTime = null;
		mAlertStart = null;
		mAlertEnd = null;

		mAlarmTimeText = null;
		mAlarmOnOff= null;

		mCallNoti = false;
		mSmsNoti = false;
		mSnsNoti = false;
		mDeviceConn = false;
		mMoveNoti = false;


		mDailyTotalStep = 0;
		mDailyTotalDistance = 0;
		mDailyTotalKcal = 0;
		mDailyTotalTime = null;

		mWeeklyTotalStep = 0;
		mWeeklyTotalDistance = 0;
		mWeeklyTotalKcal = 0;
		mWeeklyTotalTime = null;

		mMonthlyTotalStep = 0;
		mMonthlyTotalDistance = 0;
		mMonthlyTotalKcal = 0;
		mMonthlyTotalTime = null;

		mYearlyTotalStep = 0;
		mYearlyTotalDistance = 0;
		mYearlyTotalKcal = 0;
		mYearlyTotalTime = null;





		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Constants.PREFERENCE_UID, mUid);
		editor.putString(Constants.PREFERENCE_USER_ID, mUserId);
		editor.putString(Constants.PREFERENCE_USER_NM, mUserName);
		editor.putString(Constants.PREFERENCE_USER_PW , mUserPw );
		editor.putString(Constants.PREFERENCE_USER_BIRTH, mUserBirth);
		editor.putString(Constants.PREFERENCE_USER_GENDER, mUserGender);
		editor.putInt(Constants.PREFERENCE_USER_TALL, mUserTall);
		editor.putInt(Constants.PREFERENCE_USER_WEIGHT, mUserWeight);
		editor.putInt(Constants.PREFERENCE_USER_TALL_UNIT, mUserTallUnit);
		editor.putInt(Constants.PREFERENCE_USER_WEIGHT_UNIT, mUserWeigthUnit);
		editor.putInt(Constants.PREFERENCE_DIS_UNIT, mUserDisUnit);
		editor.putInt(Constants.PREFERENCE_USER_HAND, mUserHand);
		editor.putInt(Constants.PREFERENCE_USER_AGE, mUserAge);
		editor.putString(Constants.PREFERENCE_USER_DEVICEADDRESS, mUserDeviceAdderess);
		editor.putString(Constants.PREFERENCE_USER_DEVICENAME, mUserDeviceName);
		editor.putInt(Constants.PREFERENCE_USING_DATE, using_date);

		editor.putString(Constants.PREFERENCE_ALERT_EVERY_TEXT, mAlertEveryTime);
		editor.putString(Constants.PREFERENCE_ALERT_START_TEXT, mAlertStart);
		editor.putString(Constants.PREFERENCE_ALERT_END_TEXT, mAlertEnd);

		editor.putString(Constants.PREFERENCE_SNS_FIRST_TEXT, mSnsFirst);
		editor.putString(Constants.PREFERENCE_SNS_SECOND_TEXT, mSnsSecond);
		editor.putString(Constants.PREFERENCE_SNS_THIRD_TEXT, mSnsThird);

		editor.putBoolean(Constants.PREFERENCE_CALL_NOTI, mCallNoti);
		editor.putBoolean(Constants.PREFERENCE_SMS_NOTI, mSmsNoti);
		editor.putBoolean(Constants.PREFERENCE_SNS_NOTI, mSnsNoti);
		editor.putBoolean(Constants.PREFERENCE_DEVICE_CONN, mDeviceConn);
		editor.putBoolean(Constants.PREFERENCE_MOVE_NOTI, mMoveNoti);

		editor.putString(Constants.PREFERENCE_ALARM_TIME_TEXT, mAlarmTimeText);
		editor.putString(Constants.PREFERENCE_ALARM_ONOFF, mAlarmOnOff);


		editor.putInt(Constants.PREFERENCE_DAILY_TOTAL_STEP, mDailyTotalStep);
		editor.putInt(Constants.PREFERENCE_DAILY_TOTAL_DISTANCE, mDailyTotalDistance);
		editor.putInt(Constants.PREFERENCE_DAILY_TOTAL_KCAL, mDailyTotalKcal);
		editor.putString(Constants.PREFERENCE_DAILY_TOTAL_TIME, mDailyTotalTime);

		editor.putInt(Constants.PREFERENCE_WEEKLY_TOTAL_STEP, mWeeklyTotalStep);
		editor.putInt(Constants.PREFERENCE_WEEKLY_TOTAL_DISTANCE, mWeeklyTotalDistance);
		editor.putInt(Constants.PREFERENCE_WEEKLY_TOTAL_KCAL, mWeeklyTotalKcal);
		editor.putString(Constants.PREFERENCE_WEEKLY_TOTAL_TIME, mWeeklyTotalTime);

		editor.putInt(Constants.PREFERENCE_MONTHLY_TOTAL_STEP, mMonthlyTotalStep);
		editor.putInt(Constants.PREFERENCE_MONTHLY_TOTAL_DISTANCE, mMonthlyTotalDistance);
		editor.putInt(Constants.PREFERENCE_MONTHLY_TOTAL_KCAL, mMonthlyTotalKcal);
		editor.putString(Constants.PREFERENCE_MONTHLY_TOTAL_TIME, mMonthlyTotalTime);

		editor.putInt(Constants.PREFERENCE_YEARLY_TOTAL_STEP, mYearlyTotalStep);
		editor.putInt(Constants.PREFERENCE_YEARLY_TOTAL_DISTANCE, mYearlyTotalDistance);
		editor.putInt(Constants.PREFERENCE_YEARLY_TOTAL_KCAL, mYearlyTotalKcal);
		editor.putString(Constants.PREFERENCE_YEARLY_TOTAL_TIME, mYearlyTotalTime);

		editor.commit();

	}
	
	public String getLastUseDate(){
		return mLastUseDate;
	}
	
	public void setUid(int i) {
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		mUid=i;
		editor.putInt(Constants.PREFERENCE_UID, mUid);
		editor.commit();
	}
	

	public void setLastUseDate(String date) {
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Constants.LAST_USE_DATE, date);
		editor.commit();
	}

	/**
	 * Remember device name for future use
	 * 
	 * @param version
	 *            device name
	 */
	public void setVersion(String version) {
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Constants.PREFERENCE_VERSION_OF_API, version);
		editor.commit();
	}

	public void setUserInfo(Bundle info) {
		mUid = info.getInt(Constants.UID);
		mUserId = info.getString(Constants.USER_ID);
		mUserName = info.getString(Constants.USER_NAME);
		mUserPw = info.getString(Constants.USER_PW); //탈퇴시 필
		
		
		mUserBirth = info.getString(Constants.USER_BIRTH);
		mUserGender = info.getString(Constants.USER_GENDER);
		mUserTall = info.getInt(Constants.USER_TALL);
		mUserWeight = info.getInt(Constants.USER_WEIGHT);
		mUserHand = info.getInt(Constants.USER_HAND);
		mUserAge = info.getInt(Constants.USER_AGE);

		mUserDeviceAdderess = info.getString(Constants.EXTRA_DEVICE_ADDRESS);
		mUserDeviceName = info.getString(Constants.EXTRA_DEVICE_NAME);
		using_date = info.getInt(Constants.PREFERENCE_USING_DATE);

		ULog.v("PREFERENCE", "info.getString(Constants.USER_ID)===>" + info.getString(Constants.USER_ID));
		ULog.v("PREFERENCE", "info.getString(Constants.USER_PW)===>" + info.getString(Constants.USER_PW));
		ULog.v("PREFERENCE", "info.getString(Constants.USER_NAME)===>" + info.getString(Constants.USER_NAME));
		ULog.v("PREFERENCE", "info.getString(Constants.USER_BIRTH)===>" + info.getString(Constants.USER_BIRTH));
		ULog.v("PREFERENCE", "info.getString(Constants.USER_GENDER)===>" + info.getString(Constants.USER_GENDER));
		ULog.v("PREFERENCE", "info.getInt(Constants.USER_TALL)===>" + info.getInt(Constants.USER_TALL));
		ULog.v("PREFERENCE", "info.getInt(Constants.USER_WEIGHT)===>" + info.getInt(Constants.USER_WEIGHT));
		ULog.v("PREFERENCE",
				"info.getString(Constants.USER_ACTIVITY_GOAL)===>" + info.getString(Constants.USER_ACTIVITY_GOAL));
		ULog.v("PREFERENCE",
				"info.getString(Constants.USER_SLEEP_GOAL)===>" + info.getString(Constants.USER_SLEEP_GOAL));
		ULog.v("PREFERENCE", "info.getString(Constants.USER_TALL_UNIT)===>" + info.getString(Constants.USER_TALL_UNIT));
		ULog.v("PREFERENCE",
				"info.getString(Constants.USER_WEIGHT_UNIT)===>" + info.getString(Constants.USER_WEIGHT_UNIT));
		ULog.v("PREFERENCE",
				"info.getString(Constants.EXTRA_DEVICE_ADDRESS)===>" + info.getString(Constants.EXTRA_DEVICE_ADDRESS));

		ULog.v("PREFERENCE", "mUserId===>" + mUserId);
		ULog.v("PREFERENCE", "mUserPw===>" + mUserPw);
		ULog.v("PREFERENCE", "mUserName===>" + mUserName);
		ULog.v("PREFERENCE", "mUserBirth===>" + mUserBirth);
		ULog.v("PREFERENCE", "mUserGender===>" + mUserGender);
		ULog.v("PREFERENCE", "mUserTall===>" + mUserTall);
		ULog.v("PREFERENCE", "mUserWeigth===>" + mUserWeight);
		ULog.v("PREFERENCE", "mUserDeviceAdderess===>" + mUserDeviceAdderess);
		ULog.v("PREFERENCE", "mUserDeviceName===>" + mUserDeviceName);

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Constants.PREFERENCE_UID, mUid);
		editor.putString(Constants.PREFERENCE_USER_ID, mUserId);
		editor.putString(Constants.PREFERENCE_USER_NM, mUserName);
		editor.putString(Constants.PREFERENCE_USER_PW , mUserPw );
		editor.putString(Constants.PREFERENCE_USER_BIRTH, mUserBirth);
		editor.putString(Constants.PREFERENCE_USER_GENDER, mUserGender);
		editor.putInt(Constants.PREFERENCE_USER_TALL, mUserTall);
		editor.putInt(Constants.PREFERENCE_USER_WEIGHT, mUserWeight);

		editor.putString(Constants.PREFERENCE_USER_DEVICEADDRESS, mUserDeviceAdderess);
		editor.putString(Constants.PREFERENCE_USER_DEVICENAME, mUserDeviceName);
		editor.putInt(Constants.PREFERENCE_USING_DATE, using_date);
		editor.putInt(Constants.PREFERENCE_USER_HAND, mUserHand);
		editor.putInt(Constants.PREFERENCE_USER_AGE, mUserAge);
		editor.commit();
	}
	
	public void setUserUnit(int tallunit,int weightunit){
		mUserTallUnit = tallunit;
		mUserWeigthUnit = weightunit;
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putInt(Constants.PREFERENCE_USER_TALL_UNIT, mUserTallUnit);
		editor.putInt(Constants.PREFERENCE_USER_WEIGHT_UNIT, mUserWeigthUnit);
		editor.putInt(Constants.PREFERENCE_DIS_UNIT, mUserDisUnit);
		
		editor.commit();
	}
	
	public void setAlert(Bundle info) {
		mMoveNoti = info.getBoolean(Constants.PREFERENCE_MOVE_NOTI);
		mAlertEveryTime = info.getString(Constants.PREFERENCE_ALERT_EVERY_TEXT);
		mAlertStart = info.getString(Constants.PREFERENCE_ALERT_START_TEXT);
		mAlertEnd = info.getString(Constants.PREFERENCE_ALERT_END_TEXT);

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putBoolean(Constants.PREFERENCE_MOVE_NOTI, mMoveNoti);
		editor.putString(Constants.PREFERENCE_ALERT_EVERY_TEXT, mAlertEveryTime);
		editor.putString(Constants.PREFERENCE_ALERT_START_TEXT, mAlertStart);
		editor.putString(Constants.PREFERENCE_ALERT_END_TEXT, mAlertEnd);

		editor.commit();
	}
	
	public void resetAlert() {
		mMoveNoti = false;
		mAlertEveryTime = null;
		mAlertStart = null;
		mAlertEnd = null;
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString(Constants.PREFERENCE_ALERT_EVERY_TEXT, mAlertEveryTime);
		editor.putString(Constants.PREFERENCE_ALERT_START_TEXT, mAlertStart);
		editor.putString(Constants.PREFERENCE_ALERT_END_TEXT, mAlertEnd);

		editor.commit();
	}
	
	public String getAlertTimeTxt(){
		return mAlertEveryTime;
	}
	public String getAlertStartTxt(){
		return mAlertStart;
	}
	public String getAlertEndTxt(){
		return mAlertEnd;
	}


	public void setAlarms(Bundle info) {
		mAlarmTimeText = convertToString(info.getStringArrayList(Constants.PREFERENCE_ALARM_TIME_TEXT));
		mAlarmOnOff = convertToString(info.getStringArrayList(Constants.PREFERENCE_ALARM_ONOFF));

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(Constants.PREFERENCE_ALARM_TIME_TEXT, mAlarmTimeText);
		editor.putString(Constants.PREFERENCE_ALARM_ONOFF, mAlarmOnOff);

		editor.commit();
	}

	public void resetAlarms() {
		mAlarmTimeText = null;
		mAlarmOnOff = null;

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(Constants.PREFERENCE_ALARM_TIME_TEXT, mAlarmTimeText);
		editor.putString(Constants.PREFERENCE_ALARM_ONOFF, mAlarmOnOff);

		editor.commit();
	}

	public ArrayList<String> getAlarmTimeTxt(){
		return convertToArray(mAlarmTimeText);
	}
	public ArrayList<String> getAlarmOnOff(){
		return convertToArray(mAlarmOnOff);
	}

	public String getUserId() {
		return mUserId;
	}
	
	public int getUid() {
		return mUid;
	}
	public int getUsingDate() {
		return using_date;
	}
	public void setUsingDate(int d) {
		using_date=d;
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Constants.PREFERENCE_USING_DATE, using_date);
	}
	public void setAllDis(int d) {
		mUserDisUnit=d;
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Constants.PREFERENCE_DIS_UNIT, mUserDisUnit);
	}
	
	public int getmUserDisUnit() {
		return mUserDisUnit;
	}

	/**
	 * Get device address string
	 * 
	 * @return String device address
	 */
	public String getVersion() {
		return mVersion;
	}

	public void setSetting(Bundle setInfo) {
		mCallNoti = setInfo.getBoolean(Constants.PREFERENCE_CALL_NOTI, false);
		mSmsNoti = setInfo.getBoolean(Constants.PREFERENCE_SMS_NOTI, false);
		mSnsNoti = setInfo.getBoolean(Constants.PREFERENCE_SNS_NOTI, false);
		mMoveNoti = setInfo.getBoolean(Constants.PREFERENCE_MOVE_NOTI, false);
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(Constants.PREFERENCE_CALL_NOTI, mCallNoti);
		editor.putBoolean(Constants.PREFERENCE_SMS_NOTI, mSmsNoti);
		editor.putBoolean(Constants.PREFERENCE_SNS_NOTI, mSnsNoti);
		editor.putBoolean(Constants.PREFERENCE_MOVE_NOTI, mMoveNoti);
		editor.commit();
	}
	
	
	public void setHeightUnit(int unit) {
		mUserTallUnit = unit;

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Constants.PREFERENCE_USER_TALL_UNIT, mUserTallUnit);
		editor.commit();
	}
	
	public void setWeightUnit(int unit) {
		mUserWeigthUnit = unit;
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Constants.PREFERENCE_USER_WEIGHT_UNIT, mUserWeigthUnit);
		editor.commit();
	}
	
	public boolean getCAllNoti(){
		return mCallNoti;
	}
	public boolean getSMSNoti(){
		return mSmsNoti;
	}
	public boolean getSnsNoti(){
		return mSnsNoti;
	}
	public boolean getDeviceConn(){
		return mDeviceConn;
	}
	public void setDeviceConn(boolean deviceConn){
		mDeviceConn=deviceConn;
	}
	public boolean getMoveNoti(){
		return mMoveNoti;
	}
	
	public String getUserName() {
		return mUserName;
	}
	public void setUserId(String id) {
		mUserId= id;
	}
	public void setUserName(String name) {
		mUserName= name;
	}

	public String getUserPw() {
		return mUserPw;
	}

	public String getUserBirth() {
		return mUserBirth;
	}
	public void setUserBirth(String birth) {
		mUserBirth = birth;
	}

	public String getUserGender() {
		return mUserGender;
	}
	public void setUserGender(String g) {
		mUserGender= g;
	}
	
	public int getUserTall() {
		return mUserTall;
	}
	public void setUserTall(int t) {
		mUserTall= t;
	}

	public int getUserWeight() {
		return mUserWeight;
	}
	public void setUserWeight(int w) {
		mUserWeight= w;
	}

	public int getUserTallUnit() {
		return mUserTallUnit;
	}
	public int getUserWeigthUnit() {
		return mUserWeigthUnit;
	}

	public String getUserDeviceAdderess() {
		return mUserDeviceAdderess;
	}
	public String getmUserDeviceName() {
		return mUserDeviceName;
	}
	public void setUserDevice(String device, String name) {
		mUserDeviceAdderess = device;
		mUserDeviceName = name;

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Constants.PREFERENCE_USER_DEVICEADDRESS, mUserDeviceAdderess);
		editor.putString(Constants.PREFERENCE_USER_DEVICENAME, mUserDeviceName);
		editor.commit();
	}

	private String convertToString(ArrayList<String> list) {

		StringBuilder sb = new StringBuilder();
		String delim = "";
		for (String s : list) {
			sb.append(delim);
			sb.append(s);
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


	public void setDailyTotalActivity(int step, int distance, int kcal, String time) {
		mDailyTotalStep = step;
		mDailyTotalDistance = distance;
		mDailyTotalKcal = kcal;
		mDailyTotalTime = time;

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Constants.PREFERENCE_DAILY_TOTAL_STEP, mDailyTotalStep);
		editor.putInt(Constants.PREFERENCE_DAILY_TOTAL_DISTANCE, mDailyTotalDistance);
		editor.putInt(Constants.PREFERENCE_DAILY_TOTAL_KCAL, mDailyTotalKcal);
		editor.putString(Constants.PREFERENCE_DAILY_TOTAL_TIME, mDailyTotalTime);
		editor.commit();
	}


	public void setWeeklyTotalActivity(int step, int distance, int kcal, String time) {
		mWeeklyTotalStep = step;
		mWeeklyTotalDistance = distance;
		mWeeklyTotalKcal = kcal;
		mWeeklyTotalTime = time;

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Constants.PREFERENCE_WEEKLY_TOTAL_STEP, mWeeklyTotalStep);
		editor.putInt(Constants.PREFERENCE_WEEKLY_TOTAL_DISTANCE, mWeeklyTotalDistance);
		editor.putInt(Constants.PREFERENCE_WEEKLY_TOTAL_KCAL, mWeeklyTotalKcal);
		editor.putString(Constants.PREFERENCE_WEEKLY_TOTAL_TIME, mWeeklyTotalTime);
		editor.commit();
	}


	public void setMonthlyTotalActivity(int step, int distance, int kcal, String time) {
		mMonthlyTotalStep = step;
		mMonthlyTotalDistance = distance;
		mMonthlyTotalKcal = kcal;
		mMonthlyTotalTime = time;

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Constants.PREFERENCE_MONTHLY_TOTAL_STEP, mMonthlyTotalStep);
		editor.putInt(Constants.PREFERENCE_MONTHLY_TOTAL_DISTANCE, mMonthlyTotalDistance);
		editor.putInt(Constants.PREFERENCE_MONTHLY_TOTAL_KCAL, mMonthlyTotalKcal);
		editor.putString(Constants.PREFERENCE_MONTHLY_TOTAL_TIME, mMonthlyTotalTime);
		editor.commit();
	}


	public void setYearlyTotalActivity(int step, int distance, int kcal, String time) {
		mYearlyTotalStep = step;
		mYearlyTotalDistance = distance;
		mYearlyTotalKcal = kcal;
		mYearlyTotalTime = time;

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Constants.PREFERENCE_YEARLY_TOTAL_STEP, mYearlyTotalStep);
		editor.putInt(Constants.PREFERENCE_YEARLY_TOTAL_DISTANCE, mYearlyTotalDistance);
		editor.putInt(Constants.PREFERENCE_YEARLY_TOTAL_KCAL, mYearlyTotalKcal);
		editor.putString(Constants.PREFERENCE_YEARLY_TOTAL_TIME, mYearlyTotalTime);
		editor.commit();
	}

	public int getDailyTotalStep(){
		return mDailyTotalStep;
	}
	public int getDailyTotalKcal(){
		return mDailyTotalDistance;
	}
	public int getmDailyTotalDistance(){
		return mDailyTotalKcal;
	}
	public String getDailyTotalTime(){
		return mDailyTotalTime;
	}

	public int getWeeklyTotalStep(){
		return mWeeklyTotalStep;
	}
	public int getWeeklyTotalKcal(){
		return mWeeklyTotalDistance;
	}
	public int getmWeeklyTotalDistance(){
		return mWeeklyTotalKcal;
	}
	public String getWeeklyTotalTime(){
		return mWeeklyTotalTime;
	}

	public int getMonthlyTotalStep(){
		return mMonthlyTotalStep;
	}
	public int getMonthlyTotalKcal(){
		return mMonthlyTotalDistance;
	}
	public int getmMonthlyTotalDistance(){
		return mMonthlyTotalKcal;
	}
	public String getMonthlyTotalTime(){
		return mMonthlyTotalTime;
	}

	public int getYearlyTotalStep(){
		return mYearlyTotalStep;
	}
	public int getYearlyTotalKcal(){
		return mYearlyTotalDistance;
	}
	public int getmYearlyTotalDistance(){
		return mYearlyTotalKcal;
	}
	public String getYearlyTotalTime(){
		return mYearlyTotalTime;
	}
}
