package se.com.band.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import se.com.band.utility.ULog;

/**
 * Created by moon on 2017-01-20.
 */

public class WalkRunChartTabPagerAdapter extends FragmentStatePagerAdapter {
    // Count number of tabs
    private int tabCount;
    private Context mContext;

    public WalkRunChartTabPagerAdapter(FragmentManager fm, int tabCount, Context c) {
        super(fm);
        this.tabCount = tabCount;
        mContext = c;
    }

    @Override
    public Fragment getItem(int position) {
//        switch (position) {
//            case 0:
                WalkRunChartTabFragment tabFragment1 = new WalkRunChartTabFragment(mContext, position);
                return tabFragment1;

//            case 1:
//                WalkRunChartTabFragment tabFragment2 = new WalkRunChartTabFragment(mContext, position);
//                return tabFragment2;
//
//            case 2:
//                WalkRunChartTabFragment tabFragment3 = new WalkRunChartTabFragment(mContext, position);
//                return tabFragment3;
//
//            case 3:
//                WalkRunChartTabFragment tabFragment4 = new WalkRunChartTabFragment(mContext, position);
//                return tabFragment4;
//            default:
//                return null;
//        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
