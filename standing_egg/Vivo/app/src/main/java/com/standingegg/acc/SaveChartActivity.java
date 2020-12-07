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
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.standingegg.acc.util.CustomAdapter;
import com.standingegg.acc.util.DBManager;
import com.standingegg.acc.util.ListModel;

import java.util.ArrayList;

public class SaveChartActivity extends AppCompatActivity {

    private DBManager dbManager;
    WebView mWebview;
    TextView mTotalText;

    int totalRate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sensorValue();
        setContentView(R.layout.save_chart_activity);

        dbManager = new DBManager(getApplicationContext(), "vivo_test", null, 1);
        mWebview = (WebView) findViewById(R.id.chart_webview);
        mTotalText = (TextView) findViewById(R.id.total_text);

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
    }

    @Override
    public boolean isDestroyed() {
        return super.isDestroyed();
    }


    private void dbSearch() {
        Cursor cursor = dbManager.selectTotal();
        final ArrayList<String> categories = new ArrayList<String>();
        final ArrayList<String> percents = new ArrayList<String>();

        while (cursor.moveToNext()) {
            Log.d("SaveActivity", "total _key : " +cursor.getInt(0) +"//today : " + cursor.getString(1) + "//count : " + cursor.getInt(2) + "//walk : " + cursor.getInt(3) + "//run : " + cursor.getInt(4) +
                    "//percent : " + cursor.getInt(5) + "//hour : " + cursor.getString(6) + "//goal : " + cursor.getString(7));
            categories.add(cursor.getString(1));
            percents.add(cursor.getInt(5)+"");
            totalRate += cursor.getInt(5);
        }

        if(totalRate > 0) {
            totalRate = totalRate/percents.size();
            mTotalText.setText(totalRate+"");
        }

        Handler mHandler = new Handler();
        Runnable mMyTask = new Runnable() {
            @Override
            public void run() {
                // 실제 동작
                String data1 = "[";
                for(int i = 0; i < categories.size(); i++) {
                    if(i > 0) {
                        data1 += ", ";
                    }
                    data1 += "'Data_"+i+"<br><span style=\"font-size: 7px;\">"+categories.get(i)+"</span>'";
                }
                data1 += "]";

                String data2 = "[";
                for(int i = 0; i < percents.size(); i++) {
                    if(i > 0) {
                        data2 += ", ";
                    }
                    data2 += percents.get(i);
                }
                data2 += "]";
//                data2 = "[98, 90, 97]";

                Log.d("SaveChartAcitivity", "data1 : " + data1);
                Log.d("SaveChartAcitivity", "data2 : " + data2);
                mWebview.loadUrl("javascript:setChartData("+data1+", "+data2+")");
            }
        };
        mHandler.postDelayed(mMyTask, 1000); // 3초후에 실행
    }


    private void chartView() {
        //line chart
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setBackgroundColor(Color.TRANSPARENT);
        mWebview.setWebChromeClient(new WebChromeClient());
        mWebview.loadUrl("file:///android_asset/user_area_chart.html");

        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
    }
}
