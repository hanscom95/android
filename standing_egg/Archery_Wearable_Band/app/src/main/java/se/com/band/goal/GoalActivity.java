package se.com.band.goal;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import se.com.band.R;
import se.com.band.utility.DBManager;
import se.com.band.utility.NetworkConnection;
import se.com.band.utility.Preferences;

public class GoalActivity extends AppCompatActivity {

    Context mContext;
    Preferences mPreference;
    DBManager mDbManager;
    GoalTabPagerAdapter mPagerAdapter;
    Bundle mBundle;

    GoalTask mAuthTask = null;

    TabLayout mTabLayout;
    ViewPager mViewPager;

    Button mContinueButton;

    boolean mDbFlag = false;
    int mMotionFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mContext = this;
        mPreference = Preferences.getInstance(mContext);
        mDbManager = new DBManager(getApplicationContext(), "apex.db", null, 1);

        Intent intent = getIntent();
        mBundle = intent.getExtras();

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.number_picker_pager);

        mPagerAdapter = new GoalTabPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mContinueButton = (Button) findViewById(R.id.continue_button);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int saveFlag = 0;
                int postion = mTabLayout.getSelectedTabPosition();
                int value = mPagerAdapter.getValue(postion);
                ArrayList<Object> array = new ArrayList<Object>();
                array.add(mMotionFlag);
                if (postion == 0) {
                    array.add(value);
                    array.add(0);
                } else {
                    array.add(0);
                    array.add(value);
                }
                array.add(mDbManager.selectId());

                if(!mDbFlag) {
                    saveFlag = 1;
                    mDbManager.insertGoal(array);
                }else {
                    saveFlag = 2;
                    mDbManager.updateGoal(array);
                }

                mAuthTask = new GoalTask(array, saveFlag);
                mAuthTask.execute((Void) null);
            }
        });

        initGoalSet();
    }

    private void initGoalSet() {
        if(mBundle != null) {
            try {
                int flag = mBundle.getInt("flag");
                mMotionFlag = flag;
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        String step = "0";
        String kcal = "0";
        Cursor goal = mDbManager.selectGoal();
        if(mMotionFlag != 0) {
            while (goal.moveToNext()) {
                if (mMotionFlag == goal.getInt(1)) {
                    step = goal.getString(2);
                    kcal = goal.getString(3);
                    mDbFlag = true;
                }
            }

            if(mDbFlag) {
                if (Integer.parseInt(step) > 0) {
                    mViewPager.setCurrentItem(0);
                    mPagerAdapter.setValue(step);
                } else {
                    mViewPager.setCurrentItem(1);
                    mPagerAdapter.setValue(kcal);
                }
            }
        }
    }

    private class GoalTask extends AsyncTask<Void, Void, Boolean> {

        NetworkConnection network;

        ArrayList<Object> mArray = new ArrayList<Object>();
        int mFlag;

        GoalTask(ArrayList<Object> array, int flag) {
            mArray = array;
            mFlag = flag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean connect = false;
            try {
                // Simulate network access.
                network = new NetworkConnection(mContext);

                if(mFlag == 1) {
                    connect = network.goalCreate(mArray);
                }else if(mFlag == 2) {
                    connect = network.goalUpdate(mArray);
                }
            } catch (Exception e) {
                return false;
            }


            if(!connect) {
                return false;
            }else {
                // TODO: register the new account here.
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            finish();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

}
