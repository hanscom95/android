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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.se.idt.R;
import com.se.idt.utility.ULog;

public class WalkRunActivity extends AppCompatActivity {

    private Context mContext;
    public static AppCompatActivity mActivity;

    static TextView mWalkingText;
    static TextView mRunningText;
    static TextView mCurrentMotionText;
    static LinearLayout mWalkLayout;
    static LinearLayout mRunLayout;

    Button mMotionButton;

    static int walkingCnt = 0;
    static int runningCnt = 0;

    static boolean initState = false;
    static int initWalkingCnt = 0;
    static int initRunningCnt = 0;


    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.walk_run);
        mContext = this;
        mActivity = this;

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbar);

        mWalkingText = (TextView) findViewById(R.id.walk_text);
        mRunningText = (TextView) findViewById(R.id.run_text);
        mCurrentMotionText = (TextView) findViewById(R.id.current_text);

        mWalkLayout = (LinearLayout) findViewById(R.id.walk_layout);
        mRunLayout = (LinearLayout) findViewById(R.id.run_layout);

        mMotionButton = (Button) findViewById(R.id.motion_button);
        mMotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdtActivity.status = 2;
                IdtActivity.motionMessageSend(4);

                initWalkingCnt = 0;
                initRunningCnt = 0;
                walkingCnt = 0;
                runningCnt = 0;
                initState = false;


                Intent intent = new Intent(getApplicationContext(), FitnessActivity.class);
                startActivity(intent);
                IdtActivity.motionMessageSend(4);
                finish();
            }
        });
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
                initState = true;
                mWalkingText.setText("0");
                mRunningText.setText("0");
                mCurrentMotionText.setText("");
                mWalkLayout.setBackgroundResource(R.drawable.walking);
                mRunLayout.setBackgroundResource(R.drawable.running);

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

    public static void setText(int walkCnt, int runCnt, int motion) {
        try {
//            walkingCnt = walkingCnt + walkCnt;
//            runningCnt = runningCnt + runCnt;

            ULog.d("walkingCnt : " + walkingCnt + "// runningCnt : " + runningCnt + " // walkCnt : " + walkCnt + " // runCnt : " + runCnt + " // initWalkingCnt : " + initWalkingCnt + " // initRunningCnt " + initRunningCnt + "//initState : " +initState);

            if(initState) {
                walkingCnt = initWalkingCnt;
                runningCnt = initRunningCnt;
                initState = false;
            }

            if (walkCnt != 0) {
                mWalkLayout.setBackgroundResource(R.drawable.walking_counting);
                mRunLayout.setBackgroundResource(R.drawable.running);
            } else if (runCnt != 0) {
                mWalkLayout.setBackgroundResource(R.drawable.walking);
                mRunLayout.setBackgroundResource(R.drawable.running_counting);
            }

            mCurrentMotionText.setText(motion+"");

            int walk = walkCnt-walkingCnt;
            int run = runCnt-runningCnt;

            if(walk > 0) {
                initWalkingCnt = walkCnt;

                mWalkingText.setText(walk+"");
            }

            if(run > 0) {
                initRunningCnt = runCnt;

                mRunningText.setText(run+"");
            }


//            if(walkingCnt > 0) {
//                mWalkingText.setText(walkingCnt + "");
//            }
//            if(runningCnt > 0) {
//                mRunningText.setText(runningCnt + "");
//            }
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
