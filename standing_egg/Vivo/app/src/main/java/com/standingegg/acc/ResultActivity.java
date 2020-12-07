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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.standingegg.acc.util.DBManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {

    TextView stepText, walkText, runText, goalText, percentText;
    Button deleteButton, saveButton;

    Bundle bundle;
    private DBManager dbManager;
    int goalCnt, totalCnt, walkCnt, runCnt, percent, page;
    String _key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sensorValue();
        setContentView(R.layout.result_activity);

        dbManager = new DBManager(getApplicationContext(), "vivo_test", null, 1);

        Intent intent = getIntent();
        bundle = intent.getExtras();
        goalCnt = bundle.getInt("goal");
        totalCnt = bundle.getInt("total");
        walkCnt = bundle.getInt("walk");
        runCnt = bundle.getInt("run");
        page = bundle.getInt("page");
        percent = (totalCnt*100)/goalCnt;


        goalText = (TextView) findViewById(R.id.goal_text);
        stepText = (TextView) findViewById(R.id.step_text);
        walkText = (TextView) findViewById(R.id.walk_text);
        runText = (TextView) findViewById(R.id.run_text);
        percentText = (TextView) findViewById(R.id.percent_text);
        saveButton = (Button) findViewById(R.id.save_button);
        deleteButton = (Button) findViewById(R.id.delete_button);


        goalText.setText("" + goalCnt);
        stepText.setText("" + totalCnt);
        walkText.setText("" + walkCnt);
        runText.setText("" + runCnt);
        percentText.setText("" + percent);

        if(page > 0) {
            _key = bundle.getString("key");
            saveButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
        }



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalCnt > 0) {
                    Date tdays = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String today = format.format(tdays);

                    dbManager.insertTotalData(today, totalCnt, walkCnt, runCnt, percent, tdays.getHours(), goalCnt);

                    Toast.makeText(getApplicationContext(), "Saved Successfully.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Not Counting Steps.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        if(page > 0) {
            getMenuInflater().inflate(R.menu.remove, menu);
        }else {
            getMenuInflater().inflate(R.menu.nullmenu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.remove:
                Log.d("ResultActivity", "key : " + _key);
                dbManager.removeSelectTotal(_key);
                Toast.makeText(getApplicationContext(), "Remove Successfully.", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause () {
        super.onPause();
        Log.d("test" , "================ onPause : ");
//        stopSensor();
    }

    @Override
    public boolean isDestroyed() {
        Log.d("test" , "================ isDestroyed : ");
        //stopSensor();
        return super.isDestroyed();
    }
}
