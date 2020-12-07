package artech.com.fivics.guide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by moon on 2017-01-20.
 */

public class GuidePagerAdapter extends FragmentStatePagerAdapter {
    // Count number of tabs
    private int tabCount;
    private int tabPosition;

    public GuidePagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("GuidePagerAdapter" , "po : " + position);
        tabPosition = position;
        GuideFragment tabFragment1 = new GuideFragment(position);
        return tabFragment1;
        /*switch (position) {
            case 0:
                GuideFragment tabFragment1 = new GuideFragment(position);
                return tabFragment1;

            case 1:
                GuideFragment tabFragment2 = new GuideFragment(position);
                return tabFragment2;

            case 2:
                GuideFragment tabFragment3 = new GuideFragment(position);
                return tabFragment3;

            case 3:
                GuideFragment tabFragment4 = new GuideFragment(position);
                return tabFragment4;

            case 4:
                GuideFragment tabFragment5 = new GuideFragment(position);
                return tabFragment5;

            case 5:
                GuideFragment tabFragment6 = new GuideFragment(position);
                return tabFragment6;

            case 6:
                GuideFragment tabFragment7 = new GuideFragment(position);
                return tabFragment7;

            case 7:
                GuideFragment tabFragment8 = new GuideFragment(position);
                return tabFragment8;

            case 8:
                GuideFragment tabFragment9 = new GuideFragment(position);
                return tabFragment9;

            case 9:
                GuideFragment tabFragment10 = new GuideFragment(position);
                return tabFragment10;

            case 10:
                GuideFragment tabFragment11 = new GuideFragment(position);
                return tabFragment11;

            case 11:
                GuideFragment tabFragment12 = new GuideFragment(position);
                return tabFragment12;

            case 12:
                GuideFragment tabFragment13 = new GuideFragment(position);
                return tabFragment13;

            case 13:
                GuideFragment tabFragment14 = new GuideFragment(position);
                return tabFragment14;

            case 14:
                GuideFragment tabFragment15 = new GuideFragment(position);
                return tabFragment15;

            case 15:
                GuideFragment tabFragment16 = new GuideFragment(position);
                return tabFragment16;
            default:
                return null;
        }*/
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public int getPosition() {
        return tabPosition;
    }
}
