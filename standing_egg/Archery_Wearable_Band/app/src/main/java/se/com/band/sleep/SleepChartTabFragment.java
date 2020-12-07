package se.com.band.sleep;

import android.annotation.TargetApi;
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
import android.widget.TextView;

import se.com.band.R;

/**
 * Created by moon on 2017-01-20.
 */

public class SleepChartTabFragment extends Fragment {

    WebView mChartWebview;
    TextView mChartAxiText;

    int mPosition;

    public SleepChartTabFragment(int position) {
        mPosition = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sleep_chart_tab, container, false);

        mChartWebview = (WebView) v.findViewById(R.id.chart_webview);
        mChartAxiText = (TextView) v.findViewById(R.id.chart_axis_text);
        chartView();



        return v;
    }

    private void chartView() {
        //line chart
        mChartWebview.getSettings().setJavaScriptEnabled(true);
        mChartWebview.setBackgroundColor(Color.TRANSPARENT);
        mChartWebview.setWebChromeClient(new WebChromeClient());
        mChartWebview.addJavascriptInterface(new ChartDataCallback(), "android");
        mChartWebview.loadUrl("file:///android_asset/chart/sleep_chart.html");

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
        String data1 = "";
        String data2 = "";
        String categories = "";
        if(mPosition == 0) {
            mChartAxiText.setText("minute");
            data1 = "[60, 43, 0, 0, 0, 0, 0, 0, 0, 0, 0, 24]";
            data2 = "[0, 27, 60, 0,0, 0, 0, 0, 0, 0, 0, 34]";
            categories = "[2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24]";
        }else if(mPosition == 1) {
            mChartAxiText.setText("hour");
            data1 = "[3, 4, 2, 6, 5, 4, 2]";
            data2 = "[4, 4, 7, 3, 3, 4, 2]";
            categories = "[2,3,4,5,6,7,8]";
        }else if(mPosition == 2) {
            mChartAxiText.setText("hour");
            data1 = "[3, 4, 2, 6, 5]";
            data2 = "[4, 4, 7, 3, 3]";
            categories = "[6,13,20,27,3]";
        }else if(mPosition == 3) {
            mChartAxiText.setText("hour");
            data1 = "[3, 4, 2, 6, 5, 4, 4, 7, 3, 3]";
            data2 = "[4, 4, 7, 3, 3, 4, 4, 7, 3, 3]";
            categories = "[5,6,7,8,9,10,11,12,1,2]";
        }

        mChartWebview.loadUrl("javascript:setChartData("+data2+", "+ data1 +", "+ categories +")");
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            TextView totalText = (TextView) getActivity().findViewById(R.id.heart_min);
            totalText.setText("07:11");
            TextView lightText = (TextView) getActivity().findViewById(R.id.heart_max);
            lightText.setText("02:50");
            TextView deepText = (TextView) getActivity().findViewById(R.id.sleep_deep_sleep);
            deepText.setText("04:21");
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
}
