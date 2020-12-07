
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.setting;

import com.standingegg.band.MainActivity.MyReceiver;
import com.standingegg.band.R;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.Preferences;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener {

	private Context mContext;

	private Button mFindBtn;
	private static TextView mBatteryTv;
	private Preferences mPreference = null;

	private static ClipDrawable mImageDrawable;

	private Switch mCallNotify, mSmsNotify, mKakaoNotify, mDeviceConn;
	private ImageView mCallLedColor, mSmsLedColor, mKakaoLedColor, mChoicLedImage; 
	private Dialog dialog;
	// a field in your class
	private static int mLevel = 0;
	private static int fromLevel = 0;
	private static int toLevel = 0;

	public static final int MAX_LEVEL = 10000;
	public static final int LEVEL_DIFF = 100;
	public static final int DELAY = 30;
	
	private int mCallLed = 0;
	private int mSmsLed = 0;
	private int mKakaoLed = 0;

	private static Handler mUpHandler = new Handler();
	private static Runnable animateUpImage = new Runnable() {

		@Override
		public void run() {
			doTheUpAnimation(fromLevel, toLevel);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mPreference = Preferences.getInstance(mContext);
		setContentView(R.layout.setting);

		ImageView img = (ImageView) findViewById(R.id.imageView1);
		mImageDrawable = (ClipDrawable) img.getDrawable();
		mImageDrawable.setLevel(0);

		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(android.R.color.transparent);

		setComponent();
		setBattery();
	}

	private void setComponent() {
		mFindBtn = (Button) findViewById(R.id.find_btn);
		mBatteryTv = (TextView) findViewById(R.id.battery);
		mCallNotify = (Switch) findViewById(R.id.call_alarm);
		mSmsNotify = (Switch) findViewById(R.id.sms_alarm);
		mKakaoNotify = (Switch) findViewById(R.id.kakao_alarm);
		mDeviceConn = (Switch) findViewById(R.id.device_connect);
		mCallLedColor = (ImageView) findViewById(R.id.call_led_color);
		mSmsLedColor = (ImageView) findViewById(R.id.sms_led_color);
		mKakaoLedColor = (ImageView) findViewById(R.id.kakao_led_color);

		mCallLed = mPreference.getCAllLedColor();
		mSmsLed = mPreference.getSMSLedColor();
		mKakaoLed = mPreference.getKaKaoLedColor();
		
		if(mCallLed == 1){
			mCallLedColor.setBackgroundColor(Color.RED);
		}else if(mCallLed == 2){
			mCallLedColor.setBackgroundColor(Color.GREEN);
		}else if(mCallLed == 3){
			mCallLedColor.setBackgroundColor(Color.BLUE);
		}
		
		if(mSmsLed == 1){
			mSmsLedColor.setBackgroundColor(Color.RED);
		}else if(mSmsLed == 2){
			mSmsLedColor.setBackgroundColor(Color.GREEN);
		}else if(mSmsLed == 3){
			mSmsLedColor.setBackgroundColor(Color.BLUE);
		}
		
		if(mKakaoLed == 1){
			mKakaoLedColor.setBackgroundColor(Color.RED);
		}else if(mKakaoLed == 2){
			mKakaoLedColor.setBackgroundColor(Color.GREEN);
		}else if(mKakaoLed == 3){
			mKakaoLedColor.setBackgroundColor(Color.BLUE);
		}

		mFindBtn.setOnClickListener(this);
		mCallLedColor.setOnClickListener(this);
		mSmsLedColor.setOnClickListener(this);
		mKakaoLedColor.setOnClickListener(this);

		mCallNotify.setChecked(mPreference.getCAllNoti());
		mSmsNotify.setChecked(mPreference.getSMSNoti());
		mKakaoNotify.setChecked(mPreference.getKaKaoNoti());
		mDeviceConn.setChecked(mPreference.getDeviceConn());

	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.call_led_color:
			dialogLedColorSetting(mCallLedColor);
			break;
			
		case R.id.sms_led_color:
			dialogLedColorSetting(mSmsLedColor);
			break;
			
		case R.id.kakao_led_color:
			dialogLedColorSetting(mKakaoLedColor);
			break;
			
		case R.id.find_btn:
			Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
			intent.putExtra(Constants.BROADCAST_RECEIVER, Constants.BROADCAST_FIND);
			sendBroadcast(intent);
			break;
		
		case R.id.red_img:
			mChoicLedImage.setBackgroundColor(Color.RED);
			setLedColor(1);
			break;
			
		case R.id.green_img:
			mChoicLedImage.setBackgroundColor(Color.GREEN);
			setLedColor(2);
			break;
			
		case R.id.blue_img:
			mChoicLedImage.setBackgroundColor(Color.BLUE);
			setLedColor(3);
			break;
			
		case R.id.cancel_button:
			dialog.dismiss();
			break;

		default:
			break;
		}
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

	private void setBattery() {
		Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
		intent.putExtra(Constants.BROADCAST_RECEIVER, Constants.BROADCAST_BAT);
		sendBroadcast(intent);

	}

	public static void setBat(String s) {
		mBatteryTv.setText(s + "%");

		int diff = 0;
		int battInt = Integer.parseInt(s);
		if(battInt > 70) {
			diff = 15;
		}else if(battInt > 50) {
			diff = 5;
		}else if(battInt > 30) {
			diff = -5;
		}else{
			diff = -15;
		}
		int temp_level = ((battInt-diff) * 100);

		mLevel = 0;
		toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;

		mUpHandler.post(animateUpImage);

	}

	@Override
	protected void onDestroy() {
		setSettings();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		setSettings();

		super.onBackPressed();
	}

	private void setSettings() {
		Bundle setInfo = new Bundle();
		setInfo.putBoolean(Constants.PREFERENCE_CALL_NOTI, mCallNotify.isChecked());
		setInfo.putBoolean(Constants.PREFERENCE_SMS_NOTI, mSmsNotify.isChecked());
		setInfo.putBoolean(Constants.PREFERENCE_KAKAO_NOTI, mKakaoNotify.isChecked());
		setInfo.putBoolean(Constants.PREFERENCE_DEVICE_CONN, mDeviceConn.isChecked());
		setInfo.putInt(Constants.PREFERENCE_CALL_LED_COLOR, mCallLed);
		setInfo.putInt(Constants.PREFERENCE_SMS_LED_COLOR, mSmsLed);
		setInfo.putInt(Constants.PREFERENCE_KAKAO_LED_COLOR, mKakaoLed);
		mPreference.setSetting(setInfo);
	}

	public static class MyReceiver1 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String s = bundle.getString(Constants.BROADCAST_RECEIVER);
			setBat(s);
		}
	}

	private static void doTheUpAnimation(int fromLevel, int toLevel) {
		mLevel += LEVEL_DIFF;
		mImageDrawable.setLevel(mLevel);
		
		if (mLevel <= toLevel) {
			mUpHandler.postDelayed(animateUpImage, DELAY);
		} else {
			mUpHandler.removeCallbacks(animateUpImage);
			fromLevel = toLevel;
		}
	}
	
	private void dialogLedColorSetting(ImageView mSelectLedImage) {
		mChoicLedImage = mSelectLedImage;
		dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.setting_led_color);
		dialog.setTitle("LED 색상을 선택하세요");
		
		ImageView redImg = (ImageView) dialog.findViewById(R.id.red_img);
		ImageView greenImg = (ImageView) dialog.findViewById(R.id.green_img);
		ImageView blueImg = (ImageView) dialog.findViewById(R.id.blue_img);
		
		Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
		
		redImg.setBackgroundColor(Color.RED);
		greenImg.setBackgroundColor(Color.GREEN);
		blueImg.setBackgroundColor(Color.BLUE);
		
		redImg.setOnClickListener(this);
		greenImg.setOnClickListener(this);
		blueImg.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		
		dialog.show();
	}
	
	private void setLedColor(int color) {
		if(mChoicLedImage.getId() == R.id.call_led_color) {
			mCallLed = color;
		}else if(mChoicLedImage.getId() == R.id.sms_led_color) {
			mSmsLed = color;
		}else if(mChoicLedImage.getId() == R.id.kakao_led_color) {
			mKakaoLed = color;
		}
		dialog.dismiss();
	}
}