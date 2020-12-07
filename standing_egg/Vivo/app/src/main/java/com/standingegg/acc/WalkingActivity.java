/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.standingegg.acc;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.standingegg.acc.util.DBManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WalkingActivity extends AppCompatActivity {

    TextView mStepText;
    WebView mWebview;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sensorValue();
        setContentView(R.layout.count_activity);

        dbManager = new DBManager(getApplicationContext(), "vivo_test", null, 1);

        mStepText = (TextView) findViewById(R.id.goal_text);
        mWebview = (WebView) findViewById(R.id.chart_webview);

        chartView();
        dbSearch();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.nullmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause () {
        super.onPause();
        Log.d("Walking" , "================ onPause : ");
//        stopSensor();
    }

    @Override
    public boolean isDestroyed() {
        Log.d("Walking" , "================ isDestroyed : ");
        //stopSensor();
        return super.isDestroyed();
    }


    private void chartView() {
        //line chart
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setBackgroundColor(Color.TRANSPARENT);
        mWebview.setWebChromeClient(new WebChromeClient());
        mWebview.loadUrl("file:///android_asset/user_line_chart.html");

        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
    }


    private void dbSearch() {
        Date tdays = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(tdays);
        int[] chartArrayValue = new int[12];

        Cursor cursor = dbManager.selectWalk(today);
        while (cursor.moveToNext()) {
            Log.d("Walking", "Walk getPosition : " +cursor.getPosition() +"//today : " + cursor.getString(0) + "//count : " + cursor.getInt(1) + "//goal : " + cursor.getInt(2) + "//date : " + cursor.getString(3) + "//hour : " + cursor.getString(4));
            if (cursor.getInt(4) % 2 == 1) {
                chartArrayValue[(((cursor.getInt(4)) + 1) / 2) - 1] = cursor.getInt(1);
            } else {
                chartArrayValue[(((cursor.getInt(4))) / 2) - 1] = chartArrayValue[(((cursor.getInt(4))) / 2) - 1] + cursor.getInt(1);
            }
        }

        int sumCnt = 0;
        for(int i = 0; i < chartArrayValue.length; i++) {
            sumCnt = chartArrayValue[i] + sumCnt;
        }
        mStepText.setText(sumCnt+"");

        final String data = "["+chartArrayValue[0]+", "+chartArrayValue[1]+", "+chartArrayValue[2]+", "+chartArrayValue[3]+", "+chartArrayValue[4]+", "+chartArrayValue[5]+", "+chartArrayValue[6]+"" +
                ", "+chartArrayValue[7]+", "+chartArrayValue[8]+", "+chartArrayValue[9]+", "+chartArrayValue[10]+", "+chartArrayValue[11]+"]";


        Handler mHandler = new Handler();
        Runnable mMyTask = new Runnable() {
            @Override
            public void run() {
                // 실제 동작
                String data1 = "[23, 0, 0, 100, 68, 765, 2010, 306, 205, 608, 201, 307]";
                mWebview.loadUrl("javascript:setChartData("+data+")");
            }
        };
        mHandler.postDelayed(mMyTask, 1000); // 3초후에 실행
    }

}
