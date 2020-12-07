
/**
 * @author CCZZZEE
 *
 */
package com.standingegg.band.login;

import com.standingegg.band.R;
import com.standingegg.band.setting.AlarmSettingActivity;
import com.standingegg.band.util.Constants;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.NumberPicker;

public class UserProfileModifyActivity extends Activity {

	Button mBeforeBtn;
	NumberPicker mActGoal, mHgoal, mMgoal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.user_profile_modify);

		setComponent();
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
	}

	private void setComponent() {
		mActGoal = (NumberPicker) findViewById(R.id.act_goal);
		mActGoal.setMinValue(1000);
		mActGoal.setMaxValue(100000);
		mActGoal.setOnLongPressUpdateInterval(1000);
		mActGoal.setValue(10000);

		mHgoal = (NumberPicker) findViewById(R.id.goal_h);
		String[] time = new String[25];
		String t;
		for (int i = 0; i < time.length; i++) {
			t = Integer.toString(i);
			time[i] = t.length() < 2 ? "0" + t : t;
		}
		mHgoal.setMinValue(0);
		mHgoal.setMaxValue(24);
		mHgoal.setDisplayedValues(time);

		String[] minutes = new String[60];
		for (int i = 0; i < minutes.length; i++) {
			t = Integer.toString(i);
			minutes[i] = t.length() < 2 ? "0" + t : t;
		}
		
		mMgoal = (NumberPicker) findViewById(R.id.goal_m);
		mMgoal.setMinValue(0);
		mMgoal.setMaxValue(59);
		mMgoal.setDisplayedValues(minutes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.save, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action:

			// preferencese, sqlite , server 전체에 업데이트

			Intent intent = new Intent();
			this.setResult(RESULT_OK, intent);
			finish();
			break;

		default:

		}
		return super.onMenuItemSelected(featureId, item);
	}
}