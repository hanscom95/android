package artech.com.fivics.prohibition;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by moon on 2017-01-20.
 */

public class ProhibitionPagerAdapter extends FragmentStatePagerAdapter {
    // Count number of tabs
    private int tabCount;
    private int tabPosition;

    public ProhibitionPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("GuidePagerAdapter" , "po : " + position);
        tabPosition = position;
        switch (position) {
            case 0:
                ProhibitionFragment tabFragment1 = new ProhibitionFragment(position);
                return tabFragment1;

            case 1:
                ProhibitionFragment tabFragment2 = new ProhibitionFragment(position);
                return tabFragment2;

            case 2:
                ProhibitionFragment tabFragment3 = new ProhibitionFragment(position);
                return tabFragment3;

            case 3:
                ProhibitionFragment tabFragment4 = new ProhibitionFragment(position);
                return tabFragment4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public int getPosition() {
        return tabPosition;
    }
}
