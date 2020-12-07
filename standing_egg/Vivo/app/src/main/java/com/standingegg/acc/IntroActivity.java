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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class IntroActivity extends Activity {

    LinearLayout IntroLayout, goalLayout;
    Button mStartButton, mGoalButton;
    EditText goalEditText, heightEditText, weightEditText;

    private Animation animUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);

        mStartButton = (Button) findViewById(R.id.start_button);
        IntroLayout = (LinearLayout) findViewById(R.id.intro_layout);
        goalLayout = (LinearLayout) findViewById(R.id.goal_layout);
        goalEditText = (EditText) findViewById(R.id.goal_edit_text);
        heightEditText = (EditText) findViewById(R.id.height_edit_text);
        weightEditText = (EditText) findViewById(R.id.weight_edit_text);

        goalEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0){
                    mGoalButton.setBackgroundResource(R.drawable.goal_button_after_normal);
                }else {
                    mGoalButton.setBackgroundResource(R.drawable.goal_button_before_press);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mGoalButton = (Button) findViewById(R.id.goal_button);
        mGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(goalEditText.getText().length() > 0  && heightEditText.getText().length() > 0 && weightEditText.getText().length() > 0) {
                if(goalEditText.getText().length() > 0){
                    Intent intent = new Intent(getApplicationContext(), StepActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("goal", Integer.parseInt(goalEditText.getText().toString()));
//                    bundle.putDouble("height", Double.parseDouble(heightEditText.getText().toString()));
//                    bundle.putDouble("weight", Double.parseDouble(weightEditText.getText().toString()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Please enter an input field.", Toast.LENGTH_LONG).show();
                }
            }
        });

        animUp = AnimationUtils.loadAnimation(this, R.anim.anim_up);

        IntroLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntroLayout.startAnimation(animUp);
            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntroLayout.startAnimation(animUp);
            }
        });

        animUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                Intent intent = new Intent(getApplicationContext(), GoalActivity.class);
//                startActivity(intent);
//                finish();
                IntroLayout.setVisibility(View.GONE);
                goalLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    @Override
    public void onPause () {
        super.onPause();
//        stopSensor();
    }

    @Override
    public boolean isDestroyed() {
        return super.isDestroyed();
    }

}
