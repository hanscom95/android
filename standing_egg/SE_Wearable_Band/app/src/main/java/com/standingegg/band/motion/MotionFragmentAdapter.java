package com.standingegg.band.motion;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.standingegg.band.IFragmentListener;
import com.standingegg.band.R;
import com.standingegg.band.activity.ActivityFragment;
import com.standingegg.band.activity.ActivityRunFragment;

import java.util.Locale;

public class MotionFragmentAdapter extends FragmentPagerAdapter {

	public static final String TAG = "MotionFragmentAdapter";

	public static final int FRAGMENT_COUNT = 2;

	// TODO: Fragment position
	public static final int FRAGMENT_POS_HAMMER_ACTIVITY = 0;
	public static final int FRAGMENT_POS_SHOULDER_ACTIVITY = 1;

	// System
	private Context mContext = null;
	private Handler mHandler = null;
	private IFragmentListener mFragmentListener = null;

	private Fragment mExampleFragment = null;
	private Fragment mLLSettingsFragment = null;

	public MotionFragmentAdapter(FragmentManager fm, Context c, IFragmentListener l, Handler h) {
		super(fm);
		mContext = c;
		mFragmentListener = l;
		mHandler = h;
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		Fragment fragment;
		// boolean needToSetArguments = false;
		if (position == FRAGMENT_POS_HAMMER_ACTIVITY) {
			if (mExampleFragment == null) {
				//20160822 mLLSettingsFragment = new SleepFragment(mContext, mFragmentListener, mHandler);
				mExampleFragment = new HammerFragment(mContext, mFragmentListener, mHandler);
				// needToSetArguments = true;
			}
			fragment = mExampleFragment;

		} else if (position == FRAGMENT_POS_SHOULDER_ACTIVITY) {
			if (mLLSettingsFragment == null) {
				//20160822 mLLSettingsFragment = new SleepFragment(mContext, mFragmentListener, mHandler);
				mLLSettingsFragment = new ShoulderFragment(mContext, mFragmentListener, mHandler);
				// needToSetArguments = true;
			}
			fragment = mLLSettingsFragment;

		} else {
			fragment = null;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return FRAGMENT_COUNT;
	}
	

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case FRAGMENT_POS_HAMMER_ACTIVITY:
			return mContext.getString(R.string.motion_title).toUpperCase(l);
		case FRAGMENT_POS_SHOULDER_ACTIVITY:
			return mContext.getString(R.string.motion_title).toUpperCase(l);
		}
		return null;
	}

}