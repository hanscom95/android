package com.standingegg.band.util;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * User 정보/app 버전 정보
 * 
 * @author cczzee
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
	private int mUserActivityGoal = 0;
	private int mUserSleepGoal = 0;
	private String mUserDeviceAdderess = null;
	private String mUserTallUnit = null;
	private String mUserWeigthUnit = null;
	private int mUserAllDis = 0;
	
	private int using_date = 0;

	// user setting
	private boolean mCallNoti = false;
	private boolean mSmsNoti = false;
	private boolean mKakaoNoti = false;
	private boolean mDeviceConn = true;

	private int mCallLedColor = 1;
	private int mSmsLedColor = 2;
	private int mKakaoLedColor = 3;
	
	private String mAlarmTimeText = null;
	private String mAlarmDayText = null;
	private String mAlarmOnOff = null;
	private String mAlarmDaySText = null;

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
		mUserGender = prefs.getString(Constants.PREFERENCE_USER_GENDER, "");
		mUserTall = prefs.getInt(Constants.PREFERENCE_USER_TALL,0);
		mUserWeight = prefs.getInt(Constants.PREFERENCE_USER_WEIGHT, 0);
		mUserActivityGoal = prefs.getInt(Constants.PREFERENCE_USER_ACTIVITY_GOAL, 1000);
		mUserSleepGoal = prefs.getInt(Constants.PREFERENCE_USER_SLEEP_GOAL, 0);
		mUserTallUnit = prefs.getString(Constants.PREFERENCE_USER_TALL_UNIT, "");
		mUserWeigthUnit = prefs.getString(Constants.PREFERENCE_USER_WEIGHT_UNIT, "");
		mUserDeviceAdderess = prefs.getString(Constants.PREFERENCE_USER_DEVICEADDRESS, "");
		
		mUserAllDis = prefs.getInt(Constants.PREFERENCE_ALL_DIS, 0);
		
		using_date = prefs.getInt(Constants.PREFERENCE_USING_DATE, 0);

		// setting
		mCallNoti = prefs.getBoolean(Constants.PREFERENCE_CALL_NOTI, false);
		mSmsNoti = prefs.getBoolean(Constants.PREFERENCE_SMS_NOTI, false);
		mKakaoNoti = prefs.getBoolean(Constants.PREFERENCE_KAKAO_NOTI, false);
		mDeviceConn = prefs.getBoolean(Constants.PREFERENCE_DEVICE_CONN, false);
		
		mCallLedColor = prefs.getInt(Constants.PREFERENCE_CALL_LED_COLOR, 1);
		mSmsLedColor = prefs.getInt(Constants.PREFERENCE_SMS_LED_COLOR, 2);
		mKakaoLedColor = prefs.getInt(Constants.PREFERENCE_KAKAO_LED_COLOR, 3);
		
		mAlarmTimeText = prefs.getString(Constants.PREFERENCE_ALARM_TIME_TEXT, null);
		mAlarmDayText = prefs.getString(Constants.PREFERENCE_ALARM_DAY_TEXT, null);
		mAlarmOnOff = prefs.getString(Constants.PREFERENCE_ALARM_ONOFF, null);
		mAlarmDaySText = prefs.getString(Constants.PREFERENCE_ALARM_DAYS_TEXT, null);
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
		mUserName = null;
		mUserPw = null; //pw 를 저장할 필요가 있나..?흠?
		mUserBirth = null;
		mUserGender = null;
		mUserTall = 0;
		mUserWeight = 0;
		mUserActivityGoal = 0;
		mUserSleepGoal = 0;
		mUserTallUnit = "cm";
		mUserWeigthUnit = "kg";
		mUserDeviceAdderess = null;
		using_date = 0;
		
		mAlarmTimeText = null;
		mAlarmDayText = null;
		mAlarmOnOff= null;
		mAlarmDaySText = null;

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
		editor.putInt(Constants.PREFERENCE_USER_ACTIVITY_GOAL, mUserActivityGoal);
		editor.putInt(Constants.PREFERENCE_USER_SLEEP_GOAL, mUserSleepGoal);
		editor.putString(Constants.PREFERENCE_USER_TALL_UNIT, mUserTallUnit);
		editor.putString(Constants.PREFERENCE_USER_WEIGHT_UNIT, mUserWeigthUnit);
		editor.putString(Constants.PREFERENCE_USER_DEVICEADDRESS, mUserDeviceAdderess);
		editor.putInt(Constants.PREFERENCE_USING_DATE, using_date);
		
		editor.putString(Constants.PREFERENCE_ALARM_TIME_TEXT, mAlarmTimeText);
		editor.putString(Constants.PREFERENCE_ALARM_DAY_TEXT, mAlarmDayText);
		editor.putString(Constants.PREFERENCE_ALARM_ONOFF, mAlarmOnOff);
		editor.putString(Constants.PREFERENCE_ALARM_DAYS_TEXT, mAlarmDaySText);
		
		editor.commit();

	}
	
	public String getLastUseDate(){
		return mLastUseDate;
	}
	
	public void setUid(int i) {
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		mUid=i;
		editor.putInt(Constants.PREFERENCE_UID, i);
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
	 * @param name
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
		mUserActivityGoal = info.getInt(Constants.USER_ACTIVITY_GOAL);
		mUserSleepGoal = info.getInt(Constants.USER_SLEEP_GOAL);
		mUserTallUnit = info.getString(Constants.USER_TALL_UNIT);
		mUserWeigthUnit = info.getString(Constants.USER_WEIGHT_UNIT);
		mUserDeviceAdderess = info.getString(Constants.EXTRA_DEVICE_ADDRESS);
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
		ULog.v("PREFERENCE", "mUserActivityGoal===>" + mUserActivityGoal);
		ULog.v("PREFERENCE", "mUserSleepGoal===>" + mUserSleepGoal);
		ULog.v("PREFERENCE", "mUserTallUnit===>" + mUserTallUnit);
		ULog.v("PREFERENCE", "mUserWeigthUnit===>" + mUserWeigthUnit);
		ULog.v("PREFERENCE", "mUserDeviceAdderess===>" + mUserDeviceAdderess);

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
		editor.putInt(Constants.PREFERENCE_USER_ACTIVITY_GOAL, mUserActivityGoal);
		editor.putInt(Constants.PREFERENCE_USER_SLEEP_GOAL, mUserSleepGoal);
		editor.putString(Constants.PREFERENCE_USER_TALL_UNIT, mUserTallUnit);
		editor.putString(Constants.PREFERENCE_USER_WEIGHT_UNIT, mUserWeigthUnit);
		editor.putString(Constants.PREFERENCE_USER_DEVICEADDRESS, mUserDeviceAdderess);
		editor.putInt(Constants.PREFERENCE_USING_DATE, using_date);
		editor.commit();
	}
	
	public void setAlarms(Bundle info) {
		mAlarmTimeText = convertToString(info.getStringArrayList(Constants.PREFERENCE_ALARM_TIME_TEXT));
		mAlarmDayText = convertToString(info.getStringArrayList(Constants.PREFERENCE_ALARM_DAY_TEXT));
		mAlarmOnOff = convertToString(info.getStringArrayList(Constants.PREFERENCE_ALARM_ONOFF));
		mAlarmDaySText = convertToString(info.getStringArrayList(Constants.PREFERENCE_ALARM_DAYS_TEXT));
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString(Constants.PREFERENCE_ALARM_TIME_TEXT, mAlarmTimeText);
		editor.putString(Constants.PREFERENCE_ALARM_DAY_TEXT, mAlarmDayText);
		editor.putString(Constants.PREFERENCE_ALARM_ONOFF, mAlarmOnOff);
		editor.putString(Constants.PREFERENCE_ALARM_DAYS_TEXT, mAlarmDaySText);
		
		editor.commit();
	}
	
	public void resetAlarms() {
		mAlarmTimeText = null;
		mAlarmDayText = null;
		mAlarmOnOff = null;
		mAlarmDaySText = null;
		
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		editor.putString(Constants.PREFERENCE_ALARM_TIME_TEXT, mAlarmTimeText);
		editor.putString(Constants.PREFERENCE_ALARM_DAY_TEXT, mAlarmDayText);
		editor.putString(Constants.PREFERENCE_ALARM_ONOFF, mAlarmOnOff);
		editor.putString(Constants.PREFERENCE_ALARM_DAYS_TEXT, mAlarmDaySText);
		
		editor.commit();
	}
	
	public ArrayList<String> getAlarmTimeTxt(){
		return convertToArray(mAlarmTimeText);
	}
	public ArrayList<String> getAlarmDayTxt(){
		return convertToArray(mAlarmDayText);
	}
	public ArrayList<String> getAlarmOnOff(){
		return convertToArray(mAlarmOnOff);
	}
	public ArrayList<String> getAlarmDaySTxt(){
		return convertToArray(mAlarmDaySText);
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
		mUserAllDis=d;
		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(Constants.PREFERENCE_ALL_DIS, mUserAllDis);
	}
	
	public int getmUserAllDis() {
		return mUserAllDis;
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
		mKakaoNoti = setInfo.getBoolean(Constants.PREFERENCE_KAKAO_NOTI, false);
		mDeviceConn = setInfo.getBoolean(Constants.PREFERENCE_DEVICE_CONN, false);
		
		mCallLedColor = setInfo.getInt(Constants.PREFERENCE_CALL_LED_COLOR, 1);
		mSmsLedColor = setInfo.getInt(Constants.PREFERENCE_SMS_LED_COLOR, 2);
		mKakaoLedColor = setInfo.getInt(Constants.PREFERENCE_KAKAO_LED_COLOR, 3);

		SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(Constants.PREFERENCE_CALL_NOTI, mCallNoti);
		editor.putBoolean(Constants.PREFERENCE_SMS_NOTI, mSmsNoti);
		editor.putBoolean(Constants.PREFERENCE_KAKAO_NOTI, mKakaoNoti);
		editor.putBoolean(Constants.PREFERENCE_DEVICE_CONN, mDeviceConn);
		editor.putInt(Constants.PREFERENCE_CALL_LED_COLOR, mCallLedColor);
		editor.putInt(Constants.PREFERENCE_SMS_LED_COLOR, mSmsLedColor);
		editor.putInt(Constants.PREFERENCE_KAKAO_LED_COLOR, mKakaoLedColor);
		editor.commit();
	}
	
	public boolean getCAllNoti(){
		return mCallNoti;
	}
	public boolean getSMSNoti(){
		return mSmsNoti;
	}
	public boolean getKaKaoNoti(){
		return mKakaoNoti;
	}
	public boolean getDeviceConn(){
		return mDeviceConn;
	}
	
	public int getCAllLedColor(){
		return mCallLedColor;
	}
	public int getSMSLedColor(){
		return mSmsLedColor;
	}
	public int getKaKaoLedColor(){
		return mKakaoLedColor;
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
	public int getUserActivityGoal() {
		return mUserActivityGoal;
	}
	public void setUserActGoal(int g) {
		mUserActivityGoal= g;
	}

	public int getUserSleepGoal() {
		return mUserSleepGoal;
	}
	public void setUserSleepGoal(int g) {
		mUserSleepGoal= g;
	}

	public String getUserTallUnit() {
		return mUserTallUnit;
	}

	public String getUserWeigthUnit() {
		return mUserWeigthUnit;
	}

	public String getUserDeviceAdderess() {
		return mUserDeviceAdderess;
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
