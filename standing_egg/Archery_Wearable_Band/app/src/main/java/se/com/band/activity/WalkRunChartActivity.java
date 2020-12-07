package se.com.band.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import se.com.band.MainMenuActivity;
import se.com.band.R;
import se.com.band.utility.Constants;
import se.com.band.utility.DBManager;
import se.com.band.utility.Preferences;
import se.com.band.utility.ULog;

public class WalkRunChartActivity extends AppCompatActivity {

    Context mContext;
    Preferences mPreference;
    DBManager mDBManager;
    Constants mConstant;

    WalkRunChartTabPagerAdapter mPagerAdapter;
    TabLayout tabLayout;
    ViewPager viewPager;

    Button mMenuButton;
    TextView mActStepText, mActDistanceText, mActKcalText, mActTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mContext = this;
        mPreference = Preferences.getInstance(mContext);
        mDBManager = new DBManager(getApplicationContext(), "apex.db", null, 1);


        mMenuButton = (Button) findViewById(R.id.menu_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.chart_pager);

        // Creating TabPagerAdapter adapter
        mPagerAdapter = new WalkRunChartTabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), mContext);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                ULog.d("onTabSelected");
                if(tab.getPosition() == 0) {
                    mActStepText.setText(mPreference.getDailyTotalStep() + "");
                    mActDistanceText.setText(mPreference.getmDailyTotalDistance() + "");
                    mActKcalText.setText(mPreference.getDailyTotalKcal() + "");
                    mActTimeText.setText(mPreference.getDailyTotalTime());
                }else if(tab.getPosition() == 1) {
                    mActStepText.setText(mPreference.getWeeklyTotalStep() + "");
                    mActDistanceText.setText(mPreference.getmWeeklyTotalDistance() + "");
                    mActKcalText.setText(mPreference.getWeeklyTotalKcal() + "");
                    mActTimeText.setText(mPreference.getWeeklyTotalTime());
                }else if(tab.getPosition() == 2) {
                    mActStepText.setText(mPreference.getMonthlyTotalStep() + "");
                    mActDistanceText.setText(mPreference.getmMonthlyTotalDistance() + "");
                    mActKcalText.setText(mPreference.getMonthlyTotalKcal() + "");
                    mActTimeText.setText(mPreference.getMonthlyTotalTime());
                }else if(tab.getPosition() == 3) {
                    mActStepText.setText(mPreference.getYearlyTotalStep() + "");
                    mActDistanceText.setText(mPreference.getmYearlyTotalDistance() + "");
                    mActKcalText.setText(mPreference.getYearlyTotalKcal() + "");
                    mActTimeText.setText(mPreference.getYearlyTotalTime());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mActStepText = (TextView) findViewById(R.id.activity_steps);
        mActDistanceText = (TextView) findViewById(R.id.activity_distance);
        mActKcalText = (TextView) findViewById(R.id.activity_cal);
        mActTimeText = (TextView) findViewById(R.id.activity_time);

        initDBSetting();
    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activit_chart_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.today:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDBSetting() {/*
        Cursor cursor = mDBManager.selectDailyActivity();
        while (cursor.moveToNext()) {
//            ULog.d("type:"+cursor.getString(0)+"///walk : " + cursor.getString(1) + "//distance:"+ cursor.getString(2) + "//kcal:"+ cursor.getString(3) + "//time:"+ cursor.getString(4));
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
    */}
}

