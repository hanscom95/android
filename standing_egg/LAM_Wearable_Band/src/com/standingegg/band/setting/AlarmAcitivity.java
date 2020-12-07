
/**
 * Bluetooth  전송은 알람이 on 일때
 * resultActivity 에서는 Adapter add
 * 
 * @author CCZZZEE
 *
 */
package com.standingegg.band.setting;

import com.standingegg.band.MainActivity.MyReceiver;

import java.util.ArrayList;

import com.standingegg.band.R;
import com.standingegg.band.util.Constants;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.ULog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class AlarmAcitivity extends Activity implements OnCheckedChangeListener{

	ListView mAlarmList;
	private AlarmAdapter adapter;
	private Preferences mPreferences;
	
	private ArrayList<String> mData;
	private ArrayList<String> mDay;
	private ArrayList<String> mOnOff;
	private ArrayList<String> mDayS;
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.alarm);
		mContext = this;
		
		mPreferences = Preferences.getInstance(this);
		

		if(mPreferences.getAlarmTimeTxt() == null){
			mData = new ArrayList<String>();
			mDay = new ArrayList<String>();
			mOnOff = new ArrayList<String>();
			mDayS = new ArrayList<String>();
		}else{
			mData = mPreferences.getAlarmTimeTxt();
			mDay = mPreferences.getAlarmDayTxt();
			mOnOff = mPreferences.getAlarmOnOff();
			mDayS = mPreferences.getAlarmDaySTxt();
		}
		
		
		
		setComponent();
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setIcon(android.R.color.transparent);
		
		actionBar.setTitle(R.string.alaram_dialog_title);
	}

	private void setComponent() {

		mAlarmList = (ListView) findViewById(R.id.alarm_list);

		mAlarmList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));
		adapter = new AlarmAdapter(this, R.layout.alarm_draw);
		mAlarmList.setAdapter(adapter);
		
		mAlarmList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(mContext)
				.setTitle(R.string.alaram_remove_dialog_title)
				.setMessage(R.string.alaram_remove_dialog_msg)
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mData.remove(arg2);
						mDay.remove(arg2);
						mOnOff.remove(arg2);
						mDayS.remove(arg2);
						broadcastAlarm();
						adapter.remove(arg2);
						adapter.notifyDataSetChanged();
					}
				})
				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.show();
				return false;
			}
		});
		
		for(int i=0; i<mData.size(); i ++){
			adapter.add(mData.get(i), mDay.get(i),mOnOff.get(i));
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.alarm, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.action:
			if(mAlarmList.getCount() < 3) {
				Intent addAlarm = new Intent(getApplicationContext(), AlarmSettingActivity.class);
				startActivityForResult(addAlarm, Constants.ALARM_ADD);
				// releaseAlarm(getApplicationContext());	
			}
			break;

		default:

		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int request, int result, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		switch (request) {
		case Constants.ALARM_ADD:
			if (result == Activity.RESULT_OK) {
				Bundle bundle = data.getExtras();
				String day = bundle.getString("DAY");
				String time = bundle.getString("TIME");
				String day_s = bundle.getString("DAY_S");
				Boolean on_off = bundle.getBoolean("ON_OFF");
				
				mData.add(time);
				mDay.add(day_s);
				mDayS.add(day+time.replace(":", ""));
				mOnOff.add(on_off?"On":"Off");
				
				adapter.add(time, day_s,"On");
				adapter.notifyDataSetChanged();
				
				broadcastAlarm();
			}
			break;
		} // End of switch(requestCode)

	}
	
	private void broadcastAlarm() {
		ULog.e("mData.size()==>"+mData.size());
		ULog.e("mDay.size()==>"+mDay.size());
		ULog.e("mOnOff.size()==>"+mOnOff.size());
		ULog.e("mData==>"+mData);
		ULog.e("mDay==>"+mDay);
		ULog.e("mDayS==>"+mDayS);
		ULog.e("mOnOff==>"+mOnOff);
		
		Bundle d = new Bundle();
		d.putStringArrayList(Constants.PREFERENCE_ALARM_TIME_TEXT, mData);
		d.putStringArrayList(Constants.PREFERENCE_ALARM_DAY_TEXT, mDay);
		d.putStringArrayList(Constants.PREFERENCE_ALARM_ONOFF, mOnOff);
		d.putStringArrayList(Constants.PREFERENCE_ALARM_DAYS_TEXT, mDayS);
		mPreferences.setAlarms(d);
		
		Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
		intent.putExtra(Constants.BROADCAST_RECEIVER, Constants.BROADCAST_ALARM_SET);
//		intent.putExtra("ALARMS_DAY", day);
		intent.putExtra("ALARMS_DAYS", mDayS);
		intent.putExtra("ALARMS_OnOff", mOnOff);
//		intent.putExtra("ALARMS_TIME", time.replace(":", ""));
		sendBroadcast(intent);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		try {
			if(mAlarmList != null && mAlarmList.getAdapter() != null && mAlarmList.getAdapter().getCount() > 0) {
				mOnOff.set(mAlarmList.getPositionForView(buttonView), (isChecked?"On":"Off"));	
				broadcastAlarm();
			}	
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
	}
}