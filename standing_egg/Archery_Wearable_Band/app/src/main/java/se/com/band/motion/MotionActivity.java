package se.com.band.motion;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gelitenight.waveview.library.WaveView;

import se.com.band.R;
import se.com.band.utility.Constants;
import se.com.band.utility.DBManager;
import se.com.band.utility.WaveHelper;

public class MotionActivity extends AppCompatActivity implements View.OnClickListener{
    DBManager mDBManager;
    Constants mConstant;

    Button mMenuButton;

    WaveHelper mWaveHelper;
    WaveView waveView;

    RelativeLayout mPushUpLayout, mSitUpLayout, mButterflyLayout, mBicepsCurlLayout, mShoulderLayout, mBenchLayout, mSquatLayout, mKettlebellLayout, mCableLayout, mDumbellLayout, mAlternateLayout;
    TextView mPushUpText, mSitUpText, mButterflyText, mBicepsCurlText, mShoulderText, mBenchText, mSquatText, mKettlebellText, mCableText, mDumbellText, mAlternateText, mTotalText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDBManager = new DBManager(this, "apex.db", null, 1);

        mMenuButton = (Button) findViewById(R.id.menu_button);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        waveView = (WaveView) findViewById(R.id.total_wave);
        waveView.setWaterLevelRatio(0.8f);
        mWaveHelper = new WaveHelper(waveView);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        waveView.setWaveColor(
                Color.parseColor("#ACC900"),
                Color.parseColor("#B2ED11"));
        mWaveHelper.start();

        mPushUpLayout = (RelativeLayout) findViewById(R.id.push_up_layout);
        mPushUpLayout.setOnClickListener(this);
        mSitUpLayout = (RelativeLayout) findViewById(R.id.sit_up_layout);
        mSitUpLayout.setOnClickListener(this);
        mButterflyLayout = (RelativeLayout) findViewById(R.id.butterfly_layout);
        mButterflyLayout.setOnClickListener(this);
        mBicepsCurlLayout = (RelativeLayout) findViewById(R.id.biceps_curl_layout);
        mBicepsCurlLayout.setOnClickListener(this);
        mShoulderLayout = (RelativeLayout) findViewById(R.id.shoulder_press_layout);
        mShoulderLayout.setOnClickListener(this);
        mBenchLayout = (RelativeLayout) findViewById(R.id.bench_kickback_layout);
        mBenchLayout.setOnClickListener(this);
        mSquatLayout = (RelativeLayout) findViewById(R.id.squat_layout);
        mSquatLayout.setOnClickListener(this);
        mKettlebellLayout = (RelativeLayout) findViewById(R.id.kettlebell_layout);
        mKettlebellLayout.setOnClickListener(this);
        mCableLayout = (RelativeLayout) findViewById(R.id.seated_cable_layout);
        mCableLayout.setOnClickListener(this);
        mDumbellLayout = (RelativeLayout) findViewById(R.id.seated_dumbell_layout);
        mDumbellLayout.setOnClickListener(this);
        mAlternateLayout = (RelativeLayout) findViewById(R.id.alternate_layout);
        mAlternateLayout.setOnClickListener(this);

        mPushUpText = (TextView) findViewById(R.id.push_up);
        mSitUpText = (TextView) findViewById(R.id.sit_up);
        mButterflyText = (TextView) findViewById(R.id.butterfly);
        mBicepsCurlText = (TextView) findViewById(R.id.biceps_curl);
        mShoulderText = (TextView) findViewById(R.id.shoulder_press);
        mBenchText = (TextView) findViewById(R.id.bench_kickback);
        mSquatText = (TextView) findViewById(R.id.squat);
        mKettlebellText = (TextView) findViewById(R.id.kettlebell);
        mCableText = (TextView) findViewById(R.id.seated_cable);
        mDumbellText = (TextView) findViewById(R.id.seated_dumbell);
        mAlternateText = (TextView) findViewById(R.id.alternate);
        mTotalText = (TextView) findViewById(R.id.total);

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
                Intent intent = new Intent(getApplicationContext(), MotionChartActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    public void onClick(View v) {
        int motion = 0;
        switch (v.getId()) {
            case R.id.push_up_layout:
                motion = 1;
                break;
            case R.id.sit_up_layout:
                motion = 2;
                break;
            case R.id.butterfly_layout:
                motion = 3;
                break;
            case R.id.biceps_curl_layout:
                motion = 4;
                break;
            case R.id.shoulder_press_layout:
                motion = 5;
                break;
            case R.id.bench_kickback_layout:
                motion = 6;
                break;
            case R.id.squat_layout:
                motion = 7;
                break;
            case R.id.kettlebell_layout:
                motion = 8;
                break;
            case R.id.seated_cable_layout:
                motion = 9;
                break;
            case R.id.seated_dumbell_layout:
                motion = 10;
                break;
            case R.id.alternate_layout:
                motion = 11;
                break;
        }

        Intent intent = new Intent(getApplicationContext(), MotionDetailActivity.class);
        intent.putExtra("motion", motion);
        startActivity(intent);
        finish();

    }

    private void setData() {
        Cursor cursor = mDBManager.selectDailyFitness();
        int pushCnt = 0, sitCnt = 0, butterflyCnt = 0, bicepsCnt = 0, shoulderCnt = 0, kettlebellCnt = 0, seatedCnt = 0, dumbellCnt = 0, benchCnt = 0, squatCnt = 0, alternateCnt = 0;
        int pushKcal = 0, sitKcal = 0, butterflyKcal = 0, bicepsKcal = 0, shoulderKcal = 0, kettlebellKcal = 0, seatedKcal = 0, dumbellKcal = 0, benchKcal = 0, squatKcal = 0, alternateKcal = 0;
        int pushDuration = 0, sitDuration = 0, butterflyDuration = 0, bicepsDuration = 0, shoulderDuration = 0, kettlebellDuration = 0, seatedDuration = 0, dumbellDuration = 0, benchDuration = 0, squatDuration = 0, alternateDuration = 0;

        while (cursor.moveToNext()) {

            switch (cursor.getInt(0)) {
                case Constants.MOTION_PUSH_UP:
                    pushCnt += pushCnt;
                    pushKcal += pushKcal;
                    pushDuration += pushDuration;
                    break;
                case Constants.MOTION_SIT_UP:
                    sitCnt += sitCnt;
                    sitKcal += sitKcal;
                    sitDuration += sitDuration;
                    break;
                case Constants.MOTION_BUTTERFLY:
                    butterflyCnt += butterflyCnt;
                    butterflyKcal += butterflyKcal;
                    butterflyDuration += butterflyDuration;
                    break;
                case Constants.MOTION_BICEPS_CURL:
                    bicepsCnt += bicepsCnt;
                    bicepsKcal += bicepsKcal;
                    bicepsDuration += bicepsDuration;
                    break;
                case Constants.MOTION_SHOULDER_PRESS:
                    shoulderCnt += shoulderCnt;
                    shoulderKcal += shoulderKcal;
                    shoulderDuration += shoulderDuration;
                    break;
                case Constants.MOTION_BENCH_KICKBACK:
                    benchCnt += benchCnt;
                    benchKcal += benchKcal;
                    benchDuration += benchDuration;
                    break;
                case Constants.MOTION_SQUAT:
                    squatCnt += squatCnt;
                    squatKcal += squatKcal;
                    squatDuration += squatDuration;
                    break;
                case Constants.MOTION_KETTLEBELL_SWING:
                    kettlebellCnt += kettlebellCnt;
                    kettlebellKcal += kettlebellKcal;
                    kettlebellDuration += kettlebellDuration;
                    break;
                case Constants.MOTION_SEATED_CABLE_ROW:
                    seatedCnt += seatedCnt;
                    seatedKcal += seatedKcal;
                    seatedDuration += seatedDuration;
                    break;
                case Constants.MOTION_DUMBELL_TRICEPS_PRESS:
                    dumbellCnt += dumbellCnt;
                    dumbellKcal += dumbellKcal;
                    dumbellDuration += dumbellDuration;
                    break;
                case Constants.MOTION_ALTERNATE_DELTOID_RAISE:
                    alternateCnt += alternateCnt;
                    alternateKcal += alternateKcal;
                    alternateDuration += alternateDuration;
                    break;
            }

        }

        int totalScore = pushCnt + sitCnt + butterflyCnt + bicepsCnt + shoulderCnt + kettlebellCnt + seatedCnt + dumbellCnt + benchCnt + squatCnt + alternateCnt;


        mPushUpText.setText(pushCnt+"");
        mSitUpText.setText(sitCnt+"");
        mButterflyText.setText(butterflyCnt+"");
        mBicepsCurlText.setText(bicepsCnt+"");
        mShoulderText.setText(shoulderCnt+"");
        mBenchText.setText(benchCnt+"");
        mSquatText.setText(squatCnt+"");
        mKettlebellText.setText(kettlebellCnt+"");
        mCableText.setText(seatedCnt+"");
        mDumbellText.setText(dumbellCnt+"");
        mAlternateText.setText(alternateCnt+"");
        mTotalText.setText(totalScore+"");
    }
}
