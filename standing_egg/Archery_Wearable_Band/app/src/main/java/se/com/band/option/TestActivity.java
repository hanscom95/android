package se.com.band.option;

import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import se.com.band.MainMenuActivity;
import se.com.band.R;

public class TestActivity extends AppCompatActivity {

    Button mActivityButton, mMotionButton, mSleepButton, mHeartRateButton;
    static TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mActivityButton = (Button) findViewById(R.id.test_activity_button);
        mMotionButton = (Button) findViewById(R.id.test_motion_button);
        mSleepButton = (Button) findViewById(R.id.test_sleep_button);
        mHeartRateButton = (Button) findViewById(R.id.test_heartrate_button);

        mTextView = (TextView) findViewById(R.id.test_log_text);
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        mActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.motionMessageSend(3);
            }
        });

        mMotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.motionMessageSend(4);
            }
        });

        mSleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.motionMessageSend(3);
            }
        });

        mHeartRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.heartRateMessageSend();
            }
        });
    }

    public static void setSleepText(int status, int sleep, String date) {
        String logData = mTextView.getText().toString();

        if(status == 0) {
            logData += "status : sleep   /   duration : " + sleep  + "   /   ";
        }else if(status == 1) {
            logData += "status : wake up   /   duration : " + sleep  + "   /   ";
        }else if(status == 2) {
            logData += "status : taken off   /   duration : " + sleep  + "   /   ";
        }else if(status == 3) {
            logData += "status : ready   /   duration : " + sleep  + "   /   ";
        }

        logData += date;
        logData += "\r\n";

        mTextView.setText(logData);
    }

    public static void setActivtyText(int motion, int count, int distanceMeters, int calories) {
        String logData = mTextView.getText().toString();

        logData += "status : " + motion + "   /   count : " + count  + "   /   distance : " + distanceMeters + "   /   calories : " + calories;

        logData += "\r\n";

        mTextView.setText(logData);
    }

    public static void setMotionText(int motion, int count, int calories) {
        String logData = mTextView.getText().toString();

        logData += "status : " + motion + "   /   count : " + count + "   /   calories : " + calories;

        logData += "\r\n";

        mTextView.setText(logData);
    }

    public static void setHeartRateText(int heartRateText) {
        String logData = mTextView.getText().toString();

        logData += "heart Rate : " + heartRateText;

        logData += "\r\n";

        mTextView.setText(logData);
    }

    public static void setCommandText(int command, int motion) {
        String logData = mTextView.getText().toString();

        logData += "command : " + command +  "   /   motion : " + motion;

        logData += "\r\n";

        mTextView.setText(logData);
    }
}
