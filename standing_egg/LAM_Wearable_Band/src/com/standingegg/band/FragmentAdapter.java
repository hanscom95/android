package com.standingegg.band;

import java.util.Locale;

import com.standingegg.band.activity.ActivityFragment;
import com.standingegg.band.sleeping.SleepFragment;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

	public static final String TAG = "FragmentAdapter";

	public static final int FRAGMENT_COUNT = 2;

	// TODO: Fragment position
	public static final int FRAGMENT_POS_ACTIVITY = 0;
	public static final int FRAGMENT_POS_SLEEP = 1;

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
		if (position == FRAGMENT_POS_ACTIVITY) {
			if (mExampleFragment == null) {
				mExampleFragment = new ActivityFragment(mContext, mFragmentListener, mHandler);
				// needToSetArguments = true;
			}
			fragment = mExampleFragment;

		} else if (position == FRAGMENT_POS_SLEEP) {  
			if (mLLSettingsFragment == null) {
				mLLSettingsFragment = new SleepFragment(mContext, mFragmentListener, mHandler);
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
		case FRAGMENT_POS_ACTIVITY:
			return mContext.getString(R.string.title_activity).toUpperCase(l);
		case FRAGMENT_POS_SLEEP:
			return mContext.getString(R.string.title_sleep).toUpperCase(l);
		}
		return null;
	}

}