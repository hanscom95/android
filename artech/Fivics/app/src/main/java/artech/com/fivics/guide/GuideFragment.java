package artech.com.fivics.guide;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Timer;
import java.util.TimerTask;

import artech.com.fivics.R;

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
        Log.d("GuideFragment", "mPosition : " + mPosition);
        if(mPosition == 0) {
            v = inflater.inflate(R.layout.fragment_guide_one, container, false);
        }else if(mPosition == 1) {
            v = inflater.inflate(R.layout.fragment_guide_two, container, false);
        }else if(mPosition == 2) {
           v = inflater.inflate(R.layout.fragment_guide_first, container, false);
        }else if(mPosition == 3) {
            v = inflater.inflate(R.layout.fragment_guide_first_two, container, false);
        }else if(mPosition == 4) {
            v = inflater.inflate(R.layout.fragment_guide_second_two, container, false);
        }else if(mPosition == 5) {
            v = inflater.inflate(R.layout.fragment_guide_second, container, false);
        }else if(mPosition == 6) {
            v = inflater.inflate(R.layout.fragment_guide_three, container, false);
        }else if(mPosition == 7) {
            v = inflater.inflate(R.layout.fragment_guide_four, container, false);
        }else if(mPosition == 8) {
            v = inflater.inflate(R.layout.fragment_guide_third, container, false);
        }else if(mPosition == 9) {
            v = inflater.inflate(R.layout.fragment_guide_fourth, container, false);
//            ImageView gifImg = (ImageView) v.findViewById(R.id.guide4_gif_image);
//            Glide.with(getContext()).asGif().load(R.raw.step2_4).into(gifImg);
        }else if(mPosition == 10) {
            v = inflater.inflate(R.layout.fragment_guide_fourth_two, container, false);
        }else if(mPosition == 11) {
            v = inflater.inflate(R.layout.fragment_guide_fifth, container, false);
        }else if(mPosition == 12) {
            v = inflater.inflate(R.layout.fragment_guide_sixth, container, false);
        }else if(mPosition == 13) {
            v = inflater.inflate(R.layout.fragment_guide_seventh, container, false);
        }else if(mPosition == 14) {
            v = inflater.inflate(R.layout.fragment_guide_seventh_two, container, false);
        }else if(mPosition == 15) {
            v = inflater.inflate(R.layout.fragment_guide_eighth, container, false);
        }

        return v;
    }

    private void imageLayoutChange(final View firstView, final View secondView) {
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if(viewBoolean) {
                    firstView.setVisibility(View.GONE);
                    secondView.setVisibility(View.VISIBLE);
                    viewBoolean = !viewBoolean;
                }else {
                    secondView.setVisibility(View.GONE);
                    firstView.setVisibility(View.VISIBLE);
                    viewBoolean = !viewBoolean;
                }
            }
        };


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 1000, 4000);
    }

}
