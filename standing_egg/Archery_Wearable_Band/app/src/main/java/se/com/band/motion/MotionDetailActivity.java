package se.com.band.motion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

import se.com.band.MainMenuActivity;
import se.com.band.R;
import se.com.band.activity.WalkRunActivity;
import se.com.band.sleep.SleepActivity;
import se.com.band.utility.GaugeView;

public class MotionDetailActivity extends AppCompatActivity {

    Context mContext;
    Bundle bundle;

    GaugeView mProgress;
    Button mMenuButton, mMotionButton, mSleepButton, mMotionMainButton;

    Button arrowLeftButton, arrowRightButton;
    ViewFlipper tabLeftLayout, tabCenterLayout, tabRightLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Intent intent = getIntent();
        bundle = intent.getExtras();

        setContentView(R.layout.activity_motion_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mProgress = (GaugeView)findViewById(R.id.progress);
        mProgress.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mMotionButton = (Button) findViewById(R.id.activity_button);
        mSleepButton = (Button) findViewById(R.id.sleep_button);
        mMotionMainButton = (Button) findViewById(R.id.motion_main_button);

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

        mMotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WalkRunActivity.class);
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

        mMotionMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        arrowLeftButton = (Button) findViewById(R.id.tab_arrow_left);
        arrowRightButton = (Button) findViewById(R.id.tab_arrwo_right);
        tabLeftLayout = (ViewFlipper) findViewById(R.id.tab_left_layout);
        tabCenterLayout = (ViewFlipper) findViewById(R.id.tab_center_layout);
        tabRightLayout = (ViewFlipper) findViewById(R.id.tab_right_layout);

        arrowLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabRightAnimation();
            }
        });

        arrowRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLeftAnimation();
            }
        });

        tabLeftLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabRightAnimation();
            }
        });

        tabRightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabLeftAnimation();
            }
        });

        if(bundle == null) {
            initTabLayout(0);
        }else {
            initTabLayout(bundle.getInt("motion"));
        }
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


    private void tabLeftAnimation() {
        tabLeftLayout.setInAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.tab_left_in));
        tabLeftLayout.setOutAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.tab_left_out));
        tabCenterLayout.setInAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.tab_left_in));
        tabCenterLayout.setOutAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.tab_left_out));
        tabRightLayout.setInAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.tab_left_in));
        tabRightLayout.setOutAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.tab_left_out));



        tabLeftLayout.showNext();
        tabCenterLayout.showNext();
        tabRightLayout.showNext();
    }


    private void tabRightAnimation() {
        tabLeftLayout.setInAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.tab_right_in));
        tabLeftLayout.setOutAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.tab_right_out));
        tabCenterLayout.setInAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.tab_right_in));
        tabCenterLayout.setOutAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.tab_right_out));
        tabRightLayout.setInAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.tab_right_in));
        tabRightLayout.setOutAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.tab_right_out));


        tabLeftLayout.showPrevious();
        tabCenterLayout.showPrevious();
        tabRightLayout.showPrevious();
    }



    private void initTabLayout(int motion) {
        if(motion == 1) {
            tabLeftLayout.setDisplayedChild(10);
        }else {
            tabLeftLayout.setDisplayedChild(motion-2);
        }
        tabCenterLayout.setDisplayedChild(motion-1);
        if(motion == 11) {
            tabRightLayout.setDisplayedChild(0);
        }else {
            tabRightLayout.setDisplayedChild(motion);
        }
    }
}
