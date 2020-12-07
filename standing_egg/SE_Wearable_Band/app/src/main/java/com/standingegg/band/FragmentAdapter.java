package com.standingegg.band;

import java.util.Locale;

import com.standingegg.band.activity.ActivityFragment;
import com.standingegg.band.activity.ActivityRunFragment;
import com.standingegg.band.motion.HammerFragment;
import com.standingegg.band.motion.ShoulderFragment;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

	public static final String TAG = "FragmentAdapter";

	public static final int FRAGMENT_COUNT = 2;

	// TODO: Fragment position
	public static final int FRAGMENT_POS_WALK_ACTIVITY = 0;
	public static final int FRAGMENT_POS_RUN_ACTIVITY = 1;

	// System
	private Context mContext = null;
	private Handler mHandler = null;
	private IFragmentListener mFragmentListener = null;

	private Fragment mExampleFragment = null;
	private Fragment mLLSettingsFragment = null;

	public FragmentAdapter(FragmentManager fm, Context c, IFragmentListener l, Handler h) {
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
		if (position == FRAGMENT_POS_WALK_ACTIVITY) {
			if (mExampleFragment == null) {
				mExampleFragment = new ActivityFragment(mContext, mFragmentListener, mHandler);
				// needToSetArguments = true;
			}
			fragment = mExampleFragment;

		} else if (position == FRAGMENT_POS_RUN_ACTIVITY) {
			if (mLLSettingsFragment == null) {
				//20160822 mLLSettingsFragment = new SleepFragment(mContext, mFragmentListener, mHandler);
				mLLSettingsFragment = new ActivityRunFragment(mContext, mFragmentListener, mHandler);
				// needToSetArguments = true;
			}
			fragment = mLLSettingsFragment;

		}else {
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
		case FRAGMENT_POS_WALK_ACTIVITY:
			return mContext.getString(R.string.activity_title).toUpperCase(l);
		case FRAGMENT_POS_RUN_ACTIVITY:
			return mContext.getString(R.string.activity_title).toUpperCase(l);
		}
		return null;
	}

}