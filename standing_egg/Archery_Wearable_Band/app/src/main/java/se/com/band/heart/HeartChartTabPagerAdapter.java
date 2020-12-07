package se.com.band.heart;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by moon on 2017-01-20.
 */

public class HeartChartTabPagerAdapter extends FragmentStatePagerAdapter {
    // Count number of tabs
    private int tabCount;

    public HeartChartTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HeartChartTabFragment tabFragment1 = new HeartChartTabFragment();
                return tabFragment1;

            case 1:
                HeartChartTabFragment tabFragment2 = new HeartChartTabFragment();
                return tabFragment2;

            case 2:
                HeartChartTabFragment tabFragment3 = new HeartChartTabFragment();
                return tabFragment3;

            case 3:
                HeartChartTabFragment tabFragment4 = new HeartChartTabFragment();
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
