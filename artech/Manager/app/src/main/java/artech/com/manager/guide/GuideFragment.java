package artech.com.manager.guide;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

import artech.com.manager.R;

/**
 * Created by moon on 2017-01-20.
 */

public class GuideFragment extends Fragment {

    int mPosition;
    boolean viewBoolean = false;

    public GuideFragment(int position) {
        mPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = null;

        viewBoolean = false;
//        Log.d("GuideFragment", "mPosition : " + mPosition);
        if(mPosition == 0) {
            v = inflater.inflate(R.layout.fragment_guide_first, container, false);
        }else if(mPosition == 1) {
            v = inflater.inflate(R.layout.fragment_guide_second, container, false);
        }else if(mPosition == 2) {
            v = inflater.inflate(R.layout.fragment_guide_third, container, false);
        }else if(mPosition == 3) {
            v = inflater.inflate(R.layout.fragment_guide_fourth, container, false);
        }else if(mPosition == 4) {
            v = inflater.inflate(R.layout.fragment_guide_fifth, container, false);
        }else if(mPosition == 5) {
            v = inflater.inflate(R.layout.fragment_guide_sixth, container, false);
        }else if(mPosition == 6) {
            v = inflater.inflate(R.layout.fragment_guide_seventh, container, false);
        }else if(mPosition == 7) {
            v = inflater.inflate(R.layout.fragment_guide_eighth, container, false);
        }else if(mPosition == 8) {
            v = inflater.inflate(R.layout.fragment_guide_ninth, container, false);
        }else if(mPosition == 9) {
            v = inflater.inflate(R.layout.fragment_guide_tenth, container, false);
        }else if(mPosition == 10) {
            v = inflater.inflate(R.layout.fragment_guide_eleventh, container, false);
        }else if(mPosition == 11) {
            v = inflater.inflate(R.layout.fragment_guide_twelve, container, false);
        }else if(mPosition == 12) {
            v = inflater.inflate(R.layout.fragment_guide_thirteen, container, false);
        }else if(mPosition == 13) {
            v = inflater.inflate(R.layout.fragment_guide_fourteen, container, false);
        }else if(mPosition == 14) {
            v = inflater.inflate(R.layout.fragment_guide_fifteen, container, false);
        }else if(mPosition == 15) {
            v = inflater.inflate(R.layout.fragment_guide_sixteen, container, false);
        }

        return v;
    }

}
