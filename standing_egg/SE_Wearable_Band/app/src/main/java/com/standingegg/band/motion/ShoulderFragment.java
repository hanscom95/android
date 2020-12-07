
package com.standingegg.band.motion;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.standingegg.band.IFragmentListener;
import com.standingegg.band.R;
import com.standingegg.band.contents.DBManager;
import com.standingegg.band.util.Preferences;
import com.standingegg.band.util.TodayPreferences;

public class ShoulderFragment extends Fragment implements OnClickListener {
	private Context mContext = null;
	private IFragmentListener mFragmentListener = null;
	private Handler mActivityHandler = null;
	private DBManager dbManager;

	// packet show
	// TextView mTextChat;
	TextView mShoulderCntTv;

	private Preferences mPreference = null;
	private TodayPreferences mTodayPreference = null;

	private int todayShoulderCnt, todayDis,todayCal ;

	float activityGoal = 1000.0f;

	public ShoulderFragment() {};

	public ShoulderFragment(Context c, IFragmentListener l, Handler h) {
		mContext = c;
		mFragmentListener = l;
		mActivityHandler = h;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.motion_shoulder, container, false);

		setContents(v);
		mPreference = Preferences.getInstance(mContext);
		activityGoal =  mPreference.getUserActivityGoal()< 1000? 1000.0f : (float)mPreference.getUserActivityGoal();
		mTodayPreference = TodayPreferences.getInstance(mContext);
		dbManager = new DBManager(mContext, "seband.db", null, 1);
		
		setData();
		return v;
	}

	public void setData() {
		todayShoulderCnt = mTodayPreference.getMotionShoulderAllCount();

//		mShoulderCntTv.setText(Integer.toString(todayShoulderCnt));

		setCount(Integer.toString(todayShoulderCnt), 0, 0);
	}

	private void setContents(View v) {
		mShoulderCntTv = (TextView) v.findViewById(R.id.shoulder_cnt_tv);
	}

	/**
	 * set 활동 main UI walk, progress cnt
	 * 
	 * @param cnt
	 * @param mActivityAllM
	 * @param mActivityAllCal
	 */
	public void setCount(String cnt, int mActivityAllM, int mActivityAllCal) {
		if (cnt == null || cnt.length() < 1)
			return;

		Cursor cursor = dbManager.dailyShouldeTotalCntDate(mTodayPreference.getDataDate());
		int totalCnt = 0, totalCar = 0;
		while (cursor.moveToNext()) {
			totalCnt = totalCnt + cursor.getInt(0);
			totalCar = totalCar + cursor.getInt(1);
		}

		mShoulderCntTv.setText(""+totalCnt);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.activity_bar:
//			Intent i = new Intent(mContext, ActivityTodayDetailActivity.class);
//			startActivityForResult(i, Constants.ACTIVITY_SLEEP_FLAG);
//			break;
		default:
			break;
		}

	}
}