package se.com.band.motion;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by moon on 2017-01-20.
 */

public class MotionChartTabPagerAdapter extends FragmentStatePagerAdapter {
    // Count number of tabs
    private int tabCount;
    private Context mContext;

    public MotionChartTabPagerAdapter(FragmentManager fm, int tabCount, Context c) {
        super(fm);
        this.tabCount = tabCount;
        mContext = c;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MotionChartTabFragment tabFragment1 = new MotionChartTabFragment(mContext, position);
                return tabFragment1;

            case 1:
                MotionChartTabFragment tabFragment2 = new MotionChartTabFragment(mContext, position);
                return tabFragment2;

            case 2:
                MotionChartTabFragment tabFragment3 = new MotionChartTabFragment(mContext, position);
                return tabFragment3;

            case 3:
                MotionChartTabFragment tabFragment4 = new MotionChartTabFragment(mContext, position);
                return tabFragment4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
