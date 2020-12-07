package se.com.band.option;

import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import se.com.band.R;

public class BatteryActivity extends AppCompatActivity {

    ClipDrawable mImageDrawable;

    TextView mBatteryText;

    // a field in your class
    int mLevel = 0;
    int fromLevel = 0;
    int toLevel = 0;

    int MAX_LEVEL = 10000;
    int LEVEL_DIFF = 100;
    int DELAY = 1;

    Handler mUpHandler = new Handler();
    Runnable animateUpImage = new Runnable() {

        @Override
        public void run() {
            doTheUpAnimation(fromLevel, toLevel);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mBatteryText = (TextView) findViewById(R.id.battery_percent_text);

        ImageView img = (ImageView) findViewById(R.id.battery_full_img);
        //img.setScaleX(-1.0f);
        mImageDrawable = (ClipDrawable) img.getDrawable();
        mImageDrawable.setLevel(0);

        setBat("100");
    }

    private void doTheUpAnimation(int fromLevel, int toLevel) {
        mLevel += LEVEL_DIFF;
        mImageDrawable.setLevel(mLevel);

        if (mLevel <= toLevel) {
            mUpHandler.postDelayed(animateUpImage, DELAY);
        } else {
            mUpHandler.removeCallbacks(animateUpImage);
            fromLevel = toLevel;
        }
    }

    private void setBat(String s) {
        mBatteryText.setText(s + "%");

        int diff = 0;
        int battInt = Integer.parseInt(s);
        if(battInt == 100) {
            diff = 0;
        }else if (battInt > 70) {
            diff = 15;
        } else if (battInt > 50) {
            diff = 5;
        } else if (battInt > 30) {
            diff = -5;
        }else{
            diff = -15;
        }
        int temp_level = ((battInt - diff) * 100);

        mLevel = 0;
        toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;

        mUpHandler.post(animateUpImage);

    }

}
