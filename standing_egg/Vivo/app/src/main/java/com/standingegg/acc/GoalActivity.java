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
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class GoalActivity extends Activity {

    EditText goalEditText, heightEditText, weightEditText;
    Button goalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_activity);

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
                    goalButton.setBackgroundResource(R.drawable.goal_button_after_normal);
                }else {
                    goalButton.setBackgroundResource(R.drawable.goal_button_before_press);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        goalButton = (Button) findViewById(R.id.goal_button);
        goalButton.setOnClickListener(new View.OnClickListener() {
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
                }else {
                    Toast.makeText(getApplicationContext(), "Please enter an input field.", Toast.LENGTH_LONG).show();
                }
            }
        });

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
}
