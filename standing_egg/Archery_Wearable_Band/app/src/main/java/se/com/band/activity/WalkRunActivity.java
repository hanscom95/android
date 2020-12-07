package se.com.band.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import se.com.band.R;
import se.com.band.motion.MotionActivity;
import se.com.band.sleep.SleepActivity;
import se.com.band.utility.Constants;
import se.com.band.utility.DBManager;
import se.com.band.utility.GaugeView;
import se.com.band.utility.ULog;

public class WalkRunActivity extends AppCompatActivity {

    Context mContext;
    DBManager mDBManager;
    Constants mConstant;

    GaugeView mProgress;
    Button mMotionButton, mSleepButton, mActivityButton, mMenuButton;
    TextView mInfoTitleText, mActivityStepsText, mActivityCalText, mActivityDistanceText, mActivityTimeText;
    TextView mGaugePercentText, mGaugeStepText, mGaugekcalText;

    int activityStatus = 0;

    int w_goal = 4000;
    int r_goal = 4000;
    int walk = 0;
    int run = 0;
    int w_distance = 0;
    int w_duration = 0;
    int w_kcal = 0;
    int r_kcal = 0;
    int r_distance = 0;
    int r_duration = 0;

    int w_hr = 0;
    int w_min = 0;
    int r_hr = 0;
    int r_min = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mContext = this;
        mDBManager = new DBManager(getApplicationContext(), "apex.db", null, 1);


        mProgress = (GaugeView)findViewById(R.id.progress);
        mProgress.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mProgress.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                mLevel = 0;
                toLevel = (Integer.parseInt(mGaugePercentText.getText().toString())*2);

                mUpHandler.post(animateUpImage);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
            }
        });

        mMenuButton = (Button) findViewById(R.id.menu_button);
        mMotionButton = (Button) findViewById(R.id.motion_button);
        mSleepButton = (Button) findViewById(R.id.sleep_button);
        mActivityButton = (Button) findViewById(R.id.activity_main_button);

        mInfoTitleText = (TextView) findViewById(R.id.info_title);
        mActivityStepsText = (TextView) findViewById(R.id.activity_steps);
        mActivityDistanceText = (TextView) findViewById(R.id.activity_distance);
        mActivityCalText = (TextView) findViewById(R.id.activity_cal);
        mActivityTimeText = (TextView) findViewById(R.id.activity_time);
        mGaugeStepText = (TextView) findViewById(R.id.gauge_step);
        mGaugePercentText = (TextView) findViewById(R.id.gauge_percent);
        mGaugekcalText = (TextView) findViewById(R.id.gauge_cal);

        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityStatus == 0) {
                    activityStatus = 1;
                    mActivityButton.setBackground(getDrawable(R.drawable.toggle_running_button));
                    mInfoTitleText.setText("RUNNING");


                    mGaugeStepText.setText(run+"");
                    mGaugePercentText.setText(((run*100)/r_goal) + "");
                    mGaugekcalText.setText(r_kcal+"");

                    mActivityStepsText.setText(run+"");
                    mActivityDistanceText.setText(r_distance+"");
                    mActivityCalText.setText(r_kcal+"");

                    String time = (r_hr < 10 ? "0" + r_hr : r_hr) + ":" +
                            (r_min < 10 ? "0" + r_min : r_min);
                    mActivityTimeText.setText(time);
                }else if(activityStatus == 1) {
                    activityStatus = 0;
                    mActivityButton.setBackground(getDrawable(R.drawable.toggle_walking_button));
                    mInfoTitleText.setText("WALKING");


                    mGaugeStepText.setText(walk+"");
                    mGaugePercentText.setText(((walk*100)/w_goal) + "");
                    mGaugekcalText.setText(w_kcal+"");

                    mActivityStepsText.setText(walk+"");
                    mActivityDistanceText.setText(w_distance+"");
                    mActivityCalText.setText(w_kcal+"");

                    String time = (w_hr < 10 ? "0" + w_hr : w_hr) + ":" +
                            (w_min < 10 ? "0" + w_min : w_min);
                    mActivityTimeText.setText(time);
                }
            }
        });

        mMotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MotionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mSleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SleepActivity.class);
                startActivity(intent);
                finish();
            }
        });

        setData();
    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chart:
                Intent intent = new Intent(getApplicationContext(), WalkRunChartActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static int mLevel = 0;
    private static int fromLevel = 0;
    private static int toLevel = 0;

    public static final int MAX_LEVEL = 200;
    public static final int LEVEL_DIFF = 10;
    public static final int DELAY = 30;

    private Handler mUpHandler = new Handler();
    private Runnable animateUpImage = new Runnable() {

        @Override
        public void run() {
            doTheUpAnimation(fromLevel, toLevel);
        }
    };

    private void doTheUpAnimation(int fromLevel, int toLevel) {
        mLevel += LEVEL_DIFF;
        mProgress.setClipping(mLevel);

        if (mLevel <= toLevel) {
            mUpHandler.postDelayed(animateUpImage, DELAY);
        } else {
            mUpHandler.removeCallbacks(animateUpImage);
            fromLevel = toLevel;
        }
    }

    private void setData() {
        Cursor cursor = mDBManager.selectDailyActivity();
        while (cursor.moveToNext()) {
            ULog.d("type:"+cursor.getString(0)+"///walk : " + cursor.getString(1) + "//distance:"+ cursor.getString(2) + "//kcal:"+ cursor.getString(3) + "//time:"+ cursor.getString(4) + "//duration:"+ cursor.getString(6));
            if(Integer.parseInt(cursor.getString(0)) == mConstant.MOTION_WALKING) {
                walk += Integer.parseInt(cursor.getString(1));
                w_distance += Integer.parseInt(cursor.getString(2));
                w_kcal += Integer.parseInt(cursor.getString(3));
                w_duration += Integer.parseInt(cursor.getString(4));
            }else if(Integer.parseInt(cursor.getString(0)) == mConstant.MOTION_RUNNING) {
                run += Integer.parseInt(cursor.getString(1));
                r_distance += Integer.parseInt(cursor.getString(2));
                r_kcal += Integer.parseInt(cursor.getString(3));
                r_duration += Integer.parseInt(cursor.getString(4));
            }
        }

        if(activityStatus == 0) {
            mGaugeStepText.setText(walk+"");
            mGaugePercentText.setText(((walk*100)/w_goal) + "");
            mGaugekcalText.setText(w_kcal+"");

            mActivityStepsText.setText(walk+"");
            mActivityDistanceText.setText(w_distance+"");
            mActivityCalText.setText(w_kcal+"");


            w_hr = w_duration/3600;
            w_min = ((w_duration%3600)/60);
            String time = (w_hr < 10 ? "0" + w_hr : w_hr) + ":" +
                    (w_min < 10 ? "0" + w_min : w_min);
            mActivityTimeText.setText(time);
        }else if(activityStatus == 1) {
            mGaugeStepText.setText(run+"");
            mGaugePercentText.setText(((run*100)/r_goal) + "");
            mGaugekcalText.setText(r_kcal+"");

            mActivityStepsText.setText(run+"");
            mActivityDistanceText.setText(r_distance+"");
            mActivityCalText.setText(r_kcal+"");

            r_hr = r_duration/3600;
            r_min = ((r_duration%3600)/60);
            String time = (r_hr < 10 ? "0" + r_hr : r_hr) + ":" +
                    (r_min < 10 ? "0" + r_min : r_min);
            mActivityTimeText.setText(time);
        }
    }
}
