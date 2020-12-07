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

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.standingegg.acc.util.DBManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class StepActivity extends AppCompatActivity {

    TextView stepText, walkText, runText, goalText, percentText;
    Button stepButton, walkButton, runButton, doneButton;

    Bundle bundle;
    private Timer accTimer;
    private DBManager dbManager;
    int mCount, mReset, mWalkReset, mRunReset, totalWCount = 0, totalRCount = 0, wCount = 0, rCount = 0, mMotion = 0;
    int goalCnt;
    double heightCnt, weightCnt;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        sensorValue();
        setContentView(R.layout.step_activity);

        dbManager = new DBManager(getApplicationContext(), "vivo_test", null, 1);

        Intent intent = getIntent();
        bundle = intent.getExtras();
        goalCnt = bundle.getInt("goal");
//        heightCnt = bundle.getDouble("height");
//        weightCnt = bundle.getDouble("weight");



        stepText = (TextView) findViewById(R.id.step_text);
        walkText = (TextView) findViewById(R.id.walk_text);
        runText = (TextView) findViewById(R.id.run_text);
        goalText = (TextView) findViewById(R.id.goal_text);
        percentText = (TextView) findViewById(R.id.percent_text);
        stepButton = (Button) findViewById(R.id.step_button);
        walkButton = (Button) findViewById(R.id.walk_button);
        runButton = (Button) findViewById(R.id.run_button);
        doneButton = (Button) findViewById(R.id.done_button);

        goalText.setText(goalCnt+"");

        stepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("StepAcitiviy", "button click1");
                if("start".equals(stepButton.getText())) {
                    stepButton.setText("stop");
                    sensorValue();
                    accTimer = new Timer();
                    accTimer.schedule(new AccTimerTask(), 1000);
                }else {
                    stepButton.setText("start");
                    stopSensor();
                    accTimer.cancel();
                    accTimer = null;
                }
            }
        });

        walkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                Intent intent = new Intent(getApplicationContext(), WalkingActivity.class);
                startActivity(intent);
            }
        });

        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                Intent intent = new Intent(getApplicationContext(), RunningActivity.class);
                startActivity(intent);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();

                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                Bundle doneBundle = new Bundle();


                doneBundle.putInt("goal", goalCnt);
                doneBundle.putInt("total", Integer.parseInt(stepText.getText().toString()));
                doneBundle.putInt("walk", Integer.parseInt(walkText.getText().toString()));
                doneBundle.putInt("run", Integer.parseInt(runText.getText().toString()));
                doneBundle.putInt("page", 0);
                intent.putExtras(doneBundle);
                startActivity(intent);
            }
        });


        sensorValue();
        accTimer = new Timer();
        accTimer.schedule(new AccTimerTask(), 1000);
    }

    Handler mHandler = new Handler();
    private class AccTimerTask extends TimerTask {
        public AccTimerTask() {
        }
        public void run() {
            mHandler.post(new Runnable() {
                public void run() {
                    try {
                        String accCallback = (String) accCallback();
                        String[] callback = accCallback.split(",");
                        int totalStep, resultWalk, resultRun;
                        int count = Integer.parseInt(callback[0]);
                        int motion = Integer.parseInt(callback[1]);
                        int percent;


                        Date tdays = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String today = format.format(tdays);

                        if(motion == 2) {
                            wCount = (count - mCount);
                            if(wCount > 0) {
                                totalWCount = totalWCount + wCount;
                                dbManager.insertWalkData(today, wCount, tdays.getHours(), 1000);
                            }
                        }else if(motion == 3){
                            rCount = (count - mCount);
                            if(rCount > 0) {
                                totalRCount = totalRCount + rCount;
                                dbManager.insertRunData(today, rCount, tdays.getHours(), 1000);
                            }
                        }

                        mCount = count;
                        mMotion = motion;

//                        Log.d("StepActivity", "mCount : " + mCount + "// mReset: " + mReset + "// totalWCount: " + totalWCount + "// mWalkReset: " + mWalkReset + "// totalRCount: " + totalRCount + "// mRunReset: " + mRunReset);

                        if(mReset > 0) {
                            totalStep = (mCount - mReset);
                            resultWalk = (totalWCount - mWalkReset);
                            resultRun = (totalRCount - mRunReset);
                        }else {
                            totalStep = mCount;
                            resultWalk = totalWCount;
                            resultRun = totalRCount;
                        }
                        percent = (totalStep*100/goalCnt);

                        stepText.setText(totalStep+"");
                        percentText.setText(percent+"%");
                        walkText.setText(resultWalk+"");
                        runText.setText(resultRun+"");

                        accTimer.schedule(new StepActivity.AccTimerTask(),1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.goal:
                stop();
                finish();
                Intent goalIntent = new Intent(getApplicationContext(), GoalActivity.class);
                startActivity(goalIntent);
                break;

            case R.id.list:
//                mReset = mCount;
//                stepText.setText("0 Step");
//                dbManager.dropDB();
                stop();
                Intent intent = new Intent(getApplicationContext(), SaveActivity.class);
                startActivity(intent);
                break;

            /*case R.id.reset:
                mReset = mCount;
                mWalkReset = totalWCount;
                mRunReset = totalRCount;
                stepText.setText("0");
                percentText.setText("0%");
                walkText.setText("0");
                runText.setText("0");
//                dbManager.dropDB();
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if(0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime){
            super.onBackPressed();
            finish();
        } else{
            backPressedTime = tempTime;
        }
    }



    @Override
    public void onResume() {
        super.onResume();

        /*if(accTimer == null) {
            sensorValue();
            accTimer = new Timer();
            accTimer.schedule(new AccTimerTask(), 1000);
        }*/
    }

    private PowerManager.WakeLock mWakeLock;

    @Override
    public void onPause () {
        super.onPause();

        final PowerManager powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        releaseWakeLock();
        //Acquire new wake lock
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "PARTIAL_WAKE_LOCK");
        mWakeLock.acquire();
    }

    public void releaseWakeLock() {
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    @Override
    public boolean isDestroyed() {
        return super.isDestroyed();
    }

    private void stop() {
        mReset = mCount;
        mWalkReset = totalWCount;
        mRunReset = totalRCount;

//        if(accTimer != null) {
//            stopSensor();
//            accTimer.cancel();
//            accTimer = null;
//        }
    }






    public native void  sensorValue();
    public native String  accCallback();
    public native void stopSensor();
    static {
        System.loadLibrary("hello-jni");
    }

}
