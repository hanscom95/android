package se.com.band.motion;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import se.com.band.R;
import se.com.band.utility.Constants;
import se.com.band.utility.DBManager;

/**
 * Created by moon on 2017-01-20.
 */

public class MotionChartTabFragment extends Fragment {
    Context mContext;
    DBManager mDBManager;
    Constants mConstant;

    WebView mChartWebview;
    TextView mMoCountText, mMoCalText,mMoTimeText;

    int tabType = 0;

    MotionChartTabFragment(Context c, int type) {
        this.mContext = c;
        this.tabType = type;
        mDBManager = new DBManager(mContext, "apex.db", null, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.motion_chart_tab, container, false);

        mChartWebview = (WebView) v.findViewById(R.id.chart_webview);
        chartView();

        mMoCountText = (TextView) getActivity().findViewById(R.id.motion_count);
        mMoCalText = (TextView) getActivity().findViewById(R.id.motion_cal);
        mMoTimeText = (TextView) getActivity().findViewById(R.id.motion_time);

        setData();

        return v;
    }

    private void chartView() {
        //column chart
        mChartWebview.getSettings().setJavaScriptEnabled(true);
        mChartWebview.setBackgroundColor(Color.TRANSPARENT);
        mChartWebview.setWebChromeClient(new WebChromeClient());
        mChartWebview.addJavascriptInterface(new ChartDataCallback(), "android");
        mChartWebview.loadUrl("file:///android_asset/chart/motion_chart.html");

        mChartWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                chartSampleData();
            }
        });
    }

    public void chartSampleData(){
        String data2 = "[10, 0, 0, 15, 10, 8, 15]";
        String data1 = "[29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6]";
        String data3 = "[" +
                "{dataLabels: {useHTML: true, enabled: true, x: 0, y: -30, formatter: function(){return '<img src=\"file:///android_asset/chart/medal.png\" style=\"width: 22px; height:28px;\" />'; }},y: 29}, 40, 20, 15, 5, 10, 6]";
        mChartWebview.loadUrl("javascript:setChartData("+data3+", "+ data1 +", "+ data1 +", "+ data1 +", "+ data1 +", "+ data1 +", "+ data1 +", "+ data1 + ", "+ data1 +", "+ data1 +", "+ data1 +")");
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            TextView actStep = (TextView) getActivity().findViewById(R.id.motion_count);
            actStep.setText("3475");
            TextView actCal = (TextView) getActivity().findViewById(R.id.motion_cal);
            actCal.setText("164");
            TextView actTime = (TextView) getActivity().findViewById(R.id.motion_time);
            actTime.setText("01:24");

            ImageView pushupImage = (ImageView) getActivity().findViewById(R.id.push_up_credit_view);
            pushupImage.setColorFilter(getResources().getColor(R.color.chart_push_up));
            TextView pushupText = (TextView) getActivity().findViewById(R.id.chart_credits_push_up_text);
            pushupText.setTextColor(getResources().getColor(R.color.chart_activation_text));

            ImageView situpImage = (ImageView) getActivity().findViewById(R.id.sit_up_credit_view);
            situpImage.setColorFilter(getResources().getColor(R.color.chart_sit_up));
            TextView situpText = (TextView) getActivity().findViewById(R.id.chart_credits_sit_up_text);
            situpText.setTextColor(getResources().getColor(R.color.chart_activation_text));

            ImageView butterflyImage = (ImageView) getActivity().findViewById(R.id.butterfly_credit_view);
            butterflyImage.setColorFilter(getResources().getColor(R.color.chart_butterfly));
            TextView butterflyText = (TextView) getActivity().findViewById(R.id.chart_credits_butterfly_text);
            butterflyText.setTextColor(getResources().getColor(R.color.chart_activation_text));

            ImageView shoulderImage = (ImageView) getActivity().findViewById(R.id.shoulder_press_credit_view);
            shoulderImage.setColorFilter(getResources().getColor(R.color.chart_shoulder_press));
            TextView shoulderText = (TextView) getActivity().findViewById(R.id.chart_credits_shoulder_press_text);
            shoulderText.setTextColor(getResources().getColor(R.color.chart_activation_text));

            ImageView bicepsImage = (ImageView) getActivity().findViewById(R.id.biceps_curl_credit_view);
            bicepsImage.setColorFilter(getResources().getColor(R.color.chart_biceps_curl));
            TextView bicepsText = (TextView) getActivity().findViewById(R.id.chart_credits_biceps_curl_text);
            bicepsText.setTextColor(getResources().getColor(R.color.chart_activation_text));

            ImageView kettlebellImage = (ImageView) getActivity().findViewById(R.id.kettlebell_credit_view);
            kettlebellImage.setColorFilter(getResources().getColor(R.color.chart_kettlebell));
            TextView kettlebellText = (TextView) getActivity().findViewById(R.id.chart_credits_kettlebell_text);
            kettlebellText.setTextColor(getResources().getColor(R.color.chart_activation_text));

            ImageView squatImage = (ImageView) getActivity().findViewById(R.id.squat_credit_view);
            squatImage.setColorFilter(getResources().getColor(R.color.chart_squat));
            TextView squatText = (TextView) getActivity().findViewById(R.id.chart_credits_squat_text);
            squatText.setTextColor(getResources().getColor(R.color.chart_activation_text));

            ImageView benchImage = (ImageView) getActivity().findViewById(R.id.bench_credit_view);
            benchImage.setColorFilter(getResources().getColor(R.color.chart_bench));
            TextView benchText = (TextView) getActivity().findViewById(R.id.chart_credits_bench_text);
            benchText.setTextColor(getResources().getColor(R.color.chart_activation_text));

            ImageView cableImage = (ImageView) getActivity().findViewById(R.id.cable_row_credit_view);
            cableImage.setColorFilter(getResources().getColor(R.color.chart_cable_row));
            TextView cableText = (TextView) getActivity().findViewById(R.id.chart_credits_cable_row_text);
            cableText.setTextColor(getResources().getColor(R.color.chart_activation_text));

            ImageView dumbelImage = (ImageView) getActivity().findViewById(R.id.dumbell_credit_view);
            dumbelImage.setColorFilter(getResources().getColor(R.color.chart_dumbell));
            TextView dumbelText = (TextView) getActivity().findViewById(R.id.chart_credits_dumbell_text);
            dumbelText.setTextColor(getResources().getColor(R.color.chart_activation_text));

            ImageView alternateImage = (ImageView) getActivity().findViewById(R.id.alternate_credit_view);
            alternateImage.setColorFilter(getResources().getColor(R.color.chart_alternate));
            TextView alternateText = (TextView) getActivity().findViewById(R.id.chart_credits_alternate_text);
            alternateText.setTextColor(getResources().getColor(R.color.chart_activation_text));

        }
    };


    final public class ChartDataCallback {
        ChartDataCallback() {
        }


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @JavascriptInterface
        public void callBackChart(String str) {
//            WalkRunChartActivity.infoLayoutSetup(str);
            new Thread() {
                public void run() {
                    Message message = handler.obtainMessage();
                    handler.sendMessage(message);
                }
            }.start();
        }
    }

    private void setData() {
        if(tabType == 0) { // daily

        }else if(tabType == 1) {// weekly
        }else if(tabType == 2) {// monthly
        }else if(tabType == 3) {// yearly
        }
    }
}
