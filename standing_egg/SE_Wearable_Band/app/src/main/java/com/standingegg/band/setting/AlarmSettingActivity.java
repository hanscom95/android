
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.setting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.standingegg.band.R;
import com.standingegg.band.contents.OnOffButton;
import com.standingegg.band.util.Preferences;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmSettingActivity extends Activity {
	Context mContext;
	CheckBox mMon, mTue, mWed, mThu, mFri, mSat, mSun;
	OnOffButton mRepeatFlag;
//	Switch mAlarmOnOff;
	LinearLayout mRepeatLayout;
	
	RelativeLayout mTimePickerLayout;
	TextView mTime;

	int hour;
	int min;

	private Preferences mPreferences;
	private ArrayList<String> mData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.alarm_set);
		mContext = this;
//		getActionBar().setIcon(android.R.color.transparent);


		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.action_bar);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);
		TextView title = (TextView)actionBar.getCustomView().findViewById(R.id.action_bar_title);
		title.setText("ALARM ON/OFF");

		hour = 9;
		min = 0;
		setComponent();
		
		mPreferences = Preferences.getInstance(this);
		if(mPreferences.getAlarmTimeTxt() != null) {
			mData = mPreferences.getAlarmTimeTxt();
		}
	}

	private void setComponent() {
		mTimePickerLayout  = (RelativeLayout) findViewById(R.id.timepicker);
		
		mTime = (TextView) findViewById(R.id.time);
//		mTime.setIs24HourView(DateFormat.is24HourFormat(this));

		mMon = (CheckBox) findViewById(R.id.mon);
		mTue = (CheckBox) findViewById(R.id.tue);
		mWed = (CheckBox) findViewById(R.id.wed);
		mThu = (CheckBox) findViewById(R.id.thu);
		mFri = (CheckBox) findViewById(R.id.fri);
		mSat = (CheckBox) findViewById(R.id.sat);
		mSun = (CheckBox) findViewById(R.id.sun);

		mRepeatFlag = (OnOffButton) findViewById(R.id.repeat_sw);
//		mAlarmOnOff = (Switch) findViewById(R.id.alarm_onoff);
		mRepeatLayout = (LinearLayout) findViewById(R.id.repeat_check);

		mRepeatFlag.setOnCheckChangedListner(new OnOffButton.OnCheckChangedListner() {
			@Override
			public void onCheckChanged(View v, boolean isChecked) {
				if(isChecked){
					mRepeatLayout.setVisibility(View.VISIBLE);
					mRepeatFlag.setBackgroundResource(R.drawable.alarm_background_on_state);
				}else{
					mRepeatLayout.setVisibility(View.GONE);
					mRepeatFlag.setBackgroundResource(R.drawable.alarm_background_off_state);
				}
			}
		});
		
		mTimePickerLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SleepGoalDialog();
			}
		});
	}

	
	private void SleepGoalDialog() {
		AlertDialog.Builder alert1 = new AlertDialog.Builder(this);

		alert1.setTitle(R.string.alaram_time_title);

		final TimePicker mTimePicker;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.time_picker_dialog, null);
		alert1.setView(view);

		mTimePicker = (TimePicker) view.findViewById(R.id.time_picker);
		mTimePicker.setIs24HourView(DateFormat.is24HourFormat(this));
		mTimePicker.setCurrentHour(hour);
		mTimePicker.setCurrentMinute(min);

		alert1.setPositiveButton(R.string.set, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				hour = mTimePicker.getCurrentHour();
				min = mTimePicker.getCurrentMinute();

				mTime.setText((hour < 10 ? "0" + Integer.toString(hour) : hour) + ":"
						+ (min < 10 ? "0" + Integer.toString(min) : Integer.toString(min)));
			}
		});
		alert1.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});

		alert1.show();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.alarm_set, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action:
			String time = (hour < 10 ? "0" + Integer.toString(hour) : hour) + ":"
					+ (min < 10 ? "0" + Integer.toString(min) : Integer.toString(min));
			
			if(mData != null && mData.size() > 0) {
				for(int i=0; i<mData.size(); i ++){
					if(time.equals(mData.get(i))) {
						Toast.makeText(this, R.string.alaram_time_chk, Toast.LENGTH_SHORT).show();
						return false;
					}
				}
			}
			
			Intent intent = new Intent();
			byte b = 0;// = new byte[8];

			if (mRepeatFlag.isChecked()) {
				b |= 128;
				if (mMon.isChecked())
					b |= 1;
				
				if (mTue.isChecked())
					b |= 2;

				if (mWed.isChecked())
					b |= 4;

				if (mThu.isChecked())
					b |= 8;

				if (mFri.isChecked())
					b |= 16;

				if (mSat.isChecked())
					b |= 32;

				if (mSun.isChecked())
					b |= 64;
			}

			String days = "";
			if (mMon.isChecked() && mTue.isChecked() && mWed.isChecked() && mThu.isChecked() && mFri.isChecked()
					&& mSat.isChecked() && mSun.isChecked()) {
				days = getString(R.string.alaram_date_everyday_msg);
			} else if (mMon.isChecked() && mTue.isChecked() && mWed.isChecked() && mThu.isChecked() && mFri.isChecked()
					&& !mSat.isChecked() && !mSun.isChecked()) {
				days = getString(R.string.alaram_date_weekday_msg);
			} else if (mSat.isChecked() && mSun.isChecked() && !mMon.isChecked() && !mTue.isChecked()
					&& !mWed.isChecked() && !mThu.isChecked() && !mFri.isChecked()) {
				days = getString(R.string.alaram_date_weekend_msg);
			} else {

				if (mMon.isChecked()) {
					days = days + " " + getString(R.string.alaram_date_mon_day);
				}
				if (mTue.isChecked()) {
					days = days + " " + getString(R.string.alaram_date_tue_day);
				}
				if (mWed.isChecked()) {
					days = days + " " + getString(R.string.alaram_date_wed_day);
				}
				if (mThu.isChecked()) {
					days = days + " " + getString(R.string.alaram_date_thu_day);
				}
				if (mFri.isChecked()) {
					days = days + " " + getString(R.string.alaram_date_fri_day);
				}
				if (mSat.isChecked()) {
					days = days + " " + getString(R.string.alaram_date_sat_day);
				}
				if (mSun.isChecked()) {
					days = days + " " + getString(R.string.alaram_date_sun_day);
				}
			}

			if ("".equals(days)) {
				days = getString(R.string.one);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());

				if (cal.get(Calendar.DAY_OF_WEEK) == 2)//월요일
					b = 1;
				else if (cal.get(Calendar.DAY_OF_WEEK) == 3)//화요일
					b = 2;
				else if (cal.get(Calendar.DAY_OF_WEEK) == 4)//수요일
					b = 4;
				else if (cal.get(Calendar.DAY_OF_WEEK) == 5)//목요일
					b = 8;
				else if (cal.get(Calendar.DAY_OF_WEEK) == 6)//금요일
					b = 16;
				else if (cal.get(Calendar.DAY_OF_WEEK) == 7)//토요일
					b = 32;
				else if (cal.get(Calendar.DAY_OF_WEEK) == 1)//일요일
					b = 64;
			}

			intent.putExtra("DAY", String.format("%02x", b&0xff)); // 요일
			intent.putExtra("DAY_S", days); // 요일 String "월요일" "화요일" ...
			intent.putExtra("ON_OFF", true);
			intent.putExtra("TIME", time);
			this.setResult(RESULT_OK, intent);
 
			finish();
			break;

		default:

		}
		return super.onMenuItemSelected(featureId, item);
	}

	public static int hex2decimal(String s) {
		String digits = "0123456789ABCDEF";
		s = s.toUpperCase();

		int val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = digits.indexOf(c);
			val = 16 * val + d;
		}
		return val;
	}
}