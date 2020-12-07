/**
 * Disclaimer: IMPORTANT:  This Nulana software is supplied to you by Nulana
 * LTD ("Nulana") in consideration of your agreement to the following
 * terms, and your use, installation, modification or redistribution of
 * this Nulana software constitutes acceptance of these terms.  If you do
 * not agree with these terms, please do not use, install, modify or
 * redistribute this Nulana software.
 *
 * In consideration of your agreement to abide by the following terms, and
 * subject to these terms, Nulana grants you a personal, non-exclusive
 * license, under Nulana's copyrights in this original Nulana software (the
 * "Nulana Software"), to use, reproduce, modify and redistribute the Nulana
 * Software, with or without modifications, in source and/or binary forms;
 * provided that if you redistribute the Nulana Software in its entirety and
 * without modifications, you must retain this notice and the following
 * text and disclaimers in all such redistributions of the Nulana Software.
 * Except as expressly stated in this notice, no other rights or licenses, 
 * express or implied, are granted by Nulana herein, including but not limited 
 * to any patent rights that may be infringed by your derivative works or by other
 * works in which the Nulana Software may be incorporated.
 *
 * The Nulana Software is provided by Nulana on an "AS IS" basis.  NULANA
 * MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 * THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE, REGARDING THE NULANA SOFTWARE OR ITS USE AND
 * OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 *
 * IN NO EVENT SHALL NULANA BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION,
 * MODIFICATION AND/OR DISTRIBUTION OF THE NULANA SOFTWARE, HOWEVER CAUSED
 * AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE),
 * STRICT LIABILITY OR OTHERWISE, EVEN IF NULANA HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright (C) 2016 Nulana LTD. All Rights Reserved.
 */

package com.se.idt.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.se.idt.R;
import com.se.idt.utility.ULog;

public class FitnessActivity extends AppCompatActivity {

    private static Context mContext;
    public static AppCompatActivity mFitness;

    static TextView pushText;
    static TextView sitText;
    static TextView butterflyText;
    static TextView bicepsText;
    static TextView shoulderText;
    static TextView kettlebellText;
    static TextView seatedText;
    static TextView dumbellText;
//    static TextView tricepsText;
    static TextView benchText;
    static TextView squatText;
    static TextView alternateText;
//    static TextView waitingText;
//    static TextView analyzingText;
    static TextView motionText;


    static LinearLayout pushLayout;
    static LinearLayout sitLayout;
    static LinearLayout butterflyLayout;
    static LinearLayout bicepsLayout;
    static LinearLayout shoulderLayout;
    static LinearLayout kettlebellLayout;
    static LinearLayout seatedLayout;
    static LinearLayout dumbellLayout;
    static LinearLayout benchLayout;
    static LinearLayout squatLayout;
    static LinearLayout alternateLayout;

    Button mActivityButton;


    static ProgressBar mProgressBar;
    static RelativeLayout mProgressLayout;
    static TextView mSampleText;

    static int mpushCnt = 0;
    static int msitCnt = 0;
    static int mbutterflyCnt = 0;
    static int mbicepsCnt = 0;
    static int mshoulderCnt = 0;
    static int mkettlebellCnt = 0;
    static int mseatedCnt = 0;
    static int mdumbellCnt = 0;
//    static int mtricepsCnt = 0;
    static int mbenchCnt = 0;
    static int msquatCnt = 0;
    static int malternateCnt = 0;
//    static int mwaitingCnt = 0;
//    static int manalyzingCnt = 0;


    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.fitness);
        mContext = this;
        mFitness = this;

        pushText = (TextView) findViewById(R.id.pushup_text);
        sitText = (TextView) findViewById(R.id.situp_text);
        bicepsText = (TextView) findViewById(R.id.biceps_text);
        butterflyText = (TextView) findViewById(R.id.butterfly_text);
        shoulderText = (TextView) findViewById(R.id.shoulder_text);
        kettlebellText = (TextView) findViewById(R.id.kettlebell_text);
        seatedText = (TextView) findViewById(R.id.seated_text);
        dumbellText = (TextView) findViewById(R.id.dumbell_text);
//        tricepsText = (TextView) findViewById(R.id.triceps_text);
        benchText = (TextView) findViewById(R.id.bench_text);
        squatText = (TextView) findViewById(R.id.squat_text);
        alternateText = (TextView) findViewById(R.id.alternate_text);
//        waitingText = (TextView) findViewById(R.id.waiting_text);
//        analyzingText = (TextView) findViewById(R.id.analyzing_text);
        motionText  = (TextView) findViewById(R.id.motion_text);


        pushLayout = (LinearLayout) findViewById(R.id.pushup_layout);
        sitLayout = (LinearLayout) findViewById(R.id.situp_layout);
        bicepsLayout = (LinearLayout) findViewById(R.id.biceps_layout);
        butterflyLayout = (LinearLayout) findViewById(R.id.butterfly_layout);
        shoulderLayout = (LinearLayout) findViewById(R.id.shoulder_layout);
        kettlebellLayout = (LinearLayout) findViewById(R.id.kettlebell_layout);
        seatedLayout = (LinearLayout) findViewById(R.id.seated_layout);
        dumbellLayout = (LinearLayout) findViewById(R.id.dumbell_layout);
        benchLayout = (LinearLayout) findViewById(R.id.bench_layout);
        squatLayout = (LinearLayout) findViewById(R.id.squat_layout);
        alternateLayout = (LinearLayout) findViewById(R.id.alternate_layout);



        mActivityButton = (Button) findViewById(R.id.activity_button);
        mActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdtActivity.status = 1;
                IdtActivity.motionMessageSend(3);

                Intent intent = new Intent(getApplicationContext(), WalkRunActivity.class);
                startActivity(intent);
                IdtActivity.motionMessageSend(3);
                finish();
            }
        });


        mProgressLayout = (RelativeLayout) findViewById(R.id.progress_layout);
        mProgressLayout.setVisibility(View.INVISIBLE);

        mSampleText = (TextView) findViewById(R.id.sample_count_text);


        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        mProgressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!navigationDrawerFragment.isDrawerOpen()) {
        // Only show items in the action bar relevant to this screen
        // if the drawer is not showing. Otherwise, let the drawer
        // decide what to show in the action bar.
        getMenuInflater().inflate(R.menu.activtiy_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        ULog.d("onOptionsItemSelected", "id: " + item.getTitle().toString());
        switch (item.getItemId()) {
            case R.id.reset_menu:
                mpushCnt = 0;
                msitCnt = 0;
                mbutterflyCnt = 0;
                mbicepsCnt = 0;
                mshoulderCnt = 0;
                mkettlebellCnt = 0;
                mseatedCnt = 0;
                mdumbellCnt = 0;
                mbenchCnt = 0;
                msquatCnt = 0;
                malternateCnt = 0;

                pushText.setText(mpushCnt+"");
                sitText.setText(msitCnt+"");
                butterflyText.setText(mbutterflyCnt+"");
                bicepsText.setText(mbicepsCnt+"");
                shoulderText.setText(mshoulderCnt+"");
                kettlebellText.setText(mkettlebellCnt+"");
                seatedText.setText(mseatedCnt+"");
                dumbellText.setText(mdumbellCnt+"");
                benchText.setText(mbenchCnt+"");
                squatText.setText(msquatCnt+"");
                alternateText.setText(malternateCnt+"");

                layoutInit();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IdtActivity.eStatus = 1;
    }

    public static void setProgressVisibility(boolean visibility, String motion, boolean color, int count) {
        try {
            motionText.setText(motion);
            if (color) {
                motionText.setTextSize(20);
                motionText.setTextColor(Color.RED);
                mProgressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            } else {
                motionText.setTextSize(10);
                motionText.setTextColor(mContext.getResources().getColor(R.color.metrial));
                mProgressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
            }


            if (visibility) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressLayout.setVisibility(View.VISIBLE);

                if(count > 0) {
                    mSampleText.setText(count + "");
                }else {
                    mSampleText.setText("0");
                }
            } else {
                mProgressBar.setVisibility(View.INVISIBLE);
                mProgressLayout.setVisibility(View.INVISIBLE);
            }
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

//    public static void setText(int pushCnt, int sitCnt, int butterflyCnt, int bicepsCnt, int shoulderCnt, int kettlebellCnt, int seatedCnt, int dumbellCnt, int tricepsCnt, int benchCnt, int squatCnt, int alternateCnt, int waitingCnt, int analyzingCnt) {
    public static void setText(int pushCnt, int sitCnt, int butterflyCnt, int bicepsCnt, int shoulderCnt, int kettlebellCnt, int seatedCnt, int dumbellCnt, int benchCnt, int squatCnt, int alternateCnt) {
        mpushCnt += pushCnt;
        msitCnt += sitCnt;
        mbutterflyCnt += butterflyCnt;
        mbicepsCnt += bicepsCnt;
        mshoulderCnt += shoulderCnt;
        mkettlebellCnt += kettlebellCnt;
        mseatedCnt += seatedCnt;
        mdumbellCnt += dumbellCnt;
        mbenchCnt += benchCnt;
        msquatCnt += squatCnt;
        malternateCnt += alternateCnt;

        layoutInit();


        if(pushCnt != 0) {
            pushLayout.setBackgroundResource(R.drawable.pushup_);
        }else if(sitCnt != 0) {
            sitLayout.setBackgroundResource(R.drawable.situp_);
        }else if(bicepsCnt != 0) {
            bicepsLayout.setBackgroundResource(R.drawable.bicepscurl_);
        }else if(butterflyCnt != 0) {
            butterflyLayout.setBackgroundResource(R.drawable.butterfly_);
        }else if(shoulderCnt != 0) {
            shoulderLayout.setBackgroundResource(R.drawable.shoulder_press_);
        }else if(kettlebellCnt != 0) {
            kettlebellLayout.setBackgroundResource(R.drawable.kettlebell_swing_);
        }else if(seatedCnt != 0) {
            seatedLayout.setBackgroundResource(R.drawable.seatedcablerow_);
        }else if(dumbellCnt != 0) {
            dumbellLayout.setBackgroundResource(R.drawable.seateddumbell_);
        }else if(benchCnt != 0) {
            benchLayout.setBackgroundResource(R.drawable.benchkickback_);
        }else if(squatCnt != 0) {
            squatLayout.setBackgroundResource(R.drawable.squat_);
        }else if(alternateCnt != 0) {
            alternateLayout.setBackgroundResource(R.drawable.alternate_deltoid_raise_);
        }




        pushText.setText(mpushCnt+"");
        sitText.setText(msitCnt+"");
        butterflyText.setText(mbutterflyCnt+"");
        bicepsText.setText(mbicepsCnt+"");
        shoulderText.setText(mshoulderCnt+"");
        kettlebellText.setText(mkettlebellCnt+"");
        seatedText.setText(mseatedCnt+"");
        dumbellText.setText(mdumbellCnt+"");
//        tricepsText.setText(tricepsCnt+"");
        benchText.setText(mbenchCnt+"");
        squatText.setText(msquatCnt+"");
        alternateText.setText(malternateCnt+"");
//        waitingText.setText(waitingCnt+"");
//        analyzingText.setText(analyzingCnt+"");
    }

    private static void layoutInit() {
        pushLayout.setBackgroundResource(R.drawable.pushup);
        sitLayout.setBackgroundResource(R.drawable.situp);
        bicepsLayout.setBackgroundResource(R.drawable.bicepscurl);
        butterflyLayout.setBackgroundResource(R.drawable.butterfly);
        shoulderLayout.setBackgroundResource(R.drawable.shoulder_press);
        kettlebellLayout.setBackgroundResource(R.drawable.kettlebell_swing);
        seatedLayout.setBackgroundResource(R.drawable.seatedcablerow);
        dumbellLayout.setBackgroundResource(R.drawable.seateddumbell);
        benchLayout.setBackgroundResource(R.drawable.benchkickback);
        squatLayout.setBackgroundResource(R.drawable.squat);
        alternateLayout.setBackgroundResource(R.drawable.alternate_deltoid_raise);
    }

}
