
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.setting;

import com.standingegg.band.R;
import com.standingegg.band.contents.OnOffButton;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.Preferences;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SettingActivity extends Activity{

	private Context mContext;

	private Preferences mPreference = null;

	private OnOffButton mCallNotify, mSmsNotify, mWechatNotify, mQqNotify;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mPreference = Preferences.getInstance(mContext);
		setContentView(R.layout.setting);



		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);
		TextView title = (TextView)actionBar.getCustomView().findViewById(R.id.action_bar_title);
		title.setText("NOTIFICATION");

		setComponent();
	}

	private void setComponent() {
		mCallNotify = (OnOffButton) findViewById(R.id.call_alarm);
		mSmsNotify = (OnOffButton) findViewById(R.id.sms_alarm);
		mWechatNotify = (OnOffButton) findViewById(R.id.wechat_alarm);
		mQqNotify = (OnOffButton) findViewById(R.id.qq_alarm);



		mQqNotify.setOnCheckChangedListner(new OnOffButton.OnCheckChangedListner() {
			@Override
			public void onCheckChanged(View v, boolean isChecked) {
				if(isChecked){
					mQqNotify.setBackgroundResource(R.drawable.alarm_background_on_state);
//					Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
					//Intent intent = new Intent("android.settings.NOTIFICATION_LISTENER_SETTINGS");
//					startActivity(intent);
				}else{
					mQqNotify.setBackgroundResource(R.drawable.alarm_background_off_state);
				}
			}
		});
		mWechatNotify.setOnCheckChangedListner(new OnOffButton.OnCheckChangedListner() {
			@Override
			public void onCheckChanged(View v, boolean isChecked) {
				if(isChecked){
					mWechatNotify.setBackgroundResource(R.drawable.alarm_background_on_state);
//					Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
					//Intent intent = new Intent("android.settings.NOTIFICATION_LISTENER_SETTINGS");
//					startActivity(intent);
				}else{
					mWechatNotify.setBackgroundResource(R.drawable.alarm_background_off_state);
				}
			}
		});
		mCallNotify.setOnCheckChangedListner(new OnOffButton.OnCheckChangedListner() {
			@Override
			public void onCheckChanged(View v, boolean isChecked) {
				if(isChecked){
					mCallNotify.setBackgroundResource(R.drawable.alarm_background_on_state);
				}else{
					mCallNotify.setBackgroundResource(R.drawable.alarm_background_off_state);
				}
			}
		});
		mSmsNotify.setOnCheckChangedListner(new OnOffButton.OnCheckChangedListner() {
			@Override
			public void onCheckChanged(View v, boolean isChecked) {
				if(isChecked){
					mSmsNotify.setBackgroundResource(R.drawable.alarm_background_on_state);
				}else{
					mSmsNotify.setBackgroundResource(R.drawable.alarm_background_off_state);
				}
			}
		});

		mCallNotify.setChecked(mPreference.getCAllNoti());
		mSmsNotify.setChecked(mPreference.getSMSNoti());
		mWechatNotify.setChecked(mPreference.getWechatNoti());
		mQqNotify.setChecked(mPreference.getQqNoti());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.modify_user, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
			break;
			case R.id.action:
				Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
				startActivity(intent);
				break;

		default:

		}
		return super.onOptionsItemSelected(item);
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
//		setInfo.putBoolean(Constants.PREFERENCE_KAKAO_NOTI, mWechatNotify.isChecked());
		setInfo.putBoolean(Constants.PREFERENCE_WECHAT_NOTI, mWechatNotify.isChecked());
		setInfo.putBoolean(Constants.PREFERENCE_QQ_NOTI, mQqNotify.isChecked());
//		setInfo.putBoolean(Constants.PREFERENCE_DEVICE_CONN, mDeviceConn.isChecked());
		mPreference.setSetting(setInfo);
	}

}