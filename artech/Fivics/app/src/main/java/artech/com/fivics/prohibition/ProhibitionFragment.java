package artech.com.fivics.prohibition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import artech.com.fivics.R;

/**
 * Created by moon on 2017-01-20.
 */

public class ProhibitionFragment extends Fragment {


    int mPosition;

    public ProhibitionFragment(int position) {
        mPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = null;

//        ImageView nextImg = (ImageView) getActivity().findViewById(R.id.next_step_img);
//        nextImg.setVisibility(View.INVISIBLE);

//        ImageView stepNumberImg = (ImageView) getActivity().findViewById(R.id.step_number_img);

        if(mPosition == 0) {
           v = inflater.inflate(R.layout.fragment_prohibition_first, container, false);
//            stepNumberImg.setImageResource(R.mipmap.number_first);
        }else if(mPosition == 1) {
            v = inflater.inflate(R.layout.fragment_prohibition_second, container, false);
//            stepNumberImg.setImageResource(R.mipmap.number_second);
        }else if(mPosition == 2) {
            v = inflater.inflate(R.layout.fragment_prohibition_third, container, false);
//            stepNumberImg.setImageResource(R.mipmap.number_third);
        }else if(mPosition == 3) {
            v = inflater.inflate(R.layout.fragment_prohibition_fourth, container, false);
//            stepNumberImg.setImageResource(R.mipmap.number_fourth);
//            nextImg.setVisibility(View.VISIBLE);
        }

//        mChartWebview = (WebView) v.findViewById(R.id.chart_webview);
//        mChartAxiText = (TextView) v.findViewById(R.id.chart_axis_text);



        return v;
    }

}
