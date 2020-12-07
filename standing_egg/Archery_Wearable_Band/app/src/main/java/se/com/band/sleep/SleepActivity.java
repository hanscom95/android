package se.com.band.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import se.com.band.R;
import se.com.band.activity.WalkRunActivity;
import se.com.band.motion.MotionActivity;
import se.com.band.utility.GaugeView;

public class SleepActivity extends AppCompatActivity {

    GaugeView mProgress;
    Button mActivityButton, mMotionButton, mMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mProgress = (GaugeView)findViewById(R.id.progress);
        mProgress.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mMenuButton = (Button) findViewById(R.id.menu_button);
        mMotionButton = (Button) findViewById(R.id.motion_button);
        mActivityButton = (Button) findViewById(R.id.activity_button);

        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        mActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WalkRunActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
                Intent intent = new Intent(getApplicationContext(), SleepChartActivity.class);
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
}
