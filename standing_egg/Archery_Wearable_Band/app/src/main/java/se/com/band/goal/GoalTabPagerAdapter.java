package se.com.band.goal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by moon on 2017-01-20.
 */

public class GoalTabPagerAdapter extends FragmentStatePagerAdapter {
    // Count number of tabs
    private int mTabCount;
    private String mValue;

    GoalTabFragment mTabFragmentStep, mTabFragmentCalorie;

    public GoalTabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.mTabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mTabFragmentStep = new GoalTabFragment(position, mValue);
                return mTabFragmentStep;

            case 1:
                mTabFragmentCalorie = new GoalTabFragment(position, mValue);
                return mTabFragmentCalorie;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabCount;
    }

    public int getValue(int position) {
        if(position == 0) {
            return mTabFragmentStep.getValue();
        }else if(position == 1){
            return mTabFragmentCalorie.getValue();
        }else {
            return 0;
        }
    }

    public void setValue(String value) {
        mValue = value;
    }
}
