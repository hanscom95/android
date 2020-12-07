package se.com.band.heart;

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

public class HeartChartTabFragment extends Fragment {
    WebView mChartWebview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.heart_chart_tab, container, false);

        mChartWebview = (WebView) v.findViewById(R.id.chart_webview);
        chartView();

        return v;
    }

    private void chartView() {
        //line chart
        mChartWebview.getSettings().setJavaScriptEnabled(true);
        mChartWebview.setBackgroundColor(Color.TRANSPARENT);
        mChartWebview.setWebChromeClient(new WebChromeClient());
        mChartWebview.addJavascriptInterface(new ChartDataCallback(), "android");
        mChartWebview.loadUrl("file:///android_asset/chart/heart_chart.html");

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
        String data1 = "[78, 59, 64, 61, 63]";
        String data2 = "[121, 135, 118, 136, 128]";
        String categories = "[6, 13, 20, 27, 3]";
        mChartWebview.loadUrl("javascript:setChartData("+data2+", "+ data1 +", " + categories + ")");
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            TextView minText = (TextView) getActivity().findViewById(R.id.heart_min);
            minText.setText("70");
            TextView maxText = (TextView) getActivity().findViewById(R.id.heart_max);
            maxText.setText("128");
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
