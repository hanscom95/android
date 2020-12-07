package com.standingegg.band.sensor;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.standingegg.band.IFragmentListener;
import com.standingegg.band.R;
import com.standingegg.band.util.StnEggPkt;
import com.standingegg.band.util.ULog;

/**
 * Created by moon on 2016-09-20.
 */

public class SensorChartFragment extends Fragment {
    private Context mContext = null;
    private IFragmentListener mFragmentListener = null;
    private Handler mActivityHandler = null;

    RealTimeChart rtc01, rtc02, rtc03;

    public SensorChartFragment() {};

    public SensorChartFragment(Context c, IFragmentListener l, Handler h) {
        mContext = c;
        mFragmentListener = l;
        mActivityHandler = h;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sensor_data, container, false);

        rtc01 = new RealTimeChart(getActivity());
        rtc02 = new RealTimeChart(getActivity());
        rtc03 = new RealTimeChart(getActivity());

        LinearLayout realTimeLayout = (LinearLayout)v.findViewById(R.id.RealTimelayout);
        LinearLayout realTimeLayout2 = (LinearLayout)v.findViewById(R.id.RealTimelayout2);
        LinearLayout realTimeLayout3 = (LinearLayout)v.findViewById(R.id.RealTimelayout3);


        //	2. chart view add
        realTimeLayout.addView(rtc01); // RealTimeChart 1
        realTimeLayout2.addView(rtc02); // RealTimeChart 2
        realTimeLayout3.addView(rtc03); // RealTimeChart 3

        //	3. chart initialize
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int width = dm.widthPixels-100;
        int height = (dm.heightPixels/3)-50;

        rtc01.Initialize(width, height, 1); // RealTimeChart1
        rtc02.Initialize(width, height, 2); // RealTimeChart2
        rtc03.Initialize(width, height, 3); // RealTimeChart3

        return v;
    }

    public static void setData(StnEggPkt.Float3 acceleroDataG, StnEggPkt.Float3 gyroDataDps, StnEggPkt.Float3 magDataUT) {
        ULog.d("acc1 : " + acceleroDataG.getF1() + "//// acc2 : " + acceleroDataG.getF2() + "//// acc3 : " + acceleroDataG.getF3());
        ULog.d("gyro1 : " + gyroDataDps.getF1() + "//// gyro2 : " + gyroDataDps.getF2() + "//// gyro3 : " + gyroDataDps.getF3());
        ULog.d("mag1 : " + magDataUT.getF1() + "//// mag2 : " + magDataUT.getF2() + "//// mag3 : " + magDataUT.getF3());
    }
}
