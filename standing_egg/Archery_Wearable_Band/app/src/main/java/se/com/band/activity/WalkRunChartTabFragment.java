package se.com.band.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
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

import java.util.ArrayList;
import java.util.Date;

import se.com.band.R;
import se.com.band.utility.Constants;
import se.com.band.utility.DBManager;
import se.com.band.utility.Preferences;
import se.com.band.utility.ULog;

/**
 * Created by moon on 2017-01-20.
 */

public class WalkRunChartTabFragment extends Fragment {
    Context mContext;
    DBManager mDBManager;
    Preferences mPreference;
    Constants mConstant;

    WebView mChartWebview;

    TextView mActStepText, mActCalText, mActDistanceText, mActTimeText;

    int tabType = 0;

    WalkRunChartTabFragment(Context c, int type) {
        this.mContext = c;
        this.tabType = type;
        mDBManager = new DBManager(mContext, "apex.db", null, 1);
        mPreference = Preferences.getInstance(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chart_tab, container, false);

        mChartWebview = (WebView) v.findViewById(R.id.chart_webview);
        chartView();

        mActStepText = (TextView) getActivity().findViewById(R.id.activity_steps);
        mActCalText = (TextView) getActivity().findViewById(R.id.activity_cal);
        mActDistanceText = (TextView) getActivity().findViewById(R.id.activity_distance);
        mActTimeText = (TextView) getActivity().findViewById(R.id.activity_time);

        setData();

        return v;
    }

    private void chartView() {
        //line chart
        mChartWebview.getSettings().setJavaScriptEnabled(true);
        mChartWebview.setBackgroundColor(Color.TRANSPARENT);
        mChartWebview.setWebChromeClient(new WebChromeClient());
        mChartWebview.addJavascriptInterface(new ChartDataCallback(), "android");
        mChartWebview.loadUrl("file:///android_asset/chart/activity_chart.html");

        mChartWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                chartSampleData();
                setData();
            }
        });
    }

    public void chartSampleData(){
        String data1 = "[0, 0, 0, 1000, 2200, 2980, 0, 3000, 0, 0, 0, 0]";
        String data2 = "[0, 0, 0, 150, 1000, 800, 0, 1800, 0, 0, 0, 0]";
        mChartWebview.loadUrl("javascript:setChartData("+data2+", "+ data1 +")");
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ULog.d(""+msg.toString());
            mActStepText.setText("3475");
            mActCalText.setText("164");
            mActDistanceText.setText("1.52");
            mActTimeText.setText("01:24");
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
        int totalStep = 0;
        int totalCal = 0;
        int totalDistance = 0;
        int totalTime = 0;


        if(tabType == 0) { // daily
            int[] walkStepsValue = new int[12];
            int[] runStepsValue = new int[12];
            for(int i = 0; i < walkStepsValue.length; i++) {
                walkStepsValue[i] = 0;
                runStepsValue[i] = 0;
            }

            Cursor cursor = mDBManager.selectDailyActivity();
            while (cursor.moveToNext()) {
                totalStep += cursor.getInt(1);
                totalDistance += cursor.getInt(2);
                totalCal += cursor.getInt(3);
                totalTime += cursor.getInt(5);
                if (Integer.parseInt(cursor.getString(0)) == mConstant.MOTION_WALKING) {
                    if(cursor.getInt(4)%2 == 1) {
                        walkStepsValue[(((cursor.getInt(4))+1)/2)-1] = cursor.getInt(1);
                    }else {
                        walkStepsValue[(((cursor.getInt(4)))/2)-1] = walkStepsValue[(((cursor.getInt(4)))/2)-1] + cursor.getInt(1);
                    }
                } else if (Integer.parseInt(cursor.getString(0)) == mConstant.MOTION_RUNNING) {
                    if(cursor.getInt(4)%2 == 1) {
                        runStepsValue[(((cursor.getInt(4))+1)/2)-1] = cursor.getInt(1);
                    }else {
                        runStepsValue[(((cursor.getInt(4)))/2)-1] = runStepsValue[(((cursor.getInt(4)))/2)-1] + cursor.getInt(1);
                    }
                }
            }


            String category = "['2', '4', '6', '8', '10','12', '14', '16', '18', '20', '22', '24']";
            String walk = "[";
            String run = "[";
            for(int i = 0; i < walkStepsValue.length; i++) {
                if(i == 0) {
                    walk += walkStepsValue[i];
                    run += runStepsValue[i];
                }else {
                    walk += ", " + walkStepsValue[i];
                    run += ", " + runStepsValue[i];
                }
            }
            walk += "]";
            run += "]";

            mChartWebview.loadUrl("javascript:setChartData("+run+", "+ walk +", "+category+")");


            ULog.d("total step: " + totalStep + "/// cal : " + totalCal + "//// distatnce : " + totalDistance + "/// time : " + totalTime);
            int w_hr = totalTime/3600;
            int w_min = ((totalTime%3600)/60);
            String time = (w_hr < 10 ? "0" + w_hr : w_hr) + ":" +
                    (w_min < 10 ? "0" + w_min : w_min);


            mActStepText.setText(totalStep+"");
            mActCalText.setText(totalCal+"");
            mActDistanceText.setText(totalDistance+"");
            mActTimeText.setText(time);
            mPreference.setDailyTotalActivity(totalStep, totalDistance, totalCal, time);


        }else if(tabType == 1) {// weekly
            // now time setting
            Date tdays = new Date();
            int date1 = tdays.getDate();
            tdays.setDate(tdays.getDate()-1);
            int date2 = tdays.getDate();
            tdays.setDate(tdays.getDate()-1);
            int date3 = tdays.getDate();
            tdays.setDate(tdays.getDate()-1);
            int date4 = tdays.getDate();
            tdays.setDate(tdays.getDate()-1);
            int date5 = tdays.getDate();
            tdays.setDate(tdays.getDate()-1);
            int date6 = tdays.getDate();
            tdays.setDate(tdays.getDate()-1);
            int date7 = tdays.getDate();

            int[] walkStepsValue = new int[7];
            int[] runStepsValue = new int[7];
            for(int i = 0; i < walkStepsValue.length; i++) {
                walkStepsValue[i] = 0;
                runStepsValue[i] = 0;
            }

            Cursor cursor = mDBManager.selectWeeklyActivity();
            while (cursor.moveToNext()) {
                totalStep += cursor.getInt(1);
                totalDistance += cursor.getInt(2);
                totalCal += cursor.getInt(3);
                totalTime += cursor.getInt(5);
                int dayFlag = 0;
                if(Integer.parseInt(cursor.getString(4).split("-")[2]) == date1) {
                    dayFlag = 7;
                }else if(Integer.parseInt(cursor.getString(4).split("-")[2]) == date2) {
                    dayFlag = 6;
                }else if(Integer.parseInt(cursor.getString(4).split("-")[2]) == date3) {
                    dayFlag = 5;
                }else if(Integer.parseInt(cursor.getString(4).split("-")[2]) == date4) {
                    dayFlag = 4;
                }else if(Integer.parseInt(cursor.getString(4).split("-")[2]) == date5) {
                    dayFlag = 3;
                }else if(Integer.parseInt(cursor.getString(4).split("-")[2]) == date6) {
                    dayFlag = 2;
                }else if(Integer.parseInt(cursor.getString(4).split("-")[2]) == date7) {
                    dayFlag = 1;
                }

                if (Integer.parseInt(cursor.getString(0)) == mConstant.MOTION_WALKING) {
                    walkStepsValue[dayFlag-1] = cursor.getInt(1);
                } else if (Integer.parseInt(cursor.getString(0)) == mConstant.MOTION_RUNNING) {
                    runStepsValue[dayFlag-1] = cursor.getInt(1);
                }
            }

            String category = "['" + date7 +"', '"+ date6 +"', '"+ date5 +"', '"+ date4 +"', '"+ date3+"', '"+ date2 +"', '"+ date1 +"']";
            String walk = "[";
            String run = "[";
            for(int i = 0; i < walkStepsValue.length; i++) {
                if(i == 0) {
                    walk += walkStepsValue[i];
                    run += runStepsValue[i];
                }else {
                    walk += ", " + walkStepsValue[i];
                    run += ", " + runStepsValue[i];
                }
            }
            walk += "]";
            run += "]";

            mChartWebview.loadUrl("javascript:setChartData("+run+", "+ walk +", "+category+")");


            ULog.d("total step: " + totalStep + "/// cal : " + totalCal + "//// distatnce : " + totalDistance + "/// time : " + totalTime);
            int w_hr = totalTime/3600;
            int w_min = ((totalTime%3600)/60);
            String time = (w_hr < 10 ? "0" + w_hr : w_hr) + ":" +
                    (w_min < 10 ? "0" + w_min : w_min);

            mPreference.setWeeklyTotalActivity(totalStep, totalDistance, totalCal, time);



        }else if(tabType == 2) {// monthly
            // now time setting
            Date tdays = new Date();
            int week1 = tdays.getDate();
            tdays.setDate(tdays.getDate()-7);
            int week2 = tdays.getDate();
            tdays.setDate(tdays.getDate()-14);
            int week3 = tdays.getDate();
            tdays.setDate(tdays.getDate()-21);
            int week4 = tdays.getDate();
            tdays.setDate(tdays.getDate()-28);
            int week5 = tdays.getDate();


            int[] walkStepsValue = new int[5];
            int[] runStepsValue = new int[5];
            for(int i = 0; i < walkStepsValue.length; i++) {
                walkStepsValue[i] = 0;
                runStepsValue[i] = 0;
            }

            Cursor cursor = mDBManager.selectMonthlyActivity();
            while (cursor.moveToNext()) {
                totalStep += cursor.getInt(1);
                totalDistance += cursor.getInt(2);
                totalCal += cursor.getInt(3);
                totalTime += cursor.getInt(5);
                if (Integer.parseInt(cursor.getString(0)) == mConstant.MOTION_WALKING) {
                    walkStepsValue[cursor.getInt(4)-1] = cursor.getInt(1);
                } else if (Integer.parseInt(cursor.getString(0)) == mConstant.MOTION_RUNNING) {
                    runStepsValue[cursor.getInt(4)-1] = cursor.getInt(1);
                }
            }

            String category = "['" + week1 +"', '"+ week2 +"', '"+ week3 +"', '"+ week4 +"', '"+ week5 +"']";
            String walk = "[";
            String run = "[";
            for(int i = 0; i < walkStepsValue.length; i++) {
                if(i == 0) {
                    walk += walkStepsValue[i];
                    run += runStepsValue[i];
                }else {
                    walk += ", " + walkStepsValue[i];
                    run += ", " + runStepsValue[i];
                }
            }
            walk += "]";
            run += "]";

            mChartWebview.loadUrl("javascript:setChartData("+run+", "+ walk +", "+category+")");


            ULog.d("total step: " + totalStep + "/// cal : " + totalCal + "//// distatnce : " + totalDistance + "/// time : " + totalTime);
            int w_hr = totalTime/3600;
            int w_min = ((totalTime%3600)/60);
            String time = (w_hr < 10 ? "0" + w_hr : w_hr) + ":" +
                    (w_min < 10 ? "0" + w_min : w_min);

            mPreference.setMonthlyTotalActivity(totalStep, totalDistance, totalCal, time);



        }else if(tabType == 3) {// yearly

            Date tdays = new Date();
            int month =tdays.getMonth()+1;
            int[] walkStepsValue = new int[12];
            int[] runStepsValue = new int[12];
            int[] categoryValue = new int[12];
            for(int i = 0, j = 11; i < walkStepsValue.length; i++, j--) {
                walkStepsValue[i] = 0;
                runStepsValue[i] = 0;

                if(month == 0) {
                    month = 12;
                }
                categoryValue[j] = month--;
            }


            Cursor cursor = mDBManager.selectYearlyActivity();
            while (cursor.moveToNext()) {
                totalStep += cursor.getInt(1);
                totalDistance += cursor.getInt(2);
                totalCal += cursor.getInt(3);
                totalTime += cursor.getInt(5);
                if (Integer.parseInt(cursor.getString(0)) == mConstant.MOTION_WALKING) {
                    walkStepsValue[Integer.parseInt(cursor.getString(4).split("-")[1])-1] = cursor.getInt(1);
                } else if (Integer.parseInt(cursor.getString(0)) == mConstant.MOTION_RUNNING) {
                    runStepsValue[Integer.parseInt(cursor.getString(4).split("-")[1])-1] = cursor.getInt(1);
                }
            }


            String category = "[";
            String walk = "[";
            String run = "[";
            month = tdays.getMonth()+1;
            for(int i = 0; i < walkStepsValue.length; i++, month++) {
                if(month == 12) {
                    month = 0;
                }

                if(i == 0) {
                    category += "'"+categoryValue[i]+"'";
                    walk += walkStepsValue[month];
                    run += runStepsValue[month];
                }else {
                    category += ", '" + categoryValue[i]+"'";
                    walk += ", " + walkStepsValue[month];
                    run += ", " + runStepsValue[month];
                }
            }
            category += "]";
            walk += "]";
            run += "]";

            mChartWebview.loadUrl("javascript:setChartData("+run+", "+ walk +", "+category+")");


            ULog.d("total step: " + totalStep + "/// cal : " + totalCal + "//// distatnce : " + totalDistance + "/// time : " + totalTime);
            int w_hr = totalTime/3600;
            int w_min = ((totalTime%3600)/60);
            String time = (w_hr < 10 ? "0" + w_hr : w_hr) + ":" +
                    (w_min < 10 ? "0" + w_min : w_min);

            mPreference.setYearlyTotalActivity(totalStep, totalDistance, totalCal, time);
        }
    }
}
