package se.com.band.sleep;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by moon on 2017-01-20.
 */

public class SleepChartTabPagerAdapter extends FragmentStatePagerAdapter {
    // Count number of tabs
    private int tabCount;

    public SleepChartTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SleepChartTabFragment tabFragment1 = new SleepChartTabFragment(position);
                return tabFragment1;

            case 1:
                SleepChartTabFragment tabFragment2 = new SleepChartTabFragment(position);
                return tabFragment2;

            case 2:
                SleepChartTabFragment tabFragment3 = new SleepChartTabFragment(position);
                return tabFragment3;

            case 3:
                SleepChartTabFragment tabFragment4 = new SleepChartTabFragment(position);
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
